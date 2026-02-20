package com.share.audiotrackdemo.syfuction.wave.tools;

public class AmArray {
    public double[] GetAmArray(int freq, int amplitude, int sampleRate, int numSamples) {

        int carrierFreq = 7; // 载波频率 1 kHz
        int modulationFreq = 50; // 调制频率 50 Hz
        //int amplitude = 10000; // 振幅
        //int sampleRate = 44100; // 采样率 44.1 kHz
        //int numSamples = 44100; // 样本数量

        double deltaCarrier = 2.0 * Math.PI / sampleRate * carrierFreq;
        double deltaModulation = 2.0 * Math.PI / sampleRate * modulationFreq;
        double[] wave = new double[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double carrierPhase = i * deltaCarrier;
            double modulationPhase = i * deltaModulation;
            double modulatingSignal = amplitude * Math.sin(modulationPhase);
            wave[i] = modulatingSignal * Math.sin(carrierPhase);
        }

        return wave;
    }
}
