package com.udit.aijiabao.activitys;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.User;
import com.udit.aijiabao.adapters.BaseAdapterInject;
import com.udit.aijiabao.adapters.ViewHolderInject;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.configs.UrlConfig;
import com.udit.aijiabao.entitys.Details;
import com.udit.aijiabao.entitys.RespAppoint;
import com.udit.aijiabao.entitys.RespProject;
import com.udit.aijiabao.utils.PreferencesUtils;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/19.
 */

@ContentView(R.layout.activity_mine_project)
public class MineProjectActivty extends BaseActivity {

    private final String TAG = MineProjectActivty.class.getSimpleName();

    @ViewInject(R.id.titleView)
    private TitleView mtitleView;

    @ViewInject(R.id.activity_my_progject_listivew)
    private ListView listView;

    List<RespProject>mList;

    ProjectAdapter adapter;
    @Override
    public void setContentView() {

    }

    @Override
    public void initView() {
        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        mtitleView.setTitleText("我的项目");
        mtitleView.hideOperate();
        mtitleView.showBack(true);
        mtitleView.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void initData() {

        mList=new ArrayList<>();

        adapter=new ProjectAdapter(this);
        listView.setAdapter(adapter);
        queryProject();
    }

    private void queryProject(){

        RequestParams params=new RequestParams(UrlConfig.PROJECT_URL);


        params.addBodyParameter("member_id", User.getMemberId());
        params.addBodyParameter("corp_id", PreferencesUtils.getString(Constants.CORP_ID, ""));

        params.addBodyParameter("method", "query");
        params.addBodyParameter("user_type", Constants.USER_TYPE);
        params.addBodyParameter("token", User.getAuthToken());
        params.addBodyParameter("userId", String.valueOf(User.getUserId()));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG,"onSuccess"+result);

                List<RespProject>list=new Gson().fromJson(result,new TypeToken<List<RespProject>>() {
                }.getType());
                mList.clear();

                mList.addAll(list);

                adapter.setData(mList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG,"onError"+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private class ProjectAdapter extends BaseAdapterInject<RespProject>{

        public ProjectAdapter(Context context) {
            super(context);
        }

        @Override
        public int getConvertViewId(int position) {
            return R.layout.project_item;
        }

        @Override
        public ViewHolderInject<RespProject> getNewHolder(int position) {
            return new ViewHolder();
        }

        class ViewHolder extends ViewHolderInject<RespProject>{

            @ViewInject(R.id.item_my_project_kemu)
            private TextView item_my_project_kemu;

            @ViewInject(R.id.item_my_project_kemu_num)
            private TextView item_my_project_kemu_num;

            @ViewInject(R.id.item_my_project_kemu_zhuangtai)
            private TextView item_my_project_kemu_zhuangtai;
            @Override
            public void loadData(RespProject data, int position) {
                item_my_project_kemu.setText(data.getName());
                item_my_project_kemu_num.setText(""+data.getTimes());
                item_my_project_kemu_zhuangtai.setText(data.getStatus());
            }
        }

    }

}
