package edu.ua.cs.acm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import edu.ua.cs.acm.converters.LocalDateTimeConverter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Hunter Allen on 2/26/18
 */
@Entity
@Table(name="MemberSemesterLink")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberSemesterLink implements Serializable{

    @Id
    @Column(name="member_id")
    private int member_id;

    @Column(name="semester_id")
    private int semester_id;

    protected MemberSemesterLink() {

    }

    public MemberSemesterLink(int member_id, int semester_id) {
        this.member_id = member_id;
        this.semester_id = semester_id;
    }

    public void setMember_id(int member_id) { this.member_id = member_id; }

    public int getMember_id() { return this.member_id; }

    public void setSemester_id(int semester_id) { this.semester_id = semester_id; }

    public int getSemester_id() { return this.semester_id; }
}
