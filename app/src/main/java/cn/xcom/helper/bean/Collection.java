package cn.xcom.helper.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/29 0029.
 */
public class Collection implements Serializable {
    /**
     * id : 311
     * userid : 663
     * title : 手表
     * description : 测试
     * type : 3
     * object_id : 466
     * createtime : 1478076609
     * longitude : 119.65502152009266
     * latitude : 27.091081961262333
     * price : 0.01
     * goodspicture : [{"id":"1063","goodsid":"466","file":"yyy49151477982505492.jpg","time":null}]
     */

    private String id;
    private String userid;
    private String title;
    private String description;
    private String type;
    private String object_id;
    private String createtime;
    private String longitude;
    private String latitude;
    private String price;
    /**
     * id : 1063
     * goodsid : 466
     * file : yyy49151477982505492.jpg
     * time : null
     */

    private List<GoodspictureBean> goodspicture;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<GoodspictureBean> getGoodspicture() {
        return goodspicture;
    }

    public void setGoodspicture(List<GoodspictureBean> goodspicture) {
        this.goodspicture = goodspicture;
    }

    public static class GoodspictureBean {
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
}
