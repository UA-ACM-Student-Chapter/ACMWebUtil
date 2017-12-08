DROP TABLE MemberSemesterLink;


CREATE TABLE MemberSemesterLink (
  member_id INT NOT NULL,
  semester_id INT NOT NULL,
  PRIMARY KEY (member_id, semester_id)
);
