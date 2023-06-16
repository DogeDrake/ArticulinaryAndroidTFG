package com.example.articulinarytfg

import AdapterLiked
import AdapterMainFragment
import LogInFragment
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.articulinarytfg.ApiRest.initService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LikedFragment : Fragment(R.layout.fragment_liked) {

    var value: String? = ""
    private lateinit var adapter: AdapterLiked
    val TAG = "MainActivity"
    var datos: ArrayList<RecetasPopulateResponse.Data> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).findViewById<NavigationView>(R.id.bottomNavigationView).isVisible =
            true
        (activity as MainActivity).findViewById<NavigationView>(R.id.fab).isVisible =
            true

        val ToolBarDetail = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarliked)

        (activity as AppCompatActivity).setSupportActionBar(ToolBarDetail)

        (activity as AppCompatActivity).supportActionBar?.title =
            "Tus Recetas Favoritas"



        val sharedPreferences = context?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        value = sharedPreferences?.getString("user", "-1")

        if (value.isNullOrBlank()) {

            activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
                ?.replace(R.id.container, LogInFragment())?.commit()

        } else {

            getUserRutinesPopualte()

            adapter = AdapterLiked(datos, value.toString()) { recepee ->
                // var agentobj = it //llama al objeto que clickeas (item AgenteAdapter)
                activity?.let {
                    val fragment = MainDetailedFragment()
                    fragment.arguments = Bundle()
                    fragment.arguments?.putSerializable("recetas", recepee)

                    activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
                        ?.replace(R.id.container, fragment)?.commit()
                }
            }
            val mainRecyclerView = view.findViewById<RecyclerView>(R.id.RVLiked)

            mainRecyclerView?.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            mainRecyclerView?.adapter = adapter
        }
    }


    private fun getUserRutinesPopualte() {
        val call = ApiRest.service.getRecetasPopulateResponse()
        call.enqueue(object : Callback<RecetasPopulateResponse> {
            override fun onResponse(
                call: Call<RecetasPopulateResponse>,
                response: Response<RecetasPopulateResponse>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i(TAG, body.toString())
                    datos.clear()
                    datos.addAll(body.data)
                    Log.i(TAG, datos.toString())
                    for (a in datos) {
                        Log.i(TAG, "entroooo!!!!")
                        //InfoRutinas.add(a.attributes.titulorutina)
                        //InfoRutinas.add(a.attributes.publishedAt)
                    }
                    adapter?.notifyDataSetChanged()
                    // Imprimir aqui el listado con logs
                } else {
                    Log.e(TAG, response.errorBody()?.string() ?: "Porto")
                }
            }

            override fun onFailure(
                call: Call<RecetasPopulateResponse>,
                t: Throwable
            ) {
                Log.e(TAG, t.message.toString())
            }
        })
    }
}