package org.lvivjavaclub.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.OffsetDateTime;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditMetadata {

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private OffsetDateTime lastModifiedAt;

    @Column(length = 100, nullable = false)
    private String createdBy;

    @Column(length = 100)
    private String lastModifiedBy;
}
