package net.maribat.demogoojom;

import static android.bluetooth.BluetoothProfile.GATT;
import static android.bluetooth.BluetoothProfile.GATT_SERVER;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
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
            ifBlutCon();
        });

    }


    private void bluetoothadd() {
        Log.i("TAG", "bluetoothadd: onclick");
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth

            Log.e("Bluetooth ", "not found");
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

                    Log.i("device", "Name device are:  " + device.getName());
                    Log.i("device", "Class device:  device.getBluetoothClass() : " + device.getBluetoothClass());
                    Log.i("device", "Bound device:  " + device.getBondState());
                    Log.i("device", "Mac Addressess:  " + mBluetoothAdapter.getRemoteDevice(device.getAddress()));
                    Log.i("device", "Type device:  " + device.getType());
                    Log.i("device", "Class device: device.getBluetoothClass().getDeviceClass :" + device.getBluetoothClass().getDeviceClass());
                    Log.i("device", "Uuids device:  " + device.getUuids());
                    Log.i("device", "MAjor device Class:  " +device.getBluetoothClass().getMajorDeviceClass());

                    Log.i("device: ==========", "_______________________");

                }
            }

            BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
            List<BluetoothDevice> connected = manager.getConnectedDevices( GATT_SERVER);
            Log.i("Connected Devices: ", connected.size()+"");
        }

    }

    private String getBTMajorDeviceClass(int major) {
        switch (major) {
            case BluetoothClass.Device.Major.AUDIO_VIDEO:
                return "AUDIO_VIDEO";
            case BluetoothClass.Device.Major.COMPUTER:
                return "COMPUTER";
            case BluetoothClass.Device.Major.HEALTH:
                return "HEALTH";
            case BluetoothClass.Device.Major.IMAGING:
                return "IMAGING";
            case BluetoothClass.Device.Major.MISC:
                return "MISC";
            case BluetoothClass.Device.Major.NETWORKING:
                return "NETWORKING";
            case BluetoothClass.Device.Major.PERIPHERAL:
                return "PERIPHERAL";
            case BluetoothClass.Device.Major.PHONE:
                return "PHONE";
            case BluetoothClass.Device.Major.TOY:
                return "TOY";
            case BluetoothClass.Device.Major.UNCATEGORIZED:
                return "UNCATEGORIZED";
            case BluetoothClass.Device.Major.WEARABLE:
                return "AUDIO_VIDEO";
            default:
                return "unknown!";
        }
    }

    public void getConnectedDevice() {
        Log.i("TAG", "getConnectedDevice: connected clicked");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BluetoothManager bluetoothManager = (BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
            for (int profile : new int[]{GATT, GATT_SERVER}) {
                for (BluetoothDevice btDevice : bluetoothManager.getConnectedDevices(profile)) {
                    Log.i("Mac Addressess connected", "are:  " + btDevice.getAddress());

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
                Log.i("Mac Addressess connected", "are:  " + device.getAddress());
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                //Device is now connected
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //Done searching
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                //Device is about to disconnect
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Device has disconnected
            }
        }
    };

    public void goToTest(View view) {
        Intent intent = new Intent(this, TestPrinterActivity.class);
        startActivity(intent);
    }


    public void ifBlutCon(){
        ConnectivityManager connectivityManager =    (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo bluetooth = connectivityManager .getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
        if(bluetooth.isConnected())
        {
            Toast.makeText(this,"bluetooth is connected", Toast.LENGTH_SHORT).show();
        }
    }
}

// https://stackoverflow.com/questions/23273355/is-it-possible-to-get-list-of-bluetooth-printers-in-android
// https://stackoverflow.com/questions/14111353/android-bluetooth-printing
// if major class device = 1536 of more than one device than list to select one ofthose device
// else select automatically