package cpit.prototype;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 16/01/2016.
 */
public class DownloadImages extends IntentService {
    private HttpURLConnection urlConnection;

    public DownloadImages() {
        super("DownloadImages");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String urll = "http://103.18.58.26/Prototype/images/getAllImages";
        StringBuilder sb = new StringBuilder();
        try {
            urlConnection = (HttpURLConnection) ((new URL(urll).openConnection()));
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            urlConnection.disconnect();
        }

        Gson gson = new Gson();
        Image[] imgs = gson.fromJson(sb.toString(), Image[].class);
        MySQLiteHelper dbHandler = new MySQLiteHelper(getApplicationContext());
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        boolean yess = false;
        Bitmap bitMap;
        if (!path.exists()) {
            yess = path.mkdir();
        }

        for(Image img : imgs) {
            try {
                bitMap = BitmapFactory.decodeStream(new URL("http://103.18.58.26/images/" + img.id + ".jpg").openConnection().getInputStream());
                FileOutputStream out = new FileOutputStream(new File(path, img.id + ".jpg"));
                bitMap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                dbHandler.addImage(img);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
