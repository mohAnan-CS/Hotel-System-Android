package edu.birzeit.hotelproject.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.birzeit.hotelproject.R;
import edu.birzeit.hotelproject.models.Room;


public class RoomActivity extends AppCompatActivity {

    private int[] imageViewArr ;
    private String[] pricesArr ;
    private String[] roomTypeArr ;

    private ListView roomListView;
    private RequestQueue queue;
    public static final String HOTEL_SHARED = "HOTEL_SHARED";
    public static final String CHECK_ACCOUNT = "CHECK_ACCOUNT";
    private Gson gson = new Gson();
    private List<String> rooms = new ArrayList<>();

    String url = "http://10.0.2.2:80/hotel_app_backend/controllers/RoomController/get.php";
    List<Room>roomList=new ArrayList<>();
    List<Room>singleRoom,doubleRoom;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        BottomNavigationView BNV = findViewById(R.id.nav_id);

        BNV.setSelectedItemId(R.id.roomsBooking);

        BNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.homepage:
                        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.roomsBooking:
                        return true;

                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.contactus:
                        startActivity(new Intent(getApplicationContext(), ContactUsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;


                    case R.id.logout:
                        startActivity(new Intent(getApplicationContext(), LogoutActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        queue = Volley.newRequestQueue(this);
        roomListView=findViewById(R.id.listViewRoom);
        GetData getData=new GetData();
        Thread thread=new Thread(getData);
        thread.start();

    }


    class GetData implements Runnable{

        @Override
        public void run() {
         getRooms();

        }
    }

    class CollectRoomType implements Runnable{

        @Override
        public void run() {

            singleRoom=new ArrayList<>();
            doubleRoom=new ArrayList<>();

            for (Room room : roomList) {
                if (room.getRoom_type().equalsIgnoreCase("single")){
                    singleRoom.add(room);
                }else{
                    doubleRoom.add(room);
                }
            }
            Log.d("length single room",singleRoom.size()+"");
        }
    }

    public void getRooms() {

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

            //Response is a string that needs to be converted to an a json object then json array
                    JSONObject  jsnobject =null;
                    JSONArray  jsonArray=null;
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsnobject = new JSONObject(response);
                            Log.d("room json object",jsnobject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            jsonArray = jsnobject.getJSONArray("rooms");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JSONArray  finalJsonArray = jsonArray;
                        String g = finalJsonArray.toString();
                        Log.d("room messages",g);
                        Room[] roomsArray = gson.fromJson(g, Room[].class);

                        setSharedPref();
                        //Please Work
                        //Hi aljkshdlkasjdlaksjdlasdj
                        for (Room room : roomsArray) {
                            if (preferences.getString(CHECK_ACCOUNT,"").equalsIgnoreCase("customer")){
                                if (room.isRoom_reserve() == 0){
                                    rooms.add("Number : " + room.getRoom_number() + " " + "Type :" + room.getRoom_type() + "  price : " + room.getRoom_price());
                                    roomList.add(room);
                                }
                            }
                            else{
                                rooms.add("Number : " + room.getRoom_number() + " " + "Type :" + room.getRoom_type() + "  price : " + room.getRoom_price());
                                roomList.add(room);
                            }

                        }

                        String[]arr=new String[rooms.size()];
                        arr=rooms.toArray(arr);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                RoomActivity.this, android.R.layout.simple_list_item_1,
                                arr);
                        roomListView.setAdapter(adapter);
                        CollectRoomType collectRoomType=new CollectRoomType();
                        Thread thread=new Thread(collectRoomType);
                        thread.start();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);

    }


    class RoomReceptionAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return imageViewArr.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = getLayoutInflater().inflate(R.layout.card, viewGroup, false);
            ImageView imageViewRoom = view.findViewById(R.id.imageViewRoomRes);
            TextView textViewPrice = view.findViewById(R.id.textViewRoomPrice);
            TextView textViewType = view.findViewById(R.id.textViewRoomType);
            textViewPrice.setText(pricesArr[i]);
            textViewType.setText(roomTypeArr[i]);
            imageViewRoom.setImageResource(imageViewArr[i]);

            //return null;
            return view;
        }


    }


    private void setSharedPref() {
        preferences = getSharedPreferences(HOTEL_SHARED,MODE_PRIVATE);
        editor = preferences.edit();
    }


}