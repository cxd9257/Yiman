package com.demo.yiman.net.api;

import com.demo.yiman.bean.ImageModle;
import com.demo.yiman.bean.JokeModle;
import com.demo.yiman.bean.NewsDetailModle;
import com.demo.yiman.book.BookModle;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 怎么使用动态URLs
 * 它只需要你加入一个单独的注解 @Url 在你的定义的结点
 * 如
 * @Post    //不能再拼接
 * Observable<BaseModel> bookByRx( @Url String xx,@Query("page") String page, @Query("count") String count);
 */
public interface ApiServer {
    @GET("/xx/{id}")
    Observable<BookModle> bookByRx(@Path("id") int id);
    /**
     * 获取新闻列表
     */
    @POST("/toutiao/index")
    Observable<NewsDetailModle> newsByRx(@Query("type") String id, @Query("key") String key);


    /**
     * 随机获取段子
     */
    @POST("https://api.apiopen.top/getJoke")
    Observable<JokeModle> getJokeDate(@Query("page") int page,@Query("count") int count,@Query("type") String type);

    /**
     * 获取福利图片
     */
    @GET("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/{count}/{page}")
    Observable<ImageModle> getImageDate(@Path("count") int count, @Path("page") int page);
}
