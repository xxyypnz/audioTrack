package com.share.audiotrackdemo.syfuction;

import android.content.Context;
import com.share.audiotrackdemo.AppAppliction;
import com.share.audiotrackdemo.syfuction.mixer.WavePart;
import com.share.audiotrackdemo.syfuction.wave.waveImpl.*;
import org.json.JSONObject;
import java.io.InputStream;

/**
 * 合成音处理“车间”
 */
public class SyntheticTrackProcessor {
    private Context context;

    public SyntheticTrackProcessor(Context context) {
        this.context = context;
    }

    // 对应原 getWavePart：包装成混合单元
    public WavePart getPart(int type, int durationMs, JSONObject para) throws Exception {
        Wave wave = createWaveObject(type, para); // 只有freq先根据频率生成波形
        wave.SetDurationMs(durationMs);
        return new WavePart(wave, para.getInt("db")); // 抽象的连续波形, 暂时不转化为double[]保持一致性
    }

    // 对应原 getWaveByte：直接获取单音字节数组
    public byte[] getByte(int type, JSONObject para, int durationMs) throws Exception {
        Wave wave = createWaveObject(type, para);
        wave.SetDurationMs(durationMs);
        return wave.GetWaveBuffer(para.getInt("db"));
    }

    // 对应原 getWave：工厂模式创建波形对象
    private Wave createWaveObject(int type, JSONObject para) throws Exception {
        InputStream sinWaveFitting = context.getAssets().open("sin_wave_fitting");
        switch (type) {
            case 0: return new SinWave(para.getInt("freq"), sinWaveFitting);
            case 1: return new ChirpWave(para.getInt("freq"), sinWaveFitting);
            case 2: return new TriangularWave(para.getInt("freq"), sinWaveFitting);
            case 3: return new SquareWave(para.getInt("freq"), sinWaveFitting);
            case 4: return new WhiteNoise(sinWaveFitting);
            case 5: return new NarrowBandNoise(para.getInt("freq"), (int) (para.getInt("freq") * 0.15), sinWaveFitting);
            case 6: return new AMWave(para.getInt("freq"), 3, 0.99, sinWaveFitting);
            case 7: return new AMWave2(para.getInt("freq"), 10, 0.99, sinWaveFitting);
            case 10: return new PinkNoise(sinWaveFitting);
            case 11: return new PulseWave(500, 250, new PinkNoise(sinWaveFitting));
            case 12: return new PulseWave(500, 250, new ChirpWave(para.getInt("freq"), sinWaveFitting));
            case 25: return new PulseWaveSin(500, 250, new SinWave(para.getInt("freq"), sinWaveFitting));
            default: return new TriangularWave(4000, sinWaveFitting);
        }
    }
}