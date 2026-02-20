package com.share.audiotrackdemo.syfuction;

import java.nio.ByteBuffer;

public class Common {
    public static final int SAMPLE_RATE = 44100;
    // public static final int SAMPLE_DEPTH = 16;
    public static final int SAMPLE_DEPTH = 24;
    public static final int SAMPLE_POINT_BYTES = SAMPLE_DEPTH >> 3;


    //public static final int SAMPLE_VALUE_UPPER_BOUND = (int) Math.floor(Math.pow(2, SAMPLE_DEPTH) / 2 - 1);
    //public static final int SAMPLE_VALUE_LOWWER_BOUND = (int) Math.ceil(0 - (Math.pow(2, SAMPLE_DEPTH) / 2 - 1));
    public static final int SAMPLE_VALUE_UPPER_BOUND = (1 << (SAMPLE_DEPTH - 1)) - 1;  // 8,388,607
    public static final int SAMPLE_VALUE_LOWER_BOUND = -(1 << (SAMPLE_DEPTH - 1));    // -8,388,608

    public static int NumSamples(int durationMs) {
        return (int)((long)SAMPLE_RATE * (long)durationMs / 1000);
    }

    public static void SamplePointDoubleToByte1(double value, byte[] out, int index) {
        int v = (int) (value);
        //int v = (int) Math.round(value);
        if (v > SAMPLE_VALUE_UPPER_BOUND)
            v = SAMPLE_VALUE_UPPER_BOUND;
        else if (v < SAMPLE_VALUE_LOWER_BOUND)
            v = SAMPLE_VALUE_LOWER_BOUND;

        for (int i = 0; i < SAMPLE_POINT_BYTES; i++) {
            int bit = i * 8;
            out[index * SAMPLE_POINT_BYTES + i] = (byte) ((v >> bit) & 0xff);
        }
    }

    public static void SamplePointDoubleToByte(double value, byte[] out, int index) {
        int v = (int) Math.round(value);
        v = Math.min(Math.max(v, SAMPLE_VALUE_LOWER_BOUND), SAMPLE_VALUE_UPPER_BOUND);

        for (int i = 0; i < SAMPLE_POINT_BYTES; i++) {
            int bit = i * 8;
            out[index * SAMPLE_POINT_BYTES + i] = (byte) ((v >> bit) & 0xff);
        }
    }
}