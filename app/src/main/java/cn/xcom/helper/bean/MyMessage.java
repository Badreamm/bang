package cn.xcom.helper.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class MyMessage {
    /**
     * mid : 3459
     * type : 1
     * userid : 2017
     * title : 15684152807
     * content : 广告费
     * sound :
     * soundtime :
     * video :
     * create_time : 1490143071
     * name : 安卓测试
     * phone : 15684152807
     * photo : avatar_man.png
     * pic : []
     * like : []
     * comment : []
     */

    private String mid;
    private String type;
    private String userid;
    private String title;
    private String content;
    private String sound;
    private String soundtime;
    private String video;
    private long create_time;
    private String name;
    private String phone;
    private String photo;
    private List<PicBean> pic;
    private List<?> like;
    private List<?> comment;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getSoundtime() {
        return soundtime;
    }

    public void setSoundtime(String soundtime) {
        this.soundtime = soundtime;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<PicBean> getPic() {
        return pic;
    }

    public void setPic(List<PicBean> pic) {
        this.pic = pic;
    }

    public List<?> getLike() {
        return like;
    }

    public void setLike(List<?> like) {
        this.like = like;
    }

    public List<?> getComment() {
        return comment;
    }

    public void setComment(List<?> comment) {
        this.comment = comment;
    }
    public static class PicBean implements Serializable {
        private String pictureurl;

        public String getPictureurl() {
            return pictureurl;
        }

        public void setPictureurl(String pictureurl) {
            this.pictureurl = pictureurl;
        }
    }
}
