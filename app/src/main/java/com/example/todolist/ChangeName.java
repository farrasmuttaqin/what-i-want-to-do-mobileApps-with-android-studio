package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangeName extends AppCompatActivity {

    private Button changeNameNow;
    private TextView nameTitle;
    private EditText changeThis;
    SessionManager sessionManager;
    private ProgressBar loading;

    private static String URL_CHANGE_NAME = "http://mfarrasm.000webhostapp.com/JSON/api_change_name.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Change my name");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changeNameNow = findViewById(R.id.changeNameNow);
        changeThis = findViewById(R.id.changeMyName);

        loading=findViewById(R.id.loadingAdd222);

        sessionManager = new SessionManager(this);
        HashMap<String,String> user = sessionManager.getUserDetail();
        final String fullName = user.get(sessionManager.FULLNAME);

        nameTitle = findViewById(R.id.nameTitle);
        nameTitle.setText("Your name is "+fullName);



        changeNameNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m_title_name = changeThis.getText().toString().trim();
                int a =0 ;

                if (m_title_name.isEmpty()||m_title_name.length()<4){
                    changeThis.setError("Please Input Valid Full Name ");
                    a =1;
                }

                if (a==0){
                    changeName();
                }
            }
        });
    }

    public void changeName(){
        loading.setVisibility(View.VISIBLE);
        changeNameNow.setVisibility(View.GONE);

        sessionManager = new SessionManager(this);
        HashMap<String,String> user = sessionManager.getUserDetail();

        final String id_userz = user.get(sessionManager.ID_USER);
        final String nama_lengkap = this.changeThis.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHANGE_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success =  jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(ChangeName.this,"Your name has been changed",Toast.LENGTH_SHORT).show();

                                sessionManager.createSession(nama_lengkap,id_userz);
                                Intent intents = new Intent(ChangeName.this,Home.class);
                                intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intents);
                                finish();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(ChangeName.this,"Change name failed, please retry..",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChangeName.this,"Connection error, failed to change name",Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        changeNameNow.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String>params = new HashMap<>();
                params.put("id_userZ",id_userz);
                params.put("nama_lengkap",nama_lengkap);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}


