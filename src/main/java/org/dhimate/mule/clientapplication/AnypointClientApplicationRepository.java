package org.dhimate.mule.clientapplication;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnypointClientApplicationRepository extends JpaRepository<AnypointClientApplicationEntity, Long> {


    List<AnypointClientApplicationEntity> findByClientApplicationId(String clientApplicationId);
}
