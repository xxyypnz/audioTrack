package com.share.audiotrackdemo.syfuction.wave.waveImpl;

import com.share.audiotrackdemo.syfuction.Common;
import com.share.audiotrackdemo.syfuction.wave.LoudnessCalibrationUtil;
import com.share.audiotrackdemo.syfuction.wave.tools.SinArray;

import java.io.InputStream;


public class SinWave extends Wave {
    private InputStream calibrationFile;
    private int freq;
    private SinArray sinArray;

    public SinWave(int freq, InputStream calibrationFile) {
        this.calibrationFile = calibrationFile;
        this.freq = freq;
        this.sinArray = new SinArray();
    }

    // 这个函数专门给脉冲纯音，增加了类型参数
    public double[] GetRawWaveBufferPlus(double DB, int type) throws Exception {
        AmplitudeCalculator calcu = new AmplitudeCalculator(calibrationFile);
        int amplitude = calcu.GetAmplitude(freq, LoudnessCalibrationUtil.getDb(type,freq,DB));
        return sinArray.GetArray(freq, amplitude, Common.SAMPLE_RATE, NumSamples());
    }

    @Override   //pghpghpgh 20241031
    public double[] GetRawWaveBuffer(double DB) throws Exception {
        AmplitudeCalculator calcu = new AmplitudeCalculator(calibrationFile);
        int amplitude = calcu.GetAmplitude(freq, LoudnessCalibrationUtil.getDb(0,freq,DB));
        return sinArray.GetArray(freq, amplitude, Common.SAMPLE_RATE, NumSamples());
    }
}
