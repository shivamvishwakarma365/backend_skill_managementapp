package com.backendnew.part2.Repository;

import com.backendnew.part2.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.manager WHERE e.id = :employeeId")
    Optional<Employee> findByIdWithManager(@Param("employeeId") Long employeeId);

    Optional<Employee> findByEmail(String managerEmail);

    List<Employee> findByManager(Employee employee);
}
