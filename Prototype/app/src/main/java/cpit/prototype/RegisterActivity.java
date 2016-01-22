package cpit.prototype;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private Calendar myCalendar;
    private EditText edty;
    private String gender;
    private TextView tti;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tti = (TextView)findViewById(R.id.textView7);
        gender = "male";
        edty = (EditText)findViewById(R.id.editText7);
        myCalendar = Calendar.getInstance();
        edty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        edty.setText(sdf.format(myCalendar.getTime()));
                    }

                };
                new DatePickerDialog(v.getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void submit(View v) {
        EditText ed = (EditText)findViewById(R.id.editText3);
        String usr = ed.getText().toString().trim();
        EditText ed2 = (EditText)findViewById(R.id.editText4);
        String pass = ed2.getText().toString().trim();
        EditText ed3 = (EditText)findViewById(R.id.editText5);
        String pass2 = ed3.getText().toString().trim();
        EditText ed4 = (EditText)findViewById(R.id.editText6);
        String eml = ed4.getText().toString().trim();
        EditText ed5 = (EditText)findViewById(R.id.editText7);
        String bir = ed5.getText().toString().trim();
        ed.clearFocus();
        ed2.clearFocus();
        ed3.clearFocus();
        ed4.clearFocus();
        ed5.clearFocus();
        if (usr.equals("") || pass.equals("") || eml.equals("") || bir.equals("")){
            tti.setText("Please fill out all fields");
            return;
        }
        if (!pass.equals(pass2)){
            tti.setText("Password not match");
            return;
        }
        Member me = new Member(0, usr, pass, eml, gender, bir, 0.0);
        Gson gson = new Gson();
        username = usr;
        new MyDownloadTask(this).execute(gson.toJson(me));
    }

    public void gender(View v) {
        if(v.getId() == R.id.radioButton){
            RadioButton rad = (RadioButton)findViewById(R.id.radioButton8);
            rad.setChecked(false);
            gender = "male";
        }else{
            RadioButton rad = (RadioButton)findViewById(R.id.radioButton);
            rad.setChecked(false);
            gender = "female";
        }
    }

    public class MyDownloadTask extends AsyncTask<String,String,String>
    {
        private HttpURLConnection urlConnection;
        private Context mContext;
        private ProgressDialog mDialog;
        public MyDownloadTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage("Please wait...");
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String urll = "http://103.18.58.26/Prototype/members/register";
            String result = null;
            try {
                //Connect
                urlConnection = (HttpURLConnection) ((new URL(urll).openConnection()));
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                //urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();
                //Write
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(params[0]);
                writer.close();
                outputStream.close();
                //Read
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                result = sb.toString();
            } catch (Exception e) {
                result = "Bad server or connection, try again later.";
            } finally {
                urlConnection.disconnect();
            }
            return result;
        }

        public int tryParse(String text) {
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return -9;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            tti.setText(result);
            int myId = tryParse(result);
            if(myId >= 0){
                SharedPreferences settings;
                SharedPreferences.Editor editor;
                settings = getSharedPreferences("app", MODE_PRIVATE); //1
                editor = settings.edit();
                editor.putBoolean("logged", true);
                editor.putInt("id", myId);
                editor.putFloat("entry", 0);
                editor.putString("name", username);
                editor.commit();
                startService(new Intent(mContext, DownloadImages.class));
                UpdateReceiver ta = new UpdateReceiver();
                ta.starting(getApplicationContext());
                finish();
            }
            mDialog.dismiss();
        }
    }
}
