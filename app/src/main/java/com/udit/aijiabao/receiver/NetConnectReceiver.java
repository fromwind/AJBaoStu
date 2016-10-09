package com.udit.aijiabao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntegerRes;
import android.util.Log;
import android.widget.TextView;

import com.udit.aijiabao.R;
import com.udit.aijiabao.utils.EventEntity;
import com.udit.aijiabao.utils.NetWorkUtils;

import org.xutils.view.annotation.ViewInject;

import cc.zhipu.library.event.EventBus;


public class NetConnectReceiver extends BroadcastReceiver {
	private static final String TAG = "NetConnectReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		EventEntity.NetChange net = new EventEntity.NetChange();
		net.name = NetWorkUtils.getNetworkTypeName(context);
		net.isConnected = NetWorkUtils.isConnective(context);
		Log.i(TAG, "当前网络 " + net.name);

		EventBus.getDefault().post(net);
	}
}
