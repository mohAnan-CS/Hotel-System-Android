package edu.birzeit.hotelproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

import edu.birzeit.hotelproject.controller.AvailableRoomsActivity;
import edu.birzeit.hotelproject.models.Customer;
import edu.birzeit.hotelproject.models.Receptionist;
import edu.birzeit.hotelproject.services.LoginService;
import edu.birzeit.hotelproject.controller.AboutUsActivity;
import edu.birzeit.hotelproject.controller.HomePageActivity;
import edu.birzeit.hotelproject.controller.ReceptionMenue;
import edu.birzeit.hotelproject.controller.SignUpActivity;

public class MainActivity extends AppCompatActivity {
    EditText usernameEditText, passwordEditText;
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String CHECK_ACCOUNT = "CHECK_ACCOUNT";
    public static final String HOTEL_SHARED = "HOTEL_SHARED";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    List<Receptionist> receptionistList = new ArrayList<>();
    List<Customer> customerList = new ArrayList<>();
    private RequestQueue queue;
    String urlReceptions = "http://10.0.2.2/hotel_app_backend/controllers/receptionController/get.php";
    String urlCustomers = "http://10.0.2.2/hotel_app_backend/controllers/customerController/get.php";
    Gson gson = new Gson();
    String customers;
    public static String EXTRA_MESSAGE;
   String getExtraMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        queue = Volley.newRequestQueue(this);
        setViews();



        setSharedPref();
        checkPreference();

        Intent intent = getIntent();
        String message=intent.getStringExtra(SignUpActivity.EXTRA_MESSAGE);
        if (message!=null){
            Customer customer=gson.fromJson(message,Customer.class);
            usernameEditText.setText(customer.getCustomer_username());
            passwordEditText.setText(customer.getCustomer_password());
        }

        GetData getData = new GetData();
        Thread thread = new Thread(getData);
        thread.start();

        findViewById(R.id.text_create_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                intent.putExtra(EXTRA_MESSAGE, customers);
                startActivity(intent);
            }
        });
    }

    private void setViews() {
        usernameEditText = findViewById(R.id.usernameId);
        passwordEditText = findViewById(R.id.passwordId);
    }

    private void setSharedPref() {
        preferences = getSharedPreferences(HOTEL_SHARED, MODE_PRIVATE);
        editor = preferences.edit();
    }

    private void checkPreference() {
        String username = preferences.getString(USERNAME, "");
        String password = preferences.getString(PASSWORD, "");
        usernameEditText.setText(username);
        passwordEditText.setText(password);
    }


    public void go(View view) {
        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
    }

    // this is handler when user click log in
    public void signin_btn(View view) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (LoginService.isReceptionist(username, password, receptionistList)) {
            Log.d("username", username);
            editor.putString(USERNAME, username);
            editor.putString(PASSWORD, password);
            editor.putString(CHECK_ACCOUNT, "reception");
            editor.commit();
            Intent intent = new Intent(this, ReceptionMenue.class);
            intent.putExtra(EXTRA_MESSAGE, customers);
            startActivity(intent);
        } else {
            if (LoginService.isCustomer(username, password, customerList)) {
                editor.putString(USERNAME, username);
                editor.putString(PASSWORD, password);
                editor.putString(CHECK_ACCOUNT, "customer");
                editor.commit();
                Intent intent = new Intent(this, AvailableRoomsActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "you have to create account",
                        Toast.LENGTH_LONG).show();
            }
        }

        // now i will asume the customer login is successful

    }


    private void getReceptionsList() {
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlReceptions,
                new Response.Listener<String>() {

                    JSONObject jsnobject = null;
                    JSONArray jsonArray = null;

                    @Override
                    public void onResponse(String response) {
                        try {
                            jsnobject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            jsonArray = jsnobject.getJSONArray("receptionist");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JSONArray finalJsonArray = jsonArray;
                        String g = finalJsonArray.toString();
                        Receptionist[] receptionistsArray = gson.fromJson(g, Receptionist[].class);
                        for (Receptionist receptionist : receptionistsArray) {
                            receptionistList.add(receptionist);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);


    }

    public void getCustomersList() {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlCustomers,
                new Response.Listener<String>() {

                    JSONObject jsnobject = null;
                    JSONArray jsonArray = null;

                    @Override
                    public void onResponse(String response) {
                        try {
                            jsnobject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            jsonArray = jsnobject.getJSONArray("customers");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JSONArray finalJsonArray = jsonArray;
                        String g = finalJsonArray.toString();
                        customers = g;
                        Customer[] customersArray = gson.fromJson(g, Customer[].class);
                        for (Customer customer : customersArray) {
                            customerList.add(customer);
                            Log.d("customer length", customerList.size() + "");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);


    }

    class GetData implements Runnable {

        @Override
        public void run() {
            getReceptionsList();
            getCustomersList();

        }
    }
}