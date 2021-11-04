package com.ruverq.mauris.crafts.types;

import java.util.HashMap;

public class RecipeTypeManager {

    public static HashMap<String, RecipeType> types = new HashMap<>();

    public static void setUp(){
        types.clear();

        addRTM(new RTMBlastFurnace());
        addRTM(new RTMCampfire());
        addRTM(new RTMFurnace());
        addRTM(new RTMShaped());
        addRTM(new RTMShapeless());
        addRTM(new RTMSmithing());
        addRTM(new RTMSmoker());
    }

    //RTM stands for Recipe Type Mauris
    public static void addRTM(RecipeType rt){
        types.put(rt.name().toLowerCase(), rt);
    }

    public static RecipeType getFromName(String name){
        name = name.toLowerCase();
        return types.get(name);
    }
}
