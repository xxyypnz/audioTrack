package com.share.audiotrackdemo.syfuction.mixer;


import com.share.audiotrackdemo.syfuction.Common;

public class WaveMixer {
    private WavePart[] parts;
    private int durationMs;

    public void SetDurationMs(int durationMs) {
        this.durationMs = 10000;
    }

    public WaveMixer(WavePart[] parts) {
        this.parts = parts;
        this.durationMs = 10000;
    }

    public byte[] GetWaveBuffer() throws Exception {
        byte[] out = new byte[Common.NumSamples(durationMs) * Common.SAMPLE_POINT_BYTES];
        double[][] rawRaves = new double[parts.length][];

        for (int i = 0; i < parts.length; i++) {
            parts[i].SetDurationMs(durationMs);
            rawRaves[i] = parts[i].Wave().GetRawWaveBuffer(parts[i].DB());
        }

        for (int i = 0; i < Common.NumSamples(durationMs); i++) {
            double v = 0;
            for (int j = 0; j < parts.length; j++) {
                v += rawRaves[j][i];
            }
            Common.SamplePointDoubleToByte(v * 256, out, i);
        }

        return out;
    }
}
