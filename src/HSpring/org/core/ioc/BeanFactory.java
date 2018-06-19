package HSpring.org.core.ioc;


//工厂类接口
public interface BeanFactory {
	//根据名字获取Bean
	Object getBean(String name) throws Exception;
}
