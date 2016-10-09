package com.udit.aijiabao.configs;

/**
 * Created by Administrator on 2016/5/12.
 */
public class UrlConfig {

    /**
     * 基地址
     */
    private static final String BASE_URL = "http://115.159.2.160:8080/ajb.manage/rdp/business/strWebservice";
    /**
     * 天气
     */
    public static final String WEATHER_URL = "http://v.juhe.cn/weather/geo?format=2&key=" + Constants.WEATHER_KEY;//+"&lon=f&lat=f"
    /**
     * 登录
     */
    public static final String LOGIN_URL = BASE_URL + "/apploginService.htm?";


    public static final String HOME_STUFFINFO_URL = BASE_URL + "/myStuffService.htm?";


    public static final String HOME_BOOk_URL = BASE_URL + "/bookingService.htm?";

    public static final String COMMENT_URL=BASE_URL+"/commentService.htm?";

    public static final String CROP_URL = BASE_URL + "/corpService.htm?";

    public static final String QUERY_COACH_URL = BASE_URL + "/myCoachService.htm?";


    public static final String MESSAGE_URL = BASE_URL + "/notifyService.htm?";

    public static final String PROJECT_URL=BASE_URL+"/trainingItemsService.htm?";


    private static final String BASE_URL_IP="http://115.159.2.160:10022/v1/users";

    public static final String UPDATE_PASS_URL =BASE_URL_IP+ "/update_password?";

    public static final String SEND_MS_CODE=BASE_URL_IP+"/send_verification_token?";

    public static final String SIGN_URL=BASE_URL_IP+"/signup?";

    public static final String ENLIST_URL=BASE_URL+"/memberSignUpService.htm?";


    public static final String IP = "http://115.159.2.160:10022";

    public static final String FINGPASS_SEND_MS_URL = IP+"/v1/users/send_password_token?";


    public static final String VERIFY_PHONE_CODE_URL = IP+"/v1/users/validate_password_token";

//    http://115.159.2.160:10022/v1/users/update_password

    public static final String FIND_UPDATE_PASS = IP+"/v1/users/update_password";


}
