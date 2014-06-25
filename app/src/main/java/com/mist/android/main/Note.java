package com.mist.android.main;

import java.util.List;

/**
 * Created by gerard on 25/06/14.
 */
public class Note {
    public final String _id;
    public final String _revision;
    public final String _user;
    public final String title;
    public final String content;
    public final List<Task> tasks;
    public final long creationDate;
    public final long modificationDate;

    public Note(String _id, String _revision, String _user, String title, String content, List<Task> tasks, long creationDate, long modificationDate) {
        this._id = _id;
        this._revision = _revision;
        this._user = _user;
        this.title = title;
        this.content = content;
        this.tasks = tasks;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    public boolean hasTasks() {
        return tasks != null && tasks.size() != 0;
    }

    @Override
    public String toString() {
        return "Note{" +
                "_id='" + _id + '\'' +
                ", _revision='" + _revision + '\'' +
                ", _user='" + _user + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", tasks=" + tasks +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                '}';
    }
}
