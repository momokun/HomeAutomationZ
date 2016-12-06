package gs.momokun.tabtutorial;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {
    private static int BT_REQ = 1;
    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if(btAdapter.isEnabled()){
            start();
        }else{
            BluetoothStateChecker();
        }
    }

    private void BluetoothStateChecker(){

        if(btAdapter!=null){
            if(btAdapter.isEnabled()){
                //do nothing, bluetooth is on
            }else{
                Intent btRequestEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(btRequestEnable,BT_REQ);
            }
        }else{
            Toast.makeText(this, "No Bluetooth on this device, please check your device.", Toast.LENGTH_SHORT).show();

        }

    }

    protected void start(){
        Intent i = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == BT_REQ) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
               start();
            }
        }
    }
}
