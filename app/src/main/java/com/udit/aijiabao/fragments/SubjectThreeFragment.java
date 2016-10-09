package com.udit.aijiabao.fragments;

import android.content.Context;
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
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.udit.aijiabao.R;
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
public class SubjectThreeFragment extends Fragment {

    private final String TAG = SubjectThreeFragment.class.getSimpleName();

    private static SubjectThreeFragment threeFragment;


    @ViewInject(R.id.recycleView)
    private RecyclerView mRecyclerView;


    private SubjectAdapter adapter;

    public static SubjectThreeFragment newInstance() {
        threeFragment = new SubjectThreeFragment();
        return threeFragment;
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

        adapter.setLocalImages(localImages);

        adapter.notifyDataSetChanged();

    }

    private class SubjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_HEADER = 1;
        private static final int TYPE_ITEM = 0;

        private Context context;

        private List<Integer> localImages;

        private ClickListner clickListner;

        public void setLocalImages(List<Integer> localImages) {
            this.localImages = localImages;
        }

        public SubjectAdapter(Context context) {
            this.context = context;
            clickListner=new ClickListner();
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

                return new HeadHolder(LayoutInflater.from(context).inflate(R.layout.item_subject_head, parent,false));
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
                    }
                });


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

                headHolder.experience.setOnClickListener(clickListner);
                headHolder.experienceTxt.setOnClickListener(clickListner);
                headHolder.standard.setOnClickListener(clickListner);
                headHolder.standardTxt.setOnClickListener(clickListner);
                headHolder.technique.setOnClickListener(clickListner);
                headHolder.techniqueTxt.setOnClickListener(clickListner);

            }
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        class ClickListner implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.standard_layout:
                    case R.id.standard_text:

                        T.show(context,"标准");

                        break;

                    case R.id.experience_layout:
                    case R.id.experience_text:
                        T.show(context,"经验");

                        break;

                    case R.id.technique_layout:
                    case R.id.technique_text:

                        T.show(context,"技巧");
                        break;

                }
            }
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

            public ItemHolder(View itemView) {
                super(itemView);
                item= (LinearLayout) itemView.findViewById(R.id.item_layout);
            }
        }


    }

}