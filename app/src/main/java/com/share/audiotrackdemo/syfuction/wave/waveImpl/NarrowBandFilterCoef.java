package com.share.audiotrackdemo.syfuction.wave.waveImpl;

import android.content.Context;
import android.content.res.AssetManager;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class NarrowBandFilterCoef {

    // 单例实例
    private static NarrowBandFilterCoef instance;

    // 存储滤波器系数数据
    private Map<Integer, List<List<Double>>> filterData;

    // 私有构造函数，避免外部直接创建实例
    private NarrowBandFilterCoef(Map<Integer, List<List<Double>>> filterData) {
        this.filterData = filterData;
    }

    // 初始化单例的静态方法，传入 Context
    public static void initialize(Context context) {
        AssetManager assetManager = context.getAssets();

        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        // Configure the YAML parser with increased document size limit
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setMaxAliasesForCollections(1000); // Increase aliases limit if needed
        loaderOptions.setCodePointLimit(10_000_000); // Set a higher limit, e.g., 10MB (10,000,000 code points)

        Yaml yaml = new Yaml(loaderOptions);
        Map<Integer, List<List<Double>>> filterData = null;
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("filters.yaml");

            // Load YAML content as a Map of Integer to List<List<Double>>
            filterData = yaml.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        instance = new NarrowBandFilterCoef(filterData);
    }

    // 获取单例实例，调用前必须先调用 initialize()
    public static NarrowBandFilterCoef getInstance() {
        if (instance == null) {
            throw new IllegalStateException("FilterManager not initialized, call initialize() first.");
        }
        return instance;
    }

    // 获取滤波器系数
    public double[][] getCoefficient(int centerFreq) {
        // 检查中心频率对应的滤波器系数是否存在
        if (filterData.containsKey(centerFreq)) {
            List<List<Double>> coefficients = filterData.get(centerFreq);

            // 转换 List<List<Double>> 为 double[][]
            double[][] result = new double[coefficients.size()][];
            for (int i = 0; i < coefficients.size(); i++) {
                List<Double> row = coefficients.get(i);
                double[] rowArray = new double[row.size()];
                for (int j = 0; j < row.size(); j++) {
                    rowArray[j] = row.get(j);
                }
                result[i] = rowArray;
            }
            return result;
        } else {
            // 如果不存在，抛出异常
            throw new IllegalArgumentException("No filter coefficients found for the given frequency: " + centerFreq);
        }
    }
}

