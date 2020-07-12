package com.example.eattreat.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.eattreat.R
import com.example.eattreat.util.ConnectionManager
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    lateinit var etName : EditText
    lateinit var etEmail : EditText
    lateinit var etNumber : EditText
    lateinit var etAddress : EditText
    lateinit var etPassword : EditText
    lateinit var etConfirmPassword : EditText
    lateinit var btnRegister : Button

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etNumber = findViewById(R.id.etNumber)
        etAddress = findViewById(R.id.etAddress)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)

        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)





            btnRegister.setOnClickListener {

                val password = etPassword.text.toString()
                val confirmPassword = etConfirmPassword.text.toString()

                if(password == confirmPassword) {

                val number = etNumber.text.toString()
                val name = etName.text.toString()
                val address = etAddress.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()


                val queue = Volley.newRequestQueue(this@RegisterActivity)
                val url = "http://13.235.250.119/v2/register/fetch_result"
                    if (ConnectionManager().checkConnectivity(this@RegisterActivity)) {

                val jsonParams = JSONObject()
                jsonParams.put("name", name)
                jsonParams.put("mobile_number", number)
                jsonParams.put("password", password)
                jsonParams.put("address", address)
                jsonParams.put("email", email)


                val jsonRequest = object :
                    JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                        try {
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if (success) {

                                val response = data.getJSONObject("data")
                                sharedPreferences.edit()
                                    .putString("user_id", response.getString("user_id"))
                                    .apply()

                                sharedPreferences.edit()
                                    .putString("user_name", response.getString("name"))
                                    .apply()
                                sharedPreferences.edit()
                                    .putString("user_email", response.getString("email")).apply()
                                sharedPreferences.edit()
                                    .putString(
                                        "user_mobile_number",
                                        response.getString("mobile_number")
                                    )
                                    .apply()
                                sharedPreferences.edit()
                                    .putString("user_address", response.getString("address")).apply()

                                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()

                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "some unexpected error 1 occured $it",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "some unexpected error 2 occured",
                                Toast.LENGTH_LONG
                            ).show()


                        }

                    }, Response.ErrorListener {
                        Toast.makeText(
                            this@RegisterActivity,
                            "VOLLEY error occured $it",
                            Toast.LENGTH_LONG
                        ).show()
                        println("$it")


                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "d8fe3166127045"
                        return headers

                    }

                }

//                val socketTimeout = 20000 // 30 seconds. You can change it
//
//                val policy: RetryPolicy = DefaultRetryPolicy(
//                    socketTimeout,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//                )
//
//                jsonRequest.retryPolicy = policy

                queue.add(jsonRequest)
                    } else {
                        val dialog = AlertDialog.Builder(this@RegisterActivity)
                        dialog.setTitle("success")
                        dialog.setMessage("Internet NOT Connection Found")
                        dialog.setPositiveButton("Open Settings") { text, listener ->

                            val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                            startActivity(settingsIntent)
                            finish()


                        }
                        dialog.setNegativeButton("Cancel") { text, listener ->
                            ActivityCompat.finishAffinity(this@RegisterActivity)
                        }
                        dialog.create()
                        dialog.show()
                    }

            }
                else{
                    Toast.makeText(this@RegisterActivity , "password does not match confirm password" , Toast.LENGTH_LONG).show()
                }
        }


    }


}
