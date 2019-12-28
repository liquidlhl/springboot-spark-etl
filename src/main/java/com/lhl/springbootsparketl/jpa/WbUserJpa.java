package com.lhl.springbootsparketl.jpa;

import com.lhl.springbootsparketl.entity.WbUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * UserJpa
 *
 * @author:
 * @create: 2019-12-28
 **/
public interface WbUserJpa extends ElasticsearchRepository<WbUser, Long> {
}
