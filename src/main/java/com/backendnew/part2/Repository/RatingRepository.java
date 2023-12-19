package com.backendnew.part2.Repository;

import com.backendnew.part2.Entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Rating findBySkillIdAndValue(Long id, int value);
}
