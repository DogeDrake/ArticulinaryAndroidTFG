import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.articulinarytfg.R
import com.example.articulinarytfg.RecetasPopulateResponse
import com.squareup.picasso.Picasso

class AdapterFavFragment(
    private val data: ArrayList<RecetasPopulateResponse.Data>,
    val onCLick: (RecetasPopulateResponse.Data) -> Unit
) : RecyclerView.Adapter<AdapterFavFragment.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_view, parent, false)
        return ViewHolder(view)
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
        val Fav = itemView.findViewById<TextView>(R.id.tvFav)
        val Imagen = itemView.findViewById<ImageView>(R.id.ImgItem)

        @SuppressLint("SetTextI18n")
        fun bind(item: RecetasPopulateResponse.Data) {
            Titulo.text = item.attributes.titulo
            Gente.text = "Para: ${item.attributes.gente} Personas"
            Tiempo.text = "Tiempo: ${item.attributes.tiempo}'"
            User.text = "Por ${item.attributes.user.data.attributes.username}"
            Fav.text = item.attributes.likesID?.toArrayList()?.size.toString()

            // Comprobar que la imagen no sea null o vacía
            val imagen2 = item.attributes.imagen.orEmpty()
            if (imagen2.isNotEmpty()) {
                Picasso.get().load(imagen2).into(Imagen)
            } else {
                val defaultImageURL =
                    "https://t4.ftcdn.net/jpg/04/73/25/49/360_F_473254957_bxG9yf4ly7OBO5I0O5KABlN930GwaMQz.jpg"
                Picasso.get().load(defaultImageURL).into(Imagen)
            }

            card.setOnClickListener {
                onCLick(item)
            }
        }
    }
}
