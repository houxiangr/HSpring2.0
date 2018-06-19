 package HSpring.org.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import HSpring.org.entity.Aspect;
import HSpring.org.entity.Bean;
import HSpring.org.entity.Property;

//����xml�����ļ�
public class ImportConfig{
	//�����ļ�·��
	private String XmlName;
	//�����ļ���·��
	private static Element rootNode; 
	
	public String getXmlName() {
		return XmlName;
	}
	public void setXmlName(String xmlName) {
		XmlName = xmlName;
	}
	public ImportConfig(String xmlName) throws Exception{
		this.XmlName=xmlName;
		getXmlRootNode();
	}
	public void getXmlRootNode() throws Exception {
		//����SAXReader����
		SAXReader reader=new SAXReader();
		//ƴ��xml·��
		StringBuffer XmlUrl=new StringBuffer("E:\\GitHub\\HSpring1.0\\HSpring\\conf\\");
		XmlUrl.append(getXmlName());
		//����xml�ļ�
		Document doc=reader.read(XmlUrl.toString());
		//��ȡ��Ԫ��
		Element BeansNode=doc.getRootElement();
		ImportConfig.rootNode=BeansNode;
	}
	//����xml����
	public Map<String,Bean> parseXmlToBeanList() throws Exception{
		Map<String,Bean> beans = new HashMap<String,Bean>();
		@SuppressWarnings("unchecked")
		List<Element> BeanNodes=rootNode.elements("bean");
		for(Element BeanNode : BeanNodes) {
			String id="";
			String beanClass="";
			String scope="";
			String aspectName="";
			List<Property> propertys=new ArrayList<Property>();
			@SuppressWarnings("unchecked")
			List<Attribute> beanattrs=BeanNode.attributes();
			//��ȡbean�е����Բ���ֵ����Ӧ����
			for (Attribute attr : beanattrs) {
				String attrkey=attr.getName();
				String attrvalue=attr.getValue();
				switch(attrkey) {
				case "id":
					id=attrvalue;
					break;
				case "class":
					beanClass=attrvalue;
					break;
				case "scope":
					scope=attrvalue;
					break;
				case "aspectname":
					aspectName=attrvalue;
					break;
				}
			}
			@SuppressWarnings("unchecked")
			List<Element> propertyNodes=BeanNode.elements("property");
			for(Element propertyNode : propertyNodes) {
				String name="";
				String ref="";
				String value="";
				List<String> methods=new ArrayList<String>();
				List<String> proxyBeans=new ArrayList<String>();
				@SuppressWarnings("unchecked")
				List<Attribute> properattrs=propertyNode.attributes();
				//��ȡ�������Բ���ֵ
				for (Attribute attr : properattrs) {  
					String attrkey=attr.getName();
					String attrvalue=attr.getValue();
					switch(attrkey) {
					case "name":
						name=attrvalue;
						break;
					case "ref":
						ref=attrvalue;
						break;
					case "value":
						value=attrvalue;
						break;
					}
					//�������type���������type����
					//��ȡһ���������Bean��id
					if(attrkey.equals("type")) {
						if(attrvalue.equals("proxyList")) {
							@SuppressWarnings("unchecked")
							List<Element> ListNodes=propertyNode.elements("ref");
							for(Element ListNode:ListNodes) {
								@SuppressWarnings("unchecked")
								List<Attribute> Listattrs=ListNode.attributes();
								for (Attribute refAttr : Listattrs) {
									String proxyattrkey=refAttr.getName();
									String proxyattrvalue=refAttr.getValue();
									switch(proxyattrkey) {
									case "bean":
										proxyBeans.add(proxyattrvalue);
										break;
									}
								}
								
							}
						}else if(attrvalue.equals("methodList")) {
							@SuppressWarnings("unchecked")
							List<Element> ListNodes=propertyNode.elements("method");
							for(Element ListNode:ListNodes) {
								@SuppressWarnings("unchecked")
								List<Attribute> Listattrs=ListNode.attributes();
								for (Attribute methodAttr : Listattrs) {
									String methodattrkey=methodAttr.getName();
									String methodattrvalue=methodAttr.getValue();
									switch(methodattrkey) {
									case "methodname":
										methods.add(methodattrvalue);
										break;
									}
								}
							}
						}
					}
				}
				Property tempProperty=new Property(name,ref,value);
				propertys.add(tempProperty);
			}
			Bean bean=new Bean(id,beanClass,aspectName,propertys);
			//���scopeΪprototype��������
			if(scope.equals("prototype")) {
				bean.setScope(Bean.PROTOTYPE);
			}
			//��������ļ���������bean��Idһ�������쳣
			if(beans.get(bean.getId())!=null) {
				throw new Exception("�����ļ�������ͬId��bean");
			}else {
				beans.put(bean.getId(),bean);
			}
		}
		return beans;
	}
	//��ȡ����
	public Map<String,Aspect> parseXmlToAspectList() throws Exception{
		Map<String,Aspect> aspects=new HashMap<String,Aspect>();
		@SuppressWarnings({ "unchecked", "unused" })
		List<Element> AspectNodes=rootNode.elements("aspect");
		for(Element AspectNode : AspectNodes) {
			String id="";
			String expression="";
			List<String> methodBeforeAdvice=new ArrayList<String>();
			List<String> methodAfterAdvice=new ArrayList<String>();
			List<String> methodAroundAdvice=new ArrayList<String>();
			@SuppressWarnings("unchecked")
			List<Attribute> aspectattrs=AspectNode.attributes();
			//��ȡbean�е����Բ���ֵ����Ӧ����
			for (Attribute attr : aspectattrs) {  
				String attrkey=attr.getName();
				String attrvalue=attr.getValue();
				switch(attrkey) {
				case "id":
					id=attrvalue;
					break;
				case "expression":
					expression=attrvalue;
					break;
				}
			}
			@SuppressWarnings("unchecked")
			List<Element> beforeMethods=AspectNode.elements("before");
			for(Element beforeMethod : beforeMethods) {
				@SuppressWarnings("unchecked")
				List<Element> beforeMethodNames=beforeMethod.elements("method");
				for(Element beforeMethodName:beforeMethodNames) {
					methodBeforeAdvice.add(beforeMethodName.getText());
				}
			}
			@SuppressWarnings("unchecked")
			List<Element> afterMethods=AspectNode.elements("after");
			for(Element afterMethod : afterMethods) {
				@SuppressWarnings("unchecked")
				List<Element> afterMethodNames=afterMethod.elements("method");
				for(Element afterMethodName:afterMethodNames) {
					methodAfterAdvice.add(afterMethodName.getText());
				}
			}
			@SuppressWarnings("unchecked")
			List<Element> aroundMethods=AspectNode.elements("around");
			for(Element aroundMethod : aroundMethods) {
				@SuppressWarnings("unchecked")
				List<Element> aroundMethodNames=aroundMethod.elements("method");
				for(Element aroundMethodName:aroundMethodNames) {
					methodAroundAdvice.add(aroundMethodName.getText());
				}
			}
			Aspect aspect=new Aspect(id, expression, methodBeforeAdvice,
					methodAfterAdvice, methodAroundAdvice);
			aspects.put(aspect.getId(),aspect);
		}
		return aspects;
	}
}
