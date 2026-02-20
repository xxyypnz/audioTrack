package com.share.audiotrackdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.serialport.SerialPort;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.share.audiotrackdemo.other1.AudioGenerator;
import com.share.audiotrackdemo.other1.EcgActivity;
import com.share.audiotrackdemo.other1.VoiceVolumnWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private PlayThread mPlayThread;

    Button btnPlay;
    Button btnLeft;
    Button btnRight;
    Button btnStop;
    TextView tvDq;
    Button btnAdd;
    Button btnSub;
    Button ylAdd;
    Button ylSub;
    TextView tvYl;
    Button tvSure;
    EditText edHz;
    TextView cuHz;
    Button tvDemo;
    VoiceVolumnWrapper _VoiceVolumnWrapper;
    private int xd=1500;
    private int hpb=50;
    TextView tvHpb;
    private int rightRate=1;//右边评率
    TextView tvRightRata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvRightRata=findViewById(R.id.tv_pl_right);
        tvHpb=findViewById(R.id.tv_hpb);
        tvDq=findViewById(R.id.tv_pl);
        btnAdd=findViewById(R.id.btn_add);
        btnSub=findViewById(R.id.btn_sub);
        btnPlay =  findViewById(R.id.btn_play);
        btnLeft =  findViewById(R.id.btn_left);
        btnRight =  findViewById(R.id.btn_right);
        btnStop =  findViewById(R.id.btn_stop);
        tvDemo=findViewById(R.id.btn_demo);
        ylAdd=findViewById(R.id.yl_add);
        ylSub=findViewById(R.id.yl_sub);
        tvYl=findViewById(R.id.tv_yl);
        cuHz=findViewById(R.id.tv_HZ);
        tvSure=findViewById(R.id.tv_sure);
        edHz=findViewById(R.id.et_hz);
        tvSure.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        ylSub.setOnClickListener(this);
        ylAdd.setOnClickListener(this);
        tvDemo.setOnClickListener(this);
        _VoiceVolumnWrapper = new VoiceVolumnWrapper(this);
        tvYl.setText("当前响度: "+xd);
        cuHz.setText("当前音赫兹: "+hz);
        SeekBar seekBarBHP = findViewById(R.id.seekBarhpb);
        seekBarBHP.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                hpb=progress;
                tvHpb.setText("混频比:left:"+hpb+"-right:"+(100-hpb));
                if (null != mPlayThread) {
                    mPlayThread.stopPlay();
                    mPlayThread = null;
                }
                playSound(true, true);
            }
        });
        SeekBar seekBarRight = findViewById(R.id.seekBarRight);
        seekBarRight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                rightRate=progress;
                tvRightRata.setText("Right-当前频率"+rightRate);
                if (null != mPlayThread) {
                    mPlayThread.stopPlay();
                    mPlayThread = null;
                }
                playSound(true, true);
            }
        });
        SeekBar seekBar = findViewById(R.id.seekBar2);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                rate=progress;
                tvDq.setText("left-当前频率:"+rate);
                if (null != mPlayThread) {
                    mPlayThread.stopPlay();
                    mPlayThread = null;
                }
                playSound(true, true);
            }
        });
        SeekBar seekBar2 = findViewById(R.id.seekBar3);
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                xd=progress;
                tvYl.setText("当前响度:"+xd);
                if (null != mPlayThread) {
                    mPlayThread.stopPlay();
                    mPlayThread = null;
                }
                playSound(true, true);
            }
        });
//        2m一次   播放 如果不要就关闭
//        tarment();
        // sokect udp 连接
//        initSokect();
//        读取文件
//        readAsset();
//        串口

        initSerial();
    }

    private void initSerial() {
        try {
//            SerialPort serialPort=new SerialPort(new File(""),115200);
            SerialPort serialPort = SerialPort //
                    .newBuilder("", 115200) // 串口地址地址，波特率
                    .parity(2) // 校验位；0:无校验位(NONE，默认)；1:奇校验位(ODD);2:偶校验位(EVEN)
                    .dataBits(7) // 数据位,默认8；可选值为5~8
                    .stopBits(2) // 停止位，默认1；1:1位停止位；2:2位停止位
                    .build();

            InputStream input = serialPort.getInputStream();
            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();
            String s = new String(buffer);
            JSONObject jsonObject = new JSONObject(s);
            int volume = jsonObject.getInt("volume");
            int leftRate = jsonObject.getInt("leftRate");
            int left = jsonObject.getInt("left");
            int rightRate1 = jsonObject.getInt("rightRate");
            int right = jsonObject.getInt("right");
            js.setText(s);
            if (null != mPlayThread) {
                mPlayThread.stopPlay();
                mPlayThread = null;
            }
            mPlayThread = new PlayThread(volume,leftRate,rightRate1,left,right);
            mPlayThread.setChannel(true, true);
            mPlayThread.start();
//           OutputStream out = serialPort.getOutputStream();
            // serialPort.tryClose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void readAsset() {
        AssetManager assetManager = getAssets();
        InputStream input = null;
        try {
            input = assetManager.open("demo.text");
            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();
            String s = new String(buffer);
            Log.e("--------",s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Button connBtn;
    TextView js;
    private void initSokect() {
        connBtn = findViewById(R.id.btn_lj);
        js = findViewById(R.id.tv_sd);
        connBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectSoket();
            }
        });
    }
    private Socket socket;
    private void connectSoket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("192.168.70.46", 12574);
                    senMsg("1");//连接成功
                    getMsgListener();//数据监听
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this,"异常了",Toast.LENGTH_LONG).show();
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void getMsgListener() {
        try {
        //接受服务端数据
        BufferedReader recv = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String recvMsg = null;
            recvMsg = recv.readLine();
        if (recvMsg != null) {
            js.setText(recvMsg);
            senMsg("2");
        } else {
            js.setText("Cannot receive data correctly.");
            senMsg("3");
        }
        } catch (IOException e) {
            Toast.makeText(MainActivity.this,"接收数据异常",Toast.LENGTH_LONG).show();
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param sendMesg  1 代表连接成功  2 代表接收数据成功
     */
    private void senMsg(String sendMesg) {
                //向服务器发送数据
        PrintWriter send = null;
        try {
            send = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8")));
            send.println(sendMesg);
            send.flush();
            send.close();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this,"发送数据失败",Toast.LENGTH_LONG).show();
            throw new RuntimeException(e);
        }
    }

    Timer timer = new Timer();
    private void tarment() {
        timer.schedule(task, 2000, 2000);
    }
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    if (rate==12000){
                        timer.cancel();
                        return;
                    }
                    rate++;
                    tvDq.setText("left-当前频率:"+rate);
                    if (null != mPlayThread) {
                        mPlayThread.stopPlay();
                        mPlayThread = null;
                    }
                    playSound(true, true);
                }
            });
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            if (null != mPlayThread) {
                mPlayThread.stopPlay();
                mPlayThread = null;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        _VoiceVolumnWrapper.unregisterVolumeReceiver();
        super.onDestroy();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sure:
                if (TextUtils.isEmpty(edHz.getText().toString().trim())){
                    Toast.makeText(this, "请输入赫兹", Toast.LENGTH_SHORT).show();
                    return;
                }
                hz= Integer.parseInt(edHz.getText().toString().trim());
                cuHz.setText("当前音赫兹: "+hz);
                if (null != mPlayThread) {
                    mPlayThread.stopPlay();
                    mPlayThread = null;
                }
                playSound(true, true);
                break;
            case R.id.btn_play:
                playSound(true, true);
                break;
            case R.id.btn_left:
                playSound(true, false);
                break;
            case R.id.btn_right:
                playSound(false, true);
                break;
            case R.id.btn_stop:
                if (null != mPlayThread) {
                    mPlayThread.stopPlay();
                    mPlayThread = null;
                }
                break;
            case R.id.btn_add:
                if (rate==12000){
                    Toast.makeText(this, "最大为12000", Toast.LENGTH_SHORT).show();
                    return;
                }
                rate++;
                tvDq.setText("left-当前频率:"+rate);
                if (null != mPlayThread) {
                    mPlayThread.stopPlay();
                    mPlayThread = null;
                }
                playSound(true, true);
                break;
            case R.id.btn_sub:
                if (rate==0){
                    Toast.makeText(this, "最小为0", Toast.LENGTH_SHORT).show();
                    return;
                }
                rate--;
                tvDq.setText("left-当前频率:"+rate);
                if (null != mPlayThread) {
                    mPlayThread.stopPlay();
                    mPlayThread = null;
                }
                playSound(true, true);
                break;
            case R.id.yl_add:
                if (xd==32767){
                    Toast.makeText(this, "最大为32767", Toast.LENGTH_SHORT).show();
                    return;
                }
                xd++;
                tvYl.setText("当前响度:"+xd);
                if (null != mPlayThread) {
                    mPlayThread.stopPlay();
                    mPlayThread = null;
                }
                playSound(true, true);
//                _VoiceVolumnWrapper.AddMusicVoiceVolumn(10);
//                tvYl.setText("当前响度:"+_VoiceVolumnWrapper.GetMusicVoiceCurrentValue());
                break;
            case R.id.yl_sub:
                if (xd==0){
                    Toast.makeText(this, "最小为0", Toast.LENGTH_SHORT).show();
                    return;
                }
                xd--;
                tvYl.setText("当前响度:"+xd);
                if (null != mPlayThread) {
                    mPlayThread.stopPlay();
                    mPlayThread = null;
                }
                playSound(true, true);
//                _VoiceVolumnWrapper.ReduceMusicVoiceVolumn(10);
//                tvYl.setText("当前响度:"+_VoiceVolumnWrapper.GetMusicVoiceCurrentValue());
                break;
            case R.id.btn_demo:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new AudioGenerator().generateAndPlayAudio();
                    }
                }).start();

                break;

        }
    }
    private int hz=44100;
    private int rate=1;
    private void playSound(boolean left, boolean right) {
        if (null != mPlayThread) {
            mPlayThread.stopPlay();
            mPlayThread = null;
        }
        mPlayThread = new PlayThread(xd,rate,rightRate,hpb,100-hpb);
        mPlayThread.setChannel(left, right);
        mPlayThread.start();
    }

    public void goEcg(View view) {
        startActivity(new Intent(MainActivity.this, EcgActivity.class));
    }
}