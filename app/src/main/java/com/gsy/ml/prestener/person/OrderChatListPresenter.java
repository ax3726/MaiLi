package com.gsy.ml.prestener.person;


import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.OrderChatListModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/4/14.
 */

public class OrderChatListPresenter {
    final int FINDCHATLIST = 1;
    int requestType = FINDCHATLIST;
    private final ILoadPVListener mListener;

    public OrderChatListPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }


    /**
     * 查看沟通人列表
     */
    public void findChatList(String orderId,
                            String sendRingLetter,
                            String page,
                            String rows
    ) {
        requestType = FINDCHATLIST;
        Map<String, String> map = new HashMap<String, String>();
        map.put("orderId", orderId);
        map.put("sendRingLetter", sendRingLetter);
        map.put("page", page);
        map.put("rows", rows);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.FINDCHATLIST, map, callback);
    }

    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {

        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case FINDCHATLIST:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            OrderChatListModel modle = ParseJsonUtils.getBean((String) object, OrderChatListModel.class);
                            mListener.onLoadComplete(modle);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }

                    break;

            }

        }
    };

}
