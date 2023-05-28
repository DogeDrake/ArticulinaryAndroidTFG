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
import java.util.*

fun String?.toArrayList(): ArrayList<String> {
    return if (this.isNullOrEmpty()) {
        ArrayList()
    } else {
        this.split(",").map { it.trim() } as ArrayList<String>
    }
}

class AdapterLiked(
    private val data: ArrayList<RecetasPopulateResponse.Data>,
    private val value: String,
    private val onClick: (RecetasPopulateResponse.Data) -> Unit
) : RecyclerView.Adapter<AdapterLiked.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleTextView: TextView = itemView.findViewById(R.id.tvTitulo)
        private val usernameTextView: TextView = itemView.findViewById(R.id.tvUser)
        private val cardView: CardView = itemView.findViewById(R.id.card)
        private val imageCard: ImageView = itemView.findViewById(R.id.ImgItem)

        @SuppressLint("SetTextI18n")
        fun bind(item: RecetasPopulateResponse.Data) {
            val likesList = item.attributes.likesID?.toArrayList()

            if (likesList != null && likesList.contains(value)) {
                titleTextView.text = item.attributes.titulo
                usernameTextView.text = "Por ${item.attributes.user.data.attributes.username}"

                // Cargar imagen con Picasso
                Picasso.get().load(item.attributes.imagen).into(imageCard)

                cardView.setOnClickListener {
                    onClick(item)
                }

                // Mostrar el elemento
                itemView.visibility = View.VISIBLE
                val layoutParams = itemView.layoutParams as RecyclerView.LayoutParams
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            } else {
                // Ocultar el elemento
                itemView.visibility = View.GONE
                val layoutParams = itemView.layoutParams as RecyclerView.LayoutParams
                layoutParams.height = 0
                layoutParams.width = 0
            }
        }
    }
}
