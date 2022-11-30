package edu.birzeit.hotelproject.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.birzeit.hotelproject.R;
import edu.birzeit.hotelproject.models.Customer;

public class customerActivity extends AppCompatActivity {
    List<Customer>customerList;
    List<String> customerStrings;
    Gson gson=new Gson();
    ListView listView;
    String cusString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer);
        Intent intent=getIntent();
        cusString=intent.getStringExtra(ReceptionMenue.CUSTOMERS_MESSAGE);
        listView=findViewById(R.id.lstcustomers);
        getCustomers();

    }

    public void getCustomers(){
        customerList=new ArrayList<>();
        customerStrings =new ArrayList<>();

                        Customer[]customersArray=gson.fromJson(cusString,Customer[].class);

                        for (Customer customer : customersArray) {
                            customerStrings.add(customer.getCustomer_name()+ " " + customer.getCustomer_username());
                            customerList.add(customer);
                        }

                        String[]arr=new String[customerStrings.size()];
                        arr=customerStrings.toArray(arr);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                customerActivity.this, android.R.layout.simple_list_item_1,
                                arr);
                        listView.setAdapter(adapter);


    }



}