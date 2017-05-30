package cn.xcom.helper.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/27 0027.
 */
public class TaskItemInfo implements Serializable {

    /**
     * id : 63
     * taskid : 52
     * order_num : T2016092091881
     * userid : 614
     * name : pppp
     * phone : 15506584550
     * photo : avatar20160918181355.png
     * title : 测试分类
     * type : 127
     * price : 0.02
     * description : 丽人
     * address : 上尧花园
     * longitude : 121.364556557646
     * latitude : 37.521475984054
     * saddress : 蝎子顶
     * slatitude : 37.5321789622071
     * slongitude : 121.382445066284
     * expirydate : 1474602240
     * paytype : weixin
     * paystatus : 1
     * time : 1474343064
     * state : 2
     * apply_uid : 614
     * apply_name : pppp
     * apply_phone : 15506584550
     * apply_time : 1474356190
     * status : 2
     * apply_photo : avatar20160918181355.png
     * idcard :
     * files : [{"id":"8","taskid":"63","file":"avatar201609202308506071405094201.png","type":"1","time":null}]
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
    private String status;
    private String idcard;
    private List<Evaluate> evaluate;
    private ApplyUser apply;
    private String sound;
    private String soundtime;


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

    public ApplyUser getApply() {
        return apply;
    }

    public void setApply(ApplyUser apply) {
        this.apply = apply;
    }

    private List<FilesBean> files;
    private List<?> commentlist;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
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

    public List<Evaluate> getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(List<Evaluate> evaluate) {
        this.evaluate = evaluate;
    }

    /**
     * id : 8
     * taskid : 63
     * file : avatar201609202308506071405094201.png
     * type : 1
     * time : null
     */
    public static class FilesBean implements Serializable {
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

    public class ApplyUser implements Serializable{
        private String userId;
        private String name;
        private String status;
        private String phone;
        private String photo;
        private String idCard;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }
    }

}
