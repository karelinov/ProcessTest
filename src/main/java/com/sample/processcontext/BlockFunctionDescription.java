package com.sample.processcontext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ����� ����������� �������� ����� �������
 * �������� ��������� ����������  - ��� ������ ������ �������
 * ��� �������� ����� ������ �������{[��������],(�������)}
 * @author Helg
 */
public class BlockFunctionDescription {
	/**
	 * ������ ����� �����
	 */
	public String sBlockFunction;
	/**
	 * ��� �������
	 */
	public FunctionType functionType;
	/**
	 * ��������
	 */
	public String contextToken;
	/**
	 * �������
	 */
	public String formulaToken; 

	
	// ^(\w+)\{\[([\w]*)\]\,([\(\w\\+\*)]+)\}$
	public static final String BF_PATTERN = "^(\\w+)\\{\\[([\\w]*)\\]\\,([\\(\\w\\\\+\\*)]+)\\}$";
	public static final String FORMULA_ARGUMENTS_PATTERN = "[A-Z_]{1,10}[\\d]{1,5}"; 
	/**
	 * ������� ������ ����� ����� � ����������
	 * @param sBlockFunction
	 * @return
	 */
	public static BlockFunctionDescription parse(String sBlockFunction) {
		BlockFunctionDescription result = new BlockFunctionDescription();
		
		Pattern pattern = Pattern.compile(BF_PATTERN);
		Matcher matcher = pattern.matcher(sBlockFunction);
		if (matcher.find()) {
			if (matcher.groupCount() != 3)
				throw new RuntimeException("����� ����� �� ������������� ������� ="+matcher.groupCount());
			
			result.sBlockFunction = sBlockFunction;
			result.functionType = FunctionType.valueOf(matcher.group(1));
			result.contextToken = matcher.group(2);
			result.formulaToken = matcher.group(3);
		}
		else throw new RuntimeException("����� ����� �� ������������� �������");

		
		return result;
	}
	
	public static Boolean tryParse(String sBlockFunction) {
		Boolean result = true;
		
		try {
			parse(sBlockFunction);
		}
		catch (Exception re) {
			result = false;
		}
		
		return result;
	}	
	
	
}
