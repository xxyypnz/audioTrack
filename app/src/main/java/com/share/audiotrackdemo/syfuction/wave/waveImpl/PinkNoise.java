package com.share.audiotrackdemo.syfuction.wave.waveImpl;

import com.share.audiotrackdemo.syfuction.wave.LoudnessCalibrationUtil;

import java.io.InputStream;

import java.util.Random;

public class PinkNoise extends Wave {
    private InputStream calibrationFile;
    private Random random;

    public PinkNoise(InputStream calibrationFile) {
        this.calibrationFile = calibrationFile;
        this.random = new Random();
    }

    public double[] GetRawWaveBufferPlus(double DB, int type) throws Exception {
        double[] output = new double[10];
        return output;
    }

    @Override
    public double[] GetRawWaveBuffer(double DB) {
        int numSamples = NumSamples();
        int amplitude = (int) LoudnessCalibrationUtil.getDb(10,-1,DB); // TODO  // pghpghpgh 20241031 粉红噪音
        double[] buffer = new double[numSamples];
        double[] pinkNoise = new double[numSamples];
        
        // Generate white noise
        for (int i = 0; i < numSamples; i++) {
            pinkNoise[i] = random.nextGaussian();
        }

        // Generate pink noise using a simple filtering algorithm
        double b0 = 0.02109238;
        double b1 = 0.07113478;
        double b2 = 0.68873558;
        double b3 = 0.64669978;
        double b4 = 0.75815904;
        double b5 = 0.78181045;
        double b6 = 0.81467383;
        double b7 = 0.82866257;

        double z1 = 0, z2 = 0, z3 = 0, z4 = 0, z5 = 0, z6 = 0, z7 = 0;

        for (int i = 0; i < numSamples; i++) {
            double white = pinkNoise[i];
            double pink = white + b0 * z1 + b1 * z2 + b2 * z3 + b3 * z4 + b4 * z5 + b5 * z6 + b6 * z7;
            z1 = white;
            z2 = z1;
            z3 = z2;
            z4 = z3;
            z5 = z4;
            z6 = z5;
            z7 = z6;
            buffer[i] = amplitude * pink;
        }

        return buffer;
    }
}
