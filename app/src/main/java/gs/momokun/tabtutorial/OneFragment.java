package gs.momokun.tabtutorial;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static gs.momokun.tabtutorial.R.id.editTextDialogUserInput;

/**
 * Created by ElmoTan on 10/21/2016.
 */

public class OneFragment extends Fragment implements TwoFragment.FragmentBMethodsCaller{

    SwipeRefreshLayout reconnect;

    ImageButton changeName_Lamp1, changeName_Lamp2, changeName_Lamp3, changeName_Lamp4, changeName_Lamp5, viewGraph1, viewGraph2, viewGraph3, viewGraph4, viewGraph5;
    TextView electronicItem1, electronicItem2, electronicItem3, electronicItem4, electronicItem5, hardware_status;
    ToggleButton toggleButtonElectronic1,toggleButtonElectronic2,toggleButtonElectronic3,toggleButtonElectronic4,toggleButtonElectronic5;
    AlertDialog.Builder adb;
    AlertDialog ad;
    TextView temp_in_c,voltage,power,current,energy;
    View v;

    protected int status = 0;

    private InputStream mmInStream;
    private OutputStream mmOutStream;

    Handler btConnectionHandler = null;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder receivedDataFromArduino = new StringBuilder();
    private ConnectedThread mConnectedThread;

    //initialize handler state
    final int handlerState = 0;

    //Device UUID
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //arduino mac address
    private static String address;

    BluetoothDevice device = null;

    SharedPreferences sp;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("Called","onCreate");

    }

    private void initiate(){

        //get device default adapter

        //get saved address
        sp = PreferenceManager.getDefaultSharedPreferences(v.getContext());
        //check bluetooth hardware


        address = sp.getString("btAddr",null);

        reconnect = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        reconnect.setRefreshing(false);

        viewGraph1 = (ImageButton) v.findViewById(R.id.graphCheck1);
        viewGraph2 = (ImageButton) v.findViewById(R.id.graphCheck2);
        viewGraph3 = (ImageButton) v.findViewById(R.id.graphCheck3);
        viewGraph4 = (ImageButton) v.findViewById(R.id.graphCheck4);
        viewGraph5 = (ImageButton) v.findViewById(R.id.graphCheck5);

        hardware_status = (TextView) v.findViewById(R.id.arduino_status);

        changeName_Lamp1 = (ImageButton) v.findViewById(R.id.changeLamp1);
        changeName_Lamp2 = (ImageButton) v.findViewById(R.id.changeLamp2);
        changeName_Lamp3 = (ImageButton) v.findViewById(R.id.changeLamp3);
        changeName_Lamp4 = (ImageButton) v.findViewById(R.id.changeLamp4);
        changeName_Lamp5 = (ImageButton) v.findViewById(R.id.changeLamp5);

        electronicItem1 = (TextView) v.findViewById(R.id.electronicItem1);
        electronicItem2 = (TextView) v.findViewById(R.id.electronicItem2);
        electronicItem3 = (TextView) v.findViewById(R.id.electronicItem3);
        electronicItem4 = (TextView) v.findViewById(R.id.electronicItem4);
        electronicItem5 = (TextView) v.findViewById(R.id.electronicItem5);

        toggleButtonElectronic1 = (ToggleButton) v.findViewById(R.id.toggleButtonElectronic1);
        toggleButtonElectronic2 = (ToggleButton) v.findViewById(R.id.toggleButtonElectronic2);
        toggleButtonElectronic3 = (ToggleButton) v.findViewById(R.id.toggleButtonElectronic3);
        toggleButtonElectronic4 = (ToggleButton) v.findViewById(R.id.toggleButtonElectronic4);
        toggleButtonElectronic5 = (ToggleButton) v.findViewById(R.id.toggleButtonElectronic5);

        temp_in_c = (TextView) v.findViewById(R.id.temp_in_c);
        voltage = (TextView) v.findViewById(R.id.voltages);
        current = (TextView) v.findViewById(R.id.curr_ampere);
        power = (TextView) v.findViewById(R.id.pow_watt);
        energy = (TextView) v.findViewById(R.id.ec_watthour);
    }

    private void setUp(){
        toggleElectronic(toggleButtonElectronic1, "1", "0");
        toggleElectronic(toggleButtonElectronic2, "2", "6");
        toggleElectronic(toggleButtonElectronic3, "3", "7");
        toggleElectronic(toggleButtonElectronic4, "4", "8");
        toggleElectronic(toggleButtonElectronic5, "5", "9");

        changeElectronicName(changeName_Lamp1,electronicItem1);
        changeElectronicName(changeName_Lamp2,electronicItem2);
        changeElectronicName(changeName_Lamp3,electronicItem3);
        changeElectronicName(changeName_Lamp4,electronicItem4);
        changeElectronicName(changeName_Lamp5,electronicItem5);

        viewGraph(viewGraph1);
        viewGraph(viewGraph2);
        viewGraph(viewGraph3);
        viewGraph(viewGraph4);
        viewGraph(viewGraph5);

    }

    private void customDialogBuilder(String positiveButtonMsg, String negativeButtonMsg, final TextView itemName){
        LayoutInflater factory = LayoutInflater.from(v.getContext());
        final View textEntryView = factory.inflate(R.layout.change_name_lamp_dialog_custom, null);
        final EditText editTextDialogUserInput = (EditText) textEntryView.findViewById(R.id.editTextDialogUserInput);
        editTextDialogUserInput.setSingleLine(true);
        adb = new AlertDialog.Builder(getContext(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        adb.setView(textEntryView);

        final String getUserInput = editTextDialogUserInput.getText().toString();

        adb.setCancelable(true).setPositiveButton(positiveButtonMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                do {
                    itemName.setText(getUserInput);
                }while (getUserInput.length()>15);
                ad.dismiss();
            }
        }).setNegativeButton(negativeButtonMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ad.dismiss();
            }
        });
        ad = adb.create();
        ad.show();
    }

    public void changeElectronicName(ImageButton ibChangeName, final TextView tvChangeName){
        ibChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialogBuilder("Ok","Cancel",tvChangeName);
            }
        });
    }

    public void toggleElectronic(ToggleButton tbElectronic, final String on, final String off){
        try {
            tbElectronic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (compoundButton.isChecked()) {
                        mConnectedThread.write(on);    // Send "1" via Bluetooth
                    } else {
                        mConnectedThread.write(off);    // Send "0" via Bluetooth
                    }
                }
            });
        }catch(Exception e){

        }
    }

    public void viewGraph(ImageButton ibViewGraph){
        ibViewGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(v.getContext(), ViewGraph.class);
                startActivity(i);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_one, container, false);
            initiate();
            setUp();
            BroadcastReceiver broadcastReceiver = new BluetoothReceiver();
            IntentFilter f = new IntentFilter();
            f.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            f.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            getActivity().registerReceiver(broadcastReceiver, f);
            EventBus.getDefault().register(this);
        return v;
    }

   private void resetConnection() {
        if (mmInStream != null) {
            try {mmInStream.close();} catch (Exception e) {}
            mmInStream = null;
        }

        if (mmOutStream != null) {
            try {mmOutStream.close();} catch (Exception e) {}
            mmOutStream = null;
        }

        if (btSocket != null) {
            try {btSocket.close();} catch (Exception e) {}
            btSocket = null;
        }

    }

    int stateArduino = 0;
    private CountDownTimer timer;

    @Subscribe
    public void onStateReceived(ArduinoStateOnReceived event){
        stateArduino = event.getStateArduino();
        if(stateArduino == 0) {
            hardware_status.setText("Device Disconnected");
            hardware_status.setTextColor(Color.RED);
            resetConnection();

        }else if(stateArduino == 1){
            hardware_status.setText("Device Connected");
            hardware_status.setTextColor(Color.GREEN);

        }
    }

    //to create socket connection
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    //check bluetooth state
    private void BluetoothStateChecker(){
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(btAdapter!=null){
            if(btAdapter.isEnabled()){
                //do nothing, bluetooth is on
            }else{
                Intent btRequestEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(btRequestEnable,1);

            }
        }else{
            Toast.makeText(v.getContext(), "No Bluetooth on this device, please check your device.", Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        resetConnection();
    }




    private void sysHandler(){
        final String[] temp = new String[1];
        final DatabaseHandler db = new DatabaseHandler(getContext());
        btConnectionHandler = new Handler(){
            //create method for receive
            public void handleMessage(android.os.Message messageFromArduino){
                if(messageFromArduino.what == handlerState){
                    String receiveMsg = (String) messageFromArduino.obj; // msg.arg1 = bytes from connect thread
                    receivedDataFromArduino.append(receiveMsg); //appending multiple string we get from arduino until '~'
                    int endOfLineIndex = receivedDataFromArduino.indexOf("~"); //eol

                    if(endOfLineIndex > 0){
                        String extractedData = receivedDataFromArduino.substring(0, endOfLineIndex);
                        Log.d("Raw Data",extractedData);

                        //logging
                        int dataLength = extractedData.length();
                        Log.d("Raw Data Length",Integer.toString(dataLength));

                        //processing extracted data that start with '#'
                        if(receivedDataFromArduino.charAt(0) == '#'){
                            String[] ext = receivedDataFromArduino.toString().split("@");
                            String power = ext[0].substring(1);
                            String volt = ext[1];
                            temp[0] = ext[2];
                            String ec = ext[3];

                            voltage.setText("V");	//update the textviews with sensor values
                            temp_in_c.setText(temp[0] + " C");
                            current.setText("W");
                            energy.setText("WH");

                            db.addContact(new DataLogging("date",temp[0]));

                        }

                        receivedDataFromArduino.delete(0,receivedDataFromArduino.length()); //clear
                    }


                }
            }
        };
    }

    @Override
    public void onResume() {

        super.onResume();
           resetConnection();
            sysHandler();
            systemExtraTest();
            reconnect.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    reconnect.setRefreshing(true);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //resetConnection();
                                systemExtraTest();
                                reconnect.setRefreshing(false);
                            }

                        }, 3000);
                }
            });
    }

    @Override
    public void onPause()
    {
        super.onPause();
           Log.d("TAG X","Called");

    }

    @Override
    public void callTheMethodInFragmentB() {
        systemExtraTest();
    }


    //ConnectedThread
    private class ConnectedThread extends Thread {



        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;


            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);        	//read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    btConnectionHandler.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();

                } catch (IOException e) {

                    break;
                }
            }
        }

        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                Log.d("Stat","Nothing Send");
            }
        }


    }

    int connStat = 0;
    public void systemExtraTest(){
        Log.d("Call","CAlled");


        if(address!=null) {
            //create device and set the MAC address
            btAdapter = BluetoothAdapter.getDefaultAdapter();
            device = btAdapter.getRemoteDevice(address);

            try {
                btSocket = createBluetoothSocket(device);
            } catch (IOException e) {
                Toast.makeText(getContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
            }
            // Establish the Bluetooth socket connection.

                    try {
                        btSocket.connect();
                    } catch (IOException e) {
                        try {
                            btSocket.close();
                        } catch (IOException e2) {

                        }
                    }


                    mConnectedThread = new ConnectedThread(btSocket);
                    mConnectedThread.start();
            //I send a character when resuming.beginning transmission to check device is connected
            //If it is not an exception will be thrown in the write method and finish() will be called
                 mConnectedThread.write("x");


        }else{

        }


    }






}
