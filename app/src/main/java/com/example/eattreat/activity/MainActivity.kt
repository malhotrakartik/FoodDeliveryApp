package com.example.eattreat.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.eattreat.*
import com.example.eattreat.fragment.FaqFragment
import com.example.eattreat.fragment.FavouriteFragment
import com.example.eattreat.fragment.HomeFragment
import com.example.eattreat.fragment.ProfileFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout : DrawerLayout
    lateinit var coordinateLayout : CoordinatorLayout
    lateinit var appToolbar : androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout

    lateinit var navigationView : NavigationView

    var previousMenuItem : MenuItem? = null
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        drawerLayout = findViewById(R.id.drawerLayout)
        coordinateLayout = findViewById(R.id.coordinatorLayout)
        appToolbar = findViewById(R.id.appToolbar)
        frameLayout = findViewById(R.id.frameLayout)
        navigationView = findViewById(R.id.navigationView)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        setUpToolBar()

        openHome()


        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity ,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if(previousMenuItem != null){
                previousMenuItem?.isChecked = false
            }

            it.isChecked = true
            this.previousMenuItem = it



            when(it.itemId){
                R.id.home -> {
                    openHome()
                }
                R.id.favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            FavouriteFragment()
                        )

                        .commit()


                    drawerLayout.closeDrawers()

                    supportActionBar?.title = "Favourites"
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            ProfileFragment()
                        )

                        .commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "Profile"
                }

                R.id.faq -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            FaqFragment()
                        )

                        .commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "FAQs"
                }
                R.id.LogOut -> {

                    val dialog = AlertDialog.Builder(this@MainActivity)
//                    dialog.setTitle("success")
                    dialog.setMessage("Are you sure you want to logout")
                    dialog.setPositiveButton("YES"){
                            text,listener ->
                        sharedPreferences.edit().clear().apply()
                        val intent = Intent(this@MainActivity,
                            LoginActivity::class.java)
                        startActivity(intent)
                        finish()
//                        Toast.makeText(this@MainActivity ,"hello" , Toast.LENGTH_LONG).show()





                    }
                    dialog.setNegativeButton("Cancel"){
                            text,listener ->
                        it.isChecked = false


                    }
                    dialog.create()
                    dialog.show()
//                        )
//
//                        .commit()
                    drawerLayout.closeDrawers()


                }

            }


            return@setNavigationItemSelectedListener true
        }

    }
        private fun setUpToolBar(){
        setSupportActionBar(appToolbar)
        supportActionBar?.title = "title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id=item.itemId

        if(id == android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openHome(){
        val fragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()

        transaction
            .replace(R.id.frameLayout,fragment)

            .commit()
        drawerLayout.closeDrawers()

        supportActionBar?.title = "Home"
        navigationView.setCheckedItem(R.id.home)

    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frameLayout)
        when(frag) {

            !is HomeFragment -> openHome()

            else -> super.onBackPressed()
        }


    }
    override fun onPause() {
        super.onPause()
        finish()
    }




}














