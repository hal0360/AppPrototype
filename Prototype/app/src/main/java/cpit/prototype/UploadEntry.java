package cpit.prototype;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 21/01/2016.
 */
public class UploadEntry extends IntentService {
    public UploadEntry() {
        super("UploadEntry");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MySQLiteHelper dbHandler = new MySQLiteHelper(this);
        SharedPreferences pref = getSharedPreferences("app", MODE_PRIVATE);
        Gson gson = new Gson();
        Member me = new Member(pref.getInt("id", 0), "s", "s", "s", "s", "s", pref.getFloat("entry", 0));
        connAndGet(gson.toJson(me), "http://103.18.58.26/Prototype/members/updateEntry");
        if(connAndGet(gson.toJson(dbHandler.getStamps()), "http://103.18.58.26/Prototype/image-stamps/recieveStamp")){
            dbHandler.clearStamp();
        }
        UpdateReceiver.completeWakefulIntent(intent);
    }

    private boolean connAndGet(String up, String urll){
        HttpURLConnection urlConnection;
        try {
            urlConnection = (HttpURLConnection) ((new URL(urll).openConnection()));
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(up);
            writer.close();
            outputStream.close();
            InputStream instream = urlConnection.getInputStream();
            instream.close();
            urlConnection.disconnect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
