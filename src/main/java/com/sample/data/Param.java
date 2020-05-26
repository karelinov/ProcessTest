package com.sample.data;

public class Param {
	public Param(String name, Object oValue) {
		super();
		this.name = name;
		this.oValue = oValue;
	}
	public Param(String name, Integer iValue) {
		super();
		this.name = name;
		this.iValue = iValue;
	}
	public Param(String name, String sValue) {
		super();
		this.name = name;
		this.sValue = sValue;
	}
	public String name;
	public String sValue;
	public Integer iValue;
	public Object oValue;
	
	

}
