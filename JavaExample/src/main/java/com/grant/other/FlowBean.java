package com.grant.other;

/**
 * Created by Grant on 2018/1/4/004.
 */
public class FlowBean {
    private String number;
    private long upFlow;
    private long dFlow;
    private long sumFlow;

    public FlowBean(String number, long upFlow, long dFlow, long sumFlow) {
        this.number = number;
        this.upFlow = upFlow;
        this.dFlow = dFlow;
        this.sumFlow = sumFlow;
    }

    public FlowBean() {
    }

    public void set(long upFlow, long dFlow){
        this.upFlow=upFlow;
        this.dFlow = dFlow;
        this.sumFlow = upFlow+dFlow;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getUpFlow() {
        return upFlow;
    }

    public void setUpFlow(long upFlow) {
        this.upFlow = upFlow;
    }

    public long getdFlow() {
        return dFlow;
    }

    public void setdFlow(long dFlow) {
        this.dFlow = dFlow;
    }

    public long getSumFlow() {
        return sumFlow;
    }

    public void setSumFlow(long sumFlow) {
        this.sumFlow = sumFlow;
    }
}
