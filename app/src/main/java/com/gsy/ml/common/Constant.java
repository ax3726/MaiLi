package com.gsy.ml.common;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/10.
 */

public class Constant {

    public static final String WEXAPPID = "wxb49050ad3515f101";//微信appid
    public static final String ISFIRST = "is_first";//是否第一次进入
    public static final String ROLESTATUE = "role_status";//角色状态
    public static final String PHONE = "phone";//保存用户账号
    public static final String ESCAPE = "\\^\\|";//转义符
    public static final String ESCAPE1 = "^|";//转义符
    public static final String USERINFO = "userinfo";//用户信息
    public static final String REGEX_ID_CARD = "(\\d{15})|(\\d{17}[0-9|X|x])";
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }
}