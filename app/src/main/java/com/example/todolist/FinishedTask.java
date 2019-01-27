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

public class FinishedTask extends AppCompatActivity {

    TextView textTitle,textDate;

    private ProgressBar loading;
    Button deleteS;

    private static String URL_DELETE = "http://mfarrasm.000webhostapp.com/JSON/api_delete_task.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_finished_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Completed activity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final String deskripsi= getIntent().getStringExtra("DesAct");
        final String tanggal= getIntent().getStringExtra("DateAct");
        final String id_kegiatanS= getIntent().getStringExtra("IDAct");

        textTitle = findViewById(R.id.textTitleF);
        textDate = findViewById(R.id.textDateF);

        textTitle.setText("'' "+deskripsi+" ''");
        textDate.setText(tanggal);

        deleteS = findViewById(R.id.deleteS);
        loading=findViewById(R.id.loadingDelete);

        deleteS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteActivity(id_kegiatanS,deskripsi);
            }
        });

    }

    public void deleteActivity(final String id_kegiatanS,final String deskripsi){
        loading.setVisibility(View.VISIBLE);
        deleteS.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success =  jsonObject.getString("success");
                            if (success.equals("1")){
                                Toast.makeText(FinishedTask.this,  deskripsi+" has been deleted", Toast.LENGTH_SHORT).show();
                                Intent intents = new Intent(FinishedTask.this,Home.class);
                                intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intents);
                                finish();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FinishedTask.this,"Connection error, failed to delete activity",Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
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
