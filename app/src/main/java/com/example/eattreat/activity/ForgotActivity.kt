package com.example.eattreat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.eattreat.R
import org.json.JSONObject
import java.lang.Exception

class ForgotActivity : AppCompatActivity() {

    lateinit var etNumber: EditText
    lateinit var etEmail: EditText
    lateinit var btnSubmit: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        etNumber = findViewById(R.id.etNumber)
        etEmail = findViewById(R.id.etEmail)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {


            val queue = Volley.newRequestQueue(this@ForgotActivity)
            val url = "http://13.235.250.119/v2/forgot_password/fetch_result"
            val jsonParams = JSONObject()

            jsonParams.put("mobile_number", etNumber.text.toString())
            jsonParams.put("email", etEmail.text.toString())

//            var args =  Bundle();
//            args.putString("number" , etNumber.text.toString())



            val jsonRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        val firstTry = data.getBoolean("first_try")

                        if (success) {
                            val intent = Intent(this@ForgotActivity, OtpActivity::class.java)
                            intent.putExtra("mobile_number",etNumber.text.toString())
                            startActivity(intent)
                            finish()

                        } else {
                            Toast.makeText(
                                this@ForgotActivity, "some unexpected error  occured $it",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } catch (e: Exception) {
                        Toast.makeText(
                            this@ForgotActivity, "some unexpected error  occured $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(
                        this@ForgotActivity, "Volley Error Occurred $it",
                        Toast.LENGTH_LONG
                    ).show()

                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "d8fe3166127045"
                        return headers
                    }
                }

            queue.add(jsonRequest)
        }
    }
}
