package com.lhl.springbootsparketl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SearchParams
 *
 * @author:
 * @create: 2019-12-29
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchParams {
    private String keyword;
    private String filterword;
    private Long from_time;
    private Long to_time;
    private String sentiment;
    private Integer birthyear;
    private String gender;

}
