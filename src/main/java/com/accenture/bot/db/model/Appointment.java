package com.accenture.bot.db.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "appointment")

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "get_appointments_by_date",
                query = "select * from appointment where datetime >= ?1",
                resultClass = Appointment.class),
        @NamedNativeQuery(
                name = "get_appointments",
                query = "from appointment where datetime >= current_timestamp()",
                resultClass = Appointment.class)
})
public class Appointment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "datetime", nullable = false)
    private Date datetime;

    public Integer getId() {
        return id;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
