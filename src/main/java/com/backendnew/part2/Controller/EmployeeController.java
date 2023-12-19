package com.backendnew.part2.Controller;

import com.backendnew.part2.Entity.Employee;
import com.backendnew.part2.Entity.Skill;
import com.backendnew.part2.Repository.EmployeeRepository;
import com.backendnew.part2.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@CrossOrigin
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    //    @GetMapping
//    public List<Employee> getAllEmployees() {
//        return employeeService.getAllEmployees();
//    }
    @GetMapping("/all")
    public List<Map<String, Object>> getAllEmployeesDetails() {
        List<Map<String, Object>> allEmployeesDetails = new ArrayList<>();

        List<Employee> allEmployees = employeeService.getAllEmployees();
        for (Employee employee : allEmployees) {
            Map<String, Object> employeeDetails = employeeService.getEmployeeById(employee.getId());
            if (employeeDetails != null) {
                allEmployeesDetails.add(employeeDetails);
            }
        }

        return allEmployeesDetails;
    }

    @GetMapping("/under-manager/{managerEmail}")
    public List<Map<String, Object>> getEmployeesUnderManager(@PathVariable String managerEmail) {
        return employeeService.getEmployeesByManagerEmail(managerEmail);
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        Map<String, Object> employeeResponse = employeeService.getEmployeeById(id);

        if (employeeResponse != null) {
            return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> addEmployee(@RequestBody Employee employee) {
        try {
            employeeService.addEmployee(employee);
            return new ResponseEntity<>("Employee registered successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        ResponseEntity<String> response = employeeService.updateEmployee(id, employee);
        return response;
    }


    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

    @PostMapping("/sorted-by-skills")
    public List<Map<String, Object>> getEmployeesSortedBySkills(@RequestBody Map<String, Integer> skillRatings) {

        List<Map<String, Object>> sortedEmployees = new ArrayList<>();

        List<Employee> allEmployees = employeeService.getAllEmployees();
        for (Employee employee : allEmployees) {
            boolean meetsCriteria = true;

            for (Map.Entry<String, Integer> entry : skillRatings.entrySet()) {
                String skillName = entry.getKey();
                int minRating = entry.getValue();

                boolean hasSkill = false;
                for (Skill skill : employee.getSkills()) {
                    if (skill.getSkillName().equalsIgnoreCase(skillName) && skill.getRating().getValue() >= minRating) {
                        hasSkill = true;
                        break;
                    }
                }

                if (!hasSkill) {
                    meetsCriteria = false;
                    break;
                }
            }

            if (meetsCriteria) {
                Map<String, Object> employeeDetails = employeeService.getEmployeeById(employee.getId());
                if (employeeDetails != null) {
                    sortedEmployees.add(employeeDetails);
                }
            }
        }


        return sortedEmployees;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginDetails) {
        String email = loginDetails.get("email");
        String password = loginDetails.get("password");

        // Validate the email and password
        if (email != null && password != null) {
            return employeeService.login(email, password);
        } else {
            // Invalid request, return bad request response
            return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);
        }
    }


}
