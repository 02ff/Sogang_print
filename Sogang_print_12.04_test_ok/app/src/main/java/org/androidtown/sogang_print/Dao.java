package org.androidtown.sogang_print;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KoJunghwan on 2016-10-03.
 */

public class Dao {
    private Context context;
    private SQLiteDatabase database;

    //localdata에 database(students, documents)를 형성
    public Dao(Context context){
        this.context=context;
        database=context.openOrCreateDatabase("LocalDATA.db",SQLiteDatabase.CREATE_IF_NECESSARY,null);


        try{
            String stu_sql=
                    "CREATE TABLE IF NOT EXISTS Students (ID integer primary key autoincrement,"
                            +" StudentNum integer UNIQUE not null,"
                            +" Password not null,"
                            +" Point not null);";
            String doc_sql=
                    "CREATE TABLE IF NOT EXISTS Documents (ID integer primary key autoincrement,"
                            +" DocName not null,"
                            +" DocColor not null,"
                            +" DocFace not null,"
                            +" DocSize not null,"
                            +" DocGrid not null,"
                            +" DocNum not null,"
                            +" DocComplete not null,"
                            +" StudentNum not null,"
                            +" LocationNum not null);";


            database.execSQL(stu_sql);
            database.execSQL(doc_sql);
        }catch(Exception e){
            Log.e("test","CREATE TABLE FAILED! -" + e);
            e.printStackTrace();
        }
    }

    //jsonData를 argument로 받아 각각 students,documents sql table에 넣어줌
    public void insertJsonData(String jsonData){
        int studentNum;
        String password;
        int point;
        String DocName;
        String DocColor;
        String DocFace;
        String DocSize;
        String DocGrid;
        int DocNum;
        int DocComplete;
        int LocationNum;

        try{
            JSONArray jArr = new JSONArray(jsonData);
            for (int i=0; i<jArr.length();++i){
                JSONObject jObj = jArr.getJSONObject(i);

                studentNum=jObj.getInt("StudentNum");
                password=jObj.getString("Password");
                point=jObj.getInt("Point");

                DocName=jObj.getString("DocName");
                DocColor=jObj.getString("DocColor");
                DocFace=jObj.getString("DocFace");
                DocSize=jObj.getString("DocSize");
                DocGrid=jObj.getString("DocGrid");
                DocNum=jObj.getInt("DocNum");
                DocComplete=jObj.getInt("DocComplete");
                LocationNum=jObj.getInt("LocationNum");

                Log.i("test","ArticleNumber:"+studentNum+"Title:"+password);
                String stu_sql = "INSERT INTO Students(StudentNum,Password,Point)"
                        +" Values("+studentNum+','+password+','+point+");";
                String doc_sql = "INSERT INTO Documents(DocName,DocColor,DocFace,DocSize,DocGrid,DocNum,DocComplete,LocationNum)"
                        +" Values("+DocName+','+DocColor+','+DocFace+"+"+DocSize+"+"+DocGrid+"+"+DocNum+"+"+DocComplete+"+"+LocationNum+");";

                try{
                    database.execSQL(stu_sql);
                    database.execSQL(doc_sql);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }catch (JSONException e){
            Log.e("test", "JSON ERROR! -"+e);
            e.printStackTrace();
        }
    }

    public int Login(String id, String pw){
        String sql="Select * From Students Where StudentNum="+id+";";

        Cursor cursor = database.rawQuery(sql,null);
        int studentNum;
        String password = null;
        while(cursor.moveToNext()) {
            studentNum = cursor.getInt(1);
            password = cursor.getString(2);
          //  System.out.println(studentNum);
         //   System.out.println(password);
        }
        if(password!=null){
                if (password.equals(pw)){
                 return 0;
                 }else{
                 return 1;
                  }
            }else{
                return 2;
            }

        }




    //sql database에서 모든 데이터불러와서 array를 형성
    public ArrayList<List> getArticleList(){
       //ArrayList<Article> articleList = new ArrayList<>();
        int studentNum;
        String password;
        int point;
        String DocName;
        String DocColor;
        String DocFace;
        String DocSize;
        String DocGrid;
        int DocNum;
        int DocComplete;
        int LocationNum;

        String stu_sql="SELECT * FROM Students;";
        Cursor cursor = database.rawQuery(stu_sql,null);

        while(cursor.moveToNext()) {
            studentNum = cursor.getInt(1);
            password = cursor.getString(2);
            point = cursor.getInt(3);
        }

        String doc_sql="SELECT * FROM Documents;";
        cursor = database.rawQuery(doc_sql,null);


        while(cursor.moveToNext()) {

           DocName= cursor.getString(1);
        DocColor= cursor.getString(2);
           DocFace= cursor.getString(3);
        DocSize= cursor.getString(4);
           DocGrid= cursor.getString(5);
            DocNum= cursor.getInt(6);
            DocComplete= cursor.getInt(7);
          LocationNum= cursor.getInt(8);

        }

        return null;
    }

    //현재는 수동으로 json 데이터형성, 나중에는 서버에서 json 데이터 받아와야함
    public String getJsonTestData(){
        StringBuilder sb = new StringBuilder();
        sb.append("");

        sb.append("[");

        sb.append("      {");
        sb.append("         'StudentNum':'1',");
        sb.append("         'Password':'1234',");
        sb.append("         'Point':'1000',");
        sb.append("         'DocName':'name',");
        sb.append("         'DocColor':'yes',");
        sb.append("         'DocFace':'1',");
        sb.append("         'DocSize':'1',");
        sb.append("         'DocGrid':'1',");
        sb.append("         'DocNum':'1',");
        sb.append("         'DocComplete':'1',");
        sb.append("         'LocationNum':'1'");
        sb.append("      }");
        sb.append("]");

        return sb.toString();
    }
}
