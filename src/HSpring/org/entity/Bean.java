package HSpring.org.entity;

import java.util.List;
//�����ļ���Bean��ʵ����
public class Bean {
	//����Bean��scopeΪ�����ĳ���
	public static final int SINGLETON=1;
	//����BeanΪscopeΪ��ʵ��ĳ���
	public static final int PROTOTYPE=2;
	//�����ļ���BeanԪ�ص�һЩ����
	private String id;
	private String beanClass;
	private String aspectName;
	//scopeĬ������Ϊ����ģʽ
	private int scope=SINGLETON;
	private List<Property> propertys;
	public Bean(String id, String beanClass,String aspectName,List<Property> propertys) {
		this.id = id;
		this.beanClass = beanClass;
		this.aspectName=aspectName;
		this.propertys = propertys;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAspectName() {
		return aspectName;
	}
	public void setAspectName(String aspectName) {
		this.aspectName = aspectName;
	}
	public String getBeanClass() {
		return beanClass;
	}
	public void setBeanClass(String beanClass) {
		this.beanClass = beanClass;
	}
	public int getScope() {
		return scope;
	}
	public void setScope(int scope) {
		this.scope = scope;
	}
	public List<Property> getPropertys() {
		return propertys;
	}
	public void setPropertys(List<Property> propertys) {
		this.propertys = propertys;
	}
	
}
