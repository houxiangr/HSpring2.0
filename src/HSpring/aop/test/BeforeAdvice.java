package HSpring.aop.test;



import HSpring.org.core.aop.MethodBeforeAdvice;

public class BeforeAdvice extends MethodBeforeAdvice {
	public void before() {
		// TODO Auto-generated method stub
		System.out.println("---before method1---");
	}
}
