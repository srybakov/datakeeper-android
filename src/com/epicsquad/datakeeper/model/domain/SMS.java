package com.epicsquad.datakeeper.model.domain;

import java.util.Date;

public class SMS {

    public SMS() {
    }

    private SMSType type;
    private String number;
    private long timestamp;
    private String text;

    public SMSType getType() {
        return type;
    }

    public void setType(SMSType type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SMS)) return false;

        SMS sms = (SMS) o;

        return timestamp == sms.timestamp && number.equals(sms.number) && text.equals(sms.text) && type == sms.type;

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + number.hashCode();
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + text.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(new Date(timestamp)).append(":");
        if (SMSType.INCOMING.equals(type)){
            sb.append("Входящее.");
        } else if (SMSType.OUTGOING.equals(type)){
            sb.append("Исходящее.");
        }
        sb.append(number).append(":").append(text);
        return sb.toString();
    }
}
