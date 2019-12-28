package com.lhl.springbootsparketl;

import com.lhl.springbootsparketl.entity.WbContent;
import com.lhl.springbootsparketl.entity.WbUser;
import com.lhl.springbootsparketl.jpa.WbContentJpa;
import com.lhl.springbootsparketl.jpa.WbUserJpa;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * TestGenerateData2Es
 *
 * @author:
 * @create: 2019-12-28
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestGenerateData2Es {
    @Autowired
    WbUserJpa wbUserJpa;
    @Autowired
    WbContentJpa wbContentJpa;

    @Test
    public void test() {
        WbUser wbUser = new WbUser();
        wbUser.setId(1 + (int) (Math.random() * System.currentTimeMillis()));
        wbUser.setName("t1");
        wbUser.setBirthyear(0);
        wbUser.setUrl("");
        wbUser.setGender("");
        wbUser.setFollowers_count(0);
        wbUser.setFriends_count(0);
        wbUser.setFavourites_count(0);

        WbUser save = wbUserJpa.save(wbUser);
        System.out.println("save = " + save);

        WbContent wbContent = new WbContent();
        wbContent.setMid(5612814510546515491L + (long) (Math.random() * System.currentTimeMillis()));
        wbContent.setUid(0);
        wbContent.setPublish_time(0L);
        wbContent.setText("求关注 " + new Date().getTime());
        wbContent.setReposts_count(0);
        wbContent.setComments_count(0);
        wbContent.setSentiment(0);
        WbContent save2 = wbContentJpa.save(wbContent);
        System.out.println("save2 = " + save2);
//
//        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//        boolQueryBuilder.must(QueryBuilders.termQuery("field", "2"));
//        System.out.println("boolQueryBuilder = " + boolQueryBuilder);

        Iterable<WbContent> all = wbContentJpa.findAll();

        Page<WbContent> jpaByText = wbContentJpa.findByText("关注", PageRequest.of(0, 2));
        long totalElements = jpaByText.getTotalElements();
        System.out.println("totalElements = " + totalElements);
        List<WbContent> content = jpaByText.getContent();
        content.forEach(System.out::println);


        List<WbContent> list = wbContentJpa.findByText("关注");
        System.out.println("list = " + list);


    }


}
