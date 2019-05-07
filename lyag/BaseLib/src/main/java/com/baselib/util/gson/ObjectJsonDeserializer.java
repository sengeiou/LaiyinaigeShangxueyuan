package com.baselib.util.gson;

import com.baselib.util.GsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;


public class ObjectJsonDeserializer implements JsonDeserializer {

    @Override
    public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (jsonElement.isJsonObject()) {
            try {
                JsonObject object = jsonElement.getAsJsonObject();
                return GsonUtils.jsonToBean(object.toString(), type);
            } catch (Exception ignore) {
                return null;
            }
//            JsonObject object = jsonElement.getAsJsonObject();
//            Type itemType = ((ParameterizedType) type).getActualTypeArguments()[0];
//            return jsonDeserializationContext.deserialize(object, itemType);
        } else {
            return null;
        }
    }

}
