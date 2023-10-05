package com.example.viewactivity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var diceImage: ImageView

    private val imageList = listOf(
        R.drawable.dice_1,
        R.drawable.dice_2,
        R.drawable.dice_3,
        R.drawable.dice_4,
        R.drawable.dice_5,
        R.drawable.dice_6
    )

    private var currentIndex = 0
    private var isAnimationRunning = true
    private var animationDuration = 2000L // Thời gian chạy animation (3 giây)
    private var imageChangeInterval = 300L // Thời gian thay đổi ảnh (0.3 giây)
    private var currentTime = 0L
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rollButton: Button = findViewById(R.id.rollBtn)
        diceImage = findViewById(R.id.imageView)
        diceImage.setImageResource(imageList[0])

        rollButton.setOnClickListener { startImageAnimation() }

    }

    private fun rollDice() {
        val dice = Dice(6)
        val diceRoll = dice.roll()
        diceImage.setImageResource(imageList[diceRoll -1 ])
        currentIndex = diceRoll - 1;
        diceImage.contentDescription = diceRoll.toString()
        showCenteredDialog(this, "Bạn đã gieo được $diceRoll")


    }

    private fun getRandom(min: Int, max: Int): Int {
        return (min..max).random()
    }

    fun showCenteredDialog(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setCancelable(true)

        val dialog = builder.create()
        val window = dialog.window
        val layoutParams = window?.attributes
        layoutParams?.gravity = android.view.Gravity.CENTER
        window?.attributes = layoutParams

        dialog.show()
    }

    private fun startImageAnimation() {
        val imageChangeRunnable = object : Runnable {
            override fun run() {
                if (isAnimationRunning) {
                    val randomIndex = (0 until imageList.size).random()
                    diceImage.setImageResource(imageList[randomIndex])
                    handler.postDelayed(this, imageChangeInterval)
                    currentTime += imageChangeInterval

                    if (currentTime >= animationDuration) {
                        handler.postDelayed(this, 500L)
                        isAnimationRunning = false
                        rollDice()
                    }
                }
            }
        }

        currentTime = 0L
        isAnimationRunning = true
        handler.postDelayed(imageChangeRunnable, imageChangeInterval)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

}

class Dice(private val numSides: Int) {

    fun roll(): Int {
        return (1..numSides).random()
    }
}