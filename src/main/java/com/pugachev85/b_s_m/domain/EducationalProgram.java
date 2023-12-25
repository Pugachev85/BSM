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
 * A EducationalProgram.
 */
@Entity
@Table(name = "educational_program")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EducationalProgram implements Serializable {

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
    @DecimalMin(value = "0.5")
    @DecimalMax(value = "60.0")
    @Column(name = "month_length", nullable = false)
    private Double monthLength;

    @ManyToOne(fetch = FetchType.LAZY)
    private AcademicYear academicYear;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_educational_program__academic_subject",
        joinColumns = @JoinColumn(name = "educational_program_id"),
        inverseJoinColumns = @JoinColumn(name = "academic_subject_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "educationalPrograms", "employees" }, allowSetters = true)
    private Set<AcademicSubject> academicSubjects = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EducationalProgram id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public EducationalProgram title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getMonthLength() {
        return this.monthLength;
    }

    public EducationalProgram monthLength(Double monthLength) {
        this.setMonthLength(monthLength);
        return this;
    }

    public void setMonthLength(Double monthLength) {
        this.monthLength = monthLength;
    }

    public AcademicYear getAcademicYear() {
        return this.academicYear;
    }

    public void setAcademicYear(AcademicYear academicYear) {
        this.academicYear = academicYear;
    }

    public EducationalProgram academicYear(AcademicYear academicYear) {
        this.setAcademicYear(academicYear);
        return this;
    }

    public Set<AcademicSubject> getAcademicSubjects() {
        return this.academicSubjects;
    }

    public void setAcademicSubjects(Set<AcademicSubject> academicSubjects) {
        this.academicSubjects = academicSubjects;
    }

    public EducationalProgram academicSubjects(Set<AcademicSubject> academicSubjects) {
        this.setAcademicSubjects(academicSubjects);
        return this;
    }

    public EducationalProgram addAcademicSubject(AcademicSubject academicSubject) {
        this.academicSubjects.add(academicSubject);
        return this;
    }

    public EducationalProgram removeAcademicSubject(AcademicSubject academicSubject) {
        this.academicSubjects.remove(academicSubject);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EducationalProgram)) {
            return false;
        }
        return getId() != null && getId().equals(((EducationalProgram) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EducationalProgram{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", monthLength=" + getMonthLength() +
            "}";
    }
}
