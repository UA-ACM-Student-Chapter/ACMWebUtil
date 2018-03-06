DROP TABLE Semester;

CREATE SEQUENCE semester_id_sequence;

CREATE TABLE Semester (
  semester_id INT NOT NULL DEFAULT nextval('semester_id_sequence') PRIMARY KEY,
  start_date TIMESTAMP NOT NULL,
  end_date TIMESTAMP NOT NULL,
  due_date TIMESTAMP NOT NULL
);
