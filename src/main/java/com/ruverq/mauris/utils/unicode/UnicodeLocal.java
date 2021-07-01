package com.ruverq.mauris.utils.unicode;

public class UnicodeLocal implements UnicodeRange{
    @Override
    public String min() {
        return "E000";
    }

    @Override
    public String max() {
        return "F8FF";
    }
}
