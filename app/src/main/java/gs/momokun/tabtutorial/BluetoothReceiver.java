package gs.momokun.tabtutorial;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

/**
 * Created by ElmoTan on 12/2/2016.
 */

public class BluetoothReceiver extends BroadcastReceiver{




    @Override
    public void onReceive(Context context, Intent intent) {
        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                -1);

        String action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);


        switch (action){
            case BluetoothDevice.ACTION_ACL_CONNECTED:
               // Toast.makeText(context, "Arduino Connected", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new ArduinoStateOnReceived(1));

                break;
            case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                //Toast.makeText(context, "Arduino Disconnected", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new ArduinoStateOnReceived(0));

                break;
        }






      /*switch(state){

            case BluetoothAdapter.STATE_CONNECTED:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_CONNECTED",
                        Toast.LENGTH_SHORT).show();
                break;
            case BluetoothAdapter.STATE_CONNECTING:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_CONNECTING",
                        Toast.LENGTH_SHORT).show();
                break;
            case BluetoothAdapter.STATE_DISCONNECTED:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_DISCONNECTED",
                        Toast.LENGTH_SHORT).show();
                break;
            case BluetoothAdapter.STATE_DISCONNECTING:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_DISCONNECTING",
                        Toast.LENGTH_SHORT).show();
                break;
            case BluetoothAdapter.STATE_OFF:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_OFF",
                        Toast.LENGTH_SHORT).show();
                break;
            case BluetoothAdapter.STATE_ON:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_ON",
                        Toast.LENGTH_SHORT).show();
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_TURNING_OFF",
                        Toast.LENGTH_SHORT).show();
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                Toast.makeText(context,
                        "BTStateChangedBroadcastReceiver: STATE_TURNING_ON",
                        Toast.LENGTH_SHORT).show();
                break;
        }*/
    }

}