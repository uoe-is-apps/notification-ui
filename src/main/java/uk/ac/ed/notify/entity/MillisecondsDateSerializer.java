package uk.ac.ed.notify.entity;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


public class MillisecondsDateSerializer extends JsonSerializer<Long> {

	@Override
	public void serialize(Long milliseconds, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
		
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		generator.writeString(format.format(new Date(milliseconds)));
		
	}
}
