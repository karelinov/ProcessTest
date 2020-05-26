package com.sample.processcontext;

import java.util.ArrayList;

/**
 * Класс, в котором агрегируются возвращаемые RULEFLOW_GROUP данные
 * Используется для хранения результатов в datacontext 
 * @author Helg
 *
 */
public class RuleResult {
	public Boolean result;
	public ArrayList<Object> resultObjects = new ArrayList<Object>();
	//public Object resultObject;
	
	public void setResult(Boolean result, Object resultObject) {
		this.result = result;
		this.resultObjects.add(resultObject);
	}
	
	public String toString() {
		String result = "result="+this.result.toString()+",resultObjects =(" ;
		if (resultObjects == null) {
			result = result + "null)";
		}
		else {
			for (int i=0; i<resultObjects.size(); i++) {
				Object resultobject = resultObjects.get(i);
				if (resultobject == null) result = result + "null";
				else result = result + resultobject;
				
				if (i != resultObjects.size() -1) {
					result = result + ",";
				}
				else {
					result = result + ")";
				}
					
			}
		}
		
		return result;
	}	
	
	
}
