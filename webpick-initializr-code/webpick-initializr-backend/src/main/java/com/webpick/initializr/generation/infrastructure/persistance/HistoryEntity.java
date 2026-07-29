package com.webpick.initializr.generation.infrastructure.persistance;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "history")
@Data
public class HistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID contextId;
    private String projectName;
    private LocalDateTime generatedAt;
}
