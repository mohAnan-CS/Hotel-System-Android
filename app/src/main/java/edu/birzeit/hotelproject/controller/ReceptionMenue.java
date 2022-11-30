package edu.birzeit.hotelproject.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import edu.birzeit.hotelproject.MainActivity;
import edu.birzeit.hotelproject.R;

public class ReceptionMenue extends AppCompatActivity {
    private ListView listView;
    static String CUSTOMERS_MESSAGE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reception_menue);
        Intent intent = getIntent();
        String message=intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(ReceptionMenue.this, RoomActivity.class);
                        startActivity(intent);
                        break;

                    case 1:
                        Intent intent1 = new Intent(ReceptionMenue.this, customerActivity.class);
                        intent1.putExtra(CUSTOMERS_MESSAGE,message);
                        startActivity(intent1);
                        break;

                }
            }
        };
        ListView listView = (ListView)findViewById(R.id.listChoices);
        listView.setOnItemClickListener(itemClickListener);



        }

    }

