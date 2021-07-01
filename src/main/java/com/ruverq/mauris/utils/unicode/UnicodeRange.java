package com.ruverq.mauris.utils.unicode;


import java.util.ArrayList;
import java.util.List;

public interface UnicodeRange {

    String min();
    String max();

    default List<Character> getSymbols(){
        List<Character> chars = new ArrayList<>();

        int min = Integer.parseInt(min(), 16);
        int max = Integer.parseInt(max(), 16);

        for(int i = min; i < max; i++){
            chars.add((char) i);
        }

        return chars;
    }
}
