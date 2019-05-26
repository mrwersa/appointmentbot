package com.accenture.bot.query.Intent;

import com.accenture.bot.db.model.Appointment;

import java.util.List;

public interface AppointmentIntent {

    /**
     * Resolve an appointment intent
     *
     * @return the result
     */
    List<Appointment> resolve();
}
