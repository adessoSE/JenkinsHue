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

import de.adesso.jenkinshue.common.enumeration.Scenario.ScenarioDeserializer;

/**
 * 
 * @author wennier
 *
 */
@JsonSerialize(using = SimpleEnumJsonSerializer.class)
@JsonDeserialize(using = ScenarioDeserializer.class)
public enum Scenario implements SimpleEnum {

	BUILDING_AFTER_FAILURE("BUILDING nach FAILURE"), 
	BUILDING_AFTER_UNSTABLE("BUILDING nach UNSTABLE"), 
	BUILDING_AFTER_SUCCESS("BUILDING nach SUCCESS"), 
	
	FAILURE_AFTER_SUCCESS("FAILURE nach SUCCESS"), 
	FAILURE_AFTER_UNSTABLE("FAILURE nach UNSTABLE"), 
	FAILURE_AFTER_FAILURE("FAILURE nach FAILURE"), 
	
	UNSTABLE_AFTER_SUCCESS("UNSTABLE nach SUCCESS"), 
	UNSTABLE_AFTER_FAILURE("UNSTABLE nach FAILURE"), 
	UNSTABLE_AFTER_UNSTABLE("UNSTABLE nach UNSTABLE"), 
	
	SUCCESS_AFTER_FAILURE("SUCCESS nach FAILURE"), 
	SUCCESS_AFTER_UNSTABLE("SUCCESS nach UNSTABLE"), 
	SUCCESS_AFTER_SUCCESS("SUCCESS nach SUCCESS"), 
	
	BUILDING("BUILDING (Fallback)"),
	FAILURE("FAILURE (Fallback)"),
	UNSTABLE("UNSTABLE (Fallback)"),
	SUCCESS("SUCCESS (Fallback)");
	
	
	private String text;
	
	private Scenario(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public static int getPriorityListLength() {
		return 12;
	}
	
	public boolean isFallback() {
		switch(this) {
		case BUILDING:
			return true;
		case FAILURE:
			return true;
		case UNSTABLE:
			return true;
		case SUCCESS:
			return true;
		default:
			return false;
		}
	}
	
	static class ScenarioDeserializer extends JsonDeserializer<Scenario> {

		@Override
		public Scenario deserialize(JsonParser jp, DeserializationContext ctx)
				throws IOException, JsonProcessingException {
			ObjectCodec oc = jp.getCodec();
			JsonNode node = oc.readTree(jp);
			return Scenario.valueOf(node.get("name").asText());
		}
		
	}
	
}
