package com.example.a15056112.c302_dmsdchatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    TextView tvTitle;
    ListView lv;
    ArrayList<Chat> alChat;
    ChatAdapter aa;

    private Chat chat;

    EditText etMessage;
    Button btnAdd;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userReference, nameReference;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        etMessage = (EditText)findViewById(R.id.etMessage);
        btnAdd = (Button)findViewById(R.id.btnAddMessage);
        lv = (ListView)findViewById(R.id.lv);
        tvTitle = (TextView)findViewById(R.id.tvTitle);

        alChat = new ArrayList<Chat>();

        aa = new ChatAdapter(ChatActivity.this, R.layout.row, alChat);
        lv.setAdapter(aa);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference("profiles/");



        nameReference = firebaseDatabase.getReference("/messages");

        HttpRequest request = new HttpRequest("https://api.data.gov.sg/v1/environment/2-hour-weather-forecast");
        request.setMethod("GET");
        request.setAPIKey("api-key","XyoGJLm9Hnzz9SVbNUGTfMelufGyUltW");
        request.execute();

        try {
            String jsonString = request.getResponse();

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray items = jsonObject.getJSONArray("items");
            JSONObject itemsJSONObject = items.getJSONObject(0);
            JSONArray forecasts = itemsJSONObject.getJSONArray("forecasts");


            for (int i = 0; i <forecasts.length(); i++) {
                JSONObject forecast = forecasts.getJSONObject(i);
                String area = forecast.getString("area");

                if (area.equalsIgnoreCase("Woodlands")) {
                    String forecastDescription = forecast.getString("forecast");
                    tvTitle.setText("Weather forecast @ woodlands: " + forecastDescription);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String messageText = etMessage.getText().toString();
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                Date currentDate = Calendar.getInstance().getTime();
                final String strTime = String.valueOf(currentDate);

                userReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userName = dataSnapshot.child(user.getUid()).getValue().toString();
                        Chat chat = new Chat(messageText,strTime,userName);
                        nameReference.push().setValue(chat);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

        nameReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                alChat.clear();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    Chat postMessage = dataSnapshot1.getValue(Chat.class);
                    alChat.add(postMessage);
                }

                for (int i=0; i< alChat.size(); i++) {
                    aa = new ChatAdapter(ChatActivity.this,R.layout.row, alChat);

                }
                lv.setAdapter(aa);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            Intent i = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
