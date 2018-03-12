package com.supconit.kqfx.web.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public final class CollectionUtils {
    private CollectionUtils() {
        
    }
    
    public static <T> Collection<T> diff(Collection<T> c1, Collection<T> c2) {
        if (c1 == null || c1.size() == 0 || c2 == null || c2.size() == 0) {
            return c1;
        }
        Collection<T> difference = new ArrayList<T>();
        for (T item : c1) {
            if (!c2.contains(item)) {
                difference.add(item);
            }
        }
        return difference;
    }
    
    public static <T> boolean isEmpty(Collection<T> c) {
        if (c == null || c.size() == 0) {
            return true;
        }
        for (Iterator<T> iter = c.iterator(); iter.hasNext();) {
            if (iter.next() != null) {
                return false;
            }
        }
        return true;
    }
    
}