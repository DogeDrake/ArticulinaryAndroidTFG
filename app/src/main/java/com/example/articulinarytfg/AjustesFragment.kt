import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.articulinarytfg.R

class AjustesFragment : PreferenceFragmentCompat() {
    private var value: String? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_screen, rootKey)

        val logoutPreference = findPreference<Preference>("logout")
        logoutPreference?.setOnPreferenceClickListener {
            // Mostrar el diálogo de confirmación
            showConfirmationDialog()

            true // Indica que el evento de clic está siendo manejado
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = context?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        value = sharedPreferences?.getString("user", "-1")
    }


    private fun showConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("¿Deseas cerrar sesión?")
            .setPositiveButton("Sí") { dialog, _ ->
                // Acciones cuando se selecciona "Sí"
                val sharedPreferences = context?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences?.edit()
                editor?.putString("token", "")
                editor?.putString("user", "")
                editor?.apply()
                activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
                    ?.replace(R.id.container, LogInFragment())?.commit()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                // Acciones cuando se selecciona "No"
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
