package com.tdtu.backend.controller;

import com.tdtu.backend.model.ServiceModel;
import com.tdtu.backend.service.FileStorageService;
import com.tdtu.backend.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
public class ServiceController {

    @Autowired
    private ServiceService serviceService;
    @Autowired
    private FileStorageService fileStorageService;


    @GetMapping("/services")
    public String showServices(Model model) {
        List<ServiceModel> serviceModelList = serviceService.findAll();
        model.addAttribute("servicesList", serviceModelList);
        return "service";
    }


    @GetMapping("/admin/services/add")
    public String addServiceForm(Model model) {
        return "add-service";
    }

    @PostMapping("/admin/services/add")
    public String addService(@RequestParam("name") String name,
                             @RequestParam("price") double price,
                             @RequestParam("description") String description,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             Model model) {
        String imagePath = fileStorageService.saveImage(imageFile);
        ServiceModel newServiceModel = new ServiceModel();
        newServiceModel.setName(name);
        newServiceModel.setPrice(price);
        newServiceModel.setDescription(description);

        newServiceModel.setImagePath(imagePath);

        serviceService.save(newServiceModel);

        return "redirect:/admin/services";
    }

    @GetMapping("/admin/services/{id}/delete")
    public String deleteService(@PathVariable Long id) {
        serviceService.deleteById(id);
        return "redirect:/admin/services";
    }

    @GetMapping("/admin/services/{id}/edit")
    public String editServiceForm(@PathVariable("id") Long id, Model model) {

        Optional<ServiceModel> service = serviceService.findById(id);
        service.ifPresent(services -> model.addAttribute("service", services));
        return "edit-service";
    }

    @PostMapping("/admin/services/{id}/edit")
    public String editService(@PathVariable("id") Long id,
                              @RequestParam("name") String name,
                              @RequestParam("price") double price,
                              @RequestParam("description") String description,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              Model model) {
        Optional<ServiceModel> serviceOptional = serviceService.findById(id);
        ServiceModel existingServiceModel = serviceOptional.get();
        existingServiceModel.setName(name);
        existingServiceModel.setPrice(price);
        existingServiceModel.setDescription(description);
        if (!imageFile.isEmpty()) {
            String imagePath = fileStorageService.saveImage(imageFile);
            existingServiceModel.setImagePath(imagePath);
        }

            serviceService.save(existingServiceModel);


        return "redirect:/admin/services";
    }
    @GetMapping("/services/details/{id}")
    public String viewServiceDetails(@PathVariable("id") Long id, Model model) {
        Optional<ServiceModel> serviceOptional = serviceService.findById(id);
        if (serviceOptional.isPresent()) {
            model.addAttribute("service", serviceOptional.get());
            return "service-details";
        } else {
            return "service-not-found";
        }
    }


}