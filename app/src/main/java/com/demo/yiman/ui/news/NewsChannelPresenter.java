package com.demo.yiman.ui.news;

import com.demo.yiman.MyApp;
import com.demo.yiman.R;
import com.demo.yiman.base.baseMVP.BaseObserver;
import com.demo.yiman.base.baseMVP.BasePresenter;
import com.demo.yiman.bean.Channel;
import com.demo.yiman.bean.NewsDetailModle;
import com.demo.yiman.bean.NewsWeather;
import com.demo.yiman.database.ChannelDao;
import com.demo.yiman.net.ApiConfig;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class NewsChannelPresenter extends BasePresenter<NewsView> implements NewsContract{


    public NewsChannelPresenter(NewsView baseView) {
        super(baseView);
    }

    public void getChannel() {
        List<Channel> channelList;
        List<Channel> myChannels = new ArrayList<>();
        List<Channel> otherChannels = new ArrayList<>();
        channelList = ChannelDao.getAllChannels();
        if (channelList.size()<1){
            List<String> channelName = Arrays.asList(MyApp.getContext()
                    .getResources().getStringArray(R.array.news_channel_name));
            List<String> channelId = Arrays.asList(MyApp.getContext()
                    .getResources().getStringArray(R.array.news_channel_id));
            List<Channel> channels = new ArrayList<>();
            for (int i = 0; i<channelName.size(); i++){
                Channel channel = new Channel();
                channel.setChannelId(channelId.get(i));
                channel.setChannelName(channelName.get(i));
                channel.setChannelType(i<1?1:0);   //除了第1个不能移除外都可移除
                channel.setChannelSelect(i<channelId.size()-3);//除最后3个外都是默认选中
                if (i<channelId.size()-3){
                    myChannels.add(channel);  //选中的
                }else {
                    otherChannels.add(channel);  //待选的
                }
                channels.add(channel);
            }
            DataSupport.saveAllAsync(channels).listen(new SaveCallback() {
                @Override
                public void onFinish(boolean success) {

                }
            });
            channelList = new ArrayList<>();
            channelList.addAll(channels);
        }else{
            channelList = ChannelDao.getAllChannels();
            Iterator<Channel> iterator = channelList.iterator();
            while (iterator.hasNext()){
                Channel channel = iterator.next();
                if (!channel.isChannelSelect()){
                    otherChannels.add(channel);
                    iterator.remove();
                }
            }
            myChannels.addAll(channelList);
        }
        baseView.loadData(myChannels,otherChannels);
    }
    public void getNewsWeather(String city){
        addDisposable(apiServer.weatherByRx(city, ApiConfig.JuHeWeatherKey), new BaseObserver<NewsWeather>(baseView) {

            @Override
            protected void onSuccess(NewsWeather  newsWeatherModle) {
                baseView.loaWeatherdData(newsWeatherModle);
                baseView.hideLoading();
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
                baseView.hideLoading();
            }
        });

    }

}
