package com.demo.yiman.ui.news;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.demo.yiman.R;
import com.demo.yiman.base.BaseFragment;
import com.demo.yiman.base.baseMVP.BaseModel;
import com.demo.yiman.base.baseMVP.BaseView;
import com.demo.yiman.bean.NewsDataBean;
import com.demo.yiman.bean.NewsDetailModle;
import com.demo.yiman.database.YimanDbController;
import com.demo.yiman.ui.adapter.NewsDetailAdapter;
import com.demo.yiman.utils.NetworkUtil;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 聚合新闻API接口，因API没有提供分页功能
 * 故没做加载更多操作
 */
public class DetailFragment extends BaseFragment<DetailPresenter> implements DetailView {
    @BindView(R.id.ptrClaFrameLayout)
    PtrClassicFrameLayout mPtrClassicFrameLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_news_tips)
    TextView mNews_Tips;

    private String newsId;
    private int position;
    private List<NewsDataBean> beanList;
    private NewsDetailAdapter detailAdapter;
    private boolean isRemoveHeaderView = false;
    public static DetailFragment newInstance(String newsid,int position){
        Bundle args = new Bundle();
        args.putString("newsId",newsid);
        args.putInt("position",position);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_detail;
    }

    @Override
    protected DetailPresenter createPresenter() {
        return new DetailPresenter(this);
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        super.bindView(view, savedInstanceState);
        mPtrClassicFrameLayout.disableWhenHorizontalMove(true);
        mPtrClassicFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,mRecyclerView,header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isRemoveHeaderView = true;
                mPresenter.getNews(newsId);
            }
        });

        beanList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(detailAdapter = new NewsDetailAdapter(R.layout.item_detail_doc_slideimg,beanList));
        detailAdapter.setEnableLoadMore(true);
        detailAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewsDataBean itemBean = (NewsDataBean) adapter.getItem(position);
                goRead(itemBean.getUrl(),itemBean.getCategory());
            }
        });
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                NewsDataBean itemBean = (NewsDataBean) adapter.getItem(position);
                switch (view.getId()){
                    case R.id.iv_news_close:
                        showToast("关闭");
                        break;
                }
            }
        });
    }

    private void showNewsTips(int num, boolean isRefresh){
        if (isRefresh){
            mNews_Tips.setText(String.format(getResources().getString(R.string.news_tips),num+""));
        }else{
            mNews_Tips.setText("减少为您推送此类内容");
        }
        mNews_Tips.setVisibility(View.VISIBLE);
        ViewAnimator.animate(mNews_Tips)
                .newsPaper()
                .duration(1000)
                .start()
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        ViewAnimator.animate(mNews_Tips)
                                .bounceOut()
                                .duration(1000)
                                .start();
                    }
                });
    }

    private void goRead(String url,String source) {
        if (url == null){
            return;
        }
        NewsDetailActivity.launch(mContext,url,source);
    }
    @Override
    public void initData() {
        super.initData();
        if (getArguments() == null) return;
        newsId = getArguments().getString("newsId");
        position = getArguments().getInt("position");

        if (!NetworkUtil.isNetworkAvailable(mContext)){
            lodingData();
        }else{
            mPresenter.getNews(newsId);
        }
    }

    @Override
    public void onNewsSucc(NewsDetailModle newsDetailModle) {
        try {
            if (newsDetailModle == null){
                //showFaild();
                mPtrClassicFrameLayout.refreshComplete();
            }else {
                if (isRemoveHeaderView){
                    detailAdapter.removeAllHeaderView();
                }
                detailAdapter.setNewData(newsDetailModle.getResult().getData());
                mPtrClassicFrameLayout.refreshComplete();
                showNewsTips(newsDetailModle.getResult().getData().size(),true);
            }
            handleDataBase(newsDetailModle.getResult().getData()); //存储数据
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void showError(String msg) {
        super.showError(msg);
        mPtrClassicFrameLayout.refreshComplete();
    }

    private void lodingData(){  //读取本地
        beanList = YimanDbController.getInstance().queryAll();
        if (beanList == null) return;
        detailAdapter.setNewData(beanList);
    }
    private void handleDataBase(List<NewsDataBean> list){
        List<NewsDataBean> newList = new ArrayList<>();
        for (int i=0; i<list.size(); i++){
            NewsDataBean info = list.get(i);
            newList.add(info);
        }
        YimanDbController.getInstance().delete(newList);
        YimanDbController.getInstance().insertOrReplace(newList);
    }

}
