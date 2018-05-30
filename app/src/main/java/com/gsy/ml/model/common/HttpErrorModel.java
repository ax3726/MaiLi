package com.gsy.ml.model.common;

/**
 * *                            _ooOoo_
 * *                           o8888888o
 * *                           88" . "88
 * *                           (| -_- |)
 * *                            O\ = /O
 * *                        ____/`---'\____
 * *                      .   ' \\| |// `.
 * *                       / \\||| : |||// \
 * *                     / _||||| -:- |||||- \
 * *                       | | \\\ - /// | |
 * *                     | \_| ''\---/'' | |
 * *                      \ .-\__ `-` ___/-. /
 * *                   ___`. .' /--.--\ `. . __
 * *                ."" '< `.___\_<|>_/___.' >'"".
 * *               | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * *                 \ \ `-. \_ __\ /__ _/ .-` / /
 * *         ======`-.____`-.___\_____/___.-`____.-'======
 * *                            `=---='
 * *         .............................................
 * *
 * *                 佛祖保佑             永无BUG
 * *          佛曰:
 * *                  别人笑我太疯癫，我笑他人看不穿。
 *
 * @author Young
 * @date 15/8/19
 * 网络错误实体类
 */
public class HttpErrorModel {

    public static final String HTTP_ERROR_CONTAINS_STR = "\"status\":\"200\"";
    public static final String HTTP_ERROR_CONTAINS_STR2 = "{\"status\":\"200\"";
    /**
     * msg : 暂无本周课程信息
     * data :
     * info : 40012
     */
    private String status;
    private String data;
    private String info;


    public void setData(String data) {
        this.data = data;
    }

    public void setinfo(String info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getData() {
        return data;
    }

    public String getinfo() {
        return info;
    }

    public HttpErrorModel(String info, String data, String status) {
        this.info = info;
        this.data = data;
        this.status = status;

    }

    public static HttpErrorModel createError() {
        return new HttpErrorModel("网络不稳定，请稍后再试!", "", "404");
    }

    public static HttpErrorModel createNetWorkError() {
        return new HttpErrorModel("网络不可用，请稍后重试！", "", "309");
    }

    public static HttpErrorModel createError(String errer) {
        return new HttpErrorModel(errer, "", "404");
    }
}



