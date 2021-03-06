package com.example.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditTask extends AppCompatActivity {
    private Button edit;
    private EditText title,dates,time;
    SessionManager sessionManager;
    private ProgressBar loading;

    private static String URL_EDIT_TASK = "http://mfarrasm.000webhostapp.com/JSON/api_editing_task.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Editing an activity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final EditText edittext= (EditText) findViewById(R.id.dateTextE);

        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });



        //time

        final EditText Edit_Time = (EditText) findViewById(R.id.timeTextE);

        Edit_Time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Edit_Time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        //go

        title = findViewById(R.id.titleTextE);
        dates = findViewById(R.id.dateTextE);
        time = findViewById(R.id.timeTextE);

        edit = findViewById(R.id.editE);
        loading=findViewById(R.id.loadingEdit);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m_title = title.getText().toString().trim();
                String m_date = dates.getText().toString().trim();
                String m_time = time.getText().toString().trim();

                int a =0 ;

                if (m_title.isEmpty()){
                    title.setError("What do you want to do ?");
                    a =1;
                }

                if (m_date.isEmpty()||m_time.isEmpty()){
                    Toast.makeText(EditTask.this,"Due date / time cannot be empty",Toast.LENGTH_SHORT).show();
                    a =1;
                }

                if (a==0){
                    editKegiatan();
                }
            }
        });
    }

    public void showDateTimePicker(){
        final Calendar currentDate = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                currentDate.set(year, monthOfYear, dayOfMonth);

                final Calendar myCalendar = Calendar.getInstance();

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "EEEE, d MMMM yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                EditText edittext= (EditText) findViewById(R.id.dateTextE);
                edittext.setText(sdf.format(myCalendar.getTime()));
                //use this date as per your requirement
            }
        };
        DatePickerDialog datePickerDialog = new  DatePickerDialog(EditTask.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),   currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void editKegiatan(){
        loading.setVisibility(View.VISIBLE);
        edit.setVisibility(View.GONE);

        sessionManager = new SessionManager(this);
        HashMap<String,String> user = sessionManager.getUserDetail();

        final String id_userz = user.get(sessionManager.ID_USER);
        final String id_kegiatanS= getIntent().getStringExtra("IDAct");
        final String titlez = this.title.getText().toString().trim();
        final String datez = this.dates.getText().toString().trim();
        final String timez = this.time.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_TASK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success =  jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(EditTask.this,"Activity has been changed",Toast.LENGTH_SHORT).show();
                                Intent intents = new Intent(EditTask.this, Home.class);
                                intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intents);
                                finish();
                            }
                            if (success.equals("0")){
                                Toast.makeText(EditTask.this,"There is already an activity on "+datez+", "+timez,Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                edit.setVisibility(View.VISIBLE);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(EditTask.this,"Edit failed, please retry..",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditTask.this,"Connection error, failed to edit activity",Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        edit.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String>params = new HashMap<>();
                params.put("id_userZ",id_userz);
                params.put("id_kegiatanZ",id_kegiatanS);
                params.put("titleZ",titlez);
                params.put("dateZ",datez);
                params.put("timeZ",timez);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
