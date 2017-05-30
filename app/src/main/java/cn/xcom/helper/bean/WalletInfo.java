package cn.xcom.helper.bean;

import java.io.Serializable;

/**
 * Created by zhuchongkun.
 */
public class WalletInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String momney;
    private String availableMomney;
    private String allTasks;
    private String monthTasks;
    private String allIncome;
    private String monthIncome;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMomney() {
        if (momney==null||momney.equals("null")){
            return "0.00";
        }
        return momney;
    }

    public void setMomney(String momney) {
        this.momney = momney;
    }

    public String getAvailableMomney() {
        if (availableMomney==null||availableMomney.equals("null")){
            return "0.00";
        }
        return availableMomney;
    }

    public void setAvailableMomney(String availableMomney) {
        this.availableMomney = availableMomney;
    }

    public String getAllTasks() {
        if (allTasks==null||allTasks.equals("null")){
            return "0";
        }
        return allTasks;
    }

    public void setAllTasks(String allTasks) {
        this.allTasks = allTasks;
    }

    public String getMonthTasks() {
        if (monthTasks==null||monthTasks.equals("null")){
            return "0";
        }
        return monthTasks;
    }

    public void setMonthTasks(String monthTasks) {
        this.monthTasks = monthTasks;
    }

    public String getAllIncome() {
        if (allIncome==null||allIncome.equals("null")){
            return "0.00";
        }
        return allIncome;
    }

    public void setAllIncome(String allIncome) {
        this.allIncome = allIncome;
    }

    public String getMonthIncome() {
        if (monthIncome==null||monthIncome.equals("null")){
            return "0.00";
        }
        return monthIncome;
    }

    public void setMonthIncome(String monthIncome) {
        this.monthIncome = monthIncome;
    }

    @Override
    public String toString() {
        return "WalletInfo{" +
                "momney='" + momney + '\'' +
                ", availableMomney='" + availableMomney + '\'' +
                ", allTasks='" + allTasks + '\'' +
                ", monthTasks='" + monthTasks + '\'' +
                ", allIncome='" + allIncome + '\'' +
                ", monthIncome='" + monthIncome + '\'' +
                '}';
    }
}
