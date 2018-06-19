package HSpring.org.core.aop;

import java.lang.reflect.Method;

public interface MethodImp {
	public Object intercept(Object obj, Method method, Object[] args,
			MyAspect asp) throws Throwable;
}
