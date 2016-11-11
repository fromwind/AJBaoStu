package com.udit.aijiabao.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.udit.aijiabao.R;
import com.udit.aijiabao.activitys.ActivityWeb;
import com.udit.aijiabao.activitys.VideoViewActivity;
import com.udit.aijiabao.activitys.project2_skillclass;
import com.udit.aijiabao.entitys.Movies;
import com.udit.aijiabao.madapters.LocalImageHolderView;
import com.udit.aijiabao.utils.T;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */
@ContentView(R.layout.fragment_subject_two)
public class SubjectTwoFragment extends Fragment {

    private final String TAG = SubjectTwoFragment.class.getSimpleName();

    private static SubjectTwoFragment twoFragment;

    @ViewInject(R.id.recycleView)
    private RecyclerView mRecyclerView;
    private SubjectAdapter adapter;
    private List<Movies> moviesList;
    public static SubjectTwoFragment newInstance() {
        twoFragment = new SubjectTwoFragment();
        return twoFragment; 
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        adapter = new SubjectAdapter(getActivity());

        mRecyclerView.setAdapter(adapter);

        List<Integer> localImages = new ArrayList<>();

        localImages.add(R.mipmap.banner1);
        localImages.add(R.mipmap.banner2);
        localImages.add(R.mipmap.banner3);
        localImages.add(R.mipmap.banner4);
        Movies movie1=new Movies("直角拐弯",R.mipmap.p1,"http://bmob-cdn-6792.b0.upaiyun.com/2016/11/09/1dff92d9400f9c2a8054e96bb28d1235.flv",false,"08:88");
        Movies movie2=new Movies("倒车入库",R.mipmap.p1,"http://bmob-cdn-6792.b0.upaiyun.com/2016/11/09/b842fa5d40004d6c80914b31aba3ebf7.flv",true,"05:88");
        Movies movie3=new Movies("侧方位停车",R.mipmap.p1,"http://bmob-cdn-6792.b0.upaiyun.com/2016/11/09/634c004840dedcf3804029ee8a6fffa3.flv",false,"18:88");
        Movies movie4=new Movies("坡道起步",R.mipmap.p1,"http://bmob-cdn-6792.b0.upaiyun.com/2016/11/09/557c9672406c424e8079833aef02b7c6.flv",false,"28:88");
        Movies movie5=new Movies("曲线行驶",R.mipmap.p1,"http://bmob-cdn-6792.b0.upaiyun.com/2016/11/09/de66500b4008bb66802e024b04c61b79.flv",false,"38:88");
        moviesList=new ArrayList<Movies>();
        moviesList.add(movie1);
        moviesList.add(movie2);
        moviesList.add(movie3);
        moviesList.add(movie4);
        moviesList.add(movie5);
        adapter.setLocalImages(localImages);
        adapter.setListMovies(moviesList);
        adapter.notifyDataSetChanged();
    }

    private class SubjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_HEADER = 1;
        private static final int TYPE_ITEM = 0;

        private Context context;
        private List<Integer> localImages;
        private ClickListener mClickListener;
        private List<Movies> moviesList;
        public void setLocalImages(List<Integer> localImages) {
            this.localImages = localImages;
        }
        public void setListMovies(List<Movies> moviesList){this.moviesList=moviesList;}
        public SubjectAdapter(Context context) {
            this.context = context;
            mClickListener = new ClickListener();
        }

        @Override
        public int getItemViewType(int position) {

            if (0 == position) {
                return TYPE_HEADER;
            } else {
                return TYPE_ITEM;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
                return new ItemHolder(view);
            } else if (viewType == TYPE_HEADER) {
                return new HeadHolder(LayoutInflater.from(context).inflate(R.layout.item_subject_head, null));
            }

            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ItemHolder) {
                ItemHolder itemHolder = (ItemHolder) holder;
                itemHolder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        T.show(context, position + "");
                        Intent appIntent = new Intent(getActivity(),VideoViewActivity.class);
                        appIntent.putExtra("url",moviesList.get(position-1).getPath());
                        startActivity(appIntent);
                    }
                });
                itemHolder.movie_name.setText(moviesList.get(position-1).getName());
                itemHolder.movie_downtxt.setText(moviesList.get(position-1).isLocal()?"已下载":"未下载");
                itemHolder.movie_downtxt.setTextColor(moviesList.get(position-1).isLocal()?getResources().getColor(R.color.green):getResources().getColor(R.color.red));
                itemHolder.img_proj2.setImageResource(moviesList.get(position-1).getImg());
            } else if (holder instanceof HeadHolder) {
                HeadHolder headHolder = (HeadHolder) holder;
                headHolder.banner.
                        setPages(
                                new CBViewHolderCreator<LocalImageHolderView>() {
                                    @Override
                                    public LocalImageHolderView createHolder() {
                                        return new LocalImageHolderView();
                                    }
                                }, localImages)
                                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                        .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                                //设置指示器的方向
                        .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);

                headHolder.banner.startTurning(5000);

                headHolder.banner.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        T.show(context,position+"");
                    }
                });
                headHolder.experience.setOnClickListener(mClickListener);
                headHolder.experienceTxt.setOnClickListener(mClickListener);
                headHolder.standard.setOnClickListener(mClickListener);
                headHolder.standardTxt.setOnClickListener(mClickListener);
                headHolder.technique.setOnClickListener(mClickListener);
                headHolder.techniqueTxt.setOnClickListener(mClickListener);
            }
        }

        class ClickListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.standard_layout:
                    case R.id.standard_text:
                        Intent intent=new Intent();
                        intent.putExtra("url","file:///android_asset/project2_standom.html");
                        intent.setClass(getActivity(),ActivityWeb.class);
                        startActivity(intent);
                        T.show(context,"标准");
                        break;

                    case R.id.experience_layout:
                    case R.id.experience_text:
                        T.show(context,"技巧");
                        startActivity(new Intent(getActivity(),project2_skillclass.class));
                        break;

                    case R.id.technique_layout:
                    case R.id.technique_text:

                        T.show(context,"预约");
                        break;
                }
            }
        }
        @Override
        public int getItemCount() {
            return moviesList.size()+1;
        }

        private class HeadHolder extends RecyclerView.ViewHolder {

            ConvenientBanner banner;

            LinearLayout standard, experience, technique;

            TextView standardTxt, experienceTxt, techniqueTxt;

            public HeadHolder(View itemView) {
                super(itemView);
                banner = (ConvenientBanner) itemView.findViewById(R.id.convenientBanner);

                initBanner();

                standard = (LinearLayout) itemView.findViewById(R.id.standard_layout);

                experience = (LinearLayout) itemView.findViewById(R.id.experience_layout);

                technique = (LinearLayout) itemView.findViewById(R.id.technique_layout);

                standardTxt = (TextView) itemView.findViewById(R.id.standard_text);

                experienceTxt = (TextView) itemView.findViewById(R.id.experience_text);

                techniqueTxt = (TextView) itemView.findViewById(R.id.technique_text);
            }

            private void initBanner(){
                DisplayMetrics metric = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
                int windowsHeight = metric.heightPixels;

                ViewGroup.LayoutParams params = banner.getLayoutParams();
                params.height = windowsHeight / 4;
                banner.setLayoutParams(params);
            }
        }

        private class ItemHolder extends RecyclerView.ViewHolder {
            LinearLayout item;
            ImageView img_proj2;
            ImageView movie_download;
            TextView movie_name,movie_downtxt,movie_big;
            public ItemHolder(View itemView) {
                super(itemView);
                item= (LinearLayout) itemView.findViewById(R.id.item_layout);
                img_proj2= (ImageView) itemView.findViewById(R.id.item_movie);
                movie_download= (ImageView) itemView.findViewById(R.id.item_movie_download);
                movie_name= (TextView) itemView.findViewById(R.id.item_movie_name);
                movie_downtxt= (TextView) itemView.findViewById(R.id.item_movie_downtxt);
                movie_big= (TextView) itemView.findViewById(R.id.item_movie_big);
            }
        }

    }

}
