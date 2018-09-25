package com.masterwok.demoffmpegglidevideodecoder.activities

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.centerCropTransform
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.masterwok.demoffmpegglidevideodecoder.R
import com.masterwok.demoffmpegglidevideodecoder.glide.GlideApp
import com.masterwok.ffmpegglidevideodecoder.FFmpegVideoDecoder
import com.masterwok.ffmpegglidevideodecoder.ImageUtil
import java.io.File
import android.Manifest.permission
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.Manifest.permission.READ_CONTACTS
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import com.kishan.askpermission.AskPermission
import com.kishan.askpermission.ErrorCallback
import com.kishan.askpermission.PermissionCallback
import com.kishan.askpermission.PermissionInterface

class MainActivity:  PermissionCallback, ErrorCallback, AppCompatActivity() {

    private val REQUEST_PERMISSIONS: Int = 20
    private lateinit var linearLayoutRoot: LinearLayout

    // Place videos you'd like to test here..
    private val videoLocations = arrayOf(
//             "https://demo.erlyvideo.ru/b1-74f2cebca2/preview.mp4?token=WyJjY2Q1Iiw2XQ.Dolpgg.imt_MAoxCMeO9MxyCQO6LcWzndU",
//             "https://demo.erlyvideo.ru/cam.13-f9fb0e04c9/preview.mp4?token=WyIzNzI4Iiw2XQ.Dolpgg.939rnTnY-tmV7B_pGDftINJ9PIk",
//             "https://demo.erlyvideo.ru/cam.17-4e78437092/preview.mp4?token=WyI0NDNiIiw2XQ.Dolpgg.LWDdXUpS4qoX-uEA6mDRTXAzrto",
//             "https://demo.erlyvideo.ru/cam.32-e723faada0/preview.mp4?token=WyJhYjlhIiw2XQ.Dolpgg.7d3atUaeH7ss6X7USSbu-kVQvCw",
//             "https://demo.erlyvideo.ru/cam10/preview.mp4?token=WyIzZGU0Iiw2XQ.Dolpgg.jCQp3UlIoZQLhPST8oiNkWvS3f8",
//             "https://demo.erlyvideo.ru/d1-e80ebd88c7/preview.mp4?token=WyJhZWE2Iiw2XQ.Dolpgg.aTbF__v0-kX5-emoTqy_3tMLENE",
             "http://demo-watcher-s1.flussonic.com/camera.13-cb9c52e100/preview.mp4?token=WyJkMzQ5IiwiMyJd.DorPjA.d-htIxtbVC7AwCWlPNQmiz2EN8g&_t=1537818125589",
            "https://demo.erlyvideo.ru/fake-fc4a0eb388/preview.mp4?token=WyI3ZjFjIiwiNiJd.DorbwQ.YFpXs1i2BIY5YoKnQh-PrFD6UU8&_t=1537821424419"
            ,"https://www.quirksmode.org/html5/videos/big_buck_bunny.mp4"
            ,"https://www.quirksmode.org/html5/videos/big_buck_bunny.webm"
            ,"https://www.quirksmode.org/html5/videos/big_buck_bunny.ogv"
//            ,            "http://mirrors.standaloneinstaller.com/video-sample/jellyfish-25-mbps-hd-hevc.3gp"
//            , "http://mirrors.standaloneinstaller.com/video-sample/page18-movie-4.avi"
//            , "http://mirrors.standaloneinstaller.com/video-sample/Panasonic_HDC_TM_700_P_50i.flv"
//            , "http://mirrors.standaloneinstaller.com/video-sample/DLP_PART_2_768k.m4v"
//            , "http://mirrors.standaloneinstaller.com/video-sample/star_trails.mkv"
//            , "http://mirrors.standaloneinstaller.com/video-sample/small.mts"
//            , "http://mirrors.standaloneinstaller.com/video-sample/metaxas-keller-Bell.vob"
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViewComponents()
        onPermissionSuccess()
//        AskPermission.Builder(this)
//                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .setCallback(this)
//                .setErrorCallback(this)
//                .request(REQUEST_PERMISSIONS)
    }
    override fun onPermissionsDenied(requestCode: Int) {
        Log.d(javaClass.name, "permissions forbidden")
    }

    override fun onPermissionsGranted(requestCode: Int) {
        onPermissionSuccess()
        Log.d(javaClass.name, "permissions granted")
    }

    override fun onShowSettings(permissionInterface: PermissionInterface?, requestCode: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this);
        builder.setMessage("We need permissions for this app. Open setting screen?");
        builder.setPositiveButton(R.string.btn_ok, object: DialogInterface.OnClickListener {
            public override fun onClick(dialog: DialogInterface, which: Int) {
                if (permissionInterface != null) {
                    permissionInterface.onSettingsShown()
                };
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, null);
        builder.show();                    }

    override fun onShowRationalDialog(permissionInterface: PermissionInterface?, requestCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("We need permissions for this app.")
        builder.setPositiveButton(R.string.btn_ok) { dialog, which -> permissionInterface?.onDialogShown() }
        builder.setNegativeButton(R.string.btn_cancel, null)
        builder.show()
    }

    fun onPermissionSuccess() {

        videoLocations.forEach {
            val imageView = createImageView()

            loadImage(imageView, it)

            linearLayoutRoot.addView(imageView)
        }
    }

    private fun bindViewComponents() {
        linearLayoutRoot = findViewById(R.id.linear_layout_root)
    }

    private fun createImageView(): ImageView = ImageView(this).apply {
        layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.MATCH_PARENT
                , 1F
        )
    }

    private fun loadImage(
            imageView: ImageView
            , source: String
    ) = GlideApp
            .with(this)
            .applyDefaultRequestOptions(RequestOptions().apply {
                frame(0L)
//                set(FFmpegVideoDecoder.PERCENTAGE_DURATION, 0.03F)
                 set(FFmpegVideoDecoder.FRAME_AT_TIME, 1000 * 1000 * 0) // One second
                 override(480*16/9, 480)
                 centerCropTransform()
                diskCacheStrategy(DiskCacheStrategy.DATA)
                skipMemoryCache(true)
            })
            .asBitmap()
            .load(Uri.parse(source))
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                    Log.e(javaClass.name, "load Failed " + isFirstResource + target.toString(), e)
                    return false
                }

                override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    Log.e(javaClass.name, "Resource ready " + isFirstResource + target.toString() + " is bitmap null ")
                    if (resource != null) {
                        imageView.setImageBitmap(resource)
                        resource.prepareToDraw()
                    } else {
                        Log.e(javaClass.name, "Resource ready " + "bitmap is null ")
                    }
                    return false
                }

            })
            .into(imageView)

}
