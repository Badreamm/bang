package cn.xcom.helper.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/10/9 0009.
 */
public class Adress {

    /**
     * id : 53
     * userid : 610
     * address : 张三
     * longitude : 123.1232
     * latitude : 123.1232
     * default : 0
     * time : 1475979217
     */

    private String id;
    private String userid;
    private String address;
    private String longitude;
    private String latitude;
    @SerializedName("default")
    private String defaultX;
    private String time;

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

    public String getDefaultX() {
        return defaultX;
    }

    public void setDefaultX(String defaultX) {
        this.defaultX = defaultX;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
