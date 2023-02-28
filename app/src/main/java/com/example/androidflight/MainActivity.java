package com.example.androidflight;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.androidflight.databinding.ActivityMainBinding;

import java.security.Permission;

public class MainActivity extends AppCompatActivity  {

    private ActivityMainBinding binding;
    private Joystick joystick;
    private boolean isAutopilot = false;
    private BluetoothService bluetoothService;
    private BluetoothAdapter bluetoothAdapter;
    private ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setActionBar();
        setJoystick();
        setAutopilotBtn();
        requestBluetoothPermission();
        setBluetoothAdapter();
        //setBluetoothService();

    }

    private void requestBluetoothPermission() {
        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.BLUETOOTH_CONNECT }, 100);
        } else {

        }
    }

    private void setBluetoothAdapter() {
        if (bluetoothAdapter.isEnabled()) {

        } else {
            requestBluetooth();
        }
    }

    private void requestBluetooth() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

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
                }
        );

        activityResultLauncher.launch(intent);
    }

    private void setBluetoothService() {
        bluetoothService = new BluetoothService();
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

    private void toggleAutopilot(boolean autopilot) {
        if (autopilot) {

        } else {

        }
    }

}