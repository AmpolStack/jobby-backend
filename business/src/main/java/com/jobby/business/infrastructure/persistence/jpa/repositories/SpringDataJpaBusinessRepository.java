package com.jobby.business.infrastructure.persistence.jpa.repositories;

import com.jobby.business.infrastructure.persistence.jpa.entities.JpaBusinessEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataJpaBusinessRepository extends JpaRepository<JpaBusinessEntity, Integer> {
    @EntityGraph(attributePaths = {
            "address",
            "address.city",
            "address.city.country"
    })
    Optional<JpaBusinessEntity> findById(int id);

    boolean existsByName(String name);

    void deleteById(int id);

    @Modifying
    @Query("UPDATE business b SET b.name = :name WHERE b.id = :id")
    void updateName(@Param("id") int id, @Param("name") String name);
    
    @Modifying
    @Query("UPDATE business b SET b.description = :description WHERE b.id = :id")
    void updateDescription(@Param("id") int id, @Param("description") String description);
    
    @Modifying
    @Query("UPDATE business b SET b.bannerImageUrl = :bannerImageUrl, b.profileImageUrl = :profileImageUrl WHERE b.id = :id")
    void updatePictures(@Param("id") int id, @Param("bannerImageUrl") String bannerImageUrl, @Param("profileImageUrl") String profileImageUrl);
    
    @Modifying
    @Query("UPDATE business b SET b.name = :name, b.description = :description WHERE b.id = :id")
    void updateNameAndDescription(@Param("id") int id, @Param("name") String name, @Param("description") String description);
}
