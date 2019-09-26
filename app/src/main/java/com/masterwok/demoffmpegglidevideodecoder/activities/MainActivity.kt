package com.masterwok.demoffmpegglidevideodecoder.activities

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

class MainActivity : AppCompatActivity() {

    private lateinit var linearLayoutRoot: LinearLayout

    // Place videos you'd like to test here..
    private val videoLocations:Array<String> = resources.getStringArray(R.array.urls_array)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViewComponents()

        videoLocations.plus(resources.getStringArray(R.array.custom_urls_array)).forEach {
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
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .skipMemoryCache(true)
            .into(imageView)

}
