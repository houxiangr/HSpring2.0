package HSpring.org.core.aop;

import java.lang.reflect.Method;
import java.util.List;


public class RunMethod implements MethodImp {

	@Override
	public Object intercept(Object obj, Method method, Object[] args,
			MyAspect asp)
			throws Throwable {
		String express=asp.getExpression();
		boolean flag = method.getName().matches(express);
		List<MethodBeforeAdvice> beforeAdvices=asp.getBeforeMap();
		List<MethodAfterAdvice> afterAdvices=asp.getAfterMap();
		List<MethodAroundAdvice> aroundAdvices=asp.getAroundMap();
		Object result=null;
		if(flag) {
			for(MethodBeforeAdvice beforeAdvice:beforeAdvices) {
				beforeAdvice.before();
			}
			for(MethodAroundAdvice aroundAdvice:aroundAdvices) {
				aroundAdvice.before();
			}
		}
		result = method.invoke(obj, args);
		if(flag) {
			for(MethodAfterAdvice afterAdvice:afterAdvices) {
				afterAdvice.after();
			}
			for(MethodAroundAdvice aroundAdvice:aroundAdvices) {
				aroundAdvice.after();
			}
		}
		return result;
	}

}
