package com.lhl.springbootsparketl.service;

import com.lhl.springbootsparketl.entity.SearchParams;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * SparkEtlService
 *
 * @author:
 * @create: 2019-12-29
 **/
public interface SparkEtlService {

    void run(JavaSparkContext javaSparkContext, SearchParams searchParams);

}
