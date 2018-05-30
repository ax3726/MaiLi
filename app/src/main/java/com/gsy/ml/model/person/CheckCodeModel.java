package com.gsy.ml.model.person;

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
 * @author Wanwan
 * @date 15/11/17
 */
public class CheckCodeModel {

    /**
     * info : 200
     * msg : 成功
     * data : 13
     */

    private String info;
    private String status;
    private String data;

    public void setinfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}

