package com.denistiago.amqp;

import com.google.common.collect.ImmutableMap;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

import java.util.Map;

@Configuration
@Profile("!test")
public class AmqpConfig {

    public static final String WATERMARK_PROCESSING = "watermark.processing";
    public static final String WATERMARK_PROCESSING_DLQ = "dlq.watermark.processing";

    public static final int MAX_MESSAGE_RETRY_ATTEMPT = 3;

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAdviceChain(interceptor());
        return factory;
    }

    private RetryOperationsInterceptor interceptor() {
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(MAX_MESSAGE_RETRY_ATTEMPT)
                .backOffPolicy(new ExponentialBackOffPolicy())
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build();
    }

    @Bean
    public InitializingBean setupQueues(AmqpAdmin amqpAdmin) {
        return () -> {
            declareQueue(amqpAdmin, WATERMARK_PROCESSING, ImmutableMap.of("x-dead-letter-exchange", WATERMARK_PROCESSING_DLQ));
            declareQueue(amqpAdmin, WATERMARK_PROCESSING_DLQ, null);
        };
    }

    private void declareQueue(AmqpAdmin amqpAdmin, String name, Map<String, Object> args) {

        Queue queue = new Queue(name, true, false, false, args);
        DirectExchange exchange = new DirectExchange(name);
        Binding binding = BindingBuilder.bind(queue).to(exchange)
                .with(name);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareBinding(binding);
    }

}
