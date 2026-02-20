package com.share.audiotrackdemo.syfuction.serialport;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SerialPortUtils {

    private static final String TAG = "SerialPortUtils";

    private FileDescriptor mFd;
    private FileInputStream mInputStream;
    private FileOutputStream mOutputStream;

    // 设备路径
    private static final String DEVICE_PATH = "/dev/ttyAS2";

    // 波特率
    private static final int BAUD_RATE = 115200;

    public void openSerialPort() {
        try {
            // 打开设备文件
            mFd = new FileDescriptor();
            mFd = open(DEVICE_PATH, BAUD_RATE);
            mInputStream = new FileInputStream(mFd);
            mOutputStream = new FileOutputStream(mFd);
            Log.i(TAG, "串口打开成功");
        } catch (Exception e) {
            Log.e(TAG, "串口打开失败：" + e.getMessage());
        }
    }

    public void closeSerialPort() {
        try {
            if (mInputStream != null) {
                mInputStream.close();
                mInputStream = null;
            }
            if (mOutputStream != null) {
                mOutputStream.close();
                mOutputStream = null;
            }
            close();
            Log.i(TAG, "串口关闭成功");
        } catch (IOException e) {
            Log.e(TAG, "串口关闭失败：" + e.getMessage());
        }
    }

    public void sendData(byte[] data) {
        try {
            if (mOutputStream != null) {
                mOutputStream.write(data);
                mOutputStream.flush();
                Log.i(TAG, "发送数据成功");
            } else {
                Log.e(TAG, "串口未打开，发送数据失败");
            }
        } catch (IOException e) {
            Log.e(TAG, "发送数据失败：" + e.getMessage());
        }
    }

    public byte[] receiveData() {
        byte[] buffer = new byte[1024];
        try {
            if (mInputStream != null) {
                int size = mInputStream.read(buffer);
                if (size > 0) {
                    byte[] data = new byte[size];
                    System.arraycopy(buffer, 0, data, 0, size);
                    Log.i(TAG, "接收到数据：" + new String(data));
                    return data;
                }
            } else {
                Log.e(TAG, "串口未打开，接收数据失败");
            }
        } catch (IOException e) {
            Log.e(TAG, "接收数据失败：" + e.getMessage());
        }
        return null;
    }

    // JNI方法，用于打开串口设备
    private native static FileDescriptor open(String path, int baudRate);

    // JNI方法，用于关闭串口设备
    private native void close();

    // 加载JNI库
    static {
        System.loadLibrary("serial_port");
    }
}

