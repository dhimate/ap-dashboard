package org.dhimate.mule.apimanager;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnypointAPIManagerRepository extends JpaRepository<AnypointAPIManagerEntity, Long>{

    List<AnypointAPIManagerEntity> findByApiId(Long apiId);

}
