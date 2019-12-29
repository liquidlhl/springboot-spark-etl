package com.lhl.springbootsparketl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

import java.io.Serializable;

/**
 * WeiboUser
 *
 * @author:
 * @create: 2019-12-28
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "wb_t", type = "user")
@Mapping(mappingPath = "mapping/WbUser_mapping.json")
public class WbUser implements Serializable {


    /**
     * id : 1404376560
     * name : zaku
     * birthyear : 1980
     * url : http://blog.sina.com.cn/zaku
     * gender : m
     * followers_count : 1204
     * friends_count : 447
     * favourites_count : 0
     */

    @Id
    private String id;
    private String name;
    private Integer birthyear;
    private String url;
    private String gender;
    private Integer followers_count;
    private Integer friends_count;
    private Integer favourites_count;

}
