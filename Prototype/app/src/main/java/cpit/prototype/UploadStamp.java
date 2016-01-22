package cpit.prototype;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 18/01/2016.
 */
public class UploadStamp extends IntentService {
    private HttpURLConnection urlConnection;
    public UploadStamp() {
        super("UploadStamp");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MySQLiteHelper dbHandler = new MySQLiteHelper(this);
        Gson gson = new Gson();
        String urll = "http://103.18.58.26/Prototype/image-stamps/recieveStamp";
        try {
            urlConnection = (HttpURLConnection) ((new URL(urll).openConnection()));
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(gson.toJson(dbHandler.getStamps()));
            writer.close();
            outputStream.close();
            InputStream instream = urlConnection.getInputStream();
            instream.close();
            dbHandler.clearStamp();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
            UpdateReceiver.completeWakefulIntent(intent);
        }
    }
}
