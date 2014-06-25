package com.mist.android.main.notes;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.inject.Inject;
import com.mist.android.R;
import com.mist.android.adapters.Factory;
import com.mist.android.adapters.Holder;
import com.mist.android.adapters.TypedFactory;
import com.mist.android.adapters.TypedHolder;
import com.mist.android.adapters.TypedHolderAdapter;
import com.mist.android.globals.ActionDelegate;
import com.mist.android.main.MainActivity;
import com.mist.android.main.Note;
import com.mist.android.main.holder.NoteHolder;
import com.mist.android.main.holder.TodoHolder;
import com.mist.android.managers.notes.NoteManager;
import com.mist.android.util.LogWrapper;

import java.util.ArrayList;
import java.util.List;

import roboguice.fragment.RoboListFragment;

/**
 * Created by gerard on 25/06/14.
 */
public class AllFragment extends RoboListFragment implements ActionDelegate<List<Note>> {

    @Inject
    LogWrapper mLogWrapper;
    @Inject
    NoteManager mNoteManager;

    private TypedHolderAdapter<Note> mAdapter;

    public static AllFragment newInstance() {
        final AllFragment allFragment = new AllFragment();
        return allFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Factory<Holder<Note>> factory = new Factory<Holder<Note>>() {
            @Override
            public Holder<Note> create() {
                return new NoteHolder();
            }
        };

        List<TypedFactory<Note>> factories = new ArrayList<TypedFactory<Note>>();
        factories.add(new TypedFactory<Note>() {
            @Override
            public boolean canCreate(Note object) {
                return object != null && !object.hasTasks();
            }

            @Override
            public TypedHolder create() {
                return new NoteHolder();
            }
        });
        factories.add(new TypedFactory<Note>() {
            @Override
            public boolean canCreate(Note object) {
                return object != null && object.hasTasks();
            }

            @Override
            public TypedHolder create() {
                return new TodoHolder();
            }
        });
        mAdapter = new TypedHolderAdapter<Note>(getActivity(), factories);
        setListAdapter(mAdapter);

        mNoteManager.getAll(this);
    }

    @Override
    public void onSuccess(List<Note> result) {
        mLogWrapper.d("MainActivity", "Success : " + result);
        mAdapter.addAll(result);
    }

    @Override
    public void onError(Exception e) {
        mLogWrapper.d("MainActivity", "Error", e);
    }
}
