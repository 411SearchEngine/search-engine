# search-engine

搜索后端工程



### 环境安装

1. 安装MongoDB,Navicat测试连接
2. 下载es,  下载地址 : https://www.elastic.co/cn/downloads/past-releases/elasticsearch-6-5-4
   - jdk要求1.8

- 解压 . /bin目录 运行 .\elasticsearch.bat .启动失败的话设置java_home路径
- http://localhost:9200/ 查看是否安装成功

1. 安装中文插件
   - https://github.com/medcl/elasticsearch-analysis-ik/releases/tag/v6.5.4
   - 解压. 复制到es  plugins/ik目录下


### 爬数据接口 

1. 爬取基本数据接口

   localhost:3000/find/weather

2. 爬取视频数据

   localhost:3000/find/video

3. 查询天气历史数据

   localhost:3000/find/lishi?id=0&time=20191130

   - id  查询 provinceCityDo表中id > 0的数据 . 爬取历史数据
   - time 爬取的开始时间.   格式为 yyyyMMdd  例如 : 20151211