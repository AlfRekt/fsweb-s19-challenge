package com.workintech.twitterapi.entity;

import com.workintech.twitterapi.service.TweetService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "likes",schema = "twitter",uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "tweet_id"})})
public class Like extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "tweet_id",nullable = false)
    private Tweet tweet;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}
