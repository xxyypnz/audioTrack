package com.share.audiotrackdemo.syfuction.wave.waveImpl;


import com.share.audiotrackdemo.syfuction.Common;
import com.share.audiotrackdemo.syfuction.wave.IWave;
import com.share.audiotrackdemo.syfuction.wave.LoudnessCalibrationUtil;

public class PulseWave extends Wave {
    private int width;
    private int gap;
    private IWave substance;

    private int p_width = 500; // pghpghpgh maybe change later.
    private int p_gap = 250; // pghpghpgh maybe change later.

    public PulseWave(int width, int gap, IWave substance) {
        this.width = width;
        this.gap = gap;
        this.substance = substance;
    }

    public double[] GetRawWaveBufferPlus(double DB, int type) throws Exception {
        double[] output = new double[10];
        return output;
    }

    @Override

    public double[] GetRawWaveBuffer(double DB) throws Exception {
        int durationSec = 10;
        int durationMs = durationSec * 1000; // 多1秒用于后续首尾1秒叠加

        // 设置持续时间
        substance.SetDurationMs(durationMs);

        // 获取波形数据
        double[] swave = substance.GetRawWaveBufferPlus(LoudnessCalibrationUtil.getDb(1, -1, DB), 12);

        // 初始化波形数组
        int sampleRate = Common.SAMPLE_RATE;
        int totalSamples = durationSec * sampleRate;

        // 确保音频总长度为一个完整脉冲周期的整数倍
        int positiveSamplesInAPeriod = width * sampleRate / 1000;
        int negtiveSamplesInAPeriod = gap * sampleRate / 1000;
        int smaplesInAPeriod = positiveSamplesInAPeriod + negtiveSamplesInAPeriod;

        // 如果剩余部分不足一个完整的周期，补充完整
        int remainingSamples = totalSamples % smaplesInAPeriod;
        if (remainingSamples > 0) {
            // 补充一个完整周期的样本
            totalSamples += smaplesInAPeriod - remainingSamples;
        }

        // 创建新的波形数组以适应完整的周期
        double[] wave = new double[totalSamples];

        int fadeDurationInMs = 500;
        int fadeLength = sampleRate * fadeDurationInMs / 1000;
        int useMethod = 3;   // 使用 sine-squared窗
        int useSegment = 3;  // 三段方式

        // 对脉冲进行平滑处理
        applyPulseShaping(swave, wave, positiveSamplesInAPeriod, negtiveSamplesInAPeriod,
                smaplesInAPeriod, fadeLength, useMethod, useSegment);

        // 处理最后的渐出部分，确保不会有重复间隔音
        int overlapSamples = fadeLength;  // 最后渐出长度与 fadeLength 对齐
        int overlapStart = totalSamples - overlapSamples;

        // 将最后一段音频进行渐出过渡
        for (int i = overlapStart; i < totalSamples; i++) {
            wave[i] *= (1 - (double) (i - overlapStart) / overlapSamples);
        }

        // 确保音频在结尾时不会变得不同
        // 补充最后一个周期以确保音频结束时音频变化与前面一致
        if (remainingSamples > 0) {
            // 获取最后一个周期的数据
            int lastPeriodStart = totalSamples - smaplesInAPeriod;
            System.arraycopy(wave, 0, wave, lastPeriodStart, smaplesInAPeriod);
        }

        return wave;
    }

//    public double[] GetRawWaveBuffer(double DB) throws Exception {
//        int durationSec = 10;
//        int durationMs = durationSec * 1000; // 多1秒用于后续首尾1秒叠加
//
//        // substance.SetDurationMs(DurationMs());
//        substance.SetDurationMs(durationMs);
//
//        double[] swave = substance.GetRawWaveBuffer(LoudnessCalibrationUtil.getDb(11, -1, DB));
//
//        //double[] wave = new double[NumSamples()];
//        double[] wave = new double[durationSec*44100];
//
//        int positiveSamplesInAPeriod = width * Common.SAMPLE_RATE / 1000;
//        int negtiveSamplesInAPeriod = gap * Common.SAMPLE_RATE / 1000;
//        int smaplesInAPeriod = positiveSamplesInAPeriod + negtiveSamplesInAPeriod;
//
//        int fadeDurationInMs = 500;
//        int fadeLength = Common.SAMPLE_RATE * fadeDurationInMs / 1000;
//        int useMethod = 3;   // 0: 无平滑, 1: 余弦窗, 2: 高斯窗, 3: sine-squared窗
//        int useSegment = 3;  // 2: 两段方式, 3: 三段方式
//
//        /*
//            内部控制使用平滑方式，支持 0: 不做平滑, 1: 余弦窗, 2: 高斯窗, 3: sine-squared窗
//            int useMethod = 3;  // 0: 不做平滑，1: 余弦窗，2: 高斯窗，3: sine-squared窗
//            内部控制选择的脉冲周期方式:
//            1. 两段方式（useSegment = 2）：仅进行渐入和渐出，脉冲周期没有中间的目标值部分
//            2. 三段方式（useSegment = 3）：首先进行渐入（音量从 0 渐变到目标值），然后保持目标值，最后进行渐出（音量从目标值减小到 0）
//            3. 渐入部分：音量从 0 增加到目标值。持续时间由 fadeLength 决定。
//            4. 目标值部分：保持目标值。其持续时间为脉冲周期总长减去渐入和渐出的部分，即：positiveSamplesInAPeriod - 2 * fadeLength。
//            5. 渐出部分：音量从目标值下降到 0。持续时间也是由 fadeLength 决定
//            6. fadeLength 是以样本数（数据的长度）为单位的，而不是时间的毫秒数
//        */
//        applyPulseShaping(swave, wave, positiveSamplesInAPeriod, negtiveSamplesInAPeriod,
//                smaplesInAPeriod, fadeLength, useMethod, useSegment);
//
//
////        // 切分 head / middle / tail 和淡入淡出
////        double durationSec_out = 10.0; //  = 10.0;   输出时长（秒）
////        double overlapSec = 3.0;      // 交叉淡入/淡出长度（秒）
////        int sampleRate = 44100;
////        int totalSamples =(int) durationSec * sampleRate;             // durationSec * sampleRate
////        int overlapSamples = (int) overlapSec * sampleRate;           // overlapSec * sampleRate
////        double[] wout = applyFadeInOut(wave, durationSec_out, overlapSec,totalSamples,overlapSamples);
//
//        return wave;
//    }

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
