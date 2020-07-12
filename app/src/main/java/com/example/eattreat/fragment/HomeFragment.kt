package com.example.eattreat.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.eattreat.adapter.HomeRecyclerAdapter
import com.example.eattreat.R
import com.example.eattreat.database.RestaurantDatabase1
import com.example.eattreat.database.RestaurantEntity
import com.example.eattreat.model.Restaurant
import com.example.eattreat.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    lateinit var recyclerHome : RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
//    lateinit var searchItem : androidx.appcompat.widget.SearchView
    lateinit var recyclerAdapter: HomeRecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar : ProgressBar


    var isFavourite : Boolean = false


    val restaurantInfoList = arrayListOf<Restaurant>()

    val ratingComparator = Comparator<Restaurant>{restaurant1, restaurant2 ->
        if(restaurant1.restaurantRating.compareTo(restaurant2.restaurantRating , true)==0){
            restaurant1.restaurantName.compareTo(restaurant2.restaurantName , true)
        }else{
            restaurant1.restaurantRating.compareTo(restaurant2.restaurantRating , true)
        }

    }

    val costComparator = Comparator<Restaurant>{restaurant1, restaurant2 ->
        if(restaurant1.restaurantPrice.toString().compareTo(restaurant2.restaurantPrice.toString() , true)==0){
            restaurant1.restaurantName.compareTo(restaurant2.restaurantName , true)
        }else{
            restaurant1.restaurantPrice.toString().compareTo(restaurant2.restaurantPrice.toString() , true)
        }

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view :View = inflater.inflate(R.layout.fragment_home, container, false)
        setHasOptionsMenu(true)

        recyclerHome = view.findViewById(R.id.recyclerHome)
        layoutManager = LinearLayoutManager(activity)
//        searchItem = view.findViewById(R.id.searchItem)

        progressBar = view.findViewById(R.id.progressBar)
        progressLayout = view.findViewById(R.id.progressLayout)

        progressLayout.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(activity as Context)
        val url = " http://13.235.250.119/v2/restaurants/fetch_result/ "
        if(ConnectionManager().checkConnectivity(activity as Context)){

            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET,url,null, Response.Listener {

                try {
                    println("Response is $it")

                    progressLayout.visibility = View.GONE
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if(success){
                        val resArray  = data.getJSONArray("data")
                        for(i in 0 until resArray.length()){
                            val restaurantJsonObject = resArray.getJSONObject(i)
                            val restaurantObject = Restaurant(
                                restaurantJsonObject.getString("id").toInt(),
                                restaurantJsonObject.getString("name"),
                                restaurantJsonObject.getString("rating"),
                                restaurantJsonObject.getString("cost_for_one").toInt(),

                                restaurantJsonObject.getString("image_url")
                            )















                            restaurantInfoList.add(restaurantObject)








                            recyclerAdapter =
                                HomeRecyclerAdapter(
                                    activity as Context,
                                    restaurantInfoList,
                                    object :
                                        HomeRecyclerAdapter.OnItemClickListener {
                                        override fun onAddItemClick(restaurant: Restaurant) {
                                            println(restaurant)
                                            println("restaurant id is")
                                            println(restaurant.restaurantId)


                                            val restaurantEntity = RestaurantEntity(
                                                restaurant.restaurantId.toString(),
                                                restaurant.restaurantName,
                                                restaurant.restaurantRating,
                                                restaurant.restaurantPrice.toString()
//                                            restaurant.restaurantImage
                                            )



                                            println(restaurantEntity)

//
                                            val async =
                                                DBAsyncTask(
                                                    activity as Context,
                                                    restaurantEntity,
                                                    mode = 2
                                                ).execute()

                                        }


//
//
//


                                        override fun onRemoveItemClick(restaurant: Restaurant) {

                                            val restaurantEntity = RestaurantEntity(
                                                restaurant.restaurantId.toString(),
                                                restaurant.restaurantName,
                                                restaurant.restaurantRating,
                                                restaurant.restaurantPrice.toString()
//                                            restaurant.restaurantImage
                                            )


                                            val async =
                                                DBAsyncTask(
                                                    activity as Context,
                                                    restaurantEntity,
                                                    mode = 3
                                                ).execute()
//                                        val result = async.get()
//                                        if(result){
//
//
//                                            Toast.makeText(context,"Removed from favourites" , Toast.LENGTH_LONG).show()
//                                        }else{
//                                            Toast.makeText(context,"Some Error occurred 1" , Toast.LENGTH_LONG).show()
//                                        }
                                        }


                                    }

                                )

                            recyclerHome.adapter = recyclerAdapter
                            recyclerHome.layoutManager = layoutManager




                    }

                    }else{
                        Toast.makeText(activity as Context,"Some Error Occured!", Toast.LENGTH_LONG).show()
                    }

                }catch (e : JSONException){

                    Toast.makeText(activity as Context,"Some Unexpected Error Occured! $it", Toast.LENGTH_LONG).show()
                    println("$it")
                }


            }, Response.ErrorListener {
                if(activity != null){
                    Toast.makeText(activity as Context,"Volley Error Occurred" , Toast.LENGTH_LONG).show()}

            })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String , String>()

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
//            jsonObjectRequest.retryPolicy = policy
            queue.add(jsonObjectRequest)


        }else
        {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("success")
            dialog.setMessage("Internet NOT Connection Found")
            dialog.setPositiveButton("Open Settings"){
                    text,listener ->

                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()



            }
            dialog.setNegativeButton("Cancel"){
                    text,listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }



        return view



    }


    class DBAsyncTask(val context: Context, val restaurantEntity: RestaurantEntity ,val mode : Int) : AsyncTask<Void, Void, Boolean>(){

        /*mode 1 = check db if book is fav or not
         mode 2 = add to fav
         mode 3 = remove from fav*/

        val db = Room.databaseBuilder(context , RestaurantDatabase1::class.java, "restaurants-db").build()
        override fun doInBackground(vararg p0: Void?): Boolean {
            when(mode){
                1 ->{

                    val restaurant : RestaurantEntity? =db.restaurantDao().getRestaurantById(restaurantEntity.restaurantId)
                    db.close()
                    return restaurant!= null        //will return false if no book is present

                }

                2->{
                    db.restaurantDao().insertRestaurant(restaurantEntity)
                    db.close()
                    return true

                }
                3->{
                    db.restaurantDao().deleteRestaurant(restaurantEntity)
                    db.close()
                    return true

                }


            }
            return false
        }

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_sorting, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item?.itemId
        if(id == R.id.action_sort){
            Collections.sort(restaurantInfoList , ratingComparator)
            restaurantInfoList.reverse()
        }
        else if(id == R.id.action_sort2){
            Collections.sort(restaurantInfoList , costComparator)
        }

        else if (id == R.id.cost_sort){
            Collections.sort(restaurantInfoList , costComparator)
            restaurantInfoList.reverse()
        }

        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }






}


