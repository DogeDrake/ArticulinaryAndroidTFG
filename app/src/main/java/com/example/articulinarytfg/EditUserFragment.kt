package com.example.articulinarytfg

import UserFragment

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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*


class EditUserFragment : Fragment(R.layout.fragment_edit_user) {

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

        val view = inflater.inflate(R.layout.fragment_edit_user, container, false)

        ivImage = view.findViewById(R.id.editprofileimage)

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
            startGallery()
        }

        checkPermissions()

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val ToolBarDetail2 =
            view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.EditUserToolBar)

        (activity as AppCompatActivity).setSupportActionBar(ToolBarDetail2)

        (activity as AppCompatActivity).supportActionBar?.title =
            "Editar Usuario"

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val sharedPreferences = context?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        value = sharedPreferences?.getString("user", "-1")

        var ETUsername = view.findViewById<EditText>(R.id.username_edit_text)
        var ETEmail = view.findViewById<EditText>(R.id.email_edit_text)
        var ETRealname = view.findViewById<EditText>(R.id.name_edit_text)
        var SaveEditUser = view.findViewById<Button>(R.id.SaveEditUser)


        lifecycleScope.launch {
            val userResponse = getUser(value!!)

            //var tvMail = view.findViewById<TextView>(R.id.example3_TV)
            Log.i("OnView", userResponse.toString())

            ETUsername.setText("userResponse[0].username")
            ETEmail.setText("userResponse[0].email")
            ETRealname.setText("userResponse[0].realName")



            if (ETUsername != null && ETEmail != null && ETRealname != null) {
                ETUsername.setText(userResponse[0].username)
                ETEmail.setText(userResponse[0].email)
                ETRealname.setText(userResponse[0].realName)
            } else {
                Log.e("EditText Error", "One or more EditTexts not found")
            }


        }

        SaveEditUser.setOnClickListener {
            var UsernameUser = ETUsername.text.toString()
            var MailUser = ETEmail.text.toString()
            var Realname = ETRealname.text.toString()
            var Id = value.toString().toInt()

            // Upload the image file

            // Callback when image upload is completed
            putUser(UsernameUser, Realname, MailUser, imageString, Id)
            activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
                ?.replace(R.id.container, UserFragment())?.commit()

        }

    }

    private fun uploadFile(file: Uri?) {
        file?.let {
            val extension = getFileExtension(file)
            val imageRef = FirebaseStorage.getInstance()
                .reference
                .child("notes/images/${UUID.randomUUID()}.$extension")
            val uploadTask = imageRef.putFile(file)

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
                Log.e("ERROR", "error de subida")
            }.addOnSuccessListener { taskSnapshot ->
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    imageString = uri.toString()
                    Log.i("Errore", imageString)

                    // Invoke the callback with the image URL
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
            File.createTempFile(
                "tmp_image_file",
                ".png",
                requireContext().cacheDir
            ).apply {
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


private fun putUser(Username: String, Realname: String, Mail: String, userImg: String, id: Int) {
    val user = User(
        Username,
        Realname,
        Mail,
        userImg,
        1,
    )
    Log.i("Errore", user.toString())
    val call = ApiRest.service.updateUser(id, user)
    call.enqueue(object : Callback<UserResponsePopulate> {
        override fun onResponse(
            call: Call<UserResponsePopulate>,
            response: Response<UserResponsePopulate>
        ) {
            if (response.isSuccessful) {
                val userResponse = response.body()
                // Update UI with user response
                Log.e("Errore", "Error en la respuesta: ${response}")
            } else {
                // Handle error response
                Log.e("Errore", "Error en la respuesta: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<UserResponsePopulate>, t: Throwable) {
            Log.e("Errore", "Error en la respuesta: ${t.toString()}")
        }
    })
}