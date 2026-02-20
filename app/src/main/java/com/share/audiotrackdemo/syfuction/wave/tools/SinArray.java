package com.share.audiotrackdemo.syfuction.wave.tools;

public class SinArray {
    public double[] GetArray(int freq, int amplitude, int SampleRate, int numSamples) {
        double delta = 2.00 * Math.PI / SampleRate;
        double[] wave = new double[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double x = i * delta * freq;
            wave[i] = amplitude * Math.sin(x);
        }

        return wave;
    }
}
