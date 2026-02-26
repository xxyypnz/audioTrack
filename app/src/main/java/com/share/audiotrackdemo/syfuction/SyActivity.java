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

// ###
// 3.sin_wave_fitting数值怎么得到的
// 4.gainTables自动化求值
// 5.降噪的逻辑
// 6.所有种类的波
// 7.循环播放

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
                play2();
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
        // ### 分析结果: 修改后更佳
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
            android.util.Log.e("---decript---", "---failed---2026-02-25---");
            // Toast.makeText(this, "授权失败，即将退出", Toast.LENGTH_SHORT).show();
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

    // ### 分析结果: MAC确实是有效的
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
        Log.e("---decript---", "---success---2026-02-25---");
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

        if (naturalProcessor == null || syntheticProcessor == null) {
            Log.e("AudioServer", "收到指令，但处理器还没准备好！指令已丢弃: " + jsonStr);
            return;
        }

        // pnz on 2026-02-24
        // 也要记得加入打印
        Log.e("---pnz goPlayNew收到---", jsonStr);

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
    // ### 分析结果: 改为作为简单测试用
    private void play2() {
        int volume = vo;
        int frequency = pl;
        String mockJson = "{" +
                "\"time\":4," +
                "\"left\":[{\"type\":0, \"para\":{\"freq\":" + frequency + ",\"db\":" + volume + "}}]," +
                "\"right\":[]" +
                "}";

        Log.d("ManualTest", "点击了播放按钮，模拟指令: " + mockJson);
        goPlayNew(mockJson);
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

    private boolean isTypeBoolean(int type) {
        if( type == 8 || type == 9||(type >= 13&&type<=24)){
            return false;
        }
        return true;
    } //# 合成音为true

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
                    Log.e("---pnz 02-26---", millisUntilFinished + "---onTick");
                }

                @Override
                public void onFinish() {
                    Log.e("---pnz 02-26---", "---playAudio onFinish");
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
    // ### 分析结果: 删除函数playType8and9Lists, 统一使用playAudio进行播放

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