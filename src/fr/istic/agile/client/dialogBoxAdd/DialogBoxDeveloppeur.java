package fr.istic.agile.client.dialogBoxAdd;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import fr.istic.agile.client.api.DevelopperClient;
import fr.istic.agile.client.domain.Developper;

/**
 * Popup pour ajouter un développeur
 *
 * @author ramage
 *
 */
public class DialogBoxDeveloppeur extends DialogBox {

    private final DevelopperClient developperClient = GWT.create(DevelopperClient.class);

    public DialogBoxDeveloppeur() {
        // Enable animation.
        setAnimationEnabled(true);

        // Enable glass background.
        setGlassEnabled(true);


        final VerticalPanel vert = new VerticalPanel();
        final Label label = new Label("Ajouter un développeur");
        vert.add(label);
        final HorizontalPanel horizon1 = new HorizontalPanel();
        final Label labelNom = new Label("Prénom :");
        horizon1.add(labelNom);
        final TextBox textbox = new TextBox();
        horizon1.add(textbox);
        vert.add(horizon1);
        final HorizontalPanel horizon2 = new HorizontalPanel();
        final Label labelDevelopper = new Label("Nom :");
        final TextBox textbox2 = new TextBox();
        horizon2.add(labelDevelopper);
        horizon2.add(textbox2);
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
        buttonValidate.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                // vérification que les champs soient bien remplies
                if (textbox.getValue().trim().equals("") || textbox2.getValue().trim().equals("")) {
                    vert.add(new Label("erreur dans le formulaire"));
                } else {
                    // récupération et affectation des valeurs tapées par
                    // l'utilisateur
                    final Developper developper = new Developper();
                    developper.setFirstName(textbox.getValue());
                    developper.setLastName(textbox2.getValue());
                    // appel de la méthode REST pou rajouter un développeur
                    developperClient.addDevelopper(developper, new MethodCallback<Void>() {

                        @Override
                        public void onFailure(final Method method, final Throwable exception) {

                        }

                        @Override
                        public void onSuccess(final Method method, final Void response) {
                            // on stoppe la popup
                            hide(true);
                        }

                    });
                }

            }
        });
        add(vert);
    }

}
