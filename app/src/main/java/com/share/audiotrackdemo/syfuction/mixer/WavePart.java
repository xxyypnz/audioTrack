package com.share.audiotrackdemo.syfuction.mixer;


import com.share.audiotrackdemo.syfuction.wave.IWave;
//注释
public class WavePart {
    private IWave wave;
    private int DB;

    public IWave Wave() {return wave;}

    public int DB() {return DB;}

    public void SetDurationMs(int durationMs) {
        wave.SetDurationMs(durationMs);
    }

    public WavePart(IWave wave, int ratio) throws Exception {
        this.wave = wave;
        this.DB = ratio;
    }
}
