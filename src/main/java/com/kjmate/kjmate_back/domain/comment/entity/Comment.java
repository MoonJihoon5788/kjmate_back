package com.kjmate.kjmate_back.domain.comment.entity;

import com.kjmate.kjmate_back.domain.member.entity.Member;
import com.kjmate.kjmate_back.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cm_id")
    private Long id;

    @Column(name = "content", nullable=false, length=2048)
    private String content;

    @Column(name = "created_at", nullable=false)
    private LocalDateTime createdAt;

    @Column(name = "parent_comment")
    private Long parentComment;

    @Column(name = "likes_count", nullable = false)
    private int likesCount;

    @ManyToOne
    @JoinColumn(name = "me_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "po_id")
    private Post post;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CommentLike> commentLikes = new ArrayList<>();
}
