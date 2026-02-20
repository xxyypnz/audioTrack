package com.share.audiotrackdemo.syfuction.wave.waveImpl;

import com.share.audiotrackdemo.syfuction.wave.LoudnessCalibrationUtil;
import com.share.audiotrackdemo.syfuction.wave.tools.RandomArray;

import java.io.InputStream;


public class WhiteNoise extends Wave {
    private InputStream calibrationFile;
    private RandomArray randomArray;

    public WhiteNoise(InputStream calibrationFile) throws Exception {
        this.calibrationFile = calibrationFile;
        randomArray = new RandomArray();
    }

    public double[] GetRawWaveBufferPlus(double DB, int type) throws Exception {
        double[] output = new double[10];
        return output;
    }

    @Override  // pghpghpgh 20241031 白噪音
    public double[] GetRawWaveBuffer(double DB) throws Exception {
        int amplitude = (int) LoudnessCalibrationUtil.getDb(4,-1,DB); // TODO
        return randomArray.GetArray(amplitude, NumSamples());
    }
}
