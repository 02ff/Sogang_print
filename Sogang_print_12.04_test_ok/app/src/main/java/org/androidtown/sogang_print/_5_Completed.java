package org.androidtown.sogang_print;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class _5_Completed extends AppCompatActivity implements View.OnClickListener{
    private Button back_to_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);
       back_to_start= (Button) findViewById(R.id.back_to_start_button);
       back_to_start.setOnClickListener(this);



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String studentNum=prefs.getString("studentNum","no studentNum");
        String location=prefs.getString("location","");
        String doc_color=prefs.getString("doc_color","");
        String doc_size=prefs.getString("doc_size","");
        String doc_face=prefs.getString("doc_face","");
        String doc_name=prefs.getString("path","");

        TextView complete = (TextView) findViewById(R.id.complete_text);
        complete.setText(studentNum+location+doc_color+doc_size+doc_face+doc_name);


    }

    @Override
    public void onClick(View v){
        Intent next = new Intent(this,_0_MainActivity.class);
        //next.putExtra("name",login_text.getText());
        startActivity(next);
    }

}