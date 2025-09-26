package com.kjmate.kjmate_back.domain.post.entity;

import com.kjmate.kjmate_back.domain.comment.entity.Comment;
import com.kjmate.kjmate_back.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "po_id")
    private Long id;

    @Column(name="title", nullable = false, length = 20)
    private String title;

    @Column(name = "content", nullable = false , length = 2048)
    private String content;

    @Column(name = "views", nullable = false)
    private int views;

    @Column(name = "likes_count", nullable = false)
    private int likesCount;

    @Column(name = "category" , nullable = false, length = 5)
    private String category;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<PostPhoto> postPhotos = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<PostLike> postLikes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "me_id")
    private Member member;
}
