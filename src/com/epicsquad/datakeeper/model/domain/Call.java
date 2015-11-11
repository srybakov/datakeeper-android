package com.epicsquad.datakeeper.model.domain;

import java.util.Date;

public class Call {

    public Call() {
    }

    private CallType type;
    private long duration;
    private long timestamp;
    private String number;

    public CallType getType() {
        return type;
    }

    public void setType(CallType type) {
        this.type = type;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Call)) return false;

        Call call = (Call) o;

        if (duration != call.duration) return false;
        if (timestamp != call.timestamp) return false;
        if (!number.equals(call.number)) return false;
        if (type != call.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (int) (duration ^ (duration >>> 32));
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + number.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(new Date(timestamp)).append(":");
        if (CallType.INCOMING.equals(type)){
            sb.append("Входящий.");
        } else if (CallType.OUTGOING.equals(type)){
            sb.append("Исходящий.");
        } else if (CallType.MISSED.equals(type)){
            sb.append("Пропущенный.");
        }
        sb.append(" Длительность:").append(duration).append(":").append(number);
        return sb.toString();
    }
}
