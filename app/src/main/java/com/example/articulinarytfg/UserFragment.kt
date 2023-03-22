package com.example.articulinarytfg

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class UserFragment : Fragment(R.layout.fragment_user) {

    var value: String? = "-1"
    var username: String = ""
    var realname: String = ""
    var mail: String = ""
    var datos: ArrayList<String> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = context?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        value = sharedPreferences?.getString("user", "-1")
        Log.i("VALOR", value.toString())




        lifecycleScope.launch {
            val userResponse = getUser(value!!)
            var tvUsername = view.findViewById<TextView>(R.id.example1_TV)
            var tvRealname = view.findViewById<TextView>(R.id.example2_TV)
            var tvMail = view.findViewById<TextView>(R.id.example3_TV)
            Log.i("ErrorUser", "Onview")

            username = userResponse[0].username
            realname = userResponse[0].realName
            mail = userResponse[0].email

            tvUsername.text = username
            tvRealname.text = realname
            tvMail.text = mail
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
}