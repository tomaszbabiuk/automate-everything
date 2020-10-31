package com.geekhome.common.math;

import java.util.ArrayList;
import java.util.Calendar;

public class AverageCollection<T extends Timestamped> extends ArrayList<T> {

    private final Class<T> _clazz;

    public AverageCollection(Class<T> clazz) {
        _clazz = clazz;
    }

    public T findOrCreate(Calendar now) throws IllegalAccessException, InstantiationException {
        int ms = now.get(Calendar.MILLISECOND);
        int s = now.get(Calendar.SECOND) * 1000;
        int min = now.get(Calendar.MINUTE) * 60 * 1000;
        long hours = now.getTimeInMillis() - min - s - ms;

        if (size() > 0) {
            T lastAverage = get(size() - 1);
            if (lastAverage.getTimestamp() == hours) {
                return lastAverage;
            }
        }

        T newInstance = _clazz.newInstance();
        newInstance.setTimestamp(hours);
        add(newInstance);
        if (size() > 2) {
            remove(0);
        }
        return newInstance;
    }
}