package org.droidex.util.text;

public class Dish
    extends IndexedName
{
    public static enum Index { salada, acompanhamento, principal, sobremesa, INVALID }

    public Dish(String name) {
        super(Index.class);
        setName(name);
    }
}
