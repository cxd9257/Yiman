package com.demo.yiman.bean;

import java.io.Serializable;
import java.util.List;

public class JokeModle implements Serializable {

    /**
     * code : 200
     * message : 成功!
     * result : [{"sid":"29687038","text":"献身娱乐事业无法自拔...","type":"video","thumbnail":"http://wimg.spriteapp.cn/picture/2019/0728/5d3d6cc2e0127_wpd.jpg","video":"http://wvideo.spriteapp.cn/video/2019/0728/5d3d6cc2e0127_wpd.mp4","images":null,"up":"101","down":"8","forward":"1","comment":"11","uid":"20272202","name":"淼兔兔i","header":"http://wimg.spriteapp.cn/profile/large/2018/04/19/5ad85b1dc8973_mini.jpg","top_comments_content":"兄弟，本店新款，开档寿衣，欢迎品鉴","top_comments_voiceuri":"","top_comments_uid":"20748367","top_comments_name":"寿衣店老板","top_comments_header":"http://wimg.spriteapp.cn/profile/large/2019/07/15/5d2c38712a436_mini.jpg","passtime":"2019-07-30 02:58:01"},{"sid":"29687672","text":"【国漫混剪】国漫神话宇宙（哪吒 X 大圣 X 白蛇）","type":"video","thumbnail":"http://wimg.spriteapp.cn/picture/2019/0729/b7898d8eb1b011e9ac35842b2b4c75ab_wpd.jpg","video":"http://wvideo.spriteapp.cn/video/2019/0729/b7898d8eb1b011e9ac35842b2b4c75ab_wpd.mp4","images":null,"up":"143","down":"7","forward":"1","comment":"6","uid":"20746551","name":"温柔尝尽了吗","header":"http://wimg.spriteapp.cn/profile/large/2019/03/26/5c99f6e152ea2_mini.jpg","top_comments_content":"哪吒近5年来国漫巅峰，不接受任何反驳","top_comments_voiceuri":"","top_comments_uid":"19832171","top_comments_name":"解药x","top_comments_header":"http://wimg.spriteapp.cn/profile/large/2018/04/30/5ae6f9f6e3e51_mini.jpg","passtime":"2019-07-30 01:38:01"}]
     */

    private int code;
    private String message;
    private List<ResultBean> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * sid : 29687038
         * text : 献身娱乐事业无法自拔...
         * type : video
         * thumbnail : http://wimg.spriteapp.cn/picture/2019/0728/5d3d6cc2e0127_wpd.jpg
         * video : http://wvideo.spriteapp.cn/video/2019/0728/5d3d6cc2e0127_wpd.mp4
         * images : null
         * up : 101
         * down : 8
         * forward : 1
         * comment : 11
         * uid : 20272202
         * name : 淼兔兔i
         * header : http://wimg.spriteapp.cn/profile/large/2018/04/19/5ad85b1dc8973_mini.jpg
         * top_comments_content : 兄弟，本店新款，开档寿衣，欢迎品鉴
         * top_comments_voiceuri :
         * top_comments_uid : 20748367
         * top_comments_name : 寿衣店老板
         * top_comments_header : http://wimg.spriteapp.cn/profile/large/2019/07/15/5d2c38712a436_mini.jpg
         * passtime : 2019-07-30 02:58:01
         */

        private String sid;
        private String text;
        private String type;
        private String thumbnail;
        private String video;
        private Object images;
        private String up;
        private String down;
        private String forward;
        private String comment;
        private String uid;
        private String name;
        private String header;
        private String top_comments_content;
        private String top_comments_voiceuri;
        private String top_comments_uid;
        private String top_comments_name;
        private String top_comments_header;
        private String passtime;

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public Object getImages() {
            return images;
        }

        public void setImages(Object images) {
            this.images = images;
        }

        public String getUp() {
            return up;
        }

        public void setUp(String up) {
            this.up = up;
        }

        public String getDown() {
            return down;
        }

        public void setDown(String down) {
            this.down = down;
        }

        public String getForward() {
            return forward;
        }

        public void setForward(String forward) {
            this.forward = forward;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getTop_comments_content() {
            return top_comments_content;
        }

        public void setTop_comments_content(String top_comments_content) {
            this.top_comments_content = top_comments_content;
        }

        public String getTop_comments_voiceuri() {
            return top_comments_voiceuri;
        }

        public void setTop_comments_voiceuri(String top_comments_voiceuri) {
            this.top_comments_voiceuri = top_comments_voiceuri;
        }

        public String getTop_comments_uid() {
            return top_comments_uid;
        }

        public void setTop_comments_uid(String top_comments_uid) {
            this.top_comments_uid = top_comments_uid;
        }

        public String getTop_comments_name() {
            return top_comments_name;
        }

        public void setTop_comments_name(String top_comments_name) {
            this.top_comments_name = top_comments_name;
        }

        public String getTop_comments_header() {
            return top_comments_header;
        }

        public void setTop_comments_header(String top_comments_header) {
            this.top_comments_header = top_comments_header;
        }

        public String getPasstime() {
            return passtime;
        }

        public void setPasstime(String passtime) {
            this.passtime = passtime;
        }
    }
}