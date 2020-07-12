package com.example.eattreat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.eattreat.R

class OrderPlacedActivity : AppCompatActivity() {

    lateinit var btnOrderPlaced : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_placed)

        btnOrderPlaced = findViewById(R.id.btnOrderPlaced)
        btnOrderPlaced.setOnClickListener {




            val intent = Intent(this@OrderPlacedActivity , MainActivity::class.java)
            startActivity(intent)
            finish()




        }


    }

}
