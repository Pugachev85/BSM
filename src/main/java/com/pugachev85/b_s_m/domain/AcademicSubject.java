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
 * A AcademicSubject.
 */
@Entity
@Table(name = "academic_subject")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AcademicSubject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "hours", nullable = false)
    private Integer hours;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "academicSubjects")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "academicYear", "academicSubjects" }, allowSetters = true)
    private Set<EducationalProgram> educationalPrograms = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "academicSubjects")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "academicSubjects" }, allowSetters = true)
    private Set<Employee> employees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AcademicSubject id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public AcademicSubject title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getHours() {
        return this.hours;
    }

    public AcademicSubject hours(Integer hours) {
        this.setHours(hours);
        return this;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Set<EducationalProgram> getEducationalPrograms() {
        return this.educationalPrograms;
    }

    public void setEducationalPrograms(Set<EducationalProgram> educationalPrograms) {
        if (this.educationalPrograms != null) {
            this.educationalPrograms.forEach(i -> i.removeAcademicSubject(this));
        }
        if (educationalPrograms != null) {
            educationalPrograms.forEach(i -> i.addAcademicSubject(this));
        }
        this.educationalPrograms = educationalPrograms;
    }

    public AcademicSubject educationalPrograms(Set<EducationalProgram> educationalPrograms) {
        this.setEducationalPrograms(educationalPrograms);
        return this;
    }

    public AcademicSubject addEducationalProgram(EducationalProgram educationalProgram) {
        this.educationalPrograms.add(educationalProgram);
        educationalProgram.getAcademicSubjects().add(this);
        return this;
    }

    public AcademicSubject removeEducationalProgram(EducationalProgram educationalProgram) {
        this.educationalPrograms.remove(educationalProgram);
        educationalProgram.getAcademicSubjects().remove(this);
        return this;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.removeAcademicSubject(this));
        }
        if (employees != null) {
            employees.forEach(i -> i.addAcademicSubject(this));
        }
        this.employees = employees;
    }

    public AcademicSubject employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public AcademicSubject addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.getAcademicSubjects().add(this);
        return this;
    }

    public AcademicSubject removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.getAcademicSubjects().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AcademicSubject)) {
            return false;
        }
        return getId() != null && getId().equals(((AcademicSubject) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AcademicSubject{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", hours=" + getHours() +
            "}";
    }
}
