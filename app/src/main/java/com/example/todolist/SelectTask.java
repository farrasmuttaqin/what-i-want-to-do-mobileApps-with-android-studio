package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SelectTask extends AppCompatActivity {

    TextView textTitle,textDate;

    private ProgressBar loading1,loading2,loading3;

    Button editS,finishS,deleteS;

    private static String URL_FINISH = "http://mfarrasm.000webhostapp.com/JSON/api_finish_task.php";
    private static String URL_DELETE = "http://mfarrasm.000webhostapp.com/JSON/api_delete_task.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loading1=findViewById(R.id.loading1);
        loading2=findViewById(R.id.loading2);
        loading3=findViewById(R.id.loading3);
        editS = findViewById(R.id.editS);
        finishS = findViewById(R.id.finishS);
        deleteS = findViewById(R.id.deleteS);

        getSupportActionBar().setTitle("Uncompleted activity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String deskripsi= getIntent().getStringExtra("DesAct");
        final String tanggal= getIntent().getStringExtra("DateAct");
        final String id_kegiatanS= getIntent().getStringExtra("IDAct");

        textTitle = findViewById(R.id.textTitle);
        textDate = findViewById(R.id.textDate);

        textTitle.setText("'' "+deskripsi+" ''");
        textDate.setText(tanggal);

        editS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editActivity(id_kegiatanS,deskripsi,tanggal);
            }
        });



        finishS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity(id_kegiatanS,deskripsi);
            }
        });



        deleteS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteActivity(id_kegiatanS,deskripsi);
            }
        });

    }

    public void editActivity(final String id_kegiatanS, String deskripsi, String tanggal){
        Intent intent = new Intent(SelectTask.this,EditTask.class);
        intent.putExtra("DesAct", deskripsi);
        intent.putExtra("DateAct", tanggal);
        intent.putExtra("IDAct", id_kegiatanS);
        startActivity(intent);
    }


    public void finishActivity(final String id_kegiatanS, final String deskripsi){
        loading2.setVisibility(View.VISIBLE);
        finishS.setVisibility(View.GONE);
        editS.setVisibility(View.GONE);
        deleteS.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FINISH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success =  jsonObject.getString("success");
                            if (success.equals("1")){
                                Toast.makeText(SelectTask.this,  deskripsi+" has been finished", Toast.LENGTH_SHORT).show();
                                Intent intents = new Intent(SelectTask.this,Home.class);
                                intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intents);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SelectTask.this,"Connection error, failed to finish activity",Toast.LENGTH_SHORT).show();
                        loading2.setVisibility(View.GONE);
                        finishS.setVisibility(View.VISIBLE);
                        editS.setVisibility(View.VISIBLE);
                        deleteS.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id_kegiatan",id_kegiatanS);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void deleteActivity(final String id_kegiatanS,final String deskripsi){
        loading3.setVisibility(View.VISIBLE);
        finishS.setVisibility(View.GONE);
        editS.setVisibility(View.GONE);
        deleteS.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success =  jsonObject.getString("success");
                            if (success.equals("1")){
                                Toast.makeText(SelectTask.this,  deskripsi+" has been deleted", Toast.LENGTH_SHORT).show();
                                Intent intents = new Intent(SelectTask.this,Home.class);
                                intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intents);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SelectTask.this,"Connection error, failed to delete activity",Toast.LENGTH_SHORT).show();
                        loading3.setVisibility(View.GONE);
                        finishS.setVisibility(View.VISIBLE);
                        editS.setVisibility(View.VISIBLE);
                        deleteS.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id_kegiatan",id_kegiatanS);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
