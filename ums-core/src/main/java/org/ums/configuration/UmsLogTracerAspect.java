package org.ums.configuration;

/**
 * Created by Monjur-E-Morshed on 08-Aug-17.
 */

/*
 * import org.aspectj.lang.annotation.After; import org.aspectj.lang.annotation.Pointcut; import
 * org.slf4j.LoggerFactory; import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.kafka.core.KafkaTemplate;
 * 
 * @Aspect
 * 
 * @Component public class UmsLogTracerAspect {
 * 
 * Logger mLogger = LoggerFactory.getLogger(UmsLogTracerAspect.class);
 * 
 * @Autowired private KafkaTemplate<String, String> mKafkaTemplate;
 * 
 * @Pointcut("within(org.ums.*)") public void inWebLayer() {}
 * 
 * 
 * @After("execution(* org.ums.manager.*(..))") public void generateLog(JoinPoint pJoinPoint) {
 * 
 * System.out.println("***************"); System.out.println("logger---->" +
 * pJoinPoint.getSignature().toString()); mLogger.trace("the logger******************");
 * 
 * mKafkaTemplate.send("ums_logger", "the method accessed --> ");
 * 
 * }
 * 
 * }
 */
