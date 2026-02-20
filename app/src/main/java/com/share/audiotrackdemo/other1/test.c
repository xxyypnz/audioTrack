#include <stdio.h>
#include <math.h>

#define SAMPLE_RATE 44100 // 采样率
#define AMPLITUDE 32767 // 最大振幅

// 计算频率对应的角频率
double calculateAngularFrequency(double frequency) {
    return 2 * M_PI * frequency;
}

// 计算振幅对应的音量
short calculateAmplitude(double volume) {
    return (short)(volume * AMPLITUDE);
}

// 生成音频数据
void generateAudioData(double frequency, double volume, short* buffer, int numSamples) {
    double angularFrequency = calculateAngularFrequency(frequency);
    short amplitude = calculateAmplitude(volume);

    for (int i = 0; i < numSamples; i++) {
        double time = (double)i / SAMPLE_RATE;
        double sample = sin(angularFrequency * time);
        buffer[i] = (short)(sample * amplitude);
    }
}

int main() {
    int numSamples = SAMPLE_RATE; // 1秒钟的采样数
    short audioBuffer[numSamples];

    double minFrequency = 25.0; // 最小频率
    double maxFrequency = 12000.0; // 最大频率
    double minVolume = 0.0; // 最小音量
    double maxVolume = 120.0; // 最大音量

    double frequencyStep = (maxFrequency - minFrequency) / numSamples;
    double volumeStep = (maxVolume - minVolume) / numSamples;

    for (int i = 0; i < numSamples; i++) {
        double frequency = minFrequency + i * frequencyStep;
        double volume = minVolume + i * volumeStep;

        generateAudioData(frequency, volume, audioBuffer, numSamples);

        // 在这里可以将音频数据写入文件或进行播放操作
        // ...
    }

    return 0;
}