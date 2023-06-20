import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.articulinarytfg.R
import com.example.articulinarytfg.RecetasPopulateResponse
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class AdapterSearchFragment(

    private val data: ArrayList<RecetasPopulateResponse.Data>,
    val onCLick: (RecetasPopulateResponse.Data) -> Unit
) :
    RecyclerView.Adapter<AdapterSearchFragment.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.favcardview, parent, false)
        return ViewHolder(view)
    }

    private var filteredList: ArrayList<RecetasPopulateResponse.Data> = data


    fun updateData(newData: List<RecetasPopulateResponse.Data>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    fun filter(text: String) {
        filteredList.clear()
        if (text.isEmpty()) {
            filteredList.addAll(data)
        } else {
            val searchIngredients = text.lowercase(Locale.getDefault()).split(" ")
            for (item in data) {
                val itemIngredients = item.attributes.ingredientesTexto.map { it.toLowerCase() }
                if (searchIngredients.all { ingredient -> itemIngredients.toString().contains(ingredient) }) {
                    filteredList.add(item)
                }
            }
        }

        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val Titulo = itemView.findViewById<TextView>(R.id.title_text_view)

        //val Gente = itemView.findViewById<TextView>(R.id.tvGente)
        //val Tiempo = itemView.findViewById<TextView>(R.id.tvTiempo)
        val User = itemView.findViewById<TextView>(R.id.username_text_view)
        val card = itemView.findViewById<CardView>(R.id.favcard)
        val Imagen = itemView.findViewById<ImageView>(R.id.FavImageCard)

        var vegano: Boolean = false
        var vegetariano: Boolean = false
        var SinGluten: Boolean = false
        var SinLactosa: Boolean = false
        var BajoEnAzucar: Boolean = false


        @SuppressLint("SetTextI18n")
        fun bind(item: RecetasPopulateResponse.Data) {
            Titulo.text = item.attributes.titulo
            //Gente.text = "Para: " + item.attributes.gente.toString() + " Personas"
            //Tiempo.text = "Tiempo: " + item.attributes.tiempo.toString() + "'"
            User.text = "Por " + item.attributes.user.data.attributes.username
            //comprobar que nada sea null


            vegano = item.attributes.isVegano
            vegetariano = item.attributes.isVegetariano
            SinGluten = item.attributes.isSinGluten
            SinLactosa = item.attributes.isSinLactosa
            BajoEnAzucar = item.attributes.isBajoEnAzucar


            val imagen2 = item.attributes.imagen?.toString() ?: ""

            if (imagen2.isNotEmpty()) {
                Picasso.get().load(imagen2)
                    .into(Imagen)
            } else {
                val defaultImageURL =
                    "https://t4.ftcdn.net/jpg/04/73/25/49/360_F_473254957_bxG9yf4ly7OBO5I0O5KABlN930GwaMQz.jpg"
                Picasso.get().load(defaultImageURL)
                    .into(Imagen)
            }

            card.setOnClickListener {
                onCLick(item)
            }
        }


    }
}



