package com.example.eattreat.adapter




import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.eattreat.R
import com.example.eattreat.database.CartEntity


class CartRecyclerAdapter(val context: Context, val cartList: List<CartEntity>, private val listener: OnCartClickListener) : RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>() {
    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtDishSn : TextView = view.findViewById(R.id.txtDishSn)
        val txtDishName : TextView = view.findViewById(R.id.txtDishName)
        val txtDishPrice : TextView = view.findViewById(R.id.txtDishPrice)



        val llContent : LinearLayout = view.findViewById(R.id.llContent)
        val btnRemove : Button = view.findViewById(R.id.btnRemove)





    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_cart_single_row, parent ,false)
        return CartViewHolder(
            view
        )



    }

    override fun getItemCount(): Int {
        return cartList.size
    }
    interface OnCartClickListener{
        fun onRemoveCartClick(dishId : String)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = cartList[position]


        holder.txtDishName.text = cart.DishName
        val dish_price = cart.DishPrice
        holder.txtDishPrice.text = "Rs.$dish_price"
        holder.btnRemove.visibility = View.VISIBLE


        holder.btnRemove.setOnClickListener {

            listener.onRemoveCartClick(cart.DishId)


        }










    }


}



