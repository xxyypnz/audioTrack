package com.share.audiotrackdemo.syfuction;

import java.io.Serializable;
import java.util.List;

public class PlayBean implements Serializable{


    public int time;
    public List<DataBean> left;
    public List<DataBean> right;


    public static class DataBean implements Serializable {
        public int type;
        public ParaBean para;


        public static class ParaBean implements Serializable {
            public int freq;
            public int db;
        }
    }
}

