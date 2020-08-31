# panda
## spring-cloud系列全家桶 后续还在完善

## 模块分类
- admin-server 
  - springboot admin监控模块
- auth-server 
  - oauth2.0 认证服务
- config-server
  - spring cloud config
- gateway
  - spring cloud gateway
- goods 
  - 演示商品模块
- order 
  - 演示订单模块
- registry
  - spring cloud eureka 
- resource-server
  - oauth2.0 资源服务
- user
  - 演示用户模块
 
目前注册中心支持 eureka consul zookeeper 

### spring cloud oauth2.0 系列
- 认证服务 资源服务 使用内存存储信息模式完成认证

## 后续
- 认证服务 资源服务 使用数据库存储信息模式完成认证
- 添加网关来完成认证及授权  
 




### tip

### zipkinServer 服务搭建 
#### Linux环境

```shell script
curl -sSL https://zipkin.io/quickstart.sh | bash -s
java -jar zipkin.jar  