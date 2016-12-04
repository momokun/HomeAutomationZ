package gs.momokun.tabtutorial;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class PairDevice extends AppCompatActivity {

    //layout initiator
    private CoordinatorLayout coordinatorLayout;
    private ListView pairedDeviceList;

    //bluetooth initiator
    private BluetoothAdapter btAdapter;
    private ArrayAdapter<String> btListAdapter;

    //store device address
    public static String DeviceAddress = "device_address";

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_device);
    }

    @Override
    public void onResume(){
        super.onResume();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        //component initialization
        pairedDeviceList = (ListView) findViewById(R.id.list_paired_devicxe);


        //array initialization for paired device
        btListAdapter = new ArrayAdapter<String>(this, R.layout.device_title);

        //set up ListViev
        pairedDeviceList.setAdapter(btListAdapter);
        pairedDeviceList.setOnItemClickListener(pairedDeviceClickListener);

        //get bluetooth adapter on device
        btAdapter = BluetoothAdapter.getDefaultAdapter();

        //show paired device
        Set<BluetoothDevice> pairedDevice = btAdapter.getBondedDevices();

        if (pairedDevice.size()>0){
            //show paired device list
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for(BluetoothDevice devices : pairedDevice){
                btListAdapter.add(devices.getName() + "\n" + devices.getAddress());
            }
        }else{
            //if no devices
            String noDevices = "Nu".toString();
            btListAdapter.add(noDevices);
        }
    }

    //onClickListener for every paired device
    private AdapterView.OnItemClickListener pairedDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Toast.makeText(PairDevice.this, "Connecting..", Toast.LENGTH_SHORT).show();


            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);




            //sharedpreference
            sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("btAddr", address);
            edit.commit();

            Intent push = new Intent(PairDevice.this, MainActivity.class);
            //push.putExtra(DeviceAddress, address);
            overridePendingTransition(0, 0);
            startActivity(push);
        }
    };

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
            Toast.makeText(this, "No Bluetooth on this device, please check your device.", Toast.LENGTH_SHORT).show();
        }
    }

    private void CallSnackbar(String message){

        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


}
