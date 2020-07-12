package com.example.eattreat.adapter

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.eattreat.R
import com.example.eattreat.activity.MainActivity
import com.example.eattreat.database.RestaurantDatabase1
import com.example.eattreat.database.RestaurantEntity
import com.example.eattreat.fragment.RestaurantFragment
import com.example.eattreat.model.Restaurant
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(
    val context: Context,

    val itemList: ArrayList<Restaurant>,
    private val listener: OnItemClickListener

) : RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_single_row,parent,false)

        return HomeViewHolder(
            view
        )
    }

    interface OnItemClickListener {
        fun onAddItemClick(restaurant:Restaurant )
        fun onRemoveItemClick(restaurant:Restaurant)
    }

    override fun getItemCount(): Int {
        return itemList.size

    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurant = itemList[position]


        holder.txtResName.text = restaurant.restaurantName
        holder.txtResRating.text = restaurant.restaurantRating
        val res_price = restaurant.restaurantPrice.toString()
        holder.txtResPrice.text = "Rs.$res_price"





        Picasso.get().load(restaurant.restaurantImage).error(R.drawable.ic_default_cover).into(holder.imgResImage)

        println(restaurant.restaurantId)
        val async = ItemsOfCart(
            context,
            restaurant.restaurantId.toString()
        ).execute()
                                val result = async.get()
                                if(result){

                                    holder.btnFavouriteRemoveIcon.visibility = View.VISIBLE
                                    holder.btnFavouriteAddIcon.visibility = View.GONE

                                }else{
                                    holder.btnFavouriteRemoveIcon.visibility = View.GONE
                                    holder.btnFavouriteAddIcon.visibility = View.VISIBLE

                                }


        holder.llContent.setOnClickListener {
            val fragment = RestaurantFragment()

            val args =  Bundle()
            args.putInt("id", restaurant.restaurantId )
            args.putString("name" , restaurant.restaurantName)
            fragment.arguments = args

            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(
                    R.id.frameLayout,
                    fragment
                ).commit()

        }
        holder.btnFavouriteAddIcon.setOnClickListener {


            holder.btnFavouriteAddIcon.visibility = View.GONE
            holder.btnFavouriteRemoveIcon.visibility = View.VISIBLE
                listener.onAddItemClick(restaurant)

        }

        holder.btnFavouriteRemoveIcon.setOnClickListener {
            holder.btnFavouriteRemoveIcon.visibility = View.GONE
            holder.btnFavouriteAddIcon.visibility = View.VISIBLE
            listener.onRemoveItemClick(restaurant)
        }





    }
    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val txtResName : TextView = view.findViewById(R.id.txtResName)
        val txtResPrice : TextView = view.findViewById(R.id.txtResPrice)
        val txtResRating : TextView = view.findViewById(R.id.txtResRating)

        val imgResImage : ImageView = view.findViewById(R.id.imgResImage)
        val llContent : LinearLayout = view.findViewById(R.id.llContent)
        val btnFavouriteAddIcon : Button = view.findViewById(R.id.btnFavouriteAddIcon)
        val btnFavouriteRemoveIcon : Button = view.findViewById(R.id.btnFavouriteRemoveIcon)




    }


}
class ItemsOfCart(
    context: Context,
    val restaurantId: String

) : AsyncTask<Void, Void, Boolean>() {
    val db = Room.databaseBuilder(context, RestaurantDatabase1::class.java, "restaurants-db").build()


    override fun doInBackground(vararg params: Void?): Boolean {


        val restaurant : RestaurantEntity? =db.restaurantDao().getRestaurantById(restaurantId)
        db.close()
        return restaurant!= null

    }

}