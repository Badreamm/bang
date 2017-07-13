package cn.xcom.helper.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zhuchongkun.
 */
public class TimeUtils {
    /**
     * 获得当前时间，时间戳
     *
     * @return 当前时间的时间戳
     */
    public static long getNowTime() {
        return System.currentTimeMillis();
    }

    /**
     * 获得当前时间，按照给定的格式格式化<br/>
     *
     * @param fromateType
     *            格式化格式
     * @return 当前时间
     */
    public static String getNowTime(String fromateType) {
        return fromateTimeShow(getNowTime(), fromateType);
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString(long time) {
        SimpleDateFormat sf;
        Date d = new Date(time);
        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(d);
        }

    public static String newChangeTime(long time){
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(time * 1000));
    }
    /**
     * 格式化时间
     *
     * @param time
     *            时间，UNIX时间戳格式
     * @param fType
     *            格式
     * @return 格式化后的时间
     */

    public static String fromateTimeShow(long time, String fType) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(fType, Locale.getDefault());
        return format.format(date);
    }

    /**
     * @exception 1分钟以内  显示“刚刚”  1小时以内    显示“几分钟前”，24小时以内  显示“几小时前” ，24*3小时以内  显示“几天前”，其他显示具体时间： 年 月  日
     * @param time
     * @return
     */
    public static String fromateTimeShowByRule(long time){
        long timeDistance=getNowTime() - time;
        long days = timeDistance / (1000 * 60 * 60 * 24);
        long hours = (timeDistance - days * (1000 * 60 * 60 * 24))
                / (1000 * 60 * 60);
        long minutes = (timeDistance-days*(1000 * 60 * 60 *24)-hours*(1000* 60 * 60))/(1000* 60);
        if (days==0) {
            if (hours==0) {
                if (minutes==0) {
                    return "刚刚";
                }else{
                    return minutes+"分钟前";
                }
            }else{
                return hours+"小时前";
            }
        }else if (days==1) {
            return "1天前";
        }else if (days==2) {
            return "2天前";
        }else if (days==3) {
            return "3天前";
        }else{
            return fromateTimeShow(time, "yyyy-MM-dd");
        }
    }
    /**
     * 判断一个UNIX时间戳是否是今天以前的时间<br/>
     * 如果异常则返回true
     *
     * @param time
     *            要判断的时间
     * @return true表示是今天以前的时间，false表示不是 @ 未知异常
     */
    public static boolean ifBeforeToday(long time) {
        boolean result = true;
        try {
            long now = getTodayTime(0, 0, 0);
            result = (now - time) > 0;
        } catch (Exception e) {
            result = true;
        }
        return result;
    }

    /**
     * 根据今天的时间，时、分、秒来获得一个UNIX时间戳<br/>
     * 如果异常则返回0L
     *
     * @param hours
     *            时
     * @param minute
     *            分
     * @param seconds
     *            秒
     * @return UNIX时间戳 @ 未知异常
     */
    public static long getTodayTime(int hours, int minute, int seconds) {
        long result = 0L;
        try {
            Calendar nowC = Calendar.getInstance();
            nowC.set(nowC.get(Calendar.YEAR), nowC.get(Calendar.MONTH), nowC.get(Calendar.DAY_OF_MONTH), hours, minute, seconds);
            result = nowC.getTime().getTime();
        } catch (Exception e) {
            result = 0L;
        }
        return result;
    }

    public static long timeDifferent(long endTime){
        Date endDate = new Date(endTime * 1000) ;
        return endDate.getTime() - new Date().getTime();
    }
}
