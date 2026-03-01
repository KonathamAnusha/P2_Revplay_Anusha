package com.rev.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "artist_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistProfile {

 @Id
 @SequenceGenerator(
         name = "artist_seq",
         sequenceName = "ARTIST_SEQ",
         allocationSize = 1
 )
 @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "artist_seq")
 @Column(name = "artist_id", updatable = false, nullable = false)
 private Long artistId;

 @OneToOne
 @JoinColumn(name = "user_id", nullable = false, unique = true)
 @JsonBackReference
 private UserAccount userAccount;

 @Column(nullable = false)
 private String stageName;

 @Column(nullable = false)
 private String genre;

 @Column(length = 1000)
 private String bio;

 @Column(nullable = false)
 private String bannerImage;

 @Column
 private String instagram;

 @Column
 private String twitter;

 @Column
 private String youtube;



 @Column(nullable = false, updatable = false)
 private LocalDateTime createdAt;

 @PrePersist
 protected void onCreate() {
  this.createdAt = LocalDateTime.now();
 }
}