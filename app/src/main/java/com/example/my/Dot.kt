package com.example.my

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Dot(x: Float, y: Float,radius:Float,tag:Int) {
    //点的中心位置
    val cx = x
    val cy = y
    //圆点的半径
    val cradius = radius
    //标记圆点的tag值
    val ctag = tag
    //是否已被点亮
    var isOrHeightlight = false
    //圆点的画笔
    var paint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 5f
    }
    //圆点的矩形
    var rect = Rect((cx-cradius).toInt(),(cy-cradius).toInt(),
            (cx+cradius).toInt(),(cy+cradius).toInt())
    //设置圆点的画笔
    fun setPaint(color:Int){
        paint.color = color
    }
}