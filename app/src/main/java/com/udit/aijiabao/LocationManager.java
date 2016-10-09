package com.udit.aijiabao;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.utils.PreferencesUtils;

import cc.zhipu.library.event.EventBus;


/**
 * Created by Administrator on 2015/7/1.
 */
public class LocationManager {
	public static LocationClient mLocationClient;

	private static BDLocationListener mLocationListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			if (bdLocation != null) {
				Log.d("CR", "onReceiveLocation=" + bdLocation.getAddrStr() + "err=" + bdLocation.getLocType()+"citycode="+bdLocation.getCityCode());
				PreferencesUtils.putString(Constants.POSITION, bdLocation.getCity());

				PreferencesUtils.putString(Constants.CITY_ID, bdLocation.getCityCode());

				PreferencesUtils.putString(Constants.LONGITUDE, String.valueOf(bdLocation.getLongitude()));

				PreferencesUtils.putString(Constants.LATITUDE, String.valueOf(bdLocation.getLatitude()));


					if (null != bdLocation) {
						Uri uri = Uri.parse("http://api.map.baidu.com/staticimage?" +
								"center=" +
								bdLocation.getLongitude() +
								"," +
								bdLocation.getLatitude() +
								"&width=" +
								"240" +
								"&height=" +
								"240" +
								"&zoom=16" +
								"&markers=" +
								bdLocation.getLongitude() +
								"," +
								bdLocation.getLatitude() +
								"&markerStyles=-1,http://api.map.baidu.com/images/marker_red.png,-1,23,25");

						Log.e("", "AddrStr" + bdLocation.getAddrStr());

				}
				EventBus.getDefault().post(bdLocation);
				mLocationClient.stop();
			}
		}

//		@Override
//		public void onReceivePoi(BDLocation bdLocation) {
//			Log.d("location", "onReceivePoi = " + bdLocation.toString());
//		}
	};

	public static void startLocation(Context context) {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setIsNeedAddress(true);
		option.setCoorType("gcj02");
		option.setScanSpan(3000);
		option.setPriority(LocationClientOption.GpsFirst);
		option.disableCache(true);
		mLocationClient = new LocationClient(context, option);
		mLocationClient.registerLocationListener(mLocationListener);
		mLocationClient.start();
		mLocationClient.requestLocation();
	}


}
