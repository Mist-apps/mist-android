package com.mist.android.main;

/**
 * Created by gerard on 25/06/14.
 */
public class Task {
    public final int order;
    public final String content;
    public final boolean done;

    public Task(String content, boolean done) {
        order = 0;
        this.content = content;
        this.done = done;
    }

    public Task(int order, String content, boolean done) {
        this.order = order;
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
