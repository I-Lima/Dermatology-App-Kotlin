package br.com.ilstudio.dermatologyapp.ui.customview

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import br.com.ilstudio.dermatologyapp.R
import br.com.ilstudio.dermatologyapp.databinding.ActivityCameraBinding
import br.com.ilstudio.dermatologyapp.ui.EditProfileActivity
import br.com.ilstudio.dermatologyapp.utils.Permissions.checkPermissions
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private lateinit var previewView: PreviewView
    private lateinit var imageCapture: ImageCapture
    private lateinit var photo: File
    private lateinit var sharedPreferences: SharedPreferences
    private var cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        previewView = binding.previewView
        sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE)

        checkPermissions(this)
        startCamera()

        binding.buttonClose.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
            finish()
        }

        binding.buttonCapture.setOnClickListener {
            takePhoto()
        }

        binding.buttonSwitchCamera.setOnClickListener {
            toggleCamera()
        }

        binding.buttonTry.setOnClickListener {
            binding.capturedImageContainer.visibility = View.GONE
            binding.previewContainer.visibility = View.VISIBLE
        }

        binding.buttonSave.setOnClickListener {
            lifecycleScope.launch {
                val base64Image = convertImageToBase64(photo)
                val editor = sharedPreferences.edit()

                editor.putString("profilePicture", base64Image)
                editor.apply()

                Toast.makeText(baseContext, "Saved image", Toast.LENGTH_SHORT).show()
                startActivity(Intent(baseContext, EditProfileActivity::class.java))
                finish()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture )
            } catch (e: Exception) {
                Toast.makeText(baseContext, "Camera failed", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun showCapturedImage(photoFile: File) {
        photo = photoFile
        val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
        binding.capturedImageView.setImageBitmap(bitmap)
        binding.capturedImageContainer.visibility = View.VISIBLE
        binding.previewContainer.visibility = View.GONE
    }

    private fun takePhoto() {
        val photoFile = File(getOutputDirectory(), "${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(baseContext, "Photo capture failed", Toast.LENGTH_SHORT).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    var bitmap = correctImageOrientation(photoFile)

                    if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
                        bitmap = mirrorImage(bitmap)
                    }

                    FileOutputStream(photoFile).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    }

                    showCapturedImage(photoFile)
                }
            })
    }

    private fun toggleCamera() {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
            CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
        startCamera()
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    private fun convertImageToBase64(photoFile: File): String {
        val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
        val outputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val byteArray = outputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun correctImageOrientation(photoFile: File): Bitmap {
        val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
        val exif = ExifInterface(photoFile.absolutePath)

        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        val rotationMatrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotationMatrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotationMatrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotationMatrix.postRotate(270f)
        }

        return Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, rotationMatrix, true
        )
    }

    private fun mirrorImage(bitmap: Bitmap): Bitmap {
        val mirrorMatrix = Matrix()
        mirrorMatrix.postScale(-1f, 1f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, mirrorMatrix, true)
    }
}