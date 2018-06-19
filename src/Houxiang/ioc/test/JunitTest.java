package Houxiang.ioc.test;


import org.junit.Test;

import HSpring.org.core.ioc.BeanFactory;
import HSpring.org.core.ioc.HSpringContext;

public class JunitTest {

	@Test
	public void test() throws Exception {
		BeanFactory bf = new HSpringContext("HSpring.xml");
		BeanA a = (BeanA) bf.getBean("BeanA");
		System.out.println(a.getProperty1());
		BeanB b=(BeanB)bf.getBean("BeanB");
		System.out.println(b.getProperty1());
		System.out.println(b.getPropertyA());
		System.out.println(b.getProperty2());
	}
}
