package cn.xcom.helper.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/25.
 */
public class StringJoint {

    public static String arrayJointchar(List<String> stringArrayList, String separator) {
        StringBuilder sb = new StringBuilder();
        if (stringArrayList != null && stringArrayList.size() > 0) {
            for (int i = 0; i < stringArrayList.size(); i++) {
                if (i < stringArrayList.size() - 1) {
                    sb.append(stringArrayList.get(i) + separator);
                } else {
                    sb.append(stringArrayList.get(i));
                }
            }
        }
        return sb.toString();
    }

}
