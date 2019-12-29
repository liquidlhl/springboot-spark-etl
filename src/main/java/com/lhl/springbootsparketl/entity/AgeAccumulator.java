package com.lhl.springbootsparketl.entity;

import org.apache.spark.AccumulatorParam;
import org.apache.spark.AccumulatorParam$class;

import java.util.HashMap;
import java.util.Map;

/**
 * AgeAccumulator
 * <p>
 * 自定义累加器
 * ref:https://blog.csdn.net/Purplme/article/details/84064802
 *
 * @author:
 * @create: 2019-12-29
 **/
public class AgeAccumulator implements AccumulatorParam<HashMap<Integer, Integer>> {


    @Override
    public HashMap<Integer, Integer> addAccumulator(HashMap<Integer, Integer> t1, HashMap<Integer, Integer> t2) {
//        System.out.println("t1 = " + t1);
//        System.out.println("t2 = " + t2);
        if (null == t1) return t2;
        t1.keySet().stream().forEach(e -> {
            Integer value = t1.get(e);
            if (value == null) {
                t2.put(e, 1);
            } else {
                t2.put(e, value + 1);
            }
        });

        return t2;
    }

    @Override
    public HashMap<Integer, Integer> addInPlace(HashMap<Integer, Integer> t1, HashMap<Integer, Integer> t2) {
//        System.out.println("tt1 = " + t1);
//        System.out.println("tt2 = " + t2);
        if (null == t1) return t2;
        t1.keySet().stream().forEach(e -> {
            Integer value = t1.get(e);
            if (value == null) {
                t2.put(e, 1);
            } else {
                t2.put(e, value + 1);
            }
        });

        return t2;
    }

    @Override
    public HashMap<Integer, Integer> zero(HashMap<Integer, Integer> integerIntegerHashMap) {
        return new HashMap<Integer, Integer>();
    }
}
