package com.example.androidflight;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidflight.databinding.RowBluetoothDeviceBinding;

import java.util.ArrayList;

public class RecyclerAdapterBluetoothDevice extends RecyclerView.Adapter<RecyclerAdapterBluetoothDevice.BluetoothDeviceHolder> {

    private ArrayList<BluetoothDeviceModel> bluetoothDeviceModels = new ArrayList<>();
    private MainActivity activity;
    public RecyclerAdapterBluetoothDevice(ArrayList<BluetoothDeviceModel> bluetoothDeviceModels, MainActivity activity) {
        this.bluetoothDeviceModels = bluetoothDeviceModels;
        this.activity = activity;
    }
    @NonNull
    @Override
    public RecyclerAdapterBluetoothDevice.BluetoothDeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerAdapterBluetoothDevice.BluetoothDeviceHolder(RowBluetoothDeviceBinding.inflate(activity.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterBluetoothDevice.BluetoothDeviceHolder holder, int position) {
        BluetoothDeviceModel bluetoothDeviceModel = bluetoothDeviceModels.get(position);
        String name = bluetoothDeviceModel.getName() != null ? bluetoothDeviceModel.getName() : "undefined";
        String address = bluetoothDeviceModel.getAddress() != null ? bluetoothDeviceModel.getAddress() : "undefined";
        holder.rowBluetoothDeviceBinding.bluetoothDeviceTv.setText(name + " : " + address);
    }

    @Override
    public int getItemCount() {
        return bluetoothDeviceModels == null ? 0 : bluetoothDeviceModels.size();
    }

    public class BluetoothDeviceHolder extends RecyclerView.ViewHolder {

        public RowBluetoothDeviceBinding rowBluetoothDeviceBinding;

        public BluetoothDeviceHolder(@NonNull RowBluetoothDeviceBinding binding) {
            super(binding.getRoot());
            this.rowBluetoothDeviceBinding = binding;

        }

    }
}
