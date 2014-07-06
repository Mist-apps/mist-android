package com.mist.android.main.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mist.android.R;
import com.mist.android.adapters.TypedHolder;
import com.mist.android.main.Note;

/**
 * Created by gerard on 25/06/14.
 */
public class NoteHolder implements TypedHolder<Note> {
    private TextView mTextViewTitle;
    private TextView mTextViewContent;

    @Override
    public View getView(LayoutInflater inflater, ViewGroup parent, Note content) {
        final View root = inflater.inflate(R.layout.cell_note, parent, false);
        mTextViewTitle = (TextView) root.findViewById(R.id.cell_note_title);
        mTextViewContent = (TextView) root.findViewById(R.id.cell_note_content);
        return root;
    }

    @Override
    public void setContent(Note content) {
        if (content.title == null || content.title.isEmpty()) {
            mTextViewTitle.setVisibility(View.GONE);
        } else {
            mTextViewTitle.setVisibility(View.VISIBLE);
            mTextViewTitle.setText(content.title);
        }
        if (content.getContent() != null) {
            mTextViewContent.setText(content.getContent());
        } else {
            mTextViewContent.setText("Bad Tasks");
        }
    }

    @Override
    public boolean canHandle(Object object) {
        return object instanceof Note && !((Note) object).hasTasks();
    }
}
