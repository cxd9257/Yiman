package com.demo.yiman.ui.image;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.demo.yiman.R;
import com.demo.yiman.base.BaseFragment;
import com.demo.yiman.bean.ImageModle;
import com.demo.yiman.ui.adapter.ImageAdapter;
import com.demo.yiman.ui.joke.ImageBrowseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 正常情况应该要后端同学把图片宽高一同下发
 * 前端就不需要去计算图片宽 耗时而影响用户体验了
 * @param
 */
public class ImageFragment extends BaseFragment<ImagePresenter> implements ImageView {
    @BindView(R.id.ptrClaFrameLayout)
    PtrClassicFrameLayout mPtrClassicFrameLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private List<ImageModle.ResultsBean> mList = new ArrayList<>();;
    private int count=10;
    private int page=1;
    private boolean isRemoveHeaderView = false;
    private boolean isLoadMore;//是否是底部加载更多
    public static ImageFragment newInstance(){
        Bundle args = new Bundle();
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_image;
    }

    @Override
    protected ImagePresenter createPresenter() {
        return new ImagePresenter(this);
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
                isLoadMore = false;
                page = 1;
                mList.clear();
                mPresenter.getImageData(count,page);
            }
        });

        mAdapter = new ImageAdapter(mList);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isLoadMore =true;
                mPresenter.getImageData(count,page);
            }
        },mRecyclerView);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ImageBrowseActivity.launch(getActivity(), new String[]{mList.get(position).getUrl()},position);
            }
        });
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        super.initData();
        mPresenter.getImageData(count,page);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }


    @Override
    public void onImageSucc(ImageModle imageModle) {
        if (imageModle == null || imageModle.getResults().size() == 0){
            showError(String.valueOf(imageModle.isError()));
        }else {
            //开启服务去计算图片宽高。
            page++;
            ImageService.startService(mContext,imageModle,"aaa");
            //mAdapter.setNewData(iamgeModle.getResults());
        }
    }
    @Override
    public void loadMoreData(ImageModle imageModle) {
        page++;
        ImageService.startService(mContext,imageModle,"aaa");
        //mAdapter.setNewData(iamgeModle.getResults());
        mPtrClassicFrameLayout.refreshComplete();
    }


    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onEvent(List<ImageModle.ResultsBean> imageData){
        Log.e("加入幾條數據",imageData.size()+"");
            mList.addAll(imageData);
            setData(imageData);
}
    private void setData(List<ImageModle.ResultsBean> data){
        int count = data.size();
        if (page>1) {
            if (data.size() == 0) {
                mAdapter.loadMoreFail();
            } else {
                Log.e("xx","喜歡");

                mAdapter.addData(data);
            }
        } else {
            Log.e("xx","e喜歡");
            mAdapter.setNewData(data);
            mPtrClassicFrameLayout.refreshComplete();
        }

        if (count < 10) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd(!isLoadMore);
        } else {
            mAdapter.loadMoreComplete();
            mPtrClassicFrameLayout.refreshComplete();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

}
