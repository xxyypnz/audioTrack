package com.share.audiotrackdemo.syfuction;

/**
 * @Description: 描述
 * @Author: wangcheng
 * @CeateDate: 2023/12/12
 * @UpdateUser: 更新着
 * @UpdateData: 更新时间
 * @UpdateRemark: 更新说明
 * @Vsersion: 1.0
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.share.audiotrackdemo.MainActivity;


public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            Intent thisIntent = new Intent(context, SyActivity.class);

            thisIntent.setAction("android.intent.action.MAIN");

            thisIntent.addCategory("android.intent.category.LAUNCHER");

            thisIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(thisIntent);
        }
    }
}

