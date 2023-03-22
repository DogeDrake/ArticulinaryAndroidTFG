package com.example.articulinarytfg

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.articulinarytfg.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LogInFragment : Fragment(R.layout.fragment_log_in) {

    val TAG = "MainActivity"
    var datos: ArrayList<UserResponse.UserResponseItem> = ArrayList()
    val username = mutableListOf<String>()
    var idUser = ""
    lateinit var correo: String
    lateinit var contasena: String
/*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).findViewById<NavigationView>(R.id.bottomNavigationView).isVisible = false
        val buttonnext =  view.findViewById<Button>(R.id.loginButton)
        buttonnext.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
                ?.replace(R.id.container, MainFragment())?.commit()
            (activity as? AppCompatActivity)?.supportActionBar?.hide()
        }

    }

 */


    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).findViewById<NavigationView>(R.id.bottomNavigationView).isVisible =
            false
        (activity as MainActivity).findViewById<NavigationView>(R.id.fab).isVisible =
            false
        //(activity as? AppCompatActivity)?.supportActionBar?.hide()
        ApiRest.initService()
        getUsers()
        view.findViewById<Button>(R.id.loginButton).setOnClickListener {
            correo = view.findViewById<EditText>(R.id.etuserName).text.toString()
            contasena = view.findViewById<EditText>(R.id.etpassword).text.toString()
            if (correo == "" || (contasena) == "") {
                // view.findViewById<TextView>(R.id.txtError).text = "RELLENE TODOS LOS CAMPOS"
            } else {
                if (correo in username) {
                    val indexUser = username.indexOf(correo)
                    Log.d(TAG, username[indexUser + 1])
                    if ((contasena).equals(username[indexUser + 1])) {
                        //loginUser()
                        idUser = username[indexUser + 2]
                        val sharedPreferences =
                            context?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                        sharedPreferences!!.edit().putString("user", idUser).apply()
                        activity?.let {
                            val fragment = MainFragment()
                            Log.d(TAG, idUser)
                            fragment.arguments = Bundle().apply {
                                putString("idUsuario", idUser)
                            }
                            it.supportFragmentManager.beginTransaction()
                                .replace(R.id.container, fragment).commit()
                            (activity as? AppCompatActivity)?.supportActionBar?.show()
                        }

                    } else {
                        //  view.findViewById<TextView>(R.id.txtError).text = "CONTRASEÑA INCORRECTA"
                    }
                } else {
                    //view.findViewById<TextView>(R.id.txtError).text = "CORREO INEXISTENTE"
                }
            }
        }

        val ButtonToRegis = view.findViewById<Button>(R.id.movetoregsiter_button)
        val ButtonGoogle = view.findViewById<Button>(R.id.GoogleLogIn)
        val UserName = view.findViewById<TextView>(R.id.etuserName)
        val UserPassword = view.findViewById<TextView>(R.id.etpassword)




        ButtonGoogle.setOnClickListener {

            UserName.text = "leonardo"
            UserPassword.text = "leonardo"
        }

        ButtonToRegis.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
                ?.replace(R.id.container, RegisterFragment())?.commit()
        }
    }

    /*
    view.findViewById<Button>(R.id.btIrRegistro).setOnClickListener {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container, RegistroFragment())?.addToBackStack(null)?.commit()

    }

    private fun hashPassword(password: String): String {
        try {
            val md = MessageDigest.getInstance("SHA-256")
            val bytes = md.digest(password.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }

// ...

    val passwordFromUser = "password123"
    val hashFromDatabase = "$2a$10$oXJ7HTBVW5mVS21NlK//ouC9SEfqsgVS9E4O1vYpaVAJv26N6mmVK"

    val hashedPasswordFromUser = hashPassword(passwordFromUser)
    if (hashedPasswordFromUser == hashFromDatabase) {
        // contraseña correcta
    } else {
        // contraseña incorrecta
    }


}

override fun onStop() {
    super.onStop()
    activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isVisible = true

}
 */


    private fun getUsers() {

        val call = ApiRest.service.getUser()
        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i("bodypass", body.toString())
                    datos.clear()
                    datos.addAll(body) //.data

                    for (a in datos) {
                        Log.i(TAG, "entroooo!!!!")
                        username.add(a.username)
                        username.add(a.password)
                        username.add(a.id.toString())
                    }

                    Log.d(TAG, username.toString())
                    // Imprimir aqui el listado con logs
                } else {
                    Log.e(TAG, response.errorBody()?.string() ?: "Porto")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }
        })
    }
}


