package com.example.articulinarytfg

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso


class MainDetailedFragment : Fragment(R.layout.fragment_main_detailed) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).findViewById<NavigationView>(R.id.fab).isGone=
            true
        val recetasBundle =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getSerializable(
                    "recetas", //Key del Serializable de la clase AgentFragment
                    RecetasPopulateResponse.Data::class.java
                ) as? RecetasPopulateResponse.Data
            } else {
                arguments?.getSerializable("recetas") as? RecetasPopulateResponse.Data
            }

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
        DetailTiempo.compoundDrawablePadding = 15
        DetailGente.compoundDrawablePadding = 15

        DetailGente.text = recetasBundle?.attributes?.gente.toString() + " Personas"
        DetailTiempo.text = recetasBundle?.attributes?.tiempo.toString() + "'"
        DetailIngredientes.text = recetasBundle?.attributes?.ingredientesTexto.toString()
        DetailPasos.text = recetasBundle?.attributes?.pasosTexto.toString()
        DetailUser.text = recetasBundle?.attributes?.user?.data?.attributes?.username.toString()
        val ImagenTop = recetasBundle?.attributes?.imagen.toString()
        Picasso.get().load(ImagenTop)
            .into(DetailImage)
    }
/*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

 */
}




