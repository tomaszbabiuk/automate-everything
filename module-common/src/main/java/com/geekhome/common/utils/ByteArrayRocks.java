package com.geekhome.common.utils;

import java.util.ArrayList;

public class ByteArrayRocks
{
    private static final ArrayList<Integer> _empty = new ArrayList<>();

    public static ArrayList<Integer> locate(byte[] self, byte[] candidate)
    {
        if (isEmptyLocate(self, candidate))
            return _empty;

        ArrayList<Integer> list = new ArrayList<>();

        for (int i = 0; i < self.length; i++)
        {
            if (!isMatch(self, i, candidate))
                continue;

            list.add(i);
        }

        return list.size() == 0 ? _empty : list;
    }

    static boolean isMatch(byte[] array, int position, byte[] candidate)
    {
        if (candidate.length > (array.length - position))
            return false;

        for (int i = 0; i < candidate.length; i++)
            if (array[position + i] != candidate[i])
                return false;

        return true;
    }

    static boolean isEmptyLocate(byte[] array, byte[] candidate)
    {
        return array == null
                || candidate == null
                || array.length == 0
                || candidate.length == 0
                || candidate.length > array.length;
    }
}
