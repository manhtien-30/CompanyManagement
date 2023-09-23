import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import kotlin.math.sqrt


//mindistance and max distance is pixel
open class OnSwipeTouchListener(
    var context: Context,
    var mindistance: Float = 10F,
    var maxdistance: Float = 20F,
) :
    View.OnTouchListener {
    enum class Direction {
        up, down, left, right
    }

    private val gestureDetector: GestureDetector
    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(motionEvent)
    }

    fun onSwipe(direction: Direction?): Boolean {
        return false
    }

    open fun onSwipeRight() {}
    open fun onSwipeLeft() {}
    open fun onSwipeUp() {}
    open fun onSwipeDown() {}
    open fun onClick() {}
    open fun onDoubleClick() {}
    open fun onLongClick() {}

    fun getAngle(x1: Float, y1: Float, x2: Float, y2: Float): Double {
        val rad = Math.atan2((y1 - y2).toDouble(), (x2 - x1).toDouble()) + Math.PI
        return (rad * 180 / Math.PI + 180) % 360
    }

    private inner class GestureListener(c: Context) : SimpleOnGestureListener() {
        var context: Context

        init {
            this.context = c
        }

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }


        override fun onSingleTapUp(e: MotionEvent): Boolean {
            onClick()
            return super.onSingleTapUp(e)
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            onDoubleClick()
            return super.onDoubleTap(e)
        }

        override fun onLongPress(e: MotionEvent) {
            onLongClick()
            super.onLongPress(e)
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float,
        ): Boolean {
            val x1 = e1.x
            val y1 = e1.y
            val x2 = e2.x
            val y2 = e2.y
            val distance = getDistance(x1, y1, x2, y2);
            val direction = getDirection(x1, y1, x2, y2)
            sendEvent(distance, direction);
            return false;
        }

        private fun sendEvent(distance: Float, direction: Direction) {
            Log.d("mindistance",mindistance.toString()  )
            Log.d("maxdistance",maxdistance.toString()  )
            if (distance > mindistance && distance < maxdistance) {

                onSwipe(direction)
                when (direction) {
                    Direction.up -> onSwipeUp()
                    Direction.down -> onSwipeDown()
                    Direction.right -> onSwipeRight()
                    Direction.left -> onSwipeLeft()
                }
            }
        }

        private fun getDirection(x1: Float, y1: Float, x2: Float, y2: Float): Direction {
            val angle = getAngle(x1, y1, x2, y2)
            return getAngleAngle(angle)
        }

        private fun getAngleAngle(angle: Double): Direction {
            return if (inRange(angle, 45f, 135f)) {
                Direction.up
            } else if (inRange(angle, 0f, 45f) || inRange(angle, 315f, 360f)) {
                Direction.right
            } else if (inRange(angle, 135f, 225f)) {
                Direction.left
            } else {
                Direction.down
            }
        }

        fun getDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
            return sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)))

        }

        private fun inRange(angle: Double, init: Float, end: Float): Boolean {
            return angle >= init && angle < end
        }

    }

    init {
        gestureDetector = GestureDetector(context, GestureListener(context))
    }
}