package com.mist.android.managers.notes;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.mist.android.globals.ActionDelegate;
import com.mist.android.main.Note;
import com.mist.android.managers.notes.actions.NotesAction;
import com.mist.android.managers.users.UserManager;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Header;

/**
 * Created by gerard on 25/06/14.
 */
@Singleton
public class NoteManagerImpl implements NoteManager {
    private static final String API_URL = "http://api.leleux.me";
    /**
     * Used to make note online request.
     */
    private NoteInterface mNoteInterface;
    /**
     * User manager.
     */
    @Inject
    UserManager mUserManager;
    /**
     * Provider to get all notes of a user.
     */
    @Inject
    Provider<NotesAction> mGetNotesAction;

    public NoteManagerImpl() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();
        mNoteInterface = restAdapter.create(NoteInterface.class);
    }

    @Override
    public void getAll(ActionDelegate<List<Note>> delegate) {
        mGetNotesAction.get().perform(mNoteInterface, new NotesDelegate(delegate), mUserManager.getCurrentToken().token);
    }

    /**
     * Delegate used to retrieve all notes of a user..
     */
    class NotesDelegate implements ActionDelegate<List<Note>> {

        private ActionDelegate<List<Note>> mNext;

        public NotesDelegate(ActionDelegate<List<Note>> next) {
            mNext = next;
        }

        @Override
        public void onSuccess(List<Note> result) {
            // TODO save notes in local database.
            mNext.onSuccess(result);
        }

        @Override
        public void onError(Exception e) {
            mNext.onError(e);
        }
    }

    public interface NoteInterface {
        @GET("/note")
        List<Note> getAll(@Header("api-token") String apiToken);
    }
}
