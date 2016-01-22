package cpit.prototype;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PopService extends Service {
    private WindowManager windowManager;
    private ImageView ads;
    private WindowManager.LayoutParams params;
    private Handler handler;
    private SharedPreferences pref;
    private long tStart;
    private MySQLiteHelper dbHandler;
    private String currentTimeString;
    private int targetImg;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        dbHandler = new MySQLiteHelper(this);
        pref = getSharedPreferences("app", MODE_PRIVATE);
        String findbytime = new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date());
        currentTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date());
        targetImg = dbHandler.findImage(findbytime);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        ads = new ImageView(this);

        if(targetImg > 0){
            ads.setImageDrawable(Drawable.createFromPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + targetImg + ".jpg"));
        }else {
            ads.setImageResource(R.drawable.notaval);
        }

        ads.setScaleType(ImageView.ScaleType.FIT_XY);
        params= new WindowManager.LayoutParams(
                pref.getInt("dimension", 200),
                pref.getInt("dimension", 200),
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        //params.x = 100;
        //params.y = 100;

        switch (pref.getInt("transparency", 0)) {
            case 0:
                params.alpha = (float)1.0;
                break;
            case 1:
                params.alpha = (float)0.75;
                break;
            case 2:
                params.alpha = (float)0.5;
                break;
            case 3:
                params.alpha = (float)0.25;
                break;
        }

        switch (pref.getInt("animation", 0)) {
            case 0:
                params.windowAnimations  = android.R.style.Animation_Translucent;
                break;
            case 1:
                params.windowAnimations  = android.R.style.Animation_Dialog;
                break;
            case 2:
                params.windowAnimations  = android.R.style.Animation_Toast;
                break;
        }

        ads.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (pref.getBoolean("clickill", false)) {
                            stopSelf();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX
                                + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY
                                + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(ads, params);
                        break;
                }
                return false;
            }
        });
        tStart = System.currentTimeMillis();
        windowManager.addView(ads, params);
        handler = new Handler();
        handler.postDelayed(killit, pref.getInt("lifespan", 5)*1000);
    }

    private final Runnable killit = new Runnable(){
        @Override
        public void run(){
            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(targetImg > 0){
            SharedPreferences.Editor editor = pref.edit();
            double curEntry = pref.getFloat("entry", 0);
            double elapsed = (System.currentTimeMillis() - tStart)/1000.0;
            editor.putFloat("entry", (float) (elapsed + curEntry));
            editor.commit();
            dbHandler.addStamp(targetImg, pref.getInt("id", 0), currentTimeString, elapsed);
        }

        if (ads != null) windowManager.removeView(ads);
        handler.removeCallbacks(killit);
    }
}
