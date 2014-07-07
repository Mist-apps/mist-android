package com.mist.android.managers.notes.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mist.android.AbstractDao;
import com.mist.android.main.Note;
import com.mist.android.main.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerard on 7/07/14.
 */
@Singleton
public class TaskDaoImpl extends AbstractDao implements TaskDao {

    private String[] ALL_COLUMNS_TASKS = {
            NAME_COLUMN_PRIM_TASK_ID,
            NAME_COLUMN_TASK_ORDER,
            NAME_COLUMN_TASK_CONTENT,
            NAME_COLUMN_TASK_DONE,
            NAME_COLUMN_TASK_NOTE
    };

    @Inject
    public TaskDaoImpl(Context context) {
        super(context);
    }

    @Override
    public int save(Note note) {
        if (note == null) {
            return -1;
        }
        try {
            open();
            if (note.getTasks() != null && !note.getTasks().isEmpty()) {
                ContentValues params;
                Task currentTask;
                for (int i = 0; i < note.getTasks().size(); i++) {
                    params = new ContentValues();
                    currentTask = note.getTasks().get(i);
                    params.put(NAME_COLUMN_TASK_ORDER, i);
                    params.put(NAME_COLUMN_TASK_CONTENT, currentTask.content);
                    params.put(NAME_COLUMN_TASK_DONE, currentTask.done ? 1 : 0);
                    params.put(NAME_COLUMN_TASK_NOTE, note._id);
                    mDataBase.insert(DATABASE_TABLE_TASKS, null, params);
                }
            }
            return 1;
        } finally {
            close();
        }
    }

    @Override
    public int update(String identifier, Note note) {
        if (identifier == null || identifier.isEmpty()) {
            return -1;
        }
        if (note == null) {
            return -1;
        }
        try {
            open();
            if (note.getTasks() != null && !note.getTasks().isEmpty()) {
                ContentValues params;
                Task currentTask;
                for (int i = 0; i < note.getTasks().size(); i++) {
                    params = new ContentValues();
                    currentTask = note.getTasks().get(i);
                    params.put(NAME_COLUMN_TASK_ORDER, i);
                    params.put(NAME_COLUMN_TASK_CONTENT, currentTask.content);
                    params.put(NAME_COLUMN_TASK_DONE, currentTask.done ? 1 : 0);
                    params.put(NAME_COLUMN_TASK_NOTE, note._id);
                    mDataBase.update(DATABASE_TABLE_TASKS, params, NAME_COLUMN_TASK_NOTE + " = ?", new String[]{identifier});
                }
            }
            return 1;
        } finally {
            close();
        }
    }

    @Override
    public int remove(String identifier) {
        if (identifier == null || identifier.isEmpty()) {
            return -1;
        }
        try {
            open();
            return mDataBase.delete(DATABASE_TABLE_TASKS, NAME_COLUMN_TASK_NOTE + " = ?", new String[]{identifier});
        } finally {
            close();
        }
    }

    @Override
    public List<Task> getAll(Note note) {
        if (note == null) {
            return new ArrayList<Task>();
        }
        try {
            open();
            final Cursor cTasks = mDataBase.query(DATABASE_TABLE_TASKS, ALL_COLUMNS_TASKS, NAME_COLUMN_TASK_NOTE + " = ?", new String[]{note._id}, null, null, null);
            return getTasksFromCursor(cTasks);
        } finally {
            close();
        }
    }

    public static List<Task> getTasksFromCursor(final Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return new ArrayList<Task>();
        }
        final List<Task> tasks = new ArrayList<Task>();
        if (cursor.moveToFirst()) {
            do {
                tasks.add(getTaskFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        return tasks;
    }

    public static Task getTaskFromCursor(final Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        final int order = cursor.getInt(cursor.getColumnIndex(NAME_COLUMN_TASK_ORDER));
        final String content = cursor.getString(cursor.getColumnIndex(NAME_COLUMN_TASK_CONTENT));
        final boolean done = cursor.getInt(cursor.getColumnIndex(NAME_COLUMN_TASK_DONE)) == 0 ? false : true;
        return new Task(order, content, done);
    }
}
