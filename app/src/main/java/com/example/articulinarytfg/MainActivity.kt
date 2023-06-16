package com.example.articulinarytfg


import LogInFragment
import UserFragment
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.articulinarytfg.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var FAB: FloatingActionButton
    private var clicked = false
    private var value = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        goToFragment(LogInFragment())

        val sharedPreferences = this.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        value = sharedPreferences?.getString("user", "-1").toString()
        Log.i("User", value.toString())


        //binding.bottomNavigationView.isVisible = view.isGone

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            menuItem.setCheckable(false)
            val view = binding.bottomNavigationView.findViewById<View>(menuItem.itemId)
            view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_nav_elevation))
            menuItem.setCheckable(true)
            false
        }

        FAB = findViewById<FloatingActionButton>(R.id.fab)

        FAB.setOnClickListener {
            Log.i("User", value)
            if (!value.isNullOrBlank()) {
                supportFragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.container, UploadRecipeFragment())
                    .commit()
            } else {
                supportFragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.container, LogInFragment())
                    .commit()
            }
        }


        //binding de navmenu toolbar
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homemenu -> if (supportFragmentManager.findFragmentById(R.id.container) !is MainFragment) {
                    // Si no estás en la pantalla, cargarla
                    supportFragmentManager.beginTransaction().addToBackStack(null)
                        .replace(R.id.container, MainFragment())
                        .commit()
                }
/*
                R.id.addmenu -> if (supportFragmentManager.findFragmentById(R.id.container) !is UploadImageFragment) {
                    // Si no estás en la pantalla, cargarla
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, UploadRecipeFragment())
                        .commit()
                }

 */
                R.id.usermenu ->/* if (supportFragmentManager.findFragmentById(R.id.container) !is UploadRecipeFragment ) */ {
                    // Si no estás en la pantalla, cargarla
                    Log.i("UserIdLoco", value)
                    supportFragmentManager.beginTransaction().addToBackStack(null)
                        .replace(R.id.container, UserFragment())
                        .commit()
                }

                R.id.searchmenu -> {
                    supportFragmentManager.beginTransaction().addToBackStack(null)
                        .replace(R.id.container, SearchFragment())
                        .commit()
                }

                R.id.transformmenu -> {
                    supportFragmentManager.beginTransaction().addToBackStack(null)
                        .replace(R.id.container, LikedFragment())
                        .commit()
                }
                /*
                R.id.usermenu -> replaceFragment(UserFragment())
                */
            }

            true
        }


    }


    private var previousFragment: Fragment? = null

/*
    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.container) is MainFragment) {
            finishAffinity()
        } else {
            super.onBackPressed()
        }
    }

 */


    //metodo de cambio de vista entre fragments
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()

    }

    fun FragmentActivity.findNavigationController(@IdRes host: Int): NavController {
        try {
            val navHostFragment = supportFragmentManager.findFragmentById(host) as NavHostFragment
            return navHostFragment.findNavController()
        } catch (e: Exception) {
            throw IllegalStateException("Activity $this does not have a NavController set on $host")
        }
    }

    //Obtener el id del usuario loggeado (del local)
    fun getCurrentUser(): Int {
        val sharedPreferencesGet = this.getSharedPreferences("login", Context.MODE_PRIVATE)
        //val getToken = sharedPreferencesGet.getString("token", "")
        val getID = sharedPreferencesGet.getInt("userID", -1)
        return getID
    }
/*
    fun goToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

 */

    fun goToFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction =
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}
