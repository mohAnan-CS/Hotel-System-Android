package edu.birzeit.hotelproject.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import edu.birzeit.hotelproject.R;

public class AvailableRoomsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_rooms);
    }

    //1- Get all reservations in ArrayList
    //2- Get all rooms ?!
    //3- For loop for the ArrayList above

    //Select * FROM room,reservation where room_id = reservation.room_id not in (select * from...);

    //This is a comment?!@>?!?@!>?@>!?@>?!@>!?>
}