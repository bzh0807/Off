
package com.example.nova.ddis;

        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.design.widget.NavigationView;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.TextView;

        import java.io.BufferedReader;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;

/**
 * Created by Nova on 2/11/2017.
 */

public class CharityActivity extends MainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view3);
        navigationView.setNavigationItemSelectedListener(this);

        TextView x= (TextView)findViewById(R.id.numPointsC);
        double num = Double.parseDouble(readFromFile(getApplicationContext(), "config.txt"));
        //double adjNum = Double.parseDouble(readFromFile(getApplicationContext(), "adjust.txt"));
        double adjNum = 0.0;
        int money = (int)(num+adjNum)/100;
        int money2 = (int)(num+adjNum)%100;
        String m = money2+"";
        if(money2<10){
            m="0"+money2;
        }
        x.setText("$"+money+"."+m);


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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
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
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
            drawer.closeDrawer(GravityCompat.START);
            this.sendPoint();
//        } else if (id == R.id.nav_rewards) {
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
//            drawer.closeDrawer(GravityCompat.START);
//            this.sendRewards();
        } else if (id == R.id.nav_charity) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
            drawer.closeDrawer(GravityCompat.START);
            this.sendCharity();
        }
        return true;
    }
}
