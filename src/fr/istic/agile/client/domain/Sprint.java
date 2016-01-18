package fr.istic.agile.client.domain;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Model de sprint
 * 
 * @author ramage
 *
 */
public class Sprint {
    private Long id;
    private List<UserStory> userStories;
    private String dateBegin;
    private String dateEnd;
    private Project project;
    @NotNull
    private String name;

    public Sprint() {

    }

    @JsonCreator
    public Sprint(@JsonProperty("id") final Long id, @JsonProperty("userStories") final List<UserStory> userStories,
            @JsonProperty("dateBegin") final String dateBegin, @JsonProperty("dateEnd") final String dateEnd,
            @JsonProperty("name") final String name) {
        this.id = id;
        this.userStories = userStories;
        this.name = name;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        for (final UserStory userStory : userStories) {
            userStory.setSprint(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @JsonIgnore
    public Project getProject() {
        return project;
    }

    public void setProject(final Project project) {
        this.project = project;
    }

    public String getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(final String dateBegin) {
        this.dateBegin = dateBegin;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(final String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public List<UserStory> getUserStories() {
        return userStories;
    }

    public void setUserStories(final List<UserStory> userStories) {
        this.userStories = userStories;
    }
}
