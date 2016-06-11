package me.gamerzking.core.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by GamerzKing on 5/21/2016.
 */
public class UtilTime {

	private static Calendar calendar = Calendar.getInstance();

	private static final String DATE_FORMAT_DETAILED = "MM-dd-yyyy HH:mm:ss";
	private static final String DATE_FORMAT_DATE = "MM-dd-yyyy";

	public static String getCurrentTime() {

		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DETAILED);
		return dateFormat.format(calendar.getTime());
	}

	public static String getDate() {

		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DATE);
		return dateFormat.format(calendar.getTime());
	}

	public static boolean elapsed(long from, long to)
	{
		return System.currentTimeMillis() - from >= to;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public static String getDateFormatDetailed() {
		return DATE_FORMAT_DETAILED;
	}

	public static String getDateFormatDate() {
		return DATE_FORMAT_DATE;
	}
}

