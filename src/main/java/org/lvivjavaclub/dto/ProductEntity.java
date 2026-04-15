package org.lvivjavaclub.dto;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    private UUID id;

    @NotBlank
    @Size(min = 3, max = 120)
    @Column(length = 120, nullable = false)
    private String name;

    @Size(max = 1000)
    @Column(length = 1000)
    private String description;

    @Positive
    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal price;

    @Positive
    @Column(precision = 10, scale = 3)
    private BigDecimal weightKg;

    @Column(length = 500)
    private URI productPage;

    private Duration warrantyPeriod;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_image_uris", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_uri", length = 500)
    @Builder.Default
    private List<URI> imageUris = new ArrayList<>();
}
