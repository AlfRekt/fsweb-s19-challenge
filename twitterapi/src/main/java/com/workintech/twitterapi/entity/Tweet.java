package com.workintech.twitterapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tweets",schema = "twitter")
public class Tweet extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tweet")
    private List<Like> likes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tweet")
    private List<Comment> comments = new ArrayList<>();
}
