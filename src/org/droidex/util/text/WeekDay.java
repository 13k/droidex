package org.droidex.util.text;

import android.text.format.DateUtils;
import java.util.Calendar;

public class WeekDay extends IndexedName
{
	private String date;

	public WeekDay(String name)
	{
		super(name);
	}

	public WeekDay(int number)
	{
		super(number);
	}
	
	public String getDate()
	{
		return date;
	}
	
	public void setDate(String date)
	{
		this.date = date;
	}

	public String getDisplayName()
	{
		return DateUtils.getDayOfWeekString(getNumber(), DateUtils.LENGTH_LONG);
	}

	public String getFullDisplayName()
	{
		return getDisplayName() + ((date != null) ? String.format(" (%s)", date) : "");
	}

	protected String numberToName(int number)
	{
		switch(number) {
			case Calendar.MONDAY:    return "segunda";
			case Calendar.TUESDAY:   return "terca";
			case Calendar.WEDNESDAY: return "quarta";
			case Calendar.THURSDAY:  return "quinta";
			case Calendar.FRIDAY:    return "sexta";
			case Calendar.SATURDAY:  return "sabado";
			case Calendar.SUNDAY:    return "domingo";
			default:                 return "";
		}
	}

	protected int nameToNumber(String name)
	{
		if ("domingo".equals(name)) {
			return Calendar.SUNDAY;
		} else if ("segunda".equals(name)) {
			return Calendar.MONDAY;
		} else if ("terca".equals(name)) {
			return Calendar.TUESDAY;
		} else if ("quarta".equals(name)) {
			return Calendar.WEDNESDAY;
		} else if ("quinta".equals(name)) {
			return Calendar.THURSDAY;
		} else if ("sexta".equals(name)) {
			return Calendar.FRIDAY;
		} else if ("sabado".equals(name)) {
			return Calendar.SATURDAY;
		} else {
			return INVALID;
		}
	}
}
