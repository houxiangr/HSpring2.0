package HSpring.aop.test;


import org.junit.Test;

import HSpring.org.core.ioc.BeanFactory;
import HSpring.org.core.ioc.HSpringContext;

public class AopTest {
	@Test
	public void testJdkProxy() throws Exception {
		/*BeanFactory bf = new HSpringContext("HSpring.xml");
        TargetBeanImp targetImp = (TargetBeanImp) bf.getBean("targetBeanProxy");
        //�⿪����ע�ͻ��ִ��һ��ǰ�ú���
        //System.out.println(targetImp);
        System.out.println(targetImp.getClass());
        targetImp.show("houxiang");*/
		BeanFactory bf = new HSpringContext("HSpring.xml");
		TargetBeanImp target = (TargetBeanImp) bf.getBean("targetBean");
		target.show("houxiang");
	}
}
