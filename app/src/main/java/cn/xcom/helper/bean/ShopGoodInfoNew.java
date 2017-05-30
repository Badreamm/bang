package cn.xcom.helper.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/1 0001.
 */
public class ShopGoodInfoNew implements Serializable {
    /**
     * id : 466
     * userid : 768
     * goodsname : 手表
     * type : 24
     * price : 0.01
     * oprice : 8
     * unit : 个
     * description : 测试
     * picture : null
     * sound :
     * showid : 0
     * address : 宁德市福安市国美电器(宁德福安店)
     * longitude : 119.65502152009266
     * latitude : 27.091081961262333
     * status : 1
     * racking : 0
     * delivery :
     * time : 1477982506
     * sellnumber : 1
     * name : 大大
     * phone : 18950580168
     * picturelist : [{"id":"1063","goodsid":"466","file":"yyy49151477982505492.jpg","time":null}]
     * commentlist : [{"id":"86","content":"好评","score":"5","add_time":"1477982764","userid":"736","name":"51帮 会所","photo":"avatar20161027213553.png"},{"id":"87","content":"好评","score":"5","add_time":"1477982825","userid":"736","name":"51帮 会所","photo":"avatar20161027213553.png"},{"id":"88","content":"好评","score":"5","add_time":"1477982891","userid":"736","name":"51帮 会所","photo":"avatar20161027213553.png"}]
     */

    private String id;
    private String userid;
    private String goodsname;
    private String type;
    private String price;
    private String oprice;
    private String unit;
    private String description;
    private Object picture;
    private String sound;
    private String showid;
    private String address;
    private String longitude;
    private String latitude;
    private String status;
    private String racking;
    private String delivery;
    private String time;
    private String sellnumber;
    private String name;
    private String phone;
    /**
     * id : 1063
     * goodsid : 466
     * file : yyy49151477982505492.jpg
     * time : null
     */

    private List<PicturelistBean> picturelist;
    /**
     * id : 86
     * content : 好评
     * score : 5
     * add_time : 1477982764
     * userid : 736
     * name : 51帮 会所
     * photo : avatar20161027213553.png
     */

    private List<CommentlistBean> commentlist;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getPicture() {
        return picture;
    }

    public void setPicture(Object picture) {
        this.picture = picture;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getShowid() {
        return showid;
    }

    public void setShowid(String showid) {
        this.showid = showid;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRacking() {
        return racking;
    }

    public void setRacking(String racking) {
        this.racking = racking;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSellnumber() {
        return sellnumber;
    }

    public void setSellnumber(String sellnumber) {
        this.sellnumber = sellnumber;
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
        private String score;
        private String add_time;
        private String userid;
        private String name;
        private String photo;

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

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }
}
