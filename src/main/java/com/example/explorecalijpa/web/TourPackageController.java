package com.example.explorecalijpa.web;

import com.example.explorecalijpa.model.TourPackage;
import com.example.explorecalijpa.repo.TourPackageRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/packages")
@Tag(name = "Tour Package", description = "The Tour Package API")
public class TourPackageController {

  @Autowired
  private TourPackageRepository tourPackageRepository;

  @GetMapping
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @Operation(summary = "Get all tour packages")
  public ResponseEntity<Page<TourPackage>> getAllPackages(Pageable pageable) {
    Page<TourPackage> packages = tourPackageRepository.findAll(pageable);
    return ResponseEntity.ok(packages);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @Operation(summary = "Get tour package by ID")
  public ResponseEntity<TourPackage> getPackageById(@PathVariable String id) {
    Optional<TourPackage> tourPackage = tourPackageRepository.findById(id);
    return tourPackage.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Create a new tour package")
  public ResponseEntity<TourPackage> createPackage(@RequestBody TourPackage tourPackage) {
    TourPackage savedPackage = tourPackageRepository.save(tourPackage);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedPackage);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Update tour package")
  public ResponseEntity<TourPackage> updatePackage(@PathVariable String id, @RequestBody TourPackage tourPackage) {
    if (!tourPackageRepository.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    // Create a new TourPackage with the correct ID
    TourPackage updatedPackage = new TourPackage(id, tourPackage.getName());
    TourPackage savedPackage = tourPackageRepository.save(updatedPackage);
    return ResponseEntity.ok(savedPackage);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Delete tour package")
  public ResponseEntity<Void> deletePackage(@PathVariable String id) {
    if (!tourPackageRepository.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    tourPackageRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
