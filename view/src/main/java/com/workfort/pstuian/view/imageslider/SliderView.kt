package com.workfort.pstuian.view.imageslider

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.animation.Interpolator
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.PagerAdapter
import com.workfort.pstuian.view.R
import com.workfort.pstuian.view.imageslider.indicatorview.PageIndicatorView
import com.workfort.pstuian.view.imageslider.indicatorview.animation.type.BaseAnimation
import com.workfort.pstuian.view.imageslider.indicatorview.animation.type.ColorAnimation
import com.workfort.pstuian.view.imageslider.indicatorview.animation.type.IndicatorAnimationType
import com.workfort.pstuian.view.imageslider.indicatorview.draw.controller.AttributeController.Companion.getRtlMode
import com.workfort.pstuian.view.imageslider.indicatorview.draw.controller.DrawController
import com.workfort.pstuian.view.imageslider.indicatorview.draw.data.Orientation
import com.workfort.pstuian.view.imageslider.indicatorview.draw.data.RtlMode
import com.workfort.pstuian.view.imageslider.indicatorview.utils.DensityUtils.dpToPx
import com.workfort.pstuian.view.imageslider.infiniteadapter.InfinitePagerAdapter
import com.workfort.pstuian.view.imageslider.transformations.AntiClockSpinTransformation
import com.workfort.pstuian.view.imageslider.transformations.ClockSpinTransformation
import com.workfort.pstuian.view.imageslider.transformations.CubeInDepthTransformation
import com.workfort.pstuian.view.imageslider.transformations.CubeInRotationTransformation
import com.workfort.pstuian.view.imageslider.transformations.CubeInScalingTransformation
import com.workfort.pstuian.view.imageslider.transformations.CubeOutDepthTransformation
import com.workfort.pstuian.view.imageslider.transformations.CubeOutRotationTransformation
import com.workfort.pstuian.view.imageslider.transformations.CubeOutScalingTransformation
import com.workfort.pstuian.view.imageslider.transformations.DepthTransformation
import com.workfort.pstuian.view.imageslider.transformations.FadeTransformation
import com.workfort.pstuian.view.imageslider.transformations.FanTransformation
import com.workfort.pstuian.view.imageslider.transformations.FidgetSpinTransformation
import com.workfort.pstuian.view.imageslider.transformations.GateTransformation
import com.workfort.pstuian.view.imageslider.transformations.HingeTransformation
import com.workfort.pstuian.view.imageslider.transformations.HorizontalFlipTransformation
import com.workfort.pstuian.view.imageslider.transformations.PopTransformation
import com.workfort.pstuian.view.imageslider.transformations.SimpleTransformation
import com.workfort.pstuian.view.imageslider.transformations.SpinnerTransformation
import com.workfort.pstuian.view.imageslider.transformations.TossTransformation
import com.workfort.pstuian.view.imageslider.transformations.VerticalFlipTransformation
import com.workfort.pstuian.view.imageslider.transformations.VerticalShutTransformation
import com.workfort.pstuian.view.imageslider.transformations.ZoomOutTransformation
import kotlinx.coroutines.Runnable

/**
 *  ****************************************************************************
 *  * Created by : arhan on 28 Nov, 2021 at 13:34.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class SliderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), Runnable, OnTouchListener,
    SliderViewAdapter.DataSetListener, SliderPager.OnPageChangeListener {
    companion object {
        const val AUTO_CYCLE_DIRECTION_RIGHT = 0
        const val AUTO_CYCLE_DIRECTION_LEFT = 1
        const val AUTO_CYCLE_DIRECTION_BACK_AND_FORTH = 2
    }

    private val mHandler = Handler(Looper.getMainLooper())
    private var mFlagBackAndForth = false
    private var mIsAutoCycle = false
    private var mAutoCycleDirection = 0
    private var mScrollTimeInMillis = 0
    private var mPagerAdapter: SliderViewAdapter<*>? = null
    private var mSliderPager: SliderPager = SliderPager(
        context
    ).apply {
        id = ViewCompat.generateViewId()
        overScrollMode = OVER_SCROLL_IF_CONTENT_SCROLLS
        setOnTouchListener(this@SliderView)
        addOnPageChangeListener(this@SliderView)
    }.also {
        val sliderParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(it, 0, sliderParams)
    }
    private var mPagerIndicator: PageIndicatorView? = null
    private var mInfinitePagerAdapter: InfinitePagerAdapter? = null
    private var mPageListener: OnSliderPageListener? = null
    private var mIsInfiniteAdapter = true
    private var mIsIndicatorEnabled = true
    private var mPreviousPosition = -1

    init {
        setUpAttributes(context, attrs)
    }

    /**
     * This class syncs all attributes from xml tag for this slider.
     *
     * @param context its android main context which is needed.
     * @param attrs   attributes from xml slider tags.
     */
    private fun setUpAttributes(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SliderView,
            0, 0)
        val indicatorEnabled =
            typedArray.getBoolean(R.styleable.SliderView_sliderIndicatorEnabled, true)
        val sliderAnimationDuration = typedArray.getInt(
            R.styleable.SliderView_sliderAnimationDuration,
            SliderPager.DEFAULT_SCROLL_DURATION
        )
        val sliderScrollTimeInSec =
            typedArray.getInt(R.styleable.SliderView_sliderScrollTimeInSec, 2)
        val sliderAutoCycleEnabled =
            typedArray.getBoolean(R.styleable.SliderView_sliderAutoCycleEnabled, true)
        val sliderStartAutoCycle =
            typedArray.getBoolean(R.styleable.SliderView_sliderStartAutoCycle, false)
        val sliderAutoCycleDirection = typedArray.getInt(
            R.styleable.SliderView_sliderAutoCycleDirection,
            AUTO_CYCLE_DIRECTION_RIGHT
        )
        setSliderAnimationDuration(sliderAnimationDuration)
        setScrollTimeInSec(sliderScrollTimeInSec)
        setAutoCycle(sliderAutoCycleEnabled)
        setAutoCycleDirection(sliderAutoCycleDirection)
        setAutoCycle(sliderStartAutoCycle)
        setIndicatorEnabled(indicatorEnabled)

        /*start indicator configs*/
        if (indicatorEnabled) {
            val indicatorOrientation = typedArray.getInt(
                R.styleable.SliderView_sliderIndicatorOrientation,
                Orientation.HORIZONTAL.ordinal
            )
            val orientation: Orientation = if (indicatorOrientation == 0) {
                Orientation.HORIZONTAL
            } else {
                Orientation.VERTICAL
            }
            val indicatorRadius = typedArray.getDimension(
                R.styleable.SliderView_sliderIndicatorRadius,
                dpToPx(2).toFloat()
            ).toInt()
            val indicatorPadding = typedArray.getDimension(
                R.styleable.SliderView_sliderIndicatorPadding,
                dpToPx(3).toFloat()
            ).toInt()
            val indicatorMargin = typedArray.getDimension(
                R.styleable.SliderView_sliderIndicatorMargin,
                    dpToPx(12).toFloat()
            ).toInt()
            val indicatorMarginLeft = typedArray.getDimension(
                R.styleable.SliderView_sliderIndicatorMarginLeft,
                dpToPx(12).toFloat()
            ).toInt()
            val indicatorMarginTop = typedArray.getDimension(
                R.styleable.SliderView_sliderIndicatorMarginTop,
                dpToPx(12).toFloat()
            ).toInt()
            val indicatorMarginRight = typedArray.getDimension(
                R.styleable.SliderView_sliderIndicatorMarginRight,
                dpToPx(12).toFloat()
            ).toInt()
            val indicatorMarginBottom = typedArray.getDimension(
                R.styleable.SliderView_sliderIndicatorMarginBottom,
                dpToPx(12).toFloat()
            ).toInt()
            val indicatorGravity = typedArray.getInt(
                R.styleable.SliderView_sliderIndicatorGravity,
                Gravity.CENTER or Gravity.BOTTOM
            )
            val indicatorUnselectedColor = typedArray.getColor(
                R.styleable.SliderView_sliderIndicatorUnselectedColor,
                Color.parseColor(ColorAnimation.DEFAULT_UNSELECTED_COLOR)
            )
            val indicatorSelectedColor = typedArray.getColor(
                R.styleable.SliderView_sliderIndicatorSelectedColor,
                Color.parseColor(ColorAnimation.DEFAULT_SELECTED_COLOR)
            )
            val indicatorAnimationDuration = typedArray.getInt(
                R.styleable.SliderView_sliderIndicatorAnimationDuration,
                BaseAnimation.DEFAULT_ANIMATION_TIME
            )
            val indicatorRtlMode = typedArray.getInt(
                R.styleable.SliderView_sliderIndicatorRtlMode,
                RtlMode.Off.ordinal
            )
            val rtlMode = getRtlMode(indicatorRtlMode)
            setIndicatorOrientation(orientation)
            setIndicatorRadius(indicatorRadius)
            setIndicatorPadding(indicatorPadding)
            setIndicatorMargin(indicatorMargin)
            setIndicatorMarginCustom(
                indicatorMarginLeft,
                indicatorMarginTop,
                indicatorMarginRight,
                indicatorMarginBottom
            )
            setIndicatorGravity(indicatorGravity)
            setIndicatorMargins(
                indicatorMarginLeft,
                indicatorMarginTop,
                indicatorMarginRight,
                indicatorMarginBottom
            )
            setIndicatorUnselectedColor(indicatorUnselectedColor)
            setIndicatorSelectedColor(indicatorSelectedColor)
            setIndicatorAnimationDuration(indicatorAnimationDuration.toLong())
            setIndicatorRtlMode(rtlMode)
        }
        /*end indicator configs*/
        typedArray.recycle()
    }

    /**
     * This method will be called only if [.mIsIndicatorEnabled] is true.
     * so initializes indicator if its active.
     */
    private fun initIndicator() {
        mPagerIndicator = PageIndicatorView(context).apply {
            setViewPager(mSliderPager)
            setDynamicCount(true)
        }.also {
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
                setMargins(20, 20, 20, 20)
            }
            addView(it, 1, params)
        }
    }

    /**
     * @param listener for indicator dots clicked.
     */
    fun setOnIndicatorClickListener(listener: DrawController.ClickListener?) {
        mPagerIndicator?.setClickListener(listener)
    }

    /**
     * @param listener is a callback of current item in sliderView.
     */
    fun setCurrentPageListener(listener: OnSliderPageListener?) {
        mPageListener = listener
    }

    /**
     * @param pagerAdapter Set a SliderAdapter that will supply views
     * for this slider as needed.
     */
    fun setSliderAdapter(pagerAdapter: SliderViewAdapter<*>) {
        mPagerAdapter = pagerAdapter
        //set slider adapter
        mInfinitePagerAdapter = InfinitePagerAdapter(pagerAdapter)
        //registerAdapterDataObserver();
        mSliderPager.setAdapter(mInfinitePagerAdapter)
        mPagerAdapter?.dataSetChangedListener(this)
        // set slider on correct position whether its infinite or not.
        setCurrentPagePosition(0)
    }

    /**
     * @param pagerAdapter Set a SliderAdapter that will supply views
     * for this slider as needed.
     */
    fun setSliderAdapter(pagerAdapter: SliderViewAdapter<*>, infiniteAdapter: Boolean) {
        mIsInfiniteAdapter = infiniteAdapter
        if (!infiniteAdapter) {
            mPagerAdapter = pagerAdapter
            mSliderPager.mAdapter = pagerAdapter
        } else {
            setSliderAdapter(pagerAdapter)
        }
    }

    fun setInfiniteAdapterEnabled(enabled: Boolean) {
        mPagerAdapter?.let { setSliderAdapter(it, enabled) }
    }

    /**
     * @return Sliders Pager
     */
    fun getSliderPager(): SliderPager = mSliderPager

    /**
     * @return adapter of current slider.
     */
    fun getSliderAdapter(): PagerAdapter? = mPagerAdapter

    /**
     * @return if is slider auto cycling or not?
     */
    fun isAutoCycle(): Boolean = mIsAutoCycle

    fun setAutoCycle(autoCycle: Boolean) {
        mIsAutoCycle = autoCycle
    }

    /**
     * @param limit How many pages will be kept offscreen in an idle state.
     *
     * You should keep this limit low, especially if your pages have complex layouts.
     * * This setting defaults to 1.
     */
    fun setOffscreenPageLimit(limit: Int) {
        mSliderPager.setOffscreenPageLimit(limit)
    }

    /**
     * @return sliding delay in seconds.
     */
    fun getScrollTimeInSec(): Int = mScrollTimeInMillis / 1000

    /**
     * @param time of sliding delay in seconds.
     */
    fun setScrollTimeInSec(time: Int) {
        mScrollTimeInMillis = time * 1000
    }

    fun getScrollTimeInMillis(): Int = mScrollTimeInMillis

    fun setScrollTimeInMillis(millis: Int) {
        mScrollTimeInMillis = millis
    }

    /**
     * @param animation changes pre defined animations for slider.
     */
    fun setSliderTransformAnimation(animation: SliderAnimations?) {
        val transformation = when (animation) {
            SliderAnimations.ANTICLOCKSPINTRANSFORMATION -> AntiClockSpinTransformation()
            SliderAnimations.CLOCK_SPINTRANSFORMATION -> ClockSpinTransformation()
            SliderAnimations.CUBEINDEPTHTRANSFORMATION -> CubeInDepthTransformation()
            SliderAnimations.CUBEINROTATIONTRANSFORMATION -> CubeInRotationTransformation()
            SliderAnimations.CUBEINSCALINGTRANSFORMATION -> CubeInScalingTransformation()
            SliderAnimations.CUBEOUTDEPTHTRANSFORMATION -> CubeOutDepthTransformation()
            SliderAnimations.CUBEOUTROTATIONTRANSFORMATION -> CubeOutRotationTransformation()
            SliderAnimations.CUBEOUTSCALINGTRANSFORMATION -> CubeOutScalingTransformation()
            SliderAnimations.DEPTHTRANSFORMATION -> DepthTransformation()
            SliderAnimations.FADETRANSFORMATION -> FadeTransformation()
            SliderAnimations.FANTRANSFORMATION -> FanTransformation()
            SliderAnimations.FIDGETSPINTRANSFORMATION -> FidgetSpinTransformation()
            SliderAnimations.GATETRANSFORMATION -> GateTransformation()
            SliderAnimations.HINGETRANSFORMATION -> HingeTransformation()
            SliderAnimations.HORIZONTALFLIPTRANSFORMATION -> HorizontalFlipTransformation()
            SliderAnimations.POPTRANSFORMATION -> PopTransformation()
            SliderAnimations.SPINNERTRANSFORMATION -> SpinnerTransformation()
            SliderAnimations.TOSSTRANSFORMATION -> TossTransformation()
            SliderAnimations.VERTICALFLIPTRANSFORMATION -> VerticalFlipTransformation()
            SliderAnimations.VERTICALSHUTTRANSFORMATION -> VerticalShutTransformation()
            SliderAnimations.ZOOMOUTTRANSFORMATION -> ZoomOutTransformation()
            else -> SimpleTransformation()
        }
        mSliderPager.setPageTransformer(false, transformation)
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (isAutoCycle()) {
            when(event.action) {
                MotionEvent.ACTION_MOVE -> stopAutoCycle()
                MotionEvent.ACTION_UP -> {
                    // resume after ~2 seconds debounce.
                    mHandler.postDelayed({ startAutoCycle() }, 2000)
                }
            }
        }
        return false
    }

    /**
     * @param animation set slider animation manually .
     * it accepts [.] animation classes.
     */
    fun setCustomSliderTransformAnimation(animation: SliderPager.PageTransformer?) {
        mSliderPager.setPageTransformer(false, animation)
    }

    /**
     * @param duration changes slider animation duration.
     */
    fun setSliderAnimationDuration(duration: Int) {
        mSliderPager.setScrollDuration(duration)
    }

    /**
     * @param duration     changes slider animation duration.
     * @param interpolator its animation duration accelerator
     * An interpolator defines the rate of change of an animation
     */
    fun setSliderAnimationDuration(duration: Int, interpolator: Interpolator?) {
        mSliderPager.setScrollDuration(duration, interpolator)
    }

    /**
     * This method handles correct position whether slider is on infinite mode or not
     *
     * @param position changes position of slider
     * items manually.
     */
    fun setCurrentPagePosition(position: Int) {
        mSliderPager.setCurrentItem(position, true)
    }

    /**
     * @return Nullable position of current sliding item.
     */
    fun getCurrentPagePosition(): Int {
        return if (getSliderAdapter() != null) {
            getSliderPager().getCurrentItem()
        } else {
            throw NullPointerException("Adapter not set")
        }
    }

    fun getPagerIndicator(): PageIndicatorView? = mPagerIndicator

    fun setPageIndicatorView(indicatorView: PageIndicatorView) {
        mPagerIndicator = indicatorView
        initIndicator()
    }

    fun setIndicatorEnabled(enabled: Boolean) {
        mIsIndicatorEnabled = enabled
        if (mPagerIndicator == null && enabled) {
            initIndicator()
        }
    }

    /**
     * @param duration modifies indicator animation duration.
     */
    fun setIndicatorAnimationDuration(duration: Long) {
        mPagerIndicator?.setAnimationDuration(duration)
    }

    /**
     * @param gravity [.View] integer gravity of indicator dots.
     */
    fun setIndicatorGravity(gravity: Int) {
        mPagerIndicator?.let { indicator ->
            val layoutParams = indicator.layoutParams as LayoutParams
            layoutParams.gravity = gravity
            indicator.layoutParams = layoutParams
        }
    }

    /**
     * @param padding changes indicator padding.
     */
    fun setIndicatorPadding(padding: Int) {
        mPagerIndicator?.setPadding(padding)
    }

    /**
     * Sets the indicator margins, in pixels.
     *
     * @param left   the left margin size
     * @param top    the top margin size
     * @param right  the right margin size
     * @param bottom the bottom margin size
     */
    fun setIndicatorMargins(left: Int, top: Int, right: Int, bottom: Int) {
        mPagerIndicator?.let { indicator ->
            val layoutParams = indicator.layoutParams as LayoutParams
            layoutParams.setMargins(left, top, right, bottom)
            indicator.layoutParams = layoutParams
        }
    }

    /**
     * @param orientation changes orientation of indicator dots.
     */
    fun setIndicatorOrientation(orientation: Orientation?) {
        mPagerIndicator?.setOrientation(orientation)
    }

    /**
     * @param animation [.SliderView] of indicator dots
     */
    fun setIndicatorAnimation(animation: IndicatorAnimationType?) {
        mPagerIndicator?.setAnimationType(animation)
    }

    /**
     * @param visibility this method changes indicator visibility
     */
    fun setIndicatorVisibility(visibility: Boolean) {
        mPagerIndicator?.visibility = if (visibility) VISIBLE else GONE
    }

    /**
     * @return number of items in [)][.SliderView]
     */
    @SuppressLint("BinaryOperationInTimber")
    private fun getAdapterItemsCount(): Int {
        if(getSliderAdapter() == null) {
            Log.e("getAdapterItemsCount", "Slider Adapter is null so, " +
                    "it can't get count of items")
        }
        return getSliderAdapter()?.count?: 0
    }

    /**
     * This method stars the auto cycling
     */
    fun startAutoCycle() {
        //clean previous callbacks
        mHandler.removeCallbacks(this)

        //Run the loop for the first time
        mHandler.postDelayed(this, mScrollTimeInMillis.toLong())
    }

    /**
     * This method cancels the auto cycling
     */
    fun stopAutoCycle() {
        //clean callback
        mHandler.removeCallbacks(this)
    }

    /**
     * This method setting direction of sliders auto cycling
     * accepts constant values defined in [.SliderView] class
     * {@value AUTO_CYCLE_DIRECTION_LEFT}
     * {@value AUTO_CYCLE_DIRECTION_RIGHT}
     * {@value AUTO_CYCLE_DIRECTION_BACK_AND_FORTH}
     */
    fun setAutoCycleDirection(direction: Int) {
        mAutoCycleDirection = direction
    }

    /**
     * @return direction of auto cycling
     * {@value AUTO_CYCLE_DIRECTION_LEFT}
     * {@value AUTO_CYCLE_DIRECTION_RIGHT}
     * {@value AUTO_CYCLE_DIRECTION_BACK_AND_FORTH}
     */
    fun getAutoCycleDirection(): Int {
        return mAutoCycleDirection
    }

    /**
     * @return size of indicator dot
     */
    fun getIndicatorRadius(): Int {
        return mPagerIndicator?.getRadius()?: 0
    }

    /**
     * @param rtlMode for indicator sliding direction
     */
    fun setIndicatorRtlMode(rtlMode: RtlMode?) {
        mPagerIndicator?.setRtlMode(rtlMode)
    }

    /**
     * @param pagerIndicatorRadius modifies size of indicator dots
     */
    fun setIndicatorRadius(pagerIndicatorRadius: Int) {
        mPagerIndicator?.setRadius(pagerIndicatorRadius)
    }

    /**
     * @param margin modifies indicator margin
     */
    fun setIndicatorMargin(margin: Int) {
        mPagerIndicator?.let { indicator ->
            val layoutParams = indicator.layoutParams as LayoutParams
            layoutParams.setMargins(margin, margin, margin, margin)
            indicator.layoutParams = layoutParams
        }
    }

    fun setIndicatorMarginCustom(left: Int, top: Int, right: Int, bottom: Int) {
        mPagerIndicator?.let { indicator ->
            val layoutParams = indicator.layoutParams as LayoutParams
            layoutParams.setMargins(left, top, right, bottom)
            indicator.layoutParams = layoutParams
        }
    }

    /**
     * @param color setting color of selected dot
     */
    fun setIndicatorSelectedColor(color: Int) {
        mPagerIndicator?.setSelectedColor(color)
    }

    /**
     * @return color of selected dot
     */
    fun getIndicatorSelectedColor(): Int = mPagerIndicator?.getSelectedColor()?: 0

    fun setIndicatorUnselectedColor(color: Int) {
        mPagerIndicator?.setUnselectedColor(color)
    }

    /**
     * @return color of unselected dots
     */
    fun getIndicatorUnselectedColor(): Int = mPagerIndicator?.getUnselectedColor()?: 0

    /**
     * This method handles sliding behaviors
     * which passed into [.SliderView]
     *
     *
     * see [.SliderView]
     */
    override fun run() {
        try {
            slideToNextPosition()
        } finally {
            if (mIsAutoCycle) {
                // continue the loop
                mHandler.postDelayed(this, mScrollTimeInMillis.toLong())
            }
        }
    }

    fun slideToNextPosition() {
        val currentPosition = mSliderPager.getCurrentItem()
        val adapterItemsCount = getAdapterItemsCount()
        if (adapterItemsCount > 1) {
            if (mAutoCycleDirection == AUTO_CYCLE_DIRECTION_BACK_AND_FORTH) {
                if (currentPosition % (adapterItemsCount - 1) == 0 && mPreviousPosition != getAdapterItemsCount() - 1 && mPreviousPosition != 0) {
                    mFlagBackAndForth = !mFlagBackAndForth
                }
                if (mFlagBackAndForth) {
                    mSliderPager.setCurrentItem(currentPosition + 1, true)
                } else {
                    mSliderPager.setCurrentItem(currentPosition - 1, true)
                }
            }
            if (mAutoCycleDirection == AUTO_CYCLE_DIRECTION_LEFT) {
                mSliderPager.setCurrentItem(currentPosition - 1, true)
            }
            if (mAutoCycleDirection == AUTO_CYCLE_DIRECTION_RIGHT) {
                mSliderPager.setCurrentItem(currentPosition + 1, true)
            }
        }
        mPreviousPosition = currentPosition
    }

    fun slideToPreviousPosition() {
        val currentPosition = mSliderPager.getCurrentItem()
        val adapterItemsCount = getAdapterItemsCount()
        if (adapterItemsCount > 1) {
            if (mAutoCycleDirection == AUTO_CYCLE_DIRECTION_BACK_AND_FORTH) {
                if (currentPosition % (adapterItemsCount - 1) == 0 && mPreviousPosition != getAdapterItemsCount() - 1 && mPreviousPosition != 0) {
                    mFlagBackAndForth = !mFlagBackAndForth
                }
                if (mFlagBackAndForth && currentPosition < mPreviousPosition) {
                    mSliderPager.setCurrentItem(currentPosition - 1, true)
                } else {
                    mSliderPager.setCurrentItem(currentPosition + 1, true)
                }
            }
            if (mAutoCycleDirection == AUTO_CYCLE_DIRECTION_LEFT) {
                mSliderPager.setCurrentItem(currentPosition + 1, true)
            }
            if (mAutoCycleDirection == AUTO_CYCLE_DIRECTION_RIGHT) {
                mSliderPager.setCurrentItem(currentPosition - 1, true)
            }
        }
        mPreviousPosition = currentPosition
    }

    //sync infinite pager adapter with real one
    override fun dataSetChanged() {
        if (mIsInfiniteAdapter) {
            mInfinitePagerAdapter?.notifyDataSetChanged()
            mSliderPager.setCurrentItem(0, false)
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        // nothing to do
    }

    override fun onPageSelected(position: Int) {
        mPageListener?.onSliderPageChanged(position)
    }

    override fun onPageScrollStateChanged(state: Int) {
        // nothing to do
    }

    interface OnSliderPageListener {
        /**
         * This method will be invoked when a new page becomes selected. Animation is not
         * necessarily complete.
         *
         * @param position Position index of the new selected page.
         */
        fun onSliderPageChanged(position: Int)
    }
}