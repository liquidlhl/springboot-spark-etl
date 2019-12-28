package com.lhl.springbootsparketl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;

/**
 * WbContent
 *
 * @author:
 * @create: 2019-12-28
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "wb", type = "content")
@Mapping(mappingPath = "mapping/WbContent_mapping.json")
public class WbContent {


    /**
     * uid : 1404376560
     * publish_time : 1577471632000
     * text : 求关注。
     * mid : 5612814510546515491
     * reposts_count : 8
     * comments_count : 9
     * sentiment : 1
     */

    @Id
    private Long mid;
    private Integer uid;
    private Long publish_time;
    private String text;
    private Integer reposts_count;
    private Integer comments_count;
    private Integer sentiment;

}
