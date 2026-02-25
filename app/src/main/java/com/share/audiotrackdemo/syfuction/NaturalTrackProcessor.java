package com.share.audiotrackdemo.syfuction;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import java.io.*;
import com.share.audiotrackdemo.R;

/**
 * 自然音处理“车间”
 */
public class NaturalTrackProcessor{
    private Context context;

    public NaturalTrackProcessor(Context context) {
        this.context = context;
    }

    // 对应原调度中心 readWavFile
    public byte[] processNaturalAudio(int type, int time, JSONObject para, boolean isLeft) {
        try {
            // pnz on 2026-02-24
            // 左右声道的json串早在goPlayNew就分开了,不需要getChannelData再区分
/*            byte[] leftChannelNaturalAudio = readWaveFile(type, time, true, para);
            byte[] rightChannelNaturalAudio = readWaveFile(type, time, false, para);*/
            byte[] naturalAudio = readWaveFile(type, time, para, isLeft);
            return naturalAudio;

/*            InputStream fis = context.getResources().openRawResource(getResid(type, isLeft));
            int db = para.optInt("db", 0);
            db = Math.max(0, Math.min(120, db));

            byte[] audioData = extractAudioData(fis, isLeft);
            byte[] adjustedData = adjustVolume(type, audioData, db);
            return pcm_16bit_to_24bit(adjustedData);*/
        } catch (Exception e) {
            return new byte[0];
        }
    }

    // --- 以下填充所有自然音的功能辅助函数 ---
    public byte[] readWaveFile(int type, long time, JSONObject para, boolean isLeft) {
        // 获取 InputStream
        InputStream fis = context.getResources().openRawResource(getResid(type, isLeft));

        try {
            // 获取 para 中的 db 值，默认值为 0，且限制在合理范围内
            int db = para.optInt("db", 0);  // 默认值 0dB
            db = Math.max(0, Math.min(120, db)); // 限制 db 在 0 到 120 范围内

            // 读取文件并获取所选声道的数据
            byte[] audioData = extractAudioData(fis, isLeft);

            // 调整音量
            byte[] adjustedAudioData = adjustVolume(type, audioData, db);

            return pcm_16bit_to_24bit(adjustedAudioData);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    } //# 自然音调度中心

    private byte[] pcm_16bit_to_24bit(byte[] wav) {
        int numSamples = wav.length / 2;
        byte[] result = new byte[numSamples * 3];

        for (int i = 0; i < numSamples; i++) {
            int sample16bit = ((wav[i * 2 + 1] & 0xFF) << 8) | (wav[i * 2] & 0xFF);
            result[i * 3] = 0;
            result[i * 3 + 1] = wav[i * 2];
            result[i * 3 + 2] = wav[i * 2 + 1];
        }

        return result;
    } //# 自然音 功能函数

    private byte[] extractAudioData(InputStream fis, boolean isLeft) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        // 跳过 WAV 文件头部分，直至音频数据部分（假设头部大小是44字节）
        fis.skip(44);

        // 读取音频数据
        while ((bytesRead = fis.read(buffer)) != -1) {
            // 处理读取的音频数据
            processAudioBuffer(buffer, bytesRead, isLeft, byteArrayOutputStream);
        }

        // 返回提取出的音频数据
        return byteArrayOutputStream.toByteArray();
    } //# 自然音 功能函数

    private void processAudioBuffer(byte[] buffer, int bytesRead, boolean isLeft, ByteArrayOutputStream byteArrayOutputStream) {
        for (int i = 0; i < bytesRead; i += 4) {
            if (isLeft) {
                // isLeft == 1：提取左声道数据（16位深度，小端格式，2声道，左声道在前）
                byteArrayOutputStream.write(buffer[i]);
                byteArrayOutputStream.write(buffer[i + 1]);
            } else {
                // isLeft == 0：提取右声道数据（16位深度，小端格式，2声道，右声道在后）
                byteArrayOutputStream.write(buffer[i + 2]);
                byteArrayOutputStream.write(buffer[i + 3]);
            }
        }
    } //# 自然音 跟着extractAudioData

    private static double getGainForDb(int type, int db) {
        double gain;
        // 遍历查表数组，根据 db 值查找对应的增益
        for (double[][] typeData : gainTables) {
            if ((int) typeData[0][0] == type) {  // 将 type 转为 int 后与 typeData[0][0] 比较
                // 遍历 db 范围并执行线性插值
                for (int i = 1; i < typeData.length - 1; i++) {
                    int db1 = (int) typeData[i][0];  // db1 是整型
                    double gain1 = typeData[i][1];   // gain1 是 double 类型
                    int db2 = (int) typeData[i + 1][0];  // db2 是整型
                    double gain2 = typeData[i + 1][1];   // gain2 是 double 类型

                    if (db >= db1 && db <= db2) {
                        // 进行线性插值
                        gain = gain1 + ((db - db1) / (double) (db2 - db1)) * (gain2 - gain1);
                        return gain;
                    }
                }
            }
        }

        // 如果 db 值没有完全匹配，且超出查表的最大值，返回最大增益值
        // 你可以根据需求返回默认的增益值，或者使用最大值
        gain = 1.0;
        return gain;
    } //# 自然音 跟着adjustVolume

    public static byte[] adjustVolume(int type, byte[] audioData, int db) {
        // 获取 db 对应的增益值
        double gain = getGainForDb(type, db);

        byte[] adjustedAudioData = new byte[audioData.length];

        // 遍历每个音频样本，调整音量
        for (int i = 0; i < audioData.length; i += 2) {  // 每个样本占 2 字节（16 位）
            // 读取 16 位小端音频样本
            short sample = (short) ((audioData[i + 1] << 8) | (audioData[i] & 0xFF));

            // 调整音量
            sample = (short) Math.min(Math.max(sample * gain, Short.MIN_VALUE), Short.MAX_VALUE);

            // 写回调整后的样本到新的字节数组中
            adjustedAudioData[i] = (byte) (sample & 0xFF);
            adjustedAudioData[i + 1] = (byte) ((sample >> 8) & 0xFF);
        }

        return adjustedAudioData;
    } //# 自然音 功能函数

    private int getResid(int type, boolean isLeft) {
        int resid=0;
        if(type==8){
            resid=isLeft?R.raw.l_type_8:R.raw.r_type_8;
        }else if(type==9){
            resid=isLeft?R.raw.l_type_9:R.raw.r_type_9;
        }else if(type==13){ // 语言噪音
            resid=isLeft?R.raw.l_type_13:R.raw.r_type_13;
        }else if(type==14){
            resid=isLeft?R.raw.l_type_14:R.raw.r_type_14;
        }else if(type==15){
            resid=isLeft?R.raw.l_type_15:R.raw.r_type_15;
        }else if(type==16){
            resid=isLeft?R.raw.l_type_16:R.raw.r_type_16;
        }else if(type==17){
            resid=isLeft?R.raw.l_type_17:R.raw.r_type_17;
        }else if(type==18){
            resid=isLeft?R.raw.l_type_18:R.raw.r_type_18;
        }else if(type==19){
            resid=isLeft?R.raw.l_type_19:R.raw.r_type_19;
        }else if(type==20){
            resid=isLeft?R.raw.l_type_20:R.raw.r_type_20;
        }else if(type==21){
            resid=isLeft?R.raw.l_type_21:R.raw.r_type_21;
        }else if(type==22){
            resid=isLeft?R.raw.l_type_22:R.raw.r_type_22;
        }else if(type==23){
            resid=isLeft?R.raw.l_type_23:R.raw.r_type_23;
        }else if(type==24){
            resid=isLeft?R.raw.l_type_24:R.raw.r_type_24;
        }
        return resid;
    } //# 自然音 功能函数

    // 搬运原 gainTables 数据
    public static double[][][] gainTables = { //# 自然音 关键数据
            //言语噪音
            //{{13}, {0, 0.000000}, {1, 0.000001}, {10, 0.0001}, {40, 0.0001}, {45, 0.005}, {50, 0.008}, {60, 0.022}, {70, 0.041}, {80, 0.1200}, {90, 0.370},{100, 0.950}, {110, 4.72800},{120, 35.9000}},
            {{13}, {0, 0.000000}, {1, 0.000001}, {10, 0.0006}, {50, 0.008}, {60, 0.022}, {70, 0.041}, {80, 0.1200}, {90, 0.370},{100, 1.150}, {110, 4.72800},{120, 35.9000}},
            //低频-风声
            {{8}, {0, 0.000000}, {1, 0.000001},  {10, 0.0006}, {50, 0.0300}, {60, 0.100}, {70, 0.3500}, {80, 0.8000}, {90, 1.0000},{100, 1.5000}, {110, 5.3000},{120, 29.9000}},
            // 低频-kuanxi室
            {{9}, {0, 0.000000}, {1, 0.000001},  {10, 0.0006}, {50, 0.0050}, {60, 0.010}, {70, 0.0300}, {80, 0.0600}, {90, 0.2000},{100, 0.7000}, {110, 2.3000},{120, 29.9000}},
            // 低频-水声1
            {{14}, {0, 0.000000}, {1, 0.000001}, {10, 0.0006},  {50, 0.0050}, {60, 0.020}, {70, 0.0500}, {80, 0.180}, {90, 0.4400},{100, 1.3700}, {110, 5.3000},{120, 31.9000}},
            // 多频-河水
            {{15}, {0, 0.000000}, {1, 0.000001}, {10, 0.0006}, {50, 0.00300}, {60, 0.00800}, {70, 0.01600}, {80, 0.04600}, {90, 0.1700},{100, 0.5500}, {110, 1.570},{120, 29.3000}},
            // 多频-瀑布
            {{16}, {0, 0.000000}, {1, 0.000001}, {10, 0.0006}, {50, 0.008}, {60, 0.018}, {70, 0.038}, {80, 0.138}, {90, 0.298},{100, 0.750}, {110, 4.3000},{120, 29.9000}},
            // 多频-树叶
            {{17}, {0, 0.000000}, {1, 0.000001}, {10, 0.0006}, {50, 0.004}, {60, 0.009}, {70, 0.019}, {80, 0.059}, {90, 0.139},{100, 0.409}, {110, 1.609},{120, 7.609}},
            // 高频-虫鸣
            {{18}, {0, 0.000000}, {1, 0.000001}, {10, 0.0006}, {50, 0.0035}, {60, 0.008}, {70, 0.0170}, {80, 0.0580}, {90, 0.1580},{100, 0.3599}, {110, 1.5580},{120, 8.5580}},
            // 高频-淋浴
            {{19}, {0, 0.000000}, {1, 0.000001}, {10, 0.0006}, {50, 0.0055}, {60, 0.013}, {70, 0.0270}, {80, 0.0780}, {90, 0.2180},{100, 0.6599}, {110, 3.0580},{120, 17.5580}},
            // 高频-鸟鸣1
            {{20}, {0, 0.000000}, {1, 0.000001}, {10, 0.0006}, {50, 0.002}, {60, 0.005}, {70, 0.0120}, {80, 0.0380}, {90, 0.0980},{100, 0.3980}, {110, 0.9980},{120, 9.9980}},
            // 全频-风声3
            {{21}, {0, 0.000000}, {1, 0.000001}, {10, 0.0006}, {50, 0.0025}, {60, 0.0880}, {70, 0.0980}, {80, 0.1380}, {90, 0.2180},{100, 0.6599}, {110, 3.0580},{120, 17.5580}},
            // 中频-滴水
            {{22}, {0, 0.000000}, {1, 0.000001}, {10, 0.0006}, {50, 0.0380}, {60, 0.0880}, {70, 0.1880}, {80, 0.3180}, {90, 0.9180},{100, 1.9599}, {110, 3.0580},{120, 17.5580}},
            // 中频-鸟鸣3
            {{23}, {0, 0.000000}, {1, 0.000001},  {10, 0.0006}, {50, 0.0055}, {60, 0.013}, {70, 0.0270}, {80, 0.0780}, {90, 0.2180},{100, 0.6599}, {110, 2.0580},{120, 6.5580}},
            // 中频-雨声
            {{24}, {0, 0.000000}, {1, 0.000001},  {10, 0.0006}, {50, 0.0055}, {60, 0.013}, {70, 0.0270}, {80, 0.0780}, {90, 0.2580},{100, 0.8599}, {110, 3.0580},{120, 13.5580}},
            // 这里可以根据实际需求继续扩展
    };
}