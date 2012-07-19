package org.droidex.util;

import java.util.Comparator;
import java.util.TreeMap;

import org.droidex.util.text.IndexedName;

public class NameOrderedDict<K extends IndexedName>
    extends TreeMap<K, DictNesting>
    implements DictNesting<K, DictNesting, Void>
{
    public NameOrderedDict() {
        super();
    }

    public boolean isEnd() {
        return false;
    }

    public void setData(Void data) {
    }

    public Void getData() {
        return null;
    }

    public DictNesting put(K k, DictNesting v) {
        return super.put(k, v);
    }

    public static class End<D>
        implements DictNesting<Void, Void, D>
    {
        D data;

        public End() {
        }

        public End(D data) {
            this.data = data;
        }

        public boolean isEnd() {
            return true;
        }

        public void setData(D data) {
            this.data = data;
        }

        public D getData() {
            return data;
        }
    
        public Void put(Void k, Void v) {
            return null;
        }
    }
}
