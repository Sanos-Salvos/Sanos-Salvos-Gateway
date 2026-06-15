package com.sanosysalvos.gateway.config;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Order(-2)
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String mensajeCustom = "El Gateway no pudo procesar la solicitud de enrutamiento.";

        if (ex instanceof org.springframework.cloud.gateway.support.NotFoundException
                || ex instanceof java.net.ConnectException) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
            mensajeCustom = "El microservicio solicitado se encuentra apagado o fuera de línea.";
        }

        response.setStatusCode(status);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "Error Crítico en el API Gateway");
        errorDetails.put("mensaje", mensajeCustom + " Detalle Técnico: " + ex.getMessage());
        errorDetails.put("status", status.value());

        return response.writeWith(Mono.fromSupplier(() -> {
            try {
                byte[] bytes = objectMapper.writeValueAsBytes(errorDetails);
                return response.bufferFactory().wrap(bytes);
            } catch (Exception e) {
                return response.bufferFactory().wrap("{\"error\":\"Fatal Gateway Error\"}".getBytes());
            }
        }));
    }
}