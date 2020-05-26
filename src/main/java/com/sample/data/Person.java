package com.sample.data;


/**
 * 
 * @author Helg
 * Класс представляет (проверяемого/проверяемых) ФЛ 
 *
 */
public class Person {
	public Person(Integer person_pk) {
		this.person_pk = person_pk;
	}
	
	/**
	 * ID ФЛ
	 */
	public Integer person_pk;
	
	
	/**
	 * Признак, что именно этого ФЛ мы проверяем
	 * (Требуется, чтобы отличить ФЛ от причих людей семьи)
	 */
	public Boolean check;
	
}
