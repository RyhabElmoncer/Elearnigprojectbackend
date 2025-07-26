package com.org.ElearnigProject.repository;

import com.org.ElearnigProject.Model.Category;
import com.org.ElearnigProject.Model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {
    Page<Video> findByIsVisible(boolean isVisible, Pageable pageable);
    
    Page<Video> findByCategoryAndIsVisible(Category category, boolean isVisible, Pageable pageable);
    
    @Query("SELECT v FROM Video v WHERE v.isVisible = true AND " +
           "(LOWER(v.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(v.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Video> searchVideos(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT v FROM Video v WHERE v.isVisible = true AND v.isPaid = :isPaid")
    Page<Video> findByIsPaidAndVisible(@Param("isPaid") boolean isPaid, Pageable pageable);
    
    @Query("SELECT v FROM Video v WHERE v.category = :category AND v.isVisible = true AND v.isPaid = :isPaid")
    Page<Video> findByCategoryAndIsPaidAndVisible(
            @Param("category") Category category, 
            @Param("isPaid") boolean isPaid, 
            Pageable pageable);
}