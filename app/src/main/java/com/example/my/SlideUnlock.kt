package com.example.my

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.contains

class SlideUnlock: View {
    //存放9个点
    private val dots = mutableListOf<Dot>()
    //存放被点亮的点
    private val heightlightdots = mutableListOf<Dot>()
    //圆点的中心点的位置
    private var cx = 0f
    private var cy = 0f
    private var radius = 0f
    //记录线条的开始位置
    private var startX = 0f
    private var startY = 0f
    //线条的末端位置
    private var endX = 0f
    private var endY = 0f
    //没有连接两个点的线的路径
    private var path = Path()
    //画线条的画笔
    private val paintline:Paint by lazy {
        Paint().apply {
            style = Paint.Style.STROKE
            color = Color.RED
            strokeWidth = 5f
        }
    }
    //画圆点的画笔
    private val paint:Paint by lazy {
        Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 5f
            color = Color.BLACK
        }
    }
    //代码创建
    constructor(context: Context):super(context){}
    //xml创建
    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet){}

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        dot()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawdot(canvas)
        canvas?.drawLine(startX,startY,endX,endY,paintline)
        drawlineIn2Dot(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                //点亮圆点
                heighlightDot(Point(event.x.toInt(), event.y.toInt())).also {
                        if (it!=null){
                            heithliget(it)
                            //将其设置为已被点亮
                            it.isOrHeightlight = true

                        }
                }
            }
            MotionEvent.ACTION_MOVE->{
                //点亮圆点
                heighlightDot(Point(event.x.toInt(), event.y.toInt())).also {
                    if (it!=null){
                        //点亮点
                        heithliget(it)
                        //改变点是否已被加入被点亮数组的状态
                        it.isOrHeightlight = true
                        invalidate()
                    }
                    movePath(Point(event.x.toInt(),event.y.toInt()))
                }
            }
            MotionEvent.ACTION_UP->{
                //恢复原状
                originalState()
            }
        }
        return true
    }
    //在两个被点亮之间的画一条线
    private fun drawlineIn2Dot(canvas: Canvas?){
      if (heightlightdots.size>1){
          for (i in 0 until heightlightdots.size-1){
              canvas?.drawLine(heightlightdots[i].cx,heightlightdots[i].cy,
              heightlightdots[i+1].cx,heightlightdots[i+1].cy,paintline)
          }
      }
    }
    //返回被触摸的点
    private fun heighlightDot(point: Point):Dot?{
        for (dot in dots){
            if (dot.rect.contains(point)){
                return dot
            }
        }
       return null
    }
    //点亮点
    private fun heithliget(dot: Dot){
        dot.setPaint(Color.RED)
        //将被点亮的点记录
        //判断是否已经被点亮了
        if (dot.isOrHeightlight == false){
        heightlightdots.add(dot)

        }
        invalidate()
    }
    //设置最后一个亮点移动的线的路径
    private fun movePath(point: Point){
            val i =heightlightdots.size
        Log.v("lfl","最后路线")
            startX = heightlightdots[i-1].cx
            startY = heightlightdots[i-1].cy
            endX = point.x.toFloat()
            endY= point.y.toFloat()
            invalidate()
    }
    //恢复原状
    private fun originalState(){
        for (dot in heightlightdots){
            dot.setPaint(Color.BLACK)
            dot.isOrHeightlight = false
            invalidate()
        }
        heightlightdots.clear()
        //消去最后一根线
        startX = 0f
        startY = 0f
        endX = 0f
        endY = 0f
    }
    //将9个点准备好
    private fun dot(){
        radius = if (measuredHeight>measuredWidth){
            measuredWidth/14f
        }else{
            measuredHeight/14f
        }
        for (row in 0..2){
            for (clum in 0..2){
                cx = (clum*4+3)*radius
                cy = (row*4+3)*radius
                Dot(cx,cy,radius,row*10+clum).also {
                    dots.add(it)
                }
            }
        }
    }
    //画9个点
    private fun drawdot(canvas: Canvas?){
        for (dot in dots){
            canvas?.drawCircle(dot.cx,dot.cy,dot.cradius,dot.paint)
        }
    }
}