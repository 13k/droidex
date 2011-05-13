package org.droidex.util.text;

import java.util.Comparator;

public class IndexedNameComparator implements Comparator<IndexedName>
{
	public int compare(IndexedName first, IndexedName second)
	{
		return (new Integer(first.getNumber())).compareTo(new Integer(second.getNumber()));
	}

	public boolean equals(Object other)
	{
		return super.equals(other);
	}
}
