package com.example.mapsproject.Account

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.FileProvider
import com.example.mapsproject.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UpdateProfilePicActivity: Activity(),View.OnClickListener {

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_SELECTION=2
    val path : String = "images/"+ Account.getUserID()
    val storageRef = Firebase.storage.reference

    // Create a child reference
    // imagesRef now points to "images"
    val imagesRef = storageRef.child(path)

    var filePath :String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_picture)
        findViewById<Button>(R.id.take_pic_btn).setOnClickListener(this)

        val imageView = findViewById<ImageView>(R.id.avatar2)
        val localFile = File.createTempFile("images", "jpg")
        imagesRef?.getFile(localFile)?.addOnSuccessListener {
            // Local temp file has been created
            imageView.setImageBitmap((BitmapFactory.decodeFile(localFile.path)))
            filePath=localFile.path
        }?.addOnFailureListener {
            // Handle any errors
            null
        }

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.take_pic_btn->dispatchTakePictureIntent()
            R.id.select_pic_btn->dispatchSelectPictureIntent()
            R.id.upload_pic_btn->{
                if(filePath!="")
                    uploadPic()
            }
        }
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile();
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.mapsProject",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    galleryAddPic()
                }
            }
        }
    }


    private fun dispatchSelectPictureIntent() {
        val pickPhoto =  Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , REQUEST_IMAGE_SELECTION)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK){
            val imageView = findViewById<ImageView>(R.id.avatar2)

            when(requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    imageView.setImageBitmap(imageBitmap)
                }

                REQUEST_IMAGE_SELECTION->{
                    filePath = data!!.extras!!.get("data") as String
                    val myBitmap = BitmapFactory.decodeFile(filePath)
                    imageView.setImageBitmap(myBitmap);
                }
            }
        }

        else  {
            //handle error by restarting activity
            finish()
            startActivity(intent)
        }

    }

    private fun uploadPic() {
        val baos = ByteArrayOutputStream()
        val imageBitmap = BitmapFactory.decodeFile(filePath)
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        var uploadTask = imagesRef!!.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        Log.i("myTag", storageDir.toString())
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            filePath = absolutePath
        }
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(filePath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }

}