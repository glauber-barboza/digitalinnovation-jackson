package digitalinnovation.example.restfull.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import digitalinnovation.example.restfull.enums.Raca;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class Jackson {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        //PROPIEDADES NÂO MAPEADAS Não quebram
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //FALHA SE ALGUMA PROPIEDADE ESTIVER VAZIA
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // SERVE Para compatiblidade de arrays, quando tem um array com um item, caso não tenha essa config ele se perde
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        //Serelize datas
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(racaModuleMapper());
        return objectMapper;
    }

    public SimpleModule racaModuleMapper() {
        SimpleModule dateModule = new SimpleModule("JSONRacaModule", PackageVersion.VERSION);
        dateModule.addSerializer(Raca.class, new RacaCerialize());
        dateModule.addDeserializer(Raca.class, new RacaDescerialize());
        return dateModule;
    }

    class RacaCerialize extends StdSerializer<Raca> {
        public RacaCerialize() {
            super(Raca.class);
        }

        @Override
        public void serialize(Raca raca, JsonGenerator json, SerializerProvider provider) throws IOException {
            json.writeString(raca.getValue());
        }
    }

    class RacaDescerialize extends StdDeserializer<Raca> {
        public RacaDescerialize() {
            super(Raca.class);
        }

        @Override
        public Raca deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value =  p.getText();
            return Raca.of(p.getText());
        }
    }
}
