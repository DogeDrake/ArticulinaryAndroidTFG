import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.articulinarytfg.R
import com.example.articulinarytfg.UserResponsePopulate
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class AdapterUserAgeno(

    private val data: ArrayList<UserResponsePopulate.UserResponsePopulateItem>,
    val onCLick: (UserResponsePopulate.UserResponsePopulateItem) -> Unit
) :
    RecyclerView.Adapter<AdapterUserAgeno.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.favcardview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val Titulo = itemView.findViewById<TextView>(R.id.title_text_view)
        val Username = itemView.findViewById<TextView>(R.id.username_text_view)
        val Imagen = itemView.findViewById<ImageView>(R.id.FavImageCard)
        val card = itemView.findViewById<CardView>(R.id.favcard)


        fun bind(item: UserResponsePopulate.UserResponsePopulateItem) {
            if (item.recetas.isNotEmpty()) {
                Titulo.text = item.recetas[0].titulo
                Username.text = item.username
                //comprobar que nada sea null
                val imagen2 = item.recetas[0].imagen

                Picasso.get().load(imagen2)
                    .into(Imagen)

                card.setOnClickListener {
                    onCLick(item)
                }
            }
        }
    }
}



