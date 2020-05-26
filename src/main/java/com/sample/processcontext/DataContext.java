package com.sample.processcontext;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Основной контекстный объект, используемый для управления процессом вычисления 
 * Содержит:
 * - Описания алгоритма вычислений (Definitions)
 * - Объект контекста с накапливаемыми данными (context)
 * - Dictionary для передачи параметров и других задач  
 * @author Helg
 *
 */
public class DataContext {
	private HashMap<String, Object> dataMap = new HashMap<String, Object>();
	private final String DEFINITIONS_NAME = "DEFINITIONS";
	private final String CONTEXTOBJECTS_NAME = "CONTEXTOBJECTS";
	private final String RULERESULTS_NAME = "RULERESULTS";
	public ContextNode context = new ContextNode("ROOT");
	
	/**
	 * Поместить объект в Dictionary
	 * @param name
	 * @param data
	 */
	public void put(String name, Object data) {
		dataMap.merge(name, data, (k,v) -> v); // upsert
	}
	
	/**
	 * Считать объект из Dictionary
	 * @param name
	 */
	public Object get(String name) {
		return dataMap.get(name);
	}
	
	/**
	 * Записать Definition
	 * @param definition
	 */
	@SuppressWarnings("unchecked")
	public void defSet(String definitionName, String definition) {
		if (!dataMap.containsKey(DEFINITIONS_NAME)) { // ���� ������ DEFINITIONS ��� ��� - ������ 
			dataMap.put(DEFINITIONS_NAME, new HashMap<String, String>());
		}
		
		((HashMap<String, String>)dataMap.get(DEFINITIONS_NAME)).merge(definitionName, definition, (k,v) -> v); // upsert
	}
	
	/**
	 * Считать definition
	 * @param definition
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String defGet(String definitionName) {
		return ((HashMap<String, String>)dataMap.get(DEFINITIONS_NAME)).get(definitionName);
	}
	
	/**
	 * Считать весь список Definitions
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> defsGet() {
		return (HashMap<String, String>)dataMap.get(DEFINITIONS_NAME);
	}
	
	/**
	 * Получить имя указанного Definition
	 * @param definition
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String defGetName(String definition) {
		HashMap<String, String> definitions = (HashMap<String, String>)dataMap.get(DEFINITIONS_NAME);
		   for(String key : definitions.keySet())
		   {
				if(definitions.get(key).equals(definition)) {
					return key;
				}
		   }
		   
		throw new RuntimeException("Definition �� ������ " +definition);
	}
	
	/**
	 * Номер текущего набора данных в контексте
	 */
	public Integer curContextNumber = 1;
	/**
	 * Установить номер текущего набора данных в контексте
	 * @param contextNumber
	 */
	public void setCurContextNumber(Integer contextNumber) {
		if (contextNumber > context.cnCount()) {
			throw new RuntimeException("������� ���������� ������������ ����� ���������");
		}
		this.curContextNumber = contextNumber;
	}
	/**
	 * Считать номер текущего набора данных в контексте
	 * @return
	 */
	public Integer getCurContextNumber() {
		return this.curContextNumber; 
	}
	
	
	/**
	 * Дамп данных (для отладки)
	 */
	public void dump() {
		
//		System.out.println("");
//		System.out.println("datamap:");
//		dataMap.forEach((k,v) -> System.out.println(k+"="+v.toString()));
//		
//		context.dump();
//		
//		System.out.println("");
	}
	
	
	/**
	 * Процедура записи результатов выполнения RULE в контекст.
	 * Расположена здесь вместо ProcessHelper для краткости вызова
	 * @param itemName
	 * @param Result
	 * @param data
	 */
	public void setRuleResult(String itemName, Boolean result, Object data) {
		RuleResult ruleResult;
		
		// RULE возвращает своё имя   
		// Вычисляем на основе этого имя для Definition
		String sDefinition = "RULE{[],"+itemName+"}";
		String sDefinitionName = defGetName(sDefinition);
		
		// Получаем текущий набор данных 
		HashMap<String,Object> curDataObjects = context.cdGet(this.curContextNumber);
		// Если объекта нет в наборе - создаём
		if (!curDataObjects.containsKey(sDefinitionName)) {
			ruleResult = new RuleResult();
			ruleResult.result = result;
			ruleResult.resultObjects.add(data);
			context.cdAdd(sDefinitionName, ruleResult, this.curContextNumber);
		}
		else { // если объект есть - добавляем результаты к объекту
			ruleResult = (RuleResult) curDataObjects.get(sDefinitionName);
			ruleResult.result = result;
			ruleResult.resultObjects.add(data);
		}	
	
	}
}


