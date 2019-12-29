package com.lhl.springbootsparketl;

import com.lhl.springbootsparketl.entity.WbContent;
import com.lhl.springbootsparketl.entity.WbUser;
import com.lhl.springbootsparketl.jpa.WbContentJpa;
import com.lhl.springbootsparketl.jpa.WbUserJpa;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

    @BeforeClass
    public static void Before(){
        //https://github.com/netty/netty/issues/6956
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }


    @Test
    public void initialize() {
        List<String> uids = new ArrayList<>(10000);
        List<WbUser> wbUsers = new ArrayList<>(10000);
        for (int i = 0; i < 50000; i++) {
            WbUser wbUser = new WbUser();
            String uid = getId(10);
            uids.add(uid);
            wbUser.setId(uid + "");
            wbUser.setName("uname_" + uid);
            wbUser.setBirthyear(getBirthYear());
            wbUser.setUrl("http://www.wb.com/u/" + uid);
            wbUser.setGender(Math.random() > 0.5 ? "m" : "f");
            wbUser.setFollowers_count((int) (Math.random() * 10000));
            wbUser.setFriends_count((int) (Math.random() * 10000));
            wbUser.setFavourites_count((int) (Math.random() * 10000));
            wbUsers.add(wbUser);
        }
        wbUserJpa.saveAll(wbUsers);

        ArrayList<WbContent> wbContents = new ArrayList<>(100000);
        for (int i = 0; i < 500000; i++) {
            WbContent wbContent = new WbContent();
            String mid = getId(19);
            wbContent.setMid(mid);
            int index = (int) (Math.random() * uids.size());
            wbContent.setUid(uids.get(index));
            wbContent.setPublish_time(getPublishTime());
            wbContent.setText("求关注 ryzen AMD#锐龙3000系列移动处理器#，12nm工艺制程，搭载Radeon Vega Graphics独显级显示性能，澎湃性能为笔记本电脑注入强劲动力，强大的多任务处理能力，带来绝佳的性能和娱乐体验！" );
            wbContent.setReposts_count((int) (Math.random() * 1000));
            wbContent.setComments_count((int) (Math.random() * 10000));
            //-1 0 1负面 中性 正面
            wbContent.setSentiment(getSentiment());
            wbContents.add(wbContent);
        }
        wbContentJpa.saveAll(wbContents);


    }


    public String getId(int len) {

        StringBuilder sb = new StringBuilder();
        sb.append((int) (Math.random() * 9) + 1);
        for (int i = 0; i < len - 1; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }

    @Test
    public void test2() {
        for (int i = 0; i < 500; i++) {
            String randomId = getId(19);
            System.out.println("randomId = " + randomId);
        }

        long publishTime = getPublishTime();
        System.out.println("publishTime = " + publishTime);

    }

    private long getPublishTime() {
// 创建指定日期时间如：2017-11-27 14：30：50：500
        Random random = new Random();
//        该值介于[0,n)的区间
        int day = random.nextInt(15) + 1;
        int hour = random.nextInt(24);
        int minus = random.nextInt(60);
        int second = random.nextInt(60);
        DateTime dt = new DateTime(2019, 12, day, hour, minus, second, 0);
        return dt.getMillis();
    }

    private int getBirthYear() {
        return 2020 - (int) (Math.random() * 100);
    }

    private Integer getSentiment() {
        int sentiment;
        double random = Math.random();
        if (random < 0.2) {
            sentiment = -1;
        } else if (random < 0.6) {
            sentiment = 0;
        } else {
            sentiment = 1;
        }
        return sentiment;
    }


    @Test
    public void test() {
        WbUser wbUser = new WbUser();
        wbUser.setId(1 + (int) (Math.random() * System.currentTimeMillis()) + "");
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
        wbContent.setMid(5612814510546515491L + (long) (Math.random() * System.currentTimeMillis()) + "");
        wbContent.setUid(0 + "");
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
