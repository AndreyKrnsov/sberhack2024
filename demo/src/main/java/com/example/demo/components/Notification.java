package com.example.demo.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends Components {
    private String info;

    public Notification(String text, String id, boolean isDeletable) {
        super.setText(text);
        super.setId(id);
        super.setDeletable(isDeletable);
    }
}
