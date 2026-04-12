package com.workfort.pstuian.util.helper

import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

object MathUtil {

    fun prettyCount(number: Number): String {
        val suffix = charArrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
        val numValue = number.toLong()
        if (numValue < 1000) return numValue.toString()
        val value = floor(log10(numValue.toDouble())).toInt()
        val base = value / 3
        return if (value >= 3 && base < suffix.size) {
            val result = numValue / 10.0.pow((base * 3).toDouble())
            formatDecimal(result) + suffix[base]
        } else {
            numValue.toString() // Fallback for very large numbers
        }
    }

    private fun formatDecimal(value: Double): String {
        val multiplier = 100.0
        val rounded = floor(value * multiplier) / multiplier
        val s = rounded.toString()
        return if (s.endsWith(".0")) s.substring(0, s.length - 2) else s
    }
}