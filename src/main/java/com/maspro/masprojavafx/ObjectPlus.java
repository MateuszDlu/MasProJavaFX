package com.maspro.masprojavafx;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class ObjectPlus {

    private static HashMap<Class, List> extents = new HashMap<>();
    public static final String EXTENT_FILENAME = "data.dat";

    public ObjectPlus() {
        List ex = extents.get(this.getClass());
        if (ex == null) {
            ex = new ArrayList();
            extents.put(this.getClass(), ex);
        }
        ex.add(this);
    }

    protected void removeThisFromClassExtent() {
        extents.get(this.getClass()).remove(this);
    }

    public static Map<Class, List> getExtents() {
        return extents;
    }

    public static <E> List<E> getExtent(Class<E> clazz) {
        List list = extents.get(clazz);
        if (list == null) {
            return new ArrayList<>();
        }
        return Collections.unmodifiableList(list);
    }

    public static void saveExtent(ObjectOutputStream oos) throws IOException {
        oos.writeObject(extents);
    }

    public static void loadExtent(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        extents = (HashMap<Class, List>) ois.readObject();
    }

}
