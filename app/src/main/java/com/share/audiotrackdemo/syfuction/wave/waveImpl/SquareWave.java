package com.share.audiotrackdemo.syfuction.wave.waveImpl;

import com.share.audiotrackdemo.syfuction.Common;
import com.share.audiotrackdemo.syfuction.wave.LoudnessCalibrationUtil;

import java.io.InputStream;


public class SquareWave extends Wave {
    private InputStream calibrationFile;
    private int freq;

    // pghpghpgh 20250534
    private static final int FREQ_LOWER = 1980;
    private static final int FREQ_MIDDLE = 2000;
    private static final int FREQ_UPPER = 2020;

    public SquareWave(int freq, InputStream calibrationFile) throws Exception {
        this.calibrationFile = calibrationFile;
        this.freq = freq;
    }

    public double[] GetRawWaveBufferPlus(double DB, int type) throws Exception {
        double[] output = new double[10];
        return output;
    }

    @Override
    public double[] GetRawWaveBuffer(double DB) throws Exception {
        double[] wave = new double[NumSamples()];
        AmplitudeCalculator calcu = new AmplitudeCalculator(calibrationFile);


        // pghpghpgh 20250534
        if ((freq > FREQ_LOWER) &&(freq <= FREQ_MIDDLE))
                freq = FREQ_LOWER;
        if ((freq > FREQ_MIDDLE) &&(freq < FREQ_UPPER))
                freq = FREQ_UPPER;

        int amplitude = calcu.GetAmplitude(freq, LoudnessCalibrationUtil.getDb(2,freq,DB));

        double period = ((double) Common.SAMPLE_RATE) / freq;

        for (int i = 0; i < NumSamples(); i++) {
            double scale = i / period;
            double xInPeriod = scale % 1.0;
            if (xInPeriod < 0.5) {
                wave[i] = amplitude;
            } else {
                wave[i] = 0 - amplitude;
            }
        }

        return wave;
    }
}
