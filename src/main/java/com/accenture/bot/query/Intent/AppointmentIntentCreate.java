package com.accenture.bot.query.Intent;

import com.accenture.bot.db.common.HibernateUtil;
import com.accenture.bot.db.model.Appointment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentIntentCreate implements AppointmentIntent {
    private Date datetime;

    public AppointmentIntentCreate(Date datetime) {
        this.datetime = datetime;
    }

    @Override
    public List<Appointment> resolve() {
        List<Appointment> result = new ArrayList<>();

        if (datetime != null) {
            Session session = null;
            Transaction transaction = null;

            try {
                session = HibernateUtil.getSessionFactory().openSession();
                transaction = session.beginTransaction();

                Appointment appointment = new Appointment();
                appointment.setDatetime(datetime);
                session.save(appointment);

                //Commit the transaction
                session.getTransaction().commit();
                result.add(appointment);
            } catch (RuntimeException runtimeException) {
                if (transaction != null) {
                    transaction.rollback();
                    result = null;
                }
            } finally {
                if (session != null) {
                    HibernateUtil.shutdown();
                }
            }
        }
        return result;
    }
}
