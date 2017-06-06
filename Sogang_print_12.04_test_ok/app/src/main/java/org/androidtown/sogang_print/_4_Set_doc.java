package org.androidtown.sogang_print;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class _4_Set_doc extends AppCompatActivity implements View.OnClickListener,MyEventListener {
    private Button set_doc_button;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Client C;
    EditText num_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_doc);
        set_doc_button= (Button) findViewById(R.id.set_doc_button);
        set_doc_button.setOnClickListener(this);
        Spinner col_spinner = (Spinner)findViewById(R.id.color_spinner);
        Spinner size_spinner = (Spinner)findViewById(R.id.size_spinner);
        Spinner face_spinner = (Spinner)findViewById(R.id.face_spinner);
         num_edit = (EditText)findViewById(R.id.num_edit);
        final TextView col_text=(TextView)findViewById(R.id.col_text);
        final TextView size_text=(TextView)findViewById(R.id.size_text);
        final TextView face_text=(TextView)findViewById(R.id.face_text);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        editor= prefs.edit();




        col_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                col_text.setText("Color :" + parent.getItemAtPosition(position));
                editor.putString("doc_color",parent.getItemAtPosition(position).toString());
                editor.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        size_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                size_text.setText("Direction :" + parent.getItemAtPosition(position));
                editor.putString("doc_direction",parent.getItemAtPosition(position).toString());
                editor.commit();


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        face_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                face_text.setText("Face :" + parent.getItemAtPosition(position));
                editor.putString("doc_face",parent.getItemAtPosition(position).toString());
                editor.commit();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public void onClick(View v){
        startEvent();





    }

    public void startEvent(){




        //edittext data firebase 연동
        String [] params = {
                prefs.getString("location",""),
                prefs.getString("id",""),
                prefs.getString("doc_name","").substring(0, prefs.getString("doc_name","").indexOf(".")),

                prefs.getString("doc_direction",""),
                prefs.getString("doc_color",""),
                prefs.getString("doc_face",""),

                num_edit.getText().toString(),
                prefs.getString("doc_type","")

              };

        System.out.println(params);
        String path = prefs.getString("path","");
        C=new Client("docSend",params,path,this);

        C.execute();/*
        Thread background = new  Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        background.start();*/


    }
    @Override
    public void onEventCompleted() {
        Intent next = new Intent(this,_5_Completed.class);
        System.out.println("event complete");
     String output=C.output;


        switch(output){
            case "0":
                startActivity(next);
                break;
            case "1":
                Toast.makeText(getApplicationContext(),"send failed",Toast.LENGTH_LONG).show();
                break;
            default:


        }
    }

    @Override
    public void onEventFailed() {
        Toast.makeText(getApplicationContext(),"Login failed",Toast.LENGTH_LONG).show();
    }
}