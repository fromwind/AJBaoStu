package com.udit.aijiabao.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.udit.aijiabao.MyApplication;
import com.udit.aijiabao.R;
import com.udit.aijiabao.activitys.RealPracticeActivity;
import com.udit.aijiabao.entitys.Question;
import com.udit.aijiabao.entitys.QuestionPosition;
import com.udit.aijiabao.entitys.RealTest;
import com.udit.aijiabao.utils.FileUtils;
import com.udit.aijiabao.utils.T;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RealPracticeAdapter extends PagerAdapter {

    RealPracticeActivity mContext;
    // 传递过来的页面view的集合
    List<View> viewItems;
    // 每个item的页面view
    View convertView;
    // 传递过来的所有数据
    List<RealTest> dataItems;
    public static int mposition;

    private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
    private Map<Integer, Boolean> mapClick = new HashMap<Integer, Boolean>();
    private Map<Integer, String> mapMultiSelect = new HashMap<Integer, String>();

    boolean isNext = false;

    String[] multi_answer;
    String youranswer;
    DbManager dbManager;
    Bitmap bitmap;
    int errortopicNum = 0;

    public RealPracticeAdapter(RealPracticeActivity context, List<View> viewItems, List<RealTest>
            dataItems) {
        mContext = context;
        this.viewItems = viewItems;
        this.dataItems = dataItems;

        dbManager = x.getDb(((MyApplication) mContext.getApplicationContext()).getDaoConfig1());
        multi_answer = new String[]{"0", "0", "0", "0"};
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewItems.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ViewHolder holder = new ViewHolder();
        mposition=position;
        convertView = viewItems.get(position);
        holder.questionType = (TextView) convertView.findViewById(R.id.activity_prepare_test_no);
        holder.question = (TextView) convertView.findViewById(R.id.activity_prepare_test_question);
        holder.img = (ImageView) convertView.findViewById(R.id.activity_prepare_test_img);
        holder.collectimg = (ImageView) convertView.findViewById(R.id.menu_bottom_collect_img);
        holder.collecttxt = (TextView) convertView.findViewById(R.id.menu_bottom_collect_txt);
        holder.previousBtn = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_upLayout);
        holder.nextBtn = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_nextLayout);
        holder.nextText = (TextView) convertView.findViewById(R.id.menu_bottom_nextTV);
        holder.collectBtn = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_collectLayout);
        holder.totalText = (TextView) convertView.findViewById(R.id.activity_prepare_test_totalTv);
        holder.nextImage = (ImageView) convertView.findViewById(R.id.menu_bottom_nextIV);
        holder.wrongLayout = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_wrongLayout);
        holder.explaindetailTv = (TextView) convertView.findViewById(R.id.activity_prepare_test_explaindetail);
        holder.layoutA = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_a);
        holder.layoutB = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_b);
        holder.layoutC = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_c);
        holder.layoutD = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_d);
        holder.ivA = (ImageView) convertView.findViewById(R.id.vote_submit_select_image_a);
        holder.ivB = (ImageView) convertView.findViewById(R.id.vote_submit_select_image_b);
        holder.ivC = (ImageView) convertView.findViewById(R.id.vote_submit_select_image_c);
        holder.ivD = (ImageView) convertView.findViewById(R.id.vote_submit_select_image_d);
        holder.tvA = (TextView) convertView.findViewById(R.id.vote_submit_select_text_a);
        holder.tvB = (TextView) convertView.findViewById(R.id.vote_submit_select_text_b);
        holder.tvC = (TextView) convertView.findViewById(R.id.vote_submit_select_text_c);
        holder.tvD = (TextView) convertView.findViewById(R.id.vote_submit_select_text_d);
        holder.multi_choice = (TextView) convertView.findViewById(R.id.activity_prepare_test_multi_choice);

        holder.totalText.setText(position + 1 + "/" + dataItems.size());
        //判断是否收藏，UI变换
        if (IsCollect(dataItems.get(position).getTopicId())) {
            holder.collectimg.setImageResource(R.mipmap.star_yellow);
            holder.collecttxt.setText("移出收藏");
        } else {
            holder.collectimg.setImageResource(R.mipmap.star_write);
            holder.collecttxt.setText("添加收藏");
        }

        holder.collectBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (IsCollect(dataItems.get(position).getTopicId())) {
                    Collect_delete(dataItems.get(position).getTopicId());
                } else {
                    Collect(dataItems.get(position).getTopicId());
                }
                if (IsCollect(dataItems.get(position).getTopicId())) {
                    holder.collectimg.setImageResource(R.mipmap.star_yellow);
                    holder.collecttxt.setText("移出收藏");
                } else {
                    holder.collectimg.setImageResource(R.mipmap.star_write);
                    holder.collecttxt.setText("添加收藏");
                }
            }
        });

        if (dataItems.get(position).getOptionC() == null) {
            holder.tvA.setText("A." + dataItems.get(position).getOptionA());
            holder.tvB.setText("B." + dataItems.get(position).getOptionB());
            holder.layoutC.setVisibility(View.GONE);
            holder.layoutD.setVisibility(View.GONE);
        } else {
            holder.tvA.setText("A." + dataItems.get(position).getOptionA());
            holder.tvB.setText("B." + dataItems.get(position).getOptionB());
            holder.tvC.setText("C." + dataItems.get(position).getOptionC());
            holder.tvD.setText("D." + dataItems.get(position).getOptionD());
        }

        if (!isExist(FileUtils.getproject1Path() + "/" + dataItems.get(position).getTopicId() + ".jpg")) {
            holder.img.setVisibility(View.GONE);
        } else {
            bitmap = getLoacalBitmap(FileUtils.getproject1Path() + "/" + dataItems.get(position).getTopicId() + ".jpg");
            holder.img.setImageBitmap(bitmap);
        }
        if (dataItems.get(position).getCorrectAnswer().length() == 1) {
            if (dataItems.get(position).getOptionC() == null || dataItems.get(position).getOptionC().isEmpty() ||
                    dataItems.get(position).getOptionC().equals("")) {
                holder.questionType.setText("判断题");
                holder.multi_choice.setVisibility(View.GONE);
                holder.question.setText(position + 1 + ". " + dataItems.get(position).getTopic());
                holder.layoutA.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        doABCD(position, "1", holder);
                    }
                });
                holder.layoutB.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        doABCD(position, "2", holder);
                    }
                });
            } else {
                holder.questionType.setText("单选题");
                holder.multi_choice.setVisibility(View.GONE);
                holder.question.setText(position + 1 + ". " + dataItems.get(position).getTopic());
                holder.layoutA.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        doABCD(position, "1", holder);
                    }
                });
                holder.layoutB.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        doABCD(position, "2", holder);
                    }
                });
                holder.layoutC.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        doABCD(position, "3", holder);
                    }
                });
                holder.layoutD.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        doABCD(position, "4", holder);
                    }
                });
            }

        } else if (dataItems.get(position).getCorrectAnswer().length() > 1) {
            //多选题
            holder.questionType.setText("多选题");
            holder.question.setText(position + 1 + ". " + dataItems.get(position).getTopic());
            holder.multi_choice.setVisibility(View.VISIBLE);
            holder.layoutA.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (map.containsKey(position)) {
                        return;
                    }
                    if (multi_answer[0] == "0") {
                        multi_answer[0] = "1";
                        holder.ivA.setImageResource(R.mipmap.circle);
                    } else {
                        multi_answer[0] = "0";
                        holder.ivA.setImageResource(R.mipmap.ic_practice_test_normal);
                    }
                }
            });
            holder.layoutB.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (map.containsKey(position)) {
                        return;
                    }
                    if (multi_answer[1] == "0") {
                        multi_answer[1] = "1";
                        holder.ivB.setImageResource(R.mipmap.circle);
                    } else {
                        multi_answer[1] = "0";
                        holder.ivB.setImageResource(R.mipmap.ic_practice_test_normal);
                    }
                }
            });
            holder.layoutC.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (map.containsKey(position)) {
                        return;
                    }
                    if (multi_answer[2] == "0") {
                        multi_answer[2] = "1";
                        holder.ivC.setImageResource(R.mipmap.circle);
                    } else {
                        multi_answer[2] = "0";
                        holder.ivC.setImageResource(R.mipmap.ic_practice_test_normal);
                    }
                }
            });
            holder.layoutD.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (map.containsKey(position)) {
                    return;
                }
                    if (multi_answer[3] == "0") {
                        multi_answer[3] = "1";
                        holder.ivD.setImageResource(R.mipmap.circle);
                    } else {
                        multi_answer[3] = "0";
                        holder.ivD.setImageResource(R.mipmap.ic_practice_test_normal);
                    }
                }
            });
            holder.multi_choice.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (map.containsKey(position)) {
                        return;
                    }
                    mapClick.put(position, true);

                    if (multi_choice_judge(multi_answer, dataItems.get(position).getCorrectAnswer(), holder)) {

                    } else SaveErrorQuestion(dataItems.get(position).getTopicId(), multi_answer.toString());
                    saveprogress(position,multi_answer.toString());
                    map.put(position, true);
                    holder.ivD.setImageResource(R.mipmap.ic_practice_test_wrong);
                    holder.tvD.setTextColor(Color.parseColor("#d53235"));
                    //提示
                    //holder.wrongLayout.setVisibility(View.VISIBLE);
                }

            });

        }
        //读取进度，如果有答案则显示对错
        if (dataItems.get(position).getYourAnswer()!=null){
            map.put(position,true);
            if (dataItems.get(position).getCorrectAnswer().equals(dataItems.get(position).getYourAnswer())) {//如果作对，显示对号标记
                String youranswer=dataItems.get(position).getYourAnswer();
                if (youranswer.equals("1")) {
                    holder.ivA.setImageResource(R.mipmap.ic_practice_test_right);
                    holder.tvA.setTextColor(Color.parseColor("#61bc31"));
                } else if (youranswer.equals("2")) {
                    holder.ivB.setImageResource(R.mipmap.ic_practice_test_right);
                    holder.tvB.setTextColor(Color.parseColor("#61bc31"));
                } else if (youranswer.equals("3")) {
                    holder.ivC.setImageResource(R.mipmap.ic_practice_test_right);
                    holder.tvC.setTextColor(Color.parseColor("#61bc31"));
                } else if (youranswer.equals("4")) {
                    holder.ivD.setImageResource(R.mipmap.ic_practice_test_right);
                    holder.tvD.setTextColor(Color.parseColor("#61bc31"));
                }
            } else {
                String youranswer=dataItems.get(position).getYourAnswer();
                if (youranswer.equals("1")) {
                    holder.ivA.setImageResource(R.mipmap.ic_practice_test_wrong);
                    holder.tvA.setTextColor(Color.parseColor("#d53235"));
                } else if (youranswer.equals("2")) {
                    holder.ivB.setImageResource(R.mipmap.ic_practice_test_wrong);
                    holder.tvB.setTextColor(Color.parseColor("#d53235"));
                } else if (youranswer.equals("3")) {
                    holder.ivC.setImageResource(R.mipmap.ic_practice_test_wrong);
                    holder.tvC.setTextColor(Color.parseColor("#d53235"));
                } else if (youranswer.equals("4")) {
                    holder.ivD.setImageResource(R.mipmap.ic_practice_test_wrong);
                    holder.tvD.setTextColor(Color.parseColor("#d53235"));
                }
                //提示
                //holder.wrongLayout.setVisibility(View.VISIBLE);
                //显示正确选项
                if (dataItems.get(position).getCorrectAnswer().equals("1")) {
                    holder.ivA.setImageResource(R.mipmap.ic_practice_test_right);
                    holder.tvA.setTextColor(Color.parseColor("#61bc31"));
                } else if (dataItems.get(position).getCorrectAnswer().equals("2")) {
                    holder.ivB.setImageResource(R.mipmap.ic_practice_test_right);
                    holder.tvB.setTextColor(Color.parseColor("#61bc31"));
                } else if (dataItems.get(position).getCorrectAnswer().equals("3")) {
                    holder.ivC.setImageResource(R.mipmap.ic_practice_test_right);
                    holder.tvC.setTextColor(Color.parseColor("#61bc31"));
                } else if (dataItems.get(position).getCorrectAnswer().equals("4")) {
                    holder.ivD.setImageResource(R.mipmap.ic_practice_test_right);
                    holder.tvD.setTextColor(Color.parseColor("#61bc31"));
                }
            }
        }


        /**
         * 知识：text部分（0,5）设置颜色（Color.parseColor("#2b89e9")）
         *
         * ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.parseColor("#2b89e9"));
         * SpannableStringBuilder builder1 = new SpannableStringBuilder(holder.question.getText().toString());
         * builder1.setSpan(blueSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
         * holder.question.setText(builder1);
         */
        /*ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.parseColor("#2b89e9"));
        BackgroundColorSpan bluecircle = new BackgroundColorSpan(R.drawable.bluecircle);
        SpannableStringBuilder builder1 = new SpannableStringBuilder(holder.question.getText().toString());
        builder1.setSpan(blueSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder1.setSpan(bluecircle, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.question.setText(builder1);
        */
        // 最后一页修改"下一步"按钮文字
        if (position == viewItems.size() - 1) {
            holder.nextText.setText("提交");
            holder.nextImage.setImageResource(R.mipmap.question_update);
        }
        holder.previousBtn.setOnClickListener(new LinearOnClickListener(position - 1, false, position, holder));
        holder.nextBtn.setOnClickListener(new LinearOnClickListener(position + 1, true, position, holder));
        container.addView(viewItems.get(position));
        return viewItems.get(position);
    }

    //判断是否收藏
    private boolean IsCollect(String topicId) {
        try {
            if (dbManager.selector(Question.class).where("topicId", "=", topicId).findFirst().isCollect() == false) {
                return false;
            } else {
                return true;
            }
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }

    //执行收藏
    private void Collect(String topicId) {
        try {
            Question question=dbManager.selector(Question.class).where("topicId","=",topicId).findFirst();
            question.setCollect(true);
            dbManager.update(question);
            T.show(mContext, "收藏成功");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void Collect_delete(String topicId) {
        try {
            Question question=dbManager.selector(Question.class).where("topicId","=",topicId).findFirst();
            question.setCollect(false);
            dbManager.update(question);
            T.show(mContext, "已移出收藏！");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private boolean multi_choice_judge(String[] multi_answer, String correctAnswer, ViewHolder holder) {
        boolean isError = false;
        if (correctAnswer.contains("1")) {
            if (multi_answer[0] == "1") {
                holder.ivA.setImageResource(R.mipmap.ic_practice_test_right);
                holder.tvA.setTextColor(Color.parseColor("#61bc31"));
            } else {
                holder.ivA.setImageResource(R.mipmap.ic_practice_test_right_noselect);
                isError = true;
            }
        } else if (multi_answer[0] == "1") {
            holder.ivA.setImageResource(R.mipmap.ic_practice_test_wrong);
            holder.tvA.setTextColor(Color.parseColor("#61bc31"));
            isError = true;
        }
        if (correctAnswer.contains("2")) {
            if (multi_answer[1] == "1") {
                holder.ivB.setImageResource(R.mipmap.ic_practice_test_right);
                holder.tvB.setTextColor(Color.parseColor("#61bc31"));
            } else {
                holder.ivB.setImageResource(R.mipmap.ic_practice_test_right_noselect);
                isError = true;
            }
        } else if (multi_answer[1] == "1") {
            holder.ivB.setImageResource(R.mipmap.ic_practice_test_wrong);
            holder.tvB.setTextColor(Color.parseColor("#61bc31"));
            isError = true;
        }
        if (correctAnswer.contains("3")) {
            if (multi_answer[2] == "1") {
                holder.ivC.setImageResource(R.mipmap.ic_practice_test_right);
                holder.tvC.setTextColor(Color.parseColor("#61bc31"));
            } else {
                holder.ivC.setImageResource(R.mipmap.ic_practice_test_right_noselect);
                isError = true;
            }
        } else if (multi_answer[2] == "1") {
            holder.ivC.setImageResource(R.mipmap.ic_practice_test_wrong);
            holder.tvC.setTextColor(Color.parseColor("#61bc31"));
            isError = true;
        }
        if (correctAnswer.contains("4")) {
            if (multi_answer[3] == "1") {
                holder.ivD.setImageResource(R.mipmap.ic_practice_test_right);
                holder.tvD.setTextColor(Color.parseColor("#61bc31"));
            } else {
                holder.ivD.setImageResource(R.mipmap.ic_practice_test_right_noselect);
                isError = true;
            }
        } else if (multi_answer[3] == "1") {
            holder.ivD.setImageResource(R.mipmap.ic_practice_test_wrong);
            holder.tvD.setTextColor(Color.parseColor("#61bc31"));
            isError = true;
        }
        return !isError;
    }

    private void doABCD(int position, String youranswer, ViewHolder holder) {

        //保存做题进度，记得新的测试做题时清空该数据库（savequestioninfo）
        saveprogress(position,youranswer);
        //如果已经有答案则return！
        if (map.containsKey(position)) {
            return;
        }
        map.put(position, true);

        if (dataItems.get(position).getCorrectAnswer().equals(youranswer)) {//如果作对，显示对号标记
            mContext.setCurrentView(position + 1);
            if (youranswer.equals("1")) {
                holder.ivA.setImageResource(R.mipmap.ic_practice_test_right);
                holder.tvA.setTextColor(Color.parseColor("#61bc31"));
            } else if (youranswer.equals("2")) {
                holder.ivB.setImageResource(R.mipmap.ic_practice_test_right);
                holder.tvB.setTextColor(Color.parseColor("#61bc31"));
            } else if (youranswer.equals("3")) {
                holder.ivC.setImageResource(R.mipmap.ic_practice_test_right);
                holder.tvC.setTextColor(Color.parseColor("#61bc31"));
            } else if (youranswer.equals("4")) {
                holder.ivD.setImageResource(R.mipmap.ic_practice_test_right);
                holder.tvD.setTextColor(Color.parseColor("#61bc31"));
            }
        } else {//把自己选错的答案显示错误标记
            errortopicNum += 1;
            //添加错误题目到本地数据库
            SaveErrorQuestion(dataItems.get(position).getTopicId(), youranswer);

            if (youranswer.equals("1")) {
                holder.ivA.setImageResource(R.mipmap.ic_practice_test_wrong);
                holder.tvA.setTextColor(Color.parseColor("#d53235"));
            } else if (youranswer.equals("2")) {
                holder.ivB.setImageResource(R.mipmap.ic_practice_test_wrong);
                holder.tvB.setTextColor(Color.parseColor("#d53235"));
            } else if (youranswer.equals("3")) {
                holder.ivC.setImageResource(R.mipmap.ic_practice_test_wrong);
                holder.tvC.setTextColor(Color.parseColor("#d53235"));
            } else if (youranswer.equals("4")) {
                holder.ivD.setImageResource(R.mipmap.ic_practice_test_wrong);
                holder.tvD.setTextColor(Color.parseColor("#d53235"));
            }
            //提示
            //holder.wrongLayout.setVisibility(View.VISIBLE);
            //显示正确选项
            if (dataItems.get(position).getCorrectAnswer().equals("1")) {
                holder.ivA.setImageResource(R.mipmap.ic_practice_test_right);
                holder.tvA.setTextColor(Color.parseColor("#61bc31"));
            } else if (dataItems.get(position).getCorrectAnswer().equals("2")) {
                holder.ivB.setImageResource(R.mipmap.ic_practice_test_right);
                holder.tvB.setTextColor(Color.parseColor("#61bc31"));
            } else if (dataItems.get(position).getCorrectAnswer().equals("3")) {
                holder.ivC.setImageResource(R.mipmap.ic_practice_test_right);
                holder.tvC.setTextColor(Color.parseColor("#61bc31"));
            } else if (dataItems.get(position).getCorrectAnswer().equals("4")) {
                holder.ivD.setImageResource(R.mipmap.ic_practice_test_right);
                holder.tvD.setTextColor(Color.parseColor("#61bc31"));
            }
        }
    }

    private void SaveErrorQuestion(String topicId, String youranswer) {
        try {
            Question errorquestion=dbManager.selector(Question.class).where("topicId","=",topicId).findFirst();
            errorquestion.setError(youranswer);
            dbManager.update(errorquestion);
            T.show(mContext, "已添加到错题本");
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    private void saveprogress(int position,String youranswer) {
        try {
            QuestionPosition questionPosition=dbManager.findById(QuestionPosition.class,4);
            questionPosition.setPosition(position);
            dbManager.update(questionPosition);
            RealTest realTest=dataItems.get(position);
            realTest.setId(position+1);
            realTest.setYourAnswer(youranswer);
            dbManager.update(realTest);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * @author 设置上一步和下一步按钮监听
     */
    class LinearOnClickListener implements OnClickListener {

        private int mPosition;
        private int mPosition1;
        private boolean mIsNext;
        private ViewHolder viewHolder;

        public LinearOnClickListener(int position, boolean mIsNext, int position1, ViewHolder viewHolder) {
            mPosition = position;
            mPosition1 = position1;
            this.viewHolder = viewHolder;
            this.mIsNext = mIsNext;
        }

        @Override
        public void onClick(View v) {
            if (mPosition == viewItems.size()) {
                mContext.uploadExamination(errortopicNum);
            } else {
                if (mPosition == -1) {
                    Toast.makeText(mContext, "已经是第一页", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    isNext = mIsNext;
                    mContext.setCurrentView(mPosition);
                }
            }
        }
    }

    @Override
    public int getCount() {
        if (viewItems == null)
            return 0;
        return viewItems.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    //错题数
    public int errorTopicNum() {
        if (errortopicNum != 0) {
            return errortopicNum;
        }
        return 0;
    }

    public class ViewHolder {
        TextView questionType;
        TextView question;
        ImageView img, collectimg;
        TextView collecttxt;
        LinearLayout previousBtn, nextBtn, collectBtn;
        TextView nextText;
        TextView totalText;
        ImageView nextImage;
        LinearLayout wrongLayout;
        TextView explaindetailTv;
        LinearLayout layoutA;
        LinearLayout layoutB;
        LinearLayout layoutC;
        LinearLayout layoutD;
        ImageView ivA;
        ImageView ivB;
        ImageView ivC;
        ImageView ivD;
        TextView tvA;
        TextView tvB;
        TextView tvC;
        TextView tvD;
        TextView multi_choice;
    }

    public boolean isExist(String path) {
        File file = new File(path);
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) return false;
        else return true;
    }

    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
