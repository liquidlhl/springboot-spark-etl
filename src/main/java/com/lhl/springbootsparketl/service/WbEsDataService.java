package com.lhl.springbootsparketl.service;

import com.lhl.springbootsparketl.entity.SearchParams;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * WbEsDataService
 *
 * @author:
 * @create: 2019-12-29
 **/
public interface WbEsDataService {

    JavaPairRDD<String, String> readWbUserRdd(JavaSparkContext jsc, SearchParams searchParams);
    JavaPairRDD<String, String> readWbContentRdd(JavaSparkContext jsc, SearchParams searchParams);

}
