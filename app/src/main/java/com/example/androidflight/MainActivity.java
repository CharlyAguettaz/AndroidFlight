package com.example.androidflight;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.androidflight.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity  {

    private ActivityMainBinding binding;
    private Joystick joystick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        joystick = new Joystick(getApplicationContext(), binding.layoutJoystick, R.drawable.btn_circle);
        joystick.setStickSize(250, 250);
        joystick.setLayoutSize(600, 600);
        joystick.setLayoutAlpha(150);
        joystick.setStickAlpha(120);
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

}