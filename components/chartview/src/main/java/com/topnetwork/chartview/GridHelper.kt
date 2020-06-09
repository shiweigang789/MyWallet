package com.topnetwork.chartview

import android.graphics.RectF
import android.text.format.DateFormat
import com.topnetwork.chartview.models.ChartConfig
import com.topnetwork.chartview.models.GridColumn
import java.text.SimpleDateFormat
import java.util.*

/**
 * @FileName     : GridHelper
 * @date         : 2020/6/9 15:44
 * @author       : Owen
 * @description  :
 */
class GridHelper(private val shape: RectF, private val config: ChartConfig) {

    fun setGridColumns(chartType: ChartView.ChartType, startTimestamp: Long, endTimestamp: Long)
            : List<GridColumn> {
        val start = startTimestamp * 1000
        val end = endTimestamp * 1000

        val calendar = Calendar.getInstance().apply { time = Date() }
        var columnLabel = columnLabel(calendar, chartType)

        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        when (chartType) {
            ChartView.ChartType.DAILY -> {
            }
            ChartView.ChartType.WEEKLY,
            ChartView.ChartType.MONTHLY -> {
                calendar.set(Calendar.HOUR_OF_DAY, 0)
            }
            ChartView.ChartType.MONTHLY3,
            ChartView.ChartType.MONTHLY6,
            ChartView.ChartType.MONTHLY12,
            ChartView.ChartType.MONTHLY24 -> {
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.DATE, 1)
            }
        }

        val delta = (end - start) / shape.right
        val columns = mutableListOf<GridColumn>()

        while (true) {
            val xAxis = (calendar.time.time - start) / delta
            if (xAxis <= 0) break

            columns.add(GridColumn(xAxis, columnLabel))
            moveColumn(chartType, calendar)
            columnLabel = columnLabel(calendar, chartType)
        }

        return columns

    }

    private fun moveColumn(type: ChartView.ChartType, calendar: Calendar) {
        when (type) {
            ChartView.ChartType.DAILY -> calendar.add(Calendar.HOUR_OF_DAY, -6)       // 6 hour
            ChartView.ChartType.WEEKLY -> calendar.add(Calendar.DAY_OF_WEEK, -2)      // 2 days
            ChartView.ChartType.MONTHLY -> calendar.add(Calendar.DAY_OF_MONTH, -6)    // 6 days
            ChartView.ChartType.MONTHLY3 -> calendar.add(Calendar.DAY_OF_MONTH, -14)  // 6 days
            ChartView.ChartType.MONTHLY6 -> calendar.add(Calendar.MONTH, -1)          // 1 month
            ChartView.ChartType.MONTHLY12 -> calendar.add(Calendar.MONTH, -2)         // 2 month
            ChartView.ChartType.MONTHLY24 -> calendar.add(Calendar.MONTH, -4)         // 4 month
        }
    }

    private fun columnLabel(calendar: Calendar, type: ChartView.ChartType): String {
        return when (type) {
            ChartView.ChartType.DAILY -> calendar.get(Calendar.HOUR_OF_DAY).toString()
            ChartView.ChartType.WEEKLY -> formatDate(calendar.time, "EEE")
            ChartView.ChartType.MONTHLY,
            ChartView.ChartType.MONTHLY3 -> calendar.get(Calendar.DAY_OF_MONTH).toString()
            ChartView.ChartType.MONTHLY6,
            ChartView.ChartType.MONTHLY12,
            ChartView.ChartType.MONTHLY24 -> formatDate(calendar.time, "MMM")
        }
    }

    private fun formatDate(date: Date, pattern: String): String {
        return SimpleDateFormat(
            DateFormat.getBestDateTimePattern(Locale.getDefault(), pattern),
            Locale.getDefault()
        ).format(date)
    }

}