package de.adesso.jenkinshue.common.enumeration;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.adesso.jenkinshue.common.enumeration.Role.RoleDeserializer;

/**
 * 
 * @author wennier
 *
 */
@JsonSerialize(using = SimpleEnumJsonSerializer.class)
@JsonDeserialize(using = RoleDeserializer.class)
public enum Role implements SimpleEnum {
	
	ROLE_ADMIN("Administrator");
	
	private String text;
	
	Role(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	static class RoleDeserializer extends JsonDeserializer<Role> {

		@Override
		public Role deserialize(JsonParser jp, DeserializationContext ctx)
				throws IOException, JsonProcessingException {
			ObjectCodec oc = jp.getCodec();
			JsonNode node = oc.readTree(jp);
			return Role.valueOf(node.get("name").asText());
		}

	}
	
}
