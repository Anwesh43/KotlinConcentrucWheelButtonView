package com.example.anweshmishra.kotlinconcentricwheelbuttonview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.example.concentricwheelbuttonview.ConcentricWheelButtonView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view : ConcentricWheelButtonView = ConcentricWheelButtonView.create(this)
        fullScreen()
        view.addOnAnimationListener({
            Toast.makeText(this, "animation complete", Toast.LENGTH_SHORT).show()
        }, {
            Toast.makeText(this, "animation complete", Toast.LENGTH_SHORT).show()
        })
    }
}

fun MainActivity.fullScreen() {
    supportActionBar?.hide()
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}
