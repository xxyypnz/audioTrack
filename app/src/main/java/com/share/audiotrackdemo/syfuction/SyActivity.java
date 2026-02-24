package com.share.audiotrackdemo.syfuction;

import android.content.Context;
import android.content.res.Resources;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.serialport.SerialPort;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import android.content.res.Resources;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.share.audiotrackdemo.syfuction.wave.waveImpl.PulseWaveSin;
import com.share.audiotrackdemo.syfuction.wave.waveImpl.SinWave;
import com.share.audiotrackdemo.syfuction.wave.waveImpl.SquareWave;
import com.share.audiotrackdemo.syfuction.wave.waveImpl.TriangularWave;
import com.share.audiotrackdemo.syfuction.wave.waveImpl.Wave;
import com.share.audiotrackdemo.syfuction.wave.waveImpl.WhiteNoise;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// 20250904 pgh added for decript

/**
 * @Description: 描述
 * @Author: wangcheng
 * @CeateDate: 2023/10/16
 * @UpdateUser: 更新着
 * @UpdateData: 更新时间
 * @UpdateRemark: 更新说明
 * @Vsersion: 1.0
 */

// ### 1.paly2 2.onCreate的那些组件对应后端的什么 3.sin_wave_fitting数值怎么得到的 4.gainTables自动化求值

public class SyActivity extends AppCompatActivity implements SerialListener{
    TextView viewById;
    SeekBar seekBar, seekBar2;
    TextView txt1, txt2;
    Button btn1, btn2, btn3, btn4, btn11, btn22, btn33, btn44, btnpl1, btnpl2, btnpl3, btnpl4, btnbf;
    private int vo = 50;
    private int pl = 440;

    //region 设置初始化之后的回调
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sy);
        viewById = findViewById(R.id.txt);
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt_pl);
        seekBar = findViewById(R.id.seekBar2);
        btn1 = findViewById(R.id.btn_1);
        btn2 = findViewById(R.id.btn_2);
        btn3 = findViewById(R.id.btn_3);
        btn4 = findViewById(R.id.btn_4);
        btn11 = findViewById(R.id.btn_11);
        btn22 = findViewById(R.id.btn_22);
        btn33 = findViewById(R.id.btn_33);
        btn44 = findViewById(R.id.btn_44);

        btnpl1 = findViewById(R.id.btn_pl1);
        btnpl2 = findViewById(R.id.btn_pl2);
        btnpl3 = findViewById(R.id.btn_pl3);
        btnpl4 = findViewById(R.id.btn_pl4);
        btnbf = findViewById(R.id.bt_bf);
        seekBar2 = findViewById(R.id.seekBar3);

        btnpl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((pl + 1) <= 12000 && (pl + 1) >= 25) {
                    pl = pl + 1;
                    txt2.setText("当前频率" + pl);
                    seekBar2.setProgress(pl);
                }
            }
        });

        btnpl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((pl + 100) <= 12000 && (pl + 100) >= 25) {
                    pl = pl + 100;
                    txt2.setText("当前频率" + pl);
                    seekBar2.setProgress(pl);
                }
            }
        });
        btnpl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((pl - 1) <= 12000 && (pl - 1) >= 25) {
                    pl = pl - 1;
                    txt2.setText("当前频率" + pl);
                    seekBar2.setProgress(pl);
                }
            }
        });
        btnpl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((pl - 100) <= 12000 && (pl - 100) >= 25) {
                    pl = pl - 100;
                    txt2.setText("当前频率" + pl);
                    seekBar2.setProgress(pl);
                }
            }
        });
        btnbf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paly2();
            }
        });
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                pl = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((vo + 1) <= 120) {
                    vo = vo + 1;
                    seekBar.setProgress(vo);
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((vo + 2) <= 120) {
                    vo = vo + 2;
                    seekBar.setProgress(vo);
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((vo + 5) <= 120) {
                    vo = vo + 5;
                    seekBar.setProgress(vo);
                }
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((vo + 10) <= 120) {
                    vo = vo + 10;
                    seekBar.setProgress(vo);
                }
            }
        });
        btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((vo - 1) >= 0) {
                    vo = vo - 1;
                    seekBar.setProgress(vo);
                }
            }
        });
        btn33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((vo - 5) >= 0) {
                    vo = vo - 5;
                    seekBar.setProgress(vo);
                }
            }
        });
        btn22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((vo - 2) >= 0) {
                    vo = vo - 2;
                    seekBar.setProgress(vo);
                }
            }
        });
        btn44.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((vo - 10) >= 0) {
                    vo = vo - 10;
                    seekBar.setProgress(vo);
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                vo = i;
                txt1.setText("响度:" + vo);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // pnz on 2026-02-22
        // 某一个直接发送全部命令字符串的简便入口
        findViewById(R.id.bt_zfc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPlayNew("{\"time\":4," +
                        "\"left\":[{\"type\":0, \"para\":{\"freq\":5540,\"db\":60}},{\"type\":2,\"para\":{\"freq\":5540,\"db\":55}}]," +
                        "\"right\":[{\"type\":2, \"para\":{\"freq\":5540,\"db\":50}},{\"type\":3,\"para\":{\"freq\":5540,\"db\":70}}]" +
                        "}");
            }
        });

        // pnz on 2026-02-22
        // 按照自然音和合成音的分类标准, 新建两个类
        // 放到onCreate初始化
        syntheticProcessor = new SyntheticTrackProcessor(this);
        naturalProcessor = new NaturalTrackProcessor(this);

        // pnz on 2026-02-22
        // onCreate里面new线程
        // 线程隐式包含Activity的引用
        // 退出时线程可能没有释放(因为原来的逻辑是initSerial不退出)
/*        new Thread(new Runnable() {
            @Override
            public void run() {
                if(decript()){
                    initSerial();
                }else{
                    finish();
                }

            }
        }).start();*/
        if(!decript()){
            Toast.makeText(this, "授权失败，即将退出", Toast.LENGTH_SHORT).show();
            finish();
        }
        else initSerial();
    }

    // pnz added since 2026-02-21
    // 新开串口线程逻辑下的统一销毁策略
    @Override
    protected void onDestroy() {
        if(serverThread != null){
            serverThread.stopServer();
            serverThread = null;
        }
        releaseAndInitMediaList(true);
        destroyTime();
        destroyPlayer();
        super.onDestroy();
    }

    // pnz on 2026-02-21
    // 重写接口SerialListener
    @Override
    public void onCommandReceived(final String json) {
        // 必须在 UI 线程更新界面和触发播放
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewById.setText("收到指令: " + json);
/*                json串发送方能够在界面上加入接收逻辑即可调用
                serverThread.sendData(json);*/
                goPlayNew(json); // 调用原有的播放解析逻辑
            }
        });
    }

    @Override
    public void onStopSignalReceived(){
        releaseAndInitMediaList(false);
        destroyPlayer();
        destroyTime();
    }

    @Override
    public void onSerialError(Exception e) {
        Log.e("SyActivity", "串口发生故障: " + e.getMessage());
    }

    //endregion 设置初始化之后的回调

    //region 在onCreate中设定的主线程先进行硬件授权校验, 决定是否开启serial监听线程

    private boolean decript() {
        // 请将下面的MAC地址替换为您设备实际的以太网MAC地址（获取后替换）
        String expectedMac = "8E:CB:A4:DD:12:91"; // 例如 "0A:1B:2C:3D:4E:5F"
        //String expectedMac = "8E:CB:A4:DD:12:90";

        String actualMac = getEthernetMacAddress();

        // 打印获取到的MAC地址，方便您拷贝
        //android.util.Log.d("MAC_ADDRESS", "获取到的以太网MAC地址: " + actualMac);
        //System.out.println("获取到的以太网MAC地址: " + actualMac);

        // 进行匹配判断
        if (expectedMac.equals(actualMac)) {
            return true;
        }
        return false;
    }

    /**
     * 通过读取系统文件尝试获取以太网MAC地址
     * @return 成功返回MAC地址字符串（如 "0A:1B:2C:3D:4E:5F"），失败返回null
     */
    private String getEthernetMacAddress() {
        // 常见的以太网接口名称，请根据实际设备调整，如 eth0, eth1
        String[] possibleEthInterfaces = {"eth0", "eth1"};
        BufferedReader reader = null;

        for (String ethInterface : possibleEthInterfaces) {
            String filePath = "/sys/class/net/" + ethInterface + "/address";
            try {
                reader = new BufferedReader(new FileReader(filePath));
                String line = reader.readLine();
                if (line != null && !line.trim().isEmpty()) {
                    // 通常文件内容就是MAC地址字符串，去除首尾空格
                    return line.trim().toUpperCase(); // 统一转为大写便于比较
                }
            } catch (Exception e) {
                // 文件不存在或读取错误是常见的，继续尝试下一个接口
                //android.util.Log.d("MAC_ADDRESS", "尝试接口 " + ethInterface + " 失败: " + e.getMessage());
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //android.util.Log.e("MAC_ADDRESS", "所有尝试的以太网接口均未成功获取MAC地址");
        return null;
    }

    private SerialServerThread serverThread;
    private void initSerial(){
        try{
            SerialPort serialPort = SerialPort.newBuilder("/dev/ttyAS2", 115200).build();
            serverThread = new SerialServerThread(serialPort, this);
            serverThread.start();
        }catch(Exception e){
            Log.e("Serial", "打开串口失败: " + e.getMessage());
        }
    }

    //endregion 在onCreate中设定的主线程先进行硬件授权校验, 决定是否开启serial监听线程

    //region 尝试将左右耳播放逻辑统一化
    // pnz on 2026-02-22
    private SyntheticTrackProcessor syntheticProcessor;
    private NaturalTrackProcessor naturalProcessor;

    // 将palynew这个最顶层的逻辑函数替换成goPlayNew
    private void goPlayNew(String jsonStr) {
        // 1. 初始化清场
        releaseAndInitMediaList(false);
        destroyPlayer();
        destroyTime();

        try {
            JSONObject json = new JSONObject(jsonStr);
            long time = json.getLong("time");

            // 2. 分别获取左、右声道的缓冲区
            byte[] leftChannelBuffer = getChannelData(json.optJSONArray("left"), time, true);
            byte[] rightChannelBuffer = getChannelData(json.optJSONArray("right"), time, false);

            // 3. 立体声合成 (Interleave)
            int totalSamples = Math.max(leftChannelBuffer.length, rightChannelBuffer.length) / 3;
            byte[] stereoData = new byte[totalSamples * 6];

            for (int i = 0; i < totalSamples; i++) {
                // 填充左声道 (3字节)
                if (i * 3 + 2 < leftChannelBuffer.length) {
                    System.arraycopy(leftChannelBuffer, i * 3, stereoData, i * 6, 3);
                }
                // 填充右声道 (3字节)
                if (i * 3 + 2 < rightChannelBuffer.length) {
                    System.arraycopy(rightChannelBuffer, i * 3, stereoData, i * 6 + 3, 3);
                }
            }

            // 4. 播放
            playAudio(stereoData, time, leftChannelBuffer.length > 0, rightChannelBuffer.length > 0);

        } catch (Exception e) {
            Log.e("AudioServer", "播放逻辑崩溃", e);
        }
    }

    /**
     * 内部核心逻辑：判断左右耳 -> 分别获取 -> 混合
     * 这是一个通用的单声道构建器
     */
    private byte[] getChannelData(JSONArray audioSources, long time, boolean isLeft) throws Exception {
        if (audioSources == null || audioSources.length() == 0) return new byte[0];

        List<WavePart> parts = new ArrayList<>(); // 存放合成音单元
        List<byte[]> naturalList = new ArrayList<>(); // 存放自然音字节流

        for (int i = 0; i < audioSources.length(); i++) {
            JSONObject item = audioSources.getJSONObject(i);
            int type = item.getInt("type");
            JSONObject para = item.getJSONObject("para");

            if (isTypeBoolean(type)) {
                // 合成音车间加工
                parts.add(syntheticProcessor.getPart(type, (int)(time * 1000), para));
            } else {
                // 自然音车间加工
                // pnz on 2026-02-24 直接使用readWaveFile
                // naturalList.add(naturalProcessor.processNaturalAudio(type, (int)(time * 1000), para, isLeft));
                naturalList.add(naturalProcessor.readWaveFile(type, (int)(time * 1000), para, isLeft));
            }
        }

        // 开始混合
        byte[] syntheticResult = new byte[0];
        if (parts.size() == 1) {
            // pnz on 2026-02-23
            // 首先Wave抽象类调用GetWaveBuffer(最终应该得到byte[])
            // GetWaveBuffer函数内部会再调用各个波形自身的GetRawWaveBuffer得到double[]
            // 而GetRawWaveBuffer又会调用GetAmplitude, 根据volume产生振幅的真正一步
            // 关键函数: amp = GetAmplitude(freq, LoudnessCalibrationUtil.getDb(type, freq, volume))
            // 公式如下: db = a * ln(amp) + b 因此 amp = exp((db - b) /a)
            syntheticResult = parts.get(0).Wave().GetWaveBuffer(parts.get(0).DB());
        } else if (parts.size() > 1) {
            WaveMixer mixer = new WaveMixer(parts.toArray(new WavePart[0]));
            mixer.SetDurationMs((int) (time * 1000));
            syntheticResult = mixer.GetWaveBuffer();
        }

        if (naturalList.size() > 0) {
            if (syntheticResult.length > 0) naturalList.add(syntheticResult);
            return mixAudioArrays(naturalList); // 调用原有的多路 PCM 混合函数
        } else {
            return syntheticResult;
        }
    }

    //endregion 尝试将左右耳播放逻辑统一化

    /**
     * 收到播放
     */
    // ###
    private void paly2() {
        int volume = vo;
        int leftRate = pl;
        int rightRate1 = 0;
        long time = 4;//播放时长
        try {
            /* 分贝校准文件 */
//            File calibrationFile = new File("file:///android_asset/sin_wave_fitting");
//
//            byte[] wave; /* 最终波形 */
//            IWave sinLeft;
//            IWave sinRight;
//            if (rightRate1 == 0) {//纯音
//                sinLeft = new SinWave(leftRate/* 频率 */, calibrationFile);
//                wave = sinLeft.GetWaveBuffer(volume);
//            } else {//混音
//                sinLeft = new SinWave(leftRate/* 频率 */, calibrationFile);
//                sinRight = new SinWave(rightRate1/* 频率 */, calibrationFile);
//                WavePart parts[] = new WavePart[2];
//                parts[0] = new WavePart(sinLeft, volume);
//                parts[1] = new WavePart(sinRight, volume);
//                WaveMixer mixer = new WaveMixer(parts);
//                wave = mixer.GetWaveBuffer();
//            }
//            goPlayAudio(wave, time);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 最新版本
     * {"time":4,
     * "left":[{"type":0, "para":{"freq":440,"db":60}},{"type":1,"para":{"freq":450,"db":65}}],
     * "right":[{"type":2, "para":{"freq":460,"db":70}},{"type":3,"para":{"freq":470,"db":80}}]
     * }
     * Type: 声音类型， 0: 纯音; 1: 转音: 2: 三角波:3: 方波:4:白噪音:5: 窄带噪音:6:调幅音-1;7: 调幅音-2; 8: 纯音-1;
     * 9: 纯音-2;10粉红噪音 11 脉冲粉红噪音 12脉冲转音
     *
     */

    /*private void palynew(String s) {
        releaseAndInitMediaList(false);
        Log.e("收到---", s);
        JSONObject jsonObject = null;
        try {

            byte[] waveLeft; *//* 最终波形 左*//*
            byte[] waveRight; *//* 最终波形 右*//*
            boolean isLeft = false;//是否有左
            boolean isRight = false;//是否有右
            jsonObject = new JSONObject(s);
            long time = jsonObject.getLong("time");//播放时间
            JSONArray right = jsonObject.getJSONArray("right");//右
            if (right.length() != 0) {
                //不是空数组
                isRight = true;

                // pghpghpgh 20250404,处理右声道
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
                        waveRight =readWavFile(type, time,false,para);
//                        playType8and9Lists(type, time,false,para);
//                        return;
                    }
                } else {
                    List<WavePart> parts=new ArrayList<>();
                    List<byte[]> listRight=new ArrayList<>();
//                    WavePart parts[] = new WavePart[right.length()];

                    // pghpghpgh 20250404,处理左声道
                    for (int i = 0; i < right.length(); i++) {
                        JSONObject rightd = right.getJSONObject(i);
                        int type = rightd.getInt("type");//声音类型
                        JSONObject para = rightd.getJSONObject("para");


                        if (isTypeBoolean(type)) {  // pghpghpgh 20250404,判断是合成音还是自然音
                            Wave freq = getWave(para, type);
                            freq.SetDurationMs((int) (time*1000));
                            if (freq == null) {
                                return;
                            }

                            // pghpghpgh 20250404,合成音
                            parts.add( getWavePart(freq, para, type)) ;
                        }else{

                            // pghpghpgh 20250404,自然音
                            listRight.add(readWavFile(type, time,true,para));
//                            playType8and9Lists(type, time,false,para);
                        }
                    }


                    if(parts.size()==0&&listRight.size()==0){
                        return;
                    }
                    byte[] le=new byte[0];
                    if (parts.size()==1){
                        le = parts.get(0).Wave().GetWaveBuffer(parts.get(0).DB());
                    }else if (parts.size()>1){
                        WavePart[] wavePartArray = parts.toArray(new WavePart[parts.size()]);
                        WaveMixer mixer = new WaveMixer(wavePartArray);
                        mixer.SetDurationMs((int) (time*1000));
                        le = mixer.GetWaveBuffer();
                    }
                    if (parts.size()>0&&listRight.size()>0){
                        listRight.add(le);
                        waveRight=mixAudioArrays(listRight);
                    }else if (parts.size()==0&&listRight.size()>0){
                        waveRight=mixAudioArrays(listRight);
                    }else{
                        waveRight=le;
                    }
                }
            } else {
                waveRight = new byte[0];
            }
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
                        waveLeft =readWavFile(type, time,true,para);
//                        playType8and9Lists(type, time,true,para);
//                        return;length=
                    }
                } else {
                    List<WavePart> parts=new ArrayList<>();
                    List<byte[]> listLeft=new ArrayList<>();
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
                            listLeft.add(readWavFile(type, time,true,para));
//                            playType8and9Lists(type, time,true,para);
                        }
                    }
                    if(parts.size()==0&&listLeft.size()==0){
                        return;
                    }
                    byte[] le=new byte[0];
                    if (parts.size()==1){
                        le = parts.get(0).Wave().GetWaveBuffer(parts.get(0).DB());
                    }else if (parts.size()>1){
                        WavePart[] wavePartArray = parts.toArray(new WavePart[parts.size()]);
                        WaveMixer mixer = new WaveMixer(wavePartArray);
                        mixer.SetDurationMs((int) (time*1000));
                        le = mixer.GetWaveBuffer();
                    }
                    if (parts.size()>0&&listLeft.size()>0){
                        listLeft.add(le);
                        waveLeft=mixAudioArrays(listLeft);
                    }else if (parts.size()==0&&listLeft.size()>0){
                        waveLeft=mixAudioArrays(listLeft);
                    }else{
                        waveLeft=le;
                    }


                }
            } else {
                waveLeft = new byte[0];
            }

            // pghpghpgh 20250404,左右声音混音
            if (isLeft && isRight) {//两种都有
                int minSize = Math.min(waveLeft.length, waveRight.length)/3;
                byte[] stereoData = new byte[minSize * 6]; // 存储交错的立体声数据
                // 将左右声道的波形交错存储
                for (int i = 0; i < minSize; i++) {
                    stereoData[i * 6] = waveLeft[i * 3];
                    stereoData[i * 6 + 1] = waveLeft[i * 3 + 1];
                    stereoData[i * 6 + 2] = waveLeft[i * 3 + 2];
                    stereoData[i * 6 + 3] = waveRight[i * 3];
                    stereoData[i * 6 + 4] = waveRight[i * 3 + 1];
                    stereoData[i * 6 + 5] = waveRight[i * 3 + 2];
                }
                // pghpghpgh 20250510,播放声音
                goPlayAudioNew(stereoData, time, true, true);
            } else if (isLeft && !isRight) {//只有左
                int minSize = waveLeft.length / 3; // 单声道样本数，原始数据是字节数，除以 2 得到样本数
                byte[] stereoData = new byte[minSize * 6]; // 每个立体声样本占用 4 个字节
                // 将左右声道的波形交错存储
                for (int i = 0; i < minSize; i++) {
                    stereoData[i * 6] = waveLeft[i * 3];
                    stereoData[i * 6 + 1] = waveLeft[i * 3 + 1];
                    stereoData[i * 6 + 2] = waveLeft[i * 3 + 2];
                    stereoData[i * 6 + 3] = 0;
                    stereoData[i * 6 + 4] = 0;
                    stereoData[i * 6 + 5] = 0;
                }
                // pghpghpgh 20250404,播放声音
                goPlayAudioNew(stereoData, time, true, false);
                //goPlayAudioNew(waveLeft, time, true, false);
            } else if (!isLeft && isRight) {//只有右
                int minSize = waveRight.length/3;
                byte[] stereoData = new byte[minSize * 6]; // 存储交错的立体声数据
                // 将左右声道的波形交错存储
                for (int i = 0; i < minSize; i++) {
                    stereoData[i * 6] = 0;
                    stereoData[i * 6 + 1] = 0;
                    stereoData[i * 6 + 2] = 0;
                    stereoData[i * 6 + 3] = waveRight[i * 3];
                    stereoData[i * 6 + 4] = waveRight[i * 3 + 1];
                    stereoData[i * 6 + 5] = waveRight[i * 3 + 2];
                }
                // pghpghpgh 20250404,播放声音
                goPlayAudioNew(stereoData, time, false, true);
            }
        } catch (JSONException e) {
            Log.e("-------", "解析异常");
//            throw new RuntimeException(e);
        } catch (Exception e) {
            Log.e("-------", "生产异常" + e.getMessage());
            throw new RuntimeException(e);
        }

    } //# palynew 最大的入口*/

    private void goPlayAudioNew(byte[] wave, long time, boolean left, boolean right) {
        destroyPlayer();
        destroyTime();
        isPlay = true;
        player = new AudioPlayer(this, wave, left, right);
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
    } //# palynew的辅助函数

    // pnz on 2026-02-24
    // mixAudioArrays应该从原来的16bit版本改成24bit版本
    // 数据容器从short变成int
    // 因为合成音和自然音都保证是 采样点 对应 3字节 的结构
    private byte[] mixAudioArrays(List<byte[]> audioDataList) {
        if (audioDataList == null || audioDataList.isEmpty()) {
            Log.e("MainActivity", "音频数据列表为空");
            return new byte[0];
        }

        int minLength = Integer.MAX_VALUE;
        for (byte[] audioData : audioDataList) {
            if (audioData == null) {
                Log.e("MainActivity", "音频数据为空");
                return new byte[0];
            }
            // 校验：24位音频必须是 3 的倍数
            if (audioData.length % 3 != 0) {
                Log.e("MainActivity", "音频数据长度不是3的倍数(非24bit对齐)");
                return new byte[0];
            }
            if (audioData.length < minLength) {
                minLength = audioData.length;
            }
        }

        // 确保 minLength 是 3 的倍数（对齐采样点）
        minLength = (minLength / 3) * 3;

        // 使用 int[][] 承载 24-bit 的数据（short存不下）
        int[][] intAudioDataArray = new int[audioDataList.size()][minLength / 3];
        for (int i = 0; i < audioDataList.size(); i++) {
            intAudioDataArray[i] = bytesToInts24Bit(audioDataList.get(i), minLength);
        }

        int[] mixedIntAudioData = new int[minLength / 3];
        for (int i = 0; i < minLength / 3; i++) {
            long sum = 0; // 使用 long 防止累加时溢出
            for (int j = 0; j < intAudioDataArray.length; j++) {
                sum += intAudioDataArray[j][i];
            }

            // 归一化处理（平均混合），并防止 24-bit 边界溢出
            // 24-bit 范围: -8388608 到 8388607
            int avg = (int) (sum / intAudioDataArray.length);
            mixedIntAudioData[i] = Math.min(Math.max(avg, -8388608), 8388607);
        }

        return intsToBytes24Bit(mixedIntAudioData);
    }

    /**
     * 辅助函数：将 24-bit 字节数组转为有符号整数数组 (Little-Endian)
     */
    private int[] bytesToInts24Bit(byte[] bytes, int length) {
        int[] ints = new int[length / 3];
        for (int i = 0; i < ints.length; i++) {
            int base = i * 3;
            // 拼凑 24 位值：低位在前，高位在后
            int value = (bytes[base] & 0xFF) |
                    ((bytes[base + 1] & 0xFF) << 8) |
                    (bytes[base + 2] << 16); // 第3字节包含符号位
            ints[i] = value;
        }
        return ints;
    }

    /**
     * 辅助函数：将整数数组转回 24-bit 字节数组 (Little-Endian)
     */
    private byte[] intsToBytes24Bit(int[] ints) {
        byte[] bytes = new byte[ints.length * 3];
        for (int i = 0; i < ints.length; i++) {
            int base = i * 3;
            int v = ints[i];
            bytes[base] = (byte) (v & 0xFF);         // 低字节
            bytes[base + 1] = (byte) ((v >> 8) & 0xFF);  // 中字节
            bytes[base + 2] = (byte) ((v >> 16) & 0xFF); // 高字节
        }
        return bytes;
    }

/*    private byte[] mixAudioArrays(List<byte[]> audioDataList) {
        if (audioDataList == null || audioDataList.isEmpty()) {
            Log.e("MainActivity", "音频数据列表为空");
            return new byte[0];
        }

        int minLength = Integer.MAX_VALUE;
        for (byte[] audioData : audioDataList) {
            if (audioData == null) {
                Log.e("MainActivity", "音频数据为空");
                return new byte[0];
            }
            if (audioData.length % 2 != 0) {
                Log.e("MainActivity", "音频数据长度不是偶数");
                return new byte[0];
            }
            if (audioData.length < minLength) {
                minLength = audioData.length;
            }
        }

        // 确保 minLength 是偶数
        minLength = minLength / 2 * 2;

        short[][] shortAudioDataArray = new short[audioDataList.size()][minLength / 2];
        for (int i = 0; i < audioDataList.size(); i++) {
            shortAudioDataArray[i] = bytesToShorts(audioDataList.get(i), minLength);
        }

        short[] mixedShortAudioData = new short[minLength / 2];
        for (int i = 0; i < minLength / 2; i++) {
            int sum = 0;
            for (int j = 0; j < shortAudioDataArray.length; j++) {
                sum += shortAudioDataArray[j][i];
            }
            // 归一化处理，防止溢出
            mixedShortAudioData[i] = (short) Math.min(Math.max(sum / shortAudioDataArray.length, Short.MIN_VALUE), Short.MAX_VALUE);
        }

        return shortsToBytes(mixedShortAudioData);
    } //# 最后的混合

    private short[] bytesToShorts(byte[] bytes, int length) {
        short[] shorts = new short[length / 2];
        for (int i = 0; i < shorts.length; i++) {
            shorts[i] = (short) ((bytes[i * 2 + 1] << 8) | (bytes[i * 2] & 0xFF));
        }
        return shorts;
    } //# 最后的混合

    private byte[] shortsToBytes(short[] shorts) {
        byte[] bytes = new byte[shorts.length * 2];
        for (int i = 0; i < shorts.length; i++) {
            bytes[i * 2] = (byte) (shorts[i] & 0xFF);
            bytes[i * 2 + 1] = (byte) ((shorts[i] >> 8) & 0xFF);
        }
        return bytes;
    } //# 最后的混合

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

    private byte[] readWavFile(int type, long time, boolean isLeft, JSONObject para) {
        // 获取 InputStream
        InputStream fis = getResources().openRawResource(getResid(type, isLeft));

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
    } //# 自然音 功能函数*/

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
    } //# 合成音调度中心
    private boolean isTypeBoolean(int type) {
        if( type == 8 || type == 9||(type >= 13&&type<=24)){
            return false;
        }
        return true;
    } //# 合成音为true

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
    } //# 合成音 合成音调度中心

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
                    wave = new TriangularWave(para.getInt("freq")/* 频率 */, sinWaveFitting);
                    break;
                case 3://3: 方波
                    wave = new SquareWave(para.getInt("freq")/* 频率 */, sinWaveFitting);
                    break;
                case 1://转音
                    wave = new ChirpWave(para.getInt("freq")/* 频率 */,sinWaveFitting);
                    break;
                case 4://白噪声
                    wave = new WhiteNoise(sinWaveFitting);
                    break;
                case 5://窄带噪音
                    //int v = (int) (para.getInt("freq") * 1.19 - para.getInt("freq") * 0.84);
                    //int v = (int) (para.getInt("freq") * 0.55);
                    int v = (int) (para.getInt("freq") * 0.15);
                    //int v = (int) (para.getInt("freq"));
                    //int v = (int) (1000 - 125);
                    //int v = 780;

                    wave = new NarrowBandNoise(para.getInt("freq")/* 频率 */,v , sinWaveFitting);
                    //wave = new NarrowBandNoise(para.getInt("freq"));
                    break;
//                case 6://调幅音-1
//                    wave = new AMWave(para.getInt("freq")/* 频率 */, 7,1, sinWaveFitting);
//                    break;
//                case 7://调幅音-2
//                    wave = new AMWave(para.getInt("freq")/* 频率 */, 20,1, sinWaveFitting);
//                    break;
                case 6://调幅音-1
                    wave = new AMWave(para.getInt("freq")/* 频率 */, 3,0.99, sinWaveFitting);
                    break;
                case 7://调幅音-2
                    wave = new AMWave2(para.getInt("freq")/* 频率 */, 10,0.99, sinWaveFitting);
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
                    wave = new PulseWaveSin(500,250/* 间隔 */,wave3);
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
    } //# getWave 合成音功能函数

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

    //region 将两种播放统一放在此处
/*    pnz added since 2026-02-21
    和audioplayer(非wav)相关的函数是 goPlayAudioNew
    和mediaplayer(wav)相关的函数是 playType8and9Lists
    均已弃用改成playAudio*/

    // 这一组是纯音,方波,噪音...
    // audioplayer - timer

    private AudioPlayer player;
    private boolean isPlay = true;
    private CountDownTimer timer;

    private void playAudio(byte[] wave, long time, boolean left, boolean right) {
        destroyPlayer();
        destroyTime();
        isPlay = true;
        player = new AudioPlayer(this, wave, left, right);
        player.play();
        if (timer == null) {
            timer = new CountDownTimer(time * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.e("---pnz 02-23---", millisUntilFinished + "");
                }

                @Override
                public void onFinish() {
                    Log.e("---pnz 02-23---", "结束");
                    player.stop();
                }
            };
        }
        timer.start();
    }

    private void destroyTime() {
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
    }

    private void destroyPlayer() {
        isPlay = false;
        if (null != player) {
            player.stop();
            player = null;
        }
    }

    // 这一组是wav
    // mediaplayer - timers

    // pnz added since 2026-02-21
    // 这个函数的意义在于系统原生的 MediaPlayer 只能实现“播放文件”，
    // 它并没有一个方法叫 playForSeconds(5)
    // 所以需要定时器和json解析出的time来控制
    // 暂时弃用, 需要判断是否启用###
    /*private void playType8and9Lists(int type, long time,boolean isLeft,JSONObject para) {
        final MediaPlayer media = MediaPlayer.create(this, getResid(type,isLeft));
        media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {//控制循环播放
                if (mp != null) {
                    mp.start();
                }
            }
        });

        try {
            int db=60; // test default val
            if(para.has("db")){
                db=para.getInt("db");
            }
            float volume = (1.0f/ 120) * db;
            media.setVolume(volume,volume);
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
    }*/

    private List<MediaPlayer> mediaPlayers;
    private CountDownTimer timers;

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
}
    //endregion 将两种播放统一放在此处