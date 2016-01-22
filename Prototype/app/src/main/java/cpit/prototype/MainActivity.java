package cpit.prototype;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        pref = getSharedPreferences("app", MODE_PRIVATE); //1
        SharedPreferences.Editor editor = pref.edit();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        editor.putInt("dimension", metrics.widthPixels / 2);
        editor.commit();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        }
       // if (pref.getBoolean("first", true)) {

      //  }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("Welcome: " + pref.getString("name", "Please login") + " id: " + pref.getInt("id", 0));
    }

    public void mania(View v) {
        switch(v.getId())
        {
            case R.id.button1:

               /* LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
                View promptView = layoutInflater.inflate(R.layout.login, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setView(promptView);
                final EditText input = (EditText) promptView.findViewById(R.id.userInput);
                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and set it to result
                        //editTextMainScreen.setText(input.getText());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertD = alertDialogBuilder.create();
                alertD.show();*/

                break;
            case R.id.button2:
                if(pref.getBoolean("logged", false)){
                    Toast.makeText(this, "You already logged in", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;
            case R.id.button3:
                startActivity(new Intent(this, ControlActivity.class));
                break;
        }
    }

    public void getImp (View view) {



    }

    public void testImg (View view) {
       // Toast.makeText(this, "sdsd", Toast.LENGTH_SHORT).show();
        //startService(new Intent(this, DownloadImages.class));

       // startService(new Intent(this, UploadStamp.class));

        MySQLiteHelper dbHandler = new MySQLiteHelper(this);
       Gson gson = new Gson();
       Toast.makeText(this, gson.toJson(dbHandler.getStamps()), Toast.LENGTH_SHORT).show();
    }
}
