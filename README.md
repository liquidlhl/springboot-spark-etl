# springboot-spark-etl
a spark etl project by read and write data from elasticsearch

根据请求参数,使用spark从es读取数据进行指标统计分析,在将结果写入到es或MySQL.
## 版本 ##
java 1.8  
spark 1.6.2  
elasticsearch 5.6.8  
mysql 5.7  
springboot 2.0.1.release  

## 初始化 ##
1.搭建es环境  
2.安装head插件  
3.基于JPA生成demo的es数据  
格式如下:  
/wb_t/user
```
{
    "id": 1404376560,
    "name": "zaku",
    "birthdyear": 1980,
    "url": "http://blog.sina.com.cn/zaku",
    "gender": "m",
    "followers_count": 1204,
    "friends_count": 447,
    "favourites_count": 0,
}
```
/wb_t/content  
```
{
	"uid": 1404376560,
	"publish_time": 1577471632000,
	"text": "求关注。",
	"mid": "5612814510546515491",//帖子的唯一id
	"reposts_count": 8,
	"comments_count": 9,
	"sentiment": 1
}
```


## etl流程 ##
0.程序初始化(主要是JavaSparkContext),目前是standalone模式运行(后续增加on yarn模式支持代码)  
```
{
    "sentiment":"-1,0",
    "birthyear":1990,
    "gender":"f,m",
    "filterword":"intel",
    "keyword":"ryzen",
    "to_time":1577623960000,
    "from_time":1574523960000
}

```
1.请求参数如上所示  
2.根据参数构建es的query语句  
3.根据javaSparkContext及es语句进行取用户 帖子数据  
4.对RDD统计分析(使用累加器)  
5.结果写入es(或mysql)(暂未实现)  

## 统计分析 ##
主要根据帖子及用户统计以下指标  
1.帖子情感分布  
2.用户性别分布  
3.用户年龄分布  
结果封装到一个对象之中,如下所示:
```
ResultStats(sentiment=-1:119796,0:240092,1:240312, gender=f:302047,m:298153, age={0=1276, -1=1413, 1=1339, 2=1328, 3=1129, 4=1320, 5=1004, 6=1130, 7=1115, 8=1297, 9=1156, 10=1149, 11=1269, 12=1302, 13=1239, 14=1141, 15=1210, 16=1463, 17=1479, 18=1285, 19=1110, 20=1236, 21=1193, 22=1087, 23=1130, 24=1392, 25=1169, 26=1323, 27=1175, 28=1277, 29=1197, 30=1164, 31=1297, 32=1115, 33=1178, 34=1123, 35=1093, 36=1306, 37=1152, 38=1220, 39=1380, 40=1248, 41=1324, 42=1080, 43=1397, 44=1198, 45=1076, 46=1054, 47=1358, 48=1206, 49=1224, 50=1294, 51=1353, 52=1293, 53=1335, 54=1199, 55=1159, 56=1436, 57=1188, 58=1206, 59=1179, 60=1129, 61=1119, 62=1254, 63=1290, 64=1180, 65=991, 66=1208, 67=1198, 68=1184, 69=1445, 70=1057, 71=1325, 72=1213, 73=1296, 74=1091, 75=1303, 76=1225, 77=1017, 78=1174, 79=1169, 80=1055, 81=1270, 82=1302, 83=1280, 84=1354, 85=1123, 86=1113, 87=1118, 88=1347, 89=1127, 90=1156, 91=1145, 92=1089, 93=1342, 94=1284, 95=1249, 96=1180, 97=1333, 98=1381})

```

##  调优 ##
待补充


## 参考 ##
0.es及head插件安装  
https://www.cnblogs.com/hts-technology/p/8477258.html  

1.jpa读写es(springboot整合es)
https://my.oschina.net/woter/blog/1842779  
https://blog.csdn.net/weixin_43814195/article/details/85281287  
https://blog.csdn.net/KingBoyWorld/article/details/78654820  

2.spark读写es
https://www.elastic.co/guide/en/elasticsearch/hadoop/5.6/spark.html#spark-write-json  
https://spark.apache.org/docs/1.6.2/  
https://blog.csdn.net/Purplme/article/details/84064802
