package com.KLTN.nguyen.hotelbooking.repository;

import com.KLTN.nguyen.hotelbooking.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Integer> {
}
