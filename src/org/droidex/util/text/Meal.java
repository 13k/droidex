package org.droidex.util.text;

public class Meal
    extends IndexedName
{
    public static enum Index { almoco, jantar, INVALID }

    public Meal(String name) {
        super(Index.class);
        setName(name);
    }

    @Override
    public String getDisplayName() {
        switch((Index)getEnumConst()) {
            case almoco:
                return "Almoço";
            case jantar:
                return "Jantar";
            default:
                return "";
        }
    }
}
