package com.example.eattreat.adapter

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.eattreat.R
import com.example.eattreat.database.CartEntity
import com.example.eattreat.database.OrderDatabase
import com.example.eattreat.model.Menu

class RestaurantRecyclerAdapter(
    var context: Context,
    val itemList : ArrayList<Menu>,
    private val listener: OnItemClickListener

) : RecyclerView.Adapter<RestaurantRecyclerAdapter.RestaurantViewHolder>() {

//    companion object {
//        var isCartEmpty = true
//    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    interface OnItemClickListener {
        fun onAddItemClick(menu: Menu)
        fun onRemoveItemClick(menu: Menu)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {

        val menu : Menu = itemList[position]

        holder.txtDishSn.text = (position+1).toString()
        holder.txtDishName.text = menu.MenuName
        val menu_price = menu.MenuCost_for_one.toString()
        holder.txtDishPrice.text = "Rs.$menu_price"

        val async = CartItem(
            context,
            menu.MenuId.toString()
        ).execute()
        val result = async.get()
        if(result){

            holder.btnRemoveFromCart.visibility = View.VISIBLE
            holder.btnAddToCart.visibility = View.GONE

        }else{
            holder.btnRemoveFromCart.visibility = View.GONE
            holder.btnAddToCart.visibility = View.VISIBLE

        }



        holder.btnAddToCart.setOnClickListener {

            holder.btnRemoveFromCart.visibility = View.VISIBLE

            holder.btnAddToCart.visibility = View.GONE
            listener.onAddItemClick(menu)
        }

        holder.btnRemoveFromCart.setOnClickListener {
            holder.btnRemoveFromCart.visibility = View.GONE
            holder.btnAddToCart.visibility = View.VISIBLE
            listener.onRemoveItemClick(menu)
        }






    }

    class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtDishSn : TextView = view.findViewById(R.id.txtDishSn)
        val txtDishName : TextView = view.findViewById(R.id.txtDishName)
        val txtDishPrice : TextView = view.findViewById(R.id.txtDishPrice)
        val btnAddToCart : Button = view.findViewById(R.id.btnAddToCart)
        val btnRemoveFromCart : Button = view.findViewById(R.id.btnRemoveFromCart)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_menu_single_row,parent,false)

        return RestaurantViewHolder(
            view
        )

    }

}

class CartItem(
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