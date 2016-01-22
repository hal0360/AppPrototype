package cpit.prototype;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ControlActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText lifeEdit;
    private EditText popEdit;
    private CheckBox chk1;
    private CheckBox chk2;
    private RadioButton r1;
    private RadioButton r2;
    private RadioButton r3;
    private RadioButton r4;
    private RadioButton r5;
    private RadioButton r6;
    private RadioButton r7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        lifeEdit = (EditText)findViewById(R.id.editText);
        popEdit = (EditText)findViewById(R.id.editText2);
        pref = getSharedPreferences("app", MODE_PRIVATE);
        editor = pref.edit();
        chk1 = (CheckBox)findViewById(R.id.checkBox);
        chk2 = (CheckBox)findViewById(R.id.checkBox2);
        r1 = (RadioButton)findViewById(R.id.radioButton1);
        r2 = (RadioButton)findViewById(R.id.radioButton2);
        r3 = (RadioButton)findViewById(R.id.radioButton3);
        r4 = (RadioButton)findViewById(R.id.radioButton4);
        r5 = (RadioButton)findViewById(R.id.radioButton5);
        r6 = (RadioButton)findViewById(R.id.radioButton6);
        r7 = (RadioButton)findViewById(R.id.radioButton7);
        if(pref.getBoolean("clickill", false)){
            chk1.setChecked(true);
        }
        if(pref.getBoolean("popup", false)){
            chk2.setChecked(true);
            popEdit.setHint(String.valueOf(pref.getInt("popspan", 5)));
        }
        else{
            popEdit.setHint("N/A");
            popEdit.setEnabled(false);
        }
        lifeEdit.setHint(String.valueOf(pref.getInt("lifespan", 5)));
        if(pref.getInt("transparency", 0) == 0){
            r4.setChecked(true);
        }
        else if(pref.getInt("transparency", 0) == 1){
            r5.setChecked(true);
        }
        else if(pref.getInt("transparency", 0) == 2){
            r6.setChecked(true);
        }
        else{
            r7.setChecked(true);
        }
        if(pref.getInt("animation", 0) == 0){
            r1.setChecked(true);
        }
        else if(pref.getInt("animation", 0) == 1){
            r2.setChecked(true);
        }
        else{
            r3.setChecked(true);
        }

        lifeEdit.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(lifeEdit.getText().toString().equals("")){
                    return;
                }
                try {
                    int tepm = Integer.parseInt(lifeEdit.getText().toString());
                    if (tepm < 1) {
                        tepm = 1;
                    } else if (tepm > 15) {
                        tepm = 15;
                    }
                    editor.putInt("lifespan", tepm);
                    editor.commit();
                } catch (Exception e) {
                    lifeEdit.setText("");
                    lifeEdit.setHint(String.valueOf(pref.getInt("lifespan", 5)));
                }
            }
        });

        popEdit.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(popEdit.getText().toString().equals("")){
                    return;
                }
                try {
                    int tepm = Integer.parseInt(popEdit.getText().toString());
                    if (tepm < 1) {
                        tepm = 1;
                    } else if (tepm > 20) {
                        tepm = 20;
                    }
                    editor.putInt("popspan", tepm);
                    editor.commit();
                } catch (Exception e) {
                    popEdit.setText("");
                    popEdit.setHint(String.valueOf(pref.getInt("popspan", 5)));
                }
            }
        });

        RadioGroup.OnCheckedChangeListener RadioOnclic = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radioButton1:
                        editor.putInt("animation", 0);
                        break;
                    case R.id.radioButton2:
                        editor.putInt("animation", 1);
                        break;
                    case R.id.radioButton3:
                        editor.putInt("animation", 2);
                        break;
                    case R.id.radioButton4:
                        editor.putInt("transparency", 0);
                        break;
                    case R.id.radioButton5:
                        editor.putInt("transparency", 1);
                        break;
                    case R.id.radioButton6:
                        editor.putInt("transparency", 2);
                        break;
                    case R.id.radioButton7:
                        editor.putInt("transparency", 3);
                        break;
                }
                editor.commit();
            }
        };
        RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup);
        RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        radioGroup1.setOnCheckedChangeListener(RadioOnclic);
        radioGroup2.setOnCheckedChangeListener(RadioOnclic);
    }

    public void up1(View v) {
        int jack = pref.getInt("lifespan", 5);
        if(jack >= 15){
            return;
        }
        jack += 1;
        editor.putInt("lifespan", jack);
        editor.commit();
        lifeEdit.setText("");
        lifeEdit.setHint(String.valueOf(jack));
    }

    public void down1(View v) {
        int jack = pref.getInt("lifespan", 5);
        if(jack <= 1){
            return;
        }
        jack -= 1;
        editor.putInt("lifespan", jack);
        editor.commit();
        lifeEdit.setText("");
        lifeEdit.setHint(String.valueOf(jack));
    }

    public void up2(View v) {
        int jack = pref.getInt("popspan", 5);
        if(jack >= 20 || !pref.getBoolean("popup", false)){
            return;
        }
        jack += 1;
        editor.putInt("popspan", jack);
        editor.commit();
        popEdit.setText("");
        popEdit.setHint(String.valueOf(jack));
    }

    public void down3(View v) {
        int jack = pref.getInt("popspan", 5);
        if(jack <= 1 || !pref.getBoolean("popup", false)){
            return;
        }
        jack -= 1;
        editor.putInt("popspan", jack);
        editor.commit();
        popEdit.setText("");
        popEdit.setHint(String.valueOf(jack));
    }

    public void ape(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        if(checked){
            editor.putBoolean("clickill", true); //3
        }
        else{
            editor.putBoolean("clickill", false); //3
        }
        editor.commit();
    }

    public void skull(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        SchedulePop ta = new SchedulePop();
        if(checked){
            editor.putBoolean("popup", true); //3
            popEdit.setEnabled(true);
            popEdit.setHint(String.valueOf(pref.getInt("popspan", 5)));
            ta.starting(getApplicationContext());
        }
        else{
            editor.putBoolean("popup", false); //3
            popEdit.setEnabled(false);
            popEdit.setHint("N/A");
            ta.cancelling(getApplicationContext());
        }
        editor.commit();
    }

    public void goback(View view) {
        finish();
    }
}
