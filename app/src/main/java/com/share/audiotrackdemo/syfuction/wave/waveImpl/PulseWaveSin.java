package com.share.audiotrackdemo.syfuction.wave.waveImpl;


import com.share.audiotrackdemo.syfuction.Common;
import com.share.audiotrackdemo.syfuction.wave.IWave;
import com.share.audiotrackdemo.syfuction.wave.LoudnessCalibrationUtil;

public class PulseWaveSin extends Wave {
    private int width;
    private int gap;
    private IWave substance;

    private int p_width = 500; // pghpghpgh maybe change later.
    private int p_gap = 250; // pghpghpgh maybe change later.

    public PulseWaveSin(int width, int gap, IWave substance) {
        this.width = width;
        this.gap = gap;
        this.substance = substance;
    }

    public double[] GetRawWaveBufferPlus(double DB, int type) throws Exception {
        double[] output = new double[10];
        return output;
    }

    // 只进行平滑处理
    @Override
    public double[] GetRawWaveBuffer(double DB) throws Exception {
        int durationSec = 11;
        int durationMs = durationSec * 1000; // 多1秒用于后续首尾1秒叠加

        // 设置持续时间
        substance.SetDurationMs(durationMs);

        double[] swave = substance.GetRawWaveBufferPlus(LoudnessCalibrationUtil.getDb(25,-1,DB), 25);


        double[] wave = new double[NumSamples()];

        //int positiveSamplesInAPeriod = width * Common.SAMPLE_RATE / 1000;
        int positiveSamplesInAPeriod = 500 * Common.SAMPLE_RATE / 1000;
        int negtiveSamplesInAPeriod = gap * Common.SAMPLE_RATE / 1000;
        int smaplesInAPeriod = positiveSamplesInAPeriod + negtiveSamplesInAPeriod;

        int sampleRate = Common.SAMPLE_RATE;
        int totalSamples = durationSec * sampleRate;
        int fadeDurationInMs = 500;
        int fadeLength = sampleRate * fadeDurationInMs / 1000;
        int useMethod = 3;   // 使用 sine-squared窗
        int useSegment = 3;  // 三段方式

        // 对脉冲进行平滑处理
        applyPulseShaping(swave, wave, positiveSamplesInAPeriod, negtiveSamplesInAPeriod,
                smaplesInAPeriod, fadeLength, useMethod, useSegment);

        return wave;
    }

//    public double[] GetRawWaveBuffer(double DB) throws Exception {
//        int durationSec = 11;
//        int durationMs = durationSec * 1000; // 多1秒用于后续首尾1秒叠加
//
//        // 设置持续时间
//        substance.SetDurationMs(durationMs);
//
//        double[] swave = substance.GetRawWaveBufferPlus(LoudnessCalibrationUtil.getDb(25,-1,DB), 25);
//        double[] wave = new double[NumSamples()];
//
//        int positiveSamplesInAPeriod = width * Common.SAMPLE_RATE / 1000;
//        int negtiveSamplesInAPeriod = gap * Common.SAMPLE_RATE / 1000;
//        int smaplesInAPeriod = positiveSamplesInAPeriod + negtiveSamplesInAPeriod;
//
//        int sampleRate = Common.SAMPLE_RATE;
//        int totalSamples = durationSec * sampleRate;
//        int fadeDurationInMs = 500;
//        int fadeLength = sampleRate * fadeDurationInMs / 1000;
//        int useMethod = 3;   // 使用 sine-squared窗
//        int useSegment = 3;  // 三段方式
//
//        // 对脉冲进行平滑处理
//        applyPulseShaping(swave, wave, positiveSamplesInAPeriod, negtiveSamplesInAPeriod,
//                smaplesInAPeriod, fadeLength, useMethod, useSegment);
//
//        return wave;
//    }


    // 切分 head / middle / tail 和淡入淡出
    private double[] applyFadeInOut(double[] wave, double durationSec, double overlapSec, int totalSamples, int overlapSamples) {
        // 切分 head / middle / tail
        double[] head   = new double[overlapSamples];
        double[] middle = new double[totalSamples - overlapSamples];
        double[] tail   = new double[overlapSamples];
        System.arraycopy(wave, 0,                        head,   0, overlapSamples);
        System.arraycopy(wave, overlapSamples,           middle, 0, middle.length);
        System.arraycopy(wave, wave.length - overlapSamples, tail, 0, overlapSamples);

        // 生成 sin/cos 窗
        double[] winIn  = new double[overlapSamples];
        double[] winOut = new double[overlapSamples];
        for (int i = 0; i < overlapSamples; i++) {
            double t = (Math.PI / 2) * i / overlapSamples;
            winIn[i]  = Math.sin(t);
            winOut[i] = Math.cos(t);
        }

        // 淡入淡出叠加
        double[] overlap = new double[overlapSamples];
        for (int i = 0; i < overlapSamples; i++) {
            overlap[i] = head[i] * winIn[i] + tail[i] * winOut[i];
        }

        // 合并 middle + overlap
        double[] output = new double[totalSamples];
        System.arraycopy(middle, 0,           output, 0,             middle.length);
        System.arraycopy(overlap, 0,          output, middle.length, overlapSamples);

        return output;
    }

    /*
        内部控制使用平滑方式，支持 0: 不做平滑, 1: 余弦窗, 2: 高斯窗, 3: sine-squared窗
        int useMethod = 3;  // 0: 不做平滑，1: 余弦窗，2: 高斯窗，3: sine-squared窗
        内部控制选择的脉冲周期方式:
        1. 两段方式（useSegment = 2）：仅进行渐入和渐出，脉冲周期没有中间的目标值部分
        2. 三段方式（useSegment = 3）：首先进行渐入（音量从 0 渐变到目标值），然后保持目标值，最后进行渐出（音量从目标值减小到 0）
        3. 渐入部分：音量从 0 增加到目标值。持续时间由 fadeLength 决定。
        4. 目标值部分：保持目标值。其持续时间为脉冲周期总长减去渐入和渐出的部分，即：positiveSamplesInAPeriod - 2 * fadeLength。
        5. 渐出部分：音量从目标值下降到 0。持续时间也是由 fadeLength 决定
        6. fadeLength 是以样本数（数据的长度）为单位的，而不是时间的毫秒数
    */
    private void applyPulseShaping(double[] swave, double[] wave,
                                   int positiveSamplesInAPeriod, int negtiveSamplesInAPeriod,
                                   int smaplesInAPeriod, int fadeLength,
                                   int useMethod, int useSegment) {
        for (int i = 0; i < wave.length; i++) {
            int pulseIndex = i % smaplesInAPeriod;

            if (pulseIndex < positiveSamplesInAPeriod) {
                if (useSegment == 3) {
                    if (pulseIndex < fadeLength) {
                        wave[i] = swave[i] * getWindowValue(pulseIndex, fadeLength, useMethod, true);
                    } else if (pulseIndex < positiveSamplesInAPeriod - fadeLength) {
                        wave[i] = swave[i];
                    } else {
                        int relIndex = pulseIndex - (positiveSamplesInAPeriod - fadeLength);
                        wave[i] = swave[i] * getWindowValue(relIndex, fadeLength, useMethod, false);
                    }
                } else if (useSegment == 2) {
                    if (pulseIndex < fadeLength) {
                        wave[i] = swave[i] * getWindowValue(pulseIndex, fadeLength, useMethod, true);
                    } else if (pulseIndex >= positiveSamplesInAPeriod - fadeLength) {
                        int relIndex = pulseIndex - (positiveSamplesInAPeriod - fadeLength);
                        wave[i] = swave[i] * getWindowValue(relIndex, fadeLength, useMethod, false);
                    } else {
                        wave[i] = swave[i];
                    }
                }
            } else {
                wave[i] = 0;
            }
        }
    }

    private double getWindowValue(int i, int fadeLength, int useMethod, boolean isFadeIn) {
        switch (useMethod) {
            case 0:
                return isFadeIn ? 1.0 : 0.0;
            case 1:
                return cosineWindow(i, fadeLength);
            case 2:
                return gaussianWindow(i, fadeLength);
            case 3:
                return sineSquaredWindow(i, fadeLength);
            default:
                return isFadeIn ? 1.0 : 0.0;
        }
    }

    // 余弦窗的实现
    public static double cosineWindow(int i, int fadeLength) {
        return 0.5 * (1 - Math.cos(Math.PI * i / fadeLength));
    }

    // 高斯窗的实现
    public static double gaussianWindow(int i, int fadeLength) {
        double alpha = 2.0 / fadeLength;
        double x = (i - fadeLength / 2.0) / (fadeLength / 2.0);
        return Math.exp(-0.5 * x * x / alpha);
    }

    // sine-squared窗的实现
    public static double sineSquaredWindow(int i, int fadeLength) {
        double x = Math.PI * i / fadeLength;
        return Math.sin(x) * Math.sin(x);
    }

}
