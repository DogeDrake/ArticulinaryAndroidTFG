import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.articulinarytfg.*
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserAgenoFragment : Fragment(R.layout.fragment_user) {

    private var value: String? = null
    private var username: String = ""
    private var realname: String = ""
    private var mail: String = ""
    private val TAG = "UserProfile"
    private var datos: ArrayList<RecetasPopulateResponse.Data> = ArrayList()
    private lateinit var adapter: AdapterUserAgeno
    private lateinit var emptyTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbaruser)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = "Mi título"
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.topbarmenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addrecipe -> activity?.supportFragmentManager?.beginTransaction()
                ?.addToBackStack(null)?.replace(R.id.container, UploadRecipeFragment())?.commit()
            R.id.edituser -> activity?.supportFragmentManager?.beginTransaction()
                ?.addToBackStack(null)?.replace(R.id.container, EditUserFragment())?.commit()
            R.id.appinfo -> activity?.supportFragmentManager?.beginTransaction()
                ?.addToBackStack(null)?.replace(R.id.container, AjustesFragment())?.commit()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val appBarLayout = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbaruser)
        appBarLayout.visibility = View.GONE

        value =  arguments?.getString("UserNamePage")
        Log.i("VALOR", value.toString())

        lifecycleScope.launch {
            val userResponse = getUser(value!!)
            var tvUsername = view.findViewById<TextView>(R.id.example1_TV)
            var tvRealname = view.findViewById<TextView>(R.id.example2_TV)
            var userImage = view.findViewById<ImageView>(R.id.profile_image)

            username = userResponse[0].username
            getUserRutinesPopulate(value.toString())

            val imagen2 = userResponse[0].userImg?.toString() ?: "userprofile.jpg"

            if (userResponse[0].realName.isNullOrBlank()) {
                realname = " "
                mail = userResponse[0].email
                tvUsername.text = "@" + username
                tvRealname.text = realname
            } else {
                realname = userResponse[0].realName
                mail = userResponse[0].email
                tvUsername.text = "@" + username
                tvRealname.text = realname
            }

            if (imagen2.isNotEmpty()) {
                Picasso.get().load(imagen2)
                    .into(userImage)
            } else {
                val defaultImageURL =
                    "https://img.freepik.com/free-icon/user-image-with-black-background_318-34564.jpg?w=360"
                Picasso.get().load(defaultImageURL)
                    .into(userImage)
            }
        }

        val mainRecyclerView = view.findViewById<RecyclerView>(R.id.RVUSer)
        emptyTextView = view.findViewById(R.id.emptyTextView)
        adapter = AdapterUserAgeno(value, datos) { recepee ->
            activity?.let {
                val fragment = MainDetailedFragment()
                fragment.arguments = Bundle()
                fragment.arguments?.putSerializable("recetas", recepee)

                activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
                    ?.replace(R.id.container, fragment)?.commit()
            }
        }

        mainRecyclerView?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mainRecyclerView?.adapter = adapter

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

    private fun getUserRutinesPopulate(value: String) {
        val call = ApiRest.service.getRecetasPopulateResponse()
        call.enqueue(object : Callback<RecetasPopulateResponse> {
            override fun onResponse(
                call: Call<RecetasPopulateResponse>, response: Response<RecetasPopulateResponse>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i(TAG, body.toString())

                    // Filtrar los datos en función de `value` y `idUsuario`
                    val filteredData = body.data.filter { item ->
                        value.toInt() == item.attributes.user.data.id
                    }

                    datos.clear()
                    datos.addAll(filteredData)
                    datos.sortByDescending { it.attributes.likesID?.toArrayList()?.size ?: 0 }
                    adapter.notifyDataSetChanged()
                    Log.i(TAG, datos.toString())

                    // Mostrar u ocultar el TextView vacío según la cantidad de datos filtrados
                    if (filteredData.isEmpty()) {
                        emptyTextView.visibility = View.VISIBLE
                    } else {
                        emptyTextView.visibility = View.GONE
                    }
                } else {
                    Log.e(TAG, response.errorBody()?.string() ?: "Porto")
                }
            }

            override fun onFailure(
                call: Call<RecetasPopulateResponse>, t: Throwable
            ) {
                Log.e(TAG, t.message.toString())
            }
        })
    }
}
