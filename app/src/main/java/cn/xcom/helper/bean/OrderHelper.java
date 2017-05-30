package cn.xcom.helper.bean;

/**
 * Created by Administrator on 2016/10/20 0020.
 *用于订单分类
 */

public class OrderHelper {
    public static final int SellerOrder = 101;//商户订单
    public static final int BuyerOrder = 102;//我的订单

    public static final int CANCELED = -1;//订单状态 已取消
    public static final int UNPAY = 1;//未付款
    public static final int UN_SEND_OUT = 2;//未发货
    public static final int UNCONFIRMED = 3;//未确认
    public static final int BUYER_UNCOMMENT = 4;//买家未评价
    public static final int SELLER_UNCOMMENT = 5;//卖家未评价
    public static final int COMPLETED = 10;//订单完成

}
