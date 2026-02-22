// pnz on 2026-02-21
package com.share.audiotrackdemo.syfuction;

/**
 * 作用：当串口收到完整指令后的回调接口
 */
public interface SerialListener {
    // 当收到一个完整的符合 $...! 格式的 JSON 字符串时触发
    void onCommandReceived(String json);

    // pnz on 2026-02-22
    // 暂停按键没有写在xml中, 而是作为json串stoptis发送
    void onStopSignalReceived();

    // 当串口发生错误时触发
    void onSerialError(Exception e);
}