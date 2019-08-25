package com.demo.yiman.ui.image;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.demo.yiman.R;
import com.demo.yiman.base.BaseFragment;
import com.demo.yiman.bean.ImageModle;
import com.demo.yiman.ui.adapter.ImageAdapter;
import com.demo.yiman.ui.joke.ImageBrowseActivity;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

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
public class ImageFragment extends BaseFragment<ImagePresenter> implements ImageViews {
    @BindView(R.id.ptrClaFrameLayout)
    PtrClassicFrameLayout mPtrClassicFrameLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.banner)
    MZBannerView mBanner;

//    @BindView(R.id.toolbar)
//    Toolbar mToolbar;
//    @BindView(R.id.tv_title)
//    TextView mTitle;
    private ImageAdapter mAdapter;
    private List<ImageModle.ResultsBean> mList = new ArrayList<>();;
    private int count=10;
    private int page=1;
    public static final int[]RESULT = new int[]{R.mipmap.image1,R.mipmap.image2,R.mipmap.image3,R.mipmap.image4,R.mipmap.image5};

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
        //initToolbar();//暂时没有对Toolbar进行封装
        mzBanner();
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

//    private void initToolbar() {
//        AppCompatActivity mAppCompatActivity = (AppCompatActivity) mContext;
//        mAppCompatActivity.setSupportActionBar(mToolbar);
//        mToolbar.setTitle("");
//        mTitle.setTextColor(Color.WHITE);
//        mTitle.setText(getResources().getString(R.string.title_image));
//    }
    private void mzBanner(){
        List<Integer> imgList = new ArrayList<>();
        for (int i =0; i<RESULT.length; i++){
            imgList.add(RESULT[i]);
        }
        mBanner.setPages(imgList, new MZHolderCreator<BannerViewHolder>() {

            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });

    }
    public static class BannerViewHolder implements MZViewHolder<Integer> {
        private ImageView mImageView;
        @Override
        public View createView(Context context) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.item_banner,null);
            mImageView = view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, Integer data) {
            // 数据绑定
            mImageView.setImageResource(data);
        }
    }
    @Override
    public void initData() {
        super.initData();
        mPresenter.getImageData(count,page);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBanner.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBanner.start();
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
            page++; //开启服务去计算图片宽高。
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
            mList.addAll(imageData);
            setData(imageData);
}
    private void setData(List<ImageModle.ResultsBean> data){
        int count = data.size();
        if (page>1) {
            if (data.size() == 0) {
                mAdapter.loadMoreFail();
            } else {
                mAdapter.addData(data);
            }
        } else {
            mAdapter.setNewData(data);
            mPtrClassicFrameLayout.refreshComplete();
        }

        if (count < 10) { //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd(!isLoadMore);
        } else {
            mAdapter.loadMoreComplete();
            mPtrClassicFrameLayout.refreshComplete();
        }
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

}
