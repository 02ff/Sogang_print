package org.androidtown.sogang_print;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.R.attr.path;
import static android.content.ContentValues.TAG;
import static org.androidtown.sogang_print.R.id.size_spinner;
import static org.androidtown.sogang_print.R.id.text;

public class _3_Select_doc extends ListActivity implements View.OnClickListener{ //바꿈
    private Button select_doc_button;
    private Button find_doc_button;

    private ArrayList<String> list;//추가
    private ArrayAdapter<String> adapter;//추가

    private ArrayList<ListData> listDataArray = new ArrayList<>();
    public TextView returnVal;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = new ArrayList<String>();//추가
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);//추가
        setListAdapter(adapter);//추가





        setContentView(R.layout.activity_select_doc);
        select_doc_button= (Button) findViewById(R.id.select_doc_button);
        select_doc_button.setOnClickListener(this);
        find_doc_button= (Button) findViewById(R.id.find_doc_button);
        find_doc_button.setOnClickListener(this);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        editor= prefs.edit();

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try {
            super.onActivityResult(requestCode, resultCode, data);
            Uri uri = data.getData();
            ContentResolver cR = getApplicationContext().getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String type = mime.getExtensionFromMimeType(cR.getType(uri));


          // Get the path
            final String path = FileUtils.getPath(this, uri);

            final String fileName  = new File(path).getName();//추가
            list.add(fileName);//추가
            adapter.notifyDataSetChanged();//추가


            editor.putString("path",path);
            editor.putString("doc_name",fileName);
            editor.putString("doc_type",type);

            editor.commit();

           Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG).show();


        } catch (Exception e) {
            Log.e("test","error"+e);



            LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            final View dialogView = inflater.inflate(R.layout.dialog_addmember, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(_3_Select_doc.this);
            builder.setView(dialogView);
            builder.setTitle("인쇄물 레이아웃 설정"); //Dialog 제목
            builder.setPositiveButton("Complite", new DialogInterface.OnClickListener() {
                //Dialog에 "Complite"라는 타이틀의 버튼을 설정
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    //멤버 정보의 입력을 완료하고 TextView에 추가 하도록 하는 작업 수행

                      //dialogView 객체 안에서 입력받는 RadioGroup 객체 찾아오기
                    RadioGroup rg1= (RadioGroup)dialogView.findViewById(R.id.radioGroup_1);
                    RadioGroup rg2= (RadioGroup)dialogView.findViewById(R.id.radioGroup_2);
                    RadioGroup rg3= (RadioGroup)dialogView.findViewById(R.id.radioGroup_3);

                    //선택된 RadioButton의 ID를 RadioGroup에게 얻어오기
                    int checkedId1= rg1.getCheckedRadioButtonId();
                    int checkedId2= rg2.getCheckedRadioButtonId();
                    int checkedId3= rg3.getCheckedRadioButtonId();

                    //Check 된 RadioButton의 ID로 라디오버튼 객체 찾아오기
                    RadioButton rb1= (RadioButton)rg1.findViewById(checkedId1);
                    RadioButton rb2= (RadioButton)rg2.findViewById(checkedId2);
                    RadioButton rb3= (RadioButton)rg3.findViewById(checkedId3);

                    String Text1= rb1.getText().toString();//RadionButton의 Text 얻어오기
                    String Text2= rb2.getText().toString();//RadionButton의 Text 얻어오기
                    String Text3= rb3.getText().toString();//RadionButton의 Text 얻어오기

                    Toast.makeText(_3_Select_doc.this, Text1+", "+Text2+", "+Text3, Toast.LENGTH_SHORT).show();

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                //Dialog에 "Cancel"이라는 타이틀의 버튼을 설정

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                }
            });

            //설정한 값으로 AlertDialog 객체 생성
            AlertDialog dialog=builder.create();

            //Dialog 보이기
            dialog.show();


        }
    }

private static final int REQUEST_PHOTO_ALBUM=1;

    @Override
    public void onClick(View v){
        switch(v.getId()) {
            case R.id.select_doc_button:
                Intent next = new Intent(this, _4_Set_doc.class);
                startActivity(next);
                break;
            case R.id.find_doc_button:

                /*Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);*/


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                startActivityForResult(Intent.createChooser(intent, "dd"),REQUEST_PHOTO_ALBUM);

                break;
        }
    }
}

// 해야할일 -> 1. 리스트 뷰로 만들어놨는데, 아이템을 클릭해서 제거할 수 있게끔 만들기
// 2. 리스트뷰에 있는 파일이름들 Set_doc 할 때 전부다 한번에 처리하게 만들기