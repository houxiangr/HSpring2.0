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
	//��������е�Bean����
	private Map<String,Object> context=new HashMap<>();
	//��������е��������
	private Map<String,MyAspect> aspectContext=new HashMap<>();
	//��������ļ���Ϣ
	private Map<String,Bean> configBean=new HashMap<String,Bean>();
	//���������Ϣ
	private Map<String,Aspect> configAspect=new HashMap<String,Aspect>();
	//ͨ�����췽������������Ϣ
	//��Bean��scopeΪsingleton��bean����������������
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
				//��������������ʱ���Ѿ��������������������
				if(context.get(bean.getKey())==null) {
					Object beanObject=createBeanObject(bean.getValue());
					context.put(bean.getKey(), beanObject);
				}
			}
		}
	}
	//ͨ�����������bean��Ϣ��������
	public Object createBeanObject(Bean bean) throws Exception {
		Object aimBeanObject=null;
		@SuppressWarnings("rawtypes")
		Class clazz=null;
		//ͨ�������ļ��ж�Ӧbean�����·�����class
		clazz=Class.forName(bean.getBeanClass());
		//���ʵ��
		aimBeanObject=clazz.newInstance();
		//���bean�е�property���������Ϣ
		List<Property> propertys=bean.getPropertys();
		for(Property property:propertys) {
			Map<String,Object> mp=new HashMap<String,Object>();
			@SuppressWarnings("unused")
			Map<String,List<String>> mpmethod=new HashMap<String,List<String>>();
			@SuppressWarnings("unused")
			Map<String,List<Object>> mpList=new HashMap<String,List<Object>>();
			//�������ļ�����ʾ��������value��ֱ�Ӹ�ֵ
			if(!property.getValue().equals("")) {
				//System.out.println(property.getName()+"   "+property.getValue());
				mp.put(property.getName(), property.getValue());
				//��map�еļ�ֵӳ�䵽aimBeanObject����ȥ
				BeanUtils.copyProperties( aimBeanObject, mp);
			}
			if(!property.getRef().equals("")) {
				//��ȡ���õĶ���
				Object ref=context.get(property.getRef());
				//���������û�д˶�����ݹ���ô˺�����������
				if(ref==null) {
					ref=createBeanObject(configBean.get(property.getRef()));
				}
				mp.put(property.getName(), ref);
				//��map�еļ�ֵӳ�䵽aimBeanObject����ȥ
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
	//�������������������֧��ѭ����������
	public MyAspect createAspectObject(Aspect aspect) throws Exception {
			List<MethodBeforeAdvice> BeforeMap=new ArrayList<MethodBeforeAdvice>();
			List<MethodAfterAdvice> AfterMap=new ArrayList<MethodAfterAdvice>();
			List<MethodAroundAdvice> AroundMap=new ArrayList<MethodAroundAdvice>();
			List<String> beforeMethods=aspect.getBeforeMethod();
			List<String> afterMethods=aspect.getAfterMethod();
			List<String> aroundMethods=aspect.getAroundMethod();
			for(String beforeMethod:beforeMethods) {
				//��ȡ���õĶ���
				Object ref=context.get(beforeMethod);
				//���������û�д˶�����ݹ���ô˺�����������
				if(ref==null) {
					ref=createBeanObject(configBean.get(beforeMethod));
				}
				//˳�㱣�浽Bean��������
				context.put(beforeMethod, ref);
				BeforeMap.add((MethodBeforeAdvice) ref);
			}
			for(String afterMethod:afterMethods) {
				//��ȡ���õĶ���
				Object ref=context.get(afterMethod);
				//���������û�д˶�����ݹ���ô˺�����������
				if(ref==null) {
					ref=createBeanObject(configBean.get(afterMethod));
				}
				context.put(afterMethod, ref);
				AfterMap.add((MethodAfterAdvice) ref);
			}
			for(String aroundMethod:aroundMethods) {
				//��ȡ���õĶ���
				Object ref=context.get(aroundMethod);
				//���������û�д˶�����ݹ���ô˺�����������
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
		//�������ǵ�����ֱ��ȥ��������
		if(bean.getScope()==Bean.SINGLETON) {
			result=context.get(bean.getId());
		}else if(bean.getScope()==Bean.PROTOTYPE) {//�������´���
			result=createBeanObject(bean);
		}
		return result;
	}
}
