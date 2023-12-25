package com.pugachev85.b_s_m.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PersonalGrade.
 */
@Entity
@Table(name = "personal_grade")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonalGrade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "grade", nullable = false)
    private Integer grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "educationalPrograms", "employees" }, allowSetters = true)
    private AcademicSubject academicSubject;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_personal_grade__student",
        joinColumns = @JoinColumn(name = "personal_grade_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "group", "orders", "personalGrades" }, allowSetters = true)
    private Set<Student> students = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PersonalGrade id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGrade() {
        return this.grade;
    }

    public PersonalGrade grade(Integer grade) {
        this.setGrade(grade);
        return this;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public AcademicSubject getAcademicSubject() {
        return this.academicSubject;
    }

    public void setAcademicSubject(AcademicSubject academicSubject) {
        this.academicSubject = academicSubject;
    }

    public PersonalGrade academicSubject(AcademicSubject academicSubject) {
        this.setAcademicSubject(academicSubject);
        return this;
    }

    public Set<Student> getStudents() {
        return this.students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public PersonalGrade students(Set<Student> students) {
        this.setStudents(students);
        return this;
    }

    public PersonalGrade addStudent(Student student) {
        this.students.add(student);
        return this;
    }

    public PersonalGrade removeStudent(Student student) {
        this.students.remove(student);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonalGrade)) {
            return false;
        }
        return getId() != null && getId().equals(((PersonalGrade) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonalGrade{" +
            "id=" + getId() +
            ", grade=" + getGrade() +
            "}";
    }
}
