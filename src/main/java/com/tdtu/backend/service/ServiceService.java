package com.tdtu.backend.service;

import com.tdtu.backend.model.ServiceModel;
import com.tdtu.backend.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;

    public ServiceModel save(ServiceModel serviceModel) {
        return serviceRepository.save(serviceModel);
    }

    public void delete(ServiceModel serviceModel) {
        serviceRepository.delete(serviceModel);
    }
    public void deleteById(Long id){serviceRepository.deleteById(id);}

    public Optional<ServiceModel> findById(Long id) {
        return serviceRepository.findById(id);
    }

    public List<ServiceModel> findAll() {
        return serviceRepository.findAll();
    }

    public ServiceModel updateService(Long id, ServiceModel updatedServiceModel) {
        Optional<ServiceModel> existingService = serviceRepository.findById(id);
        if (existingService.isPresent()) {
            ServiceModel serviceModelToUpdate = existingService.get();
            serviceModelToUpdate.setName(updatedServiceModel.getName());
            serviceModelToUpdate.setDescription(updatedServiceModel.getDescription());
            serviceModelToUpdate.setPrice(updatedServiceModel.getPrice());

            return serviceRepository.save(serviceModelToUpdate);
        } else {
            return null;
        }
    }
    public Optional<ServiceModel> findByName(String name) {
        return serviceRepository.findByName(name);
    }
}
