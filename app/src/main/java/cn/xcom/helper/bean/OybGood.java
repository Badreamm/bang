package cn.xcom.helper.bean;

import java.util.List;

/**
 * Created by hzh on 2017/7/8.
 * 一元购商品实例
 * "id":"1",
 * "title":"123",
 * "price":"123",
 * "smeta":[
 * {
 * "url":"20170707/595f889fdb05f.jpg",
 * "alt":"705-1406231J408-52"
 * },
 * {
 * "url":"20170707/595f88a4dc7b6.jpeg",
 * "alt":"20130512095153_WSu5Q"
 * }
 * ],
 * "create_time":"1499433127",
 * "count":"0",
 * "comments":[
 * <p>
 * ]
 */

public class OybGood {
    private String id;
    private String title;
    private String price;
    private long create_time;
    private String count;
    private String mark;
    private List<GoodImg> smeta;
    private long time;//开奖时间
    private String name;//获奖用户
    private String num;//幸运号码
    private String status;//0未完成 1已完成 2带揭晓
    private String prize_num;//我购买的号码
    private String phone;//中奖人的手机号
    private String iscomment;//中奖后是否评论
    private long countTime;//活动时间与当前时间的时间差
    private String showTime;//用于显示的倒计时时间

    //只在MyOrder页面使用
    private String myBuyCount;//我购买的次数
    private String saledCount;//已经卖出去的次数

    public String getMyBuyCount() {
        return myBuyCount;
    }

    public void setMyBuyCount(String myBuyCount) {
        this.myBuyCount = myBuyCount;
    }

    public String getSaledCount() {
        return saledCount;
    }

    public void setSaledCount(String saledCount) {
        this.saledCount = saledCount;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public long getCountTime() {
        return countTime;
    }

    public void setCountTime(long countTime) {
        this.countTime = countTime;
    }

    private List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getIscomment() {
        return iscomment;
    }

    public void setIscomment(String iscomment) {
        this.iscomment = iscomment;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrize_num() {
        return prize_num;
    }

    public void setPrize_num(String prize_num) {
        this.prize_num = prize_num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<GoodImg> getSmeta() {
        return smeta;
    }

    public void setSmeta(List<GoodImg> smeta) {
        this.smeta = smeta;
    }

    public String getMark() {
        return mark;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public class GoodImg {
        private String url;
        private String alt;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }
    }

    public class Comment{
        private String id;
        private String mid;
        private String userid;
        private String phone;
        private String content;
        private String address;
        private long create_time;
        private String photo;
        private String name;
        private List<CommentPic> pic;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public List<CommentPic> getPic() {
            return pic;
        }

        public void setPic(List<CommentPic> pic) {
            this.pic = pic;
        }
    }

    public class CommentPic{
        String pictureurl;

        public String getPictureurl() {
            return pictureurl;
        }

        public void setPictureurl(String pictureurl) {
            this.pictureurl = pictureurl;
        }
    }
}
