package cn.xcom.helper.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/30 0030.
 */
public class MyApplyTask implements Serializable{

    /**
     * id : 116
     * taskid : 178
     * order_num : T2016093099140
     * userid : 614
     * name : pppp
     * phone : 15506584550
     * photo : avatar20160918181355.png
     * title : 测试推送用，
     * type : 22
     * price : 0.01
     * description : 跑腿
     * address : 鲁东大学幼儿园
     * longitude : 121.365383928143
     * latitude : 37.522983540995
     * saddress : 鲁东大学幼儿园
     * slatitude : 37.522983540995
     * slongitude : 121.365383928143
     * expirydate : 1475389680
     * paytype : weixin
     * paystatus : 1
     * time : 1475216942
     * state : 2
     * apply_uid : 610
     * apply_name : 朱崇乾
     * apply_phone : 18653503680
     * apply_time : 1475217787
     * status : 1
     * apply_photo : 1474536152926.jpg
     * idcard :
     * files : [{"id":"19","taskid":"116","file":"avatar20160924150156607665456740.png","type":"1","time":null},{"id":"20","taskid":"116","file":"avatar201609241501576073732055319.png","type":"1","time":null}]
     * evaluate : null
     * commentlist : []
     */

    private String id;
    private String taskid;
    private String order_num;
    private String userid;
    private String name;
    private String phone;
    private String photo;
    private String title;
    private String type;
    private String price;
    private String description;
    private String address;
    private String longitude;
    private String latitude;
    private String saddress;
    private String slatitude;
    private String slongitude;
    private String expirydate;
    private String paytype;
    private String paystatus;
    private String time;
    private String state;
    private String apply_uid;
    private String apply_name;
    private String apply_phone;
    private String apply_time;
    private String status;
    private String apply_photo;
    private String idcard;
    private String ishire;
    private Object evaluate;
    /**
     * id : 19
     * taskid : 116
     * file : avatar20160924150156607665456740.png
     * type : 1
     * time : null
     */

    private List<FilesBean> files;
    private List<?> commentlist;

    public String getIshire() {
        return ishire;
    }

    public void setIshire(String ishire) {
        this.ishire = ishire;
    }

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

    public String getOrder_num() {
        return order_num;
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

    public String getApply_uid() {
        return apply_uid;
    }

    public void setApply_uid(String apply_uid) {
        this.apply_uid = apply_uid;
    }

    public String getApply_name() {
        return apply_name;
    }

    public void setApply_name(String apply_name) {
        this.apply_name = apply_name;
    }

    public String getApply_phone() {
        return apply_phone;
    }

    public void setApply_phone(String apply_phone) {
        this.apply_phone = apply_phone;
    }

    public String getApply_time() {
        return apply_time;
    }

    public void setApply_time(String apply_time) {
        this.apply_time = apply_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApply_photo() {
        return apply_photo;
    }

    public void setApply_photo(String apply_photo) {
        this.apply_photo = apply_photo;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public Object getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(Object evaluate) {
        this.evaluate = evaluate;
    }

    public List<FilesBean> getFiles() {
        return files;
    }

    public void setFiles(List<FilesBean> files) {
        this.files = files;
    }

    public List<?> getCommentlist() {
        return commentlist;
    }

    public void setCommentlist(List<?> commentlist) {
        this.commentlist = commentlist;
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
