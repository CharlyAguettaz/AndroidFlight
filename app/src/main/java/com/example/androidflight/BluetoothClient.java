package com.example.androidflight;

import static com.example.androidflight.ConstantStates.STATE_CONNECTED;
import static com.example.androidflight.ConstantStates.STATE_FAILED;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Message;

import java.io.IOException;

public class BluetoothClient extends Thread
{
    private BluetoothDevice device;
    private BluetoothSocket socket;
    public BluetoothService bluetoothService;

    @SuppressLint("MissingPermission")
    public BluetoothClient (BluetoothDevice device)
    {
        this.device = device;

        try {
            socket = this.device.createRfcommSocketToServiceRecord(MainActivity.uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    public void run()
    {
        try {
            System.out.println(socket);
            socket.connect();
            Message message=Message.obtain();
            message.what=STATE_CONNECTED;
            MainActivity.handler.sendMessage(message);

            bluetoothService = new BluetoothService(socket);
            bluetoothService.start();

        } catch (IOException e) {
            e.printStackTrace();
            Message message=Message.obtain();
            message.what=STATE_FAILED;
            MainActivity.handler.sendMessage(message);
        }
    }
}
