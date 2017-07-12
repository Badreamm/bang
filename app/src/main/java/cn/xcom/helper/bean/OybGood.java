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

}
