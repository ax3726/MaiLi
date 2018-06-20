package com.gsy.ml.ui.utils;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.gsy.ml.R;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.model.workType.AgricultureModel;
import com.gsy.ml.model.workType.BeautyModel;
import com.gsy.ml.model.workType.CityServiceModel;
import com.gsy.ml.model.workType.CommonTypeModel;
import com.gsy.ml.model.workType.FactoryModel;
import com.gsy.ml.model.workType.GuideContentModel;
import com.gsy.ml.model.workType.HouseModel;
import com.gsy.ml.model.workType.LandTenantModel;
import com.gsy.ml.model.workType.MedicinalModel;
import com.gsy.ml.model.workType.MoTeModel;
import com.gsy.ml.model.workType.MotionModel;
import com.gsy.ml.model.workType.OtherServiceModel;
import com.gsy.ml.model.workType.PartJobModel;
import com.gsy.ml.model.workType.TutorModel;
import com.gsy.ml.ui.home.WorkType.AgricultureActivity;
import com.gsy.ml.ui.home.WorkType.ArtTutorActivity;
import com.gsy.ml.ui.home.WorkType.ComputerActivity;
import com.gsy.ml.ui.home.WorkType.ElectricClearActivity;
import com.gsy.ml.ui.home.WorkType.FactoryActivity;
import com.gsy.ml.ui.home.WorkType.HouseActivity;
import com.gsy.ml.ui.home.WorkType.LandTenantActivity;
import com.gsy.ml.ui.home.WorkType.LeafletsActivity;
import com.gsy.ml.ui.home.WorkType.MedicinalActivity;
import com.gsy.ml.ui.home.WorkType.ModelActivity;
import com.gsy.ml.ui.home.WorkType.PayoutActivity;
import com.gsy.ml.ui.home.WorkType.SalonActivity;
import com.gsy.ml.ui.home.WorkType.SportsActivity;
import com.gsy.ml.ui.home.WorkType.TourGuideActivity;
import com.gsy.ml.ui.home.WorkType.GameLevelingActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ml.gsy.com.library.utils.ParseJsonUtils;
import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/4/28.
 */

public class DemoUtils {


    public static ObjectAnimator nope(View view) {
        int delta = view.getResources().getDimensionPixelOffset(R.dimen.spacing_medium);

        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(.10f, -delta),
                Keyframe.ofFloat(.26f, delta),
                Keyframe.ofFloat(.42f, -delta),
                Keyframe.ofFloat(.58f, delta),
                Keyframe.ofFloat(.74f, -delta),
                Keyframe.ofFloat(.90f, delta),
                Keyframe.ofFloat(1f, 0f)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).
                setDuration(500);
    }

    public static void TypeToActivity(Activity aty, int type) {
        switch (type) {
            case 1://同城配送
                aty.startActivity(new Intent(aty, PayoutActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 2:
                aty.startActivity(new Intent(aty, ArtTutorActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 3:
                aty.startActivity(new Intent(aty, ArtTutorActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 4://钟点工
                aty.startActivity(new Intent(aty, LeafletsActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 5://护工
            case 23://保洁
            case 31://充场
                aty.startActivity(new Intent(aty, LeafletsActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;

            case 6://保姆
                aty.startActivity(new Intent(aty, LeafletsActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 7://厨师
                aty.startActivity(new Intent(aty, ElectricClearActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;

            case 8://传单派发
                aty.startActivity(new Intent(aty, LeafletsActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 9://导游
                aty.startActivity(new Intent(aty, TourGuideActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;


            case 10://服务员 保安
                aty.startActivity(new Intent(aty, LeafletsActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 11://礼仪模特
                aty.startActivity(new Intent(aty, ModelActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 12://健身教练、运动陪练
                aty.startActivity(new Intent(aty, SportsActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 13:

            case 14://车辆、保养、美容、维修
                aty.startActivity(new Intent(aty, ElectricClearActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 15://美容化妆
                aty.startActivity(new Intent(aty, SalonActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 16:
            case 17://电器维修
                aty.startActivity(new Intent(aty, ElectricClearActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 18://电脑维修
                aty.startActivity(new Intent(aty, ComputerActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 19://技工
                aty.startActivity(new Intent(aty, FactoryActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 20://工厂临时用工
                aty.startActivity(new Intent(aty, FactoryActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 21://搬运工
                aty.startActivity(new Intent(aty, FactoryActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 22://促销导购
                aty.startActivity(new Intent(aty, LeafletsActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 24://游戏陪练
                aty.startActivity(new Intent(aty, GameLevelingActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 25://衣物干洗
                aty.startActivity(new Intent(aty, ElectricClearActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 26://电动车维修
                aty.startActivity(new Intent(aty, ElectricClearActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 27://手机维修
                aty.startActivity(new Intent(aty, ElectricClearActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 28://保安
                aty.startActivity(new Intent(aty, FactoryActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 29://上门开锁
                aty.startActivity(new Intent(aty, ElectricClearActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;

            case 30://下水道疏通
                aty.startActivity(new Intent(aty, ElectricClearActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 32://茶叶交易
            case 34://花卉苗木
            case 35://林业产品
            case 41://中药材交易
                aty.startActivity(new Intent(aty, MedicinalActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 33://果蔬粮油
            case 37://农用物资
            case 38://禽畜水产
            case 40://文化艺术
                aty.startActivity(new Intent(aty, AgricultureActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 39://土地承租
                aty.startActivity(new Intent(aty, LandTenantActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
            case 36://配套设施
                aty.startActivity(new Intent(aty, HouseActivity.class)
                        .putExtra("type_name", TypeToOccupation(type))
                        .putExtra("type", type));
                break;
        }
    }

    public static int TypeToImage(int type) {
        switch (type) {
            case 1:
                return R.drawable.tongcheng_icon;
            case 2:
                return R.drawable.kecheng_icon;
            case 3:
                return R.drawable.yishu_icon;
            case 4:
                return R.drawable.zhongdian_icon;
            case 5:
                return R.drawable.hugong_icon;
            case 6:
                return R.drawable.baomu_icon;
            case 7:
                return R.drawable.chushii_icon;
            case 8:
                return R.drawable.chuandan_icon;
            case 9:
                return R.drawable.daoyou_icon;
            case 10:
                return R.drawable.fuwuyuan_icon;
            case 11:
                return R.drawable.mote_icon;
            case 12:
                return R.drawable.jianshen_icon;
            case 13:
                return R.drawable.daijia_icon;
            case 14:
                return R.drawable.car_icon;
            case 15:
                return R.drawable.meirong_icon;
            case 16:
                return R.drawable.chongwu_icon;
            case 17:
                return R.drawable.dianqi_icon;
            case 18:
                return R.drawable.computer_icon;
            case 19:
                return R.drawable.jigong_icon;
            case 20:
                return R.drawable.linshi_icon;
            case 21:
                return R.drawable.banyun_icon;
            case 22:
                return R.drawable.daogou_icon;
            case 23:
                return R.drawable.baomu_icon;
            case 24:
                return R.drawable.youxi_icon;
            case 25:
                return R.drawable.yiwu_icon;
            case 26:
                return R.drawable.diandongche_icon;
            case 27:
                return R.drawable.shoujii_icon;
            case 28:
                return R.drawable.baoan_icon;
            case 29:
                return R.drawable.kaisuo_icon;
            case 30:
                return R.drawable.xiashuidao_icon;
            case 31:
                return R.drawable.chongchang_icon;
            case 32:
                return R.drawable.icon_jy_cyjy;
            case 33:
                return R.drawable.icon_jy_gsly;
            case 34:
                return R.drawable.icon_jy_hhmm;
            case 35:
                return R.drawable.icon_jy_lycp;
            case 36:
                return R.drawable.icon_jy_ms;
            case 37:
                return R.drawable.icon_jy_nywz;
            case 38:
                return R.drawable.icon_jy_qxsc;
            case 39:
                return R.drawable.icon_jy_tdcz;
            case 40:
                return R.drawable.icon_jy_whys;
            case 41:
                return R.drawable.icon_jy_zycjy;
        }
        return R.drawable.tongcheng_icon;
    }

    public static String TypeToPriceInfo(int type) {
        switch (type) {
            case -6://发单协议
                return Link.AGREEINFO + "sendOrder_agreement.html";
            case -5://接单协议
                return Link.AGREEINFO + "acceptOrder_agreement.html";
            case -4://关于我们    注册协议
                return Link.AGREEINFO + "registration_detil.html";
            case -3://法律条文
                return Link.AGREEINFO + "exemption_clause.html";
            case -2://注册协议
                return Link.AGREEINFO + "registration_protocol.html";
            case -1://积分明细
                return Link.AGREEINFO + "integral.html";
            case 1:
                return Link.PRICEINFO + "distribution.html";
            case 2:
                return Link.PRICEINFO + "course_tutor.html";
            case 3:
                return Link.PRICEINFO + "art_tutor.html";
            case 4:
                return Link.PRICEINFO + "hourly_worker.html";
            case 5:
                return Link.PRICEINFO + "nursing_workers.html";
            case 6:
                return Link.PRICEINFO + "nanny.html";
            case 7:
                return Link.PRICEINFO + "cook.html";
            case 8:
                return Link.PRICEINFO + "leaflet.html";
            case 9:
                return Link.PRICEINFO + "guide.html";
            case 10:
                return Link.PRICEINFO + "waiter_security.html";
            case 11:
                return Link.PRICEINFO + "model.html";
            case 12:
                return Link.PRICEINFO + "fitness_coach.html";
            case 13:
                return Link.PRICEINFO + "drive.html";
            case 14:
                return Link.PRICEINFO + "car_repair.html";
            case 15:
                return Link.PRICEINFO + "makeup.html";
            case 16:
                return Link.PRICEINFO + "pet_grooming.html";
            case 17:
                return Link.PRICEINFO + "electric_cleaning.html";
            case 18:
                return Link.PRICEINFO + "computer_repair.html";
            case 19:
                return Link.PRICEINFO + "mechanic.html";
            case 20:
                return Link.PRICEINFO + "temporary_worker.html";
            case 21:
                return Link.PRICEINFO + "hamal.html";
            case 22:
                return Link.PRICEINFO + "promotion.html";
            case 23:
                return Link.PRICEINFO + "cleaning.html";
            case 24:
                return Link.PRICEINFO + "power_leveling.html";
            case 25:
                return Link.PRICEINFO + "laundry_dry_cleaning.html";
            case 26:
                return Link.PRICEINFO + "electric_vehicle_maintenance.html";
            case 27:
                return Link.PRICEINFO + "cellphone_repairs.html";
            case 28:
                return Link.PRICEINFO + "security_staff.html";
            case 29:
                return Link.PRICEINFO + "door_unlock.html";
            case 30:
                return Link.PRICEINFO + "sewer_dredge.html";
            case 31:
                return Link.PRICEINFO + "chongchang.html";
        }
        return "";
    }

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
            case 24://普通
                return "游戏陪练";
            case 25://普通
                return "衣物干洗 ";
            case 26://高级
                return "电动车维修";
            case 27://高级
                return "手机维修";
            case 28://普通
                return "保安";
            case 29://高级
                return "上门开锁";
            case 30://高级
                return "下水道疏通";
            case 31://普通
                return "充场";
            case 32://普通
                return "茶叶交易";
            case 33://普通
                return "果蔬粮油";
            case 34:
                return "花卉苗木";
            case 35:
                return "林业产品";
            case 36:
                return "民宿";
            case 37:
                return "农用物资";
            case 38:
                return "禽畜水产";
            case 39:
                return "土地承租";
            case 40:
                return "文化艺术";
            case 41:
                return "中药材交易";
        }
        return "信息不限";
    }

    public static boolean TypeToNoAddress(int type) {

        switch (type) {
            case 24:
                return true;
            case 32:
                return true;
            case 33:
                return true;
            case 34:
                return true;
            case 35:
                return true;
            case 36:
                return true;
            case 37:
                return true;
            case 38:
                return true;
            case 39:
                return true;
            case 40:
                return true;
            case 41:
                return true;

        }
        return false;
    }

    public static int TypeToMuchPeopleOrder(int type) {
        switch (type) {
            case 1:
                return 0;
            case 2:
                return 0;
            case 3:
                return 0;
            case 4:
                return 1;
            case 5:
                return 1;
            case 6:
                return 1;
            case 7:
                return 0;
            case 8:
                return 1;
            case 9:
                return 0;
            case 10:
                return 1;
            case 11:
                return 1;
            case 12:
                return 0;
            case 13:
                return 0;
            case 14:
                return 0;
            case 15:
                return 0;
            case 16:
                return 0;
            case 17:
                return 0;
            case 18:
                return 0;
            case 19:
                return 1;
            case 20:
                return 1;
            case 21:
                return 1;
            case 22:
                return 1;
            case 23:
                return 1;
        }
        return 0;
    }

    public static int TypeToConfirOrder(int type) {
        switch (type) {
            case 1:
                return 0;
            case 2:
                return 0;
            case 3:
                return 0;
            case 4:
                return 0;
            case 5:
                return 0;
            case 6:
                return 0;
            case 7:
                return 0;
            case 8:
                return 0;
            case 9:
                return 0;
            case 10:
                return 0;
            case 11:
                return 0;
            case 12:
                return 0;
            case 13:
                return 0;
            case 14:
                return 0;
            case 15:
                return 0;
            case 16:
                return 0;
            case 17:
                return 0;
            case 18:
                return 0;
            case 19:
                return 0;
            case 20:
                return 0;
            case 21:
                return 0;
            case 22:
                return 0;
            case 23:
                return 0;
        }
        return 0;
    }


    public static boolean checkEmpty(String con) {
        if (TextUtils.isEmpty(con) || " ".equals(con)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            return "";
        } else {
            return "<font color= '#707070'>" + title + ":&nbsp;&nbsp;</font>";
        }
    }

    /**
     * 根据工种格式化做工内容
     *
     * @param type
     * @param content
     * @return
     */
    public static String[] TypeToContent(int type, String content) {
        switch (type) {
            case 1://预约时间+ 重量 +内容
                try {
                    CityServiceModel modle = ParseJsonUtils.getBean((String) content, CityServiceModel.class);
                    long timelong = modle.getSubscribeTime();
                    String time = "";
                    if (timelong > 0) {
                        time = getTitle("配送时间") + Utils.getDateToString(timelong, "yyyy年MM月dd日HH:mm");
                    } else {
                        time = getTitle("配送时间") + "立即配送";
                    }
                    String kg = getTitle("物品重量") + modle.getWeight() + "kg";
                    String con = checkEmpty(modle.getContent()) ? kg : kg + "<br/>" + getTitle("物品描述及要求") + modle.getContent();

                    return new String[]{con, time};
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new String[]{"", ""};
            case 2://内容: 年级+ 课程 + 时长 +天数 + 性别 + 特殊要求+学生情况描述
            case 3://艺术家教
                try {
                    TutorModel modle = ParseJsonUtils.getBean((String) content, TutorModel.class);
                    String classes = checkEmpty(modle.getGrade()) ? "" : getTitle("年级") + modle.getGrade() + "<br/>";
                    String course = checkEmpty(modle.getCourse()) ? "" : getTitle("课程") + modle.getCourse() + "<br/>";
                    String time = checkEmpty(modle.getStageTime()) ? "" : getTitle("工作时间段") + "<br/>" + modle.getStageTime() + "<br/>";
                    String day = checkEmpty(modle.getDayNum()) ? "" : getTitle("工作天数") + modle.getDayNum();
                    String sex = checkEmpty(modle.getSex()) ? "" : getTitle("性别要求") + modle.getSex() + "<br/>";
                    long timelong = modle.getStartTime();
                    String start_time = getTitle("预约工作时间") + Utils.getDateToString(timelong, "yyyy年MM月dd日") + "<br/>";
                    String spec = checkEmpty(modle.getSpecialContent()) ? "" : getTitle("特殊要求") + modle.getSpecialContent() + "<br/>";
                    String con = checkEmpty(modle.getContent()) ? "" : getTitle("学生情况描述") + modle.getContent();
                    return new String[]{classes + course + sex + spec + con, start_time + time + day};
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new String[]{"", ""};

            case 31://充场
            case 4://钟点工
            case 23://保洁
            case 5://护工
            case 6://保姆
            case 8://传单派发
            case 22://促销导购
            case 10://服务员、保安
                try {
                    PartJobModel modle = ParseJsonUtils.getBean((String) content, PartJobModel.class);
                    long timelong = modle.getStartTime();
                    String time = getTitle("预约工作时间") + Utils.getDateToString(timelong, "yyyy年MM月dd日HH:mm") + "<br/>";
                    String danwei = checkEmpty(modle.getTimeUnit()) ? "" : modle.getTimeUnit();
                    String time_long = checkEmpty(modle.getTimeDuration()) ? "" : getTitle("需求时间") + modle.getTimeDuration() + danwei;
                    String sex = checkEmpty(modle.getSex()) ? "" : getTitle("性别要求") + modle.getSex() + "<br/>";
                    String con = checkEmpty(modle.getContent()) ? "" : getTitle("工作内容及要求") + modle.getContent();

                    return new String[]{sex + con, time + time_long};
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new String[]{"", ""};


            case 11://礼仪模特
                try {
                    MoTeModel modle = ParseJsonUtils.getBean((String) content, MoTeModel.class);
                    long timelong = modle.getStartTime();
                    String time = getTitle("预约工作时间") + Utils.getDateToString(timelong, "yyyy年MM月dd日HH:mm") + "<br/>";
                    String danwei = checkEmpty(modle.getTimeUnit()) ? "" : modle.getTimeUnit();
                    String time_long = checkEmpty(modle.getTimeDuration()) ? "" : getTitle("需求时间") + modle.getTimeDuration() + danwei;
                    String sex = checkEmpty(modle.getSex()) ? "" : getTitle("性别要求") + modle.getSex() + "<br/>";
                    String shen = checkEmpty(modle.getStatureContent()) ? "" : getTitle("身材要求") + modle.getStatureContent() + "<br/>";
                    String con = checkEmpty(modle.getContent()) ? "" : getTitle("工作内容及要求") + modle.getContent();

                    return new String[]{sex + shen + con, time + time_long};
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new String[]{"", ""};


            case 12://健身教练、运动陪练

                try {
                    MotionModel modle = ParseJsonUtils.getBean((String) content, MotionModel.class);
                    long timelong = modle.getStartTime();
                    String time = getTitle("预约工作时间") + Utils.getDateToString(timelong, "yyyy年MM月dd日HH:mm") + "<br/>";
                    String danwei = checkEmpty(modle.getTimeUnit()) ? "" : modle.getTimeUnit();
                    String time_long = checkEmpty(modle.getTimeDuration()) ? "" : getTitle("需求时间") + modle.getTimeDuration() + danwei;
                    String sex = checkEmpty(modle.getSex()) ? "" : getTitle("性别要求") + modle.getSex() + "<br/>";
                    String con = checkEmpty(modle.getContent()) ? "" : getTitle("工作内容及要求") + modle.getContent();


                    return new String[]{sex + con, time + time_long};
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new String[]{"", ""};
            case 7://厨师
            case 14://车辆服务
            case 16://宠物服务
            case 17://电器服务
            case 18://电脑维修
            case 25://衣物干洗
            case 26://电动车维修
            case 27://手机维修
            case 29://上门开锁
            case 30://下水道疏通

                try {
                    OtherServiceModel modle = ParseJsonUtils.getBean((String) content, OtherServiceModel.class);
                    long timelong = modle.getStartTime();
                    String time = getTitle("预约工作时间") + Utils.getDateToString(timelong, "yyyy年MM月dd日HH:mm");
                    String con = checkEmpty(modle.getContent()) ? "" : getTitle("工作内容及要求") + modle.getContent();

                    return new String[]{con, time};

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new String[]{"", ""};

            case 15://美容化妆

                try {
                    BeautyModel modle = ParseJsonUtils.getBean((String) content, BeautyModel.class);
                    long timelong = modle.getStartTime();
                    String time = getTitle("预约工作时间") + Utils.getDateToString(timelong, "yyyy年MM月dd日HH:mm");
                    String sex = checkEmpty(modle.getSex()) ? "" : getTitle("性别要求") + modle.getSex() + "<br/>";
                    String con = checkEmpty(modle.getContent()) ? "" : getTitle("工作内容及要求") + modle.getContent();

                    return new String[]{sex + con, time};
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new String[]{"", ""};

            case 9: //导游
                try {
                    GuideContentModel modle = ParseJsonUtils.getBean((String) content, GuideContentModel.class);
                    String danwei = checkEmpty(modle.getTimeUnit()) ? "" : modle.getTimeUnit();
                    String jindian = checkEmpty(modle.getSpotAddress()) ? "" : getTitle("景点地址") + modle.getSpotAddress() + "<br/>";
                    String dizhi = checkEmpty(modle.getAddress()) ? "" : getTitle("会合地址") + modle.getAddress() + "<br/>";
                    long timelong = modle.getStartTime();
                    String yuyue = getTitle("预约工作时间") + Utils.getDateToString(timelong, "yyyy年MM月dd日HH:mm") + "<br/>";
                    String sex = checkEmpty(modle.getSex()) ? "" : getTitle("性别要求") + modle.getSex() + "<br/>";
                    String day = checkEmpty(modle.getTimeDuration()) ? "" : getTitle("需求时间") + modle.getTimeDuration() + danwei;
                    String jobContent = checkEmpty(modle.getContent()) ? "" : getTitle("工作内容及要求") + modle.getContent();
                    return new String[]{jindian + dizhi + sex + jobContent, yuyue + day};
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new String[]{"", ""};
            case 19://技工
            case 20://工厂临时用工
            case 21://搬运工
            case 28://保安
                try {
                    FactoryModel modle = ParseJsonUtils.getBean((String) content, FactoryModel.class);
                    long timelong = modle.getStartTime();
                    String time = getTitle("预约工作时间") + Utils.getDateToString(timelong, "yyyy年MM月dd日HH:mm") + "<br/>";
                    String danwei = checkEmpty(modle.getTimeUnit()) ? "" : modle.getTimeUnit();
                    String time_long = checkEmpty(modle.getTimeDuration()) ? "" : getTitle("需求时间") + modle.getTimeDuration() + danwei;
                    String con = checkEmpty(modle.getContent()) ? "" : getTitle("工作内容及要求") + modle.getContent();
                    return new String[]{con, time + time_long};
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new String[]{"", ""};
            case 24://游戏陪练
                try {
                    MotionModel modle = ParseJsonUtils.getBean((String) content, MotionModel.class);
                    long timelong = modle.getStartTime();
                    String time = getTitle("预约工作时间") + Utils.getDateToString(timelong, "yyyy年MM月dd日HH:mm") + "<br/>";
                    String danwei = checkEmpty(modle.getTimeUnit()) ? "" : modle.getTimeUnit();
                    String time_long = checkEmpty(modle.getTimeDuration()) ? "" : getTitle("需求时间") + modle.getTimeDuration() + danwei;
                    String con = checkEmpty(modle.getContent()) ? "" : getTitle("工作内容及要求") + modle.getContent();


                    return new String[]{con, time + time_long};
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new String[]{"", ""};

            case 32://茶叶交易
            case 34://花卉苗木
            case 35://林业产品
            case 41://中药材交易
                try {
                    MedicinalModel modle = ParseJsonUtils.getBean((String) content, MedicinalModel.class);
                    String mudi = checkEmpty(modle.getReleasePurpose()) ? "" : getTitle("发布目的") + modle.getReleasePurpose() + "<br/>";
                    String productName = checkEmpty(modle.getProductName()) ? "" : getTitle("产品名称") + modle.getProductName() + "<br/>";
                    String origin = checkEmpty(modle.getOrigin()) ? "" : getTitle("产地") + modle.getOrigin() + "<br/>";
                    String productNum = checkEmpty(modle.getProductNum()) ? "" : getTitle("产品数量") + modle.getProductNum() + "<br/>";
                    String priceInfo = checkEmpty(modle.getPriceInfo()) ? "" : getTitle("价格说明") + modle.getPriceInfo() + "<br/>";
                    String con = checkEmpty(modle.getContent()) ? "" : getTitle("其他描述及特殊要求") + modle.getContent();
                    long timelong = modle.getEndTime();
                    String time = getTitle("信息有效时间") + Utils.getDateToString(timelong, "yyyy年MM月dd日HH:mm");
                    return new String[]{mudi + productName + origin + productNum + priceInfo + con, time};
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new String[]{"", ""};

            case 33://果蔬粮油
            case 37://农用物资
            case 38://禽畜水产
            case 40://文化艺术
                try {
                    AgricultureModel modle = ParseJsonUtils.getBean((String) content, AgricultureModel.class);
                    String mudi = checkEmpty(modle.getReleasePurpose()) ? "" : getTitle("发布目的") + modle.getReleasePurpose() + "<br/>";
                    String productType = checkEmpty(modle.getProductType()) ? "" : getTitle("产品类别") + modle.getProductType() + "<br/>";
                    String productName = checkEmpty(modle.getProductName()) ? "" : getTitle("产品名称") + modle.getProductName() + "<br/>";
                    String origin = checkEmpty(modle.getOrigin()) ? "" : getTitle("产地") + modle.getOrigin() + "<br/>";
                    String productNum = checkEmpty(modle.getProductNum()) ? "" : getTitle("产品数量") + modle.getProductNum() + "<br/>";
                    String priceInfo = checkEmpty(modle.getPriceInfo()) ? "" : getTitle("价格说明") + modle.getPriceInfo() + "<br/>";
                    String con = checkEmpty(modle.getContent()) ? "" : getTitle("其他描述及特殊要求") + modle.getContent();
                    long timelong = modle.getEndTime();
                    String time = getTitle("信息有效时间") + Utils.getDateToString(timelong, "yyyy年MM月dd日HH:mm");
                    return new String[]{mudi + productType + productName + origin + productNum + priceInfo + con, time};
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new String[]{"", ""};

            case 39://土地承租
                try {
                    LandTenantModel modle = ParseJsonUtils.getBean((String) content, LandTenantModel.class);
                    String mudi = checkEmpty(modle.getReleasePurpose()) ? "" : getTitle("发布目的") + modle.getReleasePurpose() + "<br/>";
                    String productType = checkEmpty(modle.getLandType()) ? "" : getTitle("土地类型") + modle.getLandType() + "<br/>";
                    String productName = checkEmpty(modle.getOtherTag()) ? "" : getTitle("其它标记") + modle.getOtherTag() + "<br/>";
                    String origin = checkEmpty(modle.getLandAddress()) ? "" : getTitle("土地所在地") + modle.getLandAddress() + "<br/>";
                    String productNum = checkEmpty(modle.getLandArea()) ? "" : getTitle("面积") + modle.getLandArea() + "<br/>";
                    String priceInfo = checkEmpty(modle.getPriceInfo()) ? "" : getTitle("价格说明") + modle.getPriceInfo() + "<br/>";
                    String con = checkEmpty(modle.getContent()) ? "" : getTitle("其他描述及特殊要求") + modle.getContent();
                    long timelong = modle.getEndTime();
                    String time = getTitle("信息有效时间") + Utils.getDateToString(timelong, "yyyy年MM月dd日HH:mm");
                    return new String[]{mudi + productType + productName + origin + productNum + priceInfo + con, time};
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new String[]{"", ""};

            case 36://民宿
                try {
                    HouseModel modle = ParseJsonUtils.getBean((String) content, HouseModel.class);
                    String mudi = checkEmpty(modle.getReleasePurpose()) ? "" : getTitle("发布目的") + modle.getReleasePurpose() + "<br/>";
                    String productType = checkEmpty(modle.getHouseAddress()) ? "" : getTitle("房源所在地") + modle.getHouseAddress() + "<br/>";
                    String priceInfo = checkEmpty(modle.getPriceInfo()) ? "" : getTitle("价格说明") + modle.getPriceInfo() + "<br/>";
                    String productName = checkEmpty(modle.getHouseType()) ? "" : getTitle("房源类型") + modle.getHouseType() + "<br/>";
                    String origin = checkEmpty(modle.getHouseArea()) ? "" : getTitle("房源面积") + modle.getHouseArea() + "<br/>";
                    String huxing = checkEmpty(modle.getHouseDoor()) ? "" : getTitle("房源户型") + modle.getHouseDoor() + "<br/>";
                    String productNum = checkEmpty(modle.getHousePeopleNum()) ? "" : getTitle("可入住人数") + modle.getHousePeopleNum() + "<br/>";
                    String houseDay = checkEmpty(modle.getHouseDay()) ? "" : getTitle("最少入住天数") + modle.getHouseDay() + "<br/>";
                    String facilities = checkEmpty(modle.getFacilities()) ? "" : getTitle("配套设施") + modle.getFacilities() + "<br/>";
                    String con = checkEmpty(modle.getContent()) ? "" : getTitle("其他描述及特殊要求") + modle.getContent();
                    long timelong = modle.getEndTime();
                    String time = getTitle("信息有效时间") + Utils.getDateToString(timelong, "yyyy年MM月dd日HH:mm");
                    return new String[]{mudi + productType + priceInfo + productName + origin+huxing + productNum +houseDay+ facilities + con, time};
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new String[]{"", ""};

        }
        return new String[]{"", ""};
    }

    /**
     * 根据工种格式化做工内容
     *
     * @param type
     * @param content
     * @return
     */
    public static String TypeToContent2(int type, String content) {
        try {
            CommonTypeModel modle = ParseJsonUtils.getBean((String) content, CommonTypeModel.class);
            return modle.getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String countDistance(String jing, String wei) {

        LatLng start = null;
        LatLng end = null;
        if (!TextUtils.isEmpty(jing) && !TextUtils.isEmpty(wei)) {
            start = new LatLng(Float.valueOf(wei), Float.valueOf(jing));
        } else {
            return "不详";
        }
        UserInfoModel.UserPlaceBean userPlace = MaiLiApplication.getInstance().getUserPlace();
        if (!TextUtils.isEmpty(userPlace.getWei()) && !TextUtils.isEmpty(userPlace.getJing())) {
            end = new LatLng(Float.valueOf(userPlace.getWei()), Float.valueOf(userPlace.getJing()));
        } else {
            return "不详";
        }

        int distance = (int) AMapUtils.calculateLineDistance(start, end);
        if (distance < 1000) {
            return distance + "m";
        } else {
            NumberFormat ddf1 = NumberFormat.getNumberInstance();//保留一位小数
            ddf1.setMaximumFractionDigits(1);

            float di = (float) distance / 1000;
            return ddf1.format(di) + "km";
        }
    }

    public static String countDistance1(Long distance) {
        if (distance < 1000) {
            return distance + "m";
        } else {
            NumberFormat ddf1 = NumberFormat.getNumberInstance();//保留一位小数
            ddf1.setMaximumFractionDigits(1);
            float di = (float) distance / 1000;
            return ddf1.format(di) + "km";
        }
    }

    public static void wakeUpAndUnlock(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();

    }

    public static Double formatDoubleDOWN(double d) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        // 保留两位小数
        nf.setMaximumFractionDigits(2);
        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        nf.setRoundingMode(RoundingMode.DOWN);
        return Double.valueOf(nf.format(d).replace(",", ""));
    }

    /**
     * 根据byte数组，生成图片
     */
    public static String saveJPGFile(byte[] data) {
        if (data == null)
            return null;
        SimpleDateFormat t = new SimpleDateFormat("yyyyMMddssSSS");
        String filename = "MT" + (t.format(new Date())) + ".jpg";
        String state = MaiLiApplication.BASEPHOTOURL;

        File photoFile = new File(state + "/" + filename);// 图片储存路径
        if (!photoFile.getParentFile().exists()) {
            photoFile.getParentFile().mkdirs();
        }
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(state + "/" + filename);
            bos = new BufferedOutputStream(fos);
            bos.write(data);
            return state + "/" + filename;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }


    //以下代码可以跳转到应用详情，可以通过应用详情跳转到权限界面(6.0系统测试可用)
    public static void getAppDetailSettingIntent(Activity activity) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
        }
        activity.startActivityForResult(localIntent, 1001);
    }

}
