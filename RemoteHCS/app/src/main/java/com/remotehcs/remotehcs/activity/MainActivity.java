package com.remotehcs.remotehcs.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Set;

import com.remotehcs.remotehcs.R;
import com.remotehcs.remotehcs.record.Record;
import com.remotehcs.remotehcs.record.Visit;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private Button searchButton;

    private RelativeLayout progressSpinner;

    public static Record patient;
    public static String token;
    public static String user;
    public static boolean visitInProgress;
    public static boolean connectedToHub;
    public static boolean offlineMode;
    public static boolean previousBluetoothError;
    public static BluetoothDevice mmDevice;
    public static BluetoothSocket mmSocket;
    public static FragmentManager fragmentManager;

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
        previousBluetoothError = false;

        progressSpinner = (RelativeLayout) findViewById(R.id.progressBarView);
        progressSpinner.setVisibility(View.GONE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.mainfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectedToHub) {
                    displayView(3);
                } else {
                    if (previousBluetoothError) {
                        retryConnection();
                    } else {
                        new BluetoothConnection().execute();
                    }
                }
            }
        });

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

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
        if (position == 3) {
            if (connectedToHub) {
                displayView(3);
            } else {
                if (previousBluetoothError) {
                    retryConnection();
                } else {
                    new BluetoothConnection().execute();
                }
            }
        } else {
            displayView(position);
        }
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
                MainActivity.patient.getVisits().add(0, new Visit());
                visitInProgress = true;
                fragment = new NewVisitFragment();
                title = getString(R.string.new_visit);
            default:
                break;
        }

        if (fragment != null) {
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    public void retryConnection () {
        AlertDialog alertDialog3 = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog3.setTitle("Previous Bluetooth Failure");
        alertDialog3.setMessage("The last attempt to connect to the device hub failed, would you like to retry or continue without bluetooth connection");
        alertDialog3.setButton(AlertDialog.BUTTON_POSITIVE, "Continue",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        displayView(3);
                    }
                });
        alertDialog3.setButton(AlertDialog.BUTTON_NEGATIVE, "Retry",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        new BluetoothConnection();
                    }
                });
        alertDialog3.show();
    }

    public void handleBluetoothConnection(int value) {
        switch (value) {
            case 1:
                previousBluetoothError = true;
                connectedToHub = false;
                progressSpinner.setVisibility(View.GONE);
                AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog1.setTitle("Bluetooth Not Supported");
                alertDialog1.setMessage("This Android device does not support bluetooth." +
                        " Continuing without connection to device hub.");
                alertDialog1.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                displayView(3);
                            }
                        });
                alertDialog1.show();
                Log.d("Joseph", "Device does not support bluetooth");
                break;
            case 2:
                previousBluetoothError = false;
                progressSpinner.setVisibility(View.GONE);
                connectedToHub = true;
                Toast.makeText(getApplicationContext(), "Successfully Connected to Device Hub", Toast.LENGTH_LONG).show();
                displayView(3);
                break;
            case 3:
                previousBluetoothError = true;
                connectedToHub = false;
                progressSpinner.setVisibility(View.GONE);
                AlertDialog alertDialog3 = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog3.setTitle("Connection Failed");
                alertDialog3.setMessage("Conneciton with hub could not be established." +
                        " Continue without connection to device hub?");
                alertDialog3.setButton(AlertDialog.BUTTON_POSITIVE, "Continue",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                displayView(3);
                            }
                        });
                alertDialog3.setButton(AlertDialog.BUTTON_NEGATIVE, "Retry",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                new BluetoothConnection();
                            }
                        });
                alertDialog3.show();
                Log.d("Joseph", "Connection to Hub Failed");
                break;
            case 4:
                previousBluetoothError = true;
                connectedToHub = false;
                progressSpinner.setVisibility(View.GONE);
                AlertDialog alertDialog4 = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog4.setTitle("Hub Not Paired");
                alertDialog4.setMessage("This Android device has not been paired with a RemoteHcs" +
                        " device hub. Continuing without connection to device hub.");
                alertDialog4.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                displayView(3);
                            }
                        });
                alertDialog4.show();
                Log.d("Joseph", "RemoteHCS Hub is not a paired device.");
                break;
            case 5:
                previousBluetoothError = true;
                connectedToHub = false;
                progressSpinner.setVisibility(View.GONE);
                AlertDialog alertDialog5 = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog5.setTitle("Bluetooth Not Enabled");
                alertDialog5.setMessage("Bluetooth is not enabled on this Android device." +
                        " Continuing without connection to device hub.");
                alertDialog5.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                displayView(3);
                            }
                        });
                alertDialog5.show();
                Log.d("Joseph", "Bluetooth is not enabled on device");
            default:
                break;
        }
    }

    public int connectBluetooth () {
        BluetoothAdapter mBluetoothAdapter;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        boolean foundHub = false;

        if(mBluetoothAdapter == null) {
            return 1;
        }

        if(!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        if (mBluetoothAdapter.isEnabled()) {
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals("raspberrypi")) {
                        mmDevice = device;
                        foundHub = true;
                        break;
                    }
                }
            }
            if (foundHub) {
                try {
                    Method m = mmDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                    mmSocket = (BluetoothSocket) m.invoke(mmDevice, 1);
                    if (!mmSocket.isConnected()) {
                        mmSocket.connect();
                    }
                    if (mmSocket.isConnected()) {
                        connectedToHub = true;
                        return 2;
                    }
                } catch (Exception e) {
                    Log.e("Bluetooth", e.getMessage(), e);
                    return 3;
                }
            } else {
                return 4;
            }
        }
        return 5;
    }

    private class BluetoothConnection extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            return connectBluetooth();
        }

        @Override
        protected void onPostExecute(Integer result) {
            handleBluetoothConnection(result);
        }

        @Override
        protected void onPreExecute() {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            View view = findViewById(android.R.id.content);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            progressSpinner.setVisibility(View.VISIBLE);
            progressSpinner.setClickable(true);
        }

    }
}