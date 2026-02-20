package com.share.audiotrackdemo.syfuction.wave.waveImpl;


import com.share.audiotrackdemo.syfuction.Common;
import com.share.audiotrackdemo.syfuction.wave.IWave;

public abstract class Wave implements IWave {
    protected int durationMs;

    public Wave() {
        this.durationMs = 10000;
    }

    protected int NumSamples() {
        return Common.NumSamples(DurationMs());
    }

    @Override
    public int DurationMs() {
        return this.durationMs;
    }

    @Override
    public void SetDurationMs(int ms) {
        // pghpghpgh , 注意这个地方改为=ms，会有问题
        this.durationMs = 10000;
        //this.durationMs = ms;
    }

    @Override
    public byte[] GetWaveBuffer(double DB) throws Exception{
        double[] rawWave = GetRawWaveBuffer(DB);
        byte[] out = new byte[Common.SAMPLE_POINT_BYTES * rawWave.length];

        for (int i = 0; i < rawWave.length; i++)
            Common.SamplePointDoubleToByte(rawWave[i] * 256, out, i);

        return out;
    }

}
