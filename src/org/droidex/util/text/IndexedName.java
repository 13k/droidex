package org.droidex.util.text;

public abstract class IndexedName
{
	public static final int INVALID = 0;

	private String name;
	private int number;

	public IndexedName(String name)
	{
		setName(name);
	}

	public IndexedName(int number)
	{
		setNumber(number);
	}
	
	public int getNumber()
	{
		return number;
	}
	
	public void setNumber(int number)
	{
		this.number = number;
		this.name = numberToName(number);
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
		this.number = nameToNumber(name);
	}

	public String toString()
	{
		return String.format("%s [%s]", name, number);
	}

	protected abstract int nameToNumber(String name);
	protected abstract String numberToName(int number);
}
