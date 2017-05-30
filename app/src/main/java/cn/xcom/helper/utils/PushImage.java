package cn.xcom.helper.utils;

import java.io.IOException;

/**
 * Created by 尉鑫鑫 on 2016/9/22.
 */
public interface PushImage {
    void success(boolean state) throws IOException;
    void error();
}
