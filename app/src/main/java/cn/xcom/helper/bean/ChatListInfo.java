package cn.xcom.helper.bean;

/**
 * Created by Administrator on 2016/10/21 0021.
 * 聊天列表信息
 */

public class ChatListInfo {
    private String id;//聊天信息的id
    private String uid;//用户的id
    private String chat_uid;//聊天对象的id
    private String last_content;
    private String status;
    private String last_chat_id;
    private String create_time;
    private String my_face;
    private String my_nickname;
    private String other_face;
    private String other_nickname;
    private String noreadcount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getChat_uid() {
        return chat_uid;
    }

    public void setChat_uid(String chat_uid) {
        this.chat_uid = chat_uid;
    }

    public String getLast_content() {
        return last_content;
    }

    public void setLast_content(String last_content) {
        this.last_content = last_content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLast_chat_id() {
        return last_chat_id;
    }

    public void setLast_chat_id(String last_chat_id) {
        this.last_chat_id = last_chat_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getMy_face() {
        return my_face;
    }

    public void setMy_face(String my_face) {
        this.my_face = my_face;
    }

    public String getMy_nickname() {
        return my_nickname;
    }

    public void setMy_nickname(String my_nickname) {
        this.my_nickname = my_nickname;
    }

    public String getOther_face() {
        return other_face;
    }

    public void setOther_face(String other_face) {
        this.other_face = other_face;
    }

    public String getOther_nickname() {
        return other_nickname;
    }

    public void setOther_nickname(String other_nickname) {
        this.other_nickname = other_nickname;
    }

    public String getNoreadcount() {
        return noreadcount;
    }

    public void setNoreadcount(String noreadcount) {
        this.noreadcount = noreadcount;
    }
}
