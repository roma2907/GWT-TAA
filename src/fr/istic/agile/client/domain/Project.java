package fr.istic.agile.client.domain;

import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Model du projet
 * 
 * @author ramage
 *
 */
public class Project {

    private String name;
    private Long id;
    private List<Developper> developpers;
    private List<Sprint> sprints;

    public Project() {
    }

    @JsonCreator
    public Project(@JsonProperty("id") final Long id, @JsonProperty("name") final String name,
            @JsonProperty("developpers") final List<Developper> developpers,
            @JsonProperty("sprints") final List<Sprint> sprints) {
        this.id = id;
        this.name = name;
        this.sprints = sprints;
        this.developpers = developpers;
        for (final Sprint sprint : sprints) {
            sprint.setProject(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public List<Developper> getDeveloppers() {
        return developpers;
    }

    public void setDeveloppers(final List<Developper> developpers) {
        this.developpers = developpers;
    }

    public List<Sprint> getSprints() {
        return sprints;
    }

    public void setSprints(final List<Sprint> sprints) {
        this.sprints = sprints;
    }
}
