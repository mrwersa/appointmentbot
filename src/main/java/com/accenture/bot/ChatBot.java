package com.accenture.bot;

import com.accenture.bot.db.model.Appointment;
import com.accenture.bot.query.AppointmentQuery;
import com.accenture.bot.query.WitAiAppointmentQuery;
import com.clivern.wit.Wit;
import com.clivern.wit.api.Message;
import com.clivern.wit.api.endpoint.MessageEndpoint;
import com.clivern.wit.util.Config;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static spark.Spark.get;

public class ChatBot {
    public static void main(String[] args) {
        get("/", (request, response) -> {

            Config config = new Config();
            config.loadPropertiesFile("src/main/resources/config.properties");
            config.configLogger();

            Wit wit = new Wit(config);

            // analyse the input message using wit.ai
            Message message = new Message(MessageEndpoint.GET);
            message.setQ(request.queryParams("q"));

            if (wit.send(message)) {
                // create and execute an appointment query based on the wit.ai response
                AppointmentQuery appointmentQuery = new WitAiAppointmentQuery(wit.getResponse());
                List<Appointment> result = appointmentQuery.execute();

                if (result == null) {
                    response.status(400);
                    return "Query couldn't be executed";
                }

                // convert the results to json
                Gson gson = new Gson();
                Type type = new TypeToken<List<Appointment>>() {
                }.getType();
                response.type("application/json");
                response.status(200);
                return gson.toJson(result, type);

            } else {
                response.status(500);
                return "Query couldn't be analysed";
            }
        });
    }
}
