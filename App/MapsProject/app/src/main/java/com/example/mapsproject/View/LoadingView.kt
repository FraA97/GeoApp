package com.example.mapsproject.View

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

class LoadingView (context: Context?) : View(context), View.OnTouchListener {


    private var touched=false //ball already touched
    private val howclose = 1.3f //how close the finger needs to be for touching
    private val radius = 100f

    var bx = radius
    var by = radius

    var vx=500f
    var vy=1500f


    var barrierx=0f
    var barriery=0f

    var current = System.currentTimeMillis()

    var m = Matrix()
    val ballPainter= Paint().apply {
        shader=
                RadialGradient(0f,0f,radius,
                        Color.WHITE, Color.BLACK, Shader.TileMode.CLAMP)
    }

    init {
        setOnTouchListener(this)

    }
    override fun onDraw(cv: Canvas?) {
        super.onDraw(cv)

        val now = System.currentTimeMillis()

        val dt = now-current
        current=now

        //Update ball position

        bx+=vx* dt/1000
        by+=vy* dt/1000


        //Ball hits and edge?
        if ((bx<radius) and (vx<0)) vx*=-1f //hits left edge and moves towards left
        if ((bx>width-radius) and (vx>0)) vx*=-1f //right edge while moving right
        if ((by>height-radius) and (vy>0)) vy*=-1 //bottom edge while moving downward
        if ((by<radius) and (vy<0)) {vy*=-1;touched=false} //top while moving up


        //Ball was already touched?
        if ( !touched and                   //was not touched
                (barriery>=by+radius) and   //now the y finger is below the ball..
                (barriery<=by+howclose*radius) and // and no more than howclose below
                (Math.abs(barrierx - bx) <radius) ) // and x finger coordinate close to the ball
        {
            if (vy>0) { //the ball was falling
                vy*=-1
                touched=true
            }
        }


        val offx = 2*bx/width-1
        val offy = 2*by/height-1

        m.setTranslate(bx+0.3f*radius*offx,by+0.3f*radius*offy)
        ballPainter.shader.setLocalMatrix(m)
        cv?.drawCircle(bx,by,radius,ballPainter)
        invalidate()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        when(event?.action){
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE ->{
                barrierx=event?.x
                barriery=event?.y
            }
            MotionEvent.ACTION_UP -> {
                barrierx=0f;barriery=0f
            }
        }

        return true
    }

}