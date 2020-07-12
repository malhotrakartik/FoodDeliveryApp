package com.example.eattreat.fragment


import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.eattreat.adapter.FavouriteRecyclerAdapter
import com.example.eattreat.R
import com.example.eattreat.database.RestaurantDatabase1
import com.example.eattreat.database.RestaurantEntity


/**
 * A simple [Fragment] subclass.
 */
class FavouriteFragment : Fragment() {

    lateinit var recyclerFavourite: RecyclerView
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar : ProgressBar
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavouriteRecyclerAdapter


    var dbRestaurantList = listOf<RestaurantEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite2, container, false)

        recyclerFavourite =view.findViewById(R.id.recyclerFavourite)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout = view.findViewById(R.id.progressLayout)
        layoutManager = LinearLayoutManager(activity)

//        layoutManager = GridLayoutManager(activity as Context , 2)
        dbRestaurantList = RetrieveFavourites(
            activity as Context
        ).execute().get()



        if(activity!=null){
            progressLayout.visibility =View.GONE
            recyclerAdapter =
                FavouriteRecyclerAdapter(
                    activity as Context,
                    dbRestaurantList,
                    object :
                        FavouriteRecyclerAdapter.OnRestaurantClickListener {


                        override fun onRemoveRestaurantClick(restaurantId: String) {

                            ClearDBAsync(
                                activity as Context,
                                restaurantId
                            ).execute()


                        }

                    })

            recyclerFavourite.adapter = recyclerAdapter
            recyclerFavourite.layoutManager = layoutManager

        }else{

            Toast.makeText(context , "list is null" , Toast.LENGTH_LONG).show()
        }





        return view
    }

    class RetrieveFavourites(val context: Context) : AsyncTask<Void , Void ,List<RestaurantEntity>>() {
        val db = Room.databaseBuilder(context , RestaurantDatabase1::class.java, "restaurants-db").build()
        override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {
            return db.restaurantDao().getAllRestaurants()

        }
    }

    class ClearDBAsync(context: Context , val resId :String) : AsyncTask<Void , Void , Boolean>(){

        val db = Room.databaseBuilder(context , RestaurantDatabase1::class.java, "restaurants-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            db.restaurantDao().deleteRestaurantById(resId)
            db.close()
            return true
        }
    }


}
