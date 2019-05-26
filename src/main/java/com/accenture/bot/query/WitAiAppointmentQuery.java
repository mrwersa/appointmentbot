package com.accenture.bot.query;

import com.accenture.bot.db.model.Appointment;
import com.accenture.bot.query.Intent.AppointmentIntent;
import com.accenture.bot.query.Intent.AppointmentIntentCreate;
import com.accenture.bot.query.Intent.AppointmentIntentShow;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WitAiAppointmentQuery implements AppointmentQuery {

    private Date datetime;
    private AppointmentIntent appointmentIntent;

    public WitAiAppointmentQuery(String response) {
        JsonParser parser = new JsonParser();
        JsonElement jsonTree = parser.parse(response);
        JsonObject responseObject = jsonTree.getAsJsonObject();

        try {
            this.datetime = this.getDatetimeEntity(responseObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.appointmentIntent = this.getIntentType(responseObject, this.datetime);
    }

    private Date getDatetimeEntity(JsonObject responseObject) throws ParseException {
        if (!responseObject.has("entities") || !responseObject.get("entities").getAsJsonObject().has("datetime")) {
            return null;
        }

        String type = responseObject.get("entities").getAsJsonObject().get("datetime").getAsJsonArray().get(0).getAsJsonObject().get("type").getAsString();

        String value = null;
        if (type.equalsIgnoreCase("value")) {
            value = responseObject.get("entities").getAsJsonObject().get("datetime").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();
        } else if (type.equalsIgnoreCase("interval")) {
            value = responseObject.get("entities").getAsJsonObject().get("datetime").getAsJsonArray().get(0).getAsJsonObject().get("to").getAsJsonObject().get("value").getAsString();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault());
        return value == null ? null : sdf.parse(value);
    }

    private AppointmentIntent getIntentType(JsonObject responseObject, Date datetime) {
        if (!responseObject.has("entities") || !responseObject.get("entities").getAsJsonObject().has("intent")) {
            return null;
        }

        String value = responseObject.get("entities").getAsJsonObject().get("intent").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();
        AppointmentIntent appointmentIntent = null;
        if (value.equalsIgnoreCase("appointment_create")) {
            appointmentIntent = new AppointmentIntentCreate(datetime);
        } else if (value.equalsIgnoreCase("appointment_show")) {
            appointmentIntent = new AppointmentIntentShow(datetime);
        }

        return appointmentIntent;
    }


    public List<Appointment> execute() {
        if (this.appointmentIntent != null) {
            return this.appointmentIntent.resolve();
        } else {
            return null;
        }
    }
}
