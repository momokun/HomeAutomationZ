package gs.momokun.tabtutorial;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by ElmoTan on 10/21/2016.
 */

public class TwoFragment extends Fragment {
    View v;
    Button btn_about,pair_button;

    SharedPreferences sp;

    //ListView listView;

    LayoutInflater factory;
    View textEntryView;

    AlertDialog.Builder adb;
    AlertDialog ad;

    private ListView pairedDeviceList;
    private ArrayAdapter<String> btListAdapter;
    public static String DeviceAddress = "device_address";


    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("Called","onCreate2");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_two, container, false);

        btn_about = (Button) v.findViewById(R.id.about_button);
        pair_button = (Button) v.findViewById(R.id.pair_button);

        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(v.getContext(),"Home Automation Nightly Build v0.0001",Toast.LENGTH_SHORT).show();
            }
        });

        pair_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent i = new Intent(getActivity(), PairDevice.class);
               // startActivity(i);
                customDialogBuilder("Ok","Cancel Pair");
            }
        });



        return v;
    }

    private BluetoothAdapter btAdapter;

    private void customDialogBuilder(String positiveButtonMsg, String negativeButtonMsg){
        factory = LayoutInflater.from(v.getContext());
        textEntryView = factory.inflate(R.layout.list_view_dialog, null);

        pairedDeviceList=(ListView) textEntryView.findViewById(R.id.list_view);

        btListAdapter = new ArrayAdapter<String>(textEntryView.getContext(), R.layout.device_title);



        btAdapter = BluetoothAdapter.getDefaultAdapter();

        //show paired device
        Set<BluetoothDevice> pairedDevice = btAdapter.getBondedDevices();

        if (pairedDevice.size()>0){
            //show paired device list
            for(BluetoothDevice devices : pairedDevice){
                btListAdapter.add(devices.getName() + "\n" + devices.getAddress());
            }
        }else{
            //if no devices
            String noDevices = "No paired device detected, make sure you have turn on your bluetooth.".toString();
            btListAdapter.add(noDevices);
        }

        pairedDeviceList.setAdapter(btListAdapter);
        pairedDeviceList.setOnItemClickListener(pairedDeviceClickListener);

        //adapter= new CustomAdapter(dataModels,getApplicationContext());
        //listView.setAdapter(adapter);

        adb = new AlertDialog.Builder(v.getContext());
        adb.setView(textEntryView);

        adb.setCancelable(true).setNegativeButton(negativeButtonMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ad.dismiss();

            }
        });
        ad = adb.create();
        ad.show();
    }

    private AdapterView.OnItemClickListener pairedDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);
            sp = PreferenceManager.getDefaultSharedPreferences(v.getContext());
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("btAddr", address);
            edit.commit();
            ad.dismiss();
            Intent push = new Intent(getContext(), MainActivity.class);
            //push.putExtra(DeviceAddress, address);
            startActivity(push);

        }
    };

    public interface FragmentBMethodsCaller{
        void callTheMethodInFragmentB();
    }
}
