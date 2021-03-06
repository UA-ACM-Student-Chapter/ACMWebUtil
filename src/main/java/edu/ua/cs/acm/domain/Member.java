package edu.ua.cs.acm.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.google.gson.annotations.Expose;
import edu.ua.cs.acm.converters.LocalDateTimeConverter;

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
    @Expose
    @SequenceGenerator(name = "member_id_sequence", sequenceName = "member_id_sequence")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "member_id_sequence")
    private int id;

    @Column(name="first_name")
    @Expose
    private String firstName;

    @Column(name="last_name")
    @Expose
    private String lastName;

    @Column(name="shirt_size")
    @Expose
    private String shirtSize;

    @Column(name="birthday")
    @Expose
    @Convert(converter = LocalDateTimeConverter.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime birthday;

    @Column(name="crimson_email")
    @Expose
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

    public Member(String firstName, String lastName, String shirtSize, LocalDateTime birthday, String crimsonEmail) {
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

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public String getCrimsonEmail() {
        return crimsonEmail;
    }

    public void setCrimsonEmail(String crimsonEmail) {
        this.crimsonEmail = crimsonEmail;
    }
}
