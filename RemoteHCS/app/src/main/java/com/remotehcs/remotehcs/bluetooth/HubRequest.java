package com.remotehcs.remotehcs.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class HubRequest {

    private String btMsg;
    private BluetoothSocket socket;

    public HubRequest(String msg, BluetoothSocket socket) {
        this.socket = socket;
        btMsg = msg;
    }

    public void sendBtMsg(String msg2send){
        String msg = msg2send;
        byte[] bytemsg = msg.getBytes();
        OutputStream mmOutputStream = null;
        try {
            mmOutputStream = socket.getOutputStream();
            mmOutputStream.write(bytemsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getResults() {
        if (socket.isConnected()) {
            sendBtMsg(btMsg);
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    InputStreamReader newisr = new InputStreamReader(socket.getInputStream());
                    int i = newisr.read();
                    String str = "";

                    while (i != 10){
                        str += (char)i;
                        i = newisr.read();
                    }

                    final String output = str;

                    return output;
                } catch (IOException e) {
                    e.printStackTrace();
                    return e.getMessage();
                }
            }
        } else {
            return "disconnected socket";
        }
        return null;
    }
}
