package qimenapi;

/**
 * Created by xyyz150 on 2016/1/23.
 */
public enum  MainErroreeee {
    INVALID_FORMAT("无效的format参数"),
    INVALID_APP_KEY("无效的appkey参数"),
    INVALID_SESSIONKEY("无效的sessionkey参数"),
    ISP_SERVICE_UNAVAILABLE("服务错误"),
    ISP_SERVICE_TIMEOUT("服务超时");

    private String msg;
    private MainErroreeee(String errormsg){msg=errormsg;}

    public String GetValue(){return msg;}
}
