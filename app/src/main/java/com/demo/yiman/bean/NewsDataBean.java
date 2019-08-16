package com.demo.yiman.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public  class NewsDataBean implements Serializable, MultiItemEntity {
    /**
     * uniquekey : ff9dc82ef29d5e8b464fd8d7bcf469c1
     * title : 全球最大集装箱巨轮首航深圳盐田港区
     * date : 2019-07-23 18:19
     * category : 头条
     * author_name : 中国新闻网
     * url : http://mini.eastday.com/mobile/190723181915882.html
     * thumbnail_pic_s : http://02imgmini.eastday.com/mobile/20190723/20190723181915_cf003b9d0afe66b85c10fa131331bfd6_1_mwpm_03200403.jpg
     * thumbnail_pic_s02 : http://02imgmini.eastday.com/mobile/20190723/20190723181915_cf003b9d0afe66b85c10fa131331bfd6_2_mwpm_03200403.jpg
     * thumbnail_pic_s03 : http://09imgmini.eastday.com/mobile/20190723/20190723181636_dc8567a3c4451c502b4b5db1ff5e258e_7_mwpm_03200403.jpg
     */
    //显示形式多图
    public static final int TYPE_DOC_SLIDEIMG = 1;
    public static final int TYPE_DOC_SLIDEIMG1 = 2;
    private static final long serialVersionUID = -8045384665677175225L;
    @Id(autoincrement = true)
    private Long idSs;
    public NewsDataBean(int itemType) {
        this.itemType = itemType;
    }

    @Generated(hash = 1093784542)
    public NewsDataBean(Long idSs, int itemType, String uniquekey, String title, String date, String category, String author_name, String url,
            String thumbnail_pic_s, String thumbnail_pic_s02, String thumbnail_pic_s03) {
        this.idSs = idSs;
        this.itemType = itemType;
        this.uniquekey = uniquekey;
        this.title = title;
        this.date = date;
        this.category = category;
        this.author_name = author_name;
        this.url = url;
        this.thumbnail_pic_s = thumbnail_pic_s;
        this.thumbnail_pic_s02 = thumbnail_pic_s02;
        this.thumbnail_pic_s03 = thumbnail_pic_s03;
    }

    @Generated(hash = 207911213)
    public NewsDataBean() {
    }

    @Override
    public int getItemType() {
        return itemType;
    }
    public int itemType;
    private String uniquekey;
    private String title;
    private String date;
    private String category;
    private String author_name;
    private String url;
    private String thumbnail_pic_s;
    private String thumbnail_pic_s02;
    private String thumbnail_pic_s03;

    public String getUniquekey() {
        return uniquekey;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail_pic_s() {
        return thumbnail_pic_s;
    }

    public void setThumbnail_pic_s(String thumbnail_pic_s) {
        this.thumbnail_pic_s = thumbnail_pic_s;
    }

    public String getThumbnail_pic_s02() {
        return thumbnail_pic_s02;
    }

    public void setThumbnail_pic_s02(String thumbnail_pic_s02) {
        this.thumbnail_pic_s02 = thumbnail_pic_s02;
    }

    public String getThumbnail_pic_s03() {
        return thumbnail_pic_s03;
    }

    public void setThumbnail_pic_s03(String thumbnail_pic_s03) {
        this.thumbnail_pic_s03 = thumbnail_pic_s03;
    }

    public Long getIdSs() {
        return this.idSs;
    }

    public void setIdSs(Long idSs) {
        this.idSs = idSs;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }


}