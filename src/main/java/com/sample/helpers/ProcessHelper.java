package com.sample.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.sample.processcontext.BlockFunctionDescription;
import com.sample.processcontext.ContextNode;
import com.sample.processcontext.DataContext;
import com.sample.processcontext.FunctionType;
import com.sample.processcontext.RuleResult;

/**
 * Хэлпер для усправления процессами
 * @author Helg
 *
 */
public class ProcessHelper {
	
	/**
	 * Функция проверяет тип контекста - просто набор переменных или вложенный бдок функций
	 * @param sBlockFunction
	 * @param dataContext
	 * @return
	 */
	public static Boolean checkIsBlockFunction(String sBlockFunction) {
		return (BlockFunctionDescription.tryParse(sBlockFunction));
	}
	
	/**
	 * Функция проверяет, требуется ли вычисление контекста
	 * @param sBlockFunction
	 * @return
	 */
	public static Boolean checkContextIsBlockFunction(String sToken, DataContext datacontext) {
		// пустая строка - ничего вычислять не надо
		if (sToken.length() ==0) {
			return false;
		}
		if (BlockFunctionDescription.tryParse(sToken)) {
			return true;
		}
		else { // Иначе- в строке контекста либо данные контекста, либо DEFINITION
			// Проверяем, что в строке контекста - DEFINITION, если да - значит блок функций
			if(datacontext.defsGet().containsKey(sToken)) {
				return true;
			}
			else {
				return false;
				/*
				// на всякий случай проверим, что объект контекст есть в объекте контекста
				if(datacontext.getCurrentContextObjectSet().containsKey(sToken)) {
					return false;
				}
				else
					throw new RuntimeException("Токен не определён");
				*/	
			}
		}
	}
	
	

	/***
	 * Функция выделяет из логической формулы вида X1*(X2+X3) отдельные аргументы
	 * @param sFormula
	 * @return
	 */
	public static ArrayList<String> getFormulaItems(String sFormula) {
		ArrayList<String> result = new ArrayList<String>();
		
		Pattern pattern = Pattern.compile(BlockFunctionDescription.FORMULA_ARGUMENTS_PATTERN);
		Matcher matcher = pattern.matcher(sFormula);
		while (matcher.find()) {
			result.add(matcher.group());
		}
		
		return result;
	}
	
	
	private static Integer processIdent = 0; 
	
	/**
	 * Вывод сообщения с учётом вложенности процесса
	 * @param msg
	 */
	public static void out(String msg) {
		String identFill = "----------------------------------------------> ";		
		
		if (identFill.length() >= processIdent+2) {
			identFill = identFill.substring(identFill.length() - 2 - processIdent);
		}
		else {
			identFill = "X"+identFill;
		}

		System.out.println(processIdent.toString()+ identFill + msg);
		
	}

	/**
	 * Функция фиксации увеличения вложенности
	 * @param datacontext
	 */
	public static void incIdent() {
		processIdent++;
	}
	/**
	 * Функция фиксации уменьшения вложенности
	 * @param datacontext
	 */
	public static void decIdent() {
		processIdent--;
		
		if (processIdent <0) {
			throw new RuntimeException("Установлен уровень вложенности");
		}
	}
	
	/**
	 * Реализация функции SELECT
	 * @param definition
	 * @param datacontext
	 */
	public static void functionSELECT(String definition, DataContext datacontext) {
//    	datacontext.setDefinition("X1", "EXISTS{[K1],X2}");
//    	datacontext.setDefinition("K1", "SELECT{[],FAMILY1}");
//    	datacontext.setDefinition("FAMILY1", "RULE{[],1001_FAMILY}");
		
		BlockFunctionDescription bfDescription = BlockFunctionDescription.parse(definition);
		
		String defName = datacontext.defGetName(definition); // получаем имя для definition
		
		// Получаем данные, записанные для определения
		String dataObjectName = bfDescription.formulaToken; // FAMILY1
		RuleResult  ruleResult = (RuleResult) datacontext.context.cdGet(datacontext.curContextNumber).get(dataObjectName);
		// Проходимся по списку данных
		if (ruleResult.resultObjects.size() == 0) { // если данных нет
			ContextNode childNode = datacontext.context.cnAddChild(dataObjectName); // FAMILY1
			childNode.cdAdd(dataObjectName, null);
		}
		else {
			for (int i = 1; i<=ruleResult.resultObjects.size(); i++) {
				ContextNode childNode = datacontext.context.cnAddChild(dataObjectName); // FAMILY1
				childNode.coAdd(dataObjectName, ruleResult.resultObjects.get(i-1)); 
			}
		}
		
		// объект данных, использованный для расщепления контекста, удаляется
		datacontext.context.cdRemove(dataObjectName);
		
	}
	
	/**
	 * Реализация функции EVAL  - формирование лог. выражения (вычислится в WF в jscript)
	 * @param definition
	 * @param datacontext
	 */
	public static Object functionEVAL(String definition, DataContext datacontext) {
		String resultString;
		Object result;
		
		// Получаем определение текущего блока
		BlockFunctionDescription bfDescription = BlockFunctionDescription.parse(definition);
		// Получаем формулу блока
		resultString = bfDescription.formulaToken;
		// Получаем аргументы формулы
		ArrayList<String> formulaItems = getFormulaItems(bfDescription.formulaToken);
		// Получаем список объектов данных контекста (для текущего номера набора)
		HashMap<String,Object> contextData = datacontext.context.cdGet(datacontext.curContextNumber);
		
		// Проходимся по элементам 
		for(int i = 0; i< formulaItems.size(); i++) {
			String curItem = formulaItems.get(i);
			
			// Получаем объекты данных
			Boolean curItemRes;
			Object res = contextData.get(curItem);
			if (res instanceof RuleResult)
				curItemRes = ((RuleResult)contextData.get(curItem)).result;
			else
				curItemRes = (Boolean) res;
			
			
			// Подставляем в результат
			resultString = resultString.replace(curItem, curItemRes.toString());
		}
		// заменяем логические операторы
		resultString = resultString.replace("*"," && ");
		resultString = resultString.replace("+"," || ");
		
		
	    ScriptEngineManager scriptEngineManager=new ScriptEngineManager();
	    ScriptEngine scriptEngine=scriptEngineManager.getEngineByName("nashorn");
	    try {
	    	result = scriptEngine.eval(resultString);
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}		
		
		
		return result; 
	}
	

	/**
	 * Реализация функции EXISTS
	 * @param definition
	 * @param datacontext
	 */
	public static Boolean functionEXISTS(String definition, DataContext datacontext) {
//    	datacontext.setDefinition("X1", "EXISTS{[K1],X2}");
//    	datacontext.setDefinition("K1", "SELECT{[],FAMILY1}");
//    	datacontext.setDefinition("FAMILY1", "RULE{[],1001_FAMILY}");
		
		Boolean result = false;
		
		// Получаем описание блока // EXISTS{[K1],X2}
		BlockFunctionDescription bfDescription = BlockFunctionDescription.parse(definition);
		// Получаем имя объекта данных, которые проверяем
		String checkDataName = bfDescription.formulaToken; // X2    
		
		
		// Получаем результат
		// надо получить все результаты в каждом наборе данных 
		for(int i=1; i<= datacontext.context.cnCount(); i++) {
			HashMap<String,Object> contextData = datacontext.context.cdGet(i);
			Boolean dataToCheck = (Boolean)contextData.get(checkDataName);
			if (dataToCheck) result = true;
		}
		
		// надо удалить агрегируемые узлы 
		datacontext.context.coRemove(bfDescription.contextToken); // K1
		String childDefinition = datacontext.defGet(bfDescription.contextToken); // SELECT{[],FAMILY1}
		BlockFunctionDescription childDescription = BlockFunctionDescription.parse(childDefinition);
		datacontext.context.cnRemoveChild(childDescription.formulaToken); // FAMILY1 
		
		// Записываем результат
		String resultName = datacontext.defGetName(definition); // получаем имя для definition
		datacontext.context.cdAdd(resultName, result);
		
		return result;
	}	
	
}
