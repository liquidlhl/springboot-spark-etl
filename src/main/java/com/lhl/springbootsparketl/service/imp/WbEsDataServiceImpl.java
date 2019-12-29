package com.lhl.springbootsparketl.service.imp;

import com.lhl.springbootsparketl.conf.SearchConst;
import com.lhl.springbootsparketl.entity.SearchParams;
import com.lhl.springbootsparketl.service.WbEsDataService;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SparkEtlServiceImpl
 *
 * @author:
 * @create: 2019-12-29
 **/
@Service
public class WbEsDataServiceImpl implements WbEsDataService {

    @Value("${es.wb.content}")
    private String Wb_Content;
    @Value("${es.wb.user}")
    private String Wb_User;


    @Override
    public JavaPairRDD<String, String> readWbUserRdd(JavaSparkContext jsc, SearchParams searchParams) {
        JavaPairRDD<String, String> userRdd = JavaEsSpark.esJsonRDD(jsc, Wb_User, getUserQueryStr(searchParams));
        return userRdd;
    }

    @Override
    public JavaPairRDD<String, String> readWbContentRdd(JavaSparkContext jsc, SearchParams searchParams) {
        JavaPairRDD<String, String> contentRdd = JavaEsSpark.esJsonRDD(jsc, Wb_Content, getContentQueryStr(searchParams));
        return contentRdd;
    }

    private String getUserQueryStr(SearchParams searchParams) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        Integer birthyear = searchParams.getBirthyear();
        if (null != birthyear) {
            boolQueryBuilder.must(QueryBuilders.termQuery(SearchConst.BIRTHYEAR, birthyear));
        }

        String gender = searchParams.getGender();
        if (!StringUtils.isEmpty(gender)) {
            String[] split = gender.split(",");
            boolQueryBuilder.must(QueryBuilders.termsQuery(SearchConst.GENDER, split));
        }
        return "{\"query\":" + boolQueryBuilder.toString() + "}";

    }


    private String getContentQueryStr(SearchParams searchParams) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        String keyword = searchParams.getKeyword();
        if (!StringUtils.isEmpty(keyword)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery(SearchConst.TEXT, keyword));
        }
        String filterword = searchParams.getFilterword();
        if (!StringUtils.isEmpty(filterword)) {
            boolQueryBuilder.mustNot(QueryBuilders.matchQuery(SearchConst.TEXT, filterword));
        }
        Long from_time = searchParams.getFrom_time();
        if (null != from_time) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(SearchConst.PUBLIC_TIME).gte(from_time);
            Long to_time = searchParams.getTo_time();
            if (null != to_time) {
                rangeQueryBuilder.lte(to_time);
            }
            boolQueryBuilder.must(rangeQueryBuilder);
        }
        String sentiment = searchParams.getSentiment();
        if (!StringUtils.isEmpty(sentiment)) {
            String[] split = sentiment.split(",");
            List<Integer> sentiments = Arrays.stream(split).map(Integer::parseInt).collect(Collectors.toList());
            boolQueryBuilder.must(QueryBuilders.termsQuery(SearchConst.SENTIMENT, sentiments));
        }

        return "{\"query\":" + boolQueryBuilder.toString() + "}";
    }

    public static void main(String[] args) {
        WbEsDataServiceImpl sparkEtlService = new WbEsDataServiceImpl();
        SearchParams searchParams = new SearchParams();
        searchParams.setKeyword("22");
        searchParams.setFilterword("11");
        searchParams.setFrom_time(10L);
        searchParams.setTo_time(90L);
        searchParams.setBirthyear(1990);
        searchParams.setSentiment("-1,0");
        searchParams.setGender("f,m");
        String contentQueryStr = sparkEtlService.getContentQueryStr(searchParams);
        System.out.println("contentQueryStr = " + contentQueryStr);

        String userQueryStr = sparkEtlService.getUserQueryStr(searchParams);
        System.out.println("userQueryStr = " + userQueryStr);


    }

}
