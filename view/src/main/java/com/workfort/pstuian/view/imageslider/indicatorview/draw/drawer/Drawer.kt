package com.workfort.pstuian.view.imageslider.indicatorview.draw.drawer

import android.graphics.Canvas
import android.graphics.Paint
import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.Value
import com.workfort.pstuian.view.imageslider.indicatorview.draw.data.Indicator
import com.workfort.pstuian.view.imageslider.indicatorview.draw.drawer.type.BasicDrawer
import com.workfort.pstuian.view.imageslider.indicatorview.draw.drawer.type.ColorDrawer
import com.workfort.pstuian.view.imageslider.indicatorview.draw.drawer.type.DropDrawer
import com.workfort.pstuian.view.imageslider.indicatorview.draw.drawer.type.FillDrawer
import com.workfort.pstuian.view.imageslider.indicatorview.draw.drawer.type.ScaleDownDrawer
import com.workfort.pstuian.view.imageslider.indicatorview.draw.drawer.type.ScaleDrawer
import com.workfort.pstuian.view.imageslider.indicatorview.draw.drawer.type.SlideDrawer
import com.workfort.pstuian.view.imageslider.indicatorview.draw.drawer.type.SwapDrawer
import com.workfort.pstuian.view.imageslider.indicatorview.draw.drawer.type.ThinWormDrawer
import com.workfort.pstuian.view.imageslider.indicatorview.draw.drawer.type.WormDrawer

class Drawer(indicator: Indicator) {
    val paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private val basicDrawer = BasicDrawer(paint, indicator)
    private val colorDrawer = ColorDrawer(paint, indicator)
    private val scaleDrawer = ScaleDrawer(paint, indicator)
    private val wormDrawer = WormDrawer(paint, indicator)
    private val slideDrawer = SlideDrawer(paint, indicator)
    private val fillDrawer = FillDrawer(paint, indicator)
    private val thinWormDrawer = ThinWormDrawer(paint, indicator)
    private val dropDrawer = DropDrawer(paint, indicator)
    private val swapDrawer = SwapDrawer(paint, indicator)
    private val scaleDownDrawer = ScaleDownDrawer(paint, indicator)
    private var position = 0
    private var coordinateX = 0
    private var coordinateY = 0

    fun setup(position: Int, coordinateX: Int, coordinateY: Int) {
        this.position = position
        this.coordinateX = coordinateX
        this.coordinateY = coordinateY
    }

    fun drawBasic(canvas: Canvas, isSelectedItem: Boolean) {
        basicDrawer.draw(canvas, position, isSelectedItem, coordinateX, coordinateY)
    }

    fun drawColor(canvas: Canvas, value: Value) {
        colorDrawer.draw(canvas, value, position, coordinateX, coordinateY)
    }

    fun drawScale(canvas: Canvas, value: Value) {
        scaleDrawer.draw(canvas, value, position, coordinateX, coordinateY)
    }

    fun drawWorm(canvas: Canvas, value: Value) {
        wormDrawer.draw(canvas, value, coordinateX, coordinateY)
    }

    fun drawSlide(canvas: Canvas, value: Value) {
        slideDrawer.draw(canvas, value, coordinateX, coordinateY)
    }

    fun drawFill(canvas: Canvas, value: Value) {
        fillDrawer.draw(canvas, value, position, coordinateX, coordinateY)
    }

    fun drawThinWorm(canvas: Canvas, value: Value) {
        thinWormDrawer.draw(canvas, value, coordinateX, coordinateY)
    }

    fun drawDrop(canvas: Canvas, value: Value) {
        dropDrawer.draw(canvas, value, coordinateX, coordinateY)
    }

    fun drawSwap(canvas: Canvas, value: Value) {
        swapDrawer.draw(canvas, value, position, coordinateX, coordinateY)
    }

    fun drawScaleDown(canvas: Canvas, value: Value) {
        scaleDownDrawer.draw(canvas, value, position, coordinateX, coordinateY)
    }
}