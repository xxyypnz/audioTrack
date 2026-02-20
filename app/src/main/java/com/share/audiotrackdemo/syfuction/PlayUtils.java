package com.share.audiotrackdemo.syfuction;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;

import com.share.audiotrackdemo.AppAppliction;
import com.share.audiotrackdemo.R;
import com.share.audiotrackdemo.syfuction.mixer.WaveMixer;
import com.share.audiotrackdemo.syfuction.mixer.WavePart;
import com.share.audiotrackdemo.syfuction.wave.waveImpl.AMWave;
import com.share.audiotrackdemo.syfuction.wave.waveImpl.AMWave2;
import com.share.audiotrackdemo.syfuction.wave.waveImpl.ChirpWave;
import com.share.audiotrackdemo.syfuction.wave.waveImpl.NarrowBandNoise;
import com.share.audiotrackdemo.syfuction.wave.waveImpl.PinkNoise;
import com.share.audiotrackdemo.syfuction.wave.waveImpl.PulseWave;
import com.share.audiotrackdemo.syfuction.wave.waveImpl.SinWave;
import com.share.audiotrackdemo.syfuction.wave.waveImpl.SquareWave;
import com.share.audiotrackdemo.syfuction.wave.waveImpl.TriangularWave;
import com.share.audiotrackdemo.syfuction.wave.waveImpl.Wave;
import com.share.audiotrackdemo.syfuction.wave.waveImpl.WhiteNoise;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 描述
 * @Author: wangcheng
 * @CeateDate: 2024/4/7
 * @UpdateUser: 更新着
 * @UpdateData: 更新时间
 * @UpdateRemark: 更新说明
 * @Vsersion: 1.0
 */
@Deprecated
public class PlayUtils {
    private Context mContent;//上下文
    private AudioPlayer player;//播放器
    private boolean isPlay = true;//一直播放
    private CountDownTimer timers;//播放本地文件的倒计时
    private CountDownTimer timer;//其他数据倒计时
    private List<MediaPlayer> mediaPlayers;

    public PlayUtils(Context context) {
        this.mContent = context;
    }
    /**
     * 播放
     * @param s
     */

    @Deprecated
    public void palynew(String s) {
        releaseAndInitMediaList(false);
        Log.e("收到---", s);
        JSONObject jsonObject = null;
        try {

            byte[] waveLeft; /* 最终波形 左*/
            byte[] waveRight; /* 最终波形 右*/
            boolean isLeft = false;//是否有左
            boolean isRight = false;//是否有右
            jsonObject = new JSONObject(s);
            long time = jsonObject.getLong("time");//播放时间
            JSONArray left = jsonObject.getJSONArray("left");//左
            if (left.length() != 0) {
                //不是空数组
                isLeft = true;
                if (left.length() == 1) {
                    JSONObject left1 = left.getJSONObject(0);
                    int type = left1.getInt("type");//声音类型
                    JSONObject para = left1.getJSONObject("para");
                    if (isTypeBoolean(type)) {
                        Wave wave = getWave(para, type);
                        wave.SetDurationMs((int) (time*1000));
                        if (wave == null) {
                            return;
                        }
                        waveLeft = getWaveByte(wave, para, type);

                    } else {
                        playType8and9Lists(type, time,true,para);
                        return;
                    }
                } else {
                    List<WavePart> parts=new ArrayList<>();
//                    WavePart parts[] = new WavePart[left.length()];
                    for (int i = 0; i < left.length(); i++) {
                        JSONObject leftd = left.getJSONObject(i);
                        int type = leftd.getInt("type");//声音类型
                        JSONObject para = leftd.getJSONObject("para");
                        if (isTypeBoolean(type)) {
                            Wave freq = getWave(para, type);
                            freq.SetDurationMs((int) (time*1000));
                            if (freq == null) {
                                return;
                            }
                            parts.add(getWavePart(freq, para, type));
                        }else{
                            playType8and9Lists(type, time,true,para);
                        }
                    }
                    if (parts.size()==1){
                        waveLeft = parts.get(0).Wave().GetWaveBuffer(parts.get(0).DB());
                    }else if (parts.size()>1){
                        WavePart[] wavePartArray = parts.toArray(new WavePart[parts.size()]);
                        WaveMixer mixer = new WaveMixer(wavePartArray);
                        mixer.SetDurationMs((int) (time*1000));
                        waveLeft = mixer.GetWaveBuffer();
                    }else {
                        return;
                    }
                }
            } else {
                waveLeft = new byte[0];
            }
            JSONArray right = jsonObject.getJSONArray("right");//右
            if (right.length() != 0) {
                //不是空数组
                isRight = true;
                if (right.length() == 1) {
                    JSONObject right1 = right.getJSONObject(0);
                    int type = right1.getInt("type");//声音类型
                    JSONObject para = right1.getJSONObject("para");
                    if (isTypeBoolean(type)) {
                        Wave wave = getWave(para, type);
                        wave.SetDurationMs((int) (time*1000));
                        if (wave == null) {
                            return;
                        }
                        waveRight = getWaveByte(wave, para, type);
                    } else {
                        playType8and9Lists(type, time,false,para);
                        return;

                    }
                } else {
                    List<WavePart> parts=new ArrayList<>();
//                    WavePart parts[] = new WavePart[right.length()];
                    for (int i = 0; i < right.length(); i++) {
                        JSONObject rightd = right.getJSONObject(i);
                        int type = rightd.getInt("type");//声音类型
                        JSONObject para = rightd.getJSONObject("para");
                        if (isTypeBoolean(type)) {
                            Wave freq = getWave(para, type);
                            freq.SetDurationMs((int) (time*1000));
                            if (freq == null) {
                                return;
                            }
                            parts.add( getWavePart(freq, para, type)) ;
                        }else{
                            playType8and9Lists(type, time,false,para);
                        }
                    }
                    if (parts.size()==1){
                        waveRight = parts.get(0).Wave().GetWaveBuffer(parts.get(0).DB());
                    }else if (parts.size()>1){
                        WavePart[] wavePartArray = parts.toArray(new WavePart[parts.size()]);
                        WaveMixer mixer = new WaveMixer(wavePartArray);
                        mixer.SetDurationMs((int) (time*1000));
                        waveRight = mixer.GetWaveBuffer();
                    }else {
                        return;
                    }
                }
            } else {
                waveRight = new byte[0];
            }
            if (isLeft && isRight) {//两种都有
                int minSize = Math.min(waveLeft.length, waveRight.length);
                byte[] stereoData = new byte[minSize * 2]; // 存储交错的立体声数据
                // 将左右声道的波形交错存储
                for (int i = 0; i < minSize / 2; i++) {
                    stereoData[i * 4] = waveLeft[i * 2];
                    stereoData[i * 4 + 1] = waveLeft[i * 2 + 1];
                    stereoData[i * 4 + 2] = waveRight[i * 2];
                    stereoData[i * 4 + 3] = waveRight[i * 2 + 1];
                }
                goPlayAudioNew(stereoData, time, true, true);
            } else if (isLeft && !isRight) {//只有左
                int minSize = waveLeft.length;
                byte[] stereoData = new byte[minSize * 2]; // 存储交错的立体声数据
                // 将左右声道的波形交错存储
                for (int i = 0; i < minSize / 2; i++) {
                    stereoData[i * 4] = waveLeft[i * 2];
                    stereoData[i * 4 + 1] = waveLeft[i * 2 + 1];
                    stereoData[i * 4 + 2] = 0;
                    stereoData[i * 4 + 3] = 0;
                }
                goPlayAudioNew(stereoData, time, true, false);
            } else if (!isLeft && isRight) {//只有右
                int minSize = waveRight.length;
                byte[] stereoData = new byte[minSize * 2]; // 存储交错的立体声数据
                // 将左右声道的波形交错存储
                for (int i = 0; i < minSize / 2; i++) {
                    stereoData[i * 4] = 0;
                    stereoData[i * 4 + 1] = 0;
                    stereoData[i * 4 + 2] = waveRight[i * 2];
                    stereoData[i * 4 + 3] = waveRight[i * 2 + 1];
                }
                goPlayAudioNew(stereoData, time, false, true);
            }
        } catch (JSONException e) {
            Log.e("-------", "解析异常");
//            throw new RuntimeException(e);
        } catch (Exception e) {
            Log.e("-------", "生产异常" + e.getMessage());
//            throw new RuntimeException(e);
        }

    }

    private boolean isTypeBoolean(int type) {
        if( type == 8 || type == 9||(type >= 13&&type<=24)){
            return false;
        }
        return true;
    }

    /**
     * 获取类型
     *
     * @param wave
     * @param para
     * @param type
     * @return  添加需要修改
     */
    private byte[] getWaveByte(Wave wave, JSONObject para, int type) {
        byte[] dbs = new byte[0];
        try {
            switch (type) {
                case 0://纯音
                case 1://转音
                case 2://三角波
                case 3://方波
                case 4://白噪声
                case 5://窄带噪音
                case 6://调幅音-1
                case 7://调幅音-2
                case 10://粉红噪音
                case 11://脉冲粉红噪音
                case 12://脉冲转音
                case 25://脉冲转音
                    dbs = wave.GetWaveBuffer(para.getInt("db"));
                    break;
//                case 8://
//                    dbs = WaveExtract.extractRawWaveform(AppAppliction.mApp.getResources().getAssets().open("nature_shuisheng_1_type8.wav"));
//                    break;
//                case 9:
//                    dbs = WaveExtract.extractRawWaveform(AppAppliction.mApp.getResources().getAssets().open("nature_fengsheng_1_type9.wav"));
//                    break;
//                    break;
                default://默认
                    dbs = wave.GetWaveBuffer(50);
                    break;
            }
            return dbs;
        } catch (Exception e) {
            Log.e("----------", e.getMessage());
        }
        return dbs;
    }
    //0:纯音;1: 峨音;2:三角波;3:方波，4:白噪音;5: 窄带噪音;
    // 6: 调幅音-1;7:调幅音-2;8:自然音-大海，9:自然音-风声，10 粉红噪音
    // 添加需要修改
    private WavePart getWavePart(Wave freq, JSONObject para, int type) {
        WavePart db = null;
        try {
            switch (type) {
                case 0://纯音
                case 1://转音
                case 2://三角波
                case 3://方波
                case 4://白噪音
                case 5://窄带噪音
                case 6://调幅音-1
                case 7://调幅音-2
                case 10://粉红噪音
                case 11://脉冲粉红噪音
                case 12://脉转冲噪音
                case 25://脉转冲噪音
                    db = new WavePart(freq, para.getInt("db"));
                    break;
//                case 8:
//                case 9:
//                    break;
                default://默认
                    db = new WavePart(freq, 50);
                    break;
            }
        } catch (Exception e) {

        }
        return db;
    }
    /**
     * 获取Wave
     *
     * @param para
     * @param type
     * @return 添加需要修改
     * pghpghpgh
     */
    private Wave getWave(JSONObject para, int type) {
        Wave wave = null;
        try {
            InputStream sinWaveFitting = AppAppliction.mApp.getResources().getAssets().open("sin_wave_fitting");
            /* 分贝校准文件 */
            File calibrationFile = new File("file:///android_asset/sin_wave_fitting");
            switch (type) {
                case 0://纯音
                    wave = new SinWave(para.getInt("freq")/* 频率 */, sinWaveFitting);
                    break;
                case 2://2: 三角波:
                    wave = new SquareWave(para.getInt("freq")/* 频率 */, sinWaveFitting);
                    break;
                case 3://3: 方波
                    wave = new TriangularWave(para.getInt("freq")/* 频率 */, sinWaveFitting);
                    break;
                case 1://转音
                    wave = new ChirpWave(para.getInt("freq")/* 频率 */,sinWaveFitting);
                    break;
                case 4://白噪声
                    wave = new WhiteNoise(sinWaveFitting);
                    break;
                case 5://窄带噪音
                    int v = (int) (para.getInt("freq") * 1.19 - para.getInt("freq") * 0.84);
                    wave = new NarrowBandNoise(para.getInt("freq")/* 频率 */,v , sinWaveFitting);
                    //wave = new NarrowBandNoise(para.getInt("freq"));
                    break;
//                case 6://调幅音-1
//                    wave = new AMWave(para.getInt("freq")/* 频率 */, 7,1, sinWaveFitting);
//                    break;
//                case 7://调幅音-2
//                    wave = new AMWave(para.getInt("freq")/* 频率 */, 20,1, sinWaveFitting);
//                    break;
                case 6://调幅音-1 // pgh ,不是这个地方调用，是在SyActivity.java中
                    wave = new AMWave(para.getInt("freq")/* 频率 */, 2000,0.5, sinWaveFitting);
                    break;
                case 7://调幅音-2
                    wave = new AMWave(para.getInt("freq")/* 频率 */, 4000,0.5, sinWaveFitting);
                    break;

                case 10://粉红噪音
                    wave = new PinkNoise(sinWaveFitting);
                    break;
                case 11://脉冲粉红噪音
                    Wave wave1 = new PinkNoise(sinWaveFitting);
                    wave = new PulseWave(500/* 宽度 */,250/* 间隔 */,wave1);
                    break;
                case 12://脉冲转音
                    Wave wave2 = new ChirpWave(para.getInt("freq")/* 频率 */, sinWaveFitting);
                    wave = new PulseWave(500,250/* 间隔 */,wave2);
                    break;
                case 25://脉冲纯音
                    Wave wave3 = new SinWave(para.getInt("freq")/* 频率 */, sinWaveFitting);
                    wave = new PulseWave(500,250/* 间隔 */,wave3);
                    break;
//                case 8://
//                    WaveExtract.extractWavData(AppAppliction.mApp.getResources().getAssets().open("nature_shuisheng_1_type8"));
//                    break;
//                case 9:
//                    WaveExtract.extractWavData(AppAppliction.mApp.getResources().getAssets().open("nature_fengsheng_1_type9"));
//                    break;
                default://默认
                    wave = new TriangularWave(4000/* 频率 */, sinWaveFitting);
                    break;
            }
        } catch (IOException e) {
//            throw new RuntimeException(e);
        } catch (Exception e) {
//            throw new RuntimeException(e);
        }
        return wave;
    }
    /**
     * 同时播放一个或者多个
     */
    private void playType8and9Lists(int type, long time,boolean isLeft,JSONObject para) {
        final MediaPlayer media = MediaPlayer.create(mContent, getResid(type,isLeft));
        media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {//控制循环播放
                if (mp != null) {
                    mp.start();
                }
            }
        });
//        media.setAudioAttributes(new AudioAttributes.Builder()
//                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
//                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
//                .setUsage(AudioAttributes.USAGE_MEDIA)
//                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//
//                .build());
        // 设置左耳和右耳的音量
        try {
//            media.prepare();
            int db=60; // test default val
            if(para.has("db")){
                db=para.getInt("db");
            }
            float volume = (1.0f/ 120) * db;
            media.setVolume(isLeft?volume:0.0f, isLeft?0.0f:volume); // 将左耳音量设置为 0，只播放右耳音频
            media.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        mediaPlayers.add(media);
        if(timers==null){
            timers = new CountDownTimer(time * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.e("-------12", millisUntilFinished + "");
                }

                @Override
                public void onFinish() {
                    Log.e("-------end", "结束");
                    if (mediaPlayers != null) {
                        for (int i = 0; i < mediaPlayers.size(); i++) {
                            MediaPlayer mediaPlayer = mediaPlayers.get(i);
                            if (null != mediaPlayer) {
                                mediaPlayer.release();
                                mediaPlayer = null;
                            }
                        }
                    }
                }
            };
            timers.start();
        }

    }

    private int getResid(int type,boolean isLeft) {
        int resid=0;
        if(type==8){
            resid=isLeft?R.raw.l_type_8:R.raw.r_type_8;
        }else if(type==9){
            resid=isLeft?R.raw.l_type_9:R.raw.r_type_9;
        }else if(type==13){
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
    }


    /**
     * @param wave 播放波形
     * @param time 播放时长
     * pghpghpgh
     */
    private void goPlayAudioNew(byte[] wave, long time, boolean left, boolean right) {
        destroyPlayer();
        destroyTime();
        isPlay = true;
        player = new AudioPlayer(mContent, wave, left, right);
        player.play();
        if (timer == null) {
            timer = new CountDownTimer(time * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.e("-------12", millisUntilFinished + "");
                }

                @Override
                public void onFinish() {
                    Log.e("-------end", "结束");
                    player.stop();
                }
            };
        }
        timer.start();
    }

    /**
     * 销毁播放器
     */
    private void destroyPlayer() {
        isPlay = false;
        if (null != player) {
            player.stop();
            player = null;
        }
    }
    /**
     * 销毁倒计时
     */
    private void destroyTime() {
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
    }
    /**
     * 释放并初始化
     */
    private void releaseAndInitMediaList(boolean isDes) {
        if (mediaPlayers != null) {
            for (int i = 0; i < mediaPlayers.size(); i++) {
                MediaPlayer mediaPlayer = mediaPlayers.get(i);
                if (null != mediaPlayer) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }
        }
        if (null != timers) {
            timers.cancel();
            timers = null;
        }
        if(!isDes){
            mediaPlayers = new ArrayList<>();
        }
    }

    /**
     * 销毁所有数据
     */
    public void destroyAll(){
        releaseAndInitMediaList(true);
        destroyTime();
        destroyPlayer();
    }
} 
