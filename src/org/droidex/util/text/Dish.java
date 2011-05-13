package org.droidex.util.text;

public class Dish extends IndexedName
{
	public static final int SALAD   = 1;
	public static final int SIDE    = 2;
	public static final int MAIN    = 3;
	public static final int DESSERT = 4;

	public Dish(String name)
	{
		super(name);
	}

	public String getDisplayName() {
		switch(getNumber()) {
			case SALAD:   return "Salada";
			case SIDE:    return "Acompanhamento";
			case MAIN:    return "Principal";
			case DESSERT: return "Sobremesa";
			default: return "";
		}
	}

	protected String numberToName(int number)
	{
		switch(number) {
			case SALAD:   return "salada";
			case SIDE:    return "acompanhamento";
			case MAIN:    return "principal";
			case DESSERT: return "sobremesa";
			default:     return "";
		}
	}

	protected int nameToNumber(String name)
	{
		if ("salada".equals(name)) {
			return SALAD;
		} else if ("acompanhamento".equals(name)) {
			return SIDE;
		} else if ("principal".equals(name)) {
			return MAIN;
		} else if ("sobremesa".equals(name)) {
			return DESSERT;
		} else {
			return INVALID;
		}
	}
}
