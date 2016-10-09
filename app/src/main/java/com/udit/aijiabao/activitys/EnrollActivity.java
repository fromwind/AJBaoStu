package com.udit.aijiabao.activitys;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.dialog.JDialog;
import com.udit.aijiabao.dialog.ToastDialog;
import com.udit.aijiabao.widgets.ClearEditText;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/5/20.
 */
@ContentView(R.layout.activity_enroll)
public class EnrollActivity extends BaseActivity {

    @ViewInject(R.id.titleView)
    private TitleView titleView;

    @ViewInject(R.id.schoolaName)
    private TextView schoolaName;

    @ViewInject(R.id.name_edit)
    private ClearEditText nameEdit;

    @ViewInject(R.id.idcard_edit)
    private ClearEditText idcardEdit;

    @ViewInject(R.id.phone_edit)
    private ClearEditText PhoneEdit;

    @ViewInject(R.id.sex)
    private TextView sexTxt;

    @ViewInject(R.id.licenseType)
    private TextView licenseType;


    private String corpId;
    private String corpName;
    private String price;

    private String name,phone,idcard,sex,liType;//男1女0

    @Override
    public void setContentView() {
        titleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        titleView.showBack(true);
        titleView.hideOperate();
        titleView.setTitleText("报名");
        titleView.setTitleTextColor(getResources().getColor(R.color.white));

        Bundle bundle = getIntent().getExtras();
        corpName=bundle.getString("corpName","");
        corpId=bundle.getString("corpId","");
        price=bundle.getString("price","");


    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

        schoolaName.setText(corpName);

    }

    @Event(value = {R.id.sex_layout, R.id.license_layout, R.id.pay_btn})
    private void clicks(View view) {
        switch (view.getId()) {

            case R.id.sex_layout:

                JDialog.showDialog(this, "选择性别", new String[]{"女", "男"}, new JDialog.DialogItemClickListener() {
                    @Override
                    public void confirm(String result) {
                        sexTxt.setText(result);
                    }
                });

                break;

            case R.id.license_layout:
                JDialog.showDialog(this, "选择驾照类型", new String[]{"C1", "C2"}, new JDialog.DialogItemClickListener() {
                    @Override
                    public void confirm(String result) {
                        licenseType.setText(result);
                    }
                });
                break;

            case R.id.pay_btn:

                verify();

                break;

        }
    }

    private void verify() {
        name = nameEdit.getText().toString().trim();
        idcard = idcardEdit.getText().toString().trim();
        phone=PhoneEdit.getText().toString().trim();
        liType=licenseType.getText().toString().trim();
        sex=sexTxt.getText().toString();

        Spanned error;
        if (TextUtils.isEmpty(name)) {
            error = Html.fromHtml("<font color='red'>"
                    + "请输入姓名！" + "</font>");
            nameEdit.setError(error);
        } else if (TextUtils.isEmpty(idcard)) {
            error = Html.fromHtml("<font color='red'>"
                    + "请输入身份证号！" + "</font>");
            idcardEdit.setError(error);
        } else if (idcard.length() < 18){
            error = Html.fromHtml("<font color='red'>"
                    + "身份证长度错误！" + "</font>");
            idcardEdit.setError(error);
        } else if(TextUtils.isEmpty(phone)){
            error = Html.fromHtml("<font color='red'>"
                    + "请输入手机号！" + "</font>");
            PhoneEdit.setError(error);
        }else if(TextUtils.isEmpty(liType)){

            new ToastDialog(this).builder().setTip2("请选择驾照类型！").show();

        } else if(TextUtils.isEmpty(sex)){
            new ToastDialog(this).builder().setTip2("请选择性别！").show();
        }else {
            pay();
        }
    }

    private void  pay(){

        Bundle bundle=new Bundle();

        bundle.putString("name",name);

        bundle.putString("phone",phone);

        bundle.putString("idcard",idcard);

        if (TextUtils.equals(sex,"男")){
            bundle.putString("sex","1");
        }else{
            bundle.putString("sex","0");
        }

        bundle.putString("liType",liType);

        bundle.putString("corpId",corpId);
        bundle.putString("corpName",corpName);
        /*
        * 666
        * */
        bundle.putString("price",price);

        startActivity(PayMentActivity.class, bundle);

    }
}
