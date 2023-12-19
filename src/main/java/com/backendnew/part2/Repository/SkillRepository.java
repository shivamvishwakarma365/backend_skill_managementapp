package com.backendnew.part2.Repository;

import com.backendnew.part2.Entity.Employee;
import com.backendnew.part2.Entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    Skill findByEmployeeAndSkillName(Employee employee, String skillName);
}
