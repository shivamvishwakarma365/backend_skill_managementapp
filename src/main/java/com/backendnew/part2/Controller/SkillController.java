package com.backendnew.part2.Controller;

import com.backendnew.part2.Entity.Skill;
import com.backendnew.part2.Service.SkillService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;



@CrossOrigin
@RestController
@RequestMapping("/api/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @PostMapping
    public Skill saveSkill(@RequestBody Skill skill) {
        return skillService.saveSkill(skill);
    }

    @GetMapping
    public List<Skill> getAllSkills() {
        return skillService.getAllSkills();
    }

    @GetMapping("/{id}")
    public Skill getSkillById(@PathVariable Long id) {
        return skillService.getSkillById(id);
    }

    @PutMapping("/{skillId}/rating")
    public ResponseEntity<String> updateSkillRating(@PathVariable Long skillId, @RequestBody Map<String, Integer> requestBody) {
        try {
            int ratingValue = requestBody.get("ratingValue");
            skillService.updateSkillRating(skillId, ratingValue);
            return ResponseEntity.ok("Skill rating updated successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Skill not found with id: " + skillId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating skill rating");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateSkills(@RequestBody List<Skill> skills) {
        try {
            skillService.updateSkills(skills);
            return ResponseEntity.ok("Skills updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating skills");
        }
    }



    @DeleteMapping("/{id}")
    public void deleteSkill(@PathVariable Long id) {
        skillService.deleteSkill(id);
    }
}
