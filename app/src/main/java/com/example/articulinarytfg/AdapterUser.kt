import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.articulinarytfg.R
import com.example.articulinarytfg.RecetasPopulateResponse
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KSuspendFunction1


class AdapterUser(
    private val value: String?,
    private val data: ArrayList<RecetasPopulateResponse.Data>,
    private val onDelete: (Int) -> Unit,
    private val context: Context,
    // Nueva función para eliminar recetas
    val onCLick: (RecetasPopulateResponse.Data) -> Unit
) : RecyclerView.Adapter<AdapterUser.ViewHolder>(), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.favcardview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onItemDismiss(position: Int) {
        val recetaId = data[position].id

        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Eliminar Receta")
            .setMessage("¿Deseas eliminar la receta?")
            .setPositiveButton("Si") { _, _ ->
                onDelete(recetaId)
                data.removeAt(position)
                notifyItemRemoved(position)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
                notifyDataSetChanged() // Recargar el RecyclerView
            }
            .create()

        alertDialog.show()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.title_text_view)
        private val user = itemView.findViewById<TextView>(R.id.username_text_view)
        private val card = itemView.findViewById<CardView>(R.id.favcard)
        private val imageView = itemView.findViewById<ImageView>(R.id.FavImageCard)

        fun bind(recetasPopulateResponse: RecetasPopulateResponse.Data) {
            title.text = recetasPopulateResponse.attributes.titulo
            user.text =
                "Para: " + recetasPopulateResponse.attributes.gente + " Personas" + "\n" + "Tiempo: " + recetasPopulateResponse.attributes.tiempo + "'"
            val imagen = recetasPopulateResponse.attributes.imagen?.toString()
            val recetaIdApi = recetasPopulateResponse.id
            if (imagen != "" && imagen != null) {
                Picasso.get().load(imagen)
                    .into(imageView)
            } else {
                val defaultImageURL =
                    "https://t4.ftcdn.net/jpg/04/73/25/49/360_F_473254957_bxG9yf4ly7OBO5I0O5KABlN930GwaMQz.jpg"
                Picasso.get().load(defaultImageURL)
                    .into(imageView)
            }

            card.setOnClickListener {
                onCLick(recetasPopulateResponse)
            }
        }
    }
}