package com.demo.yiman.ui.joke;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.demo.yiman.R;
import com.demo.yiman.base.BaseFragment;
import com.demo.yiman.bean.JokeModle;
import com.demo.yiman.widget.CustomLoadMoreView;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

@SuppressLint("ValidFragment")
public class JokeDetailFragment extends BaseFragment<JokePresenter> implements JokeView{
    public static final String TYPE="type";
    private BaseQuickAdapter mAdapter;

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptrClaFrameLayout)
    PtrFrameLayout mPtrFrameLayout;

    private int pageNum = 1;
    private int count = 20;
    private String type;
    public JokeDetailFragment(BaseQuickAdapter adapter){
        this.mAdapter= adapter;
    }
    public static JokeDetailFragment newInstance(String type, BaseQuickAdapter baseQuickAdapter){
        Bundle args = new Bundle();
        args.putCharSequence(TYPE,type);
        JokeDetailFragment fragment = new JokeDetailFragment(baseQuickAdapter);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected JokePresenter createPresenter() {
        return new JokePresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_joke_detail;
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        super.bindView(view, savedInstanceState);
        mPtrFrameLayout.disableWhenHorizontalMove(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,mRecyclerView,header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageNum = 1;
                mPresenter.getData(pageNum,count,type);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setPreLoadNumber(1);
//        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getData(pageNum,count,type);
            }
        }, mRecyclerView);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if (type.equals(JanDanApi.TYPE_FRESH)){
//                    ReadActivity.launch(getActivity(), (FreshNewsBean.PostsBean) adapter.getItem(position));
//                }
                showToast(position+"");
            }
        });
    }
     @Override
    public void initData() {
        super.initData();
        if (getArguments() == null){
            return;
        }
        type = getArguments().getString(TYPE);
        mPresenter.getData(pageNum,count,type);
    }

    @Override
    public void onJokeSucc(JokeModle jokeModle) {

        if (jokeModle == null || jokeModle.getResult().size() == 0){
            //showFaild();
            mPtrFrameLayout.refreshComplete();
        }else {
            pageNum++;
            mAdapter.setNewData(jokeModle.getResult());
            mPtrFrameLayout.refreshComplete();
        }
    }

    @Override
    public void loadMoreData(String type, JokeModle jokeModle) {
        if (jokeModle ==null){
            mAdapter.loadMoreFail();
        }else{
            pageNum++;
            mAdapter.addData(jokeModle.getResult());
            mAdapter.loadMoreComplete();
        }
    }
}
