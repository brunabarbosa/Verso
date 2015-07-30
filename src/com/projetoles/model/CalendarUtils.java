package com.projetoles.model;

import java.util.Calendar;
import java.util.Locale;

public class CalendarUtils {

	public static Calendar longToCalendar(Long timeInMilli) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeInMilli);
		return c;
	}
	
	public static Calendar stringToCalendar(String timeInMilli) {
		return CalendarUtils.longToCalendar(Long.valueOf(timeInMilli));
	}
	
	public static String getTimeInMilli(Calendar c) {
		return String.valueOf(c.getTimeInMillis());
	} 

	public static String getDataFormada(Calendar dataCriacao) {
		return String.format(Locale.getDefault(), "%02d/%02d/%d às %02d:%02d", dataCriacao.get(Calendar.DAY_OF_MONTH), dataCriacao.get(Calendar.MONTH), dataCriacao.get(Calendar.YEAR),
				dataCriacao.get(Calendar.HOUR_OF_DAY), dataCriacao.get(Calendar.MINUTE));
	}

}
