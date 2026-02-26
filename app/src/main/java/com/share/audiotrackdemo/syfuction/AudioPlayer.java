package com.share.audiotrackdemo.syfuction;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

/**
 * @Description: 24-bit PCM 音频播放器，支持动态声道配置和循环播放
 * @Author: pnz (Modified by AI Assistant)
 * @Version: 2.0
 */
public class AudioPlayer {
    private static final String TAG = "AudioPlayer";
    private static final int SAMPLE_RATE = 44100;
    private static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_24BIT_PACKED;

    private AudioTrack audioTrack;
    private byte[] audioData;
    private boolean isAuto = false;
    private boolean isPlaying = false;

    // 记录当前配置，以便重新加载数据时使用
    private int currentChannelConfig;
    private int currentFrameSizeInBytes;

    private Context context;

    /**
     * 构造函数
     *
     * @param context   上下文
     * @param audioData 原始音频数据 (24-bit PCM)
     * @param left      是否启用左声道
     * @param right     是否启用右声道
     */
    public AudioPlayer(Context context, byte[] audioData, boolean left, boolean right) {
        this.context = context.getApplicationContext(); // 使用 Application Context 防止泄漏
        this.audioData = audioData;

        initAudioTrack(left, right);
    }

    /**
     * 初始化 AudioTrack
     */
    private void initAudioTrack(boolean left, boolean right) {
        if (audioData == null || audioData.length == 0) {
            Log.e(TAG, "Audio data is empty");
            return;
        }

        // 1. 动态计算声道配置和每帧字节数
        int channelConfig;
        int frameSizeInBytes;

        // 只有当左右都 true 时才是立体声，否则视为单声道（或单侧输出）
        if (left && right) {
            channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
            frameSizeInBytes = 2 * 3; // 立体声: 2声道 * 3字节(24bit) = 6 字节/帧
        } else {
            channelConfig = AudioFormat.CHANNEL_OUT_MONO;
            frameSizeInBytes = 1 * 3; // 单声道: 1声道 * 3字节(24bit) = 3 字节/帧
        }

        this.currentChannelConfig = channelConfig;
        this.currentFrameSizeInBytes = frameSizeInBytes;

        // 2. 计算缓冲区大小
        int minBufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, channelConfig, AUDIO_ENCODING);
        // 对于 MODE_STATIC，缓冲区大小至少要是数据长度，且建议大于 minBufferSize
        int bufferSize = Math.max(audioData.length, minBufferSize);

        try {
            // 3. 创建 AudioTrack (MODE_STATIC 适合短音频循环)
            audioTrack = new AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    SAMPLE_RATE,
                    channelConfig,
                    AUDIO_ENCODING,
                    bufferSize,
                    AudioTrack.MODE_STATIC
            );

            // 4. 写入数据
            int written = audioTrack.write(audioData, 0, audioData.length);
            if (written != audioData.length) {
                Log.e(TAG, "Failed to write all audio data. Written: " + written + ", Expected: " + audioData.length);
            }

            // 5. 设置循环点 (单位：帧 Frame)
            // 总帧数 = 总字节数 / 每帧字节数
            int totalFrames = audioData.length / frameSizeInBytes;

            if (totalFrames > 0) {
                // 从第 0 帧 到 最后一帧，无限循环 (-1)
                audioTrack.setLoopPoints(0, totalFrames - 1, -1);
                Log.d(TAG, "Loop points set: 0 to " + (totalFrames - 1) + " frames");
            } else {
                Log.w(TAG, "Audio data too short to calculate frames.");
            }

            // 6. 初始设置声道音量
            setChannel(left, right);

        } catch (Exception e) {
            Log.e(TAG, "Error initializing AudioTrack", e);
            release(); // 初始化失败则释放资源
        }
    }

    /**
     * 设置左右声道音量平衡
     * 左声道为 true 时左耳有声，false 时静音；右声道同理
     *
     * @param left  是否开启左声道
     * @param right 是否开启右声道
     */
    public void setChannel(boolean left, boolean right) {
        if (audioTrack != null) {
            // setStereoVolume(leftGain, rightGain), 范围 0.0f - 1.0f
            float leftGain = left ? 1.0f : 0.0f;
            float rightGain = right ? 1.0f : 0.0f;

            // 即使配置的是 MONO，调用此方法通常也不会报错，但只在 STEREO 配置下生效明显
            // 如果是 MONO 配置且需要切换左右，理论上需要重新创建 AudioTrack 改变 channelConfig
            // 这里为了简单，假设用户主要是在 STEREO 模式下做平衡，或者 Mono 模式下两边都响
            if (currentChannelConfig == AudioFormat.CHANNEL_OUT_STEREO) {
                audioTrack.setStereoVolume(leftGain, rightGain);
            } else {
                // 如果是单声道配置，setStereoVolume 可能不起作用或行为依赖设备
                // 如果需要在单声道配置下彻底切换物理输出通道，建议重新调用 initAudioTrack
                Log.w(TAG, "Current config is MONO. setStereoVolume might not switch physical channels dynamically.");
            }
        }
    }

    /**
     * 重新加载音频数据 (可选功能)
     * 如果需要播放完全不同的音频片段，调用此方法
     */
    public void reloadAudioData(byte[] newData, boolean left, boolean right) {
        stop(); // 先停止并释放旧资源
        this.audioData = newData;
        initAudioTrack(left, right); // 重新初始化
    }

    /**
     * 写入数据 (针对 MODE_STATIC 模式，通常只在初始化时写入)
     * 如果需要在运行时动态追加数据，建议使用 MODE_STREAM 模式重构此类
     */
    public void write(byte[] newData) {
        if (audioTrack != null && audioTrack.getPlayState() == AudioTrack.PLAYSTATE_STOPPED) {
            // 在 STATIC 模式下，通常不建议反复 write，除非先 flush 或 reload
            // 这里仅作演示，实际 STATIC 模式数据已在构造函数写入
            Log.w(TAG, "Write called in STATIC mode. Data should be loaded at initialization.");
        }
    }

    /**
     * 开始播放
     */
    public void play() {
        if (audioTrack == null) {
            Log.e(TAG, "AudioTrack is not initialized");
            return;
        }

        if (!isPlaying) {
            try {
                audioTrack.play();
                isPlaying = true;
                isAuto = true;
                Log.d(TAG, "Playback started");
            } catch (IllegalStateException e) {
                Log.e(TAG, "Error starting playback", e);
            }
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (audioTrack != null && isPlaying) {
            audioTrack.pause();
            isPlaying = false;
            isAuto = false; // 暂停时标记为非自动播放状态，视业务需求而定
            Log.d(TAG, "Playback paused");
        }
    }

    /**
     * 停止播放
     */
    public void stop() {
        Log.d(TAG, "Stopping playback");
        if (audioTrack != null) {
            try {
                if (isPlaying || audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                    audioTrack.stop();
                }
                isPlaying = false;
                isAuto = false;
            } catch (IllegalStateException e) {
                Log.e(TAG, "Error stopping playback", e);
            }
        }
    }

    /**
     * 释放资源
     * 当不再使用该播放器时务必调用
     */
    public void release() {
        stop();
        if (audioTrack != null) {
            try {
                audioTrack.release();
            } catch (Exception e) {
                Log.e(TAG, "Error releasing AudioTrack", e);
            }
            audioTrack = null;
        }
        audioData = null;
        Log.d(TAG, "Resources released");
    }

    /**
     * 获取当前播放状态
     */
    public boolean isPlaying() {
        return isPlaying && audioTrack != null && audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING;
    }
}