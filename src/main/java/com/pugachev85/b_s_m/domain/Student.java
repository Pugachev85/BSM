package com.pugachev85.b_s_m.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Student.
 */
@Entity
@Table(name = "student")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "alphabet_book_number", nullable = false, unique = true)
    private Long alphabetBookNumber;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "studyPlace", "educationalProgram" }, allowSetters = true)
    private Group group;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_student__order",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "students" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "students")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "academicSubject", "students" }, allowSetters = true)
    private Set<PersonalGrade> personalGrades = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Student id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlphabetBookNumber() {
        return this.alphabetBookNumber;
    }

    public Student alphabetBookNumber(Long alphabetBookNumber) {
        this.setAlphabetBookNumber(alphabetBookNumber);
        return this;
    }

    public void setAlphabetBookNumber(Long alphabetBookNumber) {
        this.alphabetBookNumber = alphabetBookNumber;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Student firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return this.secondName;
    }

    public Student secondName(String secondName) {
        this.setSecondName(secondName);
        return this;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Student lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Student birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Student group(Group group) {
        this.setGroup(group);
        return this;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Student orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public Student addOrder(Order order) {
        this.orders.add(order);
        return this;
    }

    public Student removeOrder(Order order) {
        this.orders.remove(order);
        return this;
    }

    public Set<PersonalGrade> getPersonalGrades() {
        return this.personalGrades;
    }

    public void setPersonalGrades(Set<PersonalGrade> personalGrades) {
        if (this.personalGrades != null) {
            this.personalGrades.forEach(i -> i.removeStudent(this));
        }
        if (personalGrades != null) {
            personalGrades.forEach(i -> i.addStudent(this));
        }
        this.personalGrades = personalGrades;
    }

    public Student personalGrades(Set<PersonalGrade> personalGrades) {
        this.setPersonalGrades(personalGrades);
        return this;
    }

    public Student addPersonalGrade(PersonalGrade personalGrade) {
        this.personalGrades.add(personalGrade);
        personalGrade.getStudents().add(this);
        return this;
    }

    public Student removePersonalGrade(PersonalGrade personalGrade) {
        this.personalGrades.remove(personalGrade);
        personalGrade.getStudents().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        return getId() != null && getId().equals(((Student) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Student{" +
            "id=" + getId() +
            ", alphabetBookNumber=" + getAlphabetBookNumber() +
            ", firstName='" + getFirstName() + "'" +
            ", secondName='" + getSecondName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            "}";
    }
}
