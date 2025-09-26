package com.kjmate.kjmate_back.domain.member.entity;

import com.kjmate.kjmate_back.domain.comment.entity.Comment;
import com.kjmate.kjmate_back.domain.comment.entity.CommentLike;
import com.kjmate.kjmate_back.domain.post.entity.Post;
import com.kjmate.kjmate_back.domain.post.entity.PostLike;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "me_id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name="password", nullable = false, length = 100)
    private String password;

    @Column(name = "name", nullable = false, length = 15)
    private String name;

    @Column(name = "nickname", nullable = false,unique = true, length = 15)
    private String nickname;

    @Column(name="nationality", nullable = false, length = 1)
    private Character nationality;

    @Column(name="profile_image_url" , nullable = false)
    private String profileImageUrl;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "member" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<CommentLike> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<PostLike> postLikes = new ArrayList<>();
}
