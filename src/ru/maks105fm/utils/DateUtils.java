package ru.maks105fm.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	static public Date getStartOfMonth(int add) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, add);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, c.getActualMinimum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c.getActualMinimum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));
		return c.getTime();
	}

	static public String getStartOfMonthStr(int add) {
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		return df.format(getStartOfMonth(add));
	}

	static public Date getEndOfMonth(int add) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, add);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, c.getActualMaximum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));
		return c.getTime();
	}

	static public String getEndOfMonthStr(int add) {
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		return df.format(getEndOfMonth(add));
	}

}
