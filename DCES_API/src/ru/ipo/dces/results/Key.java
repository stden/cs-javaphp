package ru.ipo.dces.results;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: ilya
 * Date: 14.10.2010
 * Time: 17:20:59
 */
public class Key {

    public enum SortDirection {
        No,
        Asc,
        Desc,
    }

    private String name;
    private boolean transferable = true;
    private boolean summable = true;
    private SortDirection sortDirection = SortDirection.Desc;

    public String getName() {
        return name;
    }

    public boolean isTransferable() {
        return transferable;
    }

    public boolean isSummable() {
        return summable;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    // factory

    private static HashMap<String, Key> keys = new HashMap<String, Key>();

    private static void addKey(Key key) {
        keys.put(key.getName(), key);
    }

    public static Key getKey(String name) {
        return keys.get(name);
    }

    public static Key[] getAllKeys() {
        return keys.values().toArray(new Key[keys.values().size()]);
    }

    private Key(String name) {
        this.name = name;
    }

    // initialization
    static {
        Key k;

        k = new Key("scores");
        addKey(k);

        k = new Key("penalty");
        k.sortDirection = SortDirection.Asc;
        addKey(k);

        k = new Key("jury");
        k.summable = false;
        k.transferable = false;
        k.sortDirection = SortDirection.No;
        addKey(k);       
    }
}
