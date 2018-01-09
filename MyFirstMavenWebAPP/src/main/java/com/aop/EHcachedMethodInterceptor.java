package com.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

public class EHcachedMethodInterceptor implements MethodInterceptor, InitializingBean{
	public static final Logger logger = Logger.getLogger(EHcachedMethodInterceptor.class);

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		
		String targetName = invocation.getThis().getClass().getName();
        String methodName = invocation.getMethod().getName();
        Object[] arguments = invocation.getArguments();
        Object result;
        logger.info(targetName);
        logger.info(methodName);
        logger.info(arguments);
        String cacheKey = getCacheKey(targetName, methodName, arguments);

		return invocation.proceed();
	}

	 /**
     * <b>function:</b> ???????????????????? ????
     * @author hoojo
     * @createDate 2012-7-2 ????06:12:39
     * @param targetName ?????
     * @param methodName ????????
     * @param arguments ????
     * @return ????????????
     */
    private String getCacheKey(String targetName, String methodName, Object[] arguments) {
        StringBuffer sb = new StringBuffer();
        sb.append(targetName).append(".").append(methodName);
        if ((arguments != null) && (arguments.length != 0)) {
            for (int i = 0; i < arguments.length; i++) {
                sb.append(".").append(arguments[i]);
            }
        }
        return sb.toString();
    }

}
