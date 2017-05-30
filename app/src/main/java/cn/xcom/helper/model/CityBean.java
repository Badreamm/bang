package cn.xcom.helper.model;


import java.util.List;

/**
 * Created by Administrator on 2017/4/11 0011.
 */

public class CityBean {

        /**
         * cityid : 1
         * name : 北京市
         * status : 1
         * latitude : 39.914555
         * longitude : 116.402343
         * haschild : 1
         * childlist : [{"cityid":"42","name":"朝阳区","status":"1","latitude":"39.925898","longitude":"116.443499","haschild":0},{"cityid":"402","name":"东城区","status":"1","latitude":"116.42281","longitude":"39.934855","haschild":0},{"cityid":"403","name":"西城区","status":"1","latitude":"39.918318","longitude":"116.372448","haschild":0},{"cityid":"404","name":"崇文区","status":"1","latitude":"","longitude":"","haschild":0},{"cityid":"405","name":"宣武区","status":"1","latitude":"","longitude":"","haschild":0},{"cityid":"406","name":"丰台区","status":"1","latitude":"39.865007","longitude":"116.292534","haschild":0},{"cityid":"407","name":"石景山区","status":"1","latitude":"39.911401","longitude":"116.229581","haschild":0},{"cityid":"408","name":"海淀区","status":"1","latitude":"39.966224","longitude":"116.304751","haschild":0},{"cityid":"409","name":"门头沟区","status":"1","latitude":"39.946313","longitude":"116.107986","haschild":0},{"cityid":"410","name":"房山区","status":"1","latitude":"39.754375","longitude":"116.149524","haschild":0},{"cityid":"411","name":"通州区","status":"1","latitude":"39.916381","longitude":"116.663211","haschild":0},{"cityid":"412","name":"顺义区","status":"1","latitude":"40.136607","longitude":"116.661055","haschild":0},{"cityid":"413","name":"昌平区","status":"1","latitude":"40.226898","longitude":"116.238061","haschild":0},{"cityid":"414","name":"大兴区","status":"1","latitude":"39.732821","longitude":"116.34805","haschild":0},{"cityid":"415","name":"怀柔区","status":"1","latitude":"40.32279","longitude":"116.638633","haschild":0},{"cityid":"416","name":"平谷区","status":"1","latitude":"","longitude":"","haschild":0},{"cityid":"417","name":"密云县","status":"1","latitude":"","longitude":"","haschild":0},{"cityid":"418","name":"延庆县","status":"1","latitude":"40.462472","longitude":"115.981649","haschild":0}]
         */

        private String cityid;
        private String name;
        private String status;
        private String latitude;
        private String longitude;
        private int haschild;
        private List<ChildlistBean> childlist;

        public String getCityid() {
            return cityid;
        }

        public void setCityid(String cityid) {
            this.cityid = cityid;
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

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public int getHaschild() {
            return haschild;
        }

        public void setHaschild(int haschild) {
            this.haschild = haschild;
        }

        public List<ChildlistBean> getChildlist() {
            return childlist;
        }

        public void setChildlist(List<ChildlistBean> childlist) {
            this.childlist = childlist;
        }

        public static class ChildlistBean {
            /**
             * cityid : 42
             * name : 朝阳区
             * status : 1
             * latitude : 39.925898
             * longitude : 116.443499
             * haschild : 0
             */

            private String cityid;
            private String name;
            private String status;
            private String latitude;
            private String longitude;
            private int haschild;

            public String getCityid() {
                return cityid;
            }

            public void setCityid(String cityid) {
                this.cityid = cityid;
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

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public int getHaschild() {
                return haschild;
            }

            public void setHaschild(int haschild) {
                this.haschild = haschild;
            }
        }

}