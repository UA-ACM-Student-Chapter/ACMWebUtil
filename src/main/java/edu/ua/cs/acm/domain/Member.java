package edu.ua.cs.acm.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jzarobsky on 11/21/17.
 */
@Entity
@Table(name="Member")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Member implements Serializable {

    @Id
    @Column(name = "member_id")
    @SequenceGenerator(name = "member_id_sequence", sequenceName = "member_id_sequence")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "member_id_sequence")
    private int id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="shirt_size")
    private String shirtSize;

    @Column(name="birthday")
    private Date birthday;

    @Column(name="crimson_email")
    private String crimsonEmail;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="MemberSemesterLink", joinColumns = { @JoinColumn(name="member_id", referencedColumnName = "member_id") },
            inverseJoinColumns = { @JoinColumn(name = "semester_id", referencedColumnName = "semester_id") })
    private Set<Semester> semesters = new HashSet<>();

    public Set<Semester> getSemesters() {
        return semesters;
    }

    public void setSemesters(Set<Semester> semesters) {
        this.semesters = semesters;
    }

    // Needed for Spring to create entities
    protected Member() {

    }

    public Member(String firstName, String lastName, String shirtSize, Date birthday, String crimsonEmail) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.shirtSize = shirtSize;
        this.birthday = birthday;
        this.crimsonEmail = crimsonEmail;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getShirtSize() {
        return shirtSize;
    }

    public void setShirtSize(String shirtSize) {
        this.shirtSize = shirtSize;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getCrimsonEmail() {
        return crimsonEmail;
    }

    public void setCrimsonEmail(String crimsonEmail) {
        this.crimsonEmail = crimsonEmail;
    }
}
