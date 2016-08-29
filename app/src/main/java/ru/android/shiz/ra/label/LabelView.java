package ru.android.shiz.ra.label;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import ru.android.shiz.ra.model.stream.Label;
import ru.android.shiz.ra.model.stream.Stream;

/**
 * Created by kassava on 22.08.2016.
 */
public interface LabelView extends MvpLceView<List<Label>> {

    public void showLabel();

    public void changeLabel(Stream stream, String label);

    public void showChangeLabelFailed(Stream stream, Throwable t);

    public void setMail(Stream stream);

}
