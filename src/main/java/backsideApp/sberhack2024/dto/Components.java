package backsideApp.sberhack2024.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "Notification", value = Notification.class),
        @JsonSubTypes.Type(name = "Message", value = Message.class),
        @JsonSubTypes.Type(name = "Rate", value = Rate.class),
        @JsonSubTypes.Type(name = "Checkbox", value = Checkbox.class),
        @JsonSubTypes.Type(name = "ElementsList", value = ElementsList.class)
})
@Getter
@Setter
abstract public class Components {
    private String text;
    private String id;
    private boolean isDeletable;
}
