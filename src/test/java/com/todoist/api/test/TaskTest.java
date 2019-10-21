package com.todoist.api.test;

import com.todoist.api.utils.APIException;
import com.todoist.api.utils.Constants;
import com.todoist.api.utils.ToDoistConnector;
import kong.unirest.Unirest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class TaskTest {

    @BeforeMethod
    public void beforeTest() {
        Unirest.config().reset();
    }

    @Test(description = "Create task without not required parameters")
    public void testCreateTaskWithoutNotRequiredParameters() throws APIException {
        ToDoistConnector todoist = new ToDoistConnector(Constants.PERSONAL_TOKEN);
        long taskId = todoist.createNewTask("Create task test").id;

        //Delete task
        todoist.deleteTask(taskId);
    }

    @Test(description = "Create task without required parameter")
    public void testCreateTaskWithoutRequiredParameter() throws APIException {
        ToDoistConnector todoist = new ToDoistConnector(Constants.PERSONAL_TOKEN);
        assert todoist.createNewTaskWithMessage("") == Constants.HTTP_BAD_REQUEST;
    }

    @Test(description = "Create task with special symbols")
    public void testCreateTaskWithSpecialSymbols() throws APIException {
        ToDoistConnector todoist = new ToDoistConnector(Constants.PERSONAL_TOKEN);
        long taskId = todoist.createNewTask("Create task test!@#$%^&*()_+").id;

        //Check that content is full
        assert todoist.getActiveTask(taskId).content.contains("Create task test!@#$%^&*()_+");

        //Delete task
        todoist.deleteTask(taskId);
    }

}
