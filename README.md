#### 说明
1. 基于Spring Boot(web,aop,data,jdbc)整合Durid,Mybatis,Redis,Hessian,RestTemplate,ZooKeeper CuratorFramework等
2. 通过AbstractRoutingDataSource，Annotation实现读写分离
3. 通过JedisCluster作为redis集群的客户端，也可使用RedisTemplate作为redis客户端。实现Redis锁。
4. 通过HessianProxy,HessianProxyFactory,HessianServiceExporter实现hessian接口。
5. 业务切域之后，通过AppContext，保证同一次服务请求在跨域调用时拥有相同的context。例如，traceId。并通过PatternLayout输出至日志文件。
6. 提供并行框架，通过SimpleAsyncUtils，Function等，将父线程的context拷贝至子线程。

#### Module
1. 因为hessian接口需要为外系统提供客户端，所以需要将本应用和客户端公用的对象抽取出来，避免客户端太过冗余
2. 包含公共的Exception，Enum，Vo，Constants等

#### Context
1. 本应用业务上需要切分业务域。同一次服务请求有可能跨域调用服务。因此客观上需要拥有相同的Context数据。数据在AppContext进行维护。
2. 目前AppContext维护的数据包括TraceID和签名Sign信息。
3. TraceID用于业务跟踪，通过logback的ClassicConverter，PatternLayout输出至日志文件。方便日志查询。后续基于TraceID可以将业务导入特定的服务器等等。TraceID在Http，Hessian的客户端，服务端需要维护。具体查看mvc，hessisan-client,hessian-server。TraceID还需要在并发框架进行维护，具体查看common.concurrency。
4. 签名Sign验签功能.略

		线程安全:
		private static ThreadLocal<AppContext> current = new ThreadLocal<>();

#### Common
1. 定义通用的接口或服务。例如，Cache，Lock，HttpClient，Function。
2. 提供线程池的工厂实现，例如，ExecutorServiceFactory，ForkJoinPoolFactory。
3. 提供并行框架的实现，主要用于将父线程的Context拷贝至子线程，同时使用了函数式接口，例如，FunctionWithContext。

		example:
		CompletableFuture<?>[] 
                    cfs = limits
                            .stream()
                            .map(l->{
                                    CompletableFuture<Boolean> future = SimpleAsyncUtils.supplyAsync(() -> {
                                        //logic          
                                    },ExecutorServiceFactory.getExecutorService());
                                    return future ;
                                })
                            .toArray(CompletableFuture[]::new); 
            
		CompletableFuture.allOf(cfs).join(); 

4. RestClient是使用了Spring的RestTemplate结合HttpClient。

#### Redis
1. 实现Common中定义的Cache和Lock接口。Lock用于分布式锁。
2. Redis 支持cluster 和 sentinel 两种方式。配置在RedisClusterConfig和RedisTemplateConfig。
3. 使用方式

			example：
			    @Autowired
			    @Qualifier("MyRedisTemplate")
			    private RedisTemplate<String, String> redisTemplate;
			    
			    @Autowired
			    private ICache cache ;
4. Redis需要注意：Key的定义，声明周期的定义，Sharding机制。

#### Mybatis
1. 涉及Druid 和 Mybatis
2. 通过annotation DataSourceType ,AbstractRoutingDataSource 实现 多动态数据源切换
3. Mybatis 不用写XML，用@Mapper

		example:
			@Configuration
			@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
			@EnableTransactionManagement()
			@Import(MybatisProperties.class) 
			@MapperScan(basePackages = {MybatisAutoConfiguration.BasePackages}, sqlSessionFactoryRef = MybatisAutoConfiguration.SqlSessionFactoryBeanName )
			public class MybatisAutoConfiguration implements TransactionManagementConfigurer { 
				//...
			}
			@Mapper
			public interface SampleMapper {
			    @Select("select * from odc_i_order where order_id = #{orderId}")
			    public SampleDomain selectByPk(@Param("orderId") Integer orderId) ;
			}

#### Mvc
1. BaseController,利用 Supplier lambda 减少代码量
		
		example:
		public DefaultWebApiResult sampleTest(@Validated @RequestBody SampleVo reqVo) {
		        log.info("SampleController sampleTest : ",AppContext.getTraceId());
			return of(()->i.selectByPk(reqVo.getOrderId())) ;
		}
		
2. SimpleHttpClient,发送HTTP请求的客户端。支持同步和异步两种方式 。通过RestTemplate，AsyncRestTemplate，Proxy，FutureCallbackProxy 实现。

		sync example:
		   DefaultWebApiResult resp = (DefaultWebApiResult) SimpleHttpClient
    		.prepareDefaultWebApiResult()
    		.withUrl("http://localhost:10010/sample/test")
    		.withBody(s)
    		.post()
    		.getBody();
    	
    	async example:    	
    	  SimpleHttpClient
    		.prepareDefaultWebApiResult()
    		.withUrl("http://localhost:10010/sample/testAsync")
    		.withBody(s)
    		.withCallback(new FutureCallback<ResponseEntity<DefaultWebApiResult>>(){
				@Override
				public void onSuccess(ResponseEntity<DefaultWebApiResult> result) {
			    	logger.info("testAsyncHttp : " + JsonUtil.toJson(result.getBody()) );				
				}
				@Override	
				public void onFailure(Throwable t) {
					
				}	        	
	        })
    		.postAsync()   	;
		
3. HandlerInterceptorImpl 服务端。将请求的Head 信息，放入Context。
4. 支持异步请求。通过DeferredResult，Lambda Function ，ExecutorService实现。

		example：
		protected DeferredResult<DefaultWebApiResult> asyncOf(Supplier supplier){
	    	DeferredResult<DefaultWebApiResult> deferredResult = new DeferredResult<DefaultWebApiResult>();
	    	CompletableFuture<DefaultWebApiResult> future
	    		= SimpleAsyncUtils
	    			.supplyAsyncWithPool(()->{
	    				return  of( supplier) ;
	    			}) ; 
	    	try {
				deferredResult.setResult(future.get()) ;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
	    	return deferredResult ;
    	}
    	...
    	@RequestMapping(value = "/testAsync", method = RequestMethod.POST)
	    @ResponseBody
	    public DeferredResult<DefaultWebApiResult> sampleTestAsync(@Validated @RequestBody SampleVo reqVo) {
	        log.info("SampleController sampleTestAsync : ",AppContext.getTraceId());
	        return asyncOf(()->i.selectByPk(reqVo.getOrderId())) ;
	    }

#### Hessian
1. CustomHessianServiceExporter 继承 HessianServiceExporter。Hessian服务端。将Hessian请求头信息放入Context。
2. CustomHessianProxy 继承 HessianProxy。Hessian客户端。将Context信息放入请求头。

#### APP
1. 用pom.xml编译hessian服务端，client/pom.xml编译hessian客户端
2. HessianServerConfig 用于启动Hessian服务
3. AppHessianClient 用于生成Hessian客户端

####ZooKeeper CuratorFramework
1. 使用CuratorFramework实现Lock,UniqueCode
2. 结合CuratorFramework和Lambda Function 实现分布式锁。
		
		example:
			...
			public void lock(String path, Integer lockSec ,CuratorLockSupplier suppliper) throws Exception {
				InterProcessMutex lock = null ;
				try{			
					lock = new InterProcessMutex(singleCaseClient, path);	
					acquirelock(lock) ; 
					suppliper.get() ;
				}
				catch(RetException ret){
					logger.error(ret.getResMsg(),ret);
					throw ret ;			
				}
				catch(Exception ex){
					logger.error("- CuratorLock Lock Error . ",ex);
					throw new Exception(ex) ;
				}
				finally{
					releaselock(lock) ;
				}
			}
			
		    @FunctionalInterface
		    public interface CuratorLockSupplier {
		
		        Object get() throws RetException, Exception;
		    }			
			...
		    @Test
		    public void lock() throws Exception {
		    	lock.lock(LockCodePath,  locksupplier );
		    	lock.lock(LockCodePath,  locksupplier1 );
		    }
		
		    private CuratorLockSupplier locksupplier  = ()->{
		    	logger.info("--- Lock 1 Logic ---");
		    	return null ;
		    } ;
		    
		    private CuratorLockSupplier locksupplier1  = ()->{
		    	logger.info("--- Lock 2 Logic ---");
		    	return null ;
		    } ;

3. UniqueCode
		
		example：
		...
    	logger.info("- CuratorClientTest getUniqueCode : {} " , curatorClient.getUniqueCode(UniqueCodePath) );
			

####Kafka
1. Kafka分为生产者 kafka-producer 和 kafaka-consumer两部分。
2. producer部分，利用org.apache.commons.pool2.BasePooledObjectFactory池化对象kafka.javaapi.producer.Producer。再利用org.apache.commons.pool2.impl.GenericObjectPool创建Producer对象池。
		
		example:
		    @Test
			    public void kafkaProducer() throws Exception { 
					for (long nEvents = 0; nEvents < 10; nEvents++) { 	
						Producer<String, String> producer = null  ;
						try{
							producer =  simiPool.borrowObject() ;	
							...
						        KeyedMessage<String, String> data 
						        	= new KeyedMessage<String, String>(topic, key ,msg );         
						        producer.send(data);   
						}
						finally{
							Boolean isProducerNull = Objects.isNull(producer) ;
							if(BooleanUtils.isFalse(isProducerNull) ){
								simiPool.returnObject(producer);
							}
						}
					}
					simiPool.close();
			    }			

3. consumer部分，参见例子ConsumerApp。接合线程池，Lambda Function，Kafka High Level Consumer 编写通用的kafka消费者模板AbstractKafkaConsumer。同时，支持Kafka安全集群。
4. spring.jmx.enabled: false。否则报.MXBean already registered with name org.apache.commons.pool2

####Dubbo
1. 集成Dubbo。集成后，对外不但发布hessian,http rest还可以发布dubbo服务。当然，也可通过dubbo暴露rest或者hessian接口。
2. 与hessian类似，服务端需要编译客户端client包，然后从spring context中将bean取出。
3. 本例以zookeeper作为注册中心，结合spring boot annotation免xml配置。
4. 通过dubbo filter 使 appcontext的traceId 通过 dubbo的RpcContext 传输。保证一次服务，traceId一致。
5. 可外接dubbo admin进行管理和二次开发。