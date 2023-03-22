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

class AdapterMainFragment(

    private val data: ArrayList<RecetasPopulateResponse.Data>,
    val onCLick: (RecetasPopulateResponse.Data) -> Unit
) :
    RecyclerView.Adapter<AdapterMainFragment.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_view, parent, false)
        return ViewHolder(view)
    }

    private var filteredList: ArrayList<RecetasPopulateResponse.Data> = data

    @SuppressLint("NotifyDataSetChanged")
    fun filter(text: String) {
        filteredList.clear()
        if (text.isEmpty()) {
            filteredList.addAll(data)
        } else {
            val search = text.lowercase()

            for (item in data) {
                if (item.attributes.titulo.lowercase().contains(search)) {
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

        val Titulo = itemView.findViewById<TextView>(R.id.tvTitulo)
        val Gente = itemView.findViewById<TextView>(R.id.tvGente)
        val Tiempo = itemView.findViewById<TextView>(R.id.tvTiempo)
        val User = itemView.findViewById<TextView>(R.id.tvUser)
        val card = itemView.findViewById<CardView>(R.id.card)
        val Imagen = itemView.findViewById<ImageView>(R.id.ImgItem)


        @SuppressLint("SetTextI18n")
        fun bind(item: RecetasPopulateResponse.Data) {
            Titulo.text = item.attributes.titulo
            Gente.text = "Para: " + item.attributes.gente.toString() + " Personas"
            Tiempo.text = "Tiempo: " + item.attributes.tiempo.toString() + "'"
            User.text = "Por " + item.attributes.user.data.attributes.username
            //comprobar que nada sea null
            val imagen2 = item.attributes.imagen.toString()

            Picasso.get().load(imagen2)
                .into(Imagen)



            card.setOnClickListener {
                onCLick(item)
            }
        }


    }
}



