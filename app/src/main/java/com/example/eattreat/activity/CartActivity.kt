package com.example.eattreat.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.eattreat.adapter.CartRecyclerAdapter
import com.example.eattreat.R
import com.example.eattreat.database.CartEntity
import com.example.eattreat.database.OrderDatabase

class CartActivity : AppCompatActivity() {


    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var appToolbar: androidx.appcompat.widget.Toolbar
    lateinit var recyclerCart : RecyclerView
    lateinit var recyclerAdapter: CartRecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar : ProgressBar
    lateinit var btnPlaceOrder : Button

    var dbCartList = listOf<CartEntity>()
    var totalCost : Int = 0


     var resId : String = ""
//    var resName : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)


        setUpToolBar()






        recyclerCart =findViewById(R.id.recyclerCart)
        progressBar = findViewById(R.id.progressBar)
        progressLayout = findViewById(R.id.progressLayout)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)

        layoutManager = LinearLayoutManager(this@CartActivity)


        dbCartList = RetrieveFavourites(
            this@CartActivity
        ).execute().get()

        for(list in dbCartList){
            totalCost += (list.DishPrice).toInt()
        }

        if(totalCost == 0){
            btnPlaceOrder.visibility = View.GONE
        }

        btnPlaceOrder.text = "Place Order(Rs.$totalCost)"


        if(this@CartActivity!=null){
            progressLayout.visibility =View.GONE
            recyclerAdapter = CartRecyclerAdapter(
                this@CartActivity,
                dbCartList,
                object :
                    CartRecyclerAdapter.OnCartClickListener {

                    override fun onRemoveCartClick(dishId: String) {
                        ClearDBAsync(
                            this@CartActivity,
                            dishId
                        ).execute()
                        finish()
                        startActivity(getIntent())


                    }

                })

            recyclerCart.adapter = recyclerAdapter
            recyclerCart.layoutManager = layoutManager

        }else{
            Toast.makeText(this@CartActivity , "list is null" , Toast.LENGTH_LONG).show()
        }


        btnPlaceOrder.setOnClickListener {

            for(list in dbCartList){
                ClearDBAsync(
                    this@CartActivity,
                    list.DishId
                ).execute()
            }
            val intent = Intent(this@CartActivity , OrderPlacedActivity::class.java)
            startActivity(intent)
            finish()
        }




    }

    class RetrieveFavourites(val context: Context) : AsyncTask<Void , Void ,List<CartEntity>>() {
        val db = Room.databaseBuilder(context , OrderDatabase::class.java, "order-db").build()
        override fun doInBackground(vararg params: Void?): List<CartEntity> {
            return db.cartDao().getAllCart()
            db.close()

        }
    }

    class ClearDBAsync(val context: Context , val dishId :String) : AsyncTask<Void , Void , Boolean>(){

        val db = Room.databaseBuilder(context , OrderDatabase::class.java, "order-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            db.cartDao().deleteCartById(dishId)
            db.close()
            return true
        }
    }
    class DeleteDBAsync(val context: Context , val cartEntity: CartEntity ) : AsyncTask<Void , Void , Boolean>(){

        val db = Room.databaseBuilder(context , OrderDatabase::class.java, "order-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            db.cartDao().deleteCart(cartEntity)
            db.close()
            return true
        }
    }



    private fun setUpToolBar(){
        appToolbar = findViewById(R.id.appToolbar)
        setSupportActionBar(appToolbar)
        supportActionBar?.title = "MyCart"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this@CartActivity , MainActivity::class.java)
        startActivity(intent)
        finish()
        return true
    }

            override fun onBackPressed() {

                val intent = Intent(this@CartActivity , MainActivity::class.java)
                startActivity(intent)
            finish()

            }


}