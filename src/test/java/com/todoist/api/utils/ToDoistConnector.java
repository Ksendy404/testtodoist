package com.todoist.api.utils;

import com.todoist.api.utils.model.Task;
import com.todoist.api.utils.model.TaskRequest;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.IOException;
import java.util.List;

import static com.todoist.api.utils.Constants.*;

public class ToDoistConnector {
    public ToDoistConnector(String token) {
        Unirest.config()
                .connectTimeout(20_000)
                .socketTimeout(20_000)
                .addDefaultHeader("Authorization", String.format("Bearer %s", token));
    }

    private <T> T extract(CheckedFunction<String, T> extractionFunction, HttpResponse<String> httpResponse) throws APIException {
        try {
            return extractionFunction.apply(httpResponse.getBody());
        } catch (IOException e) {
            throw new APIException("Error mapping JSON to Object");
        }
    }

    private interface CheckedFunction<P, R> {
        R apply(P t) throws IOException;
    }

    private Task createNewTask(TaskRequest taskRequest) throws APIException {
        HttpResponse<String> response = Unirest.post(URL_BASE + "/tasks")
                .header("Content-Type", "application/json")
                .body(JsonAdapters.writeTaskRequest(taskRequest))
                .asString();
        if (response.getStatus() != HTTP_OK) throw new APIException(response.getStatus());
        return extract(JsonAdapters::extractTask, response);
    }

    public Task createNewTask(String content) throws APIException {
        return createNewTask(new TaskRequest(content, null, null, null, null, null, null, null, null));
    }

    public Task createNewTask(String content, Long projectId, Integer order, List<Long> labelIds, Integer priority, String dueString, String dueDate, String dueDateTime, String dueLang) throws APIException, APIException {
        return createNewTask(new TaskRequest(content, projectId, order, labelIds, priority, dueString, dueDate, dueDateTime, dueLang));
    }

    public int createNewTaskWithMessage(String content) throws APIException {
        HttpResponse<String> response = Unirest.post(URL_BASE + "/tasks")
                .header("Content-Type", "application/json")
                .body(JsonAdapters.writeTaskRequest(new TaskRequest(content, null, null, null, null, null, null, null, null)))
                .asString();
        return response.getStatus();
    }

    public Task getActiveTask(long id) throws APIException {
        HttpResponse<String> response = Unirest.get(URL_BASE + "/tasks/" + id)
                .asString();
        if (response.getStatus() != HTTP_OK) throw new APIException(response.getStatus());
        return extract(JsonAdapters::extractTask, response);
    }

    public void deleteTask(long id) throws APIException {
        HttpResponse<String> response = Unirest.delete(URL_BASE + "/tasks/" + id)
                .asString();
        if (response.getStatus() != HTTP_OK_NO_CONTENT) throw new APIException(response.getStatus());
    }


}
