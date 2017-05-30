package cn.xcom.helper.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/26 0026.
 */
public class TaskTypeAll {

    /**
     * id : 12
     * name : 跑腿
     * photo : null
     * parent : 0
     * listorder : 0
     * clist : [{"id":"21","name":"送快递","photo":null,"parent":"12","listorder":"0"},{"id":"22","name":"送外卖","photo":null,"parent":"12","listorder":"0"},{"id":"23","name":"送材料","photo":null,"parent":"12","listorder":"0"},{"id":"24","name":"送蔬菜","photo":null,"parent":"12","listorder":"0"},{"id":"25","name":"送蛋糕","photo":null,"parent":"12","listorder":"0"},{"id":"26","name":"送鲜花","photo":null,"parent":"12","listorder":"0"},{"id":"27","name":"送钥匙","photo":null,"parent":"12","listorder":"0"},{"id":"262","name":"超市代购","photo":null,"parent":"12","listorder":"0"},{"id":"263","name":"买化妆品","photo":null,"parent":"12","listorder":"0"},{"id":"269","name":"餐厅占座","photo":null,"parent":"12","listorder":"0"},{"id":"270","name":"教室占座","photo":null,"parent":"12","listorder":"0"},{"id":"314","name":"排队挂号","photo":null,"parent":"12","listorder":"0"},{"id":"88","name":"送水","photo":null,"parent":"12","listorder":"0"},{"id":"89","name":"送奶","photo":null,"parent":"12","listorder":"0"},{"id":"90","name":"送气","photo":null,"parent":"12","listorder":"0"},{"id":"91","name":"送报刊","photo":null,"parent":"12","listorder":"0"},{"id":"243","name":"其他...","photo":null,"parent":"12","listorder":"0"}]
     */

    private String id;
    private String name;
    private Object photo;
    private String parent;
    private String listorder;
    /**
     * id : 21
     * name : 送快递
     * photo : null
     * parent : 12
     * listorder : 0
     */

    private List<ClistBean> clist;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getPhoto() {
        return photo;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getListorder() {
        return listorder;
    }

    public void setListorder(String listorder) {
        this.listorder = listorder;
    }

    public List<ClistBean> getClist() {
        return clist;
    }

    public void setClist(List<ClistBean> clist) {
        this.clist = clist;
    }

    public static class ClistBean {
        private String id;
        private String name;
        private Object photo;
        private String parent;
        private String listorder;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getPhoto() {
            return photo;
        }

        public void setPhoto(Object photo) {
            this.photo = photo;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public String getListorder() {
            return listorder;
        }

        public void setListorder(String listorder) {
            this.listorder = listorder;
        }
    }
}
