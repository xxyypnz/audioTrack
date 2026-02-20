package com.share.audiotrackdemo.syfuction;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;


/**
 * @Description: 描述
 * @Author: wangcheng
 * @CeateDate: 2023/10/14
 * @UpdateUser: 更新着
 * @UpdateData: 更新时间
 * @UpdateRemark: 更新说明
 * @Vsersion: 1.0
 */
public class AudioPlayer {
    private AudioTrack audioTrack;
    private byte[] audioData;
    private boolean isAuto = false;

    private Context context;

    public AudioPlayer(Context context, byte[] audioData,boolean left, boolean right) {
    //public AudioPlayer(byte[] audioData,boolean left, boolean right) {
//    AudioFormat.CHANNEL_OUT_MONO  单声道
//    AudioFormat.CHANNEL_OUT_STEREO 双声道

        // 1. 获取 AudioManager 并设置音量
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            // 获取最大音量
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

            // 获取当前音量
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

            // 设置音量为最大值的 50%
            //int targetVolume = (int) (maxVolume * 0.1f);
            int targetVolume = (int) (maxVolume);

            // 打印日志
            Log.e("----", "maxVolume: " + maxVolume);
            Log.e("----", "currentVolume: " + currentVolume);
            Log.e("----", "targetVolume: " + targetVolume);

            audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    targetVolume,
                    AudioManager.FLAG_SHOW_UI // 显示系统音量条
            );
        }

        isAuto = false;
        int channelOutStereo;
        if(left&&right){
            channelOutStereo = AudioFormat.CHANNEL_OUT_STEREO;
        }else{
            channelOutStereo=AudioFormat.CHANNEL_OUT_MONO;
        }
        this.audioData = audioData;
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_24BIT_PACKED,
                //AudioFormat.ENCODING_PCM_16BIT,
                audioData.length,
                AudioTrack.MODE_STATIC);
        //setChannel(left,false);    // pghpghpgh 20250510
        audioTrack.write(audioData, 0, audioData.length);
        audioTrack.setLoopPoints(0, audioData.length/6 , -1);
        //setChannel(left,right);
    }
    /**
     * 设置左右声道，左声道时设置右声道音量为0，右声道设置左声道音量为0
     *
     * @param left  左声道
     * @param right 右声道
     */
    public void setChannel(boolean left, boolean right) {
        if (null != audioTrack) {
            audioTrack.setStereoVolume(left ? 1 : 0, right ? 1 : 0);
        }
    }
    public void write(byte[] audioData) {
        //audioTrack.write(audioData, 0, audioData.length); // 将音频数据写入
        //if(isAuto){
        // play();
        //}

    }

    public void play() {
        isAuto = true;
        audioTrack.play(); // 开始播放
    }

    public void stop() {
        Log.e("----","stop");
        if (audioTrack != null && isAuto) {
            isAuto = false;
            audioTrack.stop();
            audioTrack.release();
        }

    }


} 
