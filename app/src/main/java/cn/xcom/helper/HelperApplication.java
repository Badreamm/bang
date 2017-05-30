package cn.xcom.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.baidu.mapapi.SDKInitializer;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.bugly.crashreport.CrashReport;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.xcom.helper.bean.TaskType;
import cn.xcom.helper.service.OrderService;
import cn.xcom.helper.utils.ToolUtil;


/**
 * Created by zhuchongkun on 16/5/27.
 */
public class HelperApplication extends Application {
    private Context mContext;
    private static HelperApplication instance;
    //全局变量，选择的任务分类
    private List<TaskType> taskTypes;

    //切换城市信息
    public double mCurrentLocLat = 0;
    public double mCurrentLocLon = 0;
    public String mCurrentAddress = "";
    public String status = "";
    public String mDistrict = "";
    public String mLocaddresscity = "";
    public String mLocaddresscityid = "";
    //实时定位坐标和地址
    public double mLocLat, mLocLon;
    public String mLocAddress;

    //支付相关参数
    public String tradeNo;
    public String payType;
    public String type = "";
    public String wxpay = "";

    //提现flag
    public boolean isBack = false;

    //特卖返回flag
    public boolean saleBack = false;

    //便民圈返回flag
    public boolean trendsBack = false;
    //广告发布返回flag
    public boolean advBack = false;
    //便民圈广告发布flag
    public boolean conAdv = false;
    //第一次进入时需要上传位置 调用开工接口
    public boolean needUploadLocation = true;
    //帮我判断是否定向发单
    public boolean help = false;
    //便民圈点击进入详情
    public String convenience = "1";
    /**
     * 打开的activity
     **/
    private List<Activity> activities = new ArrayList<Activity>();

    //当前的activity
    public Activity mActivity;

    // login user name
    public final String PREF_USERNAME = "username";
    /**
     * nickname for current user, the nickname instead of ID be shown when user receive notification from APNs
     */
    public static String currentUserNick = "";
    /**
     * 把全国的省市区的信息以json的格式保存
     */
    public static JSONObject mCityJson;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        instance = this;
        taskTypes = new ArrayList<>();
        //在这里初始化
//        Bugtags.start("a0bfbbf506442a8f5ba633c758385d3c", this, Bugtags.BTGInvocationEventNone);

        //初始化极光
        setPush();
        //初始化地图
        SDKInitializer.initialize(mContext);
        //初始化ImageLoader
        initImageLoader(mContext);
        //初始化城市数据
        initJsonData();
        //初始化bugly
        initBugly();
//        setViedioFilePath();

    }


    /**
     * 新建了一个activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }


    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            this.activities.remove(activity);
            activity.finish();
            activity = null;
        }
    }


    /**
     * 结束所有的activity
     */
    @Override
    public void onTerminate() {
        super.onTerminate();

        for (Activity activity : activities) {
            activity.finish();
        }

        System.exit(0);
    }

    private void setPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    public static HelperApplication getInstance() {
        return instance;
    }

    public List<TaskType> getTaskTypes() {
        return taskTypes;
    }

    public void setTaskTypes(List<TaskType> taskTypes) {
        this.taskTypes = taskTypes;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initChat() {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null || !processAppName.equalsIgnoreCase(mContext.getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;

    }

    /**
     * 从assert文件夹中读取省市区的json文件，然后转化为json对象
     */
    private void initJsonData() {
        try {
            String s = ToolUtil.readFromAsset(mContext, "city.json");
            if (s != null) {
                mCityJson = new JSONObject(s);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initImageLoader(Context mContext) {
//        File cacheDir = StorageUtils.getOwnCacheDirectory(mContext,"caterin/Cache");
//        // LruCache通过构造函数传入缓存值，以KB为单位。
//        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
//        // 使用最大可用内存值的1/8作为缓存的大小。
//        int cacheSize = maxMemory / 8;
        // 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
//                .memoryCacheExtraOptions(480, 800)
//                .threadPoolSize(3)// 线程池内加载的数量
//                .threadPriority(Thread.NORM_PRIORITY - 1).tasksProcessingOrder(QueueProcessingType.FIFO) // default
//                .denyCacheImageMultipleSizesInMemory().memoryCache(new UsingFreqLimitedMemoryCache(cacheSize))
//                // You can pass your own memory cache
//                .memoryCache(new LruMemoryCache(cacheSize)).memoryCache(new WeakMemoryCache())
//                // implementation你可以通过自己的内存缓存实现
//                .memoryCacheSize(cacheSize).memoryCacheSizePercentage(13) // default
//                .discCacheSize(cacheSize)
//                // .discCacheFileNameGenerator(new Md5FileNameGenerator())
//                // 将保存的时候的URI名称用MD5加密
//                // .discCacheFileNameGenerator(new
//                // HashCodeFileNameGenerator())// 将保存的时候的URI名称用HASHCODE加密
//                .tasksProcessingOrder(QueueProcessingType.LIFO).discCacheFileCount(1000) // 缓存的File数量
//                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()).imageDownloader(new BaseImageDownloader(mContext, 5 * 1000, 30 * 1000))
//                // connectTimeout (5s), readTimeout(30s)超时时间
//                .writeDebugLogs() // Remove for release app
//                .build();
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(mContext);
        ImageLoader.getInstance().init(configuration);// 全局初始化此配置
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    private void initBugly() {
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(context, "18fef82344", true, strategy);//正式上线时改为false
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 设置拍摄视频缓存路径
     */
//    private void setViedioFilePath() {
//        File dcim = Environment
//                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//        if (DeviceUtils.isZte()) {
//            if (dcim.exists()) {
//                VCamera.setVideoCachePath(dcim + "/recoder/");
//            } else {
//                VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
//                        "/sdcard-ext/")
//                        + "/recoder/");
//            }
//        } else {
//            VCamera.setVideoCachePath(dcim + "/51bJuns/");
//        }
//
////		VCamera.setVideoCachePath(FileUtils.getRecorderPath());
//        // 开启log输出,ffmpeg输出到logcat
////        VCamera.setDebugMode(false);
//        // 初始化拍摄SDK，必须
//        VCamera.initialize(this);
//
//    }


}
