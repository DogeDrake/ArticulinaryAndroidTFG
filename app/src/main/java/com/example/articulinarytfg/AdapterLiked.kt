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

class AdapterLiked(

    private val data: ArrayList<RecetasPopulateResponse.Data>,
    val onCLick: (RecetasPopulateResponse.Data) -> Unit
) :
    RecyclerView.Adapter<AdapterLiked.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.favcardview, parent, false)
        return ViewHolder(view)
    }

    private var filteredList: ArrayList<RecetasPopulateResponse.Data> = data

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val Titulo = itemView.findViewById<TextView>(R.id.title_text_view)
        val User = itemView.findViewById<TextView>(R.id.username_text_view)
        val card = itemView.findViewById<CardView>(R.id.card)
        val Imagen = itemView.findViewById<ImageView>(R.id.FavImageCard)


        @SuppressLint("SetTextI18n")
        fun bind(item: RecetasPopulateResponse.Data) {
            Titulo.text = item.attributes.titulo
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



