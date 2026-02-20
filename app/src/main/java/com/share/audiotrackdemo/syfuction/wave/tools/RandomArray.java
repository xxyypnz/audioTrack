package com.share.audiotrackdemo.syfuction.wave.tools;

import java.util.Random;

public class RandomArray {
    //public double[] GetArray(int amplitude, int length) throws Exception {
    public double[] GetArray(double amplitude, int length) throws Exception {
        double[] wave = new double[length];
        Random random = new Random(19260817L);
        for (int i = 0; i < length; i++)
            wave[i] = amplitude * random.nextDouble();

        return wave;
    }
}
