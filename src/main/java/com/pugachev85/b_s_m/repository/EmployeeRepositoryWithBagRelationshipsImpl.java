package com.pugachev85.b_s_m.repository;

import com.pugachev85.b_s_m.domain.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class EmployeeRepositoryWithBagRelationshipsImpl implements EmployeeRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Employee> fetchBagRelationships(Optional<Employee> employee) {
        return employee.map(this::fetchAcademicSubjects);
    }

    @Override
    public Page<Employee> fetchBagRelationships(Page<Employee> employees) {
        return new PageImpl<>(fetchBagRelationships(employees.getContent()), employees.getPageable(), employees.getTotalElements());
    }

    @Override
    public List<Employee> fetchBagRelationships(List<Employee> employees) {
        return Optional.of(employees).map(this::fetchAcademicSubjects).orElse(Collections.emptyList());
    }

    Employee fetchAcademicSubjects(Employee result) {
        return entityManager
            .createQuery(
                "select employee from Employee employee left join fetch employee.academicSubjects where employee.id = :id",
                Employee.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Employee> fetchAcademicSubjects(List<Employee> employees) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, employees.size()).forEach(index -> order.put(employees.get(index).getId(), index));
        List<Employee> result = entityManager
            .createQuery(
                "select employee from Employee employee left join fetch employee.academicSubjects where employee in :employees",
                Employee.class
            )
            .setParameter("employees", employees)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
