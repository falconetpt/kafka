package provider1;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.currencycloud.provider.provider1.events.model.Event;
import com.currencycloud.provider.provider1.events.model.Payment;


/**
 * @author davidgammon
 *
 */
@Configuration
@SpringBootApplication
@ComponentScan({"provider1", "com.currencycloud.provider.ripple.topic.model"})
public class Provider1Application {
  private @Value("${payment.group}") String groupId;
  private @Value("${payment.topic}") String topic;
  
  
  public static void main(final String[] args) {
    SpringApplication.run(Provider1Application.class, args);
  }
  
  private final String bootstrapAddress = "localhost:9092";
  
  @Bean
  public KafkaAdmin kafkaAdmin() {
    final Map<String, Object> configs = new HashMap<>();
    configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    return new KafkaAdmin(configs);
  }

  @Bean
  public NewTopic paymentSubmissionTopic() {
    return TopicBuilder.name(topic).partitions(3).build();
  }

  @Bean
  public ProducerFactory<String, Event> producerFactory() {
    final Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, Event> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  public ConsumerFactory<String, Payment> consumerFactory() {
    final Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
    props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Payment.class);
    return new DefaultKafkaConsumerFactory<>(props);
  }

//  @Bean
//  public ConcurrentKafkaListenerContainerFactory<String, Payment> submitting(
//      final ConsumerFactory<String, Payment> p) {
//
//    final ConcurrentKafkaListenerContainerFactory<String, Payment> factory = new ConcurrentKafkaListenerContainerFactory<>();
//    factory.setConsumerFactory(consumerFactory());
//    factory.setRecordFilterStrategy(new EventFilteringStrategy("submitting"));
//
//    return factory;
//  }
//
//  @Bean
//  public ConcurrentKafkaListenerContainerFactory<String, Payment> created(
//      final ConsumerFactory<String, Payment> p) {
//
//    final ConcurrentKafkaListenerContainerFactory<String, Payment> factory = new ConcurrentKafkaListenerContainerFactory<>();
//    factory.setConsumerFactory(consumerFactory());
//    factory.setRecordFilterStrategy(new EventFilteringStrategy("created"));
//
//    return factory;
//  }
}
