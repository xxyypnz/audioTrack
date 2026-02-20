//package com.share.audiotrackdemo.syfuction.wave.waveImpl;
//
//import com.share.audiotrackdemo.syfuction.Common;
//import com.share.audiotrackdemo.syfuction.wave.LoudnessCalibrationUtil;
//import com.share.audiotrackdemo.syfuction.wave.tools.AmArray;
//import com.share.audiotrackdemo.syfuction.wave.tools.SinArray;
//
//import java.io.InputStream;
//
//
//public class AMWave extends Wave {
//    private InputStream calibrationFile;
//    private int freq;
//    private int carrier_freq;
//    double deepth;
//
//    // pghpghpgh
//    private SinArray sinArray;
//    private AmArray amArray;
//
//    public AMWave(int freq, int carrier_freq, double deepth, InputStream calibrationFile) {
//        this.calibrationFile = calibrationFile;
//        this.freq = freq;
//        this.carrier_freq = carrier_freq;
//        this.deepth = deepth;
//        this.sinArray = new SinArray();
//        //this.amArray = AmArray.GetAmArray(carrier_freq, freq, 10000, 44100, 44100);
//        //this.amArray = new AmArray();
//    }
//
//    @Override
//    public double[] GetRawWaveBuffer(double DB) throws Exception {
//        double[] wave = sinArray.GetArray(freq, 1, Common.SAMPLE_RATE, NumSamples());
//        double[] carrier_wave = sinArray.GetArray(carrier_freq, 1, Common.SAMPLE_RATE, NumSamples());
//        double[] output = new double[NumSamples()];
//        double amplitude =  LoudnessCalibrationUtil.getDb(67,freq,DB); // TODO
//
//        double amp_w = 1.0 / (deepth + 1) * deepth;
//        double amp_c = 1.0 / (deepth + 1);
//
//        for (int i = 0; i < NumSamples(); i++)
//            output[i] = amplitude * (amp_w * wave[i] + amp_c * carrier_wave[i]);
//
//        return output;
//    }
//}

package com.share.audiotrackdemo.syfuction.wave.waveImpl;

import com.share.audiotrackdemo.syfuction.Common;
import com.share.audiotrackdemo.syfuction.wave.LoudnessCalibrationUtil;
import com.share.audiotrackdemo.syfuction.wave.tools.AmArray;
import com.share.audiotrackdemo.syfuction.wave.tools.SinArray;

import java.io.InputStream;

public class AMWave extends Wave {
    private InputStream calibrationFile;
    private int freq; // 调制信号频率
    private int carrierFreq; // 载波频率
    private double depth; // 调制深度
    private int duration; // 音频持续时间（秒）

    private SinArray sinArray;
    private AmArray amArray;

    public AMWave(int freq, int carrierFreq, double depth, InputStream calibrationFile) {
        this.calibrationFile = calibrationFile;
        this.freq = freq;
        this.carrierFreq = carrierFreq;
        this.depth = depth;
        this.duration = 10000;
        this.sinArray = new SinArray();
    }

    public double[] GetRawWaveBufferPlus(double DB, int type) throws Exception {
        double[] output = new double[10];
        return output;
    }

    @Override
    public double[] GetRawWaveBuffer(double DB) throws Exception {
        int numSamples = NumSamples();

        // 根据目标音量级别调整振幅
        double amplitude = LoudnessCalibrationUtil.getDb(67, freq, DB);
        System.out.println("Amplitude after calibration: " + amplitude);

        // 提前计算一些常量
        double adjustedAmplitude = amplitude;

        // 生成调制信号
        double[] modulatingSignal = sinArray.GetArray(freq, 1, Common.SAMPLE_RATE, numSamples); // 相位设置为1
//        System.out.println("First 10 samples of the modulating signal:");
//        for (int i = 0; i < 10 && i < numSamples; i++) {
//            System.out.printf("Modulating Signal Sample %d: %.4f\n", i, modulatingSignal[i]);
//        }

        // 生成载波信号
        double[] carrierSignal = sinArray.GetArray(carrierFreq, 1, Common.SAMPLE_RATE, numSamples); // 相位设置为1
//        System.out.println("First 10 samples of the carrier signal:");
//        for (int i = 0; i < 10 && i < numSamples; i++) {
//            System.out.printf("Carrier Signal Sample %d: %.4f\n", i, carrierSignal[i]);
//        }

        // 创建输出波形数组
        double[] output = new double[numSamples];

        // 生成调幅信号
        for (int i = 0; i < numSamples; i++) {
            // 调制信号的值范围是[-1, 1]，因此需要将其转换到[1 - depth, 1 + depth]
            double modulationValue = 1.0 + depth * modulatingSignal[i];
            output[i] = adjustedAmplitude * modulationValue * carrierSignal[i];

//5
        }

        return output;
    }

    /**
     * 获取最大振幅值
     *
     * @param data 波形数据
     * @return 最大振幅值
     */
    private double getMaxAmplitude(double[] data) {
        double maxAmplitude = 0;
        for (double sample : data) {
            if (Math.abs(sample) > maxAmplitude) {
                maxAmplitude = Math.abs(sample);
            }
        }
        return maxAmplitude;
    }

    /**
     * 获取样本数量
     *
     * @return 样本数量
     */

}