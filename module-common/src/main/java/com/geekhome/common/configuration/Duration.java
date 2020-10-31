package com.geekhome.common.configuration;

public class Duration
{
    public static long parse(String duration)
    {
        String[] durationSplitted = duration.split(":");
        int hours = Integer.parseInt(durationSplitted[0]);
        int mins = Integer.parseInt(durationSplitted[1]);
        int secs = Integer.parseInt(durationSplitted[2]);
        int totalSecs = secs + mins * 60 + hours * 3600;
        return totalSecs * 1000;
    }
}
