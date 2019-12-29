package com.lhl.springbootsparketl.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ResultStats
 *
 * @author:
 * @create: 2019-12-29
 **/
@Data
public class ResultStats {

    private String sentiment;
    private String gender;
    private HashMap<Integer, Integer> age;

}
