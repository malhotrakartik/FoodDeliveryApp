package com.example.eattreat.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eattreat.R
import java.lang.Exception

class WelcomeActivity : AppCompatActivity() {

    lateinit var sharedPreferences : SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)


        val background = object : Thread(){
            override fun run() {
                try {
                    Thread.sleep(1000)
                    val intent = Intent(this@WelcomeActivity , LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }catch (e : Exception){
                    e.printStackTrace()

                }
            }
        }
        background.start()



    }
}
