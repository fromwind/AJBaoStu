package com.udit.aijiabao.activitys;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udit.aijiabao.BaseActivity;
import com.udit.aijiabao.MyApplication;
import com.udit.aijiabao.R;
import com.udit.aijiabao.User;
import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.configs.UrlConfig;
import com.udit.aijiabao.dialog.LoadDialog;
import com.udit.aijiabao.entitys.MessageEntity;
import com.udit.aijiabao.entitys.RespMessageInfo;
import com.udit.aijiabao.utils.PreferencesUtils;
import com.udit.aijiabao.widgets.TitleView;

import org.xutils.DbManager;
import org.xutils.common.Callback;

import org.xutils.common.util.KeyValue;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.security.Key;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */

@ContentView(R.layout.activity_mine_message)
public class MineMessageActivity extends BaseActivity implements ExpandableListView
        .OnChildClickListener {
    final int CONTEXT_MENU_CHILD_DELETETHIS = 0;//添加上下文菜单时每一个菜单项的item ID
    final int CONTEXT_MENU_CHILD_DELETEALL = 1;
    final int CONTEXT_MENU_CHILD_DELETEREAD = 2;
    final int CONTEXT_MENU_CHILD_DELETEOUTDATE = 6;
    final int CONTEXT_MENU_GROUP_DELETEALL = 3;
    final int CONTEXT_MENU_GROUP_DELETEREAD = 4;
    final int CONTEXT_MENU_GROUP_DELETEOUTDATE = 5;
    private final String TAG = MineMessageActivity.class.getSimpleName();
    @ViewInject(R.id.mtitleView)
    private TitleView mtitleView;
    @ViewInject(R.id.expandlistview)
    private ExpandableListView expandableListView;
    private MessageAdapters adapters;
    private List<RespMessageInfo> groupList;
    private List<MessageEntity> mList;
    DbManager db;

    @Override
    public void setContentView() {
        groupList = new ArrayList<>();
        groupList.add(new RespMessageInfo("预约消息(0)", null));
        groupList.add(new RespMessageInfo("好友消息(0)", null));
        groupList.add(new RespMessageInfo("其他消息(0)", null));
        db = x.getDb(((MyApplication) getApplication()).getDaoConfig());
    }

    @Override
    public void initView() {
        mtitleView.setTitleLayoutColor(getResources().getColor(R.color.blue_title));
        mtitleView.setTitleText("我的消息");
        mtitleView.hideOperate();
        mtitleView.showBack(true);
        mtitleView.setTitleTextColor(getResources().getColor(R.color.white));
        registerForContextMenu(expandableListView);//给ExpandListView添加上下文菜单
    }

    /*
     * 添加上下文菜单
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo)
                menuInfo;
        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            menu.setHeaderTitle("菜单");
            menu.add(0, CONTEXT_MENU_GROUP_DELETEALL, 0, "清空所有信息");
            menu.add(0, CONTEXT_MENU_GROUP_DELETEREAD, 0, "清空全部已读信息");
            menu.add(0, CONTEXT_MENU_GROUP_DELETEOUTDATE, 0, "清空过期提醒信息");
        }
        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            menu.setHeaderTitle("菜单");
            menu.add(1, CONTEXT_MENU_CHILD_DELETETHIS, 0, "删除此信息");
            menu.add(1, CONTEXT_MENU_CHILD_DELETEREAD, 0, "删除已读信息");
            menu.add(1, CONTEXT_MENU_CHILD_DELETEALL, 0, "删除此类所有信息");
            menu.add(1, CONTEXT_MENU_CHILD_DELETEOUTDATE, 0, "删除全部过期信息");
        }

    }

    /*
     * 每个菜单项的具体点击事件
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        LoadDialog.showLoad(this, "正在删除，请稍后！", null);
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo)
                item.getMenuInfo();

        String strItem = String.valueOf(info.id);//为被选中的item的id
        Log.e("Bum_item", strItem);
        switch (item.getItemId()) {
            case CONTEXT_MENU_CHILD_DELETETHIS:
                Toast.makeText(this, "删除此信息", Toast.LENGTH_SHORT).show();
                deletethis((int) info.id);
                break;
            case CONTEXT_MENU_CHILD_DELETEREAD:
                Toast.makeText(this, "删除已读信息", Toast.LENGTH_SHORT).show();
                deleteRead();
                break;
            case CONTEXT_MENU_CHILD_DELETEALL:
                Toast.makeText(this, "删除所有信息", Toast.LENGTH_SHORT).show();
                deleteall();
                break;
            case CONTEXT_MENU_CHILD_DELETEOUTDATE:
                Toast.makeText(this, "删除过期信息", Toast.LENGTH_SHORT).show();
                deletOutDate();
                break;
            case CONTEXT_MENU_GROUP_DELETEREAD:
                Toast.makeText(this, "清空所有已读信息", Toast.LENGTH_SHORT).show();
                break;
            case CONTEXT_MENU_GROUP_DELETEALL:
                Toast.makeText(this, "清空所有信息", Toast.LENGTH_SHORT).show();
                break;
            case CONTEXT_MENU_GROUP_DELETEOUTDATE:
                Toast.makeText(this, "清空所有过期信息", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        LoadDialog.close();
        queryMessage();
        return super.onContextItemSelected(item);
    }

    @Override
    public void initData() {
        expandableListView.setOnChildClickListener(this);
        adapters = new MessageAdapters(this);
        expandableListView.setAdapter(adapters);
        adapters.setGroupList(groupList);
        adapters.notifyDataSetChanged();
        queryMessage();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
//变为已读
    public void doread(int a) {
        try {
            WhereBuilder whereBuilder = WhereBuilder.b();
            whereBuilder.and("notify_id", "=", a);//.or("notify_id","=","1");//.expr(" and mobile > '2015-12-29
            // 00:00:01' ");
            db.update(MessageEntity.class, whereBuilder, new KeyValue("read", true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//删除已读
    public void deleteRead() {
        //LoadDialog.showLoad(this, "正在删除，请稍后！", null);
        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and("read", "=", true).and("user","=",User.getLocalUserName());
        try {
            db.update(MessageEntity.class, whereBuilder, new KeyValue("deleted", true));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
//删除过期
    public void deletOutDate() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String now = sDateFormat.format(new java.util.Date());
        Log.e("Bum_time_now", now);
        List<MessageEntity> list = queryAll();
        for (MessageEntity msg : list) {
            String time = msg.getNotify_time();
            Log.e("Bum_time_time", time);
            java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
            java.util.Calendar c1 = java.util.Calendar.getInstance();
            java.util.Calendar c2 = java.util.Calendar.getInstance();
            try {
                c1.setTime(df.parse(now));
                c2.setTime(df.parse(time));
            } catch (java.text.ParseException e) {
                System.err.println("格式不正确");
            }
            int result = c1.compareTo(c2);
            if (result == 0)
                System.out.println("c1相等c2");
            else if (result < 0) System.out.println("c1大于c2");
            else {
                WhereBuilder whereBuilder = WhereBuilder.b();
                whereBuilder.and("notify_id", "=", msg.getNotify_id());
                try {
                    db.update(MessageEntity.class, whereBuilder, new KeyValue("deleted", true));
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
    }
//删除此条
    public void deletethis(int id) {
        //adapters.getChild(0,id).getNotify_id();
        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and("notify_id", "=", adapters.getChild(0, id).getNotify_id());
        try {
            db.update(MessageEntity.class, whereBuilder, new KeyValue("deleted", true));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
//删除所有
    public void deleteall() {
        //adapters.getChild(0,id).getNotify_id();
        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and("user","=",User.getLocalUserName());
        try {
            db.update(MessageEntity.class, whereBuilder, new KeyValue("deleted", true));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public List<MessageEntity> queryAll() {
        LoadDialog.showLoad(this, "正在加载，请稍后！", null);
        List<MessageEntity> all;
        all = null;
        try {
            all = db.selector(MessageEntity.class).where("deleted", "=", false).and("user","=",User.getLocalUserName()).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        LoadDialog.close();
        return all;
    }

    private void queryMessage() {
        List<MessageEntity> list = queryAll();
        if (list != null) {
            Collections.reverse(list);//倒序排列，使得最新的通知排在最前
            if (null == mList) {
                mList = new ArrayList<MessageEntity>();
            }
            mList.clear();
            mList.addAll(list);
            groupList.remove(0);
            groupList.add(0, new RespMessageInfo("预约消息(" + mList.size() + ")", mList));
            adapters.setGroupList(groupList);
            adapters.notifyDataSetChanged();
        } else Log.e("Bum_list_is_null", "无数据");
    }

    private int gposition, cposition;

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int
            childPosition, long id) {
        //去详情
        gposition = groupPosition;
        cposition = childPosition;
        if (null != groupList && null != groupList.get(groupPosition).getMessageList()) {
            MessageEntity messageEntity = groupList.get(gposition).getMessageList().get(cposition);
            //变更是否已读
            doread(messageEntity.getNotify_id());
            messageEntity.setRead(true);
            adapters.setGroupList(groupList);
            adapters.notifyDataSetChanged();
        }

        return true;
    }

    //Adapter适配
    public class MessageAdapters extends BaseExpandableListAdapter {
        private Context context;

        public MessageAdapters(Context context) {
            this.context = context;
        }

        private List<RespMessageInfo> groupList;

        public void setGroupList(List<RespMessageInfo> groupList) {
            this.groupList = groupList;
        }

        @Override
        public int getGroupCount() {
            if (null == groupList || groupList.size() == 0) {
                return 0;
            }
            return groupList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (null == groupList || null == groupList.get(groupPosition).getMessageList()) {
                return 0;
            }
            return groupList.get(groupPosition).getMessageList().size();
        }

        @Override
        public RespMessageInfo getGroup(int groupPosition) {
            return groupList.get(groupPosition);
        }

        @Override
        public MessageEntity getChild(int groupPosition, int childPosition) {
            return groupList.get(groupPosition).getMessageList().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                                 ViewGroup parent) {
            GroupHolder holder;
            if (null == convertView) {
                holder = new GroupHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.group_item, null);
                holder.title = (TextView) convertView.findViewById(R.id.group_title);
                holder.arrow = (ImageView) convertView.findViewById(R.id.image_arrow);
                holder.red = (TextView) convertView.findViewById(R.id.small_red_circle1);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }
            holder.title.setText(groupList.get(groupPosition).getTitle());
            if (isExpanded) {
                holder.arrow.setImageResource(R.mipmap.arrow_up);
            } else {
                holder.arrow.setImageResource(R.mipmap.arrow_down);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View
                convertView, ViewGroup parent) {
            ChildHolder holder;
            if (null == convertView) {
                holder = new ChildHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.child_item, null);
                holder.content = (TextView) convertView.findViewById(R.id.content);
                holder.state = (TextView) convertView.findViewById(R.id.state);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.sendmen = (TextView) convertView.findViewById(R.id.sendmen);
                holder.contenttongzhi = (TextView) convertView.findViewById(R.id.content_tongzhi);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            MessageEntity messageEntity = groupList.get(groupPosition).getMessageList().get
                    (childPosition);
            String a = messageEntity.getContent();
            int ss = a.indexOf("：", 0);
            int dd = a.indexOf("：", ss);
            int cc = a.indexOf("：", dd + 3);
            int ff = a.indexOf("：", cc + 1);
            int gg = a.indexOf("：", ff + 26);
            int tt = a.indexOf("：", gg + 4);
            /*Log.e("Bum01",a.substring(1,ss-1));
            Log.e("Bum02",a.substring(dd+1,dd+3));
            Log.e("Bum03",a.substring(cc+2,ff-5));
            Log.e("Bum04",a.substring(ff+2,gg-5));
            Log.e("Bum05",a.substring(gg+2,tt-4));
            Log.e("Bum06",a.substring(tt+2,a.length()-1));*/
            holder.content.setText("学员" + ":" + a.substring(cc + 2, ff - 5) + "        教练：" + a
                    .substring(gg + 2, tt - 4) + "\n" + "时间：" + a.substring(ff + 2, gg - 5) +
                    "\n" + "车牌号：" + a.substring(tt + 2, a.length() - 1));
            holder.contenttongzhi.setText(a.substring(1, ss - 1) + ":");
            holder.sendmen.setText(messageEntity.getMsg_type().equals("4") ? "系统通知" : "");
            holder.time.setText(messageEntity.getCreated_at());
            holder.state.setText(!messageEntity.isRead() ? "未读" : "已读");

            holder.state.setTextColor(!messageEntity.isRead() ? getResources().getColor(R.color
                    .main_red) : getResources().getColor(R.color.gray_60));
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        class GroupHolder {
            TextView title;
            ImageView arrow;
            TextView red;
        }

        class ChildHolder {
            TextView content;
            TextView contenttongzhi;
            TextView time;
            TextView sendmen;
            TextView state;
        }

    }

}
