package com.backendnew.part2.Service;

import com.backendnew.part2.Entity.Department;
import com.backendnew.part2.Entity.Employee;
import com.backendnew.part2.Entity.Rating;
import com.backendnew.part2.Entity.Skill;
import com.backendnew.part2.Repository.DepartmentRepository;
import com.backendnew.part2.Repository.EmployeeRepository;
import com.backendnew.part2.Repository.RatingRepository;
import com.backendnew.part2.Repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private SkillService skillService;
    @Autowired
    private SkillRepository skillRepository;


    @Autowired
    private RatingRepository ratingRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Map<String, Object> getEmployeeById(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();

            Map<String, Object> response = new HashMap<>();
            response.put("id", employee.getId());
            response.put("name", employee.getName());
            response.put("email", employee.getEmail());

            if (employee.getDepartment() != null) {
                response.put("department", employee.getDepartment().getDepartmentName());
            }

            // Include manager's name if manager is present
            if (employee.getManager() != null) {
                Map<String, Object> managerMap = new HashMap<>();
                managerMap.put("id", employee.getManager().getId());
                managerMap.put("name", employee.getManager().getName());
                managerMap.put("email", employee.getManager().getEmail());
                response.put("manager", managerMap);
            }

            // Include skills with ratings
            List<Map<String, Object>> skills = new ArrayList<>();
            for (Skill skill : employee.getSkills()) {
                Map<String, Object> skillMap = new HashMap<>();
                skillMap.put("id", skill.getId());
                skillMap.put("skillName", skill.getSkillName());

                // Include rating information
                if (skill.getRating() != null) {
                    skillMap.put("rating", skill.getRating().getValue());
                }

                skills.add(skillMap);
            }
            response.put("skills", skills);

            return response;
        }

        return null;
    }

    public List<Map<String, Object>> getEmployeesByManagerEmail(String managerEmail) {
        List<Map<String, Object>> employeesUnderManager = new ArrayList<>();

        // Fetch the manager by email
        Optional<Employee> manager = employeeRepository.findByEmail(managerEmail);

        if (manager.isPresent()) {
            List<Employee> employees = employeeRepository.findByManager(manager.get());

            for (Employee employee : employees) {
                Map<String, Object> employeeDetails = getEmployeeById(employee.getId());
                if (employeeDetails != null) {
                    employeesUnderManager.add(employeeDetails);
                }
            }
        }

        return employeesUnderManager;
    }

    public Employee addEmployee(Employee employee) {
        // Check if the manager ID is provided
        if (employee.getManager() != null && employee.getManager().getId() != null) {
            Long managerId = employee.getManager().getId();

            // Check if the manager ID exists in the database
            Optional<Employee> manager = employeeRepository.findById(managerId);

            if (manager.isPresent()) {
                // Set the manager for the new employeen
                employee.setManager(manager.get());
            } else {
                // Handle the case where the manager ID doesn't exist
                throw new IllegalArgumentException("Manager with ID " + managerId + " not found");
            }
        }

        // Check if the department is provided
        if (employee.getDepartment() != null) {
            Department department = employee.getDepartment();

            // Check if the department name exists in the database
            Optional<Department> existingDepartment = departmentRepository.findByDepartmentName(department.getDepartmentName());

            if (existingDepartment.isPresent()) {
                // Set the existing department for the new employee
                employee.setDepartment(existingDepartment.get());
            } else {
                // If the department name is not found, save the new department before proceeding
                Department newDepartment = departmentRepository.save(department);
                employee.setDepartment(newDepartment);
            }
        }

        // Save the employee
        return employeeRepository.save(employee);
    }



    public ResponseEntity<String> updateEmployee(Long id, Employee updatedEmployee) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        if (optionalEmployee.isPresent()) {
            Employee existingEmployee = optionalEmployee.get();

            // Update name and email
            if (updatedEmployee.getName() != null) {
                existingEmployee.setName(updatedEmployee.getName());
            }
            if (updatedEmployee.getEmail() != null) {
                existingEmployee.setEmail(updatedEmployee.getEmail());
            }

            // Update manager if provided
            if (updatedEmployee.getManager() != null && updatedEmployee.getManager().getEmail() != null) {
                Optional<Employee> managerOptional = employeeRepository.findByEmail(updatedEmployee.getManager().getEmail());

                if (managerOptional.isPresent()) {
                    existingEmployee.setManager(managerOptional.get());
                } else {
                    // Handle manager not found
                }
            }

            // Check if the updated employee has a department
            if (updatedEmployee.getDepartment() != null) {
                Department updatedDepartment = updatedEmployee.getDepartment();
                Department existingDepartment;

                Optional<Department> departmentOptional = departmentRepository.findByDepartmentName(updatedDepartment.getDepartmentName());

                if (departmentOptional.isPresent()) {
                    existingDepartment = departmentOptional.get();
                } else {
                    // If the department doesn't exist, create a new department
                    Department newDepartment = new Department();
                    newDepartment.setDepartmentName(updatedDepartment.getDepartmentName());
                    existingDepartment = departmentRepository.save(newDepartment);
                }

                existingEmployee.setDepartment(existingDepartment);
            }

            // Save the employee to get the ID
            existingEmployee = employeeRepository.save(existingEmployee);

            // Update or add skills if provided
            // Update or add skills if provided
            if (updatedEmployee.getSkills() != null) {
                for (Skill updatedSkill : updatedEmployee.getSkills()) {
                    if (updatedSkill.getId() != null) {
                        // Update existing skill
                        Skill existingSkill = skillRepository.findById(updatedSkill.getId()).orElse(null);

                        if (existingSkill != null) {
                            existingSkill.setSkillName(updatedSkill.getSkillName());

                            if (updatedSkill.getRating() != null) {
                                Rating existingRating = existingSkill.getRating();
                                if (existingRating != null) {
                                    existingRating.setValue(updatedSkill.getRating().getValue());
                                } else {
                                    Rating newRating = new Rating(updatedSkill.getRating().getValue());
                                    newRating.setSkill(existingSkill);
                                    existingSkill.setRating(ratingRepository.save(newRating));
                                }
                            }

                            skillRepository.save(existingSkill);
                        }
                    } else {
                        // Add new skill
                        updatedSkill.setEmployee(existingEmployee);

                        // Check if a rating with the same skill ID and value already exists
                        Rating existingRating = ratingRepository.findBySkillIdAndValue(updatedSkill.getId(), updatedSkill.getRating().getValue());

                        if (existingRating != null) {
                            // Use the existing rating
                            updatedSkill.setRating(existingRating);
                        } else {
                            // Save the Skill entity first
                            Skill savedSkill = skillRepository.save(updatedSkill);

                            // Set the saved Skill in the Rating entity
                            Rating newRating = new Rating(updatedSkill.getRating().getValue());
                            newRating.setSkill(savedSkill);

                            // Save the Rating entity
                            ratingRepository.save(newRating);
                        }
                    }
                }
            }


            return new ResponseEntity<>("Employee updated successfully", HttpStatus.OK);
        }

        return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
    }





    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public ResponseEntity<?> login(String email, String password) {
        // Find the employee by email
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(email);

        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();

            // Check if the provided password matches (using equalsIgnoreCase for case-insensitive comparison)
            if (employee.getPassword().equals(password)) {
                // Password matches, return employee details
                Map<String, Object> employeeDetails = getEmployeeById(employee.getId());
                return new ResponseEntity<>(employeeDetails, HttpStatus.OK);
            } else {
                // Password does not match, return unauthorized response
                return new ResponseEntity<>("Invalid password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            // Employee not found, return not found response
            return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
        }
    }



}
