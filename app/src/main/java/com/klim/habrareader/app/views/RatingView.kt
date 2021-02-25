package com.klim.habrareader.app.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.klim.habrareader.R

class RatingView : LinearLayout {
    //attrs
    private var icon = 0
    private var iconColor = 0
    private var iconWidth = 0
    private var iconHeight = 0

    private var valueColor = 0
    private var textSize = 0f
    private var value = 0

    private var usePrefix = false
    private var useColors = false
    private var colorNegative = 0
    private var colorNeutral = 0
    private var colorPositive = 0

    //views
    private lateinit var ivIcon: ImageView
    private lateinit var tvValue: TextView

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.rating_view, this, true)
        getAttr(attrs)
        findViews()
        customizeView()
    }

    private fun getAttr(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RatingView, 0, 0)
        icon = a.getResourceId(R.styleable.RatingView_icon, R.drawable.ic_icon_lump)
        iconColor = a.getColor(R.styleable.RatingView_iconColor, Color.RED)
        iconWidth = a.getDimensionPixelSize(R.styleable.RatingView_iconWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
        iconHeight = a.getDimensionPixelSize(R.styleable.RatingView_iconHeight, ViewGroup.LayoutParams.WRAP_CONTENT)

        valueColor = a.getColor(R.styleable.RatingView_valueColor, Color.RED)
        textSize = a.getDimensionPixelSize(R.styleable.RatingView_textSize, 14).toFloat()
        value = a.getInt(R.styleable.RatingView_value, 0)

        usePrefix = a.getBoolean(R.styleable.RatingView_usePrefix, false)
        useColors = a.getBoolean(R.styleable.RatingView_useColors, false)

        colorNegative = a.getColor(R.styleable.RatingView_colorNegative, Color.RED)
        colorNeutral = a.getColor(R.styleable.RatingView_colorNeutral, Color.RED)
        colorPositive = a.getColor(R.styleable.RatingView_colorPositive, Color.RED)


        a.recycle()
    }

    private fun findViews() {
        ivIcon = findViewById(R.id.ivIcon)
        tvValue = findViewById(R.id.tvValue)
    }

    private fun customizeView() {
        ivIcon.setImageResource(icon)
        ivIcon.setColorFilter(iconColor)
        ivIcon.layoutParams = LayoutParams(iconWidth, iconHeight)

        tvValue.setTextColor(valueColor)
        tvValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        tvValue.text = "" + value
    }

    fun setValue(value: Int) {
        if (usePrefix) {
            tvValue.text = if (value == 0) " $value" else if (value > 0) "+$value" else value.toString()
        } else {
            tvValue.text = value.toString()
        }

        if (useColors) {
            tvValue.setTextColor(if (value == 0) colorNeutral else if (value < 0) colorNegative else colorPositive)
        }
    }

    fun setValue(value: String?) {
        tvValue.text = value
    }
}