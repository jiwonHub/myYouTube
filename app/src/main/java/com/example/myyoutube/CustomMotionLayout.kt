package com.example.myyoutube

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout

class CustomMotionLayout(context: Context, attributeSet: AttributeSet? = null): MotionLayout(context, attributeSet) {
    private var motionTouchStarted = false // 정확한 곳에 터치가 되었는지 체크하는 불형 변수
    private val mainContainerLayout by lazy {
        findViewById<View>(R.id.mainContainerLayout)
    }
    private val hitRect = Rect()

    init {
        setTransitionListener(object : TransitionListener{
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {}

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {}

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                motionTouchStarted = false
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {}

        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked){
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> { // up 은 손가락은 뗀것, cancel 은 취소한것
                motionTouchStarted = false
                return super.onTouchEvent(event)
            }
        }

        if(!motionTouchStarted){ // 터치가 되었는가
            mainContainerLayout.getHitRect(hitRect)
            motionTouchStarted = hitRect.contains(event.x.toInt(), event.y.toInt())
            // x,y 좌표가 hitRect 안에 포함이 되는가? 포함이 된다면 motionTouchStarted 를 true 로 변환
        }
        return super.onTouchEvent(event) && motionTouchStarted
    }

    private val gestureListener by lazy {
        object: GestureDetector.SimpleOnGestureListener(){
            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                mainContainerLayout.getHitRect(hitRect)
                return hitRect.contains(e1.x.toInt(), e1.y.toInt()) // motionEvent 에 포함이 된건지 확인
            }
        }
    }

    private val gestureDetector by lazy {
        GestureDetector(context, gestureListener)
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean { // 화면마다 터치기능을 가로채기 위함
        return gestureDetector.onTouchEvent(event!!)
    }
}