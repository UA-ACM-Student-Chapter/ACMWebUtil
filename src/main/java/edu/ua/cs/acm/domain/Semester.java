package edu.ua.cs.acm.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jzarobsky on 11/21/17.
 */
@Entity
@Table(name="Semester")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Semester implements Serializable {

    @Id
    @Column(name="semester_id")
    @SequenceGenerator(name="semester_id_sequence", sequenceName = "semester_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "semester_id_sequence")
    private int id;


    @Column(name="start_date")
    private Date startDate;

    @Column(name="end_date")
    private Date endDate;

    @ManyToMany(mappedBy = "semesters")
    private Set<Member> members = new HashSet<>();

    public Semester(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public Set<Member> getMembers() {
        return members;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }



}
