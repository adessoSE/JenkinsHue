package de.adesso.jenkinshue.common.enumeration;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 *
 * @author wennier
 *
 */
public class SimpleEnumJsonSerializer extends JsonSerializer<SimpleEnum> {

	@Override
	public void serialize(SimpleEnum e, JsonGenerator jg, SerializerProvider sp)
			throws IOException, JsonProcessingException {
		jg.writeStartObject();
		jg.writeStringField("name", e.toString());
		jg.writeStringField("text", e.getText());
		jg.writeEndObject();
	}
	
	@Override
	public Class<SimpleEnum> handledType() {
		return SimpleEnum.class;
	}
	
}
