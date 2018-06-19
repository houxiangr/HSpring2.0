package HSpring.org.core.aop;

import java.util.List;

public class MyAspect {
	private String id;
	private String expression;
	private List<MethodBeforeAdvice> BeforeMap;
	private List<MethodAfterAdvice> AfterMap;
	private List<MethodAroundAdvice> AroundMap;
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
	
	public List<MethodBeforeAdvice> getBeforeMap() {
		return BeforeMap;
	}
	public void setBeforeMap(List<MethodBeforeAdvice> beforeMap) {
		BeforeMap = beforeMap;
	}
	public List<MethodAfterAdvice> getAfterMap() {
		return AfterMap;
	}
	public void setAfterMap(List<MethodAfterAdvice> afterMap) {
		AfterMap = afterMap;
	}
	public List<MethodAroundAdvice> getAroundMap() {
		return AroundMap;
	}
	public void setAroundMap(List<MethodAroundAdvice> aroundMap) {
		AroundMap = aroundMap;
	}
	public MyAspect(String id, String expression, List<MethodBeforeAdvice> beforeMap,
			List<MethodAfterAdvice> afterMap, List<MethodAroundAdvice> aroundMap) {
		super();
		this.id = id;
		this.expression = expression;
		BeforeMap = beforeMap;
		AfterMap = afterMap;
		AroundMap = aroundMap;
	}
}
