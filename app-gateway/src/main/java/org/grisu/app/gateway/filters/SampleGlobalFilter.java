package org.grisu.app.gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


import java.util.Optional;

@Component
public class SampleGlobalFilter implements GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(SampleGlobalFilter.class);

    // Configuración desde una fuente externa (p. ej., variable de entorno)

    private static final String TOKEN_VALUE ="mi-token"; // O reemplaza con @Value

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Ejecutando antes del request PRE");

        // Revisar si TOKEN_VALUE está configurado
        if (TOKEN_VALUE == null) {
            logger.warn("TOKEN_VALUE no está configurado");
        } else {
            // Crear un nuevo request con el encabezado 'token' agregado
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("token", TOKEN_VALUE) // Agrega encabezado de forma correcta
                    .build();

            // Asociar el nuevo request con el exchange original
            exchange = exchange.mutate().request(mutatedRequest).build();
        }

        // Continuar con la cadena de filtros
        ServerWebExchange finalExchange = exchange;
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            logger.info("Ejecutando después del request POST");

            // Obtener el encabezado 'token' del request
            Optional<String> token = Optional.ofNullable(finalExchange.getRequest().getHeaders().getFirst("token"));
            token.ifPresent(to -> {
                logger.info("Token encontrado en el request: {}", to);

                // Añadir el token como encabezado a la respuesta
                finalExchange.getResponse().getHeaders().add("token", to);
            });

            // Agregar una cookie a la respuesta
            finalExchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "red").build());

            // Cambiar el tipo de contenido a texto plano
//            finalExchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        }));
    }

    @Override
    public int getOrder() {
        return 100; // Define la prioridad del filtro
    }

}