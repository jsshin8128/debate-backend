package com.example.debate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DebateRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private boolean closed = false;

    @Column(nullable = false)
    private int participantCount = 0;

    public void end() {
        this.closed = true;
    }

    public void join() {
        if (!closed) this.participantCount += 1;
    }

    public void leave() {
        if (participantCount > 0) this.participantCount -= 1;
    }
}
