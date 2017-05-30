package cn.xcom.helper.bean;

import android.content.Context;
import java.io.Serializable;
import java.util.ArrayList;

import cn.xcom.helper.sp.UserSp;

/**
 * Created by zhuchongkun on 16/5/27.
 */
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 2L;
    private String userId;// 用户ID
    private String userPassword;// 用户密码
    private String userImg;// 用户头像
    private String userPhone;// 用户手机号
    private String userCode;// 验证码
    private String userName;// 姓名
    private String userGender;// 性别(0=>女 1=>男)
    private String userID;//身份证
    private String userAddress;//地址
    private String myReferral;//我的邀请码
    private String userCity;
    private String userRealName;
    private String userContactName;
    private String userContactPhone;
    private String userHandIDCard;
    private String userIDCard;
    private String userDrivingLicense;
    private ArrayList<SkillTagInfo> userSkillTags;


    public UserInfo() {

    }

    public UserInfo(Context context) {
        readData(context);
    }

    public void readData(Context context) {
        UserSp sp = new UserSp(context);
        sp.read(this);

    }

    public void writeData(Context context) {
        UserSp sp = new UserSp(context);
        sp.write(this);
    }

    public void clearData(Context context) {
        UserSp sp = new UserSp(context);
        sp.clear();
    }

    public void clearDataExceptPhone(Context context){
        UserSp sp=new UserSp(context);
        this.setUserPhone(sp.read().getUserPhone());
        sp.clear();
        sp.write(this);
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean isLogined(){
        return !this.getUserID().equals("");
    }

    public String getUserId() {
        if (userId==null){
            return "";
        }else if (userId.equals("null")){
            return "";
        }
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        if (userPassword==null){
            return "";
        }else if (userPassword.equals("null")){
            return "";
        }
        return userPassword;
    }

    public String getMyReferral() {
        if (myReferral==null){
            return "";
        }else if (myReferral.equals("null")){
            return "";
        }
        return myReferral;
    }

    public void setMyReferral(String myReferral) {
        this.myReferral = myReferral;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserCode() {
        if (userCode==null){
            return "";
        }else if (userCode.equals("null")){
            return "";
        }
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserPhone() {
        if (userPhone==null){
            return "";
        }else if (userPhone.equals("null")){
            return "";
        }
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserImg() {
        if (userImg==null){
            return "";
        }else if (userImg.equals("null")){
            return "";
        }
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserName() {
        if (userName==null){
            return "";
        }else if (userName.equals("null")){
            return "";
        }
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGender() {
        if (userGender==null){
            return "";
        }else if (userGender.equals("null")){
            return "";
        }
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserID() {
        if (userID==null){
            return "";
        }else if (userID.equals("null")){
            return "";
        }
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserAddress() {
        if (userAddress==null){
            return "";
        }else if (userAddress.equals("null")){
            return "";
        }
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }


    public String getUserCity() {
        if (userCity==null){
            return "";
        }else if (userCity.equals("null")) {
            return "";
        }
            return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserRealName() {
        if (userRealName==null){
            return "";
        }else if (userRealName.equals("null")) {
            return "";
        }
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getUserContactName() {
        if (userContactName==null){
            return "";
        }else if (userContactName.equals("null")) {
            return "";
        }
        return userContactName;
    }

    public void setUserContactName(String userContactName) {
        this.userContactName = userContactName;
    }

    public String getUserContactPhone() {
        if (userContactPhone==null){
            return "";
        }else if (userContactPhone.equals("null")) {
            return "";
        }
        return userContactPhone;
    }

    public void setUserContactPhone(String userContactPhone) {
        this.userContactPhone = userContactPhone;
    }

    public String getUserHandIDCard() {
        if (userHandIDCard==null){
            return "";
        }else if (userHandIDCard.equals("null")) {
            return "";
        }
        return userHandIDCard;
    }

    public void setUserHandIDCard(String userHandIDCard) {
        this.userHandIDCard = userHandIDCard;
    }

    public String getUserIDCard() {
        if (userIDCard==null){
            return "";
        }else if (userIDCard.equals("null")) {
            return "";
        }
        return userIDCard;
    }

    public void setUserIDCard(String userIDCard) {
        this.userIDCard = userIDCard;
    }

    public String getUserDrivingLicense() {
        if (userDrivingLicense==null){
            return "";
        }else if (userDrivingLicense.equals("null")) {
            return "";
        }
        return userDrivingLicense;
    }

    public void setUserDrivingLicense(String userDrivingLicense) {
        this.userDrivingLicense = userDrivingLicense;
    }

    public ArrayList<SkillTagInfo> getUserSkillTags() {
        if (userSkillTags==null){
            return new ArrayList<SkillTagInfo>();
        }else if (userSkillTags.equals("null")) {
            return new ArrayList<SkillTagInfo>();
        }
        return userSkillTags;
    }

    public void setUserSkillTags(ArrayList<SkillTagInfo> userSkillTags) {
        this.userSkillTags = userSkillTags;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId='" + userId + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userImg='" + userImg + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userCode='" + userCode + '\'' +
                ", userName='" + userName + '\'' +
                ", userGender='" + userGender + '\'' +
                ", userID='" + userID + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", userCity='" + userCity + '\'' +
                ", userRealName='" + userRealName + '\'' +
                ", userContactName='" + userContactName + '\'' +
                ", userContactPhone='" + userContactPhone + '\'' +
                ", userHandIDCard='" + userHandIDCard + '\'' +
                ", userIDCard='" + userIDCard + '\'' +
                ", userDrivingLicense='" + userDrivingLicense + '\'' +
                ", userSkillTags=" + userSkillTags +
                '}';
    }
}
