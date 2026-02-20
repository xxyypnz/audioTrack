package com.share.audiotrackdemo.syfuction.wave.waveImpl;

import android.util.Log;

import com.share.audiotrackdemo.syfuction.Common;
import com.share.audiotrackdemo.syfuction.wave.LoudnessCalibrationUtil;
import com.share.audiotrackdemo.syfuction.wave.exceptions.UnsupportPeriod;

import java.io.InputStream;



public class ChirpWave extends Wave {
    private InputStream calibrationFile;
    private int freq;
    private int period;
    private int bandwidth;

    public ChirpWave(int freq, InputStream calibrationFile) {
        this.calibrationFile = calibrationFile;
        this.freq = freq;
    }

    @Override
    public double[] GetRawWaveBuffer(double DB) throws UnsupportPeriod {

        // pghpghpgh, 注意，这个地方如果沿用原来的10秒，会有问题，以后再找原因
        //double[] wave = new double[NumSamples()];
        int durationSec = 11;
        double[] wave = new double[durationSec*44100];

        double amplitude = LoudnessCalibrationUtil.getDb(1,freq,DB); // TODO
        double f_baseband = 5;
        double kf = freq * 0.318;

        //double[] t = new double[NumSamples()];
        double[] t = new double[durationSec*44100];

        for (int i = 0; i < t.length; i++) {
            t[i] = i / (double) Common.SAMPLE_RATE;
        }

        double[] baseband_signal = new double[t.length];
        for (int i = 0; i < baseband_signal.length; i++) {
            baseband_signal[i] = Math.sin(2 * Math.PI * f_baseband * t[i]);
        }

        double cumsum = 0;  // 累积和
        for (int i = 0; i < wave.length; i++) {
            cumsum += baseband_signal[i] / Common.SAMPLE_RATE;
            wave[i] = amplitude * Math.sin(2 * Math.PI * freq * t[i] + kf * cumsum);
        }

        return wave;
    }

    // 增加这个函数，为了给脉冲啭音调用时，LoudnessCalibrationUtil.getDb的传入type 为12
    public double[] GetRawWaveBufferPlus(double DB, int type) throws UnsupportPeriod {
        // pghpghpgh, 注意，这个地方如果沿用原来的10秒，会有问题，以后再找原因
        //double[] wave = new double[NumSamples()];
        int durationSec = 11;
        double[] wave = new double[durationSec*44100];

        double amplitude = LoudnessCalibrationUtil.getDb(type,freq,DB); // TODO
        double f_baseband = 5;
        double kf = freq * 0.318;

        //double[] t = new double[NumSamples()];
        double[] t = new double[durationSec*44100];

        for (int i = 0; i < t.length; i++) {
            t[i] = i / (double) Common.SAMPLE_RATE;
        }

        double[] baseband_signal = new double[t.length];
        for (int i = 0; i < baseband_signal.length; i++) {
            baseband_signal[i] = Math.sin(2 * Math.PI * f_baseband * t[i]);
        }

        double cumsum = 0;  // 累积和
        for (int i = 0; i < wave.length; i++) {
            cumsum += baseband_signal[i] / Common.SAMPLE_RATE;
            wave[i] = amplitude * Math.sin(2 * Math.PI * freq * t[i] + kf * cumsum);
        }

        return wave;
    }

}