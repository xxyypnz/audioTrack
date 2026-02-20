package com.share.audiotrackdemo.syfuction.wave;

public interface IWave {
     byte[] GetWaveBuffer(double DB) throws Exception;
     double[] GetRawWaveBuffer(double DB) throws Exception;

     double[] GetRawWaveBufferPlus(double DB, int type) throws Exception;
     int DurationMs();
     void SetDurationMs(int ms);
}