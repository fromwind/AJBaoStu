package com.udit.aijiabao.activitys;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.BaseActivity0;
import com.udit.aijiabao.R;
import com.udit.aijiabao.adapters.BaseAdapterInject;
import com.udit.aijiabao.adapters.ViewHolderInject;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.utils.PreferencesUtils;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/5/24.
 */

@ContentView(R.layout.activity_searchlocation)
public class SearchLocationActivity extends BaseActivity0 {

    @ViewInject(R.id.titleView)
    private TitleView mtitleView;

    @ViewInject(R.id.search_edit)
    private AutoCompleteTextView completeTextView;

    @ViewInject(R.id.location_text)
    private TextView locationTx;

    @ViewInject(R.id.result_listview)
    private ListView listView;

    @ViewInject(R.id.hot_gridview)
    private GridView gridView;

    private GridAdapter gridAdapter;

    private ListAdapter listAdapter;

    final String[] hotCities = new String[]{"苏州", "无锡", "常州", "南通",
            "泰州", "扬州", "盐城", "镇江", "宿迁", "淮安", "徐州", "连云港"};

    final String[] cities = new String[]{"南京", "苏州", "无锡", "常州", "南通",
            "泰州", "扬州", "盐城", "镇江", "宿迁", "淮安", "徐州", "连云港"};

    private List<String>cityList;

    private InputMethodManager imm;

    @Override
    public void setContentView() {
        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        mtitleView.setTitleText("当前城市:" + PreferencesUtils.getString(Constants.POSITION));
        mtitleView.hideOperate();
        mtitleView.showBack(true);

        mtitleView.setBackImage(R.mipmap.location_back);
        mtitleView.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void initView() {
        locationTx.setText("当前城市:" + PreferencesUtils.getString(Constants.POSITION));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();

                if (null != listAdapter) {
                    String item = listAdapter.getItem(position);

                    intent.putExtra("ITEM", item);
                }

                setResult(Constants.LOCATION_RESP, intent);
                finish();
                overridePendingTransition(R.anim.bottom_,
                        R.anim.bottom_close);

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();

                if(null!=gridAdapter){
                    String item=gridAdapter.getItem(position);

                    intent.putExtra("ITEM", item);
                }

                setResult(Constants.LOCATION_RESP, intent);
                finish();
                overridePendingTransition(R.anim.bottom_,
                        R.anim.bottom_close);
            }
        });

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imm.hideSoftInputFromWindow(completeTextView.getWindowToken(), 0);
                }

                return false;
            }
        });
        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imm.hideSoftInputFromWindow(completeTextView.getWindowToken(), 0);
                }

                return false;
            }
        });

        completeTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    imm.showSoftInput(v, 0);
                } else {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

    }

    @Override
    public void initData() {

        cityList=Arrays.asList(cities);

        listAdapter = new ListAdapter(this);

        listView.setAdapter(listAdapter);

        listAdapter.setData(cityList);

        gridAdapter = new GridAdapter(this);

        gridView.setAdapter(gridAdapter);

        gridAdapter.setData(Arrays.asList(hotCities));

        gridView.setVisibility(View.GONE);

        completeTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (TextUtils.isEmpty(str)) {
                    gridView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                } else {
                    gridView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String aa = s.toString();
                Pattern p = Pattern.compile(aa);
                List<String> we = new ArrayList<String>();
                for (int i = 0; i < cityList.size(); i++) {
                    String pp = cityList.get(i);
                    Matcher matcher = p.matcher(pp);
                    if (matcher.find()) {
                        we.add(pp);
                    }
                }

                listAdapter.setData(we);
            }
        });
    }

    private class GridAdapter extends BaseAdapterInject<String> {

        public GridAdapter(Context context) {
            super(context);
        }

        @Override
        public int getConvertViewId(int position) {
            return R.layout.grid_item;
        }

        @Override
        public ViewHolderInject<String> getNewHolder(int position) {
            return new GridHolder();
        }

        class GridHolder extends ViewHolderInject<String> {

            @ViewInject(R.id.item_text)
            private TextView text;

            @Override
            public void loadData(String data, int position) {
                text.setText(data);
            }
        }

    }

    private class ListAdapter extends BaseAdapterInject<String> {

        public ListAdapter(Context context) {
            super(context);
        }

        @Override
        public int getConvertViewId(int position) {
            return R.layout.list_item;
        }

        @Override
        public ViewHolderInject<String> getNewHolder(int position) {
            return new ListHolder();
        }

        class ListHolder extends ViewHolderInject<String> {

            @ViewInject(R.id.item_text)
            private TextView text;

            @Override
            public void loadData(String data, int position) {
                text.setText(data);
            }
        }

    }

}
