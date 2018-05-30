package com.gsy.ml.model.person;

import java.util.List;

/**
 * Created by Administrator on 2017/5/4.
 * 钱包数据集
 */

public class WalletModel {

    /**
     * data : {"financialRecord":[{"id":1,"phone":"18397845702","time":1494130123784,"money":73,"moneyType":1,"moneyAbstract":"三墩镇同人精华大厦底商16号同城配送发单扣除的金额","personHand":null,"remarks":null},{"id":21,"phone":"18397845702","time":1494148254232,"money":54,"moneyType":1,"moneyAbstract":"紫荆花路858号艺术家教发单扣除的金额","personHand":null,"remarks":null},{"id":23,"phone":"18397845702","time":1494150420740,"money":90,"moneyType":1,"moneyAbstract":"萍水西街216号电脑修理、系统安装发单扣除的金额","personHand":null,"remarks":null},{"id":60,"phone":"18397845702","time":1494213590886,"money":60,"moneyType":1,"moneyAbstract":"三墩镇同人精华3号楼内导游发单扣除的金额","personHand":null,"remarks":null},{"id":65,"phone":"18397845702","time":1494215750840,"money":69,"moneyType":1,"moneyAbstract":"古墩路616号同人精华大厦1座底层商铺608-1号导游发单扣除的金额","personHand":null,"remarks":null},{"id":66,"phone":"18397845702","time":1494223590198,"money":57,"moneyType":1,"moneyAbstract":"萍水西街耀江文鼎院215号导游发单扣除的金额","personHand":null,"remarks":null},{"id":67,"phone":"18397845702","time":1494223726055,"money":79,"moneyType":1,"moneyAbstract":"莫干山路1135号导游发单扣除的金额","personHand":null,"remarks":null},{"id":68,"phone":"18397845702","time":1494224002325,"money":65,"moneyType":1,"moneyAbstract":"三墩镇萍水西街209号导游发单扣除的金额","personHand":null,"remarks":null},{"id":70,"phone":"18397845702","time":1494224195937,"money":67,"moneyType":1,"moneyAbstract":"古墩路与萍水西街交叉口北150米导游发单扣除的金额","personHand":null,"remarks":null},{"id":71,"phone":"18397845702","time":1494224509505,"money":15,"moneyType":1,"moneyAbstract":"三墩镇萍水西街209号导游发单扣除的金额","personHand":null,"remarks":null}],"workGet":1167.3,"money":0,"acceptOrdersList":[],"sendOrdersList":[],"teamBonusCash":500,"count":0,"totalMoney":9992065.86,"teamBonus":1000,"workGetCash":967.3}
     * status : 200
     * info : 查询成功
     */

    private DataBean data;
    private String status;
    private String info;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
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

    public static class DataBean {
        /**
         * financialRecord : [{"id":1,"phone":"18397845702","time":1494130123784,"money":73,"moneyType":1,"moneyAbstract":"三墩镇同人精华大厦底商16号同城配送发单扣除的金额","personHand":null,"remarks":null},{"id":21,"phone":"18397845702","time":1494148254232,"money":54,"moneyType":1,"moneyAbstract":"紫荆花路858号艺术家教发单扣除的金额","personHand":null,"remarks":null},{"id":23,"phone":"18397845702","time":1494150420740,"money":90,"moneyType":1,"moneyAbstract":"萍水西街216号电脑修理、系统安装发单扣除的金额","personHand":null,"remarks":null},{"id":60,"phone":"18397845702","time":1494213590886,"money":60,"moneyType":1,"moneyAbstract":"三墩镇同人精华3号楼内导游发单扣除的金额","personHand":null,"remarks":null},{"id":65,"phone":"18397845702","time":1494215750840,"money":69,"moneyType":1,"moneyAbstract":"古墩路616号同人精华大厦1座底层商铺608-1号导游发单扣除的金额","personHand":null,"remarks":null},{"id":66,"phone":"18397845702","time":1494223590198,"money":57,"moneyType":1,"moneyAbstract":"萍水西街耀江文鼎院215号导游发单扣除的金额","personHand":null,"remarks":null},{"id":67,"phone":"18397845702","time":1494223726055,"money":79,"moneyType":1,"moneyAbstract":"莫干山路1135号导游发单扣除的金额","personHand":null,"remarks":null},{"id":68,"phone":"18397845702","time":1494224002325,"money":65,"moneyType":1,"moneyAbstract":"三墩镇萍水西街209号导游发单扣除的金额","personHand":null,"remarks":null},{"id":70,"phone":"18397845702","time":1494224195937,"money":67,"moneyType":1,"moneyAbstract":"古墩路与萍水西街交叉口北150米导游发单扣除的金额","personHand":null,"remarks":null},{"id":71,"phone":"18397845702","time":1494224509505,"money":15,"moneyType":1,"moneyAbstract":"三墩镇萍水西街209号导游发单扣除的金额","personHand":null,"remarks":null}]
         * workGet : 1167.3
         * money : 0.0
         * acceptOrdersList : []
         * sendOrdersList : []
         * teamBonusCash : 500.0
         * count : 0
         * totalMoney : 9992065.86
         * teamBonus : 1000.0
         * workGetCash : 967.3
         * rechargeReward : 100.0
         */

        private double workGet;
        private double money;
        private double teamBonusCash;
        private int count;
        private double totalMoney;
        private double teamBonus;
        private double workGetCash;
        private double deposit;
        private double binging;
        private double rechargeReward;
        private List<FinancialRecordBean> financialRecord;

        public double getDeposit() {
            return deposit;
        }

        public void setDeposit(double deposit) {
            this.deposit = deposit;
        }

        public double getBinging() {
            return binging;
        }

        public void setBinging(double binging) {
            this.binging = binging;
        }

        public double getWorkGet() {
            return workGet;
        }

        public void setWorkGet(double workGet) {
            this.workGet = workGet;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public double getTeamBonusCash() {
            return teamBonusCash;
        }

        public void setTeamBonusCash(double teamBonusCash) {
            this.teamBonusCash = teamBonusCash;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public double getTotalMoney() {
            return totalMoney;
        }

        public void setTotalMoney(double totalMoney) {
            this.totalMoney = totalMoney;
        }

        public double getTeamBonus() {
            return teamBonus;
        }

        public void setTeamBonus(double teamBonus) {
            this.teamBonus = teamBonus;
        }

        public double getWorkGetCash() {
            return workGetCash;
        }

        public void setWorkGetCash(double workGetCash) {
            this.workGetCash = workGetCash;
        }

        public double getRechargeReward() {
            return rechargeReward;
        }

        public void setRechargeReward(double rechargeReward) {
            this.rechargeReward = rechargeReward;
        }

        public List<FinancialRecordBean> getFinancialRecord() {
            return financialRecord;
        }

        public void setFinancialRecord(List<FinancialRecordBean> financialRecord) {
            this.financialRecord = financialRecord;
        }


        public static class FinancialRecordBean {
            /**
             * id : 1
             * phone : 18397845702
             * time : 1494130123784
             * money : 73.0
             * moneyType : 1
             * moneyAbstract : 三墩镇同人精华大厦底商16号同城配送发单扣除的金额
             * personHand : null
             * remarks : null
             */

            private int id;
            private String phone;
            private long time;
            private double money;
            private int moneyType;
            private String moneyAbstract;
            private Object personHand;
            private Object remarks;

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

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
            }

            public int getMoneyType() {
                return moneyType;
            }

            public void setMoneyType(int moneyType) {
                this.moneyType = moneyType;
            }

            public String getMoneyAbstract() {
                return moneyAbstract;
            }

            public void setMoneyAbstract(String moneyAbstract) {
                this.moneyAbstract = moneyAbstract;
            }

            public Object getPersonHand() {
                return personHand;
            }

            public void setPersonHand(Object personHand) {
                this.personHand = personHand;
            }

            public Object getRemarks() {
                return remarks;
            }

            public void setRemarks(Object remarks) {
                this.remarks = remarks;
            }
        }
    }
}
