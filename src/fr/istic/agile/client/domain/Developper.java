package fr.istic.agile.client.domain;

import java.util.Set;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Model d'un developpeur
 * 
 * @author ramage
 *
 */
public class Developper {

    private Long id;
    private String firstName;
    private String lastName;

    public Developper() {

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Developper other = (Developper) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    private Set<Project> projets;

    @JsonCreator
    public Developper(@JsonProperty("id") final Long id, @JsonProperty("firstName") final String firstName,
            @JsonProperty("lastName") final String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    @JsonIgnore
    public Set<Project> getProjets() {
        return projets;
    }

    public void setProjets(final Set<Project> projets) {
        this.projets = projets;
    }

}
