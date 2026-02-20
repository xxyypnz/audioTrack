package com.share.audiotrackdemo.demo1;

/**
 * @Description: 描述
 * @Author: wangcheng
 * @CeateDate: 2023/8/10
 * @UpdateUser: 更新着
 * @UpdateData: 更新时间
 * @UpdateRemark: 更新说明
 * @Vsersion: 1.0
 */
public class SinWave1 {
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
    public static byte[] sin(int xd,byte[] wave, int waveLen, int length) {
        for (int i = 0; i < length; i++) {
            wave[i] = (byte) (xd * (1 - Math.sin(TWOPI * ((i % waveLen) * 1.00 / waveLen))));
        }
        return wave;
    }
}