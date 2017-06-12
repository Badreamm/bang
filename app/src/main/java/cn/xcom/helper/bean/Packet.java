package cn.xcom.helper.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hzh on 2017/6/12.
 * 红包
 */

/*
*         "id":"67",
        "mid":"6295",
        "userid":"1590",
        "order_num":"W2017061154929",
        "paytype":"支付宝",
        "paystatus":"1",
        "draw_money":"",
        "money":"10",
        "count":"10",
        "create_time":"1497194105",
        "name":"汪汪",
        "phone":"15506584550",
        "photo":"avatar20170302184821.png",
        "ispacket":[
            {
                "name":"汪汪",
                "phone":"15506584550",
                "money":"0.46",
                "create_time":"1497194157"
            }
        ],
        "left_money":9.54,
        "state":"3"*/

public class Packet implements Serializable{
    private String id;
    private String mid;
    private String userid;
    private String order_num;
    private String paytype;
    private String paystatus;
    private String draw_money;
    private String money;
    private String count;
    private String create_time;
    private String name;
    private String phone;
    private String photo;
    private String left_money;
    private String state;
    private List<PacketRecord> ispacket;//红包记录

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

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
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

    public String getDraw_money() {
        return draw_money;
    }

    public void setDraw_money(String draw_money) {
        this.draw_money = draw_money;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
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

    public String getLeft_money() {
        return left_money;
    }

    public void setLeft_money(String left_money) {
        this.left_money = left_money;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<PacketRecord> getIspacket() {
        return ispacket;
    }

    public void setIspacket(List<PacketRecord> ispacket) {
        this.ispacket = ispacket;
    }
}
