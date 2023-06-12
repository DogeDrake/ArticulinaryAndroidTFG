package com.example.articulinarytfg

import UserAgenoFragment
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.example.articulinarytfg.ApiRest.service
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainDetailedFragment : Fragment(R.layout.fragment_main_detailed) {
    var isLiked: Boolean = false
    var value: String? = "-1"
    var postid: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
        (activity as MainActivity).findViewById<NavigationView>(R.id.fab).isGone =
            true

         */
        val recetasBundle =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getSerializable(
                    "recetas", //Key del Serializable de la clase AgentFragment
                    RecetasPopulateResponse.Data::class.java
                ) as? RecetasPopulateResponse.Data
            } else {
                arguments?.getSerializable("recetas") as? RecetasPopulateResponse.Data
            }

        val sharedPreferences = context?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        value = sharedPreferences?.getString("user", "-1")

        val ToolBarDetail = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        (activity as AppCompatActivity).setSupportActionBar(ToolBarDetail)

        (activity as AppCompatActivity).supportActionBar?.title =
            recetasBundle?.attributes?.titulo.toString()

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ToolBarDetail.apply {
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }


        val DetailGente = view.findViewById<TextView>(R.id.DetailGente)
        val DetailTiempo = view.findViewById<TextView>(R.id.DetailTiempo)
        val DetailIngredientes = view.findViewById<TextView>(R.id.DetailIngredientes)
        val DetailPasos = view.findViewById<TextView>(R.id.DetailPasos)
        val DetailUser = view.findViewById<TextView>(R.id.DetailUser)
        val DetailImage = view.findViewById<ImageView>(R.id.topDetailImage)
        val LikeButton = view.findViewById<ImageButton>(R.id.favButton)




        DetailTiempo.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_baseline_access_time_24,
            0,
            0,
            0
        )
        DetailGente.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_baseline_diversity_3_24,
            0,
            0,
            0
        )
        DetailUser.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_baseline_person_24,
            0,
            0,
            0
        )

        //Mover a la pantalla del usuario


        DetailTiempo.compoundDrawablePadding = 15
        DetailGente.compoundDrawablePadding = 15





        //Ejemplo de LikeIDs = "0,3,4"
        val LikeIDs = recetasBundle?.attributes?.likesID?.toString()
        var listLike = LikeIDs?.toList()?.map { it.toString() } ?: emptyList()

        if (listLike.contains(value)) {
            println("La lista contiene el valor $value")
            LikeButton.setImageResource(R.drawable.heartfull24)
            isLiked = true
        } else {
            println("La lista no contiene el valor $value")
            LikeButton.setImageResource(R.drawable.heartvoid24)
            isLiked = false
        }


        DetailGente.text = recetasBundle?.attributes?.gente.toString() + " Personas"
        DetailTiempo.text = recetasBundle?.attributes?.tiempo.toString() + "'"
        DetailIngredientes.text = recetasBundle?.attributes?.ingredientesTexto.toString()
        DetailPasos.text = recetasBundle?.attributes?.pasosTexto.toString()
        DetailUser.text = recetasBundle?.attributes?.user?.data?.attributes?.username.toString()
        var UserId = recetasBundle?.attributes?.user?.data?.id.toString()


        postid = recetasBundle?.id!!

        val ImagenTop = recetasBundle.attributes.imagen?.toString() ?: ""

        if (ImagenTop.isNotEmpty()) {
            Picasso.get().load(ImagenTop)
                .into(DetailImage)
        } else {
            val defaultImageURL =
                "https://t4.ftcdn.net/jpg/04/73/25/49/360_F_473254957_bxG9yf4ly7OBO5I0O5KABlN930GwaMQz.jpg"
            Picasso.get().load(defaultImageURL)
                .into(DetailImage)
        }

        DetailUser.setOnClickListener {
            Log.i("String", " Semanda: " + UserId)
            val bundle = Bundle()
            bundle.putString("UserNamePage", UserId)
            val fragment = UserAgenoFragment()
            fragment.arguments = bundle

            activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
                ?.replace(R.id.container, fragment)?.commit()
        }


        LikeButton.setOnClickListener {
            if (isLiked) {
                LikeButton.setImageResource(R.drawable.heartvoid24)
                isLiked = false
                val nuevoArray = listLike!!.filter { it != value }.toTypedArray()
                val stringLikeDislike = nuevoArray.joinToString(",")
                Log.i("Like", "Se ha dado DISLike, se ha subido $stringLikeDislike")
                // Dislike
                val likesId = stringLikeDislike
                Log.i("Like", "Se ha dado DISLike, se ha subido $likesId")
                LikeDislike(postid.toString(), likesId)
            } else {
                LikeButton.setImageResource(R.drawable.heartfull24)
                isLiked = true
                listLike = listLike!!.plus(value.toString())
                Log.i("Like", "Se añade a lista $value")
                Log.i("Like", "Se añade a lista $listLike")
                val stringLikeDislike = listLike!!.joinToString(",")
                Log.i("Like", "Se ha dado Like, se sube $stringLikeDislike")
                val likesId = stringLikeDislike
                // Like
                Log.i("Like", "Se ha dado Like, se sube $likesId")
                LikeDislike(postid.toString(), likesId)
            }
        }
    }

    private fun LikeDislike(postId: String, likesId: String) {
        val attributes = ApiService.LikesIdOb(likesId)

        val call = service.updateLikesId(postId.toInt(), attributes)
        call.enqueue(object : Callback<RecetasResponse> {
            override fun onResponse(
                call: Call<RecetasResponse>,
                response: Response<RecetasResponse>
            ) {
                if (response.isSuccessful) {
                    val updatedResponse = response.body()
                    val updatedData = updatedResponse?.data
                    // Manejar la respuesta actualizada aquí
                    Log.i("Like", "Se ha Actualizado la Api")
                } else {
                    // La solicitud no fue exitosa
                    // Manejar el error aquí
                    val errorBody = response.errorBody()?.string()
                    Log.e("Like", "Error en la respuesta de la API: $errorBody")
                }
            }

            override fun onFailure(call: Call<RecetasResponse>, t: Throwable) {
                // Ocurrió un error durante la llamada
                // Manejar el error aquí
                Log.e("Like", "Error al Actualizar la Api")
            }
        })
    }
}