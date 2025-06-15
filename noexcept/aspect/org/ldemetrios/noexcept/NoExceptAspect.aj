//package org.ldemetrios.noexcept;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//
//@Aspect
//public class NoExceptAspect {
//    @Around("@annotation(org.ldemetrios.noexcept.NoExcept)")
//    public Object wrapWithTryCatch(ProceedingJoinPoint joinPoint) {
//        try {
//            return joinPoint.proceed();
//        } catch (Throwable t) {
//            handleException(joinPoint, t);
//            return null;
//        }
//    }
//
//    private void handleException(ProceedingJoinPoint joinPoint, Throwable t) {
//        System.err.println("Exception in @NoExcept method: " + joinPoint.getSignature());
//        t.printStackTrace();
//        System.exit(1);
//    }
//}

package org.ldemetrios.noexcept;

import org.ldemetrios.noexcept.NoExcept;
import org.aspectj.lang.ProceedingJoinPoint;

public aspect NoExceptAspect {
    pointcut noexcept() : execution(@NoExcept * *(..));

    Object around() : noexcept() {
        try {
            return proceed();
        } catch (Throwable t) {
            System.err.println("Exception in @NoExcept method: " + thisJoinPoint.getSignature());
            t.printStackTrace();
            System.exit(1);
            return null;
        }
    }
}
