package com.udit.aijiabao.activitys;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.User;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.configs.UrlConfig;
import com.udit.aijiabao.dialog.PromptDialog;
import com.udit.aijiabao.entitys.CorpDetail;
import com.udit.aijiabao.utils.PreferencesUtils;
import com.udit.aijiabao.utils.T;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.net.URISyntaxException;

import cc.zhipu.library.imageloader.core.DisplayImageOptions;
import cc.zhipu.library.imageloader.core.ImageLoader;
import cc.zhipu.library.imageloader.core.assist.ImageScaleType;
import cc.zhipu.library.imageloader.core.display.FadeInBitmapDisplayer;
import cc.zhipu.library.imageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by Administrator on 2016/5/30.
 */

@ContentView(R.layout.activity_mine_school)
public class MineSchoolActivity extends BaseActivity {

    private final String TAG = MineSchoolActivity.class.getSimpleName();
    @ViewInject(R.id.school_img)
    private ImageView img;
    @ViewInject(R.id.titleView)
    private TitleView titleView;

    @ViewInject(R.id.corp_name)
    private TextView corpName;

    @ViewInject(R.id.ratingbar)
    private RatingBar ratingBar;

    @ViewInject(R.id.now_price)
    private TextView nowPrice;

    @ViewInject(R.id.old_price)
    private TextView oldPrice;

    @ViewInject(R.id.comment_num)
    private TextView commentNum;

    @ViewInject(R.id.attation_num)
    private TextView attationNum;

    @ViewInject(R.id.phone)
    private TextView phone;

    @ViewInject(R.id.address)
    private TextView address;

    @ViewInject(R.id.profile)
    private TextView profileText;

    @ViewInject(R.id.feature)
    private TextView featureText;

    private CorpDetail detail;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    @Override
    public void setContentView() {

    }

    @Override
    public void initView() {
        titleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        titleView.showBack(true);
        titleView.hideOperate();
        titleView.setTitleText("详情");
        titleView.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void initData() {
        imageLoader=ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.img_null)
                .showImageOnFail(R.mipmap.img_null)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(300))
                .displayer(new RoundedBitmapDisplayer(6)).delayBeforeLoading(100).build();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!TextUtils.isEmpty(PreferencesUtils.getString(Constants.CORP_ID))) {
            initDats();
        }
    }

    private void initDats() {
        RequestParams params = new RequestParams(UrlConfig.CROP_URL);

        params.addBodyParameter("method", "queryCorpDetail");//queryCorpDetail

        params.addBodyParameter("userId", String.valueOf(User.getUserId()));
        params.addBodyParameter("token", User.getAuthToken());
        params.addBodyParameter("corpId", PreferencesUtils.getString(Constants.CORP_ID));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "SchoolFind_onSuccess:" + result);

                if (!TextUtils.isEmpty(result)) {
                    detail = new Gson().fromJson(result, CorpDetail.class);
                    corpName.setText(detail.getName());
                    //logo
                    imageLoader.displayImage("http://115.159.2.160:8080/ajb.manage/upload/showFile.htm?fileid="+detail.getLogo(),img,options);
                    if (!TextUtils.isEmpty(detail.getScore())) {
                        ratingBar.setRating(Float.parseFloat(detail.getScore()));
                    } else {
                        ratingBar.setRating(5);
                    }

                    if (!TextUtils.isEmpty(detail.getPrice())) {

                        if (detail.getPrice().contains(".")) {
                            nowPrice.setText("现价:￥" + detail.getPrice().substring(0, detail.getPrice().indexOf
                                    (".")));
                        } else {
                            nowPrice.setText("现价:￥" + detail.getPrice());
                        }
                    }

                    if (!TextUtils.isEmpty(detail.getOld_price())) {

                        if (detail.getOld_price().contains(".")) {
                            oldPrice.setText("原价:￥" + detail.getOld_price().substring(0, detail.getOld_price()
                                    .indexOf(".")));
                        } else {
                            oldPrice.setText("原价:￥" + detail.getOld_price());
                        }
                    }

                    if (!TextUtils.isEmpty(detail.getMembers_total())) {
                        commentNum.setText(detail.getComment_total() + "人评论");
                    }

                    if (!TextUtils.isEmpty(detail.getMembers_total())) {
                        attationNum.setText(detail.getMembers_total() + "人关注");
                    }

                    if (!TextUtils.isEmpty(detail.getTel())) {
                        phone.setText(detail.getTel());
                    }

                    address.setText(detail.getAddress());

                    profileText.setText(detail.getProfile());
                    featureText.setText(detail.getFeature());

                } else {
                    T.show(MineSchoolActivity.this, "没有获取到数据！");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Event(value = {R.id.call_layout, R.id.map_layout, R.id.btns_layout}, type = View
            .OnClickListener.class)
    private void click(View v) {

        switch (v.getId()) {

            case R.id.call_layout:
                call();
                break;

            case R.id.map_layout:

                if (null != detail && !TextUtils.isEmpty(detail.getAddress())) {

                    //调起第三方地图客户端查询
                    try {
                        //百度
                        if (isInstallByread("com.baidu.BaiduMap")) {
                            Intent intent = Intent.getIntent("intent://map/place/search?query=" + detail
                                    .getAddress() + "radius=1000&region=南京&src=爱驾宝|爱驾宝学员端#Intent;scheme=bdapp;" +
                                    "package=com.baidu.BaiduMap;end");
                            startActivity(intent); //启动调用
                            Log.e("Bum", "百度地图客户端已经安装");
                            //高德
                        } else if (isInstallByread("com.autonavi.minimap")) {
                            Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse
                                    ("androidamap://viewGeo?sourceApplication=softname&addr=" + detail.getAddress()));
                            intent.setPackage("com.autonavi.minimap");
                            startActivity(intent);
                            Log.e("Bum", "高德地图客户端已经安装");

                        } else {
                            //若没有安装第三方地图软件，调用本地百度API地图
                            Log.e("Bum", "没有安装地图客户端");
                            Bundle bundle = new Bundle();
                            bundle.putString("address", detail.getAddress());
                            startActivity(MapActivity.class, bundle);
                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } else {
                    T.show(MineSchoolActivity.this, "抱歉，暂时没有检索到该驾校的地址信息！");
                }

                break;

            case R.id.btns_layout:

                if (null != detail && !TextUtils.isEmpty(detail.getCorpId())) {

                    Bundle bundle = new Bundle();

                    bundle.putString("CorpId", detail.getCorpId());

                    bundle.putBoolean("isWrite", true);

                    startActivity(CommentListActivity.class, bundle);

                }

                break;

        }
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    private void call() {
        if (null != detail && !TextUtils.isEmpty(detail.getTel())) {
            PromptDialog.Builder dialog = new PromptDialog.Builder(
                    this);
            dialog.setTitle("拨打电话");
            dialog.setMessage(detail.getTel().trim());
            dialog.setMessageColor(getResources().getColor(R.color.main_red));
            dialog.setButton1(R.string.cancel,
                    new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.setButton2(R.string.ok, new PromptDialog.OnClickListener() {
                @Override
                public void onClick(Dialog dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri
                            .parse("tel:" + detail.getTel().trim()));
                    if (ActivityCompat.checkSelfPermission(MineSchoolActivity.this, Manifest.permission
                            .CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    MineSchoolActivity.this.startActivity(intent);
                }
            });
            dialog.show();
        }
    }
}
