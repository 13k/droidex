package org.droidex.util;

public interface DictNesting<K, V, D>
{
	public boolean isEnd();
	// nesting elements methods
	public V put(K k, V v);
	// nesting end-point methods
	public void setData(D d);
	public D getData();
}
