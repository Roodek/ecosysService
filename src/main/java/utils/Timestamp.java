package utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import lombok.*;

import java.io.IOException;
import java.time.Instant;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Timestamp implements JsonSerializable {

    private Instant time;

    public static Timestamp now(){
        return new Timestamp(Instant.now());
    }

    public Timestamp(Long millis){
        this.time = Instant.ofEpochMilli(millis);
    }

    public static Timestamp of(Instant instant) {
        return new Timestamp(instant);
    }

    @Override
    public void serialize(JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(time.toEpochMilli());
    }

    @Override
    public void serializeWithType(JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSerializer){
        throw new UnsupportedOperationException("Nor Implemented");
    }
}
