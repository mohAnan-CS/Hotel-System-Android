package edu.birzeit.hotelproject.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.birzeit.hotelproject.MainActivity;
import edu.birzeit.hotelproject.R;
import edu.birzeit.hotelproject.models.Customer;
import edu.birzeit.hotelproject.services.LoginService;

public class SignUpActivity extends AppCompatActivity {
    EditText dateformats;
    int year, month, day;
    private String name;
    EditText nameEditText,usernameEditText,passwordEditText,visaEditText,phoneEditText;
    private String username;
    private String password;
    private String visa;
    private String phone;
    private String dateOfBirth;
    private String customerMessage;
    private List<Customer>customerList;
    public static String EXTRA_MESSAGE;
    private Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Intent intent=getIntent();
        customerMessage=intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        dateformats = findViewById(R.id.date_format);
        Calendar cal = Calendar.getInstance();
        dateformats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateformats.setText(SimpleDateFormat.getDateInstance().format(cal.getTime()));
                    }
                }, year, month, day);
                dpd.show();
            }
        });

        findViewById(R.id.text_signIn_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        setViews();


    }

    private void setViews() {
        nameEditText=findViewById(R.id.nameId);
        usernameEditText=findViewById(R.id.usernameId);
        passwordEditText=findViewById(R.id.passwordId);
        visaEditText=findViewById(R.id.visaId);
        phoneEditText=findViewById(R.id.phoneId);
    }
    private void getFieldsString(){
        name=nameEditText.getText().toString();
        username=usernameEditText.getText().toString();
        password=passwordEditText.getText().toString();
        visa=passwordEditText.getText().toString();
        phone=phoneEditText.getText().toString();
        dateOfBirth=dateformats.getText().toString();
    }


    public void createAccount(View view) {
        getFieldsString();
        if (name.length()==0 || username.length()==0 || password.length()==0 || visa.length()==0 || phone.length()==0 || dateOfBirth.length()==0) {
            Toast.makeText(SignUpActivity.this, "please fill required data", Toast.LENGTH_LONG).show();
        }else {
            getCustomers();
            if (LoginService.isCustomer(username,password,customerList)){
                Toast.makeText(SignUpActivity.this, "this is already existed account", Toast.LENGTH_LONG).show();
            }else {
                if (LoginService.isUsernameUsed(username,customerList)){
                    Toast.makeText(SignUpActivity.this, "username is already used ,use another username", Toast.LENGTH_LONG).show();
                }else{
                    Customer customer=new Customer(name,username,password,visa,phone,dateOfBirth);
                    PostCustomer postCustomer=new PostCustomer(customer);
                    Thread thread=new Thread(postCustomer);
                    thread.start();
                }
            }
        }



    }
    private void getCustomers(){
        customerList=new ArrayList<>();
        Customer[]customerArray=gson.fromJson(customerMessage,Customer[].class);
        for (Customer customer : customerArray) {
            customerList.add(customer);
        }

    }


    class PostCustomer implements Runnable{
        Customer customer;

        PostCustomer(Customer customer){
            this.customer=customer;
        }



        @Override
        public void run() {
            String url="http://10.0.2.2/hotel_app_backend/controllers/customerController/post.php";
            RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
            StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("TAG", "RESPONSE IS " + response);
                    if (response.equalsIgnoreCase("New record created successfully")){
                        Toast.makeText(SignUpActivity.this,"cretaed account succefully",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
                        String message=gson.toJson(customer);
                        intent.putExtra(EXTRA_MESSAGE,message);
                        startActivity(intent);
                    }

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // method to handle errors.
                    Toast.makeText(SignUpActivity.this,
                            "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                    Log.d("errorr",error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    // as we are passing data in the form of url encoded
                    // so we are passing the content type below
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                protected Map<String, String> getParams() {

                    // below line we are creating a map for storing
                    // our values in key and value pair.
                    Map<String, String> params = new HashMap<String, String>();

                    // on below line we are passing our
                    // key and value pair to our parameters.

                    params.put("customer_name", name);
                    params.put("customer_username",username);
                    params.put("customer_password",password);
                    params.put("customer_visa",visa);
                    params.put("customer_phone", phone);
                    params.put("dateOfBirth",dateOfBirth);



                    // at last we are returning our params.
                    return params;
                }
            };
            // below line is to make
            // a json object request.
            queue.add(request);
        }
        }
    }
