package fr.istic.agile.client.dialogBoxAdd;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

import fr.istic.agile.client.api.SprintClient;
import fr.istic.agile.client.domain.Sprint;

/**
 * Popup pour ajouter un sprint
 *
 * @author ramage
 *
 */
public class DialogBoxProject extends DialogBox {

    private final SprintClient sprintClient = GWT.create(SprintClient.class);

    public DialogBoxProject(final Long idProject) {
        // Enable animation.
        setAnimationEnabled(true);

        // Enable glass background.
        setGlassEnabled(true);


        final VerticalPanel vert = new VerticalPanel();
        final Label label = new Label("Ajouter un sprint");
        vert.add(label);
        final HorizontalPanel horizon1 = new HorizontalPanel();
        final Label labelNom = new Label("Nom du sprint :");
        horizon1.add(labelNom);
        final TextBox textbox = new TextBox();
        horizon1.add(textbox);
        vert.add(horizon1);
        final DateBox datebox = new DateBox();
        datebox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd")));
        final HorizontalPanel horizon2 = new HorizontalPanel();
        final Label label2 = new Label("Date de début : ");
        horizon2.add(label2);
        horizon2.add(datebox);
        vert.add(horizon2);
        final HorizontalPanel horizon3 = new HorizontalPanel();
        final DateBox datebox2 = new DateBox();
        datebox2.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd")));
        final Label label3 = new Label("Date de fin : ");
        horizon3.add(label3);
        horizon3.add(datebox2);
        vert.add(horizon3);
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
                // vérification des champs et que la date de fin soit après la
                // date de début
                if (textbox.getValue().trim().equals("") || datebox.getValue().after(datebox2.getValue())) {
                    vert.add(new Label("Le champs nom est vide ou la date de début est après la date de fin."));
                } else {
                    // récupération et affectation des valeurs tapées par
                    // l'utilisateur
                    final Sprint sprint = new Sprint();
                    sprint.setDateEnd(datebox2.getFormat().format(datebox2, datebox2.getValue()).toString());
                    sprint.setDateBegin(datebox.getFormat().format(datebox, datebox.getValue()).toString());
                    sprint.setName(textbox.getValue());
                    // appelle REST pour ajouter un sprint au projet
                    sprintClient.addSprint(sprint, idProject, new MethodCallback<Void>() {

                        @Override
                        public void onSuccess(final Method method, final Void response) {
                            // on ferme la popup
                            hide(true);
                        }

                        @Override
                        public void onFailure(final Method method, final Throwable exception) {

                        }
                    });
                }

            }
        });
        add(vert);
    }
}
