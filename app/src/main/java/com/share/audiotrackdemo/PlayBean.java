package com.share.audiotrackdemo;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 描述
 * @Author: wangcheng
 * @CeateDate: 2023/12/9
 * @UpdateUser: 更新着
 * @UpdateData: 更新时间
 * @UpdateRemark: 更新说明
 * @Vsersion: 1.0
 */

public class PlayBean implements Serializable {

    private int time;
    private List<DataBean> left;
    private List<DataBean> right;


    public static class DataBean implements Serializable {
        private int type;
        private ParaBean para;


        public static class ParaBean implements Serializable {
            private int freq;
            private int db;
        }
    }



}
