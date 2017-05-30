package cn.xcom.helper.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/25.
 */
public class PhotoWithPath implements Serializable {

    private String picname;
    private String picPath;

    public String getPicname() {
        return picname;
    }

    public void setPicname(String picname) {
        this.picname = picname;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
