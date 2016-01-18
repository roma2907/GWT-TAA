package fr.istic.agile.client.domain;

import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Model d'une user story
 *
 * @author ramage
 *
 */
public class UserStory {

    private Long id;
    private List<Task> tasks;
    private String name;
    private Integer timeEstimatedDay;

    private Sprint sprint;

    public UserStory() {
    }

    @JsonCreator
    public UserStory(@JsonProperty("id") final Long id, @JsonProperty("tasks") final List<Task> tasks,
            @JsonProperty("timeEstimatedDay") final Integer timeEstimatedDay,
            @JsonProperty("name") final String name) {
        this.id = id;
        this.timeEstimatedDay = timeEstimatedDay;
        this.name = name;
        this.tasks = tasks;
        for (final Task task : tasks) {
            task.setUserStory(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @JsonIgnore
    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(final Sprint sprint) {
        this.sprint = sprint;
    }

    public Long getId() {
        return id;
    }

    public Integer getTimeEstimatedDay() {
        return timeEstimatedDay;
    }

    public void setTimeEstimatedDay(final Integer timeEstimatedDay) {
        this.timeEstimatedDay = timeEstimatedDay;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(final List<Task> tasks) {
        this.tasks = tasks;
    }
}
