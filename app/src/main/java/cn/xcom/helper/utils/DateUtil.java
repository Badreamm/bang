package cn.xcom.helper.utils;

import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 尉鑫鑫 on 2016/9/19.
 */
public class DateUtil {
    public static SimpleDateFormat sf = null;
    public static int tag;

    public static String getCurrentDate() {
        Date d = new Date();
        sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(d);
    }

    //字符串转date
    public static Date getStringToDates(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = date;
        Date dates = sdf.parse(strDate);
        return dates;
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString(long time) {
        Date d = new Date(time);
//        sf = new SimpleDateFormat("yyyy年MM月dd日hh时mm分ss秒");
        sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(d);
    }

    /*将字符串转为时间戳*/
    public static long getStringToDate(String time) {
        sf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    //点击输入框外隐藏小键盘
    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    //获取第一栏广告结束时间
    public static String getOneEnd() throws ParseException {
        Date d = new Date();
        String date;
        String nextMounth;
        String nextday;
        SimpleDateFormat mounth = new SimpleDateFormat("MM");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        nextMounth = mounth.format(d);
        nextday = day.format(d);
        if (Integer.parseInt(nextday) > 20) {
            date = getmounthend();
        } else {
            date = getNextYear(nextMounth, nextday) + "-" + getNextMounth(getCurrentDate(), nextday) + "-" + getEndDay(nextday);
        }
        return date;
    }

    //获取第二栏广告开始时间
    public static String getTwoStart() throws ParseException {
        Date d = getStringToDates(getOneEnd());
        String nextMounth;
        String nextday;
        SimpleDateFormat mounth = new SimpleDateFormat("MM");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        nextMounth = mounth.format(d);
        nextday = day.format(d);
        return getNextYear(nextMounth, nextday) + "-" + getNextMounth(getOneEnd(), nextday) + "-" + getStartDay(nextday);
    }

    //获取第二栏广告结束时间
    public static String getTwoEnd() throws ParseException {
        Date d = getStringToDates(getTwoStart());
        String nextMounth;
        String nextday;
        String date;
        SimpleDateFormat mounth = new SimpleDateFormat("MM");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        nextMounth = mounth.format(d);
        nextday = day.format(d);
        if (Integer.parseInt(nextday) > 20) {
            date = getmounthend();
        } else {
            date = getNextYear(nextMounth, nextday) + "-" + getNextMounth(getTwoStart(), nextday) + "-" + getEndDay(nextday);
        }
        return date;
    }

    //获取第三栏广告开始时间
    public static String getThreeStart() throws ParseException {
        Date d = getStringToDates(getTwoEnd());
        String nextMounth;
        String nextday;
        SimpleDateFormat mounth = new SimpleDateFormat("MM");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        nextMounth = mounth.format(d);
        nextday = day.format(d);
        return getNextYear(nextMounth, nextday) + "-" + getNextMounth(getTwoEnd(), nextday) + "-" + getStartDay(nextday);
    }

    //获取第三栏广告结束时间
    public static String getThreeEnd() throws ParseException {
        Date d = getStringToDates(getThreeStart());
        String nextMounth;
        String nextday;
        String date;
        SimpleDateFormat mounth = new SimpleDateFormat("MM");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        nextMounth = mounth.format(d);
        nextday = day.format(d);
        if (Integer.parseInt(nextday) > 20) {
            date = getmounthend();
        } else {
            date = getNextYear(nextMounth, nextday) + "-" + getNextMounth(getThreeStart(), nextday) + "-" + getEndDay(nextday);
        }
        return date;
    }

    //获取当月最后一天
    public static String getmounthend() {
        SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,
                cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        return dateFormater.format(cal.getTime());
    }

    //获取月
    public static String getNextMounth(String date, String day) throws ParseException {
        Date d = getStringToDates(date);
        sf = new SimpleDateFormat("MM");
        String nextMounth = null;
        int mounth = Integer.parseInt(sf.format(d));
//        if (mounth < 12) {
//            nextMounth = (mounth + 1) + "";
//        } else {
//            nextMounth = "01";
//        }
        if (Integer.parseInt(day) > 20) {
            if (mounth + 1 > 12) {
                nextMounth = "01";
            } else {
                if (mounth < 9) {
                    nextMounth = "0" + (mounth + 1);
                } else {
                    nextMounth = (mounth + 1) + "";
                }
            }
        } else {
            nextMounth = sf.format(d);
        }
        return nextMounth;
    }

    //获取年
    public static String getNextYear(String mounth, String day) {
        Date d = new Date();
        sf = new SimpleDateFormat("yyyy");
        String nextYear;
        if (Integer.parseInt(mounth) == 12 && Integer.parseInt(day) > 20) {
            nextYear = (Integer.parseInt(sf.format(d)) + 1) + "";
        } else {
            nextYear = sf.format(d);
        }

        return nextYear;
    }

    //获取开始天
    public static String getStartDay(String endDay) {
        String startday;
        int s = Integer.parseInt(endDay);
        if (s == 10) {
            startday = "11";
        } else if (s == 20) {
            startday = "21";
        } else {
            startday = "01";
        }
        return startday;
    }

    //获取结束天
    public static String getEndDay(String startDay) {
        String endday = null;
        int s = Integer.parseInt(startDay);
        if (s <= 10) {
            endday = "10";
        } else if (s > 10 && s <= 20) {
            endday = "20";
        }
//        else if (s>20){
//            endday = 
//        }
        return endday;
    }

}
