package cn.xcom.helper.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/24 0024.
 */
public class TaskInfo implements Serializable{

    /**
     * id : 112
     * order_num : T2016092368331
     * userid : 612
     * title : 丽测试
     * type : 22
     * price : 0.11
     * unit : null
     * description : 跑腿
     * address : 国美
     * longitude : 119.654730680389
     * latitude : 27.0908758079152
     * saddress : 国美
     * slatitude : 27.0908758079152
     * slongitude : 119.654730680389
     * expirydate : 1474811820
     * paytype : weixin
     * paytime : 1474639065
     * paystatus : 1
     * time : 1474639032
     * state : 5
     * hot : 1
     * name : 丽
     * status : 2
     * phone : 13515067861
     * typename : 送外卖
     * type_parentid : 12
     * type_parentname : 跑腿
     * files : [{"id":"16","taskid":"112","file":"avatar201609232157116123079009150.png","type":"1","time":null}]
     */

    private String id;
    private String order_num;
    private String userid;
    private String title;
    private String type;
    private String price;
    private Object unit;
    private String description;
    private String address;
    private String longitude;
    private String latitude;
    private String saddress;
    private String slatitude;
    private String slongitude;
    private String expirydate;
    private String paytype;
    private String paytime;
    private String paystatus;
    private String time;
    private String state;
    private String hot;
    private String name;
    private String status;
    private String phone;
    private String photo;
    private String typename;
    private String type_parentid;
    private String type_parentname;
    private String sound;
    private String soundtime;
    private String ishire;

    public String getIshire() {
        return ishire;
    }

    public void setIshire(String ishire) {
        this.ishire = ishire;
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

    /**
     * id : 16
     * taskid : 112
     * file : avatar201609232157116123079009150.png
     * type : 1
     * time : null
     */

    private List<FilesBean> files;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_num() {
        return order_num;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
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

    public Object getUnit() {
        return unit;
    }

    public void setUnit(Object unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getSaddress() {
        return saddress;
    }

    public void setSaddress(String saddress) {
        this.saddress = saddress;
    }

    public String getSlatitude() {
        return slatitude;
    }

    public void setSlatitude(String slatitude) {
        this.slatitude = slatitude;
    }

    public String getSlongitude() {
        return slongitude;
    }

    public void setSlongitude(String slongitude) {
        this.slongitude = slongitude;
    }

    public String getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(String expirydate) {
        this.expirydate = expirydate;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getPaystatus() {
        return paystatus;
    }

    public void setPaystatus(String paystatus) {
        this.paystatus = paystatus;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getType_parentid() {
        return type_parentid;
    }

    public void setType_parentid(String type_parentid) {
        this.type_parentid = type_parentid;
    }

    public String getType_parentname() {
        return type_parentname;
    }

    public void setType_parentname(String type_parentname) {
        this.type_parentname = type_parentname;
    }

    public List<FilesBean> getFiles() {
        return files;
    }

    public void setFiles(List<FilesBean> files) {
        this.files = files;
    }

    public static class FilesBean implements Serializable{
        private String id;
        private String taskid;
        private String file;
        private String type;
        private Object time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTaskid() {
            return taskid;
        }

        public void setTaskid(String taskid) {
            this.taskid = taskid;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getTime() {
            return time;
        }

        public void setTime(Object time) {
            this.time = time;
        }
    }
}
