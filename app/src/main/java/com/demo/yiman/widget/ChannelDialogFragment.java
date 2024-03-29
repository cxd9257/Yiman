package com.demo.yiman.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.demo.yiman.R;
import com.demo.yiman.bean.Channel;
import com.demo.yiman.event.NewChannelEvent;
import com.demo.yiman.event.SelectChannelEvent;
import com.demo.yiman.ui.adapter.NewsChannelAdapter;
import com.demo.yiman.ui.inter.ItemDragHelperCallBack;
import com.demo.yiman.ui.inter.OnChannelListener;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

public class ChannelDialogFragment extends DialogFragment implements OnChannelListener {
    private List<Channel> mDatas = new ArrayList<>();
    RecyclerView mRecyclerView;
    private ItemTouchHelper mHelper;
    private ImageView miVClose;
    private boolean isUpdate = false;
    private NewsChannelAdapter mAdapter;
    List<Channel> mSelectedDatas;
    List<Channel> mUnSelectedDatas;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,android.R.style.Theme_Black_NoTitleBar);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog!=null){
            dialog.getWindow().setWindowAnimations(R.style.dialogSlideAnim);
        }
        return inflater.inflate(R.layout.dialog_channel,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        miVClose = view.findViewById(R.id.icon_collapse);
        miVClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        processLoic();
    }
    private OnChannelListener onChannelListener;

    public void setOnChannelListener(OnChannelListener onChannelListener) {
        this.onChannelListener = onChannelListener;
    }


    public static ChannelDialogFragment newInstance(List<Channel> selectedDatas,List<Channel> unSelectedDatas){
        ChannelDialogFragment dialogFragment = new ChannelDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("dataSelected", (Serializable) selectedDatas);
        bundle.putSerializable("dataUnSelected", (Serializable) unSelectedDatas);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }
    private void setDataType(List<Channel> datas ,int type){
        for (int i = 0;i<datas.size();i++){
            datas.get(i).setItemtype(type);
        }
    }
    private void processLoic() {
        Channel channel = new Channel();
        channel.setItemtype(Channel.TYPE_MY);
        channel.setChannelName("我的频道");
        mDatas.add(channel);

        Bundle bundle = getArguments();
        mSelectedDatas = (List<Channel>) bundle.getSerializable("dataSelected");
        mUnSelectedDatas = (List<Channel>) bundle.getSerializable("dataUnSelected");
        setDataType(mSelectedDatas,Channel.TYPE_MY_CHANNEL);
        setDataType(mUnSelectedDatas,Channel.TYPE_OTHER_CHANNEL);
        mDatas.addAll(mSelectedDatas);

        Channel moreChannel = new Channel();
        moreChannel.setItemtype(Channel.TYPE_OTHER);
        moreChannel.setChannelName("频道推荐");
        mDatas.add(moreChannel);
        mDatas.addAll(mUnSelectedDatas);

        ItemDragHelperCallBack callBack = new ItemDragHelperCallBack(this);//触摸移动
        final  ItemTouchHelper helper = new ItemTouchHelper(callBack);
        helper.attachToRecyclerView(mRecyclerView);

        mAdapter = new NewsChannelAdapter(mDatas,helper);
        GridLayoutManager manager = new GridLayoutManager(getActivity(),4);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {//用显示单条或4条
            @Override
            public int getSpanSize(int i) {
                int itemViewType = mAdapter.getItemViewType(i);
                return itemViewType == Channel.TYPE_MY_CHANNEL || itemViewType == Channel.TYPE_OTHER_CHANNEL?1:4;
            }
        });
        mAdapter.OnChannelListener(this);
    }
    @OnClick(R.id.icon_collapse)
    public void onClick(View view){
        dismiss();
    }
    @Override
    public void onItemMove(int startPos, int endPos) {
        if (startPos < 0 || endPos < 0) return;
        if (mDatas.get(endPos).getChannelName().equals("头条")) return;
        //我的频道之间移动
        if (onChannelListener != null)
            onChannelListener.onItemMove(startPos - 1, endPos - 1);//去除标题所占的一个index
        onMove(startPos, endPos, false);
    }
    private String firstAddChannelName = "";


    private void onMove(int starPos, int endPos, boolean isAdd) {
        isUpdate = true;
        Channel startChannel = mDatas.get(starPos);
        //先删除之前的位置
        mDatas.remove(starPos);
        //添加到现在的位置
        mDatas.add(endPos, startChannel);
        mAdapter.notifyItemMoved(starPos, endPos);
        if (isAdd) {
            if (TextUtils.isEmpty(firstAddChannelName)) {
                firstAddChannelName = startChannel.getChannelName();
            }
        } else {
            if (startChannel.getChannelName().equals(firstAddChannelName)) {
                firstAddChannelName = "";
            }
        }
    }

    @Override
    public void onMoveToMyChannel(int starPos, int endPos) {
        onMove(starPos, endPos, true);
    }

    @Override
    public void onMoveToOtherChannel(int starPos, int endPos) {
        onMove(starPos, endPos, false);
    }


    @Override
    public void onFinish(String selectedChannelName) {
        EventBus.getDefault().post(new SelectChannelEvent(selectedChannelName));
        dismiss();
    }

    @Override
    public void onPause() {
        if (isUpdate) {
            EventBus.getDefault().post(new NewChannelEvent(mAdapter.getData(), firstAddChannelName));
        }
        super.onPause();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
