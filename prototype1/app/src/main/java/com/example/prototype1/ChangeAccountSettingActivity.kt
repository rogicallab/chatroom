package com.example.prototype1

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_change_account_setting.*
import java.io.IOException


class ChangeAccountSettingActivity : AppCompatActivity() {

    var selectedPhotoUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_account_setting)
        val user = FirebaseAuth.getInstance().currentUser
        val name = user?.displayName
        editText_user_rename.setText(name)

        val ONE_MEGABYTE: Long = 1024 * 1024
        val photoUrl = user?.photoUrl
        if(photoUrl != null){
            // 参照の作成
            val gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(photoUrl.toString())
            gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                // Data for "images/island.jpg" is returned, use this as needed
                //val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, bytes.)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imageButton_set_newImage.setImageBitmap(bitmap)
            }.addOnFailureListener {
                // Handle any errors
            }
        }

        imageButton_set_newImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, RESULT_PICK_IMAGEFILE);
        }

        button_done.setOnClickListener {
            // photo url は初期がnullになってるから、ここでnull回避または初めてアカウントを作った時点でデフォルトの写真を参照するようにするか
            val storage = FirebaseStorage.getInstance()
            if(selectedPhotoUri == null){
                selectedPhotoUri = photoUrl
            }

            // 選択された写真をストレージに保存
            val filename = user?.uid.toString()
            val ref = storage.getReference("/images/$filename")
            ref.putFile(selectedPhotoUri!!) //$filenameという名前でselectedPhotoUriの画像を保存

            // userの情報の更新
            val storageRef = storage.reference
            val imagesRef: StorageReference? = storageRef.child("/images/$filename")
            val myUri = Uri.parse(imagesRef.toString())
            val rename = editText_user_rename.text.toString()
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(rename)
                .setPhotoUri(myUri)
                .build()
            user?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        println("User profile updated.")
                    }
                }

            // MainActivityに戻る
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // 画像選択後はこのメソッドが実行される(画像が選択された/されなかったにかかわらず）
        println("onActivityResult")
        println(requestCode)
        println(resultCode)
        println(data?.data)
        if(requestCode == RESULT_PICK_IMAGEFILE && resultCode == Activity.RESULT_OK){
            // 完了を押すまではローカルの写真を参照するが、完了ボタンでアップロード、必要に応じてローカルに持って感じか
            if (data != null) {
                try {
                    selectedPhotoUri = data.data
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data?.data)
                    imageButton_set_newImage.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
    companion object {
        private const val RESULT_PICK_IMAGEFILE = 1001
    }
}
