package com.example.eattreat.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.eattreat.R

/**
 * A simple [Fragment] subclass.
 */
class FaqFragment : Fragment() {

    lateinit var txtQstn1 : TextView
    lateinit var txtQstn2 : TextView
    lateinit var txtQstn3 : TextView
    lateinit var txtQstn4 : TextView
    lateinit var txtAns1 : TextView
    lateinit var txtAns2 : TextView
    lateinit var txtAns3 : TextView
    lateinit var txtAns4 : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_faq, container, false)

        txtQstn1 = view.findViewById(R.id.txtQstn1)
        txtQstn2 = view.findViewById(R.id.txtQstn2)
        txtQstn3 = view.findViewById(R.id.txtQstn3)
        txtQstn4 = view.findViewById(R.id.txtQstn4)
        txtAns1 = view.findViewById(R.id.txtAns1)
        txtAns2 = view.findViewById(R.id.txtAns2)
        txtAns3 = view.findViewById(R.id.txtAns3)
        txtAns4 = view.findViewById(R.id.txtAns4)

        txtQstn1.text ="Ques1. Why would I want to use it?"
        txtAns1.text= "Ans1. Here at EatTreat, we prominently display blogger reviews. If you use Spoonbacks, EatTreat users will see an excerpt from your review, " +
                "and a link to click through to your blog. In short, you get exposure and traffic to your blog for free."

        txtQstn2.text = "Ques2. Can I place order on call?"
        txtAns2.text = "Ans2. Sorry, we don’t accept orders on call. However you can call us on +91-7259085922 for any help related to placing order."

        txtQstn3.text = "Ques3. How do I edit my order after placing it?"
        txtAns3.text = "Ans3. Please call us on +91-7259085922 or write email to packaging@eattreat.in mentioning your Order #."

        txtQstn4.text = "Ques4. How do I know my request has been accepted?"
        txtAns4.text = "Ans4. The partnering recruitment agency will send the restaurant a confirmation email once they have accepted its request. Soon after the restaurant places a request, it will receive communication to approve sample profile(s) and an interview will be scheduled with the candidate at a mutually convenient time. Once the " +
                "interview(s) has been scheduled, the restaurant will receive another email with all the interview details"


        return view
    }

}
