package org.androidtown.sogang_print;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.Map;

public class _01_Register extends Activity implements View.OnClickListener,MyEventListener {
    EditText edit_id,edit_name,edit_password,edit_password_confirm;
    Button register;
    Client C;
    String id,name,password,password_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity__01__register);


        register=(Button)findViewById(R.id.regi_register_button);
        edit_id=(EditText) findViewById(R.id.regi_id);
        edit_name=(EditText) findViewById(R.id.regi_name);
        edit_password=(EditText) findViewById(R.id.regi_pw);
        edit_password_confirm=(EditText) findViewById(R.id.regi_pwconfirm);

      register.setOnClickListener(this);
    }

       @Override
       public void onClick(View v){
        startEvent();

    }

    public void startEvent(){

        id=edit_id.getText().toString();
        password=edit_password.getText().toString();
        password_confirm=edit_password_confirm.getText().toString();
        name=edit_name.getText().toString();



        String [] params = {id,name,password,password_confirm};
        C=new Client("register",params,this);
        C.execute();

    }
    @Override
    public void onEventCompleted() {

        String output=C.output;
        Intent next = new Intent(this,_0_MainActivity.class);
        switch(output){
            case "0":
                /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("studentNum",studentNum);
                editor.commit();*/

                Toast.makeText(getApplicationContext(),"Register successful",Toast.LENGTH_SHORT).show();
                startActivity(next);
                break;
            case "1":
                Toast.makeText(getApplicationContext(),"ID exists",Toast.LENGTH_SHORT).show();
                break;
            case "2":
                Toast.makeText(getApplicationContext(),"Password not identical",Toast.LENGTH_SHORT).show();
                break;

            default:


        }
    }

    @Override
    public void onEventFailed() {
        Toast.makeText(getApplicationContext(),"Login failed",Toast.LENGTH_LONG).show();
    }


}
