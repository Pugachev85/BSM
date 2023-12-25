package com.pugachev85.b_s_m.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Group.
 */
@Entity
@Table(name = "jhi_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "acceptance_date")
    private LocalDate acceptanceDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private StudyPlace studyPlace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "academicYear", "academicSubjects" }, allowSetters = true)
    private EducationalProgram educationalProgram;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Group id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Group title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getAcceptanceDate() {
        return this.acceptanceDate;
    }

    public Group acceptanceDate(LocalDate acceptanceDate) {
        this.setAcceptanceDate(acceptanceDate);
        return this;
    }

    public void setAcceptanceDate(LocalDate acceptanceDate) {
        this.acceptanceDate = acceptanceDate;
    }

    public StudyPlace getStudyPlace() {
        return this.studyPlace;
    }

    public void setStudyPlace(StudyPlace studyPlace) {
        this.studyPlace = studyPlace;
    }

    public Group studyPlace(StudyPlace studyPlace) {
        this.setStudyPlace(studyPlace);
        return this;
    }

    public EducationalProgram getEducationalProgram() {
        return this.educationalProgram;
    }

    public void setEducationalProgram(EducationalProgram educationalProgram) {
        this.educationalProgram = educationalProgram;
    }

    public Group educationalProgram(EducationalProgram educationalProgram) {
        this.setEducationalProgram(educationalProgram);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Group)) {
            return false;
        }
        return getId() != null && getId().equals(((Group) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Group{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", acceptanceDate='" + getAcceptanceDate() + "'" +
            "}";
    }
}
