package com.example.androidflight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.androidflight.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity  {

    private ActivityMainBinding binding;
    private Joystick joystick;
    private boolean isAutopilot = false;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setActionBar();
        setJoystick();
        setAutopilotBtn();

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
                        binding.directionTextView.setText("UP");
                    } else if(direction == Joystick.STICK_UPRIGHT) {
                        binding.directionTextView.setText("UP RIGHT");
                    } else if(direction == Joystick.STICK_RIGHT) {
                        binding.directionTextView.setText("RIGHT");
                    } else if(direction == Joystick.STICK_DOWNRIGHT) {
                        binding.directionTextView.setText("DOWN RIGHT");
                    } else if(direction == Joystick.STICK_DOWN) {
                        binding.directionTextView.setText("DOWN");
                    } else if(direction == Joystick.STICK_DOWNLEFT) {
                        binding.directionTextView.setText("DOWN LEFT");
                    } else if(direction == Joystick.STICK_LEFT) {
                        binding.directionTextView.setText("LEFT");
                    } else if(direction == Joystick.STICK_UPLEFT) {
                        binding.directionTextView.setText("UP LEFT");
                    } else if(direction == Joystick.STICK_NONE) {
                        binding.directionTextView.setText("NONE");
                    }
                } else if(arg1.getAction() == MotionEvent.ACTION_UP) {
                    binding.directionTextView.setText("NONE");
                    binding.controllerXTextView.setText(String.valueOf(0));
                    binding.controllerYTextView.setText(String.valueOf(0));
                    binding.angleTextView.setText(String.valueOf(0));
                }
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
                } else {
                    binding.autopilotBtn.setBackgroundColor(getColor(R.color.teal_700));
                    binding.autopilotBtn.setTextColor(getColor(R.color.white));
                }
                isAutopilot = !isAutopilot;
            }
        });
    }

}