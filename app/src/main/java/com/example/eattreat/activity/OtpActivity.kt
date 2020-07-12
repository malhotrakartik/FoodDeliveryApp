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

class OtpActivity : AppCompatActivity() {

    lateinit var etOtp : EditText
    lateinit var etNewPassword : EditText
    lateinit var etConfirmPassword : EditText
    lateinit var btnSubmit  : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        etOtp = findViewById(R.id.etOtp)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnSubmit = findViewById(R.id.btnSubmit)
        val mobileNumber =intent.getStringExtra("mobile_number")

        btnSubmit.setOnClickListener {


            val queue = Volley.newRequestQueue(this@OtpActivity)
            val url = "http://13.235.250.119/v2/reset_password/fetch_result "
            val jsonParams = JSONObject()



            jsonParams.put("mobile_number", mobileNumber)
            jsonParams.put("password", etNewPassword.text.toString())
            jsonParams.put("otp",etOtp.text.toString())

            val jsonRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        val successMessage = data.getString("successMessage")

                        if (success) {
                            Toast.makeText(this@OtpActivity , successMessage,Toast.LENGTH_LONG).show()
                            val intent = Intent(this@OtpActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()

                        } else {
                            Toast.makeText(
                                this@OtpActivity, "some unexpected error  occured $it",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } catch (e: Exception) {
                        Toast.makeText(
                            this@OtpActivity, "some unexpected error  occured $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(
                        this@OtpActivity, "Volley Error Occurred $it",
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
