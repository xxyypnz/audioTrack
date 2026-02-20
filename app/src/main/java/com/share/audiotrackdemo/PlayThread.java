package com.share.audiotrackdemo;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayThread extends Thread {
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

    private int left;//混频比左边0-100
    private int right;//混频比 右边0-100

    private int leftRate;//左边频率
    private int rightRate;//右边评率
    private List<HpBean> hb= new ArrayList();//混频数组
    /**
     * json字符串传过来
     *
     * {
     *   volume:50,
     *   leftRate:1200,
     *   left:60,
     *   rightRate:1200,
     *   right:40
     * }
     *
     * //  不是混音的时候  left 默认100 不需要传rightRate 和right
     * //混音的时候   left+right=100
     */
    /**
     * 初始化
     * @param leftRate 频率
     */
    public PlayThread(int xd, int leftRate,int rightRate,int left,int right) {
        this.left=left;
        this.right=right;
        this.leftRate=leftRate;
        this.rightRate=rightRate;
        hb.add(new HpBean(leftRate,left));
        hb.add(new HpBean(rightRate,right));

        if (leftRate > 0) {
            Hz = leftRate;
            waveLen = RATE / Hz;
            length = waveLen * Hz;

            wave = SinWave.sin(xd, Hz, RATE);
//            mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, RATE,
//                    AudioFormat.CHANNEL_CONFIGURATION_STEREO, // CHANNEL_CONFIGURATION_MONO,
//                    AudioFormat.ENCODING_PCM_8BIT, length, AudioTrack.MODE_STREAM);
            mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, RATE,
                    AudioFormat.CHANNEL_OUT_MONO, // CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,  wave.length, AudioTrack.MODE_STATIC);
            ISPLAYSOUND = true;
        } else {
            return;
        }

    }
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO; // 单声道
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;// 16位PCM编码
    //private static final int BUFFER_SIZE = AudioTrack.getMinBufferSize(RATE, CHANNEL_CONFIG, AUDIO_FORMAT);

    private static final int BUFFER_SIZE = 44100 * 2;
    @Override
    public void run() {
        super.run();

        if (null != mAudioTrack) {
            mAudioTrack.write(wave, 0, wave.length);
            mAudioTrack.setLoopPoints(0, wave.length / 2, -1);
            mAudioTrack.play();
        }
//        //一直播放
//        while (ISPLAYSOUND) {
//            byte[] waveAll=new byte[RATE*1000];
//            for (int i = 0; i < 1000; i++) {
//                waveAll[i]=wave[i%RATE];
//            }
//            mAudioTrack.write(waveAll, 0, length);
//        }

        // STATIC一直播放
        while (ISPLAYSOUND) {
            try {
                sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (null != mAudioTrack)
            mAudioTrack.stop();
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