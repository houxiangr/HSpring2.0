package HSpring.org.core.ioc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;

import HSpring.org.config.ImportConfig;
import HSpring.org.core.aop.MethodAfterAdvice;
import HSpring.org.core.aop.MethodAroundAdvice;
import HSpring.org.core.aop.MethodBeforeAdvice;
import HSpring.org.core.aop.MyAspect;
import HSpring.org.core.aop.ProxyBeanFactory;
import HSpring.org.entity.Aspect;
import HSpring.org.entity.Bean;
import HSpring.org.entity.Property;

public class HSpringContext implements BeanFactory{
	//存放容器中的Bean对象
	private Map<String,Object> context=new HashMap<>();
	//存放容器中的切面对象
	private Map<String,MyAspect> aspectContext=new HashMap<>();
	//存放配置文件信息
	private Map<String,Bean> configBean=new HashMap<String,Bean>();
	//存放切面信息
	private Map<String,Aspect> configAspect=new HashMap<String,Aspect>();
	//通过构造方法传入配置信息
	//将Bean的scope为singleton的bean创建出来放入容器
	public HSpringContext(String xmlName) throws Exception{
		ImportConfig config=new ImportConfig(xmlName);
		configBean=config.parseXmlToBeanList();
		configAspect=config.parseXmlToAspectList();
		for(Entry<String, Aspect> aspect:configAspect.entrySet()) {
			MyAspect aspectObject=createAspectObject(aspect.getValue());
			aspectContext.put(aspect.getKey(), aspectObject);
		}
		for(Entry<String, Bean> bean:configBean.entrySet()) {
			//System.out.println(bean.getKey() + ":" + bean.getValue());
			if(bean.getValue().getScope()==Bean.SINGLETON) {
				//如果在配置切面的时候已经声明，则无需继续创建
				if(context.get(bean.getKey())==null) {
					Object beanObject=createBeanObject(bean.getValue());
					context.put(bean.getKey(), beanObject);
				}
			}
		}
	}
	//通过反射和利用bean信息创建对象
	public Object createBeanObject(Bean bean) throws Exception {
		Object aimBeanObject=null;
		@SuppressWarnings("rawtypes")
		Class clazz=null;
		//通过配置文件中对应bean的类的路径获得class
		clazz=Class.forName(bean.getBeanClass());
		//获得实例
		aimBeanObject=clazz.newInstance();
		//获得bean中的property配置项的信息
		List<Property> propertys=bean.getPropertys();
		for(Property property:propertys) {
			Map<String,Object> mp=new HashMap<String,Object>();
			@SuppressWarnings("unused")
			Map<String,List<String>> mpmethod=new HashMap<String,List<String>>();
			@SuppressWarnings("unused")
			Map<String,List<Object>> mpList=new HashMap<String,List<Object>>();
			//在配置文件中显示的设置了value则直接赋值
			if(!property.getValue().equals("")) {
				//System.out.println(property.getName()+"   "+property.getValue());
				mp.put(property.getName(), property.getValue());
				//将map中的键值映射到aimBeanObject类中去
				BeanUtils.copyProperties( aimBeanObject, mp);
			}
			if(!property.getRef().equals("")) {
				//获取引用的对象
				Object ref=context.get(property.getRef());
				//如果容器中没有此对象则递归调用此函数创建该类
				if(ref==null) {
					ref=createBeanObject(configBean.get(property.getRef()));
				}
				mp.put(property.getName(), ref);
				//将map中的键值映射到aimBeanObject类中去
				BeanUtils.copyProperties(aimBeanObject, mp);
			}
		}
		if(!bean.getAspectName().equals("")) {
			MyAspect myaspect=aspectContext.get(bean.getAspectName());
			ProxyBeanFactory pfb=new ProxyBeanFactory(aimBeanObject,
					myaspect);
			aimBeanObject=pfb.createProxy();
		}
		return aimBeanObject;
	}
	//创建切面对象容器，不支持循环切面配置
	public MyAspect createAspectObject(Aspect aspect) throws Exception {
			List<MethodBeforeAdvice> BeforeMap=new ArrayList<MethodBeforeAdvice>();
			List<MethodAfterAdvice> AfterMap=new ArrayList<MethodAfterAdvice>();
			List<MethodAroundAdvice> AroundMap=new ArrayList<MethodAroundAdvice>();
			List<String> beforeMethods=aspect.getBeforeMethod();
			List<String> afterMethods=aspect.getAfterMethod();
			List<String> aroundMethods=aspect.getAroundMethod();
			for(String beforeMethod:beforeMethods) {
				//获取引用的对象
				Object ref=context.get(beforeMethod);
				//如果容器中没有此对象则递归调用此函数创建该类
				if(ref==null) {
					ref=createBeanObject(configBean.get(beforeMethod));
				}
				//顺便保存到Bean的容器中
				context.put(beforeMethod, ref);
				BeforeMap.add((MethodBeforeAdvice) ref);
			}
			for(String afterMethod:afterMethods) {
				//获取引用的对象
				Object ref=context.get(afterMethod);
				//如果容器中没有此对象则递归调用此函数创建该类
				if(ref==null) {
					ref=createBeanObject(configBean.get(afterMethod));
				}
				context.put(afterMethod, ref);
				AfterMap.add((MethodAfterAdvice) ref);
			}
			for(String aroundMethod:aroundMethods) {
				//获取引用的对象
				Object ref=context.get(aroundMethod);
				//如果容器中没有此对象则递归调用此函数创建该类
				if(ref==null) {
					ref=createBeanObject(configBean.get(aroundMethod));
				}
				context.put(aroundMethod, ref);
				AroundMap.add((MethodAroundAdvice) ref);
			}
			MyAspect result=new MyAspect(aspect.getId(),aspect.getExpression(),BeforeMap,
					AfterMap,AroundMap);
			return result;
		}
	@Override
	public Object getBean(String name) throws Exception {
		Bean bean=configBean.get(name);
		Object result=null;
		//如果这个是单例类直接去容器中找
		if(bean.getScope()==Bean.SINGLETON) {
			result=context.get(bean.getId());
		}else if(bean.getScope()==Bean.PROTOTYPE) {//否则重新创建
			result=createBeanObject(bean);
		}
		return result;
	}
}
