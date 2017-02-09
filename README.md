#### 说明
1. 基于Spring Boot整合Durid,Mybatis,Redis,Hessian,RestTemplate等
2. 通过AbstractRoutingDataSource，Annotation实现读写分离
3. 通过JedisCluster作为redis集群的客户端，也可使用RedisTemplate作为redis客户端。实现Redis锁。
4. 通过HessianProxy,HessianProxyFactory,HessianServiceExporter实现hessian接口。
5. 业务切域之后，通过AppContext，保证同一次服务请求在跨域调用时拥有相同的context。例如，traceId。并通过PatternLayout输出至日志文件。
6. 提供并行框架，通过SimpleAsyncUtils，Function等，将父线程的context拷贝至子线程。