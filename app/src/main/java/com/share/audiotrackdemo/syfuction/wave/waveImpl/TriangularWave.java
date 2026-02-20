package com.share.audiotrackdemo.syfuction.wave.waveImpl;

import com.share.audiotrackdemo.syfuction.Common;
import com.share.audiotrackdemo.syfuction.wave.LoudnessCalibrationUtil;

import java.io.InputStream;


public class TriangularWave extends Wave {
    private InputStream calibrationFile;
    private int freq;

    public TriangularWave(int freq, InputStream calibrationFile) throws Exception {
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
        int amplitude = calcu.GetAmplitude(freq, LoudnessCalibrationUtil.getDb(3,freq,DB));

        double period = ((double) Common.SAMPLE_RATE) / freq;

        for (int i = 0; i < NumSamples(); i++) {
            double scale = i / period;
            double xInPeriod = scale % 1.0;
            if (xInPeriod < 0.25) {
                wave[i] = amplitude * xInPeriod * 4;
            } else if (xInPeriod >= 0.25 && xInPeriod < 0.5) {
                wave[i] = amplitude * (1 - ((xInPeriod - 0.25) * 4));
            } else if (xInPeriod >= 0.5 && xInPeriod < 0.75) {
                wave[i] = amplitude * (0 - (xInPeriod - 0.5) * 4);
            } else {
                wave[i] = amplitude * (0 - (1 - ((xInPeriod - 0.75) * 4)));
            }
        }
        // 三角波
        return wave;
    }
}
