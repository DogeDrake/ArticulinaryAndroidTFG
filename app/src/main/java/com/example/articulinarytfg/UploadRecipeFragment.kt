package com.example.articulinarytfg


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.navigation.NavigationView
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class UploadRecipeFragment : Fragment(R.layout.fragment_upload_recipe) {

    private lateinit var ingredientesLayout: LinearLayout
    private lateinit var pasosLayout: LinearLayout
    var value: String? = "-1"

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
        view.findViewById<Button>(R.id.btnCamara).setOnClickListener {
            startCamera()
        }

        view.findViewById<Button>(R.id.btnGaleria).setOnClickListener {
            startGallery()
        }

 */
        ivImage.setOnClickListener {
            startGallery()
        }

        checkPermissions()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).findViewById<NavigationView>(R.id.fab).isGone =
            true

        (activity as MainActivity).findViewById<NavigationView>(R.id.bottomNavigationView).isVisible =
            false

        ingredientesLayout = view.findViewById(R.id.ingredientesLayout)
        pasosLayout = view.findViewById(R.id.PasosLayout)

        val sharedPreferences = context?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        value = sharedPreferences?.getString("user", "-1")

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

        saveButton.setOnClickListener {

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

            Log.i("Ingredientes", "Ingredientes guardados: $listaEnumeradaIngredientes")
            Log.i("Pasos", "Pasos guardados: $listaEnumeradaPasos")
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
        }
    }

    // Método para agregar un nuevo EditText y un botón de eliminación al LinearLayout
    private fun addEditTextIngredientes() {
        val editText = EditText(context)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.edit_text_margin)
        editText.layoutParams = layoutParams

        // Crear un botón de eliminación
        val deleteButton = Button(context)
        deleteButton.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_baseline_delete_forever_24, // Icono de eliminar
            0, // Sin icono a la izquierda del texto
            0, // Sin icono a la derecha del texto
            0 // Sin icono debajo del texto
        )
        deleteButton.setOnClickListener {
            // Remover el EditText y el botón de eliminación del LinearLayout
            ingredientesLayout.removeView(editText)
            ingredientesLayout.removeView(deleteButton)
        }

        // Agregar el EditText y el botón de eliminación al LinearLayout
        ingredientesLayout.addView(editText)
        ingredientesLayout.addView(deleteButton)
    }

    private fun addEditTextPasos() {
        val editText = EditText(context)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.edit_text_margin)
        editText.layoutParams = layoutParams

        // Crear un botón de eliminación
        val deleteButton = Button(context)
        //deleteButton.setText(R.string.delete)
        deleteButton.gravity = Gravity.CENTER
        deleteButton.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_baseline_delete_forever_24, // Icono de eliminar
            0, // Sin icono a la izquierda del texto
            0, // Sin icono a la derecha del texto
            0 // Sin icono debajo del texto
        )
        deleteButton.setOnClickListener {
            // Remover el EditText y el botón de eliminación del LinearLayout
            pasosLayout.removeView(editText)
            pasosLayout.removeView(deleteButton)
        }

        // Agregar el EditText y el botón de eliminación al LinearLayout
        pasosLayout.addView(editText)
        pasosLayout.addView(deleteButton)
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