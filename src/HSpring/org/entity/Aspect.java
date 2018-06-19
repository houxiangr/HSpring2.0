package HSpring.org.entity;

import java.util.List;

public class Aspect {
	private String id;
	private String expression;
	private List<String> beforeMethod;
	private List<String> afterMethod;
	private List<String> aroundMethod;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public List<String> getBeforeMethod() {
		return beforeMethod;
	}
	public void setBeforeMethod(List<String> beforeMethod) {
		this.beforeMethod = beforeMethod;
	}
	public List<String> getAfterMethod() {
		return afterMethod;
	}
	public void setAfterMethod(List<String> afterMethod) {
		this.afterMethod = afterMethod;
	}
	public List<String> getAroundMethod() {
		return aroundMethod;
	}
	public void setAroundMethod(List<String> aroundMethod) {
		this.aroundMethod = aroundMethod;
	}
	public Aspect(String id,String expression, List<String> beforeMethod, 
			List<String> afterMethod, List<String> aroundMethod) {
		this.id=id;
		this.expression = expression;
		this.beforeMethod = beforeMethod;
		this.afterMethod = afterMethod;
		this.aroundMethod = aroundMethod;
	}
	
}
