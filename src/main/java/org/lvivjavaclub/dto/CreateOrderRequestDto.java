package org.lvivjavaclub.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record CreateOrderRequestDto(
        @NotNull UUID customerId,
        @NotNull List<OrderItemRequestDto> items,
        URI callbackUri,
        Map<String, String> metadata
) {
    public record OrderItemRequestDto(
            @NotNull UUID productId,
            @Positive int quantity
    ) {}
}
