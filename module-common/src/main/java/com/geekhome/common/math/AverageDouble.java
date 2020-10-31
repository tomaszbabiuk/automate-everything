package com.geekhome.common.math;

public class AverageDouble implements Timestamped{
    private long _timestamp;
    private double _sum;
    private int _count;

    public void setTimestamp(long timestamp) {
        _timestamp = timestamp;
    }

    public void include(double value) {
        _sum += value;
        _count++;
    }

    public double getAverage() {
        if (_count == 0) {
            return 0;
        }

        return _sum / _count;
    }

    public long getTimestamp() {
        return _timestamp;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AverageDouble && ((AverageDouble)obj).getTimestamp() == getTimestamp();
    }
}