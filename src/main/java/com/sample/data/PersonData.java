package com.sample.data;

/**
 * Класс с данными ФЛ
 */
public class PersonData {
	public PersonData(int person_pk) {
		this.person_pk = person_pk; 
	}
	
	/**
	 * ФЛ, которому принадлежат данные
	 */
	public int person_pk;
	
	/**
	 * Тип данных
	 */
	public AtomType atom_type;
	
	/**
	 * ID данных
	 * - Для ЛК - это citizen_benefit_cat.benefit_cat_pk
	 */
	public String atom_id;
	
	/**
	 * Признак наличия атома у ФЛ
	 * - Для ЛК = наличие записи citizen_benefit_cat (benefit_cat_pk = <AtomId>)
	 */
	public Boolean atom_state;
	
	/**
	 * Данные атома
	 * - Для ЛК = citizen_benefit_cat.is_active	
	 */
	public Object atom_value;
	
	
	

}
