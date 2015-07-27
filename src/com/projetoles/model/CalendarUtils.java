package com.projetoles.model;

import java.text.DateFormat;
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
		return DateFormat.getDateInstance(DateFormat.SHORT).format(dataCriacao.getTime());
	}

}
