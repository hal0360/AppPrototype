package cpit.prototype;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by user on 15/01/2016.
 */
public class getImages extends AsyncTask<Void,Void,String>
{
    private HttpURLConnection urlConnection;
    private Context mContext;
    public getImages (Context context){
        mContext = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        String urll = "http://103.18.58.26/Prototype/images/getAllImages";
        String result = null;
        try {
            urlConnection = (HttpURLConnection) ((new URL(urll).openConnection()));
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
            result = sb.toString();
        } catch (Exception e) {
            result = "bad";
        } finally {
            urlConnection.disconnect();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {


        Gson gson = new Gson();
        Image[] imgs = gson.fromJson(result, Image[].class);
        Toast.makeText(mContext, imgs[1].image_name, Toast.LENGTH_SHORT).show();

        MySQLiteHelper dbHandler = new MySQLiteHelper(mContext);

        for(int index = 0; index < imgs.length; index++) {

            dbHandler.addImage(imgs[index]);

            String[] toppings = {"http://103.18.58.26/images/" + imgs[index].id + ".jpg", imgs[index].id + ".jpg"};
            new LoadImageFromURL().execute(toppings);
        }
    }

    public class LoadImageFromURL extends AsyncTask<String[], Void, Bitmap> {
        private String joby;

        @Override
        protected Bitmap doInBackground(String[]... params) {
            try {
                String[] parapa = params[0];
                joby = parapa[1];
                URL url = new URL(parapa[0]);
                InputStream is = url.openConnection().getInputStream();
                Bitmap bitMap = BitmapFactory.decodeStream(is);
                return bitMap;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub

            if (result == null){
                Toast.makeText(mContext, "super bad", Toast.LENGTH_SHORT).show();
                // iv.setImageBitmap(bit);
            }
            else {
                super.onPostExecute(result);
                //ImageView myImg=(ImageView)findViewById(R.id.imageView2);
                // myImg.setImageBitmap(result);
                boolean yess = false;
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                if (!path.exists()) {
                    yess = path.mkdir();
                }

                File file = new File(path, joby);
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    result.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    Toast.makeText(mContext, "very super bad", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
