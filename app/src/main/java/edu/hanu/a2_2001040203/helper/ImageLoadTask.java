package edu.hanu.a2_2001040203.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class ImageLoadTask implements Callable<Bitmap> {

    private String url;
    private ImageView imageView;

    public ImageLoadTask(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
    }

    @Override
    public Bitmap call() throws Exception {
        URL urlConnection = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        Bitmap myBitmap = BitmapFactory.decodeStream(input);
        return myBitmap;
    }

    public void setImage(Bitmap result) {
        imageView.setImageBitmap(result);
    }

}

