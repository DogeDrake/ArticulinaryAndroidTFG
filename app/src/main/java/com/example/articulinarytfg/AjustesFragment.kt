import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.articulinarytfg.LogInFragment
import com.example.articulinarytfg.R

class AjustesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_screen, rootKey)

        val logoutPreference = findPreference<Preference>("logout")
        logoutPreference?.setOnPreferenceClickListener {
            // Mostrar el diálogo de confirmación
            showConfirmationDialog()

            true // Indica que el evento de clic está siendo manejado
        }
    }

    private fun showConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("¿Deseas cerrar sesión?")
            .setPositiveButton("Sí") { dialog, _ ->
                // Acciones cuando se selecciona "Sí"
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
