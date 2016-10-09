package com.udit.aijiabao.activitys;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.R;
import com.udit.aijiabao.adapters.MyErrorQuestionListAdapter;
import com.udit.aijiabao.entitys.ErrorQuestion;
import com.udit.aijiabao.entitys.ErrorQuestionInfo;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.DbManager;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的错题
 * 
 * @author 金钟焕
 */
@ContentView(R.layout.my_error_question)
public class MyErrorQuestionActivity extends BaseActivity {
	@ViewInject(R.id.titleView)
	private TitleView titleView;
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();// 列表数据
	private ListView listView;
	
	private List<ErrorQuestion> list=new ArrayList<ErrorQuestion>();
	private MyErrorQuestionListAdapter adapter;
	ErrorQuestion question;
	DbManager dbManager;
	DbManager.DaoConfig daoConfig;
	@Override
	public void setContentView() {
	}
	@Override
	public void initView() {
		titleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
		titleView.setTitleText("我的错题");
		titleView.setTitleTextColor(getResources().getColor(R.color.white));

		titleView.showBack(true);
		titleView.hideOperate();
		listView = (ListView) findViewById(R.id.listview);
		adapter = new MyErrorQuestionListAdapter(this, data, listView);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MyErrorQuestionActivity.this,MyErrorQuestionDetailActivity.class);
				question=list.get(position);
				intent.putExtra("questionName", question.getQuestionName());
				intent.putExtra("questionType", question.getQuestionType());
				intent.putExtra("questionAnswer", question.getQuestionAnswer());
				intent.putExtra("questionSelect", question.getQuestionSelect());
				intent.putExtra("isRight", question.getIsRight());
				intent.putExtra("Analysis", question.getAnalysis());
				intent.putExtra("optionA", question.getOptionA());
				intent.putExtra("optionB", question.getOptionB());
				intent.putExtra("optionC", question.getOptionC());
				intent.putExtra("optionD", question.getOptionD());
				intent.putExtra("optionE", question.getOptionE());
				intent.putExtra("optionType", question.getOptionType());
				startActivity(intent);
			}
		});
		daoConfig.setDbName("");
		dbManager = x.getDb(daoConfig);

		ErrorQuestionInfo[] errorQuestionInfos =new ErrorQuestionInfo[2];//= dbManager.findAll(ErrorQuestionInfo.class);
		if (errorQuestionInfos == null) {
			Toast.makeText(MyErrorQuestionActivity.this, "暂无数据",
					Toast.LENGTH_SHORT).show();
		} else {
			Map<String, Object> map = null;
			for (int i = 0; i < errorQuestionInfos.length; i++) {
				ErrorQuestion errorQuestion=new ErrorQuestion();
				map = new HashMap<String, Object>();
				map.put("title", errorQuestionInfos[i].questionName);// 标题
				map.put("type", errorQuestionInfos[i].questionType);// 标题
				map.put("answer", errorQuestionInfos[i].questionAnswer);// 标题
				map.put("isright", errorQuestionInfos[i].isRight);// 
				map.put("selected", errorQuestionInfos[i].questionSelect);// 
				map.put("analysis", errorQuestionInfos[i].Analysis);// 
				data.add(map);
				
				errorQuestion.setQuestionName(errorQuestionInfos[i].questionName);
				errorQuestion.setQuestionType(errorQuestionInfos[i].questionType);
				errorQuestion.setQuestionAnswer(errorQuestionInfos[i].questionAnswer);
				errorQuestion.setQuestionSelect(errorQuestionInfos[i].questionSelect);
				errorQuestion.setIsRight(errorQuestionInfos[i].isRight);
				errorQuestion.setAnalysis(errorQuestionInfos[i].Analysis);
				errorQuestion.setOptionA(errorQuestionInfos[i].optionA);
				errorQuestion.setOptionB(errorQuestionInfos[i].optionB);
				errorQuestion.setOptionC(errorQuestionInfos[i].optionC);
				errorQuestion.setOptionD(errorQuestionInfos[i].optionD);
				errorQuestion.setOptionE(errorQuestionInfos[i].optionE);
				errorQuestion.setOptionType(errorQuestionInfos[i].optionType);
				list.add(errorQuestion);
			}
		}

	}

	@Override
	public void initData() {

	}

}
