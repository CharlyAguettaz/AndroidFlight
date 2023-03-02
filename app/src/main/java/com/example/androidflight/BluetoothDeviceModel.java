package com.example.androidflight;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;

public class BluetoothDeviceModel {

    private BluetoothDevice device;
    private String name;
    private String address;

    public BluetoothDeviceModel(BluetoothDevice device, String name, String address) {
        this.device = device;
        this.name = name;
        this.address = address;
    }

    public BluetoothDevice getDevice() {
        return device;
    }
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

}
