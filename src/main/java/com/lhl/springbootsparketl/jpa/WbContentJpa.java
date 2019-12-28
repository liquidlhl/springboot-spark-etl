package com.lhl.springbootsparketl.jpa;

import com.lhl.springbootsparketl.entity.WbContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * UserJpa
 *
 * @author:
 * @create: 2019-12-28
 **/
public interface WbContentJpa extends ElasticsearchRepository<WbContent, Long> {

//    List<WbContent> findByText(String text);
    List<WbContent> findByText(String text);
    Page<WbContent> findByTextand(String text, Pageable pageable);
}
