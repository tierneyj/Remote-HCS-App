package com.remotehcs.remotehcs.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Set;

import com.remotehcs.remotehcs.R;
import com.remotehcs.remotehcs.bluetooth.HubRequest;
import com.remotehcs.remotehcs.record.PatientData;
import com.remotehcs.remotehcs.record.Record;
import com.remotehcs.remotehcs.record.Visit;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private Button searchButton;


    public static Record patient;
    public static String token;
    public static String user;
    public static boolean visitInProgress;
    public static boolean connectedToHub;
    public static boolean offlineMode;
    public static BluetoothAdapter mBluetoothAdapter;
    public static BluetoothDevice mmDevice;
    public static BluetoothSocket mmSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            token = extras.getString("token");
            user = extras.getString("user");
            offlineMode = extras.getBoolean("offlineMode");
        }

        Log.d("Joseph", token);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        patient = new Record();
        visitInProgress = false;
        connectedToHub = false;

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.mainfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.patient.getVisits().add(0, new Visit());
                visitInProgress = true;
                displayView(3);
            }
        });

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch

        connectBluetooth();

        //(new Thread(new workerThread("getbp", mmSocket))).start();

        displayView(0);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        if(id == R.id.action_search){
            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    public void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new NextPatientFragment();
                title = getString(R.string.next_patient);
                break;
            case 1:
                fragment = new PatientInfoFragment();
                title = getString(R.string.patient_info);
                break;
            case 2:
                fragment = new VisitsFragment();
                title = getString(R.string.visits);
                break;
            case 3:
                fragment = new NewVisitFragment();
                title = getString(R.string.new_visit);
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    public void connectBluetooth () {

        if(!mBluetoothAdapter.isEnabled())

        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size()>0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals("raspberrypi"))
                {
                    mmDevice = device;
                    break;
                }
            }
        } try {
            Method m = mmDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
            mmSocket = (BluetoothSocket) m.invoke(mmDevice, 1);
            if (!mmSocket.isConnected()) {
                mmSocket.connect();
            }
            if (mmSocket.isConnected()) {
                connectedToHub = true;
                Log.d("Bluetooth", "Connected");
            }
        } catch (Exception e) {
            Log.e("Bluetooth", e.getMessage(), e);
        }
    }
}