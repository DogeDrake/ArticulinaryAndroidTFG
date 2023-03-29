package com.example.articulinarytfg

import AdapterUserAgeno
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.nio.file.Files.delete
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class UserFragment : Fragment(R.layout.fragment_user) {

    var value: String? = "-1"
    var username: String = ""
    var realname: String = ""
    var mail: String = ""
    val TAG = "UserProfile"
    var datos: ArrayList<UserResponsePopulate.UserResponsePopulateItem> = ArrayList()
    private lateinit var adapter: AdapterUserAgeno

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbaruser)
        // Establecer la toolbar como action bar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        // Personalizar la action bar
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = "Mi título"
        }
        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.topbarmenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addrecipe -> activity?.supportFragmentManager?.beginTransaction()
                ?.addToBackStack(null)?.replace(R.id.container, UploadRecipeFragment())?.commit()
            R.id.edituser -> activity?.supportFragmentManager?.beginTransaction()
                ?.addToBackStack(null)?.replace(R.id.container, EditUserFragment())?.commit()
            R.id.appinfo -> activity?.supportFragmentManager?.beginTransaction()
                ?.addToBackStack(null)?.replace(R.id.container, AjustesFragment())?.commit()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = context?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        value = sharedPreferences?.getString("user", "-1")
        Log.i("VALOR", value.toString())

        lifecycleScope.launch {
            val userResponse = getUser(value!!)
            var tvUsername = view.findViewById<TextView>(R.id.example1_TV)
            var tvRealname = view.findViewById<TextView>(R.id.example2_TV)
            //var tvMail = view.findViewById<TextView>(R.id.example3_TV)
            Log.i("ErrorUser", "Onview")

            username = userResponse[0].username
            if (userResponse[0].realName.isNullOrBlank()) {
                realname = " "
                getUserRutinesPopualte(username)
                mail = userResponse[0].email
                tvUsername.text = "@" + username
                tvRealname.text = realname
            } else {
                getUserRutinesPopualte(username)
                realname = userResponse[0].realName
                mail = userResponse[0].email
                tvUsername.text = "@" + username
                tvRealname.text = realname
                //tvMail.text = mail
            }
        }
        val mainRecyclerView = view.findViewById<RecyclerView>(R.id.RVUSer)

        if (mainRecyclerView.adapter?.itemCount == 0) {
            // RecyclerView está vacío, mostrar mensaje

        } else {
            // RecyclerView no está vacío, mostrar elementos


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


            mainRecyclerView?.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            mainRecyclerView?.adapter = adapter
        }

    }

    private suspend fun getUser(id: String): UserResponse {
        return withContext(Dispatchers.IO) {
            val call = ApiRest.service.getUser(id)
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
                call: Call<UserResponsePopulate>, response: Response<UserResponsePopulate>
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
                call: Call<UserResponsePopulate>, t: Throwable
            ) {
                Log.e(TAG, t.message.toString())
            }
        })
    }
}