package com.grant.other;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grant on 2018/1/4/004.
 */
public class Test2 {
    public static void main(String[] args) {
        FlowBean fb = new FlowBean();
        fb.set(1,2);
        List<FlowBean> listflow = new ArrayList<FlowBean>();
        listflow.add(fb);
        for (FlowBean fbs: listflow) {
            System.out.println(fbs.getUpFlow()+","+fbs.getdFlow()+","+fbs.getSumFlow());
        }
        fb.set(10,20);
        for (FlowBean fbs: listflow) {
            System.out.println(fbs.getUpFlow()+","+fbs.getdFlow()+","+fbs.getSumFlow());
        }
        Integer i =10;
        ArrayList<Integer> a = new ArrayList<Integer>();
        a.add(i);
        for (Integer ii: a) {
            System.out.println(ii);
        }
        i=20;
        for (Integer ii: a) {
            System.out.println(ii);
        }
    }
}
