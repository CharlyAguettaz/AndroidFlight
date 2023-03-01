package com.example.androidflight;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.example.androidflight.BluetoothServer;

import com.example.androidflight.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.Timer;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Joystick joystick;
    private boolean isAutopilot = false;
    private final int REQUEST_BLUETOOTH_CODE = 1001;
    private final int REQUEST_BLUETOOTH_SCAN_CODE = 1010;
    private final String BLUETOOTH_DEVICE_NAME = "Plane ACCMS";
    private final String APP_NAME = "FLIGHT_PANEL";
    private BluetoothAdapter bluetoothAdapter;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private BroadcastReceiver receiver;
    private BluetoothSocket bluetoothSocket;
    private BluetoothServerSocket bluetoothServerSocket;
    public static Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setHandlerMessage();
        setActionBar();
        setJoystick();
        setAutopilotBtn();
        setBluetoothAdapter();
        setActivityResultLauncher();
        setPairDeviceBtn();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_CODE && grantResults[0] != PackageManager.PERMISSION_GRANTED) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setHandlerMessage() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                switch (message.what) {
                    case BluetoothServer.STATE_CONNECTING:
                        System.out.println("Connecting");
                        Toast.makeText(getApplicationContext(), "Connecting", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothServer.STATE_CONNECTED:
                        System.out.println("Connecting");
                        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();

                        break;
                    case BluetoothServer.STATE_FAILED:
                        System.out.println("Connecting");
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        break;

                }
                return true;
            }
        });
    }

    private boolean checkBluetoothOK() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
                && bluetoothAdapter.isEnabled()) {
            return true;
        } else {
            return false;
        }
    }


    private void setBluetoothAdapter() {
        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    private void setPairDeviceBtn() {
        binding.pairDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                if(checkBluetoothOK()) {
                    try {
                        BluetoothServer bluetoothServer = new BluetoothServer(bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(APP_NAME, UUID.fromString("00001101-0000-1000-8000-00294F9B3423")));
                        bluetoothServer.run();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    requestBluetoothPermission();
                    requestBluetoothEnable();
                }
            }
        });
    }

    private void requestBluetoothPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.BLUETOOTH_CONNECT }, REQUEST_BLUETOOTH_CODE);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.BLUETOOTH_SCAN }, REQUEST_BLUETOOTH_SCAN_CODE);
        }
    }

    private void setActivityResultLauncher() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {

                        } else {
                            Toast.makeText(getApplicationContext(), "Can't use app without bluetooth", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void requestBluetoothEnable() {
        if (!bluetoothAdapter.isEnabled() && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activityResultLauncher.launch(intent);
        } else {

        }
    }


    private void setActionBar() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this ,R.color.background));
    }

    private void setJoystick() {
        joystick = new Joystick(getApplicationContext(), binding.layoutJoystick, R.drawable.btn_circle);
        joystick.setStickSize(250, 250);
        joystick.setLayoutSize(600, 600);
        joystick.setLayoutAlpha(1000);
        joystick.setStickAlpha(200);
        joystick.setOffset(90);
        joystick.setMinimumDistance(50);
        setJoystickListener();

    }
    @SuppressLint("ClickableViewAccessibility")
    private void setJoystickListener() {
        binding.manualDisabledTv.setVisibility(View.GONE);
        binding.layoutJoystick.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                joystick.drawStick(arg1);
                if(arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    binding.controllerXTextView.setText(String.valueOf(joystick.getX()));
                    binding.controllerYTextView.setText(String.valueOf(joystick.getY()));
                    binding.angleTextView.setText(String.valueOf(joystick.getAngle()));

                    int direction = joystick.get8Direction();
                    if(direction == Joystick.STICK_UP) {
                        binding.directionTextView.setText(R.string.up);
                    } else if(direction == Joystick.STICK_UPRIGHT) {
                        binding.directionTextView.setText(R.string.up_right);
                    } else if(direction == Joystick.STICK_RIGHT) {
                        binding.directionTextView.setText(R.string.right);
                    } else if(direction == Joystick.STICK_DOWNRIGHT) {
                        binding.directionTextView.setText(R.string.down_right);
                    } else if(direction == Joystick.STICK_DOWN) {
                        binding.directionTextView.setText(R.string.down);
                    } else if(direction == Joystick.STICK_DOWNLEFT) {
                        binding.directionTextView.setText(R.string.down_left);
                    } else if(direction == Joystick.STICK_LEFT) {
                        binding.directionTextView.setText(R.string.left);
                    } else if(direction == Joystick.STICK_UPLEFT) {
                        binding.directionTextView.setText(R.string.up_left);
                    } else if(direction == Joystick.STICK_NONE) {
                        binding.directionTextView.setText(R.string.none);
                    }
                } else if(arg1.getAction() == MotionEvent.ACTION_UP) {
                    binding.directionTextView.setText(R.string.none);
                    binding.controllerXTextView.setText(String.valueOf(0));
                    binding.controllerYTextView.setText(String.valueOf(0));
                    binding.angleTextView.setText(String.valueOf(0));
                }
                return true;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void disableJoystickListener() {
        binding.manualDisabledTv.setVisibility(View.VISIBLE);
        binding.layoutJoystick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    private void setAutopilotBtn() {
        binding.autopilotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isAutopilot) {
                    binding.autopilotBtn.setBackgroundColor(getColor(R.color.background));
                    binding.autopilotBtn.setTextColor(getColor(R.color.disable));
                    setJoystickListener();
                } else {
                    binding.autopilotBtn.setBackgroundColor(getColor(R.color.teal_700));
                    binding.autopilotBtn.setTextColor(getColor(R.color.white));
                    disableJoystickListener();
                }
                isAutopilot = !isAutopilot;
            }
        });
    }

}