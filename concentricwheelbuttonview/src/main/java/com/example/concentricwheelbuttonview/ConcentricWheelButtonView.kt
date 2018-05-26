package com.example.concentricwheelbuttonview

/**
 * Created by anweshmishra on 26/05/18.
 */

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.MotionEvent
import android.graphics.*

class ConcentricWheelButtonView (ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State (var prevScale : Float = 0f, var dir : Float = 0f, var j : Int = 0, var delay : Int = 0) {

        val scales : Array<Float> = arrayOf(0f, 0f, 0f, 0f)

        val MAX_DELAY : Int = 10

        fun update(stopcb : (Float) -> Unit) {
            if (delay == 0) {
                scales[j] += 0.1f * dir
                if (Math.abs(scales[j] - prevScale) > 1) {
                    scales[j] = prevScale + dir
                    delay++
                }
            }
            else {
                delay++
                if (delay == MAX_DELAY) {
                    j += dir.toInt()
                    delay = 0
                    if (j == -1 || j == scales.size) {
                        j -= dir.toInt()
                        dir = 0f
                        prevScale = scales[j]
                        stopcb(prevScale)
                    }
                }
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class ConcentricWheelButton(var i : Int, val state : State = State()) {

        fun draw(canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            val r : Float = Math.min(w, h) * 0.4f
            val r1 : Float = Math.min(w, h) * 0.3f
            val r2 : Float = Math.min(w, h)/30
            paint.color = Color.WHITE
            paint.style = Paint.Style.STROKE
            paint.strokeCap = Paint.Cap.ROUND
            canvas.save()
            canvas.translate(w/2, h/2)
            paint.strokeWidth = Math.min(w, h) / 60
            canvas.drawArc(RectF(-r, -r, r, r), 0f, 360f * state.scales[0], false, paint)
            paint.strokeWidth = Math.min(w, h) / 10
            canvas.drawArc(RectF(-r1, -r1, r1, r1), 0f, 360f * state.scales[1], false, paint)
            paint.strokeWidth = Math.min(w, h) / 60
            for (i in 0..2) {
                canvas.save()
                canvas.rotate(i * 120f + 120f * state.scales[3])
                canvas.drawCircle(0f, -r1/4, r2 * state.scales[2], paint)
                canvas.restore()
            }
            canvas.restore()
        }

        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }

    data class Renderer(var view : ConcentricWheelButtonView) {

        private val animator : Animator = Animator(view)

        private val wheelButton : ConcentricWheelButton = ConcentricWheelButton(0)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#1A237E"))
            wheelButton.draw(canvas, paint)
            animator.animate {
                wheelButton.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            wheelButton.startUpdating {
                animator.start()
            }
        }
    }

    companion object {
        fun create(activity : Activity) : ConcentricWheelButtonView {
            val view : ConcentricWheelButtonView = ConcentricWheelButtonView(activity)
            activity.setContentView(view)
            return view
        }
    }
}