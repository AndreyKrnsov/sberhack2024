package backsideApp.sberhack2024.algo;

import backsideApp.sberhack2024.dto.Components;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonFiles {
    public static ArrayList<Components> readPojoFromJson(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        System.out.println("Devices have been read from JSON");
        return new ArrayList<>(mapper.readValue(json, new TypeReference<List<Components>>() {}));
    }

    public static String createJsonStringFromPojo(ArrayList<Components> list) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(list);  ;
            System.out.println(json);
            System.out.println("JSON-file has been created");
            return json;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String createJsonStringFromPojo(Components components) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(components);  ;
            System.out.println(json);
            System.out.println("JSON-file has been created");
            return json;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
