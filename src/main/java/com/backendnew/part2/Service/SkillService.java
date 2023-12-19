package com.backendnew.part2.Service;

import com.backendnew.part2.Entity.Rating;
import com.backendnew.part2.Entity.Skill;
import com.backendnew.part2.Repository.RatingRepository;
import com.backendnew.part2.Repository.SkillRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private RatingRepository ratingRepository;



    public Skill saveSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public Skill getSkillById(Long id) {
        return skillRepository.findById(id).orElse(null);
    }

    public void updateSkillRating(Long skillId, int ratingValue) {
        // Retrieve the skill by ID
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new EntityNotFoundException("Skill not found with id: " + skillId));

        // Update the skill rating
        Rating rating = skill.getRating();
        if (rating == null) {
            rating = new Rating();
            rating.setSkill(skill);
        }
        rating.setValue(ratingValue);

        // Save the skill first to ensure it's persisted
        skillRepository.save(skill);

        // Save the rating
        ratingRepository.save(rating);
    }

    public void updateSkills(List<Skill> skills) {
        for (Skill skill : skills) {
            Long skillId = skill.getId();
            if (skillId != null && skillRepository.existsById(skillId)) {
                // Skill with the provided ID exists, update it
               updateSkillRating(skillId, skill.getRating().getValue());
            } else {
                // Skill with the provided ID doesn't exist, add it

            }
        }
    }

    public void deleteSkill(Long id) {
        skillRepository.deleteById(id);
    }
}
