// pnz on 2026-02-21

package com.share.audiotrackdemo.syfuction;

import android.serialport.SerialPort;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialServerThread extends Thread {
    private final String TAG = "SerialServer";
    private SerialPort mSerialPort;
    private SerialListener mListener;
    private boolean isRunning = true;
    private StringBuilder buffer = new StringBuilder();

    public SerialServerThread(SerialPort port, SerialListener listener) {
        this.mSerialPort = port;
        this.mListener = listener;
    }

    @Override
    public void run() {
        if (mSerialPort == null) return;
        InputStream inputStream = mSerialPort.getInputStream();
        byte[] readBuffer = new byte[1024];

        Log.i(TAG, "串口服务端线程已进入监听状态...");

        while (isRunning && !isInterrupted()) {
            try {
                // 阻塞式读取：没有电信号时，线程会在这里挂起，不占 CPU
                int size = inputStream.read(readBuffer);
                if (size > 0) {
                    String received = new String(readBuffer, 0, size);
                    buffer.append(received);

                    // 检查缓存中是否有完整的帧 $...!
                    checkFrame();
                }
            } catch (Exception e) {
                if (mListener != null) mListener.onSerialError(e);
                break;
            }
        }
    }

    private void checkFrame() {
        // pnz on 2026-02-22
        // 下面逻辑都没有, 终止符一直不出现
        // 接收到的字符串会一直储存在buffer中
        String content = buffer.toString();
        // 查找起始符和结束符
        int startIdx = content.indexOf("$");
        int endIdx = content.indexOf("!");

        // 逻辑：如果收到了结束符，且结束符在起始符后面
        if (startIdx != -1 && endIdx != -1 && endIdx > startIdx) {
            // 截取中间的 JSON 部分
            String payload = content.substring(startIdx + 1, endIdx).trim();

            if("stoptis".equalsIgnoreCase(payload)){
                Log.w(TAG, "检测到停止信号!");
                if(mListener != null) mListener.onStopSignalReceived();
            }

            // 普通json指令
            // 回调给 Activity
            else {
                if (mListener != null) mListener.onCommandReceived(payload);
            }

            // 删掉已处理的部分，保留剩余部分（防止粘包）
            buffer.delete(0, endIdx + 1);

            // 递归检查，万一一次性收到了多条指令
            checkFrame();
        } else if (content.length() > 2048) {
            // 异常防护：如果一直没收到完整的包，缓冲区又太大，说明是脏数据，清空
            buffer.setLength(0);
        }
    }

    // 允许从 Activity 调用此方法向主机发送回执
    public void sendData(String data) {
        if (mSerialPort != null) {
            try {
                OutputStream out = mSerialPort.getOutputStream();
                // 按照协议，回传也带上标志位，方便主机解析
                String frame = "$" + data + "!";
                out.write(frame.getBytes());
                out.flush();
                Log.d(TAG, "回传数据成功: " + frame);
            } catch (IOException e) {
                Log.e(TAG, "回传数据失败", e);
            }
        }
    }

    public void stopServer() {
        isRunning = false;
        this.interrupt();
    }
}