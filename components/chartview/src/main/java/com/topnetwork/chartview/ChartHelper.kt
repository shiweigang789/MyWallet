package com.topnetwork.chartview

import android.graphics.RectF
import com.topnetwork.chartview.models.ChartConfig
import com.topnetwork.chartview.models.ChartPoint

/**
 * @FileName     : ChartHelper
 * @date         : 2020/6/9 16:09
 * @author       : Owen
 * @description  :
 */
class ChartHelper(private val shape: RectF, private val config: ChartConfig) {

    fun setCoordinates(points: List<ChartPoint>, startTimestamp: Long, endTimestamp: Long): List<ChartCurve.Coordinate> {
        val width = shape.width()
        val height = shape.height()

        val deltaX = (endTimestamp - startTimestamp) / width
        val deltaY = (config.valueTop - config.valueLow) / height

        return points.map { point ->
            val x = (point.timestamp - startTimestamp) / deltaX
            val y = (point.value - config.valueLow) / deltaY

            ChartCurve.Coordinate(x, height - y, point)
        }
    }

    fun getTopAndLow(coordinates: List<ChartCurve.Coordinate>): Pair<ChartCurve.Coordinate, ChartCurve.Coordinate> {
        var topCoordinate = coordinates[0]
        var lowCoordinate = coordinates[0]

        for (coordinate in coordinates) {
            if (coordinate.point.value > topCoordinate.point.value) {
                topCoordinate = coordinate
            }

            if (coordinate.point.value < lowCoordinate.point.value) {
                lowCoordinate = coordinate
            }
        }

        return Pair(topCoordinate, lowCoordinate)
    }
}