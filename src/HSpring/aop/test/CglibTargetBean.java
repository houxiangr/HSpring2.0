package HSpring.aop.test;

//Cglib��Ŀ�����û�м̳��κνӿ�
public class CglibTargetBean {
	private String test;
	
	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
	public void show(String user) {
		// TODO Auto-generated method stub
		System.out.println("show:"+user+test);
	}
}
