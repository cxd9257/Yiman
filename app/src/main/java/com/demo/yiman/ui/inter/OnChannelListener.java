package com.demo.yiman.ui.inter;

public interface OnChannelListener {
    void onItemMove(int startPos,int endPos);
    void onMoveToMyChannel(int startPos,int endPos);
    void onMoveToOtherChannel(int starPos, int endPos);
    void onFinish(String selectedChannelName);
}
