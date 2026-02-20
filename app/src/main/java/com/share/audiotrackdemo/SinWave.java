package com.share.audiotrackdemo;

/**
 * @Description: 描述
 * @Author: wangcheng
 * @CeateDate: 2023/8/10
 * @UpdateUser: 更新着
 * @UpdateData: 更新时间
 * @UpdateRemark: 更新说明
 * @Vsersion: 1.0
 */
public class SinWave {
    /** 正弦波的高度 2的8次方减1**/
    public static final double HEIGHT = 127;//0-32767
    /** 2PI **/
    public static final double TWOPI = 2 * 3.1415;

    /**
     * 生成正弦波
     *
     * @param wave
     * @param waveLen
     *            每段正弦波的长度
     * @param length
     *            总长度
     * @return
     */
    /*public static byte[] sin(int xd,byte[] wave, int waveLen, int length) {
        for (int i = 0; i < length; i++) {
            wave[i] = (byte) (xd * (1 - Math.sin(TWOPI * ((i % waveLen) * 1.00 / waveLen))));
        }
        return wave;
    }*/

    public static byte[] sin(int xd, int hz, int RATE) {
        double delta = 2.00 * Math.PI / RATE;
        int wave_length = RATE * 2;
        byte[] wave = new byte[wave_length];

        System.out.println("xd=" + xd + ", hz=" + hz + ", RATE=" + RATE +
                ", wave_length=" + wave_length);
        for (int i = 0; i < wave_length / 2; i++) {
            double x = i * delta * hz;
            short out = (short) (xd * Math.sin(x));
            wave[2 * i] = (byte)(out & 0xff);
            wave[2 * i + 1] = (byte)((out & 0xff00) >> 8);
        }
        return wave;
    }
}