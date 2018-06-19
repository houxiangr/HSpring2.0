package HSpring.aop.test;

public class TargetBean implements TargetBeanImp {
	@Override
	public void show(String user) {
		System.out.println("show:"+user);
	}
	@Override
	public void show2(String user) {
		System.out.println("show2:"+user);
	}
}
