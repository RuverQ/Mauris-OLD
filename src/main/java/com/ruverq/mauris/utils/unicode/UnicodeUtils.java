package com.ruverq.mauris.utils.unicode;

import java.util.ArrayList;
import java.util.List;

public class UnicodeUtils {

    public static List<Character> getPossibleSymbols(){
        List<Character> chars = new ArrayList<>();

        chars.addAll(new UnicodeLocal().getSymbols());

        return chars;
    }

}
