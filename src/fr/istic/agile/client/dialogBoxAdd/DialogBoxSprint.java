package fr.istic.agile.client.dialogBoxAdd;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import fr.istic.agile.client.api.UserStoryClient;
import fr.istic.agile.client.domain.UserStory;

/**
 * Popup pour ajouter un sprint
 *
 * @author ramage
 *
 */
public class DialogBoxSprint extends DialogBox {

    private final UserStoryClient userStoryClient = GWT.create(UserStoryClient.class);

    public DialogBoxSprint(final Long idSprint) {
        // Enable animation.
        setAnimationEnabled(true);

        // Enable glass background.
        setGlassEnabled(true);


        final VerticalPanel vert = new VerticalPanel();
        final Label label = new Label("Ajouter une user story");
        vert.add(label);
        final HorizontalPanel horizon1 = new HorizontalPanel();
        final Label labelNom = new Label("Nom de l'user story :");
        horizon1.add(labelNom);
        final TextBox textbox = new TextBox();
        horizon1.add(textbox);
        vert.add(horizon1);
        final HorizontalPanel horizon2 = new HorizontalPanel();
        final Label labelDevelopper = new Label("Temps estimé :");
        final IntegerBox integerbox = new IntegerBox();
        horizon2.add(labelDevelopper);
        horizon2.add(integerbox);
        vert.add(horizon2);
        final Button buttonValidate = new Button("Valider");
        vert.add(buttonValidate);
        final Button ferme = new Button("Fermer");
        ferme.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                hide(true);
            }
        });
        vert.add(ferme);
        // lorsqu'on clique sur le bouton pour ajouter une user story
        buttonValidate.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                // vérification que les champs soient bien remplies
                if (textbox.getValue().trim().equals("") || integerbox.getValue() == null) {
                    vert.add(new Label("erreur dans le formulaire"));
                } else {
                    // récupération et affectation des valeurs tapées par
                    // l'utilisateur
                    final UserStory userStory = new UserStory();
                    userStory.setName(textbox.getValue());
                    userStory.setTimeEstimatedDay(integerbox.getValue());
                    // appel de la méthode REST pour ajouter une user story
                    userStoryClient.addUserStory(userStory, idSprint, new MethodCallback<Void>() {

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

}
