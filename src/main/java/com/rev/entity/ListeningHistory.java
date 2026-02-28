package com.rev.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "listening_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListeningHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Songs song;

    @Column(nullable = false)
    private LocalDateTime playedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private ActionType actionType; // PLAY, SKIP, PAUSE

    @PrePersist
    protected void prePersist() {
        if (this.playedAt == null) {
            this.playedAt = LocalDateTime.now();
        }
        if (this.actionType == null) {
            this.actionType = ActionType.PLAY; // default
        }
    }

    public enum ActionType {
        PLAY,
        SKIP,
        PAUSE
    }
}