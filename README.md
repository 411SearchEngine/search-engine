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

