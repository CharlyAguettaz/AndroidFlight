package com.example.androidflight;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Message;

import java.io.IOException;

public class BluetoothServer extends Thread {

    private BluetoothServerSocket bluetoothServerSocket;
    public static final int STATE_CONNECTING = 0;
    public static final int STATE_FAILED = 1;
    public static final int STATE_CONNECTED = 2;

    public BluetoothServer(BluetoothServerSocket bluetoothServerSocket) {
        this.bluetoothServerSocket = bluetoothServerSocket;
    }
    
    public void run() {
        BluetoothSocket socket = null;
        
        while (socket == null) {
            try {
                Message message = Message.obtain();
                message.what = STATE_CONNECTING;
                MainActivity.handler.sendMessage(message);
                socket = bluetoothServerSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = STATE_FAILED;
                MainActivity.handler.sendMessage(message);
            }

            if (socket != null) {
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                MainActivity.handler.sendMessage(message);
                break;
            }
        }
    }

}
