package com.example.articulinarytfg

import AdapterMainFragment
import FragmentFavRecipes
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.articulinarytfg.ApiRest.initService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarMenuView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale.filter


class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var adapter: AdapterMainFragment
    val TAG = "MainActivity"
    var datos: ArrayList<RecetasPopulateResponse.Data> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).findViewById<NavigationView>(R.id.bottomNavigationView).isVisible =
            true

        (activity as MainActivity).findViewById<NavigationView>(R.id.fab).isVisible =
            true

        (activity as MainActivity).findViewById<NavigationView>(R.id.bottomAppBar).isVisible =
            true

        val bottomNavigationView =
            view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)


        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewpager2 = view.findViewById<ViewPager>(R.id.viewpager2)

        val tab1 = tabLayout.newTab().setText("Ultimas")
        val tab2 = tabLayout.newTab().setText("Mas Gustadas")

        tabLayout.addTab(tab1)
        tabLayout.addTab(tab2)


        // Crear el adaptador de fragments para el ViewPager
        val adapter = TabPagerAdapter(childFragmentManager)
        viewpager2.adapter = adapter

        // Conectar el TabLayout con el ViewPager
        tabLayout.setupWithViewPager(viewpager2)

        initService()

        val searchBar = view.findViewById<EditText>(R.id.searchBar)
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        searchBar.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                searchBar.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
                // Retrasar la apertura del SearchFragment para permitir que el teclado se oculte
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.container, SearchFragment())
                    ?.commit()
                // Ocultar el teclado manualmente
            }
        }
        searchBar.setOnClickListener {
            searchBar.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
            // Retrasar la apertura del SearchFragment para permitir que el teclado se oculte
            fragmentManager?.beginTransaction()
                ?.replace(R.id.container, SearchFragment())
                ?.commit()
            // Ocultar el teclado manualmente

        }

    }

    inner class TabPagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> FragmentLastRecipes()
                1 -> FragmentFavRecipes()
                else -> throw IllegalArgumentException("Invalid tab position: $position")
            }
        }

        override fun getCount(): Int {
            return 2 // Número total de pestañas
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "Ultimas"
                1 -> "Mas Gustadas"
                else -> null
            }
        }
    }

}