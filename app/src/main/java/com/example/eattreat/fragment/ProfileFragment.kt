package com.example.eattreat.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.eattreat.R

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {


    lateinit var imgProfileImage : ImageView
    lateinit var txtProfileName : TextView
    lateinit var txtProfileNumber : TextView
    lateinit var txtProfileEmail : TextView
    lateinit var txtProfileAddress : TextView



    lateinit var sharedPreferences : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
      val view =    inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPreferences = context!!.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        imgProfileImage = view.findViewById(R.id.imgProfileImage)
        txtProfileName = view.findViewById(R.id.txtProfileName)
        txtProfileNumber = view.findViewById(R.id.txtProfileNumber)
        txtProfileEmail = view.findViewById(R.id.txtProfileEmail)
        txtProfileAddress = view.findViewById(R.id.txtProfileAddress)

        txtProfileName.text = sharedPreferences.getString("user_name","Your Name").toString()
        txtProfileNumber.text = sharedPreferences.getString("user_mobile_number" , "your number").toString()
        txtProfileEmail.text = sharedPreferences.getString("user_email","Your email").toString()
        txtProfileAddress.text = sharedPreferences.getString("user_address" , "Your address").toString()





          return view
    }



}
