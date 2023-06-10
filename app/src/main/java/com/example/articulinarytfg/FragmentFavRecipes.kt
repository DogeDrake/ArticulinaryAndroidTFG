import AdapterFavFragment
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.articulinarytfg.ApiRest
import com.example.articulinarytfg.MainDetailedFragment
import com.example.articulinarytfg.R
import com.example.articulinarytfg.RecetasPopulateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentFavRecipes : Fragment(R.layout.fragment_fav_recipes) {

    private lateinit var adapter: AdapterFavFragment
    val TAG = "MainActivity"
    var datos: ArrayList<RecetasPopulateResponse.Data> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AdapterFavFragment(datos) { recepee ->
            activity?.let {
                val fragment = MainDetailedFragment()
                fragment.arguments = Bundle()
                fragment.arguments?.putSerializable("recetas", recepee)

                activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
                    ?.replace(R.id.container, fragment)?.commit()
            }
        }

        val mainRecyclerView = view.findViewById<RecyclerView>(R.id.RvFavPage)

        mainRecyclerView?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mainRecyclerView?.adapter = adapter

        getUserRutinesPopualte()
    }

    private fun getUserRutinesPopualte() {
        val call = ApiRest.service.getRecetasPopulateResponse()
        call.enqueue(object : Callback<RecetasPopulateResponse> {
            override fun onResponse(
                call: Call<RecetasPopulateResponse>, response: Response<RecetasPopulateResponse>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i(TAG, body.toString())
                    datos.clear()
                    datos.addAll(body.data)
                    datos.sortByDescending { it.attributes.likesID?.toArrayList()?.size ?: 0 }
                    adapter.notifyDataSetChanged()
                    Log.i(TAG, datos.toString())
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
