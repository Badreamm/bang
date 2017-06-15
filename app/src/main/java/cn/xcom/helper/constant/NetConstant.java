package cn.xcom.helper.constant;

/**
 * Created by zhuchongkun on 16/5/29.
 */
public interface NetConstant extends HelperConstant {
    /**
     * 注册
     */
    String NET_REGISTER = NET_HOST_PREFIX + "a=AppRegister&";
    /**
     * 登录
     */
    String NET_LOGIN = NET_HOST_PREFIX + "a=applogin&";
    /**
     * 验证用户是否注册
     */
    String NET_CHECK_PHONE = NET_HOST_PREFIX + "a=checkphone&";
    /**
     * 发送验证码
     */
    String NET_GET_CODE = NET_HOST_PREFIX + "a=SendMobileCode&";
    /**
     * 修改密码
     */
    String NET_RESET_PASSWORD = NET_HOST_PREFIX + "a=forgetpwd&";
    /**
     * 获取个人资料
     */
    String NET_GET_USER_INFO = NET_HOST_PREFIX + "a=getuserinfo&";
    /**
     * 上传图片
     */
    String NET_UPLOAD_IMG = NET_HOST_PREFIX + "a=uploadimg";
    /**
     * 图片路径
     */
    String NET_DISPLAY_IMG = "http://www.my51bang.com/uploads/images/";
    /**
     * 修改头像
     */
    String NET_UPDATE_HEAD = NET_HOST_PREFIX + "a=UpdateUserAvatar&";
    /**
     * 修改性别
     */
    String NET_UPDATE_GENDER = NET_HOST_PREFIX + "a=UpdateUserSex&";
    /**
     * 修改城市
     */
    String NET_UPDATE_CITY = NET_HOST_PREFIX + "a=UpdateUserCity&";
    /**
     * 修改姓名
     */
    String NET_UPDATE_NAME = NET_HOST_PREFIX + "a=UpdateUserName&";
    /**
     * 通过用户编号获取身份认证状态
     */
    String NET_GET_IDENTITY = NET_HOST_PREFIX + "a=getAuthRecord&";
    /**
     * 身份认证
     */
    String NET_IDENTITY_AUTHENTICATION = NET_HOST_PREFIX + "a=Authentication&";
    /**
     * 获取任务分类列表
     */
    String NET_GET_TASKLIST = NET_HOST_PREFIX + "a=getTaskTypeList&";
    /**
     * 获取我的今天是否签到
     */
    String NET_GET_SIGN_STATE = NET_HOST_PREFIX + "a=GetMySignLog";
    /**
     * 签到
     */
    String NET_TO_SIGN = NET_HOST_PREFIX + "a=SignDay";
    /**
     * 获取我的钱包
     */
    String NET_GET_WALLET = NET_HOST_PREFIX + "a=GetMyWallet";
    /**
     * 获取我的收支记录
     */
    String NET_GET_WALLET_LOG = NET_HOST_PREFIX + "a=GetMyWalletLog";
    /**
     * 获取我的提现申请记录
     */
    String NET_GET_WITHDRAW_LOG = NET_HOST_PREFIX + "a=GetMyWithdrawLog";

    /**
     * 获取认证帮列表
     */
    String NET_GET_AUTHENTICATION_LIST = NET_HOST_PREFIX + "a=getAuthenticationUserList";
    /*
    获取商品信息
     */
    String GOODSLIST = NET_HOST_PREFIX + "a=getshoppinglist";
    /*
     获取字典
     */
    String DICTIONARYS_LIST = NET_HOST_PREFIX + "a=getDictionaryList&";
    /*
    *发布任务
     */
    String RELEASE = NET_HOST_PREFIX + "a=PublishGoods&";

    /**
     * 发布任务
     */
    String PUBLISHTASK = NET_HOST_PREFIX + "a=publishTask&";

    /**
     * 更新任务支付状态
     */
    String UPDATETASKPAY = NET_HOST_PREFIX + "a=UpdataTaskPaySuccess&";

    /**
     * 更新热卖支付状态
     */
    String UPDATESHOPPAY = NET_HOST_PREFIX + "a=UpdataShoppingPaySuccess&";


    /*
    *根据地点获取抢单列表
     */
    String GETTASKLIST = NET_HOST_PREFIX + "a=getTaskListByCity&";

    /*
    *根据商品id查询商品详情
    */
    String SHOP_INFO = NET_HOST_PREFIX + "a=showshoppinginfo&";


    /*
    *根据商家的ID找出该商家发布的全部商品
    */
    String SHOP_GOOD = NET_HOST_PREFIX + "a=getMyshoppinglist&";

    /*
    *我的任务
    */
    String GET_MEY_TASK = NET_HOST_PREFIX + "a=getMyApplyTaskList&";


    /*
    *便民圈接口
    */
    String CONVENIENCE = NET_HOST_PREFIX + "a=getbbspostlist&";

    /*
        快递查询
    */
//    String DELIEVER = "http://m.kuaidi100.com";
    String DELIEVER = "http://m.ickd.cn";
    /*
    * 违章查询
    * */
    String TRAFFIC = "http://m.weizhang8.cn";
    /*
        发布便民圈信息
     */
    String CONVENIENCE_RELEASE = NET_HOST_PREFIX + "a=addbbsposts_new&";

    /*
        删除商品
     */
    String DELETE_GOOD = NET_HOST_PREFIX + "a=DeleteGoods&";

    /*
   *更新任务状态
   */
    String UPDATE_TASK_STATE = NET_HOST_PREFIX + "a=UpdataTaskState&";

    /*
    抢单
     */
    String GRAB_TASK = NET_HOST_PREFIX + "a=ApplyTask&";
    /*
   *更新任务
   */
    String UPDATA = NET_HOST_PREFIX + "a=UpdateGoodsInfo&";
    /*
  *更新图片
  */
    String UPDATA_PICTURE = NET_HOST_PREFIX + "a=UpdateGoodsPicture&";
    /*
    产品收藏
     */
    String GOOD_COLLECTION = NET_HOST_PREFIX + "a=addfavorite&";
    /*
   产品是否收藏
    */
    String GOOD_IS_COLLECTION = NET_HOST_PREFIX + "a=CheckHadFavorite&";

    /*
   取消收藏
   */
    String GOOD_CANCLE_COLLECTION = NET_HOST_PREFIX + "a=cancelfavorite&";
    /*
    收藏列表
     */
    String HAS_COLLECTION = NET_HOST_PREFIX + "a=getfavoritelist&";
    /*
    添加用户地址
     */
    String ADD_ADRESS = NET_HOST_PREFIX + "a=AddAddress&";
    /*
    获取用户地址列表
     */
    String GET_ADRESS = NET_HOST_PREFIX + "a=GetMyAddressList&";
    /*
    修改用户地址
     */
    String FIX_ADRESS = NET_HOST_PREFIX + "a=UpdateAddress&";
    /*
    删除用户地址
     */
    String DELETE_ADRESS = NET_HOST_PREFIX + "a=DeleteAddress&";

    /*
    我的接单
     */
    String GET_MY_TASK = NET_HOST_PREFIX + "a=getMyApplyTaskList&";

    /*
    检查用户是否实名认证
     */
    String Check_Had_Authentication = NET_HOST_PREFIX + "a=CheckHadAuthentication&";

    /*
    检查用户是否保险认证
     */
    String Check_Insurance = NET_HOST_PREFIX + "a=CheckInsurance&";

    /*
    获取银行信息
     */
    String GET_USER_BANK_INFO = NET_HOST_PREFIX + "a=GetUserBankInfo&";

    /**
     * 绑定银行账号
     */
    String BIND_BINK_ACCOUNT = NET_HOST_PREFIX + "a=UpdateUserBank&";

    /**
     * 绑定支付宝账号
     */
    String BIND_ALIPAY_ACCOUNT = NET_HOST_PREFIX + "a=UpdateUserAlipay&";

    /**
     * 提现申请
     */
    String APPLY_WITHDRAW = NET_HOST_PREFIX + "a=ApplyWithdraw&";

    /*
    获取城市列表（长字符串）
     */
    String GET_CITY_LIST = NET_HOST_PREFIX + "a=GetCityListToStr";

    /*
    获取我的技能
     */
    String GET_MY_SKILLS = NET_HOST_PREFIX + "a=getSkillListByUserId&";

    /*
    修改我的技能
     */
    String CHANGE_MY_SKILLS = NET_HOST_PREFIX + "a=UpdataUserSkill&";

    /*
   删除自己的帖子
    */
    String DELETE_OWN_POST = NET_HOST_PREFIX + "a=deletebbspost&";


    /**
     * 获取我发布的任务列表
     */
    String GET_TASK_LIST_BY_USERID = NET_HOST_PREFIX + "a=getTaskListByUserid&";

    /**
     * 查看任务详情
     */
    String GET_TASK_INFO_BY_TASK_ID = NET_HOST_PREFIX + "a=getTaskInfoByTaskid&";

    /**
     * 举报别人的帖子
     */
    String REPORT_OHTHER_POST = NET_HOST_PREFIX + "a=Report&";

    /**
     * 获取认证帮列表
     */
    String GET_AUTHENTICATION_LIST = NET_HOST_PREFIX + "a=getAuthenticationUserList&";

    /**
     * 首页获取小人位置
     */
    String GET_HOME_MAP_AUTHENTICATION_USER = NET_HOST_PREFIX + "a=HomegetAuthenticationUserList&";

    /**
     * 发布任务者取消订单
     */
    String OWNER_CANCEL_TASK = NET_HOST_PREFIX + "a=OwnerCancelTask&";


    /**
     * 买家获取我的商城商品列表
     */
    String BUYER_GET_SHOP_ORDER_LIST = NET_HOST_PREFIX + "a=BuyerGetShoppingOrderList&";

    /**
     * 商户分享h5
     */
    String SHARE_SHOP_H5 = "http://www.my51bang.com/index.php?g=portal&m=article&a=shop&id=";

    /**
     * 我的订单页面 取消订单
     */
    String CANCEL_ORDER = NET_HOST_PREFIX + "a=cancelorder&";


    /**
     * 发表订单评价
     */
    String POST_COMMENT = NET_HOST_PREFIX + "a=SetEvaluate&";

    /**
     * 用户二维码h5
     */
    String SHARE_QRCODE_H5 = "http://www.my51bang.com/index.php?g=portal&m=article&a=server&id=";


    /**
     * 卖家确认验证码
     */
    String VERIFY_SHOPPING_CODE = NET_HOST_PREFIX + "a=VerifyShoppingCode&";


    /**
     * 卖家获取订单列表
     */
    String SELLER_GET_SHOPPING_ORDER_LIST = NET_HOST_PREFIX + "a=SellerGetShoppingOrderList&";


    /**
     * 获取聊天列表
     */
    String GET_CHAT_LIST = NET_HOST_PREFIX + "a=xcGetChatListData&";


    /**
     * 获取聊天信息（两个人之间的）
     * send_uid,receive_uid
     */
    String GET_CHAT_DATA = NET_HOST_PREFIX + "a=xcGetChatData&";


    /**
     * 发送聊天信息
     */
    String SEND_CHAT_DATA = NET_HOST_PREFIX + "a=SendChatData&";


    /**
     * 产品上架
     */
    String GOODS_SHANG_JIA = NET_HOST_PREFIX + "a=Shangjia&";

    /**
     * 产品下架
     */
    String GOODS_XIA_JIA = NET_HOST_PREFIX + "a=Xiajia&";


    /**
     * 提交保险认证照片
     */
    String UPDATE_USER_INSURANCE = NET_HOST_PREFIX + "a=UpdateUserInsurance&";

    /**
     * 获取用户开工收工状态
     */
    String GET_WORKING_STATE = NET_HOST_PREFIX + "a=GetWorkingState&";

    /**
     * 热卖］更新状态
     */
    String UPDATE_SHOPPING_STATE = NET_HOST_PREFIX + "a=UpdataShoppingState&";


    /**
     * 切换开工收工状态
     */
    String CHANGE_WORKING_STATE = NET_HOST_PREFIX + "a=BeginWorking&";

    /**
     * 提交商品订单
     */
    String SUBMIT_GOOD_ORDER = NET_HOST_PREFIX + "a=bookingshopping&";

    /**
     * 我的二维码
     */
    String MY_QR_CODE = "http://www.my51bang.com/index.php?g=portal&m=article&a=qr&type=1";

    /**
     * 获取我的推广人员列表
     */
    String GET_MY_WORK = NET_HOST_PREFIX + "a=getMyIntroduceList&";

    /**
     * 进入app检验封号情况
     */
    String CHECK_LOGIN = NET_HOST_PREFIX + "a=checkLogin&";

    /**
     * 获取我的接单汇总
     */
    String MY_APPLY_COUNT = NET_HOST_PREFIX + "a=GetMyApplyTastTotal&";

    /**
     * 根据userid获取技能
     */
    String GET_SKILLS_BY_USERID = NET_HOST_PREFIX + "a=getAuthenticationInfoByUserId&";


    /**
     * 上传录音
     */
    String UPLOAD_RECORD = NET_HOST_PREFIX + "a=uploadRecord&";
    /*
  * 获取广告图片
  * */
    String GET_SLIDE_LIST = NET_HOST_PREFIX + "a=getslidelist&";
    /*
    * 获取同城信息单价
    * */
    String GET_MESSAGE_PRICE = NET_HOST_PREFIX + "a=GetMessagePrice";
    /*
    * 获取未读消息总数
    * */
    String XC_GET_CHAT_NOREAD_COUNT = NET_HOST_PREFIX + "a=xcGetChatnoReadCount";
    /*
    * 获取我推荐的客户数量
    * */
    String GET_MY_INTRODUCE_COUNT = NET_HOST_PREFIX + "a=getMyIntroduceCount";
    /*
    * 获取我的便民圈消息
    * */
    String GET_MY_BB_SPOSTLIST = NET_HOST_PREFIX + "a=getMybbspostlist";
    /*
    检测广告位是否可用
    */
    String CHECK_AD_IS_CAN_PUBLISH = NET_HOST_PREFIX + "a=CheckADIsCanPublish";
    /*
    * 获取城市列表
    * */
    String GET_CITY = NET_HOST_PREFIX + "a=GetCityList";
    /*
    * 获取城市id
    * */
    String CHECK_CITY = NET_HOST_PREFIX + "a=CheckCity";
    /*
    * 发布广告
    * */
    String PUBLISH_AD = NET_HOST_PREFIX + "a=PublishAD";
    /*
  * 获取广告
  * */
    String GET_MY_ADLIST = NET_HOST_PREFIX + "a=getMyAdlist";
    /*
    * 删除我的广告
    * */
    String DELETE_MY_AD = NET_HOST_PREFIX + "a=DeleteMyAD";
    /*
   * 获取广告信息（新）
   * */
    String GET_SLIDE_LIST_NEW = NET_HOST_PREFIX + "a=getslidelist_new";
    /*
 * 更新广告
 * */
    String UPDATE_AD = NET_HOST_PREFIX + "a=UpdateAD";
    /**
     * 发布雇佣任务
     */
    String PUBLISH_HIRE_TASK = NET_HOST_PREFIX + "a=publishHireTask&";
    /**
     * 取消雇佣订单
     */
    String CANCEL_HIRE_TASK = NET_HOST_PREFIX + "a=CancelHireTask&";
    /**
     * 接受雇佣订单
     */
    String APPLY_HIRE_TASK = NET_HOST_PREFIX + "a=ApplyHireTask&";
    /**
     * 接受雇佣订单
     */
    String GET_MY_NOAPPLY_HIRE_TAST_COUNT = NET_HOST_PREFIX + "a=GetMyNoApplyHireTastCount&";
    /*
    * 用户协议地址
    * */
    String USER_PROTOCOL ="http://www.my51bang.com/index.php?g=portal&m=article&a=index&id=4" ;

    /**
     * 发送红包
     */
    String SEND_PACKET = NET_HOST_PREFIX + "a=addPacket&";

    /**
     * 抢红包时的状态判断
     */
    String GET_PACKET_STATE = NET_HOST_PREFIX + "a=getPacketState";

    /**
     * 抢红包
     */
    String TOUCH_PACKET = NET_HOST_PREFIX + "a=touchPacket";

    /**
     * 判断首页是否显示红包标志
     */
    String GET_HOME_PACKET_FLAG = NET_HOST_PREFIX + "a=getCanBePacket";


    /**
     * 关闭打开红包推送
     */
    String CHANGE_PACKET_RECEIVER_STATE = NET_HOST_PREFIX + "a=isrReceivePacketMessage";

    /**
     * 获取红包推送状态 0 关闭 1打开
     */
    String GET_PACKET_RECEIVER_STATE = NET_HOST_PREFIX + "a=GetIsReceiveState";
}
