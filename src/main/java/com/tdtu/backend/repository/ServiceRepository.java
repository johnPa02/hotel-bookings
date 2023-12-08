package com.tdtu.backend.repository;

import com.tdtu.backend.model.ServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceModel,Long> {
    Optional<ServiceModel> findById(Long id);
    Optional<ServiceModel> findByName(String name);
}
