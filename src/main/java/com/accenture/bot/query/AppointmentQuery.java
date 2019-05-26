package com.accenture.bot.query;

import com.accenture.bot.db.model.Appointment;

import java.util.List;

public interface AppointmentQuery {

    /**
     * Execute an appointment query
     *
     * @return a list of appointments (result of the intent)
     */
    List<Appointment> execute();
}
