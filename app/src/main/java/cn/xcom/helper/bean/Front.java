package cn.xcom.helper.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 尉鑫鑫 on 2016/9/13.
 */
public class Front implements Serializable {

    private String id;
    private String type;
    private String goodsname;
    private String username;
    private String phone;
    private String price;
    private String oprice;
    private Object picture;
    private String description;
    private String sellnumber;
    private String address;
    private String longitude;
    private String latitude;
    private String delivery;
    private List<PicturelistBean> picturelist;
    private List<CommentlistBean> commentlist;
    private int racking;//0正在上架 1已下架
    private int commentcount;//评论条数

    public Object getPicture() {
        return picture;
    }

    public int getCommentCount() {
        return commentcount;
    }

    public void setCommentCount(int commentCount) {
        this.commentcount = commentCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOprice() {
        return oprice;
    }

    public void setOprice(String oprice) {
        this.oprice = oprice;
    }


    public void setPicture(Object picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSellnumber() {
        return sellnumber;
    }

    public void setSellnumber(String sellnumber) {
        this.sellnumber = sellnumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public List<PicturelistBean> getPicturelist() {
        return picturelist;
    }

    public void setPicturelist(List<PicturelistBean> picturelist) {
        this.picturelist = picturelist;
    }

    public List<CommentlistBean> getCommentlist() {

        return commentlist;
    }

    public void setCommentlist(List<CommentlistBean> commentlist) {
        this.commentlist = commentlist;
    }

    public int getRacking() {
        return racking;
    }

    public void setRacking(int racking) {
        this.racking = racking;
    }

    public static class PicturelistBean implements Serializable {
        private String id;
        private String goodsid;
        private String file;
        private Object time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGoodsid() {
            return goodsid;
        }

        public void setGoodsid(String goodsid) {
            this.goodsid = goodsid;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public Object getTime() {
            return time;
        }

        public void setTime(Object time) {
            this.time = time;
        }
    }

    public static class CommentlistBean implements Serializable {
        private String id;
        private String content;
        private String add_time;
        private String userid;
        private String name;
        private Object photo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getPhoto() {
            return photo;
        }

        public void setPhoto(Object photo) {
            this.photo = photo;
        }


    }


}
