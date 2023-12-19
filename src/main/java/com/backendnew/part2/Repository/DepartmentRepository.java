package com.backendnew.part2.Repository;

import com.backendnew.part2.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByDepartmentName(String departmentName);
    // You can add custom query methods if needed
}
