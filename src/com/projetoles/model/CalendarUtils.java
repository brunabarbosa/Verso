package com.projetoles.model;

import java.util.Calendar;

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
		return dataCriacao.get(Calendar.DAY_OF_MONTH) + "/" + dataCriacao.get(Calendar.MONTH) + "/" + dataCriacao.get(Calendar.YEAR) 
				+ " às " + dataCriacao.get(Calendar.HOUR_OF_DAY) + ":" + dataCriacao.get(Calendar.MINUTE);//DateFormat.getDateInstance(DateFormat.SHORT).format(dataCriacao.getTime());
	}

}
