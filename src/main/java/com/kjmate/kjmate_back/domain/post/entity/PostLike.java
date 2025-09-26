package com.kjmate.kjmate_back.domain.post.entity;


import com.kjmate.kjmate_back.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post_likes")
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pl_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "me_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "po_id")
    private Post post;
}
