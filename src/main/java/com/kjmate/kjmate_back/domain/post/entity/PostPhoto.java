package com.kjmate.kjmate_back.domain.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "post_photos")
public class PostPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pp_id")
    private Long id;

    @Column(name = "post_image_url" , nullable = false)
    private String postImageUrl;

    @ManyToOne
    @JoinColumn(name = "po_id")
    private Post post;
}
