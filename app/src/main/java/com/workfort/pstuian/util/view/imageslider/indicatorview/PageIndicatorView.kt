package com.workfort.pstuian.util.view.imageslider.indicatorview

import android.annotation.SuppressLint
import android.content.Context
import android.database.DataSetObserver
import android.graphics.Canvas
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.workfort.pstuian.util.view.imageslider.SliderPager
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.type.IndicatorAnimationType
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.type.ScaleAnimation
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.controller.DrawController.ClickListener
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.Indicator
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.Orientation
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.PositionSavedState
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.RtlMode
import com.workfort.pstuian.util.view.imageslider.indicatorview.utils.CoordinatesUtils.getProgress
import com.workfort.pstuian.util.view.imageslider.indicatorview.utils.DensityUtils.dpToPx
import com.workfort.pstuian.util.view.imageslider.indicatorview.utils.IdUtils
import com.workfort.pstuian.util.view.imageslider.infiniteadapter.InfinitePagerAdapter

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 9:05.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/11/27.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class PageIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes), SliderPager.OnPageChangeListener,
    IndicatorManager.Listener, SliderPager.OnAdapterChangeListener {
    private lateinit var manager: IndicatorManager
    private var setObserver: DataSetObserver? = null
    private var viewPager: SliderPager? = null
    private var isInteractionEnabled = false

    init {
        setupId()
        initIndicatorManager(attrs)
    }

    private fun setupId() {
        if (id == NO_ID) {
            id = IdUtils.generateViewId()
        }
    }

    private fun initIndicatorManager(attrs: AttributeSet?) {
        manager = IndicatorManager(this)
        manager.drawer().initAttributes(context, attrs)
        val indicator = manager.indicator()
        indicator.paddingLeft = paddingLeft
        indicator.paddingTop = paddingTop
        indicator.paddingRight = paddingRight
        indicator.paddingBottom = paddingBottom
        isInteractionEnabled = indicator.interactiveAnimation
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        findViewPager(parent)
    }

    override fun onDetachedFromWindow() {
        unRegisterSetObserver()
        super.onDetachedFromWindow()
    }

    public override fun onSaveInstanceState(): Parcelable {
        val indicator = manager.indicator()
        return PositionSavedState(
            super.onSaveInstanceState(),
            indicator.selectedPosition,
            indicator.selectingPosition,
            indicator.lastSelectedPosition
        )
    }

    public override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is PositionSavedState) {
            manager.indicator().apply {
                selectedPosition = state.selectedPosition
                selectingPosition = state.selectingPosition
                lastSelectedPosition = state.lastSelectedPosition
            }
            super.onRestoreInstanceState(state.superState)
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val pair = manager.drawer().measureViewSize(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(pair.first, pair.second)
    }

    override fun onDraw(canvas: Canvas) {
        manager.drawer().draw(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        manager.drawer().touch(event)
        return true
    }

    override fun onIndicatorUpdated() {
        invalidate()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        onPageScroll(position, positionOffset)
    }

    override fun onPageSelected(position: Int) {
        onPageSelect(position)
    }

    override fun onPageScrollStateChanged(state: Int) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            manager.indicator().interactiveAnimation = isInteractionEnabled
        }
    }

    override fun onAdapterChanged(
        viewPager: SliderPager,
        oldAdapter: PagerAdapter?,
        newAdapter: PagerAdapter?
    ) {
        updateState()
    }

    fun setCount(count: Int) {
        if (count >= 0 && manager.indicator().count != count) {
            manager.indicator().count = count
            updateVisibility()
            requestLayout()
        }
    }

    fun getCount(): Int {
        return manager.indicator().count
    }

    fun setDynamicCount(dynamicCount: Boolean) {
        manager.indicator().dynamicCount = dynamicCount
        if (dynamicCount) {
            registerSetObserver()
        } else {
            unRegisterSetObserver()
        }
    }

    fun setRadius(radiusDp: Int) {
        var calculatedRadiusDp = radiusDp
        if (radiusDp < 0) {
            calculatedRadiusDp = 0
        }
        manager.indicator().radius = dpToPx(calculatedRadiusDp)
        invalidate()
    }

    fun setRadius(radiusPx: Float) {
        var calculatedRadiusPx = radiusPx
        if (radiusPx < 0) {
            calculatedRadiusPx = 0f
        }
        manager.indicator().radius = calculatedRadiusPx.toInt()
        invalidate()
    }

    fun getRadius(): Int {
        return manager.indicator().radius
    }

    fun setPadding(paddingDp: Int) {
        var calculatedPaddingDp = paddingDp
        if (paddingDp < 0) {
            calculatedPaddingDp = 0
        }
        val paddingPx = dpToPx(calculatedPaddingDp)
        manager.indicator().padding = paddingPx
        invalidate()
    }

    fun setPadding(paddingPx: Float) {
        var calculatedPaddingPx = paddingPx
        if (paddingPx < 0) {
            calculatedPaddingPx = 0f
        }
        manager.indicator().padding = calculatedPaddingPx.toInt()
        invalidate()
    }

    fun getPadding(): Int {
        return manager.indicator().padding
    }

    fun setScaleFactor(factor: Float) {
        var calculatedFactor = factor
        if (factor > ScaleAnimation.MAX_SCALE_FACTOR) {
            calculatedFactor = ScaleAnimation.MAX_SCALE_FACTOR
        } else if (factor < ScaleAnimation.MIN_SCALE_FACTOR) {
            calculatedFactor = ScaleAnimation.MIN_SCALE_FACTOR
        }
        manager.indicator().scaleFactor = calculatedFactor
    }

    fun getScaleFactor(): Float {
        return manager.indicator().scaleFactor
    }

    fun setStrokeWidth(strokePx: Float) {
        var calculatedStrokePx = strokePx
        val radiusPx = manager.indicator().radius
        if (strokePx < 0) {
            calculatedStrokePx = 0f
        } else if (strokePx > radiusPx) {
            calculatedStrokePx = radiusPx.toFloat()
        }
        manager.indicator().stroke = calculatedStrokePx.toInt()
        invalidate()
    }

    fun setStrokeWidth(strokeDp: Int) {
        var strokePx = dpToPx(strokeDp)
        val radiusPx = manager.indicator().radius
        if (strokePx < 0) {
            strokePx = 0
        } else if (strokePx > radiusPx) {
            strokePx = radiusPx
        }
        manager.indicator().stroke = strokePx
        invalidate()
    }

    fun getStrokeWidth(): Int {
        return manager.indicator().stroke
    }

    fun setSelectedColor(color: Int) {
        manager.indicator().selectedColor = color
        invalidate()
    }

    fun getSelectedColor(): Int {
        return manager.indicator().selectedColor
    }

    fun setUnselectedColor(color: Int) {
        manager.indicator().unselectedColor = color
        invalidate()
    }

    fun getUnselectedColor(): Int {
        return manager.indicator().unselectedColor
    }

    fun setAutoVisibility(autoVisibility: Boolean) {
        if (!autoVisibility) {
            visibility = VISIBLE
        }
        manager.indicator().autoVisibility = autoVisibility
        updateVisibility()
    }

    fun setOrientation(orientation: Orientation?) {
        if (orientation != null) {
            manager.indicator().orientation = orientation
            requestLayout()
        }
    }

    fun setAnimationDuration(duration: Long) {
        manager.indicator().animationDuration = duration
    }

    fun getAnimationDuration(): Long {
        return manager.indicator().animationDuration
    }

    fun setAnimationType(type: IndicatorAnimationType?) {
        manager.onValueUpdated(null)
        if (type != null) {
            manager.indicator().animationType = type
        } else {
            manager.indicator().animationType = IndicatorAnimationType.NONE
        }
        invalidate()
    }

    fun setInteractiveAnimation(isInteractive: Boolean) {
        manager.indicator().interactiveAnimation = isInteractive
        isInteractionEnabled = isInteractive
    }

    fun setViewPager(pager: SliderPager?) {
        releaseViewPager()
        if (pager == null) {
            return
        }
        viewPager = pager
        viewPager?.addOnPageChangeListener(this)
        viewPager?.addOnAdapterChangeListener(this)
        manager.indicator().viewPagerId = viewPager!!.id
        setDynamicCount(manager.indicator().dynamicCount)
        updateState()
    }

    fun releaseViewPager() {
        if (viewPager != null) {
            viewPager?.removeOnPageChangeListener(this)
            viewPager = null
        }
    }

    fun setRtlMode(mode: RtlMode?) {
        val indicator = manager.indicator()
        if (mode == null) {
            indicator.rtlMode = RtlMode.Off
        } else {
            indicator.rtlMode = mode
        }
        if (viewPager == null) {
            return
        }
        val selectedPosition = indicator.selectedPosition
        var position = selectedPosition
        if (isRtl()) {
            position = indicator.count - 1 - selectedPosition
        } else if (viewPager != null) {
            position = viewPager!!.getCurrentItem()
        }
        indicator.lastSelectedPosition = position
        indicator.selectingPosition = position
        indicator.selectedPosition = position
        invalidate()
    }

    fun getSelection(): Int {
        return manager.indicator().selectedPosition
    }

    fun setSelection(position: Int) {
        var position = position
        val indicator = manager.indicator()
        position = adjustPosition(position)
        if (position == indicator.selectedPosition || position == indicator.selectingPosition) {
            return
        }
        indicator.interactiveAnimation = false
        indicator.lastSelectedPosition = indicator.selectedPosition
        indicator.selectingPosition = position
        indicator.selectedPosition = position
        manager.animate().basic()
    }

    fun setSelected(position: Int) {
        val indicator = manager.indicator()
        val animationType = indicator.animationType
        indicator.animationType = IndicatorAnimationType.NONE
        setSelection(position)
        indicator.animationType = animationType
    }

    fun clearSelection() {
        val indicator = manager.indicator()
        indicator.interactiveAnimation = false
        indicator.lastSelectedPosition = Indicator.COUNT_NONE
        indicator.selectingPosition = Indicator.COUNT_NONE
        indicator.selectedPosition = Indicator.COUNT_NONE
        manager.animate().basic()
    }

    fun setProgress(selectingPosition: Int, progress: Float) {
        var calculatedSelectingPosition = selectingPosition
        var calculatedProgress = progress
        val indicator = manager.indicator()
        if (!indicator.interactiveAnimation) {
            return
        }
        val count = indicator.count
        if (count <= 0 || selectingPosition < 0) {
            calculatedSelectingPosition = 0
        } else if (selectingPosition > count - 1) {
            calculatedSelectingPosition = count - 1
        }
        if (progress < 0) {
            calculatedProgress = 0f
        } else if (progress > 1) {
            calculatedProgress = 1f
        }
        if (calculatedProgress == 1f) {
            indicator.lastSelectedPosition = indicator.selectedPosition
            indicator.selectedPosition = calculatedSelectingPosition
        }
        indicator.selectingPosition = calculatedSelectingPosition
        manager.animate().interactive(calculatedProgress)
    }

    fun setClickListener(listener: ClickListener?) {
        manager.drawer().setClickListener(listener)
    }

    private fun registerSetObserver() {
        if (setObserver != null || viewPager == null || viewPager?.mAdapter == null) {
            return
        }
        setObserver = object : DataSetObserver() {
            override fun onChanged() {
                updateState()
            }
        }
        try {
            viewPager?.mAdapter?.registerDataSetObserver(setObserver!!)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun unRegisterSetObserver() {
        if (setObserver == null || viewPager == null || viewPager?.mAdapter == null) {
            return
        }
        try {
            viewPager?.mAdapter?.unregisterDataSetObserver(setObserver!!)
            setObserver = null
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun updateState() {
        if (viewPager == null || viewPager?.mAdapter == null) {
            return
        }
        val count: Int
        val position: Int
        if (viewPager?.mAdapter is InfinitePagerAdapter) {
            count = (viewPager?.mAdapter as InfinitePagerAdapter?)!!.getRealCount()
            position = if (count > 0) {
                viewPager!!.getCurrentItem() % count
            } else {
                0
            }
        } else {
            count = viewPager?.mAdapter!!.count
            position = viewPager!!.getCurrentItem()
        }
        val selectedPos = if (isRtl()) count - 1 - position else position
        manager.indicator().selectedPosition = selectedPos
        manager.indicator().selectingPosition = selectedPos
        manager.indicator().lastSelectedPosition = selectedPos
        manager.indicator().count = count
        manager.animate().end()
        updateVisibility()
        requestLayout()
    }

    private fun updateVisibility() {
        if (!manager.indicator().autoVisibility) {
            return
        }
        val count = manager.indicator().count
        val visibility = visibility
        if (visibility != VISIBLE && count > Indicator.MIN_COUNT) {
            setVisibility(VISIBLE)
        } else if (visibility != INVISIBLE && count <= Indicator.MIN_COUNT) {
            setVisibility(INVISIBLE)
        }
    }

    private fun onPageSelect(position: Int) {
        var calculatedPosition = position
        val indicator = manager.indicator()
        val canSelectIndicator = isViewMeasured()
        val count = indicator.count
        if (canSelectIndicator) {
            if (isRtl()) {
                calculatedPosition = count - 1 - position
            }
            setSelection(calculatedPosition)
        }
    }

    private fun onPageScroll(position: Int, positionOffset: Float) {
        val indicator = manager.indicator()
        val animationType = indicator.animationType
        val interactiveAnimation = indicator.interactiveAnimation
        val canSelectIndicator =
            isViewMeasured() && interactiveAnimation && animationType !== IndicatorAnimationType.NONE
        if (!canSelectIndicator) {
            return
        }
        val progressPair = getProgress(indicator, position, positionOffset, isRtl())
        val selectingPosition = progressPair.first
        val selectingProgress = progressPair.second
        setProgress(selectingPosition, selectingProgress)
    }

    private fun isRtl(): Boolean {
        when (manager.indicator().rtlMode) {
            RtlMode.On -> return true
            RtlMode.Off -> return false
            RtlMode.Auto -> return TextUtilsCompat.getLayoutDirectionFromLocale(context.resources.configuration.locale) == ViewCompat.LAYOUT_DIRECTION_RTL
        }
        return false
    }

    private fun isViewMeasured(): Boolean {
        return measuredHeight != 0 || measuredWidth != 0
    }

    private fun findViewPager(viewParent: ViewParent?) {
        val isValidParent = viewParent != null &&
                viewParent is ViewGroup && viewParent.childCount > 0
        if (!isValidParent) {
            return
        }
        val viewPagerId = manager.indicator().viewPagerId
        val viewPager = findViewPager((viewParent as ViewGroup), viewPagerId)
        if (viewPager != null) {
            setViewPager(viewPager)
        } else {
            findViewPager(viewParent.parent)
        }
    }

    private fun findViewPager(viewGroup: ViewGroup, id: Int): SliderPager? {
        if (viewGroup.childCount <= 0) {
            return null
        }
        val view = viewGroup.findViewById<View>(id)
        return if (view != null && view is SliderPager) view else null
    }

    private fun adjustPosition(position: Int): Int {
        var calculatedPosition = position
        val indicator = manager.indicator()
        val count = indicator.count
        val lastPosition = count - 1
        if (position <= 0) {
            calculatedPosition = 0
        } else if (position > lastPosition) {
            calculatedPosition = lastPosition
        }
        return calculatedPosition
    }
}