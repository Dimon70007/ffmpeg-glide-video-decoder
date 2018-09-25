package com.masterwok.ffmpegglidevideodecoder;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class ImageUtil {
    Bitmap bitmap;
    public ImageUtil(String path, ImageView view) {
        bitmap = ImageUtil.retriveVideoFrameFromVideo(path);
        if (bitmap != null) {
            bitmap = Bitmap.createScaledBitmap(bitmap, 240 * 16 / 9, 240, false);
            view.setImageBitmap(bitmap);
        }
    }

    static Bitmap retriveVideoFrameFromVideo(String videoPath) {
        Bitmap bitmap = null;
        FFmpegMediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new FFmpegMediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        } catch (Exception e) {
            e.printStackTrace();
//            throw new Throwable(
//                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
//                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

}
