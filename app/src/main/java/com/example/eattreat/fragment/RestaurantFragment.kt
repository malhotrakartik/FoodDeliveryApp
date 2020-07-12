package com.example.eattreat.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.eattreat.R
import com.example.eattreat.adapter.RestaurantRecyclerAdapter
import com.example.eattreat.activity.CartActivity
import com.example.eattreat.database.CartEntity
import com.example.eattreat.database.OrderDatabase
import com.example.eattreat.model.Menu
import com.example.eattreat.util.ConnectionManager
import org.json.JSONException

/**
 * A simple [Fragment] subclass.
 */
class RestaurantFragment : Fragment()  {




    lateinit var recyclerRestaurant: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: RestaurantRecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    val menuList = arrayListOf<Menu>()
    lateinit var goToCart: Button



    companion object {

        var resId: Int? = 0
        var resName: String? = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_restaurant, container, false)
        recyclerRestaurant = view.findViewById(R.id.recyclerRestaurant)
        layoutManager = LinearLayoutManager(activity)
//        appToolbar = view.findViewById(R.id.appToolbar)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
        goToCart = view.findViewById(R.id.goToCart)










        resId = arguments?.getInt("id", 0)
        setHasOptionsMenu(true)
        resName = arguments?.getString("name")

        goToCart.visibility = View.GONE
        goToCart.setOnClickListener {
            proceedToCart()
        }



//        setUpToolBar()
        setUpRestaurantMenu(view)
        return view
    }


    private fun setUpRestaurantMenu(view: View) {
        var str: String = resId.toString()

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$str"


        if (ConnectionManager().checkConnectivity(activity as Context)) {

            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                    try {
                        println("Response is $it")

                        progressLayout.visibility = View.GONE
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            val resArray = data.getJSONArray("data")
                            for (i in 0 until resArray.length()) {
                                val menuJsonObject = resArray.getJSONObject(i)
                                val menuObject = Menu(
                                    menuJsonObject.getString("id").toInt(),
                                    menuJsonObject.getString("name"),
                                    menuJsonObject.getString("cost_for_one").toInt(),
                                    menuJsonObject.getString("restaurant_id")
                                )

                                menuList.add(menuObject)

                                recyclerAdapter =
                                    RestaurantRecyclerAdapter(
                                        activity as Context,
                                        menuList,
                                        object :
                                            RestaurantRecyclerAdapter.OnItemClickListener {
                                            override fun onAddItemClick(menu: Menu) {


                                                goToCart.visibility = View.VISIBLE

                                                val cartEntity = CartEntity(
                                                    menu.MenuId.toString(),

                                                    menu.MenuName,
                                                    menu.MenuCost_for_one.toString()

                                                )

                                                val async =
                                                    DBAsyncTask(
                                                        activity as Context,
                                                        cartEntity,
                                                        mode = 2
                                                    ).execute()

//                                                val result = async.get()
//                                                if (result) {
//                                                    Toast.makeText(
//                                                        context,
//                                                        "Added to Cart successfully",
//                                                        Toast.LENGTH_LONG
//                                                    ).show()
//                                                } else {
//                                                    Toast.makeText(
//                                                        context,
//                                                        "Some Error occurred",
//                                                        Toast.LENGTH_LONG
//                                                    ).show()
//                                                }
                                            }


                                            override fun onRemoveItemClick(menu: Menu) {

                                                val cartEntity = CartEntity(
                                                    menu.MenuId.toString(),

                                                    menu.MenuName,
                                                    menu.MenuCost_for_one.toString()

                                                )


                                                val async =
                                                    DBAsyncTask(
                                                        activity as Context,
                                                        cartEntity,
                                                        mode = 3
                                                    ).execute()


                                            }
                                        })
                                recyclerRestaurant.adapter = recyclerAdapter
                                recyclerRestaurant.layoutManager = layoutManager


                            }




                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Some Error Occured!",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } catch (e: JSONException) {

                        Toast.makeText(
                            activity as Context,
                            "Some Unexpected Error Occured! $it",
                            Toast.LENGTH_LONG
                        ).show()
                        println("$it")
                    }


                }, Response.ErrorListener {
                    if (activity != null) {
                        Toast.makeText(
                            activity as Context,
                            "Volley Error Occurred",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()

                        headers["Content-type"] = "application/json"
                        headers["token"] = "d8fe3166127045"
                        return headers


                    }
                }

            queue.add(jsonObjectRequest)


        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("success")
            dialog.setMessage("Internet NOT Connection Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->

                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()


            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }


    }


    class DBAsyncTask(val context: Context, val cartEntity : CartEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        /*mode 1 = check db if book is fav or not
         mode 2 = add to fav
         mode 3 = remove from fav*/

        val db = Room.databaseBuilder(context, OrderDatabase::class.java, "order-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {

                    val cart: CartEntity? = db.cartDao().getCartById(cartEntity.DishId)
                    db.close()
                    return cart!= null        //will return false if no book is present

                }

                2 -> {
                    db.cartDao().insertCart(cartEntity)
                    db.close()
                    return true

                }
                3 -> {
                    db.cartDao().deleteCart(cartEntity)
                    db.close()
                    return true

                }




            }
            return false
        }


    }

    class ItemsOfCart(
        context: Context,
        val dishId: String

    ) : AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, OrderDatabase::class.java, "order-db").build()


        override fun doInBackground(vararg params: Void?): Boolean {


            val cart : CartEntity? =db.cartDao().getCartById(dishId)
            db.close()
            return cart!= null

        }

    }



    private fun proceedToCart() {
        val intent = Intent(activity as Context, CartActivity::class.java)
        startActivity(intent)
    }

}








