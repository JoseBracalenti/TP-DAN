package isi.dan.ms_productos.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

   public static final String STOCK_UPDATE_QUEUE = "stock-update-queue";

    @Value("${rabbitmq.exchange.name}")
    private String exc;

    @Value("${rabbitmq.queue.key}")
    private String clave;

    @Value("${rabbitmq.queue.name}")
    private String nombre;

    @Bean
    public Queue queue() {
        return new Queue(STOCK_UPDATE_QUEUE, true);
        // en teoría en vez de STOCK_UPDATE-QUEUE debería ser nombre
    }

    //spring bean para rabbitmq
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exc);
    }

    // Ruteo entre la queue y el exchange 
    public Binding binding(){
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(clave);
    }

    // Por Spring, estas 3 clases no necesitan configuración:
    // ConnectionFactory
    // RabbitTemplate
    // RabbitAdmin
}

