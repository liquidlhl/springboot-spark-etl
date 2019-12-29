package com.lhl.springbootsparketl;

import com.lhl.springbootsparketl.entity.SearchParams;
import com.lhl.springbootsparketl.service.SparkEtlService;
import com.lhl.springbootsparketl.service.WbEsDataService;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;

/**
 * SparkTest
 *
 * @author:
 * @create: 2019-12-29
 **/

@RunWith(SpringRunner.class)
@SpringBootTest()

public class SparkTest implements Serializable {
    @BeforeClass
    public static void Before() {
        //https://github.com/netty/netty/issues/6956
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    @Autowired
    ApplicationContext ioc;

    @Autowired
    JavaSparkContext jsc;

    @Autowired
    WbEsDataService wbEsDataService;

    @Autowired
    SparkEtlService sparkEtlService;

    @Test
    public void TestService() {
/*
        String[] beanDefinitionNames = ioc.getBeanDefinitionNames();
        Arrays.stream(beanDefinitionNames).forEach(System.out::println);
*/
        SearchParams searchParams = new SearchParams();
        searchParams.setKeyword("ryzen");
/*        searchParams.setFilterword("11");
        searchParams.setFrom_time(10L);
        searchParams.setTo_time(90L);
        searchParams.setBirthyear(1990);
        searchParams.setSentiment("-1,0");
        searchParams.setGender("f,m");*/
//        JavaPairRDD<String, String> rdd = wbEsDataService.readWbContentRdd(jsc, searchParams);
//        rdd.foreach(s->{
//            System.out.println("s._2() = " + s._2());
//        });

        sparkEtlService.run(jsc,searchParams);



    }

}
