package com.demo.yiman.database;

import com.demo.yiman.bean.Channel;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.SaveCallback;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.util.ArrayList;
import java.util.List;

public class ChannelDao {
    public static List<Channel> getAllChannels(){
        return DataSupport.findAll(Channel.class);
    }


    /**
     * 保存频道
     */

    public static  void saveChannels(final List<Channel> channels){
        if (channels ==null)
            return;
        if (channels.size()>0){
            final List<Channel> channelList = new ArrayList<>();
            channelList.addAll(channels);
            DataSupport.deleteAllAsync(Channel.class).listen(new UpdateOrDeleteCallback() {
                @Override
                public void onFinish(int rowsAffected) {
                    DataSupport.markAsDeleted(channelList);
                    DataSupport.saveAllAsync(channelList).listen(new SaveCallback() {
                        @Override
                        public void onFinish(boolean success) {

                        }
                    });
                }
            });
        }
    }
}
