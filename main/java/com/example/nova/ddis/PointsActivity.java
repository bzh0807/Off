package com.example.nova.ddis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class PointsActivity extends MainActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);

        setSupportActionBar(toolbar);
        tv = (TextView) findViewById(R.id.numPoints);
        String pointValue = readFromFile(getApplicationContext(), "config.txt");
        tv.setText(pointValue);
        registerReceiver(uiUpdated, new IntentFilter("LOCATION_UPDATED"));
        startService(new Intent(this, PointService.class));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);

        Button butt = (Button)findViewById(R.id.button1);
        butt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PointsActivity.this);
                                        alertDialog.setMessage("100 points for 5% off Brazo Tacos.\n Would you like to redeem this prize?");
                                        alertDialog.setTitle("Brazo Taco Rewards");
                                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                               int oldNum = Integer.parseInt(tv.getText().toString());
                                                if(oldNum<100){
                                                    return;
                                                }
                                                String newNum = (oldNum - 100)+"";
                                                tv.setText(newNum);
                                                writeToFile(newNum,getApplicationContext(), "config.txt");

                                            }});
                                        alertDialog.setNegativeButton("Cancel", null);
                                        alertDialog.show();
                                    }
                                });

        Button butt2 = (Button)findViewById(R.id.button2);
        butt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PointsActivity.this);
                alertDialog.setMessage("100 points for 7% off Chipotle.\n Would you like to redeem this prize?");
                alertDialog.setTitle("Chipotle Rewards");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int oldNum = Integer.parseInt(tv.getText().toString());
                        if(oldNum<100){
                            return;
                        }
                        String newNum = (oldNum - 100)+"";
                        tv.setText(newNum);
//                        int oldAdjust = Integer.parseInt(readFromFile(getApplicationContext(), "adjust.txt"));
//                        oldAdjust+=100;
//                        writeToFile(oldAdjust+"", getApplicationContext(), "adjust.txt");
                        writeToFile(newNum,getApplicationContext(), "config.txt");

                    }});
                alertDialog.setNegativeButton("Cancel", null);
                alertDialog.show();
            }
        });
    }

    private BroadcastReceiver uiUpdated= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String prevScoreString = tv.getText().toString();
            int prevScore = Integer.parseInt(prevScoreString);
            int add = intent.getExtras().getInt("newText");
            int newScore = Math.max(prevScore+add, 0);
            tv.setText(""+newScore);
            writeToFile(""+newScore, getApplicationContext(), "config.txt");
            Log.d("c","c");
        }
    };
    private void writeToFile(String data,Context context, String filename) {
        try {
            Log.d("c","c");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    private String readFromFile(Context context, String filename) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void sendPoint(){
        Intent inent = new Intent(getApplicationContext(), PointsActivity.class);
        String message = "Points";
        inent.putExtra(EXTRA_MESSAGE, message);
        startActivity(inent);
    }
    public void sendRewards(){
        Intent in = new Intent(getApplicationContext(),RewardsActivity.class);
        startActivity(in);
    }
    public void sendCharity(){
        Intent inChar = new Intent(getApplicationContext(),CharityActivity.class);
        startActivity(inChar);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_points) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
            drawer.closeDrawer(GravityCompat.START);
            this.sendPoint();
//        } else if (id == R.id.nav_rewards) {
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
//            drawer.closeDrawer(GravityCompat.START);
//            this.sendRewards();
        } else if (id == R.id.nav_charity) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
            drawer.closeDrawer(GravityCompat.START);
            this.sendCharity();
        }
        return true;
    }
}





