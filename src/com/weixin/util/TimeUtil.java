package com.weixin.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TimeUtil {
	private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static String getStdDateTime() {
		return df.format(new Date());
	}
	
	
	public static int getSecondsToNextDay() {
		long now = System.currentTimeMillis();
		Calendar _calendar = Calendar.getInstance();
		_calendar.setTimeInMillis(now);
		_calendar.add(Calendar.DAY_OF_MONTH, 1);
		_calendar.set(Calendar.HOUR_OF_DAY, 0);
		_calendar.set(Calendar.MINUTE, 0);
		_calendar.set(Calendar.SECOND, 0);
		_calendar.set(Calendar.MILLISECOND, 0);
		return (int)((_calendar.getTimeInMillis()-now)/1000);
	}
	
	public static void main(String[] args) {
		System.out.println(getSecondsToNextDay()/3600);
	}
}
