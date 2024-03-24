package backsideApp.sberhack2024.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Checkbox extends Components {
    private ArrayList<String> box;
    private boolean isMultiply;

    public Checkbox(String text, String id, boolean isDeletable) {
        super.setText(text);
        super.setId(id);
        super.setDeletable(isDeletable);
    }
}
