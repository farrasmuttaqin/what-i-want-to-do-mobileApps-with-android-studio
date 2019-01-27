package com.example.todolist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    boolean doubleBackToExitPressedOnce = false;



    SessionManager sessionManager;
    private static String URL_ALL_TASK = "http://mfarrasm.000webhostapp.com/JSON/api_all_task.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, AddTask.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        HashMap<String,String> user = sessionManager.getUserDetail();
        String fullName = user.get(sessionManager.FULLNAME);
        String id_user = user.get(sessionManager.ID_USER);


        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
        View headerView = nav.getHeaderView(0);
        Button change_name = (Button) headerView.findViewById(R.id.change_name);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nama_user);
        navUsername.setText("Hi, "+fullName);

        change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, ChangeName.class);
                startActivity(i);
            }
        });

        //

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        navigationView.getMenu().getItem(0).setChecked(true);

        loadRecyclerViewData(id_user);
    }


    private void loadRecyclerViewData(final String id_user){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Hold on, we're fetching your activities");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ALL_TASK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("records");

                            JSONObject object;
                            String tanggal,jam,deskripsi,status,id_kegiatan;

                            TextView z = (TextView) findViewById(R.id.textViewStatus);
                            int jumlah,i,tampung=0;
                            for (jumlah = 0; jumlah < jsonArray.length(); jumlah++) {
                                tampung=1;
                                for (i = 0; i < jsonObject.length(); i++) {

                                    object = jsonArray.getJSONObject(jumlah);
                                    tanggal = object.getString("tanggal").trim();
                                    jam = object.getString("jam").trim();
                                    deskripsi = object.getString("deskripsi").trim();
                                    status = object.getString("status").trim();
                                    id_kegiatan = object.getString("id_kegiatan").trim();

                                    if (status.equals("0")){
                                        status = "Uncompleted";
                                    }else{
                                        status = "Completed";
                                    }
                                    ListItem item = new ListItem(deskripsi,"on "+tanggal+" at "+jam,status,id_kegiatan);

                                    listItems.add(item);

                                }
                            }

                            if (tampung == 0){
                                Toast.makeText(Home.this, "You don't have any activities", Toast.LENGTH_LONG).show();
                            }
                            adapter = new MyAdapter(listItems,getApplicationContext());
                            recyclerView.setAdapter(adapter);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (id_user != null){
                            Toast.makeText(Home.this, "Connection error, please check your connection and restart the application", Toast.LENGTH_LONG).show();
                        }

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id_user",id_user);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit from what i want to do?", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_all) {
            Intent intents1 = new Intent(Home.this,Home.class);
            intents1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intents1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intents1);
            finish();
        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "What i want to do?");
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));

        } else if (id == R.id.logout) {
            sessionManager.logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
