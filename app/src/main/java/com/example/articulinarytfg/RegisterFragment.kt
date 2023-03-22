package com.example.articulinarytfg

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment

import android.view.View

import android.widget.Button
import android.widget.EditText
import com.example.articulinarytfg.ApiRest.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class RegisterFragment : Fragment(R.layout.fragment_register) {

    var UsernameUser = ""
    var MailUser = ""
    var PasswordUser = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ApiRest.initService()


        val ButtonToLogin = view.findViewById<Button>(R.id.movetologin_button)

        ButtonToLogin.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
                ?.replace(R.id.container, LogInFragment())?.commit()
        }
        val ETUsername = view.findViewById<EditText>(R.id.regis_username)
        val ETEmail = view.findViewById<EditText>(R.id.regis_email)
        val ETPassword = view.findViewById<EditText>(R.id.regis_password)

        val ButtonRegistrarse = view.findViewById<Button>(R.id.btn_register)
        ButtonRegistrarse.setOnClickListener {
            UsernameUser = ETUsername.text.toString()
            MailUser = ETEmail.text.toString()
            PasswordUser = ETPassword.text.toString()
            postUser(UsernameUser, MailUser, PasswordUser)
        }
    }
}

private fun postUser(Username: String, Mail: String, Password: String) {
    val user = User(
        Username,
        Mail,
        Password,
        1
    )
    val call = service.registerUser(user)
    call.enqueue(object : Callback<UserResponsePopulate> {
        override fun onResponse(
            call: Call<UserResponsePopulate>,
            response: Response<UserResponsePopulate>
        ) {
            if (response.isSuccessful) {
                val userResponse = response.body()
                // Update UI with user response
            } else {
                // Handle error response
            }
        }

        override fun onFailure(call: Call<UserResponsePopulate>, t: Throwable) {
            // Handle failure
        }
    })
}



