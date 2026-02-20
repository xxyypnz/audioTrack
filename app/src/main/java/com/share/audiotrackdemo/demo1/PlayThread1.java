package com.share.audiotrackdemo.demo1;

/**
 * @Description: 描述
 * @Author: wangcheng
 * @CeateDate: 2023/8/10
 * @UpdateUser: 更新着
 * @UpdateData: 更新时间
 * @UpdateRemark: 更新说明
 * @Vsersion: 1.0
 */
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.share.audiotrackdemo.SinWave;

public class PlayThread1 extends Thread {
    public  static int RATE = 44100;
    AudioTrack mAudioTrack;
    public static boolean ISPLAYSOUND;

    /**
     * 总长度
     **/
    int length;
    /**
     * 一个正弦波的长度
     **/
    int waveLen;
    /**
     * 频率
     **/
    int Hz;
    /**
     * 正弦波
     **/
    byte[] wave;

    /**
     * 初始化
     * @param rate 频率
     */
    public PlayThread1(int xd, int rate) {

        if (rate > 0) {
            Hz = rate;
            waveLen = RATE / Hz;
            length = waveLen * Hz;
            wave = new byte[RATE];
//            mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, RATE,
//                    AudioFormat.CHANNEL_CONFIGURATION_STEREO, // CHANNEL_CONFIGURATION_MONO,
//                    AudioFormat.ENCODING_PCM_8BIT, length, AudioTrack.MODE_STREAM);
            mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, RATE,
                    AudioFormat.CHANNEL_OUT_MONO, // CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,  BUFFER_SIZE, AudioTrack.MODE_STREAM);
            ISPLAYSOUND = true;
            wave = SinWave1.sin(xd,wave, waveLen, length);
        } else {
            return;
        }

    }
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO; // 单声道
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;// 16位PCM编码
    private static final int BUFFER_SIZE = AudioTrack.getMinBufferSize(RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
    @Override
    public void run() {
        super.run();
        if (null != mAudioTrack)
            mAudioTrack.play();
//        //一直播放
//        while (ISPLAYSOUND) {
//            byte[] waveAll=new byte[RATE*1000];
//            for (int i = 0; i < 1000; i++) {
//                waveAll[i]=wave[i%RATE];
//            }
//            mAudioTrack.write(waveAll, 0, length);
//        }
        //一直播放
        while (ISPLAYSOUND) {
            mAudioTrack.write(wave, 0, length);
        }
    }

    /**
     * 设置左右声道，左声道时设置右声道音量为0，右声道设置左声道音量为0
     *
     * @param left  左声道
     * @param right 右声道
     */
    public void setChannel(boolean left, boolean right) {
        if (null != mAudioTrack) {
            mAudioTrack.setStereoVolume(left ? 1 : 0, right ? 1 : 0);
        }
    }

    //设置音量
    public void setVolume(float left, float right) {
        if (null != mAudioTrack) {
            mAudioTrack.setStereoVolume(left,right);
        }
    }

    public void stopPlay() {
        ISPLAYSOUND = false;
        releaseAudioTrack();
    }

    private void releaseAudioTrack() {
        if (null != mAudioTrack) {
            mAudioTrack.stop();
            mAudioTrack.release();
            mAudioTrack = null;
        }
    }
}