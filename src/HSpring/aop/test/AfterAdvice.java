package HSpring.aop.test;



import HSpring.org.core.aop.MethodAfterAdvice;

public class AfterAdvice extends MethodAfterAdvice {

	@Override
	public void after() {
		System.out.println("---after method1---");
	}

}
