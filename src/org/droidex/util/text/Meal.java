package org.droidex.util.text;

public class Meal extends IndexedName
{
	public static final int LUNCH  = 1;
	public static final int DINNER = 2;

	public Meal(String name)
	{
		super(name);
	}

	public String getDisplayName() {
		switch(getNumber()) {
			case LUNCH:  return "Almoço";
			case DINNER: return "Jantar";
			default:     return "";
		}
	}

	protected String numberToName(int number)
	{
		switch(number) {
			case LUNCH:  return "almoco";
			case DINNER: return "jantar";
			default:     return "";
		}
	}

	protected int nameToNumber(String name)
	{
		if ("almoco".equals(name)) {
			return LUNCH;
		} else if ("jantar".equals(name)) {
			return DINNER;
		} else {
			return INVALID;
		}
	}
}

