package org.dhimate.mule.exchange;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnypointExchangeRepository extends JpaRepository<AnypointExchangeEntity, Long> {

	@Query(value="SELECT new org.dhimate.mule.exchange.AnypointExchangeAssetsByUser(e.createdByName, count(e)) from AnypointExchangeEntity e group by e.createdByName")
	List<AnypointExchangeAssetsByUser>calculateAssetsByUser();
}

