package com.share.audiotrackdemo.other1;//������һ���򵥵�Android����ʾ�����������ɺͲ���Ƶ����25���ȵ�12000����֮�䣬��ȴ�0�ֱ���120�ֱ�����Ƶ��


import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class AudioGenerator {

    private static final int SAMPLE_RATE = 44100;// 采样率
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO; // 单声道
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;// 16位PCM编码
    private static final int BUFFER_SIZE = AudioTrack.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);

    public void generateAndPlayAudio() {
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, CHANNEL_CONFIG,
                AUDIO_FORMAT, BUFFER_SIZE, AudioTrack.MODE_STREAM);

        audioTrack.play();

        double minFrequency = 25.0; // 最小频率
        double maxFrequency = 12000.0; // 最大频率
        double minVolume = 0.0; // 最小音量
        double maxVolume = 120.0;  // 最大音量

//        double frequencyStep = (maxFrequency - minFrequency) / BUFFER_SIZE;
//        double volumeStep = (maxVolume - minVolume) / BUFFER_SIZE;
        double frequencyStep = (maxFrequency - minFrequency) / BUFFER_SIZE;
        double volumeStep = (maxVolume - minVolume) / BUFFER_SIZE;
        Log.e("1----1---",frequencyStep+"----"+volumeStep);
        short[] audioBuffer = new short[BUFFER_SIZE];

        for (int i = 0; i < BUFFER_SIZE; i++) {
            double frequency = minFrequency + i * frequencyStep;
            double volume = minVolume + i * volumeStep;
            Log.e("1---2----",frequency+"----"+volume);
            generateAudioData(frequency, volume, audioBuffer);
            while (true) {
                audioTrack.write(audioBuffer, 0, audioBuffer.length);
            }
        }

//        audioTrack.stop();
//        audioTrack.release();
    }

    private void generateAudioData(double frequency, double volume, short[] buffer) {
        double angularFrequency = 2 * Math.PI * frequency;
        short amplitude = (short) (volume / 120.0 * Short.MAX_VALUE);

        for (int i = 0; i < buffer.length; i++) {
            double time = (double) i / SAMPLE_RATE;
            double sample = Math.sin(angularFrequency * time);
            buffer[i] = (short) (sample * amplitude);
        }
    }
}