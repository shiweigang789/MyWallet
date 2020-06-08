package com.topnetwork.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.topnetwork.views.R.color.grey
import com.topnetwork.views.helpers.LayoutHelper
import kotlinx.android.synthetic.main.view_icon.view.*

/**
 * @FileName     : IconView
 * @date         : 2020/6/8 10:57
 * @author       : Owen
 * @description  :
 */
class IconView : ConstraintLayout {

    init {
        inflate(context, R.layout.view_icon, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun bind(drawable: Drawable){
        dynamicIcon.setImageDrawable(drawable)
    }

    fun bind(coinCode:String){
        dynamicIcon.setImageResource(LayoutHelper.getCoinDrawableResource(context, coinCode))
        tintWithGreyColor()
    }

    fun setTint(tintColor: ColorStateList) {
        dynamicIcon.imageTintList = tintColor
    }

    fun bind(icon: Int, tintWithGrey: Boolean = false) {
        dynamicIcon.setImageResource(icon)
        if (tintWithGrey) {
            tintWithGreyColor()
        }
    }

    private fun tintWithGreyColor() {
        val greyColor = ContextCompat.getColor(context, grey)
        val tintColorStateList = ColorStateList.valueOf(greyColor)
        setTint(tintColorStateList)
    }

}