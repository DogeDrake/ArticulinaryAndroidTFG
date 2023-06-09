package com.example.articulinarytfg

import RegisterFragment
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.articulinarytfg.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.nimbusds.jwt.JWT
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.JWTParser
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.ParseException


class LogInFragment : Fragment(R.layout.fragment_log_in) {

    val TAG = "MainActivity"
    var datos: ArrayList<UserResponse.UserResponseItem> = ArrayList()
    val username = mutableListOf<String>()
    var idUser = ""
    lateinit var correo: String
    lateinit var contasena: String
    lateinit var mainActivity: MainActivity
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

        (activity as MainActivity).findViewById<NavigationView>(R.id.bottomAppBar).isVisible =
            false

        (activity as MainActivity).findViewById<NavigationView>(R.id.fab).isVisible =
            false

        mainActivity = activity as MainActivity

        //(activity as? AppCompatActivity)?.supportActionBar?.hide()
        ApiRest.initService()
        //Comprobar si hay una sesión iniciada
        var currentUserId = mainActivity.getCurrentUser()
        if (currentUserId > 0) {
            mainActivity.goToFragment(SearchFragment())
        }
        //Hacer Login al pulsar el botón
        ApiRest.initService()
        val btnLogin = view.findViewById<Button>(R.id.loginButton)
        var email = view.findViewById<TextView>(R.id.etusername)
        var password = view.findViewById<TextView>(R.id.etpassword)
        btnLogin.setOnClickListener {

            val emailA =password.text.toString()
            val passwordA = password.text.toString()

            login(emailA, passwordA)
            //login("guillermovl@gmail.com", "Guille123")
        }

        val ButtonToRegis = view.findViewById<Button>(R.id.movetoregsiter_button)
        val ButtonGoogle = view.findViewById<Button>(R.id.GoogleLogIn)


        ButtonToRegis.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
                ?.replace(R.id.container, RegisterFragment())?.commit()
        }

        //Radio Button Recuerdame
        val checkremem = view.findViewById<CheckBox>(R.id.checkboxRecordar)
        var isChecked = false
        checkremem.setOnClickListener {
            isChecked = !isChecked
            checkremem.isChecked = isChecked
        }

        ButtonGoogle.setOnClickListener {
            email.text = "leonardo"
            password.text = "leonardo"
        }


    }

    //Consulta para el Login
    private fun login(usernameOrEmail: String, password: String) {
        val credentials = ApiService.LoginCredentials(usernameOrEmail, password)
        val call = ApiRest.service.login(credentials)
        call.enqueue(object : Callback<ApiService.LoginResponse> {
            override fun onResponse(
                call: Call<ApiService.LoginResponse>, response: Response<ApiService.LoginResponse>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    val tokenString = response.body()?.jwt
                    try {
                        saveLoginLocally(tokenString!!)
                        mainActivity.goToFragment(MainFragment(), true)
                    } catch (e: ParseException) {
                        Log.e("LoginFragment", "Failed to parse JWT token: ${e.message}")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorJson = JSONObject(errorBody)
                    val errorObject = errorJson.getJSONObject("error")
                    val errorMessage = errorObject.getString("message")
                    val tvError = view?.findViewById<TextView>(R.id.tvPasswordError)
                    if (errorMessage == "identifier is a required field" || errorMessage == "password is a required field") {
                        tvError?.text = "Rellena todos los campos"
                    } else if (errorMessage == "Invalid identifier or password") {
                        tvError?.text = "Usuario o contraseña incorrectos"
                    }

                }
            }

            override fun onFailure(call: Call<ApiService.LoginResponse>, t: Throwable) {
                Log.e("LoginFragment", "Error en la solicitud de login: ${t.message}")
            }

        })
    }

    //Guardamos el id y el token en local
    private fun saveLoginLocally(JWTtoken: String) {
        //Sacar id del usuario loggeado con el token
        val jwt: JWT = JWTParser.parse(JWTtoken)
        val claimsSet: JWTClaimsSet = jwt.jwtClaimsSet
        val idUser: Int? = claimsSet.getIntegerClaim("id")

        val sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", JWTtoken)
        editor.putString("user", idUser.toString())
        editor.apply()
    }
}


/*
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
 */
