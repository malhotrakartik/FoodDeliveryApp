package com.example.eattreat.activity


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.eattreat.R
import com.example.eattreat.util.ConnectionManager
import org.json.JSONObject




class LoginActivity : AppCompatActivity() {
    lateinit var etNumber : TextView
    lateinit var etPassword : TextView
    lateinit var txtSignUp : TextView
    lateinit var txtForgot : TextView
    lateinit var btnLogIn : Button


    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)



        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()

        }

        etNumber = findViewById(R.id.etNumber)
        etPassword = findViewById(R.id.etPassword)
        txtSignUp = findViewById(R.id.txtSignUp)
        txtForgot = findViewById(R.id.txtForgot)
        btnLogIn = findViewById(R.id.btnLogIn)
        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()



        btnLogIn.setOnClickListener {


            val queue = Volley.newRequestQueue(this@LoginActivity)
            val url = "http://13.235.250.119/v2/login/fetch_result"

            if (ConnectionManager().checkConnectivity(this@LoginActivity)) {
                val jsonParams = JSONObject()

                val number = etNumber.text.toString()
                val password = etPassword.text.toString()

                jsonParams.put("mobile_number", number)
                jsonParams.put("password", password)


                val jsonRequest =
                    object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

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
                                    .putString("user_address", response.getString("address"))
                                    .apply()



                                savePreferences()
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)


                            } else {
                                Toast.makeText(
                                    this@LoginActivity, "some unexpected error 1 occured $it",
                                    Toast.LENGTH_LONG
                                ).show()

                            }

                        } catch (e: Exception) {
                            Toast.makeText(
                                this@LoginActivity, "some unexpected error 2 occured $it",
                                Toast.LENGTH_LONG
                            ).show()

                        }

                    }, Response.ErrorListener {
                        Toast.makeText(
                            this@LoginActivity, "Volley error  occured $it",
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
//            val socketTimeout = 20000 // 30 seconds. You can change it
//
//            val policy: RetryPolicy = DefaultRetryPolicy(
//                socketTimeout,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//            )
//
//            jsonRequest.retryPolicy = policy
                queue.add(jsonRequest)
            } else {
                val dialog = AlertDialog.Builder(this@LoginActivity)
                dialog.setTitle("success")
                dialog.setMessage("Internet NOT Connection Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->

                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()


                }
                dialog.setNegativeButton("Cancel") { text, listener ->
                    ActivityCompat.finishAffinity(this@LoginActivity)
                }
                dialog.create()
                dialog.show()
            }




        }
        txtSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        txtForgot.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotActivity::class.java)
            startActivity(intent)
        }

    }
    fun savePreferences(){
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()

    }





}
