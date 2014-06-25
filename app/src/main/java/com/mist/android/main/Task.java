package com.mist.android.main;

/**
 * Created by gerard on 25/06/14.
 */
public class Task {
    public final String content;
    public final boolean done;

    public Task(String content, boolean done) {
        this.content = content;
        this.done = done;
    }

    @Override
    public String toString() {
        return "Task{" +
                "content='" + content + '\'' +
                ", done=" + done +
                '}';
    }
}
