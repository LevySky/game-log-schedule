//package com.spc.aop;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class JobInfoAop {
//
//    @Pointcut("execution(* com.spc.schedule.job..*.*(..))")
//    public void execute(){
//        System.out.println(11111111);
//    }
//
//    @Before("execute()")
//    public void deBefore(JoinPoint joinPoint) throws Throwable {
//        System.out.println("方法的执行钱 : " + joinPoint);
//    }
//
//    @AfterReturning(returning = "ret", pointcut = "execute()")
//    public void doAfterReturning(Object ret) throws Throwable {
//        // 处理完请求，返回内容
//        System.out.println("方法的返回值 : " + ret);
//    }
//
//    //后置异常通知
//    @AfterThrowing("execute()")
//    public void throwss(JoinPoint jp){
//        System.out.println("方法异常时执行.....");
//    }
//
//    //后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
//    @After("execute()")
//    public void after(JoinPoint jp){
//        System.out.println("方法最后执行.....");
//    }
//
//    //环绕通知,环绕增强，相当于MethodInterceptor
//    @Around("execute()")
//    public Object arround(ProceedingJoinPoint pjp) {
//        System.out.println("方法环绕start.....");
//        try {
//            Object o =  pjp.proceed();
//            System.out.println("方法环绕proceed，结果是 :" + o);
//            return o;
//        } catch (Throwable e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//}
//
