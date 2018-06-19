package HSpring.aop.test;



import HSpring.org.core.aop.MethodAfterAdvice;

public class AfterAdvice2 extends MethodAfterAdvice {

	@Override
	public void after() {
		System.out.println("---after method2---");
	}

}
