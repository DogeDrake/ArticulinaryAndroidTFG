package com.example.articulinarytfg


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.articulinarytfg.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_anim
        )
    }

    private var clicked = false

    private lateinit var FAB: FloatingActionButton
    private lateinit var FAB1: FloatingActionButton
    private lateinit var FAB2: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        goToFragment(LogInFragment())


        FAB = findViewById<FloatingActionButton>(R.id.fab)
        FAB1 = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        FAB2 = findViewById<FloatingActionButton>(R.id.floatingActionButton2)


        //binding.bottomNavigationView.isVisible = view.isGone

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            menuItem.setCheckable(false)
            val view = binding.bottomNavigationView.findViewById<View>(menuItem.itemId)
            view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_nav_elevation))
            menuItem.setCheckable(true)
            false
        }


        //binding de navmenu toolbar
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homemenu -> if (supportFragmentManager.findFragmentById(R.id.container) !is MainFragment) {
                    // Si no estás en la pantalla, cargarla
                    supportFragmentManager.beginTransaction()
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
                R.id.usermenu -> if (supportFragmentManager.findFragmentById(R.id.container) !is UploadRecipeFragment) {
                    // Si no estás en la pantalla, cargarla
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, UserFragment())
                        .commit()
                }
                /*
                R.id.usermenu -> replaceFragment(UserFragment())
                */
            }
            true
        }

        FAB.setOnClickListener {
            onAddButtonClicked()
        }

        FAB1.setOnClickListener {
            Toast.makeText(this, "Edut Buton", Toast.LENGTH_SHORT).show()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, UploadRecipeFragment())
                .commit()
            FAB.visibility = View.INVISIBLE
            setVisibility(clicked)
        }
        FAB2.setOnClickListener {
            Toast.makeText(this, "Edut Buton", Toast.LENGTH_SHORT).show()

        }
    }

    fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickeable(clicked)
        clicked = !clicked
    }

    fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            FAB1.visibility = View.VISIBLE
            FAB2.visibility = View.VISIBLE
        } else {
            FAB1.visibility = View.INVISIBLE
            FAB2.visibility = View.INVISIBLE
        }
    }

    fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            FAB2.startAnimation(fromBottom)
            FAB1.startAnimation(fromBottom)
            FAB.startAnimation(rotateOpen)
        } else {
            FAB2.startAnimation(toBottom)
            FAB1.startAnimation(toBottom)
            FAB.startAnimation(rotateClose)
        }
    }

    private fun setClickeable(clicked: Boolean) {
        if (!clicked) {
            FAB1.isClickable = true
            FAB2.isClickable = true
        } else {
            FAB1.isClickable = false
            FAB2.isClickable = false
        }
    }


    private var previousFragment: Fragment? = null


    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.container) is MainFragment) {
            finishAffinity()
        } else {
            super.onBackPressed()
        }
    }


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
/*
    fun goToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

 */

    fun goToFragment(fragment: Fragment) {
        // Guardar una referencia al fragmento actual como el fragmento anterior
        previousFragment = supportFragmentManager.findFragmentById(R.id.container)
        // Reemplazar el fragmento actual con el nuevo fragmento
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
