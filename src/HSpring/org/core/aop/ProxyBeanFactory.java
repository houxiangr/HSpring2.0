package HSpring.org.core.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ProxyBeanFactory implements MethodInterceptor{
	private Object targetBean=null;
	private MyAspect aspect=null;
	private MethodImp interceptor=new RunMethod();
	public Object getTargetBean() {
		return targetBean;
	}
	public void setTargetBean(Object targetBean) {
		this.targetBean = targetBean;
	}
	public MyAspect getAspect() {
		return aspect;
	}
	public void setAspect(MyAspect aspect) {
		this.aspect = aspect;
	}
	public ProxyBeanFactory(Object targetBean, MyAspect aspect) {
		this.targetBean = targetBean;
		this.aspect = aspect;
	}
	public Object createProxy() {
		if(targetBean.getClass().getInterfaces().length==0) {
			return createCglibProxy();
		}
		return createJdkProxy();
	}
	//jdk�������������
	private Object createJdkProxy() {
		Class<?> interfaces[] =targetBean.getClass().getInterfaces();
		// TODO Auto-generated method stub
		Class<?> clazz = interfaces[0];
		Object jdkProxy=Proxy.newProxyInstance(targetBean.getClass().getClassLoader(),
				 new Class[] { clazz }, 
				new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) 
                    		throws Throwable {
                    	Object result=null;
                    	result=interceptor.intercept(targetBean, method, args, aspect);
                    	return result;
                    }
                });
		return jdkProxy;
	}
	private Object createCglibProxy() {
		//1.������
        Enhancer en = new Enhancer();
        //2.���ø���
        en.setSuperclass(targetBean.getClass());
        //3.���ûص�����
        en.setCallback(this);
        //4.��������(�������)
        return en.create();
	}
	
	@Override
    public Object intercept(Object obj, Method method,
    		Object[] args, MethodProxy proxy) throws Throwable {
		Object result=null;
        //�жϸ�֪ͨ�ڷ�������ǰִ��
    	result=interceptor.intercept(targetBean, method, args, aspect);
    	return result;
    }
}

