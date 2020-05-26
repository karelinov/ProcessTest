package com.sample.helpers;

import java.time.Period;

public class PeriodComparer {
	/**
	 * Вычисляет period1 < period2
	 * @param period1
	 * @param period2
	 * @return 
	 */
	public static Boolean  LT(Period period1, Period period2) {
		if (period1.getYears() == period2.getYears()) {
			if (period1.getMonths() == period2.getMonths()) {
				if (period1.getDays() == period2.getDays()) {
					return false;
				}
				else
					return (period1.getDays() < period2.getDays());
			}
			else
				return (period1.getMonths() < period2.getMonths());
		}
		else
			return (period1.getYears() < period2.getYears());
	}
	
	public static Boolean  LT(Object operiod1, String speriod2) {
		Period period1 = (Period)operiod1;
		Period period2 = Period.parse(speriod2);
		return LT(period1, period2);
	}
	
	/**
	 * Вычисляет period1 >= period2
	 * @param period1
	 * @param period2
	 * @return
	 */
	public static Boolean  GE(Period period1, Period period2) {
		if (period1.equals(period2)) { 
			return true; // если равно	
		}
		else { // если не равно, вычисляем из уже работающей процедуры , что второй не меньше 
			return !LT(period2, period1);
		}
		
	}
	
	public static Boolean  GE(Object operiod1, String speriod2) {
		Period period1 = (Period)operiod1;
		Period period2 = Period.parse(speriod2);
		return GE(period1, period2);
	}


}
