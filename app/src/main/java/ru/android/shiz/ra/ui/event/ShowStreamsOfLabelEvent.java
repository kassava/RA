package ru.android.shiz.ra.ui.event;

import ru.android.shiz.ra.model.stream.Label;

/**
 * Created by kassava on 25.08.2016.
 */
public class ShowStreamsOfLabelEvent {

    private Label label;

    public ShowStreamsOfLabelEvent(Label label) {
        this.label = label;
    }

    public Label getLabel() {
        return label;
    }
}
