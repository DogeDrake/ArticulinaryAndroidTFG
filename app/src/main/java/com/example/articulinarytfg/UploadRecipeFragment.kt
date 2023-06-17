package com.example.articulinarytfg


import LogInFragment
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Color.red
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class UploadRecipeFragment : Fragment(R.layout.fragment_upload_recipe) {

    private lateinit var ingredientesLayout: LinearLayout
    private lateinit var pasosLayout: LinearLayout
    var value: String? = ""

    private lateinit var ivImage: ImageView
    private lateinit var getContent: ActivityResultLauncher<String>
    private lateinit var takeImageResult: ActivityResultLauncher<Uri>
    private var latestTmpUri: Uri? = null
    private var imageString = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_upload_recipe, container, false)

        ivImage = view.findViewById(R.id.myImage)

        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            ivImage.setImageURI(uri)
            uploadFile(uri)
            Log.i("ULR", uri.toString())
        }

        takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
            if (ok) {
                latestTmpUri?.let { uri ->
                    ivImage.setImageURI(uri)
                }
            }
        }
/*
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // obtiene el controlador de navegación
                    val navHostFragment =
                        requireActivity().supportFragmentManager.findFragmentById(R.id.MainFragment) as NavHostFragment
                    // llama al método para volver al fragmento anterior
                    navHostFragment.navController.popBackStack("MainFragment", false)
                }
            })

 */
/*
        view.findViewById<Button>(R.id.btnCamara).setOnClickListener {
            startCamera()
        }

        view.findViewById<Button>(R.id.btnGaleria).setOnClickListener {
            startGallery()
        }

 */
        ivImage.setOnClickListener {
            val options = arrayOf("Iniciar Cámara", "Iniciar Galería")

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Elige una opción")
            builder.setItems(options) { dialog, which ->
                when (which) {
                    0 -> startCamera()
                    1 -> startGallery()
                }
            }

            val dialog = builder.create()
            dialog.show()
        }

        checkPermissions()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*
        (activity as MainActivity).findViewById<NavigationView>(R.id.bottomNavigationView).isVisible =
            false
        (activity as MainActivity).findViewById<NavigationView>(R.id.fab).isGone =
            true
            */
        val sharedPreferences = context?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        value = sharedPreferences?.getString("user", "-1")

        if (value.isNullOrBlank()) {
            activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
                ?.replace(R.id.container, LogInFragment())?.commit()
        } else {
            val ToolBarDetail =
                view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.TopToolBarUpload)

            (activity as AppCompatActivity).setSupportActionBar(ToolBarDetail)

            (activity as AppCompatActivity).supportActionBar?.title =
                "Subir una Receta"
/*
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        */

            ingredientesLayout = view.findViewById(R.id.ingredientesLayout)
            pasosLayout = view.findViewById(R.id.PasosLayout)


            val addButtonIngredientes: Button = view.findViewById(R.id.addIngredientesButton)

            addButtonIngredientes.setOnClickListener {
                addEditTextIngredientes()
            }

            val addButtonPasos: Button = view.findViewById(R.id.addPasosButton)

            addButtonPasos.setOnClickListener {
                addEditTextPasos()
            }

            val ETTitulo = view.findViewById<EditText>(R.id.ETTitulo)
            val ETGente = view.findViewById<EditText>(R.id.ETGente)
            val ETTiempo = view.findViewById<EditText>(R.id.ETTiempo)

            val saveButton: Button = view.findViewById(R.id.saveButton)

/*
        var SalirRecipe = view.findViewById<Button>(R.id.SalirRecetas)


        SalirRecipe.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
                ?.replace(R.id.container, MainFragment())?.commit()
        }

 */

            saveButton.setOnClickListener {
/*
            val ingredientes = mutableListOf<String>()
            for (i in 0 until ingredientesLayout.childCount) {
                val view = ingredientesLayout.getChildAt(i)
                if (view is EditText) {
                    ingredientes.add(view.text.toString())
                }
            }

            val pasos = mutableListOf<String>()
            for (i in 0 until pasosLayout.childCount) {
                val view = pasosLayout.getChildAt(i)
                if (view is EditText) {
                    pasos.add(view.text.toString())
                }
            }

 */

                val ingredientes = mutableListOf<String>()
                for (i in 0 until ingredientesLayout.childCount) {
                    val child = ingredientesLayout.getChildAt(i)
                    if (child is LinearLayout) {
                        val editText = child.getChildAt(0) as? EditText
                        editText?.let {
                            val texto = editText.text.toString().trim()
                            if (texto.isNotEmpty()) {
                                ingredientes.add(texto)
                            }
                        }
                    }
                }

                val pasos = mutableListOf<String>()
                for (i in 0 until pasosLayout.childCount) {
                    val child = pasosLayout.getChildAt(i)
                    if (child is LinearLayout) {
                        val editText = child.getChildAt(0) as? EditText
                        editText?.let {
                            val texto = editText.text.toString().trim()
                            if (texto.isNotEmpty()) {
                                pasos.add(texto)
                            }
                        }
                    }
                }

                var IngredientesString: String = ""
                var Pasos: String = ""

                for ((index, ingrediente) in ingredientes.withIndex()) {
                    IngredientesString += "$ingrediente\n"
                }
                for ((index, ingrediente) in pasos.withIndex()) {
                    Pasos += "${index + 1} - $ingrediente\n"
                }
                var listaEnumeradaIngredientes = IngredientesString
                var listaEnumeradaPasos = Pasos

                Log.i("SubirReceta", "Ingredientes guardados: $listaEnumeradaIngredientes")
                Log.i("SubirReceta", "Pasos guardados: $listaEnumeradaPasos")


                if (ETTitulo.text.isNullOrBlank() or ETGente.text.isNullOrBlank() or ETTiempo.text.isNullOrBlank() or listaEnumeradaPasos.isNullOrBlank() or listaEnumeradaIngredientes.isNullOrBlank() /* or imageString.isNullOrBlank()*/) {
//Error2
                    Log.i("SubirReceta", "Ingredientes guardados: $listaEnumeradaIngredientes")
                    Log.i("SubirReceta", "Pasos guardados: $listaEnumeradaPasos")
                    Log.i("SubirReceta", ETTitulo.text.toString())
                    Log.i("SubirReceta", ETGente.text.toString())
                    Log.i("SubirReceta", ETTiempo.text.toString())
                    Log.i("SubirReceta", "No se sube")

                } else {
                    //comprobar que nada sea null
                    postRecipe(
                        ETTitulo.text.toString(),
                        ETGente.text.toString().toInt(),
                        ETTiempo.text.toString().toInt(),
                        imageString,
                        listaEnumeradaPasos,
                        listaEnumeradaIngredientes,
                        value.toString().toInt()
                    )

                    fragmentManager?.beginTransaction()
                        ?.replace(R.id.container, MainFragment())
                        ?.commit()
                    Log.i("SubirReceta", "Subido")
                }
            }
        }
    }

    // Método para agregar un nuevo EditText y un botón de eliminación al LinearLayout
    private fun addEditTextIngredientes() {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.edit_text_margin)
        linearLayout.layoutParams = layoutParams

        val editText = EditText(context)
        val editTextLayoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1.0f
        )
        editText.layoutParams = editTextLayoutParams

        // Crear un botón de eliminación
        val deleteButton = Button(context)
        val buttonLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        deleteButton.layoutParams = buttonLayoutParams
        deleteButton.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_baseline_delete_forever_24, // Icono de eliminar
            0, // Sin icono a la izquierda del texto
            0, // Sin icono a la derecha del texto
            0 // Sin icono debajo del texto
        )
        deleteButton.setOnClickListener {
            // Remover el EditText, el botón de eliminación y el LinearLayout que los contiene
            ingredientesLayout.removeView(linearLayout)
        }

        // Agregar el EditText y el botón de eliminación al LinearLayout
        linearLayout.addView(editText)
        editText.hint = "Ingrediente"
        linearLayout.addView(deleteButton)
        ingredientesLayout.addView(linearLayout)
    }

    private fun addEditTextPasos() {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.edit_text_margin)
        linearLayout.layoutParams = layoutParams

        val editText = EditText(context)
        val editTextLayoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1.0f
        )
        editText.layoutParams = editTextLayoutParams

        // Crear un botón de eliminación
        val deleteButton = Button(context)
        val buttonLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        deleteButton.layoutParams = buttonLayoutParams
        deleteButton.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_baseline_delete_forever_24, // Icono de eliminar
            0, // Sin icono a la izquierda del texto
            0, // Sin icono a la derecha del texto
            0 // Sin icono debajo del texto
        )
        deleteButton.setOnClickListener {
            // Remover el EditText, el botón de eliminación y el LinearLayout que los contiene
            pasosLayout.removeView(linearLayout)
        }

        // Agregar el EditText y el botón de eliminación al LinearLayout
        linearLayout.addView(editText)
        editText.hint = "Pasos"
        linearLayout.addView(deleteButton)
        pasosLayout.addView(linearLayout)
    }


    private fun uploadFile(file: Uri?) {
        file?.let {
            val extension = getFileExtension(file)
            val imageRef = FirebaseStorage.getInstance()
                .reference
                .child("notes/images/${UUID.randomUUID()}.$extension")
            val uploadTask = imageRef.putFile(file)
            Log.i("File", imageRef.toString())
            //imageString = imageRef.toString()

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
                Log.e("ERROR", "error de subida")
            }.addOnSuccessListener { taskSnapshot ->
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    imageString = uri.toString()
                }
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
            }
        }
    }


    private fun getFileExtension(uri: Uri): String {
        val file = File(uri.toString())
        val fileName = file.name
        val dotIndex = fileName.lastIndexOf('.')
        return if (dotIndex == -1) "" else fileName.substring(dotIndex + 1)
    }

    private fun checkPermissions() {
        if (!permissionsGranted()) {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun permissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        if (permissionsGranted()) {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        } else {
            //Mostrar error al usuario
        }
    }

    private fun startGallery() {
        getContent.launch("image/*")
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile =
            File.createTempFile("tmp_image_file", ".png", requireContext().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }

        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            tmpFile
        )
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 1
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }
}

//comprobar que nada sea null
private fun postRecipe(
    titulo: String,
    gente: Int,
    tiempo: Int,
    imagen: String,
    pasosTexto: String,
    ingredientesTexto: String,
    user: Int
) {
    val receta = Recipe(
        Recipe.Data(
            gente, imagen, ingredientesTexto, pasosTexto, tiempo, titulo, user
        )
    )

    Log.i(
        "Objeto", receta.toString()
    )
    val call = ApiRest.service.uploadRecipe(receta)
    call.enqueue(object : Callback<RecetasPopulateResponse> {
        override fun onResponse(
            call: Call<RecetasPopulateResponse>,
            response: Response<RecetasPopulateResponse>
        ) {
            if (response.isSuccessful) {
                val userResponse = response.body()
                Log.e("Error", "Subido?: ${response.code()}")
                // Update UI with user response
            } else {
                Log.e("Error", "Error en la respuesta: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<RecetasPopulateResponse>, t: Throwable) {
            Log.e("Error", t.toString())
        }
    })
}