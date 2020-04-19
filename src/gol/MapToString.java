package gol;

import java.util.*;

public class MapToString {
    @SuppressWarnings("unchecked")
    public static String toString(Map m) {
        StringBuffer b = new StringBuffer();
        for (Object name : new TreeSet(m.keySet())) {
            b.append(name.toString()).append(" : ").append(m.get(name).toString()).append("\n");
        }
        return b.toString();
    }
}
