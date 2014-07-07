package com.mist.android.main.notes;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.inject.Inject;
import com.mist.android.R;
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
public class AllFragment extends RoboListFragment implements ActionDelegate<List<Note>>, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    LogWrapper mLogWrapper;
    @Inject
    NoteManager mNoteManager;

    private SwipeRefreshLayout mSrlContainer;
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
        final View root = inflater.inflate(R.layout.fragment_notes, container, false);
        mSrlContainer = (SwipeRefreshLayout) root.findViewById(R.id.notes_srl_container);
        mSrlContainer.setOnRefreshListener(this);
        mSrlContainer.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

        // Force refresh of notes if bundle is null (first launch of the fragment), otherwise,
        // we hope to hit on the cache or database.
        mNoteManager.getAll(this, savedInstanceState == null);
    }

    @Override
    public void onSuccess(List<Note> result) {
        if (mSrlContainer.isRefreshing()) {
            mSrlContainer.setRefreshing(false);
        }
        mLogWrapper.d("MainActivity", "Success : " + result);
        mAdapter.clear();
        mAdapter.addAll(result);
    }

    @Override
    public void onError(Exception e) {
        mLogWrapper.d("MainActivity", "Error", e);
    }

    @Override
    public void onRefresh() {
        mNoteManager.getAll(this, true);
    }
}
