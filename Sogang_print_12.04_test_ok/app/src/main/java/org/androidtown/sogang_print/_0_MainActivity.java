package org.androidtown.sogang_print;

import android.content.Intent;
import android.content.SharedPreferences;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.androidtown.sogang_print.R.id.password_text;
import static org.androidtown.sogang_print.R.styleable.CompoundButton;

interface MyEventListener {
    public void onEventCompleted();

    public void onEventFailed();
}

public class _0_MainActivity extends AppCompatActivity implements OnClickListener, MyEventListener {

    String studentNum;
    String password;

    CheckBox autoLogin;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Boolean loginChecked;

    private Button login_button;
    private Button register_button;
    private EditText login_text;
    private EditText pw_text;
    private Client C;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_button = (Button) findViewById(R.id.login_button);
        login_text = (EditText) findViewById(R.id.login_text);
        register_button = (Button) findViewById(R.id.register_button);
        pw_text = (EditText) findViewById((password_text));
        login_button.setOnClickListener(this);
        register_button.setOnClickListener(this);

        autoLogin = (CheckBox) findViewById(R.id.checkBox);
        pref = getSharedPreferences("pref",0);
        editor = pref.edit();

        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    editor.putBoolean("autoLogin", true);
                    editor.commit();
                } else {
                    // if unChecked, removeAll
                    loginChecked = false;
                    editor.clear();
                    editor.putBoolean("autoLogin", false);
                    editor.commit();
                }
            }
        });

        // if autoLogin checked, get input
        if (pref.getBoolean("autoLogin", false)) {
            login_text.setText(pref.getString("id", ""));
            pw_text.setText(pref.getString("pw", ""));
            autoLogin.setChecked(true);
            login_button.performClick();
        }
        // goto mainActivity

        if(autoLogin.isChecked()){
            editor.putBoolean("autoLogin", true);
            editor.commit();
        }
        else {
            editor.putBoolean("autoLogin", false);
            editor.commit();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                startEvent();
                break;
            case R.id.register_button:
                Intent next = new Intent(this, _01_Register.class);
                startActivity(next);
        }
    }



    public void startEvent() {


        studentNum = login_text.getText().toString();
        password = pw_text.getText().toString();


        //edittext data firebase 연동
        String [] params = {studentNum,password};
        editor.putString("id", studentNum);
        editor.commit();

        C = new Client("login", params, this);
        C.execute();

    }



    @Override
    public void onEventCompleted() {

        String output = C.output;
        Intent next = new Intent(this, _1_My_page.class);
        switch (output) {
            case "0":
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("id", studentNum);
                editor.commit();

                if(autoLogin.isChecked()){
                    editor = pref.edit();
                    editor.putString("id", studentNum);
                    editor.putString("pw", password);
                    editor.commit();
                }

                startActivity(next);
                break;
            case "1":
                Toast.makeText(getApplicationContext(), "wrong password", Toast.LENGTH_LONG).show();
                break;
            case "2":
                Toast.makeText(getApplicationContext(), "No such Id", Toast.LENGTH_LONG).show();
                break;
            default:

        }
    }



    @Override
    public void onEventFailed() {
        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
    }
}
