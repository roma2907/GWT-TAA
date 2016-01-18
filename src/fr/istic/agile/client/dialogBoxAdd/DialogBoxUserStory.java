package fr.istic.agile.client.dialogBoxAdd;

import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import fr.istic.agile.client.api.DevelopperClient;
import fr.istic.agile.client.api.TaskClient;
import fr.istic.agile.client.domain.Developper;
import fr.istic.agile.client.domain.Task;

/**
 * Popup pour ajouter une tache
 * 
 * @author ramage
 *
 */
public class DialogBoxUserStory extends DialogBox {

    private final DevelopperClient developperClient = GWT.create(DevelopperClient.class);
    private final TaskClient taskClient = GWT.create(TaskClient.class);

    public DialogBoxUserStory(final Long idUserStory) {
        // récupération des développeurs dans le projet
        developperClient.getDevelopperInUserStory(idUserStory, new MethodCallback<List<Developper>>() {

            @Override
            public void onSuccess(final Method method, final List<Developper> response) {
                // Enable animation.
                setAnimationEnabled(true);

                // Enable glass background.
                setGlassEnabled(true);


                final VerticalPanel vert = new VerticalPanel();
                final Label label = new Label("Ajouter une tache");
                vert.add(label);
                final HorizontalPanel horizon1 = new HorizontalPanel();
                final Label labelNom = new Label("Nom de la tache :");
                horizon1.add(labelNom);
                final TextBox textbox = new TextBox();
                horizon1.add(textbox);
                vert.add(horizon1);
                final HorizontalPanel horizon2 = new HorizontalPanel();
                final Label labelDevelopper = new Label("Développeur :");
                // liste des développeurs du projet
                final ValueListBox<Developper> listbox = new ValueListBox<Developper>(
                        new AbstractRenderer<Developper>() {
                            @Override
                            public String render(final Developper object) {
                                return object == null ? "" : object.getFirstName() + " " + object.getLastName();
                            }
                        });
                listbox.setAcceptableValues(response);
                horizon2.add(labelDevelopper);
                horizon2.add(listbox);
                vert.add(horizon2);
                final Button buttonValidate = new Button("Valider");
                vert.add(buttonValidate);
                final Button ferme = new Button("Fermer");
                ferme.addClickHandler(new ClickHandler() {
                    public void onClick(final ClickEvent event) {
                        // on ferme la popup
                        hide();
                    }
                });
                vert.add(ferme);
                // lorsqu'on clique sur le bouton pour ajouter une tâche
                buttonValidate.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(final ClickEvent event) {
                        // vérification que les champs soient bien remplies
                        if(textbox.getValue().trim().equals("") || listbox == null){
                            vert.add(new Label("erreur dans le formulaire"));
                        } else {
                            // récupération et affectation des valeurs tapées
                            // par l'utilisateur
                            final Task task = new Task();
                            task.setName(textbox.getValue());
                            task.setDevelopper(listbox.getValue());
                            // appel de la méthode REST pour ajouter une tâche
                            taskClient.addTask(task, idUserStory, new MethodCallback<Void>() {

                                @Override
                                public void onFailure(final Method method, final Throwable exception) {

                                }

                                @Override
                                public void onSuccess(final Method method, final Void response) {
                                    // on ferme la popup
                                    hide(true);
                                }

                            });
                        }

                    }
                });
                add(vert);
            }

            @Override
            public void onFailure(final Method method, final Throwable exception) {

            }
        });

    }

}
