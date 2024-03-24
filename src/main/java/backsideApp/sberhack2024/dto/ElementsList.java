package backsideApp.sberhack2024.dto;

import backsideApp.sberhack2024.dto.Components;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ElementsList extends Components {
        private String direction;
        private ArrayList<Components> elements;

        public ElementsList(String text, String id, boolean isDeletable) {
                super.setText(text);
                super.setId(id);
                super.setDeletable(isDeletable);
        }
}
