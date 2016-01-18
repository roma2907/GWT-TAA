package fr.istic.agile.client;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

import fr.istic.agile.client.domain.Project;
import fr.istic.agile.client.domain.Sprint;
import fr.istic.agile.client.domain.Task;
import fr.istic.agile.client.domain.UserStory;

/**
 * Model de l'arborescence des projets
 * projet1
 *******|sprint1
 ***********|userStory1
 ***************|tache2
 ***********|userStory2
 ***************|tache3
 *******|sprint2
 * projet2
 * @author ramage
 */
public class TreeViewModelProject<T, V> implements TreeViewModel {

    private final List<Project> projets;
    private final SingleSelectionModel<Object> selectionModel;

    public TreeViewModelProject(final List<Project> projets, final SingleSelectionModel<Object> selectionModel) {
        this.projets = projets;
        this.selectionModel = selectionModel;
    }

    @Override
    public <T> NodeInfo<?> getNodeInfo(final T value) {
        if (value == null) {
            // LEVEL 0.
            // We passed null as the root value. Return the composers.
            // Create a data provider that contains the list of composers.
            final ListDataProvider<Project> dataProvider = new ListDataProvider<Project>(projets);
            // Create a cell to display a composer.
            final Cell<Project> cell = new AbstractCell<Project>() {
                @Override
                public void render(final Context context, final Project value, final SafeHtmlBuilder sb) {
                    if (value != null) {
                        // affichage de l'image
                        sb.appendHtmlConstant(
                                "<image height='14px' width='14px' style='margin-right:5px' src='/images/project.png'/>");
                        // nom du projet
                        sb.appendEscaped(value.getName());
                    }
                }
            };

            // Return a node info that pairs the data provider and the cell.
            return new DefaultNodeInfo<Project>(dataProvider, cell, selectionModel, null);
        } else if (value instanceof Project) {
            // LEVEL 1.
            // We want the children of the composer. Return the playlists.
            final ListDataProvider<Sprint> dataProvider = new ListDataProvider<Sprint>(((Project) value).getSprints());
            final Cell<Sprint> cell = new AbstractCell<Sprint>() {
                @Override
                public void render(final Context context, final Sprint value, final SafeHtmlBuilder sb) {
                    if (value != null) {
                        // affichage de l'image su sprint
                        sb.appendHtmlConstant(
                                "<image height='14px' width='14px' style='margin-right:5px' src='/images/sprint.png'/>");
                        // affichage du nm du sprint
                        sb.appendEscaped(value.getName());
                    }
                }
            };
            return new DefaultNodeInfo<Sprint>(dataProvider, cell, selectionModel, null);
        } else if (value instanceof Sprint) {
            // LEVEL 2.
            // We want the children of the composer. Return the playlists.
            final ListDataProvider<UserStory> dataProvider = new ListDataProvider<UserStory>(
                    ((Sprint) value).getUserStories());
            final Cell<UserStory> cell = new AbstractCell<UserStory>() {
                @Override
                public void render(final Context context, final UserStory value, final SafeHtmlBuilder sb) {
                    if (value != null) {
                        // affichage de l'image de l'user story
                        sb.appendHtmlConstant(
                                "<image height='14px' width='14px' style='margin-right:5px' src='/images/book.png'/>");
                        // affichage du nom du sprint
                        sb.appendEscaped(value.getName());
                    }
                }
            };
            return new DefaultNodeInfo<UserStory>(dataProvider, cell, selectionModel, null);
        } else if (value instanceof UserStory) {
            // LEVEL 3 - LEAF.
            // We want the children of the playlist. Return the songs.
            final ListDataProvider<Task> dataProvider = new ListDataProvider<Task>(((UserStory) value).getTasks());

            final Cell<Task> cell = new AbstractCell<Task>() {
                @Override
                public void render(final Context context, final Task value, final SafeHtmlBuilder sb) {
                    if (value != null) {
                        // affichage de l'image d'une tache
                        sb.appendHtmlConstant(
                                "<image height='14px' style='margin-right:5px' width='14px' src='/images/task.png'/>");
                        // affichage du nom de la tache
                        sb.appendEscaped(value.getName());
                    }
                }
            };
            return new DefaultNodeInfo<Task>(dataProvider,cell,selectionModel, null);
        }
        return null;
    }

    @Override
    public boolean isLeaf(final Object value) {
        // renvoie true si on est sur une tache
        if (value instanceof Task) {
            return true;
        }
        return false;
    }

}
