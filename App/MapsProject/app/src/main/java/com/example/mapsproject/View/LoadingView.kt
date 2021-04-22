package com.example.mapsproject.View

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import com.example.mapsproject.Configuration.MultiPlayerServerConf.Companion.touch

class LoadingView (context: Context?) : View(context), View.OnTouchListener {

    private var touched=false //ball already touched
    private val howclose = 1.3f //how close the finger needs to be for touching
    private var radius = 100f

    var bx = radius
    var by = radius
    var dx = width/2F
    var vx=500f
    var vy=1500f

    var scoreBallGame= 0
    var count=0
    //var scorePosX = width.toFloat() - (width.toFloat()/12)
    //var scorePosY = height.toFloat()/6


    var barrierx=0f
    var barriery=0f

    var current = System.currentTimeMillis()

    var m = Matrix()
    val ballPainter= Paint().apply {
        shader=
                RadialGradient(0f,0f,radius,
                        Color.WHITE, Color.BLACK, Shader.TileMode.CLAMP)
    }
    val linesPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 18f
    }

    val scorePaint = Paint().apply {
        color = Color.BLACK
        textSize = 100F
        strokeWidth=20F

    }

    init {
        setOnTouchListener(this)

    }
    override fun onDraw(cv: Canvas?) {
        super.onDraw(cv)

        val now = System.currentTimeMillis()
        if(count==0) dx = width/2F
        val scorePosX = width.toFloat() - (width.toFloat()/10)
        val scorePosY = height.toFloat()/6
        cv?.drawText(scoreBallGame.toString(), scorePosX, scorePosY, scorePaint)

        val dt = now-current
        current=now


        //Update ball position

        bx+=vx* dt/1000
        by+=vy* dt/1000

        var offLinex = (width.toFloat()/10)
        val offLiney = (height.toFloat()/7)

        //Ball hits and edge?
        if ((bx<radius) and (vx<0)) vx*=-1f //hits left edge and moves towards left
        if ((bx>width-radius) and (vx>0)) vx*=-1f //right edge while moving right
        if ((by>height-radius)  and (vy>0)){
            vy*=-1
            scoreBallGame=-1
            radius = 100f
            cv?.drawText(scoreBallGame.toString(), scorePosX, scorePosY, scorePaint)
        } //bottom edge while moving downward

        if ((by>height-radius)  and (vy<0)){
            scoreBallGame=-1
            radius = 100f
            cv?.drawText(scoreBallGame.toString(), scorePosX, scorePosY, scorePaint)
        }
        if ((by<radius)  ) {vy*=-1} //top while moving up

        if( (by > height.toFloat()-offLiney- radius) and (by < height.toFloat()-offLiney) and (bx>dx-offLinex) and (bx<dx+offLinex) ){
            if(vy>0) {
                scoreBallGame += 1
                if((scoreBallGame>0) && scoreBallGame%10==0 && scoreBallGame<41 ){
                    radius -= radius/4
                    offLinex-= offLinex/4
                }
                cv?.drawText(scoreBallGame.toString(), scorePosX, scorePosY, scorePaint)
            }

            vy*=-1
        }

        //Ball was already touched?
        /*if ( !touched and                   //was not touched
                (barriery>=by+radius) and   //now the y finger is below the ball..
                (barriery<=by+howclose*radius) and // and no more than howclose below
                (Math.abs(barrierx - bx) <radius) ) // and x finger coordinate close to the ball
        {
            if (vy>0) { //the ball was falling
                vy*=-1
                touched=true
            }
        }*/


        val offx = 2*bx/width-1
        val offy = 2*by/height-1

        m.setTranslate(bx+0.3f*radius*offx,by+0.3f*radius*offy)
        ballPainter.shader.setLocalMatrix(m)
        cv?.drawCircle(bx,by,radius,ballPainter)


        cv?.drawLine(dx- offLinex,height.toFloat()-offLiney,dx+ offLinex, height.toFloat()-offLiney,linesPaint)
        invalidate()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        when(event?.action){
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE ->{
                //barrierx=event?.x
                //barriery=event?.y
                dx=event?.x
                count=1
                touch=1
            }
          /*  MotionEvent.ACTION_UP -> {
               // barrierx=0f;barriery=0f
            }*/
        }

        return true
    }

}