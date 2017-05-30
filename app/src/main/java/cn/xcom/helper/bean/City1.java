package cn.xcom.helper.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class City1 implements Parcelable{

    private String regionId;
    private String provinceCode;
    private String cityCode;
    private String districtCode;
    private String province;
    private String city;
    private String district;

    public String getRegionId() {
        return regionId;
    }
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvinceCode() {
        return provinceCode;
    }
    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }
    public String getCityCode() {
        return cityCode;
    }
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
    public String getDistrictCode() {
        return districtCode;
    }
    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }
    public static final Parcelable.Creator<City1> CREATOR = new Creator<City1>() {
        public City1 createFromParcel(Parcel source) {
            City1 mCity = new City1();
            mCity.regionId = source.readString();
            mCity.province = source.readString();
            mCity.city = source.readString();
            mCity.district = source.readString();
            mCity.provinceCode = source.readString();
            mCity.cityCode = source.readString();
            mCity.districtCode = source.readString();
            return mCity;
        }
        public City1[] newArray(int size) {
            return new City1[size];
        }
    };

    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(regionId);
        parcel.writeString(province);
        parcel.writeString(city);
        parcel.writeString(district);
        parcel.writeString(provinceCode);
        parcel.writeString(cityCode);
        parcel.writeString(districtCode);
    }
}
