CREATE SEQUENCE member_id_sequence;

CREATE TABLE Member (
  member_id INT NOT NULL DEFAULT nextval('member_id_sequence') PRIMARY KEY,
  first_name VARCHAR(100) NOT NULL DEFAULT '',
  last_name VARCHAR(100) NOT NULL DEFAULT '',
  shirt_size VARCHAR(100) NOT NULL DEFAULT 'M',
  birthday TIMESTAMP NOT NULL DEFAULT NOW(),
  crimson_email VARCHAR(200) NOT NULL
);

ALTER SEQUENCE member_id_sequence OWNED BY Member.member_id;

-- BEGIN SEMESTER

CREATE SEQUENCE semester_id_sequence;

CREATE TABLE Semester (
  semester_id INT NOT NULL DEFAULT nextval('semester_id_sequence') PRIMARY KEY,
  start_date TIMESTAMP NOT NULL,
  end_date TIMESTAMP NOT NULL
);

ALTER SEQUENCE semester_id_sequence OWNED BY Semester.semester_id;

CREATE TABLE MemberSemesterLink (
  member_id INT NOT NULL,
  semeseter_id INT NOT NULL,
  PRIMARY KEY (member_id, semeseter_id)
);
