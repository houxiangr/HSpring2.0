package HSpring.org.core.ioc;


//������ӿ�
public interface BeanFactory {
	//�������ֻ�ȡBean
	Object getBean(String name) throws Exception;
}
