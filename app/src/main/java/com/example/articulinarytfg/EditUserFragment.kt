package com.example.articulinarytfg

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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditUserFragment : Fragment(R.layout.fragment_edit_user) {

    var value: String? = "-1"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val ToolBarDetail2 =
            view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.EditUserToolBar)

        (activity as AppCompatActivity).setSupportActionBar(ToolBarDetail2)

        (activity as AppCompatActivity).supportActionBar?.title =
            "Editar Usuario"

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val sharedPreferences = context?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        value = sharedPreferences?.getString("user", "-1")


        lifecycleScope.launch {
            val userResponse = getUser(value!!)
            var ETUsername = view.findViewById<EditText>(R.id.username_edit_text)
            var ETEmail = view.findViewById<EditText>(R.id.email_edit_text)
            var ETRealname = view.findViewById<EditText>(R.id.name_edit_text)
            var ETNPassword = view.findViewById<EditText>(R.id.new_password_edit_text)
            var ETOldPassword = view.findViewById<EditText>(R.id.old_password_edit_text)


            var SaveEditUser = view.findViewById<Button>(R.id.SaveEditUser)

            //var tvMail = view.findViewById<TextView>(R.id.example3_TV)
            Log.i("OnView", userResponse.toString())

            ETUsername.setText("userResponse[0].username")
            ETEmail.setText("userResponse[0].email")
            ETRealname.setText("userResponse[0].realName")


            if (ETUsername != null && ETEmail != null && ETRealname != null) {

                ETUsername.setText(userResponse[0].username)
                ETEmail.setText(userResponse[0].email)
                ETRealname.setText(userResponse[0].realName)
                ETNPassword.setText(userResponse[0].password)

            } else {
                Log.e("EditText Error", "One or more EditTexts not found")
            }

            SaveEditUser.setOnClickListener {
                Log.i("Error", ETNPassword.text.toString() + ETOldPassword.text.toString())
                if (ETOldPassword.text.toString().equals(userResponse[0].password.toString())) {

                    var UsernameUser = ETUsername.text.toString()
                    var MailUser = ETEmail.text.toString()
                    var Realname = ETRealname.text.toString()
                    var Password = ETNPassword.text.toString()
                    var Id = value.toString().toInt()

                    putUser(
                        UsernameUser, Realname, MailUser, Password, Id
                    )

                    activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
                        ?.replace(R.id.container, UserFragment())?.commit()
                } else {
                    Log.i("Error", "Potat")
                }
            }
        }
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

private fun putUser(Username: String, Realname: String, Mail: String, Password: String, id: Int) {
    val user = User(
        Username,
        Mail,
        Password,
        1
    )
    val call = ApiRest.service.updateUser(id, user)
    call.enqueue(object : Callback<UserResponsePopulate> {
        override fun onResponse(
            call: Call<UserResponsePopulate>,
            response: Response<UserResponsePopulate>
        ) {
            if (response.isSuccessful) {
                val userResponse = response.body()
                // Update UI with user response
                Log.e("Error", "Error en la respuesta: ${response.code()}")
            } else {
                // Handle error response
                Log.e("Error", "Error en la respuesta: ${response.code()}")

            }
        }

        override fun onFailure(call: Call<UserResponsePopulate>, t: Throwable) {
            Log.e("Error", "Error en la respuesta: ${t.toString()}")

        }
    })
}