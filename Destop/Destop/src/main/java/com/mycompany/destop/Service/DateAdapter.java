/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Service;

/**
 *
 * @author Windows 10
 */
import com.google.gson.*;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    // Serialize Date thành chuỗi JSON
    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        synchronized (DATE_FORMATTER) {
            return new JsonPrimitive(DATE_FORMATTER.format(src));
        }
    }

    // Deserialize chuỗi JSON thành Date
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        synchronized (DATE_FORMATTER) {
            try {
                return DATE_FORMATTER.parse(json.getAsString());
            } catch (ParseException e) {
                throw new JsonParseException("Lỗi khi chuyển đổi chuỗi JSON thành Date: " + e.getMessage(), e);
            }
        }
    }
}

