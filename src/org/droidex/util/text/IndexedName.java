package org.droidex.util.text;

import java.util.Arrays;
import java.util.ArrayList;

public abstract class IndexedName
    implements Comparable
{
    private String name;
    private int index;
    private ArrayList<String> names;
    private Object[] enumConsts;

    private IndexedName() {};

    protected <T extends Enum> IndexedName(Class<T> klass) {
        enumConsts = klass.getEnumConstants();
        names = new ArrayList<String>(enumConsts.length);
        for (Object cnst : enumConsts)
            names.add(((T)cnst).name());
    }

    protected IndexedName(ArrayList<String> arr) {
        names = arr;
    }
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int i) {
        index = i;
        name = names.get(i);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String n) {
        name = n;
        index = names.indexOf(name);
        if (index < 0)
            org.droidex.util.L.d("index: " + index + " name: " + name + " names: " + names.toString());
    }

    public <T extends Enum> T getEnumConst() {
        return (T)enumConsts[index];
    }

    public String toString() {
        return String.format("%s [%s]", name, index);
    }

    public String getDisplayName() {
        return getName().substring(0, 1).toUpperCase() + getName().substring(1);
    }

    public int compareTo(Object other) {
        return ( new Integer(getIndex()) )
            .compareTo( new Integer(((IndexedName)other).getIndex()) );
    }

    public boolean equals(Object other) {
        return ( index == ((IndexedName)other).getIndex() );
    }
}
