package com.example.mapsproject.Account

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.example.mapsproject.BuildConfig
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
    val pathCloud : String = "images/"+ Account.getUserID()
    val storageRef = Firebase.storage.reference

    // Create a child reference
    // imagesRef now points to "images"
    val imagesRef = storageRef.child(pathCloud)

    var filePath :String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_picture)
        findViewById<Button>(R.id.take_pic_btn).setOnClickListener(this)
        downloadPic()
    }



    @RequiresApi(Build.VERSION_CODES.R)
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


    //send intent to take pic with camera
    private fun dispatchTakePictureIntent() {
        Log.i("myTag","launching action image capture intent")
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
                }
            }
        }
    }


    //send intent to select pic from gallery
    @RequiresApi(Build.VERSION_CODES.R)
    private fun dispatchSelectPictureIntent() {
        if(!Environment.isExternalStorageManager()){
            Toast.makeText(applicationContext,"Application needs permission to read storage",Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({dispatchStoragePermissionIntent()},2000L)

        }
        else {
            val pickPhoto = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, REQUEST_IMAGE_SELECTION)
        }
    }

    private fun dispatchStoragePermissionIntent() {
        val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
        startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,uri))
        return
    }


    //if activity is paused or stopped, need to preserve value of filePath

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run{
            putString("PATH",filePath)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        filePath = savedInstanceState?.getString("PATH").toString()
    }

    //action to be done when returning from intent launched
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK && data!=null){
            val imageView = findViewById<ImageView>(R.id.avatar2)

            when(requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    galleryAddPic()
                    updateImageView()
                }

                REQUEST_IMAGE_SELECTION->{
                    val selectedImg:Uri = data.getData()!!

                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor: Cursor? = contentResolver.query(selectedImg,
                            filePathColumn, null, null, null)
                    cursor?.moveToFirst()
                    val columnIndex: Int? = cursor?.getColumnIndex(filePathColumn[0])
                    filePath = cursor?.getString(columnIndex!!).toString()
                    cursor?.close()
                    updateImageView()
                }
            }
        }

        else  {
            //handle error by restarting activity
            finish()
            startActivity(intent)
        }

    }



    //create file for image to be saved in
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

    //add picture in filePath to gallery
    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(filePath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }

    //update Image View with file in filePath
    private fun updateImageView() {
        val myImage = findViewById<ImageView>(R.id.avatar2)
        val imgFile = File(filePath)
        if(imgFile.exists()){
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            myImage.setImageBitmap(myBitmap);
        }
    }

    //upload profile pic to cloud storage from file in filePath
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

    //download profile pic from cloud storage and shows it in View
    private fun downloadPic() {
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

}