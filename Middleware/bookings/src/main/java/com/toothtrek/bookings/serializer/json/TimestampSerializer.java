package com.toothtrek.bookings.serializer.json;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Custom serializer for Timestamp class.
 * <p>
 * Necessary to use when converting Timeslots dateAndTime attribute (Timestamp)
 * to JSON when sending data to the frontend.
 * As the frontend requires the date and time to be in the format of:
 * <p>
 * "yyyy-MM-dd HH:mm:ss".
 * 
 * @see com.toothtrek.bookings.entity.Timeslot
 * @see java.sql.Timestamp
 */
public class TimestampSerializer implements JsonSerializer<Timestamp> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public JsonElement serialize(Timestamp src, Type typeOfSrc, JsonSerializationContext context) {
        String formattedDate = dateFormat.format(src);
        return new JsonPrimitive(formattedDate);
    }
}
