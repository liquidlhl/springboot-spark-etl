package com.lhl.springbootsparketl.service.imp;

import com.alibaba.fastjson.JSON;
import com.lhl.springbootsparketl.entity.*;
import com.lhl.springbootsparketl.service.SparkEtlService;
import org.apache.spark.Accumulator;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.storage.StorageLevel;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import scala.Tuple2;

import java.io.Serializable;
import java.util.*;

/**
 * SparkEtlServiceImpl
 *
 * @author:
 * @create: 2019-12-29
 **/
@Service
public class SparkEtlServiceImpl implements SparkEtlService, Serializable {

    @Autowired
    WbEsDataServiceImpl wbEsDataService;

    @Override
    public void run(JavaSparkContext jsc, SearchParams searchParams) {
        JavaPairRDD<String, String> contentRdd = wbEsDataService.readWbContentRdd(jsc, searchParams);
        JavaPairRDD<String, String> userRdd = wbEsDataService.readWbUserRdd(jsc, searchParams);


        JavaPairRDD<String, WbContent> uidContentRdd = contentRdd.mapToPair(e -> {
            WbContent wbContent = JSON.parseObject(e._2(), WbContent.class);
            String uid = wbContent.getUid();
            return new Tuple2<>(uid, wbContent);
            //调优: 对多次使用的rdd进行持久化 避免重复计算rdd
        }).persist(StorageLevel.MEMORY_AND_DISK());


        JavaPairRDD<String, WbUser> idUserRdd = userRdd.mapToPair(e -> {
            WbUser wbUser = JSON.parseObject(e._2(), WbUser.class);
            String uid = wbUser.getId();
            return new Tuple2<>(uid, wbUser);
        });

//        使用Accumulator时，为了保证准确性，只使用一次action操作。或者用cache切断依赖
        Accumulator<Integer> positiveSentimentCount = jsc.accumulator(0, "positive_sentiment_count");
        Accumulator<Integer> neutralSentimentCount = jsc.accumulator(0, "neutral_sentiment_count");
        Accumulator<Integer> negativeSentimentCount = jsc.accumulator(0, "negative_sentiment_count");


        uidContentRdd.foreachPartition(tuple2Iterator -> {
            while (tuple2Iterator.hasNext()) {
                WbContent wbContent = tuple2Iterator.next()._2;
                Integer sentiment = wbContent.getSentiment();
                if (sentiment == -1) {
                    negativeSentimentCount.add(1);
                } else if (sentiment == 0) {
                    neutralSentimentCount.add(1);
                } else if (sentiment == 1) {
                    positiveSentimentCount.add(1);
                }
            }
        });
        ResultStats resultStats = new ResultStats();
        //保存帖子情感分布的结果
        resultStats.setSentiment("-1:" + negativeSentimentCount + "," + "0:" + neutralSentimentCount + "," + "1:" + positiveSentimentCount);

        Accumulator<Integer> fCount = jsc.accumulator(0, "f_count");
        Accumulator<Integer> mCount = jsc.accumulator(0, "m_count");
//        join：相当于mysql的INNER JOIN，当join左右两边的数据集都存在时才返回
        JavaPairRDD<String, Tuple2<WbUser, WbContent>> join = idUserRdd.join(uidContentRdd);

        Accumulator<HashMap<Integer, Integer>> ageAccumutor = jsc.accumulator(new HashMap<Integer, Integer>(100), new AgeAccumulator());

        //分析用户的性别及年龄分布
        join.foreachPartition(tuple2Iterator -> {
            HashMap<Integer, Integer> ageMap = new HashMap(100);
            while (tuple2Iterator.hasNext()) {
                WbUser wbUser = tuple2Iterator.next()._2()._1;
                String gender = wbUser.getGender();
                if (!StringUtils.isEmpty(gender)) {
                    if (gender.equals("f")) {
                        fCount.add(1);
                    } else if (gender.equals("m")) {
                        mCount.add(1);
                    }
                }

                Integer birthyear = wbUser.getBirthyear();
                if (!StringUtils.isEmpty(birthyear)) {
                    int curYear = DateTime.now().get(DateTimeFieldType.year());
                    int age = curYear - birthyear;
                    Integer value = ageMap.get(age);
                    if (value == null) {
                        ageMap.put(age, 1);
                    } else {
                        ageMap.put(age, value + 1);
                    }
                }

            }
            ageAccumutor.add(ageMap);
        });

        resultStats.setGender("f:" + fCount + "," + "m:" + mCount);

        resultStats.setAge(ageAccumutor.value());


        System.out.println("resultStats = " + resultStats);
        System.out.println("JSON.toJSONString(resultStats) = " + JSON.toJSONString(resultStats));

        try {
            Thread.sleep(1000 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


}
