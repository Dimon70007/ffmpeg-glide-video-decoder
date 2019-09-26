package com.masterwok.demoffmpegglidevideodecoder.activities

import android.app.PendingIntent.getActivity
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.masterwok.demoffmpegglidevideodecoder.R
import com.masterwok.demoffmpegglidevideodecoder.glide.GlideApp
import com.masterwok.ffmpegglidevideodecoder.FFmpegVideoDecoder
import java.util.*
import kotlin.collections.ArrayList
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class MainActivity : AppCompatActivity() {

    private lateinit var linearLayoutRoot: LinearLayout

    // Place videos you'd like to test here..
    private var videoLocations:MutableList<String> = listOf<String>(1.toString(), 2.toString()) as MutableList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViewComponents()
        videoLocations = resources.getStringArray(R.array.urls_array).toMutableList()

//        videoLocations = videoLocations.plus().toMutableList()
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
//                set(FFmpegVideoDecoder.PERCENTAGE_DURATION, 0.03F)
                 set(FFmpegVideoDecoder.FRAME_AT_TIME, -1) // loadfirstframe
            })
            .load(Uri.parse(source))
            .fitCenter()
            .override(1280, 720)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .skipMemoryCache(true)
            .into(imageView)

}
