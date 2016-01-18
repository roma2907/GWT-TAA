package fr.istic.agile.client.domain;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Model de tache
 * 
 * @author ramage
 *
 */
public class Task {

    private Long id;
    private String name;
    private Developper developper;

    private UserStory userStory;

    public Task() {
    }

    @JsonCreator
    public Task(@JsonProperty("id") final Long id, @JsonProperty("name") final String name,
            @JsonProperty("developper") final Developper developper) {
        this.id = id;
        this.name = name;
        this.developper = developper;
    }

    public Long getId() {
        return id;
    }

    @JsonIgnore
    public UserStory getUserStory() {
        return userStory;
    }

    public void setUserStory(final UserStory userStory) {
        this.userStory = userStory;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Developper getDevelopper() {
        return developper;
    }

    public void setDevelopper(final Developper developper) {
        this.developper = developper;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
