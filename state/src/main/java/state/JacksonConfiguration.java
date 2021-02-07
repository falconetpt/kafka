package state;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import state.model.Payment;

@Configuration
public class JacksonConfiguration {

  @Bean
  public ObjectMapper objectMapper() {
    final ObjectMapper mapper = new ObjectMapper();
    mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

    return mapper;
  }

  public static void main(final String[] args) throws JsonMappingException, JsonProcessingException {
    final ObjectMapper mapper = new ObjectMapper();
    
    final Payment payment1 = new Payment();
    payment1.add("banana", "klkdp");
//    payment1.getValues().put("dffdgfd", "dfgfdg");
    
    System.out.println(mapper.writeValueAsString(payment1));
    
    
    final Payment payment2 = mapper.readValue("{\"status\":\"123\", \"tot\":\"t\", \"tot2\": [\"punk\"]}", Payment.class);
    
    System.out.println(payment2.getValues().get("tot"));
    System.out.println(payment2.getStatus());
  }

}
