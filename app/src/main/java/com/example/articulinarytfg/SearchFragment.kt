package com.example.articulinarytfg

import AdapterSearchFragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment(R.layout.fragment_search) {


    private lateinit var adapter: AdapterSearchFragment
    val TAG = "MainActivity"
    var datos: ArrayList<RecetasPopulateResponse.Data> = ArrayList()

    private var isVeganoSelected = false
    private var isVegetarianoSelected = false
    private var isSinGlutenSelected = false
    private var isSinLactosaSelected = false
    private var isBajoEnAzucarSelected = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserRutinesPopualte()
        adapter = AdapterSearchFragment(datos) { recepee ->
            // var agentobj = it //llama al objeto que clickeas (item AgenteAdapter)
            activity?.let {
                val fragment = MainDetailedFragment()
                fragment.arguments = Bundle()
                fragment.arguments?.putSerializable("recetas", recepee)

                activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
                    ?.replace(R.id.container, fragment)?.commit()
            }

        }














        val etSearch = view.findViewById<EditText>(R.id.etBuscar)

        // Configurar Listener de texto para el campo de búsqueda
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchQuery = s.toString()

                getUserRutinesPopualteFilters(searchQuery)
                adapter.filter(searchQuery)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })


        val chipVegano = view.findViewById<Chip>(R.id.chipVegano)
        val chipVegetariano = view.findViewById<Chip>(R.id.chipVegetariano)
        val chipSinGluten = view.findViewById<Chip>(R.id.chipSinGluten)
        val chipSinLactosa = view.findViewById<Chip>(R.id.chipSinLactosa)
        val chipBajoEnAzucar = view.findViewById<Chip>(R.id.chipBajoEnAzucar)


        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroup)

        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val checkedChips = chipGroup.checkedChipIds
            filterDataByChips(checkedChips)
        }




        val mainRecyclerView = view.findViewById<RecyclerView>(R.id.RvSView)

        mainRecyclerView?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mainRecyclerView?.adapter = adapter




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

    private fun getUserRutinesPopualteFilters(searchQuery: String) {
        val call = ApiRest.service.getRecetasPopulateResponseFilter(searchQuery)
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