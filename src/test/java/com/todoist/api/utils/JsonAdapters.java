package com.todoist.api.utils;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.todoist.api.utils.model.Task;
import com.todoist.api.utils.model.TaskRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JsonAdapters {

    private static final Moshi moshi = new Moshi.Builder().build();
    private static final JsonAdapter<Task[]> taskArrayJsonAdapter = moshi.adapter(Task[].class);
    private static final JsonAdapter<Task> taskJsonAdapter = moshi.adapter(Task.class);
    private static final JsonAdapter<TaskRequest> taskRequestJsonAdapter = moshi.adapter(TaskRequest.class);

    public static List<Task> extractTaskList(String json) throws IOException {
        return Arrays.asList(taskArrayJsonAdapter.fromJson(json));
    }

    public static Task extractTask(String json) throws IOException {
        return taskJsonAdapter.fromJson(json);
    }

    public static String writeTaskRequest(TaskRequest taskRequest) {
        return taskRequestJsonAdapter.toJson(taskRequest);
    }

}