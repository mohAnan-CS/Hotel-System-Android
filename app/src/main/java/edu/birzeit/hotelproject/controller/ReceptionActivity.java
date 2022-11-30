package edu.birzeit.hotelproject.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.birzeit.hotelproject.R;
import edu.birzeit.hotelproject.models.Receptionist;

public class ReceptionActivity extends AppCompatActivity {

    List<String>list=new ArrayList<>();
    private ListView receptionsList;
    private RequestQueue queue;

    String url = "http://10.0.2.2/hotel_app_backend/controllers/receptionController/get.php";
    Gson gson=new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reception);
        receptionsList = findViewById(R.id.lstReceptions);
        queue = Volley.newRequestQueue(this);

        setReceptionsList();

    }


    public void setReceptionsList(){


// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    JSONObject  jsnobject =null;
                    JSONArray  jsonArray=null;
                    @Override
                    public void onResponse(String response) {
                        try {
                           jsnobject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            jsonArray = jsnobject.getJSONArray("receptionist");
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                      JSONArray  finalJsonArray = jsonArray;
                        String g = finalJsonArray.toString();
                        Receptionist[] receptionistsArray = gson.fromJson(g, Receptionist[].class);

                        for (Receptionist receptionist : receptionistsArray) {
                            list.add(receptionist.getName());
                        }

                        String[]arr=new String[list.size()];
                        arr=list.toArray(arr);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                ReceptionActivity.this, android.R.layout.simple_list_item_1,
                                arr);
                        receptionsList.setAdapter(adapter);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }

        });
        queue.add(stringRequest);


    }


}