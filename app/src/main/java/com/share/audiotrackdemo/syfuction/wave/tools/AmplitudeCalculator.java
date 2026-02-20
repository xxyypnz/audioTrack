package com.share.audiotrackdemo.syfuction.wave.tools;

import com.share.audiotrackdemo.syfuction.wave.exceptions.InvalidCalibrationFileFormat;
import com.share.audiotrackdemo.syfuction.wave.exceptions.UnsupportFrequency;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



class LogParam {
    private double a;
    private double b;

    public LogParam(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }
}

public class AmplitudeCalculator {
    private LogParam[] params;

    public AmplitudeCalculator(InputStream calibrationFile) throws InvalidCalibrationFileFormat {
        params = new LogParam[12001]; // 使用1到12000的索引，所以数组长度为12001

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(calibrationFile));
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    try {
                        int x = Integer.parseInt(parts[0]);
                        double y = Double.parseDouble(parts[1]);
                        double z = Double.parseDouble(parts[2]);

                        // 检查x的范围
                        if (x >= 20 && x <= 12000) {
                            params[x] = new LogParam(y, z);
                        } else {
                            throw new InvalidCalibrationFileFormat(lineNumber, line);
                        }
                    } catch (NumberFormatException e) {
                        throw new InvalidCalibrationFileFormat(lineNumber, line);
                    }
                } else {
                    throw new InvalidCalibrationFileFormat(lineNumber, line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    public int GetAmplitude(int freq, double db) throws UnsupportFrequency {
        if (freq < 20 || freq > 12000) {
            throw new UnsupportFrequency(freq);
        }
        
        LogParam param = params[freq];
        double a = param.getA();
        double b = param.getB();

        // 计算反函数值 x
        double y = db * 10;
        double x = Math.exp((y - b) / a);
        
        // 校验 x 的范围
        if (x < 1) {
            x = 1;
        } else if (x > 32767) {
            x = 32767;
        }
        
        return (int)Math.round(x);
    }
}
