package org.androidtown.sogang_print;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.navdrawer.SimpleSideDrawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;

import static org.androidtown.sogang_print.R.color.red;

public class _1_My_page extends AppCompatActivity
        implements View.OnClickListener, MyEventListener, NavigationView.OnNavigationItemSelectedListener {

    private Button go_to_login;
    private Button go_to_reservation;
    private ImageButton location_but;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private Client C;

    private ArrayList<ListData> listDataArray = new ArrayList<>();

    private ImageButton menubtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);


        location_but = (ImageButton) findViewById(R.id.location_button);
        location_but.setOnClickListener(this);

        pref = getSharedPreferences("pref", 0);
        editor = pref.edit();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_main, null);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("MY PAGE");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_charge_point) {
            // Handle the nav_charge_point action

        } else if (id == R.id.nav_log_out) {
            Intent next;
            editor.putBoolean("autoLogin", false);
            editor.commit();
            next = new Intent(this, _0_MainActivity.class);
            startActivity(next);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View v) {
        Intent next;
        switch (v.getId()) {
            case R.id.location_button:
                listDataArray.clear();
                startEvent(); //문서 갖고온듯


                next = new Intent(this, _2_Select_location.class);
                startActivity(next);
        }
    }


    public void startEvent() {


        String studentNum = pref.getString("id", "");
        System.out.println(studentNum);
        String[] params = {studentNum};


        C = new Client("location", params, this);
        C.execute();

    }

    @Override
    public void onEventCompleted() {
        String id= pref.getString("id","");
        String output = C.output;
        System.out.println(output);


        try {
            JSONArray jar = new JSONArray(output);
            for(int i=0;i<jar.length();i++){

                JSONObject j = jar.getJSONObject(i);
                Iterator it= j.keys();
                String loc=null;
                JSONObject item = null;
                String name=null;
                while(it.hasNext()){
                  String key = (String) it.next();
                    if (key.equals("loc")) {
                          loc = j.getString(key);
                       }else{
                        name = key;
                           item = j.getJSONObject(key);
                      }
                }
                ListData data = new ListData(id, loc,item.getString("name").toString(),item.getString("dir").toString(),item.getString("col").toString(),item.getString("face").toString(),item.getString("num").toString(),item.getString("ing").toString(),name);
                listDataArray.add(data);
            }

            ListView listView = (ListView)findViewById(R.id.listView1);
            CustomAdapter customAdapter = new CustomAdapter(this, R.layout.my_page_layout,listDataArray);
            listView.setAdapter(customAdapter);
            } catch (JSONException e1) {
            e1.printStackTrace();
        }

      //  Toast.makeText(getApplicationContext(), job.getString("color"), Toast.LENGTH_LONG).show();


    }

    @Override
    public void onEventFailed() {

    }
}