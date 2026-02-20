package com.share.audiotrackdemo.syfuction.wave.waveImpl;

// pghpghpgh 20250601
//package com.example.audiofilters;

import android.util.Log;
import org.yaml.snakeyaml.Yaml;

import com.share.audiotrackdemo.syfuction.Common;
import com.share.audiotrackdemo.syfuction.wave.LoudnessCalibrationUtil;

import java.io.InputStream;
import java.security.SecureRandom;
import android.util.FloatMath; // Android 支持高精度浮动运算

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;





public class NarrowBandNoise extends Wave {
    private final int centerFreq;               // 中心频率
    private final int sampleRate = Common.SAMPLE_RATE;
    private final double durationSec = 10.0;    // 输出时长（秒）
    private final double overlapSec = 1.0;      // 交叉淡入/淡出长度（秒）
    private final int totalSamples;             // durationSec * sampleRate
    private final int overlapSamples;           // overlapSec * sampleRate
    private final BiquadFilter[] filters;       // 二阶滤波器数组

    // 构造函数，传入频率、带宽和校准文件
    public NarrowBandNoise(int freq, int bw, InputStream calibrationFile) {
        this.centerFreq = freq;
        this.totalSamples = (int) (durationSec * sampleRate);
        this.overlapSamples = (int) (overlapSec * sampleRate);

        // 初始化滤波器
//        Log.e("pgh", "NarrowBandNoise: before loadBiquadFilter, freq=" + freq);
//        filters = loadBiquadFilter(freq);
//        Log.e("pgh", "NarrowBandNoise: after loadBiquadFilter, freq=" + freq);

        // 根据频率加载滤波器
        this.filters = loadBiquadFilter(freq);
    }

    // 加载滤波器系数，并返回滤波器数组
    // 加载滤波器系数，并返回滤波器数组
    private BiquadFilter[] loadBiquadFilter(int freq) {
        Log.e("pgh", "goes into BiquadFilter[] loadBiquadFilter");

        double[][] coeffs = null;
        try {
            coeffs = BiquadFilter.FILTER_PARAMETERS.get(freq);
        } catch (Exception e) {
            Log.e("pgh", "Error loading coefficients: " + e.getMessage());
        }

        // 确保 coeffs 不为 null 且数组长度不为 0
        if (coeffs == null || coeffs.length == 0) {
            Log.e("pgh", "coeffs is null or empty for frequency: " + freq);
            return new BiquadFilter[0];  // 返回一个空的 BiquadFilter 数组
        }

        // 创建 BiquadFilter 数组
        BiquadFilter[] filters = new BiquadFilter[coeffs.length];

        // 遍历系数，创建 BiquadFilter 对象
        for (int i = 0; i < coeffs.length; i++) {
            if (coeffs[i].length < 6) {
                Log.e("pgh", "Invalid coefficient size at index " + i + " for frequency: " + freq);
                continue;  // 跳过不合法的系数
            }

            double b0 = coeffs[i][0];
            double b1 = coeffs[i][1];
            double b2 = coeffs[i][2];
            double a1 = coeffs[i][4];
            double a2 = coeffs[i][5];

            // 创建 BiquadFilter 对象并加入数组
            filters[i] = new BiquadFilter(b0, b1, b2, a1, a2);
        }

        Log.e("pgh", "return BiquadFilter[] loadBiquadFilter");
        return filters;
    }


    @Override
//    public double[] GetRawWaveBuffer(double DB) throws Exception {
//        Log.e("pgh", "GetRawWaveBuffer: " + DB);
//        int amplitude = (int) LoudnessCalibrationUtil.getDb(5, centerFreq, DB);
//        double[] white = generateWhiteNoise(sampleRate, durationSec + overlapSec);
//        double[] filtered = new double[white.length];
//        System.arraycopy(white, 0, filtered, 0, white.length);
//
//        boolean useBandpassFilter = true;
//        //boolean useBandpassFilter = false;
//
//        // 如果选择使用带通滤波器
//        if (useBandpassFilter) {
//            // 使用带通滤波器
//            BandpassFilter bandpassFilter = new BandpassFilter(centerFreq, sampleRate);
//            filtered = bandpassFilter.process(white);  // 使用带通滤波
//        } else {
//            // 正向滤波（IIR 滤波）
//            for (BiquadFilter f : filters) {
//                filtered = f.process(filtered); // 正向滤波
//            }
//        }
//
//        applyAmplitude(filtered, amplitude);
//
//        // 2) 切分 head / middle / tail
//        double[] head   = new double[overlapSamples];
//        double[] middle = new double[totalSamples - overlapSamples];
//        double[] tail   = new double[overlapSamples];
//        System.arraycopy(filtered, 0,                        head,   0, overlapSamples);
//        System.arraycopy(filtered, overlapSamples,           middle, 0, middle.length);
//        System.arraycopy(filtered, filtered.length - overlapSamples, tail, 0, overlapSamples);
//
//        // 3) 生成 sin/cos 窗
//        double[] winIn  = new double[overlapSamples];
//        double[] winOut = new double[overlapSamples];
//        for (int i = 0; i < overlapSamples; i++) {
//            double t = (Math.PI / 2) * i / overlapSamples;
//            winIn[i]  = Math.sin(t);
//            winOut[i] = Math.cos(t);
//        }
//
//        // 4) 淡入淡出叠加
//        double[] overlap = new double[overlapSamples];
//        for (int i = 0; i < overlapSamples; i++) {
//            overlap[i] = head[i] * winIn[i] + tail[i] * winOut[i];
//        }
//
//        // 5) 合并 middle + overlap
//        double[] output = new double[totalSamples];
//        System.arraycopy(middle, 0,           output, 0,             middle.length);
//        System.arraycopy(overlap, 0,          output, middle.length, overlapSamples);
//        Log.e("xfan", "output.length: " + output.length);
//
//
//        return output;
//    }

    public double[] GetRawWaveBufferPlus(double DB, int type) throws Exception {
        double[] output = new double[10];
        return output;
    }

    public double[] GetRawWaveBuffer(double DB) throws Exception {
        Log.e("pgh", "GetRawWaveBuffer: " + DB);
        int amplitude = (int) LoudnessCalibrationUtil.getDb(5, centerFreq, DB);
        double[] white = generateWhiteNoise(sampleRate, durationSec + overlapSec);
        double[] filtered = new double[white.length];
        System.arraycopy(white, 0, filtered, 0, white.length);

        boolean useBandpassFilter = true;

        // 如果选择使用带通滤波器
        if (useBandpassFilter) {
            // 使用带通滤波器
            BandpassFilter bandpassFilter = new BandpassFilter(centerFreq, sampleRate);
            filtered = bandpassFilter.process(white);  // 使用带通滤波
        } else {
            // 正向滤波（IIR 滤波）
            for (BiquadFilter f : filters) {
                filtered = f.process(filtered); // 正向滤波
            }
        }

        applyAmplitude(filtered, amplitude);

        // 切分 head / middle / tail 和淡入淡出
        double[] output = applyFadeInOut(filtered);

        Log.e("xfan", "output.length: " + output.length);
        return output;
    }

    private double[] applyFadeInOut(double[] filtered) {
        // 切分 head / middle / tail
        double[] head   = new double[overlapSamples];
        double[] middle = new double[totalSamples - overlapSamples];
        double[] tail   = new double[overlapSamples];
        System.arraycopy(filtered, 0,                        head,   0, overlapSamples);
        System.arraycopy(filtered, overlapSamples,           middle, 0, middle.length);
        System.arraycopy(filtered, filtered.length - overlapSamples, tail, 0, overlapSamples);

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



    // 生成白噪声
    private double[] generateWhiteNoise(int sr, double duration) {
        int len = (int) (sr * duration);
        double[] w = new double[len];
        Random rnd = new Random(0); // 固定种子，保证可重复
        for (int i = 0; i < len; i++) {
            w[i] = rnd.nextGaussian();
        }
        return w;
    }

    // 应用幅度
    private void applyAmplitude(double[] buf, int amplitude) {
        double max = 0;
        // 计算最大值
        for (double v : buf) if (Math.abs(v) > max) max = Math.abs(v);

        // 使用最大幅度来计算增益系数
        double coff = amplitude / max;

        // 应用增益
        for (int i = 0; i < buf.length; i++) {
            buf[i] *= coff;
        }
    }

    // 带通滤波器
    // ===================== 带通滤波器实现 =====================

    /**
     * BandpassFilter:
     * 基于二阶 Biquad 结构（Butterworth）实现带通滤波器。构造时传入中心频率和采样率，
     * 内部根据中心频率自动选择带宽。提供 process() 对整段音频进行滤波。
     */
    public static class BandpassFilter {
        // 每一级（二阶）滤波器的参数（归一化后）
        private final double b0, b1, b2, a1, a2;

        // 第一节延迟状态
        private double x1a = 0, x2a = 0, y1a = 0, y2a = 0;
        // 第二节延迟状态
        private double x1b = 0, x2b = 0, y1b = 0, y2b = 0;

        /**
         * 构造函数：根据中心频率 centerFreq（Hz）和采样率 sampleRate（Hz）自动决定带宽，
         * 然后计算出两级二阶 Biquad 带通滤波器的各项归一化系数。
         *
         * @param centerFreq 中心频率（Hz），例如 125 ~ 8000
         * @param sampleRate 采样率（Hz），例如 44100、48000 等
         */
        public BandpassFilter(double centerFreq, int sampleRate) {
            if (centerFreq <= 0 || centerFreq >= sampleRate / 2.0) {
                throw new IllegalArgumentException("中心频率必须在 (0, sampleRate/2) 范围内");
            }

            // 根据中心频率自动选择带宽（示例策略，可按需调整）
            double bandwidth;
            bandwidth = centerFreq/2;
//            if (centerFreq <= 500) {
//                bandwidth = 150;    // 125Hz~500Hz 区间，带宽 50Hz
//            } else if (centerFreq <= 2000) {
//                bandwidth = 100;   // 500Hz~2000Hz 区间，带宽 100Hz
//            } else {
//                bandwidth = 200;   // 2000Hz~8000Hz 区间，带宽 200Hz
//            }

            // 计算数字角频率 w0 = 2π·f0/fs
            double w0 = 2.0 * Math.PI * centerFreq / sampleRate;
            // 计算 Q = f0 / 带宽
            double Q = centerFreq / bandwidth;
            // 计算 alpha = sin(w0)/(2·Q)
            double alpha = Math.sin(w0) / (2.0 * Q);

            // 未归一化的滤波器系数
            double b0_un = alpha;
            double b1_un = 0.0;
            double b2_un = -alpha;
            double a0_un = 1.0 + alpha;
            double a1_un = -2.0 * Math.cos(w0);
            double a2_un = 1.0 - alpha;

            // 归一化后系数（一级二阶带通节）
            b0 = b0_un / a0_un;
            b1 = b1_un / a0_un;
            b2 = b2_un / a0_un;
            a1 = a1_un / a0_un;
            a2 = a2_un / a0_un;
        }

        /**
         * 对整段输入信号执行四阶带通滤波：
         *   - 先通过第一节（二阶）做正向 + 反向零相位滤波；
         *   - 再通过第二节（二阶）做正向 + 反向零相位滤波；
         *   - 最终输出即为四阶带通滤波结果。
         *
         * @param input 原始信号数组（如白噪声）
         * @return      四阶带通滤波后的信号数组
         */
        public double[] process(double[] input) {
            int len = input.length;
            double[] stage1 = new double[len];
            double[] stage2 = new double[len];

            // ------------------------
            // 第一级：正向滤波
            // ------------------------
            for (int n = 0; n < len; n++) {
                double x0 = input[n];
                // 差分方程： y0 = b0*x0 + b1*x1a + b2*x2a - a1*y1a - a2*y2a
                double y0 = b0 * x0 + b1 * x1a + b2 * x2a - a1 * y1a - a2 * y2a;
                stage1[n] = y0;
                // 更新状态
                x2a = x1a;
                x1a = x0;
                y2a = y1a;
                y1a = y0;
            }
            // ------------------------
            // 第一级：反向滤波（零相位）
            // ------------------------
            double[] stage1_rev = new double[len];
            double zx1 = 0, zx2 = 0, zy1 = 0, zy2 = 0;
            for (int n = len - 1; n >= 0; n--) {
                double x0 = stage1[n];
                double y0 = b0 * x0 + b1 * zx1 + b2 * zx2 - a1 * zy1 - a2 * zy2;
                stage1_rev[n] = y0;
                zx2 = zx1;
                zx1 = x0;
                zy2 = zy1;
                zy1 = y0;
            }

            // 重置第一级的历史状态，以免影响第二级
            x1a = x2a = y1a = y2a = 0;

            // ------------------------
            // 第二级：正向滤波
            // ------------------------
            for (int n = 0; n < len; n++) {
                double x0 = stage1_rev[n];
                double y0 = b0 * x0 + b1 * x1a + b2 * x2a - a1 * y1a - a2 * y2a;
                stage2[n] = y0;
                x2a = x1a;
                x1a = x0;
                y2a = y1a;
                y1a = y0;
            }
            // ------------------------
            // 第二级：反向滤波（零相位）
            // ------------------------
            double[] output = new double[len];
            zx1 = zx2 = zy1 = zy2 = 0;
            for (int n = len - 1; n >= 0; n--) {
                double x0 = stage2[n];
                double y0 = b0 * x0 + b1 * zx1 + b2 * zx2 - a1 * zy1 - a2 * zy2;
                output[n] = y0;
                zx2 = zx1;
                zx1 = x0;
                zy2 = zy1;
                zy1 = y0;
            }

            // 重置第二级的历史状态，以便后续多次调用时保持干净
            x1a = x2a = y1a = y2a = 0;

            return output;
        }

        /**
         * 重置滤波器状态（对多段输入进行分段滤波时可调用）。
         */
        public void reset() {
            x1a = x2a = y1a = y2a = 0;
            x1b = x2b = y1b = y2b = 0;
        }
    }

    // 二阶 IIR 滤波器（Direct Form I）

    private static class BiquadFilter {
        private final double b0, b1, b2, a1, a2;
        private double z1 = 0, z2 = 0;

//        private static final Map<Integer, double[][]> FILTER_PARAMETERS = new HashMap<>();
//        static {
//            // 初始化频率-滤波参数表，四个级别的滤波器系数在同一行
//            FILTER_PARAMETERS.put(125, new double[][]{
//                    {0.014, 0, -0.014, 1, -1.8863, 0.8923},
//                    {0.014, 0, -0.014, 1, -1.8863, 0.8923},
//                    {0.014, 0, -0.014, 1, -1.8863, 0.8923},
//                    {0.014, 0, -0.014, 1, -1.8863, 0.8923}
//            });
//
//            FILTER_PARAMETERS.put(250, new double[][]{
//                    {0.014, 0, -0.014, 1, -1.8863, 0.8923},
//                    {0.014, 0, -0.014, 1, -1.8863, 0.8923},
//                    {0.014, 0, -0.014, 1, -1.8863, 0.8923},
//                    {0.014, 0, -0.014, 1, -1.8863, 0.8923}
//            });
//
//            FILTER_PARAMETERS.put(500, new double[][]{
//                    {0.014, 0, -0.014, 1, -1.8863, 0.8923},
//                    {0.015, 0, -0.015, 1, -1.8800, 0.8900},
//                    {0.013, 0, -0.013, 1, -1.8900, 0.8930},
//                    {0.016, 0, -0.016, 1, -1.8850, 0.8915}
//            });
//
//            FILTER_PARAMETERS.put(750, new double[][]{
//                    {0.015, 0, -0.015, 1, -1.8870, 0.8940},
//                    {0.015, 0, -0.015, 1, -1.8870, 0.8940},
//                    {0.015, 0, -0.015, 1, -1.8870, 0.8940},
//                    {0.015, 0, -0.015, 1, -1.8870, 0.8940}
//            });
//
//            FILTER_PARAMETERS.put(1000, new double[][]{
//                    {0.016, 0, -0.016, 1, -1.8880, 0.8950},
//                    {0.016, 0, -0.016, 1, -1.8880, 0.8950},
//                    {0.016, 0, -0.016, 1, -1.8880, 0.8950},
//                    {0.016, 0, -0.016, 1, -1.8880, 0.8950}
//            });
//
//            FILTER_PARAMETERS.put(1500, new double[][]{
//                    {0.017, 0, -0.017, 1, -1.8890, 0.8960},
//                    {0.017, 0, -0.017, 1, -1.8890, 0.8960},
//                    {0.017, 0, -0.017, 1, -1.8890, 0.8960},
//                    {0.017, 0, -0.017, 1, -1.8890, 0.8960}
//            });
//
//            FILTER_PARAMETERS.put(2000, new double[][]{
//                    {0.018, 0, -0.018, 1, -1.8900, 0.8970},
//                    {0.018, 0, -0.018, 1, -1.8900, 0.8970},
//                    {0.018, 0, -0.018, 1, -1.8900, 0.8970},
//                    {0.018, 0, -0.018, 1, -1.8900, 0.8970}
//            });
//
//            FILTER_PARAMETERS.put(3000, new double[][]{
//                    {0.019, 0, -0.019, 1, -1.8910, 0.8980},
//                    {0.019, 0, -0.019, 1, -1.8910, 0.8980},
//                    {0.019, 0, -0.019, 1, -1.8910, 0.8980},
//                    {0.019, 0, -0.019, 1, -1.8910, 0.8980}
//            });
//
//            FILTER_PARAMETERS.put(4000, new double[][]{
//                    {0.020, 0, -0.020, 1, -1.8920, 0.8990},
//                    {0.020, 0, -0.020, 1, -1.8920, 0.8990},
//                    {0.020, 0, -0.020, 1, -1.8920, 0.8990},
//                    {0.020, 0, -0.020, 1, -1.8920, 0.8990}
//            });
//
//            FILTER_PARAMETERS.put(5000, new double[][]{
//                    {0.021, 0, -0.021, 1, -1.8930, 0.9000},
//                    {0.021, 0, -0.021, 1, -1.8930, 0.9000},
//                    {0.021, 0, -0.021, 1, -1.8930, 0.9000},
//                    {0.021, 0, -0.021, 1, -1.8930, 0.9000}
//            });
//
//            FILTER_PARAMETERS.put(6000, new double[][]{
//                    {0.022, 0, -0.022, 1, -1.8940, 0.9010},
//                    {0.022, 0, -0.022, 1, -1.8940, 0.9010},
//                    {0.022, 0, -0.022, 1, -1.8940, 0.9010},
//                    {0.022, 0, -0.022, 1, -1.8940, 0.9010}
//            });
//
//            FILTER_PARAMETERS.put(7000, new double[][]{
//                    {0.023, 0, -0.023, 1, -1.8950, 0.9020},
//                    {0.023, 0, -0.023, 1, -1.8950, 0.9020},
//                    {0.023, 0, -0.023, 1, -1.8950, 0.9020},
//                    {0.023, 0, -0.023, 1, -1.8950, 0.9020}
//            });
//
//            FILTER_PARAMETERS.put(8000, new double[][]{
//                    {0.024, 0, -0.024, 1, -1.8960, 0.9030},
//                    {0.024, 0, -0.024, 1, -1.8960, 0.9030},
//                    {0.024, 0, -0.024, 1, -1.8960, 0.9030},
//                    {0.024, 0, -0.024, 1, -1.8960, 0.9030}
//            });
//        }

        private static final Map<Integer, double[][]> FILTER_PARAMETERS = new HashMap<>();
        static {
            // 初始化频率-滤波参数表，四个级别的滤波器系数在同一行
            FILTER_PARAMETERS.put(125, new double[][]{
                    {0.014, 0, -0.014, 1, -1.8863, 0.8950}, // 提高增益
                    {0.014, 0, -0.014, 1, -1.8863, 0.8950},
                    {0.014, 0, -0.014, 1, -1.8863, 0.8950},
                    {0.014, 0, -0.014, 1, -1.8863, 0.8950}
            });

            FILTER_PARAMETERS.put(250, new double[][]{
                    {0.014, 0, -0.014, 1, -1.8863, 0.8965}, // 显著增加增益
                    {0.014, 0, -0.014, 1, -1.8863, 0.8965},
                    {0.014, 0, -0.014, 1, -1.8863, 0.8965},
                    {0.014, 0, -0.014, 1, -1.8863, 0.8965}
            });

            FILTER_PARAMETERS.put(500, new double[][]{
                    {0.014, 0, -0.014, 1, -1.8863, 0.8980}, // 增强增益
                    {0.015, 0, -0.015, 1, -1.8800, 0.8995},
                    {0.013, 0, -0.013, 1, -1.8900, 0.9000},
                    {0.016, 0, -0.016, 1, -1.8850, 0.9010}
            });

            FILTER_PARAMETERS.put(750, new double[][]{
                    {0.015, 0, -0.015, 1, -1.8870, 0.9020}, // 增加增益
                    {0.015, 0, -0.015, 1, -1.8870, 0.9025},
                    {0.015, 0, -0.015, 1, -1.8870, 0.9030},
                    {0.015, 0, -0.015, 1, -1.8870, 0.9035}
            });

            FILTER_PARAMETERS.put(1000, new double[][]{
                    {0.016, 0, -0.016, 1, -1.8880, 0.9050}, // 增大高频增益
                    {0.016, 0, -0.016, 1, -1.8880, 0.9055},
                    {0.016, 0, -0.016, 1, -1.8880, 0.9060},
                    {0.016, 0, -0.016, 1, -1.8880, 0.9065}
            });

            FILTER_PARAMETERS.put(1500, new double[][]{
                    {0.017, 0, -0.017, 1, -1.8890, 0.9080}, // 显著提高高频增益
                    {0.017, 0, -0.017, 1, -1.8890, 0.9085},
                    {0.017, 0, -0.017, 1, -1.8890, 0.9090},
                    {0.017, 0, -0.017, 1, -1.8890, 0.9095}
            });

            FILTER_PARAMETERS.put(2000, new double[][]{
                    {0.018, 0, -0.018, 1, -1.8900, 0.9105}, // 更大增益差异
                    {0.018, 0, -0.018, 1, -1.8900, 0.9110},
                    {0.018, 0, -0.018, 1, -1.8900, 0.9115},
                    {0.018, 0, -0.018, 1, -1.8900, 0.9120}
            });

            FILTER_PARAMETERS.put(3000, new double[][]{
                    {0.019, 0, -0.019, 1, -1.8910, 0.9130}, // 加大增益差异
                    {0.019, 0, -0.019, 1, -1.8910, 0.9135},
                    {0.019, 0, -0.019, 1, -1.8910, 0.9140},
                    {0.019, 0, -0.019, 1, -1.8910, 0.9145}
            });

            FILTER_PARAMETERS.put(4000, new double[][]{
                    {0.020, 0, -0.020, 1, -1.8920, 0.9160}, // 更大的增益变化
                    {0.020, 0, -0.020, 1, -1.8920, 0.9165},
                    {0.020, 0, -0.020, 1, -1.8920, 0.9170},
                    {0.020, 0, -0.020, 1, -1.8920, 0.9175}
            });

            FILTER_PARAMETERS.put(5000, new double[][]{
                    {0.021, 0, -0.021, 1, -1.8930, 0.9190}, // 高频增益急剧增加
                    {0.021, 0, -0.021, 1, -1.8930, 0.9195},
                    {0.021, 0, -0.021, 1, -1.8930, 0.9200},
                    {0.021, 0, -0.021, 1, -1.8930, 0.9205}
            });

            FILTER_PARAMETERS.put(6000, new double[][]{
                    {0.022, 0, -0.022, 1, -1.8940, 0.9210}, // 明显增加增益
                    {0.022, 0, -0.022, 1, -1.8940, 0.9215},
                    {0.022, 0, -0.022, 1, -1.8940, 0.9220},
                    {0.022, 0, -0.022, 1, -1.8940, 0.9225}
            });

            FILTER_PARAMETERS.put(7000, new double[][]{
                    {0.023, 0, -0.023, 1, -1.8950, 0.9230}, // 继续增加增益
                    {0.023, 0, -0.023, 1, -1.8950, 0.9235},
                    {0.023, 0, -0.023, 1, -1.8950, 0.9240},
                    {0.023, 0, -0.023, 1, -1.8950, 0.9245}
            });

            FILTER_PARAMETERS.put(8000, new double[][]{
                    {0.024, 0, -0.024, 1, -1.8960, 0.9250}, // 最大增益
                    {0.024, 0, -0.024, 1, -1.8960, 0.9255},
                    {0.024, 0, -0.024, 1, -1.8960, 0.9260},
                    {0.024, 0, -0.024, 1, -1.8960, 0.9265}
            });
        }

        private BiquadFilter(double b0, double b1, double b2, double a1, double a2) {
            this.b0 = b0;
            this.b1 = b1;
            this.b2 = b2;
            this.a1 = a1;
            this.a2 = a2;
        }

        // 对整个数组做正向滤波
        public double[] process(double[] in) {
            double[] out = new double[in.length];

            // 正向滤波
            for (int i = 0; i < in.length; i++) {
                double x = in[i];

                // 防止非法输入，NaN 或极值会导致计算不稳定
                if (Double.isNaN(x) || Math.abs(x) > 1e10) {
                    Log.e("pgh", "process: NaN");
                    x = 0;  // 防止 NaN 输入影响计算
                }

                // 计算当前输出
                double y = b0 * x + b1 * z1 + b2 * z2 - a1 * z1 - a2 * z2;

                // 将输出限制在合理的范围内
                if (Double.isNaN(y) || Math.abs(y) > 1e10) {
                    Log.e("pgh", "process: NaN");
                    y = 0; // 如果输出超出合理范围，进行重置
                }

                out[i] = y;

                // 更新延迟状态
                z2 = z1;
                z1 = y;
            }

            // 反向滤波：为了零相位滤波，我们需要再做一次反向滤波
            out = reverseProcess(out);
            return out;
        }

        // 反向滤波
        private double[] reverseProcess(double[] in) {
            double[] out = new double[in.length];
            double z1_rev = 0, z2_rev = 0;  // 反向滤波的延迟状态

            // 反向滤波
            for (int i = in.length - 1; i >= 0; i--) {
                double x = in[i];

                // 防止非法输入，NaN 或极值会导致计算不稳定
                if (Double.isNaN(x) || Math.abs(x) > 1e10) {
                    Log.e("pgh", "reverseProcess: NaN");
                    x = 0;  // 防止 NaN 输入影响计算
                }

                // 计算当前输出
                double y = b0 * x + b1 * z1_rev + b2 * z2_rev - a1 * z1_rev - a2 * z2_rev;

                // 将输出限制在合理的范围内
                if (Double.isNaN(y) || Math.abs(y) > 1e10) {
                    Log.e("pgh", "reverseProcess: NaN");
                    y = 0; // 如果输出超出合理范围，进行重置
                }

                out[i] = y;

                // 更新反向延迟
                z2_rev = z1_rev;
                z1_rev = y;
            }

            return out;
        }
    }

}




