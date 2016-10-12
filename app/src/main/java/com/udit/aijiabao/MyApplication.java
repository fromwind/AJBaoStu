package com.udit.aijiabao;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.baidu.mapapi.SDKInitializer;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.receiver.NetConnectReceiver;
import com.udit.aijiabao.utils.EventEntity;
import com.udit.aijiabao.utils.PreferencesUtils;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

import java.io.File;

import cc.zhipu.library.event.EventBus;
import cc.zhipu.library.imageloader.cache.disc.impl.UnlimitedDiskCache;
import cc.zhipu.library.imageloader.cache.disc.naming.HashCodeFileNameGenerator;
import cc.zhipu.library.imageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import cc.zhipu.library.imageloader.core.ImageLoader;
import cc.zhipu.library.imageloader.core.ImageLoaderConfiguration;
import cc.zhipu.library.imageloader.utils.StorageUtils;

/**
 * Created by Administrator on 2016/5/11.
 */
public class MyApplication extends Application {

    private static MyApplication instance;
    private DbManager.DaoConfig config;
    private NetConnectReceiver netConnectReceiver;

    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler instance = CrashHandler.getInstance();
        //        instance.init(this);
        //        initWeatherPic();
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
            registerReceiver();
            PreferencesUtils.init(this);
            x.Ext.init(this);

            x.Ext.setDebug(true);//是否输出Debug日志
            config = new DbManager.DaoConfig();
            //每一个config对象描述一个数据库，多个数据库就创建多个config对象
            //这里作为示例，我们只创建一个名字为dbName版本号为1的数据库
            config.setDbName("Bum").setDbVersion(1);//设置数据库版本号
            //config.setDbDir(File file);
            // 该语句会将数据库文件保存在你想存储的地方
            //如果不设置则默认存储在应用程序目录下/database/dbName.db
            config.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    //用来监听数据库是否升级
                    //如果当前数据库版本号比已存在的数据库版本号高则执行此片段
                    //用途：软件升级之后在第一次运行时执行一些必要的初始化操作
                }
            });
            config.setTableCreateListener(new DbManager.TableCreateListener() {
                @Override
                public void onTableCreated(DbManager db, TableEntity table) {
                    //用来监听数据表的创建
                    //当第一次创建表的时候执行此片段
                }
            });
            config.setAllowTransaction(true);
            //是否允许开启事务

            SDKInitializer.initialize(this);
            initImageLoader();
        }
    }

    public DbManager.DaoConfig getDaoConfig() {
        return config;
    }

    private void initWeatherPic() {
        Constants.weather.put("00", R.mipmap.d00);
        Constants.weather.put("01", R.mipmap.d01);
        Constants.weather.put("02", R.mipmap.d02);
        Constants.weather.put("03", R.mipmap.d03);
        Constants.weather.put("04", R.mipmap.d04);
        Constants.weather.put("05", R.mipmap.d05);
        Constants.weather.put("06", R.mipmap.d06);
        Constants.weather.put("07", R.mipmap.d07);
        Constants.weather.put("08", R.mipmap.d08);
        Constants.weather.put("09", R.mipmap.d09);
        Constants.weather.put("10", R.mipmap.d10);
        Constants.weather.put("11", R.mipmap.d11);
        Constants.weather.put("12", R.mipmap.d12);
        Constants.weather.put("13", R.mipmap.d13);
        Constants.weather.put("14", R.mipmap.d14);
        Constants.weather.put("15", R.mipmap.d15);
        Constants.weather.put("16", R.mipmap.d16);
        Constants.weather.put("17", R.mipmap.d17);
        Constants.weather.put("18", R.mipmap.d18);
        Constants.weather.put("19", R.mipmap.d19);
        Constants.weather.put("20", R.mipmap.d20);
        Constants.weather.put("21", R.mipmap.d21);
        Constants.weather.put("22", R.mipmap.d22);
        Constants.weather.put("23", R.mipmap.d23);
        Constants.weather.put("24", R.mipmap.d24);
        Constants.weather.put("25", R.mipmap.d25);
        Constants.weather.put("26", R.mipmap.d26);
        Constants.weather.put("27", R.mipmap.d27);
        Constants.weather.put("28", R.mipmap.d28);
        Constants.weather.put("29", R.mipmap.d29);
        Constants.weather.put("30", R.mipmap.d30);
        Constants.weather.put("31", R.mipmap.d31);
        Constants.weather.put("53", R.mipmap.d53);
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 初始化imageloader
     */
    private void initImageLoader() {
        ImageLoader imageLoader = ImageLoader.getInstance();
        File cacheDir = StorageUtils.getOwnCacheDirectory(this, "ijiabao/manage/cache/image");
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800)  // max width, max height
                .threadPoolSize(5).threadPriority(Thread.NORM_PRIORITY - 1)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        imageLoader.init(configuration);
    }

    /**
     * 注册广播接收者
     */
    private void registerReceiver() {
        netConnectReceiver = new NetConnectReceiver();
        IntentFilter filter = new IntentFilter();

        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        filter.setPriority(1000);
        registerReceiver(netConnectReceiver, filter);
    }

    /**
     * 网络变化监听事件
     *
     * @param net
     */
    public void onEventMainThread(EventEntity.NetChange net) {
        if (net.isConnected) {
            LocationManager.startLocation(this);
        }
    }

    public void exit(boolean isExit) {
        EventBus.getDefault().unregister(this);

        if (isExit) {
            ActivityCollector.finishAll();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
