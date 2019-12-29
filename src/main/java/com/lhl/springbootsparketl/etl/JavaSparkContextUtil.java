package com.lhl.springbootsparketl.etl;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * SparkEtl
 *
 * @author:
 * @create: 2019-12-29
 **/
@Configuration
public class JavaSparkContextUtil {

    @Value("${es.hosts}")
    private String es_node;
    @Value("${es.port}")
    private String es_port;
    @Value("${spring.application.name}")
    private String appName;

    @Bean
    @Scope("singleton")
    public JavaSparkContext javaSparkContext() {
        SparkConf conf = new SparkConf()
                .setAppName(appName)
                .setMaster("local[*]");
        conf.set("es.index.auto.create", "true")
                .set("es.nodes", es_node)
                .set("es.port", es_port)
                .set("es.nodes.wan.only", "true")
                //调优
                //该参数用于设置shuffle过程中一个task拉取到上个stage的task的输出后，
                // 进行聚合操作时能够使用的Executor内存的比例，默认是0.2
                .set("spark.shuffle.memoryFraction", "0.3")
//        该参数用于设置RDD持久化数据在Executor内存中能占的比例，默认是0.6。
//        但是如果Spark作业中的shuffle类操作比较多，而持久化操作比较少，那么这个参数的值适当降低一些比较合适。
                .set("spark.storage.memoryFraction", "0.5");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        return jsc;
//        JavaPairRDD<String, Map<String, Object>> userRdd = JavaEsSpark.esRDD(context, userParams);

    }

}
