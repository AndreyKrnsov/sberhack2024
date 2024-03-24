package com.example.demo.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message extends Components {
    private String sender;

    public Message(String text, String id, boolean isDeletable) {
        super.setText(text);
        super.setId(id);
        super.setDeletable(isDeletable);
    }
}
