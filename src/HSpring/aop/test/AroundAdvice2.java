package HSpring.aop.test;


import HSpring.org.core.aop.MethodAroundAdvice;

public class AroundAdvice2 extends MethodAroundAdvice {

	@Override
	public void before() {
		System.out.println("---aroundBefore2---");
	}

	@Override
	public void after() {
		System.out.println("---aroundAfter2---");
	}

}
