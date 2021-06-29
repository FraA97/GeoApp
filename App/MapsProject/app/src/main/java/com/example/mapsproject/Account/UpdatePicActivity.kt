package com.example.mapsproject.Account

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.mapsproject.BuildConfig
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UpdatePicActivity : AppCompatActivity(), View.OnClickListener {
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_SELECTION=2
    val REQUEST_FILE_MANAGER = 3

    // Create a child reference
    val pathCloud : String = "images/"+ Account.getUserID()
    val storageRef = Firebase.storage.reference
    val imagesRef = storageRef.child(pathCloud)

    var filePath :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //set language config
        val myLocale = Locale(MultiPlayerServerConf.language)
        val res: Resources = resources
        val dm: DisplayMetrics = res.getDisplayMetrics()
        val conf: Configuration = res.getConfiguration()
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)

        setContentView(R.layout.activity_upload_picture)
        findViewById<Button>(R.id.take_pic_btn).setOnClickListener(this)
        findViewById<Button>(R.id.select_pic_btn).setOnClickListener(this)
        findViewById<Button>(R.id.upload_pic_btn).setOnClickListener(this)
        object : Thread() {
            override fun run(){
                super.run()
                Handler(Looper.getMainLooper()).postDelayed({downloadPic()}, 0)
            }
        }.start()

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.take_pic_btn-> {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.R) {
                    AlertDialog.Builder(this)
                            .setTitle(getString(R.string.Feature_not_available))
                            .setMessage(getString(R.string.sorry_feature_message))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show()

                } else {
                    when {
                        ContextCompat.checkSelfPermission(
                                applicationContext,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            // You can use the API that requires the permission.
                            dispatchTakePhotoIntent()
                        }
                        else -> {
                            // You can directly ask for the permission.
                            // The registered ActivityResultCallback gets the result of this request.
                            Toast.makeText(applicationContext, "Application needs permission to write storage", Toast.LENGTH_SHORT).show()
                            dispatchStoragePermissionIntentWrite()
                        }
                    }
                }
            }


            R.id.select_pic_btn->{
                when {
                    ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        // You can use the API that requires the permission.
                        dispatchSelectPictureIntent()
                    }
                    else -> {
                        // You can directly ask for the permission.
                        // The registered ActivityResultCallback gets the result of this request.
                        Toast.makeText(applicationContext, "Application needs permission to read storage", Toast.LENGTH_SHORT).show()
                        dispatchStoragePermissionIntentRead()
                    }
                }
            }

            R.id.upload_pic_btn->{
                if(filePath!="")
                    uploadPic()
            }
        }
    }


    private fun dispatchTakePhotoIntent() {
        Log.i("myTag","launching action image capture intent")
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    Log.i("myTag","Creating file")
                    createImageFile();
                } catch (ex: IOException) {
                    Log.i("myTag","Unable to create file")
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.mapsproject",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun dispatchSelectPictureIntent() {
        val pickPhoto = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_IMAGE_SELECTION)    }



    private fun dispatchStoragePermissionIntentRead() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_FILE_MANAGER);
    }
    private fun dispatchStoragePermissionIntentWrite() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_FILE_MANAGER);
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when(requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    Log.i("myTag","request image capture result")
                    //galleryAddPic()
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

                REQUEST_FILE_MANAGER->{

                }
            }
        }

        else  {
            //handle error by restarting activity
            finish()
            startActivity(Intent(this,UpdatePicActivity::class.java))
        }

    }



    //create file for image to be saved in
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        var storageDir: File? = null

        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            filePath = absolutePath
            Log.i("myTag","updated filePath: "+filePath)
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
            Toast.makeText(this,"Unable to upload the photo", Toast.LENGTH_LONG).show()
        }.addOnSuccessListener { taskSnapshot ->
            Toast.makeText(this,"Upload successfull", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this,"No profile pic yet", Toast.LENGTH_LONG).show()
            null
        }

    }
}