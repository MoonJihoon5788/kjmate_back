package com.kjmate.kjmate_back.domain.post.repository;

import com.kjmate.kjmate_back.domain.post.entity.PostPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostPhotoRepository extends JpaRepository<PostPhoto, Long> {
}
