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

import fr.istic.agile.client.api.ProjetClient;
import fr.istic.agile.client.domain.Project;

/**
 * Popup pour ajouter un projet
 *
 * @author ramage
 *
 */
public class DialogBoxProjects extends DialogBox {

    private final ProjetClient projetClient = GWT.create(ProjetClient.class);

    public DialogBoxProjects() {
        // Enable animation.
        setAnimationEnabled(true);

        // Enable glass background.
        setGlassEnabled(true);


        final VerticalPanel vert = new VerticalPanel();
        final Label label = new Label("Ajouter un projet");
        vert.add(label);
        final HorizontalPanel horizon1 = new HorizontalPanel();
        final Label labelNom = new Label("Nom du projet :");
        horizon1.add(labelNom);
        final TextBox textbox = new TextBox();
        horizon1.add(textbox);
        vert.add(horizon1);

        // création d'un bouton pour valider l'ajout
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
                // vérification que le champs soit bien rempli
                if (textbox.getValue().trim().equals("")) {
                    vert.add(new Label("erreur dans le formulaire"));
                } else {
                    // vérification que le nom du projet n'existe pas déjà
                    projetClient.existName(textbox.getValue(), new MethodCallback<Boolean>() {

                        @Override
                        public void onFailure(final Method method, final Throwable exception) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onSuccess(final Method method, final Boolean response) {
                            // si le projet n'exste pas
                            if (!response) {
                                // récupération et affectation des valeurs
                                // tapées par l'utilisateur
                                final Project project = new Project();
                                project.setName(textbox.getValue());
                                // méthode REST pour ajouter un projet
                                projetClient.addProject(project, new MethodCallback<Void>() {

                                    @Override
                                    public void onFailure(final Method method, final Throwable exception) {

                                    }

                                    @Override
                                    public void onSuccess(final Method method, final Void response) {
                                        hide(true);
                                    }

                                });
                            } else {
                                vert.add(new Label("un projet avec le même nom existe déjà."));
                            }
                        }
                    });

                }

            }
        });
        add(vert);
    }

}
