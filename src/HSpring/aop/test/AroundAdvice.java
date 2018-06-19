package HSpring.aop.test;



import HSpring.org.core.aop.MethodAroundAdvice;

public class AroundAdvice extends MethodAroundAdvice {

	@Override
	public void before() {
		System.out.println("---aroundBefore1---");
	}

	@Override
	public void after() {
		System.out.println("---aroundAfter1---");
	}

}
