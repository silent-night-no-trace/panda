1.5 消息分组
通常在生产环境，我们的每个服务都不会以单节点的方式运行在生产环境，当同一个服务启动多个实例 的时候，这些实例都会绑定到同一个消息通道的目标主题(Topic)上。默认情况下，当生产者发出一 条消息到绑定通道上，这条消息会产生多个副本被每个消费者实例接收和处理，但是有些业务场景之 下，我们希望生产者产生的消息只被其中一个实例消费，这个时候我们需要为这些消费者设置消费组来 实现这样的功能。

image-20200903145510346

实现的方式非常简单，我们只需要在服务消费者端设置

spring.cloud.stream.bindings.input.group 属性即可，比如我们可以这样实现:


spring:
  application:
    name: stream
  cloud:
    stream:
      bindings:
        groupOutput:
          destination: testGroup
          content-type: text/plain
          binder: rabbit
        groupInput:
          destination: testGroup
          content-type: text/plain
          group: group-one
          binder: rabbit
        groupInput2:
          destination: testGroup
          content-type: text/plain
          group: group-one
          binder: rabbit
      binders:
        rabbit:
          type: rabbit
          environment:
            spring:
              rabbitmq:
                host: localhost
                port: 5672
·

同一组的消费者 定义两个 消息只会被一个消费者消费


public interface GroupProcess {
​
  String GROUP_OUTPUT = "groupOutput";
  /**
   * 定义两个消费者
   */
  String GROUP_INPUT = "groupInput";
  String GROUP_INPUT2 = "groupInput2";
​
  /**
   * 分组消息发送
   *
   * @return MessageChannel
   */
  @Output(GROUP_OUTPUT)
  MessageChannel groupOutput();
​
  /**
   * 分组消息接收
   *
   * @return SubscribableChannel
   */
  @Input(GROUP_INPUT)
  SubscribableChannel groupInput();
}
消费者和生产者 代码


@EnableBinding(GroupProcess.class)
public class GroupConsumer {
​
  @StreamListener(GroupProcess.GROUP_INPUT)
  public void receive(Message<String> message){
    System.out.println("group receive:"+message.getPayload());
  }
}
​
​
@EnableBinding(GroupProcess.class)
public class GroupProducer implements CommandLineRunner {
  
  @Autowired
  @Qualifier(GroupProcess.GROUP_OUTPUT)
  private MessageChannel groupOutput;
​
  @Override
  public void run(String... args) throws Exception {
    groupOutput.send(MessageBuilder.withPayload("group message").build());
  }
}
​
启动即可观察到 消息只会被一个消费获取 并消费

通过修改配置 启动2消费者 1 个生产者

1.启动消费者1  修改配置 -Dserver.port=1231  注释掉 groupOutput


spring:
  application:
    name: stream
  cloud:
    stream:
      bindings:
#        group Output:
#          destination: testGroup
#          content-type: text/plain
#          binder: rabbit
        groupInput:
          destination: testGroup
          content-type: text/plain
          group: group-one
          binder: rabbit
​
消费者

```java
@EnableBinding(GroupProcess.class)
public class GroupConsumer {
​
  @StreamListener(GroupProcess.GROUP_INPUT)
  public void receive(Message<String> message){
    System.out.println("group receive:"+message.getPayload());
  }
}
2.启动消费者2  修改配置 -Dserver.port=1232  注释掉 groupOutput



spring:
  application:
    name: stream
  cloud:
    stream:
      bindings:
#        group Output:
#          destination: testGroup
#          content-type: text/plain
#          binder: rabbit
        groupInput:
          destination: testGroup
          content-type: text/plain
          group: group-one
          binder: rabbit
​
消费者


@EnableBinding(GroupProcess.class)
public class GroupConsumer {
​
  @StreamListener(GroupProcess.GROUP_INPUT)
  public void receive(Message<String> message){
    System.out.println("group receive2:"+message.getPayload());
  }
}
3.启动生产者 


spring:
  application:
    name: stream
  cloud:
    stream:
      bindings:
        groupOutput:
          destination: testGroup
          content-type: text/plain
          binder: rabbit
#        groupInput:
#          destination: testGroup
#          content-type: text/plain
#          group: group-one
#          binder: rabbit
都启动成功  发现最终一个只有一个消费者会接收到消息


1.6 消息分区

有一些场景需要满足, 同一个特征的数据被同一个实例消费, 比如同一个id的传感器监测数据必须被同一 个实例统计计算分析, 否则可能无法获取全部的数据。又比如部分异步任务，首次请求启动task，二次 请求取消task，此场景就必须保证两次请求至同一实例.

image-20200903151829367




总配置文件


spring:
  application:
    name: stream
  cloud:
    stream:
      bindings:
        output:
          destination: test-topic
          content-type: text/plain
          binder: rabbit
        input:
          destination: test-topic
          content-type: text/plain
          binder: rabbit
        outputOrder:
          destination: testOrderMq
          content-type: text/plain
          binder: rabbit
        inputOrder:
          destination: testOrderMq
          content-type: text/plain
          binder: rabbit
        groupOutput:
          destination: testGroup
          content-type: text/plain
          binder: rabbit
        groupInput:
          destination: testGroup
          content-type: text/plain
          group: group-one
          binder: rabbit
        partitionOutput:
          destination: testPartition
          content-type: text/plain
          producer:
            partitionCount: 2
            partitionKeyExpression: payload
          binder: rabbit
        partitionInput:
          destination: testPartition
          content-type: text/plain
          group: partitionGroup
          consumer:
            partitioned: true
            instanceIndex: 0
            instanceCount: 2
          binder: rabbit
      binders:
        rabbit:
          type: rabbit
          environment:
            spring:
              rabbitmq:
                host: localhost
                port: 5672
​

分别启动两个消费者 但是分区索引不同  然后在启动生产者


1.启动消费者1 实例索引为0   通过添加新的启动配置 -Dserver.port= 1231


spring:
  application:
    name: stream
  autoconfigure:
    exclude: org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistrationAutoConfiguration
  cloud:
    loadbalancer:
      ribbon:
        enabled: true
    stream:
      bindings:
        output:
          destination: test-topic
          content-type: text/plain
          binder: rabbit
        input:
          destination: test-topic
          content-type: text/plain
          binder: rabbit
        outputOrder:
          destination: testOrderMq
          content-type: text/plain
          binder: rabbit
        inputOrder:
          destination: testOrderMq
          content-type: text/plain
          binder: rabbit
        groupOutput:
          destination: testGroup
          content-type: text/plain
          binder: rabbit
        groupInput:
          destination: testGroup
          content-type: text/plain
          group: group-one
          binder: rabbit
#        partitionOutput:
#          destination: testPartition
#          content-type: text/plain
#          producer:
#            partitionCount: 2
#            partitionKeyExpression: payload
#          binder: rabbit
        partitionInput:
          destination: testPartition
          content-type: text/plain
          group: partitionGroup
          consumer:
            partitioned: true
            instanceIndex: 0
            instanceCount: 2
          binder: rabbit
      binders:
        rabbit:
          type: rabbit
          environment:
            spring:
              rabbitmq:
                host: localhost
                port: 5672
2.启动消费者2 分区索引为1 添加 新增配置 -Dserver.port=1232


spring:
  application:
    name: stream
  cloud:
    stream:
      bindings:
        output:
          destination: test-topic
          content-type: text/plain
          binder: rabbit
        input:
          destination: test-topic
          content-type: text/plain
          binder: rabbit
        outputOrder:
          destination: testOrderMq
          content-type: text/plain
          binder: rabbit
        inputOrder:
          destination: testOrderMq
          content-type: text/plain
          binder: rabbit
        groupOutput:
          destination: testGroup
          content-type: text/plain
          binder: rabbit
        groupInput:
          destination: testGroup
          content-type: text/plain
          group: group-one
          binder: rabbit
#        partitionOutput:
#          destination: testPartition
#          content-type: text/plain
#          producer:
#            partitionCount: 2
#            partitionKeyExpression: payload
#          binder: rabbit
        partitionInput:
          destination: testPartition
          content-type: text/plain
          group: partitionGroup
          consumer:
            partitioned: true
            instanceIndex: 1
            instanceCount: 2
          binder: rabbit
      binders:
        rabbit:
          type: rabbit
          environment:
            spring:
              rabbitmq:
                host: localhost
                port: 5672
3.最后启动生产者


spring:
  application:
    name: stream
  cloud:
    stream:
      bindings:
        output:
          destination: test-topic
          content-type: text/plain
          binder: rabbit
        input:
          destination: test-topic
          content-type: text/plain
          binder: rabbit
        outputOrder:
          destination: testOrderMq
          content-type: text/plain
          binder: rabbit
        inputOrder:
          destination: testOrderMq
          content-type: text/plain
          binder: rabbit
        groupOutput:
          destination: testGroup
          content-type: text/plain
          binder: rabbit
        groupInput:
          destination: testGroup
          content-type: text/plain
          group: group-one
          binder: rabbit
        partitionOutput:
          destination: testPartition
          content-type: text/plain
          producer:
            partitionCount: 2
            partitionKeyExpression: payload
          binder: rabbit
#        partitionInput:
#          destination: testPartition
#          content-type: text/plain
#          group: partitionGroup
#          consumer:
#            partitioned: true
#            instanceIndex: 0
#            instanceCount: 2
#          binder: rabbit
      binders:
        rabbit:
          type: rabbit
          environment:
            spring:
              rabbitmq:
                host: localhost
                port: 5672


\1. pring.cloud.stream.bindings.partitionOutput.producer.partitionKeyExpression :通过该参数 指定了分区键的表达式规则，我们可以根据实际的输出消息规则来配置SpEL来生成合适的分区键;

\2. spring.cloud.stream.bindings.partitionOutput.producer.partitionCount :该参数指定了消息分 区的数量。\

\3.spring.cloud.stream.bindings.partitionInput.consumer.partitioned 是否开启分区

\3.spring.cloud.stream.bindings.partitionInput.consumer.instanceIndex 分区的索引

\3.spring.cloud.stream.bindings.partitionInput.consumer.instanceCount 实例数量

到这里消息分区配置就完成了，我们可以再次启动这两个应用，同时消费者启动多个，但需要注意的是 要为消费者指定不同的实例索引号，这样当同一个消息被发给消费组时，我们可以发现只有一个消费实 例在接收和处理这些相同的消息。

