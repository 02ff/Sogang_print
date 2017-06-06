package org.androidtown.sogang_print;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.data;

public class _2_Select_location extends AppCompatActivity implements OnClickListener{
    private Button select_loc_button;
    private ArrayList<RadioButton> checkBoxes = new ArrayList<>();
    private RadioGroup location_group;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    private ImageButton location_ga;
    private ImageButton location_k;
    private ImageButton location_ma;
    private ImageButton location_r;
    private ImageButton location_loyola;
    private ImageButton location_d;
    private ImageButton location_j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        editor= prefs.edit();


        location_ga = (ImageButton) findViewById(R.id.location_ga);
        location_k = (ImageButton) findViewById(R.id.location_k);
        location_ma = (ImageButton) findViewById(R.id.location_ma);
        location_r = (ImageButton) findViewById(R.id.location_r);
        location_loyola = (ImageButton) findViewById(R.id.location_loyola);
        location_d = (ImageButton) findViewById(R.id.location_d);
        location_j = (ImageButton) findViewById(R.id.location_j);
        location_ga.setOnClickListener(this);
        location_k.setOnClickListener(this);
        location_ma.setOnClickListener(this);
        location_r.setOnClickListener(this);
        location_loyola.setOnClickListener(this);
        location_d.setOnClickListener(this);
        location_j.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        String str = null;
        switch (v.getId()) {
            case R.id.location_ga:
                str = "ga";
                break;
            case R.id.location_k:
                str = "k";
                break;
            case R.id.location_ma:
                str = "ma";
                break;
            case R.id.location_r:
                str = "r";
                break;
            case R.id.location_loyola:
                str = "loyola";
                break;
            case R.id.location_d:
                str = "d";
                break;
            case R.id.location_j:
                str = "j";
                break;
        }

        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();

        editor.putString("location", str);
        editor.commit();

        String message =
                "??관 2F" + "<br />" +
                        "현재 대기자수 : ??명 " + "<br />" + "<br />" +
                        "이 장소로 예약하시겠습니까?<br />";

        DialogInterface.OnClickListener goNextPage = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                Intent next;
                next = new Intent(_2_Select_location.this, _3_Select_doc.class);
                startActivity(next);
            }
        };

        new AlertDialog.Builder(_2_Select_location.this)
                .setTitle("장소확인")
                .setMessage(Html.fromHtml(message))
                .setNegativeButton("이전", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
                })
                .setPositiveButton("확인", goNextPage)
                .show();
    }
}

// 해야할일 -> 1. 서강대학교 지도 이미지에 위치 정보 덧씌우기