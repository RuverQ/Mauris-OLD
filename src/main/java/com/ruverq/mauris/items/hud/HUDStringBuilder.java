package com.ruverq.mauris.items.hud;

import com.ruverq.mauris.items.icons.MaurisIcon;
import com.ruverq.mauris.items.icons.MaurisOffsetIcon;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HUDStringBuilder{

    @Getter
    int currentOffset;

    StringBuilder sb = new StringBuilder();
    HashMap<Integer, MaurisIcon> icons = new HashMap<>();

    public void addSpace(){
        currentOffset++;
        sb.append(MaurisOffsetIcon.offsetForward);
    }

    public void getTo(int offset){
        while(offset != currentOffset){
            if(offset > currentOffset){
                addSpace();
            }else{
                addNegativeSpace();
            }
        }
    }

    public void addSpaces(int count){
        if(count < 0){
            addNegativeSpaces(-count);
            return;
        }
        for(int i = 0; i < count; i++){
            addSpace();
        }
    }

    public void addNegativeSpace(){
        /*
        MaurisIcon currentIcon = icons.get(currentOffset);
        if(currentIcon != null){
            for(int i = 0; i < currentIcon.getHeight() + 1; i++){
                sb.append(MaurisOffsetIcon.offsetBackward);
            }
            currentOffset--;
            return;
        }
         */
        currentOffset--;
        sb.append(MaurisOffsetIcon.offsetBackward);
    }

    public void addNegativeSpaces(int count){
        if(count < 0){
            addSpaces(-count);
            return;
        }
        for(int i = 0; i < count; i++){
            addNegativeSpace();
        }
    }

    /*
    public void addText(String text){
        sb.append(text);
        icons.add(currentOffset);
    }

     */

    public void addIcon(MaurisIcon icon){
        icons.put(currentOffset, icon);
        sb.append(icon.getNormalizedSymbol());

        currentOffset += icon.getHeight() + 1;
    }

    public String toString(){
        return sb.toString();
    }

}
