package com.ruverq.mauris.utils;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.math.NumberUtils;

public class BlockProperty {

    @Getter
    @Setter
    String property;

    @Getter
    @Setter
    String value;

    public JsonObject smartAdd(JsonObject jsonObject){

        if(value.equalsIgnoreCase("false") || value.equalsIgnoreCase("true")){
            jsonObject.addProperty(property, getAsBoolean());
            return jsonObject;
        }

        if(NumberUtils.isNumber(value)){
            jsonObject.addProperty(property, getAsInt());
            return jsonObject;
        }

        jsonObject.addProperty(property, value);
        return jsonObject;
    }

    public Object getAsObject(){

        if(value.equalsIgnoreCase("false") || value.equalsIgnoreCase("true")){
            return getAsBoolean();
        }

        if(NumberUtils.isNumber(value)){
            getAsInt();
        }


        return value;
    }

    public boolean getAsBoolean(){
        return Boolean.getBoolean(value);
    }

    public int getAsInt(){
        return Integer.parseInt(value);
    }

    public String getAsString(){
        return value;
    }

}
