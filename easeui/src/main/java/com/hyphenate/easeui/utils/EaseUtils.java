package com.hyphenate.easeui.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/9/2.
 */

public class EaseUtils {

    public static String TypeToOccupation(int type) {
        switch (type) {
            case 1:
                return "同城配送";
            case 2:
                return "课程家教";
            case 3:
                return "艺术家教";
            case 4:
                return "钟点工";
            case 5:
                return "护工";
            case 6:
                return "保姆";
            case 7:
                return "厨师";
            case 8:
                return "传单派发";
            case 9:
                return "导游";
            case 10:
                return "服务员";
            case 11:
                return "礼仪模特";
            case 12:
                return "运动陪练";
            case 13:
                return "代驾";
            case 14:
                return "车辆服务";
            case 15:
                return "美妆美甲";
            case 16:
                return "宠物服务";
            case 17:
                return "电器服务";
            case 18:
                return "电脑维修";
            case 19:
                return "技工";
            case 20:
                return "工厂用工";
            case 21:
                return "搬货搬家";
            case 22:
                return "促销导购";
            case 23:
                return "保洁";
            case 24:
                return "游戏陪练";
            case 25:
                return "衣物干洗 ";
            case 26:
                return "电动车维修";
            case 27:
                return "手机维修";
            case 28:
                return "保安";
            case 29:
                return "上门开锁";
            case 30:
                return "下水道疏通";
            case 31:
                return "充场";
        }
        return "默认全部";
    }

    public static String filterMessage(String message) {
        ArrayList<String> ss = getNumbers(message);
        for (int j = 0; j < ss.size(); j++) {
            long num = 0;
            try {
                num = Long.valueOf(ss.get(j));
            } catch (Exception ex) {
                num = 0;
            }
            if (num > 99999) {
                message = message.replace(ss.get(j), getstr(ss.get(j).length()));
            }
        }
        if (message.contains("手机")) {
            message = message.replaceAll("手机", "****");
        }
        if (message.contains("电话")) {
            message = message.replaceAll("电话", "****");
        }
        if (message.contains("电话号码")) {
            message = message.replaceAll("电话号码", "****");
        }
        if (message.contains("手机号码")) {
            message = message.replaceAll("手机号码", "****");
        }
        if (message.contains("号码")) {
            message = message.replaceAll("号码", "****");
        }
        if (message.contains("微信")) {
            message = message.replaceAll("微信", "****");
        }
        if (message.contains("QQ")) {
            message = message.replaceAll("QQ", "****");
        }
        if (message.contains("扣扣")) {
            message = message.replaceAll("扣扣", "****");
        }
        if (message.contains("V信")) {
            message = message.replaceAll("V信", "****");
        }
        if (message.contains("v信")) {
            message = message.replaceAll("v信", "****");
        }
        return message;
    }

    public static String getstr(int count) {
        String ss = "";
        for (int i = 0; i < count; i++) {
            ss = ss + "*";
        }
        return ss;
    }

    //截取数字
    public static ArrayList<String> getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        ArrayList<String> list = new ArrayList<String>();
        while (matcher.find()) {
            int count = matcher.groupCount();
            System.out.println("kaishi：" + matcher.start());
            System.out.println("jiesuo：" + matcher.end());
            list.add(matcher.group(0));
        }
        return list;
    }

}
