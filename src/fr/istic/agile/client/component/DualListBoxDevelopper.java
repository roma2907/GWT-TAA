package fr.istic.agile.client.component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import fr.istic.agile.client.api.DevelopperClient;
import fr.istic.agile.client.api.ProjetClient;
import fr.istic.agile.client.domain.Developper;
import fr.istic.agile.client.domain.Project;

/**
 * Composant pour permettre de déplacer et d'affecter des développeurs à un
 * projet
 * 
 * @author ramage
 *
 */
public class DualListBoxDevelopper extends Composite {

    private static final DevelopperClient developperClient = GWT.create(DevelopperClient.class);
    private static final ProjetClient projetClient = GWT.create(ProjetClient.class);

    private final ListBox listbox1 = new ListBox();
    private final ListBox listbox2 = new ListBox();

    private List<Developper> developpersAvailable;
    private List<Developper> developpersInProject;

    public DualListBoxDevelopper(final Long idProject) {
        final HorizontalPanel horizontalPanel = new HorizontalPanel();
        listbox1.setTitle("Développeur disponible");
        listbox1.setMultipleSelect(true);
        listbox2.setMultipleSelect(true);
        listbox2.setTitle("Développeur affecté au projet");
        // récupération des informations du projet
        projetClient.getProject(idProject, new MethodCallback<Project>() {

            @Override
            public void onSuccess(final Method method, final Project project) {
                // récupération des développeurs qui ne sont pas dans le projet
                developperClient.getDeveloppeurAvailable(idProject, new MethodCallback<List<Developper>>() {

                    @Override
                    public void onFailure(final Method method, final Throwable exception) {

                    }

                    @Override
                    public void onSuccess(final Method method, final List<Developper> notInProject) {
                        listbox1.clear();
                        listbox2.clear();

                        developpersInProject = project.getDeveloppers();
                        developpersAvailable = notInProject;
                        for (final Developper developper : notInProject) {
                            listbox1.addItem(developper.getFirstName() + " " + developper.getLastName());
                        }
                        for (final Developper developper : developpersInProject) {
                            listbox2.addItem(developper.getFirstName() + " " + developper.getLastName());
                        }

                        final Button change = new Button("changer");
                        change.addClickHandler(new ClickHandler() {

                            @Override
                            public void onClick(final ClickEvent event) {
                                final List<Integer> selectedItems1 = getSelectedItems(listbox1);
                                final List<Integer> selectedItems2 = getSelectedItems(listbox2);
                                for (final Integer i : selectedItems1) {
                                    final Developper dev = developpersAvailable.get(i);
                                    developpersInProject.add(dev);
                                    listbox2.addItem(dev.getFirstName() + " " + dev.getLastName());
                                }
                                for (final Integer i : selectedItems2) {
                                    final Developper dev = developpersInProject.get(i);
                                    developpersAvailable.add(dev);
                                    listbox1.addItem(dev.getFirstName() + " " + dev.getLastName());
                                }
                                for (final Integer i : selectedItems2) {
                                    developpersAvailable.remove(i);
                                    listbox2.removeItem(i);
                                }
                                for (final Integer i : selectedItems1) {
                                    developpersInProject.remove(i);
                                    listbox1.removeItem(i);
                                }
                            }
                        });
                        final VerticalPanel v1 = new VerticalPanel();
                        final VerticalPanel v2 = new VerticalPanel();
                        final Label label1 = new Label("Développeur disponible :");
                        final Label label2 = new Label("Développeur affecté au projet :");
                        v1.add(label1);
                        v2.add(label2);
                        v1.add(listbox1);
                        v2.add(listbox2);
                        horizontalPanel.add(v1);
                        horizontalPanel.add(change);
                        horizontalPanel.add(v2);

                    }
                });
            }

            @Override
            public void onFailure(final Method method, final Throwable exception) {

            }
        });
        initWidget(horizontalPanel);
    }

    private LinkedList<Integer> getSelectedItems(final ListBox listBox) {
        final LinkedList<Integer> selectedItems = new LinkedList<Integer>();

        for (int i = listBox.getItemCount() - 1; i >= 0; i--) {

            if (listBox.isItemSelected(i)) {
                selectedItems.add(i);
            }
        }
        return selectedItems;
    }

    public List<Developper> getValues() {
        final List<Developper> developpers = new ArrayList<Developper>();
        for (int i = 0; i < listbox2.getItemCount(); i++) {
            developpers.add(developpersInProject.get(i));
        }
        return developpers;
    }
}
