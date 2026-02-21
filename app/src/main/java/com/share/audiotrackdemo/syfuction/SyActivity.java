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
public class SyActivity extends AppCompatActivity implements SerialListener{
    TextView viewById;
    SeekBar seekBar, seekBar2;
    TextView txt1, txt2;
    Button btn1, btn2, btn3, btn4, btn11, btn22, btn33, btn44, btnpl1, btnpl2, btnpl3, btnpl4, btnbf;
    private int vo = 50;
    private int pl = 440;

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(decript()){
                    initSerial();
                }else{
                    finish();
                }

            }
        }).start();
        findViewById(R.id.bt_zfc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PlayUtils playUtils=new PlayUtils(SyActivity.this);
//                playUtils.palynew("{\"time\":4," +
//                        "\"left\":[{\"type\":0, \"para\":{\"freq\":5540,\"db\":60}},{\"type\":2,\"para\":{\"freq\":5540,\"db\":55}}]," +
//                        "\"right\":[{\"type\":2, \"para\":{\"freq\":5540,\"db\":50}},{\"type\":3,\"para\":{\"freq\":5540,\"db\":70}}]" +
//                        "}");
                palynew("{\"time\":4," +
                        "\"left\":[{\"type\":0, \"para\":{\"freq\":5540,\"db\":60}},{\"type\":2,\"para\":{\"freq\":5540,\"db\":55}}]," +
                        "\"right\":[{\"type\":2, \"para\":{\"freq\":5540,\"db\":50}},{\"type\":3,\"para\":{\"freq\":5540,\"db\":70}}]" +
                        "}");
            }
        });
//        setData();
    }


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



    /**
     * 收到播放
     */
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

    String data = "";


    /**
     * 初始化串口
     */
   /* private void initSerial() {
        InputStream input;//收到串口信息
        input = null;
        try {
//            SerialPort serialPort=new SerialPort(new File(""),115200);
            SerialPort serialPort = SerialPort //
                    .newBuilder("/dev/ttyAS2", 115200) // 串口地址地址，波特率
//                    .parity(0) // 校验位；0:无校验位(NONE，默认)；1:奇校验位(ODD);2:偶校验位(EVEN)
//                    .dataBits(8) // 数据位,默认8；可选值为5~8
//                    .stopBits(1) // 停止位，默认1；1:1位停止位；2:2位停止位
                    .build();
            input = serialPort.getInputStream();
            while (true) {
                int size = input.available();
                byte[] buffer = new byte[size];
                input.read(buffer);
                final String s = new String(buffer);
                if (s.length() > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                Log.e("----------", s);
                                viewById.setText(viewById.getText().toString() + s + "\n------");
                                if(s.equals("stoptis")){
                                    releaseAndInitMediaList(false);
                                    destroyPlayer();
                                    destroyTime();
                                    return;
                                }
                                if (s.startsWith("$") && !s.endsWith("!")) {
                                    data = s;
                                } else if (s.startsWith("$") && s.endsWith("!")) {
                                    data = s;
                                    String substring = data.substring(1, data.length() - 1);
                                    palynew(substring);
                                } else {
                                    data += s;
                                    if (s.endsWith("!")) {
                                        String substring = data.substring(1, data.length() - 1);
                                        palynew(substring);
                                    }
                            }
                        }
                    });
                }
            }
            // serialPort.tryClose();
        } catch (Exception e) {
            Log.e("----------", e.getMessage());
            throw new RuntimeException(e);
        } finally {

        }
    }*/

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

    /**
     * 最新版本
     * {"time":4,
     * "left":[{"type":0, "para":{"freq":440,"db":60}},{"type":1,"para":{"freq":450,"db":65}}],
     * "right":[{"type":2, "para":{"freq":460,"db":70}},{"type":3,"para":{"freq":470,"db":80}}]
     * }
     * Type: 声音类型， 0: 纯音; 1: 转音: 2: 三角波:3: 方波:4:白噪音:5: 窄带噪音:6:调幅音-1;7: 调幅音-2; 8: 纯音-1;
     * 9: 纯音-2;10粉红噪音 11 脉冲粉红噪音 12脉冲转音
     *
     * @param s
     */
    // // pghpghpgh palynew 改这个地方，20250516,这个函数负责接收串口的数据，解析并执行相应动作
    private void palynew(String s) {
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

    }
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
    }


    private short[] bytesToShorts(byte[] bytes, int length) {
        short[] shorts = new short[length / 2];
        for (int i = 0; i < shorts.length; i++) {
            shorts[i] = (short) ((bytes[i * 2 + 1] << 8) | (bytes[i * 2] & 0xFF));
        }
        return shorts;
    }

    private byte[] shortsToBytes(short[] shorts) {
        byte[] bytes = new byte[shorts.length * 2];
        for (int i = 0; i < shorts.length; i++) {
            bytes[i * 2] = (byte) (shorts[i] & 0xFF);
            bytes[i * 2 + 1] = (byte) ((shorts[i] >> 8) & 0xFF);
        }
        return bytes;
    }
    private List<MediaPlayer> mediaPlayers;
    private CountDownTimer timers;

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


    ///////////////////////////////////////////////////////////// 获取自然音的byte数组，pghpghpgh
    ///////////////////////////////////////////////////////////// 获取自然音的byte数组，pghpghpgh
    ///////////////////////////////////////////////////////////// 获取自然音的byte数组，pghpghpgh

//    private byte[] cy_16bit_to_24bit(byte[] wav) {
//        int numSamples = wav.length / 2;
//        byte[] result = new byte[numSamples * 3];
//
//        for (int i = 0; i < numSamples; i++) {
//            int sample16bit = ((wav[i * 2 + 1] & 0xFF) << 8) | (wav[i * 2] & 0xFF);
//            int sample24bit = sample16bit << 8;
//            result[i * 3] = (byte) (sample24bit & 0xFF);
//            result[i * 3 + 1] = (byte) ((sample24bit >> 8) & 0xFF);
//            result[i * 3 + 2] = (byte) ((sample24bit >> 16) & 0xFF); // 最低有效字节
//        }
//
//        return result;
//    }
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
}

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
    }

    // 子函数：从 InputStream 中提取音频数据
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
    }

    // 子函数：根据 isLeft 的值提取数据
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
    }

    // 这是你的查表数组，db 和 gain 的对应关系
    public static double[][][] gainTables = {
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

    // 根据给定的 db 值查表获取对应的 gain 值
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
    }


    // 调整音量的函数
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
    }

    /**
     * 同时播放一个或者多个
     */
    private void playType8and9Lists(int type, long time,boolean isLeft,JSONObject para) {
        final MediaPlayer media = MediaPlayer.create(this, getResid(type,isLeft));
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
            media.setVolume(volume,volume);
//            media.setVolume(isLeft?volume:0.0f, isLeft?0.0f:volume); // 将左耳音量设置为 0，只播放右耳音频
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

    // pghpghpgh , 各种自然音读取
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

    /**
     * 获取Wave
     *
     * @param para
     * @param type
     * @return 添加需要修改
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
    }


    private AudioPlayer player;//播放器
    private boolean isPlay = true;//一直播放
    private CountDownTimer timer;//倒计时

    /**
     * @param wave 播放波形
     * @param time 播放时长
     */
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
     * 销毁播放器
     */
    private void destroyPlayer() {
        isPlay = false;
        if (null != player) {
            player.stop();
            player = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(serverThread != null){
            serverThread.stopServer();
        }
        releaseAndInitMediaList(true);
        destroyTime();
        destroyPlayer();
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
                palynew(json); // 调用原有的播放解析逻辑
            }
        });
    }

    @Override
    public void onSerialError(Exception e) {
        Log.e("SyActivity", "串口发生故障: " + e.getMessage());
    }
}
