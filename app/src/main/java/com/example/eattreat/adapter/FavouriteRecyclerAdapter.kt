package com.example.eattreat.adapter




import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eattreat.R
import com.example.eattreat.activity.MainActivity
import com.example.eattreat.database.RestaurantEntity
import com.example.eattreat.fragment.FavouriteFragment


class FavouriteRecyclerAdapter(val context: Context, val restaurantList: List<RestaurantEntity>,private val listener: OnRestaurantClickListener) : RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {
    class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtResName : TextView = view.findViewById(R.id.txtResName)
        val txtResPrice : TextView = view.findViewById(R.id.txtResPrice)
        val txtResRating : TextView = view.findViewById(R.id.txtResRating)

        val imgResImage : ImageView = view.findViewById(R.id.imgResImage)
        val llContent : LinearLayout = view.findViewById(R.id.llContent)
        val btnFavouriteIcon : Button = view.findViewById(R.id.btnFavouriteIcon)





    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favourite_single_row, parent ,false)
        return FavouriteViewHolder(
            view
        )



    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }
    interface OnRestaurantClickListener{
        fun onRemoveRestaurantClick(restaurantId : String)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val restaurant = restaurantList[position]


        holder.txtResName.text = restaurant.restaurantName
        val restaurant_price = restaurant.restaurantPrice
        holder.txtResPrice.text = "Rs.$restaurant_price"

        holder.txtResRating.text = restaurant.restaurantRating
//        Picasso.get().load(restaurant.resImage).error(R.drawable.default_book_cover).into(holder.imgResImage)
        holder.btnFavouriteIcon.visibility = View.VISIBLE


        holder.btnFavouriteIcon.setOnClickListener {
            listener.onRemoveRestaurantClick(restaurant.restaurantId)

            val fragment = FavouriteFragment()

            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(
                    R.id.frameLayout,
                    fragment
                ).commit()

        }










    }


}



