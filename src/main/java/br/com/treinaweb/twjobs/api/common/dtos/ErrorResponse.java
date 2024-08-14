package br.com.treinaweb.twjobs.api.common.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String message;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
}
