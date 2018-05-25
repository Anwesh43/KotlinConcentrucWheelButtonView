package com.example.anweshmishra.kotlinconcentricwheelbuttonview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.concentricwheelbuttonview.ConcentricWheelButtonView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ConcentricWheelButtonView.create(this)
    }
}
