package cn.xcom.helper.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hzh on 2017/6/12.
 * 红包记录
 *                 "name":"汪汪",
 "phone":"15506584550",
 "money":"0.46",
 "create_time":"1497194157"
 */

public class PacketRecord implements Serializable{
    private String name;
    private String phone;
    private String money;
    private long create_time;

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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "PacketRecord{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", money='" + money + '\'' +
                ", create_time=" + create_time +
                '}';
    }
}
