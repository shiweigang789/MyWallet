package com.topnetwork.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @FileName     : Extensions
 * @date         : 2020/6/8 10:21
 * @author       : Owen
 * @description  : 扩展方法
 */
fun View.showIf(condition: Boolean, hideType: Int = View.GONE) {
    visibility = if (condition) View.VISIBLE else hideType
}

fun inflate(parent: ViewGroup, layout: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(parent.context).inflate(layout, parent, attachToRoot)
}