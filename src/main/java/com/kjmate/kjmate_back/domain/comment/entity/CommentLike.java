package com.kjmate.kjmate_back.domain.comment.entity;

import com.kjmate.kjmate_back.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "comment_likes")
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cl_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "me_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "co_id")
    private Comment comment;
}
