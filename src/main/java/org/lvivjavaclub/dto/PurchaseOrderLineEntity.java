package org.lvivjavaclub.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "purchase_order_lines")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderLineEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private PurchaseOrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ProductEntity product;

    @Positive
    @Column(nullable = false)
    private Integer quantity;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(precision = 14, scale = 2, nullable = false)
    private BigDecimal lineTotal;
}