package com.example.explorecalijpa.repo;

import com.example.explorecalijpa.model.TourPackage;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

/*
 * Challenge: Change url keyword to "packages"
 */
@Tag(name = "Tour Package", description = "The Tour Package API")
public interface TourPackageRepository extends JpaRepository<TourPackage, String> {

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @Override
  java.util.List<TourPackage> findAll();

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @Override
  Optional<TourPackage> findById(String id);

  @PreAuthorize("hasRole('ADMIN')")
  @Override
  <S extends TourPackage> S save(S entity);

  @PreAuthorize("hasRole('ADMIN')")
  @Override
  void deleteById(String id);

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  Optional<TourPackage> findByName(String name);
}
