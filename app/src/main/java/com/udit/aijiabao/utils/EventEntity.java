package com.udit.aijiabao.utils;

/**
 * Created by Administrator on 2016/2/17 0017.
 */
public class EventEntity {

    public final static class OtherLogin {
        public int notificationId = -1;

        public OtherLogin(int id) {
            notificationId = id;
        }
    }

    public final static class NetChange {
        public String name;
        public boolean isConnected;
    }

    public final static class ReceivedInfo {

    }

    public final static class ReceivedMessage {

    }

    public final static class OpenMessage {

    }

    public final static class ApplyResp {
        public int applyId = -1;

        public ApplyResp(int id) {
            applyId = id;
        }
    }
}
