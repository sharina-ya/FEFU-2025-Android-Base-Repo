package com.example.animegenres

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class MyFlexBoxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        var totalWidth = paddingLeft + paddingRight
        var maxHeight = 0
        var currentLineHeight = 0

        // Measure children - ИСПОЛЬЗУЕМ MarginLayoutParams для ВСЕХ детей
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) continue

            // Убеждаемся, что используем MarginLayoutParams
            val lp = child.layoutParams as? MarginLayoutParams ?: MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            child.layoutParams = lp


            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin

            if (totalWidth + childWidth > widthSize - paddingLeft - paddingRight) {
                totalWidth = paddingLeft + paddingRight + childWidth
                maxHeight += currentLineHeight
                currentLineHeight = childHeight
            } else {
                totalWidth += childWidth
                currentLineHeight = Math.max(currentLineHeight, childHeight)
            }
        }
        maxHeight += currentLineHeight + paddingTop + paddingBottom

        val finalWidth = if (widthMode == MeasureSpec.EXACTLY) widthSize else totalWidth
        val finalHeight = if (heightMode == MeasureSpec.EXACTLY) heightSize else maxHeight

        setMeasuredDimension(finalWidth, finalHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop

        var x = paddingLeft
        var y = paddingTop
        var currentLineHeight = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) continue

            val lp = child.layoutParams
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight
            val leftMargin = if (lp is MarginLayoutParams) lp.leftMargin else 0
            val topMargin = if (lp is MarginLayoutParams) lp.topMargin else 0
            val rightMargin = if (lp is MarginLayoutParams) lp.rightMargin else 0
            val bottomMargin = if (lp is MarginLayoutParams) lp.bottomMargin else 0


            if (x + childWidth + rightMargin > width - paddingRight) {
                x = paddingLeft
                y += currentLineHeight
                currentLineHeight = childHeight
            }

            child.layout(x + leftMargin, y + topMargin, x + childWidth + leftMargin, y + childHeight + topMargin)
            x += childWidth + leftMargin + rightMargin
            currentLineHeight = Math.max(currentLineHeight, childHeight + topMargin + bottomMargin)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }
}