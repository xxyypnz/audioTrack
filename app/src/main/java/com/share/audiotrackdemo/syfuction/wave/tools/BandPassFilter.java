package com.share.audiotrackdemo.syfuction.wave.tools;

public class BandPassFilter {
    private double sampleRate;
    private double centerFrequency;
    private double bandwidth;
    private double[] x, y;
    private double a0, a1, a2, b0, b1, b2;

    public BandPassFilter(double sampleRate, double centerFrequency, double bandwidth) {
        this.sampleRate = sampleRate;
        this.centerFrequency = centerFrequency;
        this.bandwidth = bandwidth;

        x = new double[3];
        y = new double[3];

        calculateCoefficients();
    }

    private void calculateCoefficients() {
        double omegaC = 2.0 * Math.PI * centerFrequency / sampleRate;
        double omegaB = 2.0 * Math.PI * bandwidth / sampleRate;
        double alpha = Math.sin(omegaC) * sinh(omegaB / 2.0);

        a0 = 1 + alpha;
        a1 = -2.0 * Math.cos(omegaC);
        a2 = 1 - alpha;

        b0 = alpha;
        b1 = 0;
        b2 = -alpha;
    }

    public double applyFilter(double input) {
        // 更新历史输入和输出缓冲
        x[2] = x[1];
        x[1] = x[0];
        x[0] = input;

        y[2] = y[1];
        y[1] = y[0];

        // 计算滤波器输出
        double output = (b0 / a0) * x[0] + (b1 / a0) * x[1] + (b2 / a0) * x[2]
                - (a1 / a0) * y[1] - (a2 / a0) * y[2];

        // 更新历史输出缓冲
        y[0] = output;

        return output;
    }

    public double[] applyFilter(double[] input) {
        double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++)
            output[i] = applyFilter(input[i]);
        return output;
    }

    // 辅助函数：计算双曲正弦函数 sinh
    private double sinh(double x) {
        return (Math.exp(x) - Math.exp(-x)) / 2.0;
    }
}

/*public class BandPassFilter {
    private double sampleRate;
    private double centerFrequency;
    private double bandwidth;
    private double[] x, y;
    private double a0, a1, b0, b1;

    public BandPassFilter(double sampleRate, double centerFrequency, double bandwidth) {
        this.sampleRate = sampleRate;
        this.centerFrequency = centerFrequency;
        this.bandwidth = bandwidth;

        x = new double[2];
        y = new double[2];

        calculateCoefficients();
    }

    private void calculateCoefficients() {
        double omegaC = 2.0 * Math.PI * centerFrequency / sampleRate;
        double alpha = Math.sin(omegaC) * Math.sinh(omegaC / bandwidth); // 一阶滤波器的计算方式

        a0 = 1 + alpha;
        a1 = -2.0 * Math.cos(omegaC);
        b0 = alpha;
        b1 = -alpha;
    }

    public double applyFilter(double input) {
        // 更新历史输入和输出缓冲
        x[1] = x[0];
        x[0] = input;

        y[1] = y[0];

        // 计算滤波器输出
        double output = (b0 / a0) * x[0] + (b1 / a0) * x[1] - (a1 / a0) * y[1];

        // 更新历史输出缓冲
        y[0] = output;

        return output;
    }

    public double[] applyFilter(double[] input) {
        double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = applyFilter(input[i]);
        }
        return output;
    }

    // 辅助函数：计算双曲正弦函数 sinh
    private double sinh(double x) {
        return (Math.exp(x) - Math.exp(-x)) / 2.0;
    }
}*/