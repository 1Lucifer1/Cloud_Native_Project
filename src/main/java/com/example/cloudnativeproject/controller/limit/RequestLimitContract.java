package com.example.cloudnativeproject.controller.limit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;


@Aspect
@Order
@Component
public class RequestLimitContract {

//    private static final Logger logger = LoggerFactory.getLogger("RequestLimitLogger");
//    @Autowired
//    private RedisTemplate redisTemplate = new RedisTemplate();

//    @Resource
//    RedisTemplate<String,Object> redisTemplate;



    private Map<String, Integer> redisTemplate = new HashMap<>();

    @Pointcut("@annotation(RequestLimit)")
    public void RequestLimit(){

    }

//    @Before("within(@org.springframework.stereotype.Controller *) && @annotation(limit)")
    @Around("RequestLimit()")
    public synchronized Object requestLimit(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        // 或者url(存在map集合的key)
        String url = request.getRequestURI();
        // 获取自定义注解
        RequestLimit rateLimiter = getRequestLimit(joinPoint);
//        System.out.println(url);
//        System.out.println(rateLimiter.count());
//        System.out.println(rateLimiter.time());


//        Jedis jedis = new Jedis("localhost");
//        if(jedis.get(key) == null){
//            jedis.set(key, "1");
//        }else{
//            jedis.set(key, String.valueOf(Integer.parseInt(jedis.get(key)) + 1));
//        }
//        int count = Integer.parseInt(jedis.get(key));

        String key = "req_limit_".concat(url); //hash的key
        if (!redisTemplate.containsKey(key)) { //接口未访问过
            redisTemplate.put(key, 1);
            System.out.println("1:" + key);
        } else {
            redisTemplate.put(key, redisTemplate.get(key) + 1);
            int count = redisTemplate.get(key);
            System.out.println(count + ":" + key);
            if (count > rateLimiter.count()) {
                //logger.info("超过了限定的次数[" + limit.count() + "]");\
//                return new RequestLimitException("429: Too many requests");
                throw new RequestLimitException();
            }else {
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {    //创建一个新的计时器任务。
                    @Override
                    public synchronized void run() {
                        redisTemplate.remove(key);
                    }
                };
                timer.schedule(task, rateLimiter.time());
                //安排在指定延迟后执行指定的任务。task : 所要安排的任务。time : 执行任务前的延迟时间，单位是毫秒。
            }
        }
        return joinPoint.proceed();
    }

    private RequestLimit getRequestLimit(final JoinPoint joinPoint) {
        Method[] methods = joinPoint.getTarget().getClass().getDeclaredMethods();
        String name = joinPoint.getSignature().getName();
        if (!StringUtils.isEmpty(name)) {
            for (Method method : methods) {
                RequestLimit annotation = method.getAnnotation(RequestLimit.class);
                if (!Objects.isNull(annotation) && name.equals(method.getName())) {
                    return annotation;
                }
            }
        }
        return null;
    }
}