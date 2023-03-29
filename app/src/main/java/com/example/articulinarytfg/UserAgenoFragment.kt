package com.example.articulinarytfg

import AdapterMainFragment
import AdapterUserAgeno
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserAgenoFragment : Fragment(R.layout.fragment_user_ageno) {

    var username: String = ""
    var realname: String = ""
    var mail: String = ""
    var myString = ""
    val TAG = "UserAgeno"
    var datos: ArrayList<UserResponsePopulate.UserResponsePopulateItem> = ArrayList()
    private lateinit var adapter: AdapterUserAgeno

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myString = arguments?.getString("UserNamePage").toString()
        Log.i("String", "Recibo: " + myString.toString())




        lifecycleScope.launch {
            val userResponse = getUserbyUserName(myString)
            var TVDatoUserAgenoUserName = view.findViewById<TextView>(R.id.DatoUserAgenoUserName)
            var TVDatoUserAgenoNombre = view.findViewById<TextView>(R.id.DatoUserAgenoNombre)
            //var tvMail = view.findViewById<TextView>(R.id.example3_TV)
            Log.i("ErrorUser", "Onview")
            username = userResponse[0].username
            getUserRutinesPopualte(username)
            if (!userResponse[0].realName.equals(null)) {
                realname = userResponse[0].realName
            } else {
                realname = ""
            }

            mail = userResponse[0].email

            TVDatoUserAgenoUserName.text = "@" + username
            TVDatoUserAgenoNombre.text = realname
            //tvMail.text = mail
        }


        adapter = AdapterUserAgeno(datos) { recepee ->
            // var agentobj = it //llama al objeto que clickeas (item AgenteAdapter)
            activity?.let {
                val fragment = MainDetailedFragment()
                fragment.arguments = Bundle()
                fragment.arguments?.putSerializable("recetas", recepee)

                activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
                    ?.replace(R.id.container, fragment)?.commit()
            }
        }
        val mainRecyclerView = view.findViewById<RecyclerView>(R.id.RVUSerAgeno)

        mainRecyclerView?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mainRecyclerView?.adapter = adapter


    }


    private suspend fun getUserbyUserName(username: String): UserResponse {
        return withContext(Dispatchers.IO) {
            val call = ApiRest.service.getUserbyUserName(username)
            val response = call.execute()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                body
            } else {
                throw Exception("Error fetching user data")
            }
        }
    }


    private fun getUserRutinesPopualte(username: String) {
        val call = ApiRest.service.getUsersPopulateResponsebyUsername(username)
        call.enqueue(object : Callback<UserResponsePopulate> {
            override fun onResponse(
                call: Call<UserResponsePopulate>,
                response: Response<UserResponsePopulate>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i(TAG, body.toString())
                    datos.clear()
                    datos.addAll(body)
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
                call: Call<UserResponsePopulate>,
                t: Throwable
            ) {
                Log.e(TAG, t.message.toString())
            }
        })
    }

}