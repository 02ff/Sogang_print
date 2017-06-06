package org.androidtown.sogang_print;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.androidtown.sogang_print.R.id.imageView;

/**
 * Created by KoJunghwan on 2016-11-30.
 */
class ListData{
    public String getLocation() {
        return location;
    }

    public String getDocname() {
        return docname;
    }

    public String getIng() {
        return ing;
    }

    public String getColor() {
        return color;
    }

    public String getFace() {
        return face;
    }

    public String getSides() {
        return sides;
    }

    public String getNum() {
        return num;
    }

    public String getId() {
        return id;
    }

    private String id;
    private String location;
    private String docname;
    private String ing;
    private String color;
    private String face;
    private String sides;
    private String num;
    private String name_id;

    public String getName_id() {
        return name_id;
    }

    ListData(String id, String location, String docname, String color, String face, String sides, String num, String ing, String name_id){
        this.location=location;
        this.docname=docname;
        this.ing=ing;
        this.color=color;
        this.face=face;
        this.sides=sides;
        this.num=num;
        this.id=id;
        this.name_id=name_id;
    }

}

public class CustomAdapter extends ArrayAdapter<ListData> implements MyEventListener{
private Context context;
    private int layoutResourceId;
    private ArrayList<ListData> listData;
    private Client C;
    String[] param;

    public CustomAdapter(Context context, int layoutResourceId, ArrayList<ListData> listData) {
        super(context, layoutResourceId,listData);
        this.context=context;
        this.layoutResourceId=layoutResourceId;
        this.listData=listData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;

        if (row ==null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId,parent,false);
        }

        TextView tvText0 = (TextView)row.findViewById(R.id.loc_name);
        TextView tvText1 = (TextView)row.findViewById(R.id.doc_name);
        TextView tvText2 = (TextView)row.findViewById(R.id.ing);

       ImageView tvText3 = ( ImageView)row.findViewById(R.id.direction);
        ImageView tvText4 = ( ImageView)row.findViewById(R.id.color);
        ImageView tvText5 = ( ImageView)row.findViewById(R.id.face);

        TextView tvText6 = (  TextView )row.findViewById(R.id.num);

        Button lvButton = (Button)row.findViewById(R.id.accept_button) ;
        lvButton.setTag(position);
        lvButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int position = (Integer)v.getTag();

                notifyDataSetChanged();
                param = new String[] {listData.get(position).getLocation(), listData.get(position).getId() ,listData.get(position).getName_id()};
                startEvent();
                listData.remove(position);

            }
        });
        tvText0.setText(listData.get(position).getLocation());
        tvText1.setText(listData.get(position).getDocname());
        tvText2.setText(listData.get(position).getIng());

        tvText6.setText(listData.get(position).getNum());


       // tvText3 .setText(listData.get(position).getColor());
       // tvText4.setText(listData.get(position).getFace());
      //  tvText5.setText(listData.get(position).getSides());



try{
InputStream is = context.getAssets().open(listData.get(position).getColor()+".png");
Drawable d = Drawable. createFromStream(is,null);
tvText3.setImageDrawable(d);


    is = context.getAssets().open(listData.get(position).getFace()+".png");
   d = Drawable. createFromStream(is,null);
    tvText4.setImageDrawable(d);


    is = context.getAssets().open(listData.get(position).getSides()+".png");
  d = Drawable. createFromStream(is,null);
    tvText5.setImageDrawable(d);
} catch (IOException e) {
    e.printStackTrace();
}


        return row;
    }


    public void startEvent(){



        C=new Client("doc_del",param,this);

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

    }

    @Override
    public void onEventFailed() {

    }
}
