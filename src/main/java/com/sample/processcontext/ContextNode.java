package com.sample.processcontext;

import java.util.HashMap;

public class ContextNode {
	public String name = null;
	public HashMap<String,Object> contextObjects = new HashMap<String,Object>();
	public HashMap<String,Object> contextData = new HashMap<String,Object>();
	
	public ContextNode parent = null;
	public HashMap<Integer,ContextNode> children = new HashMap<Integer,ContextNode>();
	
	public ContextNode(String name) {
		super();
		this.name = name;
	}
	/**
	 * Добавление объекта контекста в узел
	 * @param coName
	 * @param cObject
	 */
	public void coAdd(String coName, Object cObject) {
		contextObjects.merge(coName, cObject, (k,v) -> v); // upsert
	}
	/**
	 * Удаление объекта контекста из узла
	 * @param coName
	 * @param cObject
	 */
	public void coRemove(String coName) {
		for(Integer key: children.keySet()) {
			ContextNode childNode = children.get(key);
			childNode.coRemove(coName);
		}
		contextObjects.remove(coName);
		
	}
	
	/**
	 * Получение списка объектов контекста узла
	 * Для указанного набора 
	 * ! Всё, связанное с номерами наборов - сделано примитивно только  на одно ветвление!
	 * @param coSetNumber
	 */
	public HashMap<String,Object> coGet(Integer coSetNumber) {
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		// Извлекаем список данных узла
		for(String key: contextObjects.keySet()) {
			result.put(key, contextObjects.get(key));
		}
		
		// Извлекаем список данных дочернего узла  
		if (children.size() >= coSetNumber) {
			ContextNode childNode = children.get(coSetNumber);
			HashMap<String,Object> childResult = childNode.coGet(coSetNumber);
			
			for(String key: childResult.keySet()) {
				result.put(key, childResult.get(key));
			}
		}
		
		
		return result;
	}
	
	
	
	/**
	 * Добавление дочернего узла
	 */
	public ContextNode cnAddChild(String childName) {
		ContextNode childNode = new ContextNode(childName);
		children.put(children.size()+1 , childNode);
		childNode.parent = this;
		
		return childNode;
	}
	
	/**
	 * Удаление дочернего узла по номеру
	 * @param childName
	 */
	public void cnRemoveChild(Integer childNumber) {
		children.remove(childNumber);
	}
	
	
	/**
	 * Удаление дочернего узла по имени
	 */
	public void cnRemoveChild(String childName) {
		for(Integer key: children.keySet()) {
			ContextNode childNode = children.get(key);
			if (childNode.name == childName) {
				this.cnRemoveChild(key);
				return;
			}
		}
		
	}
	
	/**
	 * Возвращает количество наборов данных в узле
	 * ! Всё, связанное с номерами наборов - сделано примитивно только  на одно ветвление! 
	 * @return
	 */
	public Integer cnCount() {
		Integer result = 0;
		
		
		if (children.size() >0) {
			for(Integer key: children.keySet()) {
				ContextNode childNode = children.get(key);
				result = result + childNode.cnCount();
			}
		}
		else {
			result = 1;
		}	
	
		return result;
	}
	
	
	/**
	 * Добавление объекта данных в узел
	 * @return
	 */
	public void cdAdd(String dataName, Object dataObject) {
		contextData.merge(dataName, dataObject, (k,v) -> v); // upsert
	}
	
	/**
	 * Добавление объекта данных в указанный набор
	 * ! Всё, связанное с номерами наборов - сделано примитивно только  на одно ветвление!
	 * @return
	 */
	public void cdAdd(String dataName, Object dataObject, Integer coSetNumber) {
		
		// Примитивно - если есть дочерние - надо писать в один из них
		// если нет - в себя
		
		if (children.size() > 0) { // 
			ContextNode childnode = children.get(coSetNumber);
			childnode.cdAdd(dataName, dataObject);
		}
		else {
			this.cdAdd(dataName, dataObject);
		}
		
	}
	
	
	/**
	 * Удаление объекта данных из узла
	 * @param dataName
	 */
	public void cdRemove(String dataName) {
		for(Integer key: children.keySet()) {
			ContextNode childNode = children.get(key);
			childNode.cdRemove(dataName);
		}
		contextData.remove(dataName); 
	}
	
	/**
	 * Получение списка объектов данных для указанного набора
	 * ! Всё, связанное с номерами наборов - сделано примитивно только  на одно ветвление!
	 * @param coSetNumber
	 */
	public HashMap<String,Object> cdGet(Integer coSetNumber) {
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		// Сначала выгребаем свои данные
		for(String key: contextData.keySet()) {
			result.put(key, contextData.get(key));
		}
		
		// Потом - данные из дочернего узла, ассоциированного с набором
		if (children.size() >= coSetNumber) {
			ContextNode childNode = children.get(coSetNumber);
			HashMap<String,Object> childResult = childNode.cdGet(coSetNumber);
			
			for(String key: childResult.keySet()) {
				result.put(key, childResult.get(key));
			}
		}
		
		
		return result;
	}
	
	/**
	 * Дамп данных (для отладки, вызывается из datacontext)
	 */
	public void dump() {
		
		System.out.println("");
		System.out.println("Node "+name);
		for(String key: contextObjects.keySet()) {
			System.out.println("contextObjects["+key+"]="+contextObjects.get(key).toString());
		}
		for(String key: contextData.keySet()) {
			System.out.println("contextData["+key+"]="+contextData.get(key).toString());
		}
		
		for(Integer key: children.keySet()) {
			ContextNode childNode = children.get(key);
			childNode.dump();
		}
	}
	
	
	

}
