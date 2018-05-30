package com.gsy.ml.model.person;

import java.util.List;

/**
 * Created by Administrator on 2017/5/24.
 */

public class WorkAreaModel {

    /**
     * chinaList : [{"code":330102,"parentcode":330100,"type":3,"name":"上城","fullname":"上城区"},{"code":330103,"parentcode":330100,"type":3,"name":"下城","fullname":"下城区"},{"code":330104,"parentcode":330100,"type":3,"name":"江干","fullname":"江干区"},{"code":330105,"parentcode":330100,"type":3,"name":"拱墅","fullname":"拱墅区"},{"code":330106,"parentcode":330100,"type":3,"name":"西湖","fullname":"西湖区"},{"code":330108,"parentcode":330100,"type":3,"name":"滨江","fullname":"滨江区"},{"code":330109,"parentcode":330100,"type":3,"name":"萧山","fullname":"萧山区"},{"code":330110,"parentcode":330100,"type":3,"name":"余杭","fullname":"余杭区"},{"code":330122,"parentcode":330100,"type":3,"name":"桐庐","fullname":"桐庐县"},{"code":330127,"parentcode":330100,"type":3,"name":"淳安","fullname":"淳安县"},{"code":330182,"parentcode":330100,"type":3,"name":"建德","fullname":"建德市"},{"code":330183,"parentcode":330100,"type":3,"name":"富阳","fullname":"富阳区"},{"code":330185,"parentcode":330100,"type":3,"name":"临安","fullname":"临安市"}]
     * userPreselectedPlace : {"id":1,"phone":"15170193726","place":"拱墅,西湖,"}
     * status : 200
     * info : 查询成功
     */

    private UserPreselectedPlaceBean userPreselectedPlace;
    private String status;
    private String info;
    private List<ChinaListBean> chinaList;

    public UserPreselectedPlaceBean getUserPreselectedPlace() {
        return userPreselectedPlace;
    }

    public void setUserPreselectedPlace(UserPreselectedPlaceBean userPreselectedPlace) {
        this.userPreselectedPlace = userPreselectedPlace;
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

    public List<ChinaListBean> getChinaList() {
        return chinaList;
    }

    public void setChinaList(List<ChinaListBean> chinaList) {
        this.chinaList = chinaList;
    }

    public static class UserPreselectedPlaceBean {
        /**
         * id : 1
         * phone : 15170193726
         * place : 拱墅,西湖,
         */

        private int id;
        private String phone;
        private String place;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }
    }

    public static class ChinaListBean {
        /**
         * code : 330102
         * parentcode : 330100
         * type : 3
         * name : 上城
         * fullname : 上城区
         */

        private int code;
        private int parentcode;
        private int type;
        private String name;
        private String fullname;
        private boolean is_select;

        public boolean is_select() {
            return is_select;
        }

        public void setIs_select(boolean is_select) {
            this.is_select = is_select;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public int getParentcode() {
            return parentcode;
        }

        public void setParentcode(int parentcode) {
            this.parentcode = parentcode;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }
    }
}
