package com.accenture.bot.query.Intent;

import com.accenture.bot.db.common.HibernateUtil;
import com.accenture.bot.db.model.Appointment;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;

public class AppointmentIntentShow implements AppointmentIntent {
    private Date datetime;

    public AppointmentIntentShow(Date datetime) {
        this.datetime = datetime;
    }

    @Override
    public List<Appointment> resolve() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            if (datetime == null) {
                return session.createNamedQuery("get_appointments", Appointment.class).getResultList();
            } else {
                return session.createNamedQuery("get_appointments_by_date", Appointment.class).setParameter(1, datetime).getResultList();
            }
        } finally {
            if (session != null) {
                HibernateUtil.shutdown();
            }
        }

    }
}
