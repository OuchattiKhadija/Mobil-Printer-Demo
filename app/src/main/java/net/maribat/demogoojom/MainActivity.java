package net.maribat.demogoojom;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button get_Device_btn, print_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get_Device_btn = findViewById(R.id.get_mac);
        print_btn = findViewById(R.id.print_btn);
        get_Device_btn.setOnClickListener(view -> {
            bluetoothadd();
        });
        print_btn.setOnClickListener(view -> {
            getConnectedDevice();
        });

    }


    private void bluetoothadd(){
        Log.i("TAG", "bluetoothadd: onclick");
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth

            Log.e("Bluetooth ","not found");
        }

        //02:08:08:B7:A4:69
        /*
        * 2021-11-05 16:49:57.950 6140-6140/net.maribat.demogoojom I/Mac Addressess: are:  02:08:08:B7:A4:69
2021-11-05 16:49:57.950 6140-6140/net.maribat.demogoojom I/Class device: are:  3
2021-11-05 16:49:57.951 6140-6140/net.maribat.demogoojom I/Class device: are:  MTP-II*/

        if (mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            BluetoothClass bluetoothClass;
            // If there are paired devices
            if (pairedDevices.size() > 0) {
                // Loop through paired devices
                for (BluetoothDevice device : pairedDevices) {

                    Log.i("device","Name device are:  "+ device.getName());
                    Log.i("device","Class device:  "+ device.getBluetoothClass());
                    Log.i("device","Bound device:  "+ device.getBondState());
                    Log.i("device","Mac Addressess:  "+mBluetoothAdapter.getRemoteDevice(device.getAddress()));
                    Log.i("device","Type device:  "+ device.getType());
                    Log.i("device","Class device:  "+ device.getBluetoothClass().getDeviceClass());
                    Log.i("device","Class device:  "+ device.getUuids());
                    Log.i("==========","_______________________");

                }
            }
        }

    }

    public void getConnectedDevice(){
        Log.i("TAG", "getConnectedDevice: connected clicked");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BluetoothManager bluetoothManager = (BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
            for (int profile : new int[]{BluetoothProfile.GATT, BluetoothProfile.GATT_SERVER}) {
                for (BluetoothDevice btDevice : bluetoothManager.getConnectedDevices(profile)) {
                    Log.i("Mac Addressess connected","are:  "+btDevice.getAddress() );

                }
            }
        }
    }

    //The BroadcastReceiver that listens for bluetooth broadcasts
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
           //Device found
                Log.i("Mac Addressess connected","are:  "+device.getAddress() );
            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
         //Device is now connected
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
         //Done searching
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
        //Device is about to disconnect
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
          //Device has disconnected
            }
        }
    };
}