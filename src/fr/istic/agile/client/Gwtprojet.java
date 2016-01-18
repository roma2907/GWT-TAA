package fr.istic.agile.client;

import java.util.Date;
import java.util.List;

import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

import fr.istic.agile.client.api.DevelopperClient;
import fr.istic.agile.client.api.ProjetClient;
import fr.istic.agile.client.api.SprintClient;
import fr.istic.agile.client.api.TaskClient;
import fr.istic.agile.client.api.UserStoryClient;
import fr.istic.agile.client.component.DualListBoxDevelopper;
import fr.istic.agile.client.dialogBoxAdd.DialogBoxDeveloppeur;
import fr.istic.agile.client.dialogBoxAdd.DialogBoxProject;
import fr.istic.agile.client.dialogBoxAdd.DialogBoxProjects;
import fr.istic.agile.client.dialogBoxAdd.DialogBoxUserStory;
import fr.istic.agile.client.domain.Developper;
import fr.istic.agile.client.domain.Project;
import fr.istic.agile.client.domain.Sprint;
import fr.istic.agile.client.domain.Task;
import fr.istic.agile.client.domain.UserStory;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Gwtprojet implements EntryPoint {

    private final ProjetClient projetClient = GWT.create(ProjetClient.class);
    private final SprintClient sprintClient = GWT.create(SprintClient.class);
    private final UserStoryClient userStoyClient = GWT.create(UserStoryClient.class);
    private final TaskClient taskClient = GWT.create(TaskClient.class);
    private final DevelopperClient developperClient = GWT.create(DevelopperClient.class);

    private final Label errorLabelForm = new Label("erreur dans le formulaire.");

    @Override
    public void onModuleLoad() {
        // adresse du serveur rest
        Defaults.setServiceRoot("http://myapp.taa.fr/");
        // on appelle la méthode pour rafraichir les données de l'arborescence
        refreshTree();
    }

    /**
     * Méthode qui rafraichit les informations de l'arborescence.
     */
    private void refreshTree() {
        // on supprime les éléments du content
        RootPanel.get("content").clear();
        final Button buttonAddProject = new Button("Ajouter un projet");
        // lorsqu'on clique sur ajouter un projet
        buttonAddProject.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                // apparition d'une fenetre pour ajouter un projet
                final DialogBox box = new DialogBoxProjects();
                box.show();
                box.addCloseHandler(new CloseHandler<PopupPanel>() {

                    @Override
                    public void onClose(final CloseEvent<PopupPanel> event) {
                        // a la fermeture de la popup on rafraichit les données
                        // de l'arborescence
                        refreshTree();
                    }
                });
            }
        });
        // ajout du bouton "Ajouter un projet"
        RootPanel.get("content").add(buttonAddProject);
        // création d'un bouton pour gérer les développeurs
        final Button buttonDevelopper = new Button("Gestion des développeurs");
        // lors d'un click sur le bouton de gestion des développeurs
        buttonDevelopper.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                // on rafraichit les données développeurs
                refreshDeveloppeur();
            }
        });
        // on ajoute le bouton de gestion des développeurs
        RootPanel.get("content").add(buttonDevelopper);
        // récupération des informations des projets
        projetClient.getProjets(new MethodCallback<List<Project>>() {

            public void onFailure(final Method method, final Throwable exception) {
                final Label errorLabel = new Label();
                errorLabel.setText("error projets");
                RootLayoutPanel.get().add(errorLabel);
            }


            public void onSuccess(final Method method, final List<Project> response) {
                // création de l'arborescence
                final SingleSelectionModel<Object> selectionModel = new SingleSelectionModel<Object>();
                final TreeViewModel model = new TreeViewModelProject(response, selectionModel);
                final CellTree tree = new CellTree(model, null);
                // ajout de l'arborescence au contenu de la page
                RootPanel.get("content").add(tree);
                // ajout d'un évènement appelé quand on change de noeud dans
                // l'arborescence
                selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

                    @Override
                    public void onSelectionChange(final SelectionChangeEvent event) {
                        // récupération de l'objet sélectionné
                        final Object obj = selectionModel.getSelectedObject();
                        // on supprime le contenu de selectedTree
                        RootPanel.get("selectedTree").clear();
                        final VerticalPanel vert = new VerticalPanel();
                        if (obj instanceof Project) {
                            changeProject(vert, obj);
                        } else if (obj instanceof Sprint) {
                            changeSprint(vert, obj);
                        } else if (obj instanceof UserStory) {
                            changeUserStory(vert, obj);
                        } else if (obj instanceof Task) {
                            changeTask(vert, obj);
                        }
                        // ajout de la zone verticale
                        RootPanel.get("selectedTree").add(vert);
                    }

                    /**
                     * Lorsqu'on clique sur une tache dans l'arborescence
                     *
                     * @param vert
                     *            : panel sur lequel on ajoute les éléments
                     * @param obj
                     *            : tache à afficher
                     */
                    private void changeTask(final VerticalPanel vert, final Object obj) {
                        // modifier tache
                        final HorizontalPanel horizon = new HorizontalPanel();
                        final Task task = (Task) obj;
                        final TextBox textBox = new TextBox();
                        textBox.setText(task.getName());
                        final Label label = new Label("Nom : ");
                        horizon.add(label);
                        horizon.add(textBox);
                        vert.add(horizon);
                        final HorizontalPanel horizon2 = new HorizontalPanel();
                        final Label label2 = new Label("Développeur affecté : ");
                        horizon2.add(label2);
                        // liste des dévellopeurs du projet
                        final ValueListBox<Developper> listbox = new ValueListBox<Developper>(
                                new AbstractRenderer<Developper>() {
                                    @Override
                                    public String render(final Developper object) {
                                        return object == null ? "" : object.getFirstName() + " " + object.getLastName();
                                    }
                                });
                        listbox.setValue(task.getDevelopper());
                        listbox.setAcceptableValues(task.getUserStory().getSprint().getProject().getDeveloppers());
                        horizon2.add(listbox);
                        vert.add(horizon2);
                        // ajout d'un bouton pour modifier la tache
                        final Button button = new Button();
                        button.setText("modifier");
                        vert.add(button);
                        button.addClickHandler(new ClickHandler() {

                            @Override
                            public void onClick(final ClickEvent event) {
                                // vérification que les champs soient remplies
                                if(textBox.getValue().trim().equals("") || listbox.getValue() == null){
                                    vert.add(errorLabelForm);
                                }else{
                                    // récupération et affectation des valeurs
                                    // tapées
                                    task.setName(textBox.getValue());
                                    task.setDevelopper(listbox.getValue());
                                    // envoi au serveur rest de la tache
                                    // modifiée
                                    taskClient.putTask(task, new MethodCallback<Void>() {

                                        @Override
                                        public void onFailure(final Method method, final Throwable exception) {
                                            // TODO Auto-generated method stub

                                        }

                                        @Override
                                        public void onSuccess(final Method method, final Void response) {
                                            // une fois la requête au serveur
                                            // REST faites, on actualise les
                                            // informations de l'arborescence
                                            refreshTree();
                                        }
                                    });
                                }
                            }
                        });
                        // création d'un bouton pour supprimer es taches
                        final Button buttonDelete = new Button("Spprimer la tache");
                        buttonDelete.addClickHandler(new ClickHandler() {

                            @Override
                            public void onClick(final ClickEvent event) {
                                // message de confirmation
                                final boolean result = Window.confirm("Etes vous sure de vouloir supprimer cette tache ?");
                                if (result) {
                                    // on appelle le serveur REST, pour
                                    // supprimer la tache spécifié
                                    taskClient.deleteTask(task.getId(), new MethodCallback<Void>() {

                                        @Override
                                        public void onFailure(final Method method, final Throwable exception) {
                                            // TODO Auto-generated method stub

                                        }

                                        @Override
                                        public void onSuccess(final Method method, final Void response) {
                                            // une fois la requête faites, on
                                            // rafraichit les informations de
                                            // l'arborescence
                                            refreshTree();
                                        }
                                    });
                                }
                            }
                        });
                        // ajout du bouton de supression de tache
                        vert.add(buttonDelete);
                    }

                    /**
                     * Méthode appelé lorsqu'on clique sur un noeud UserStory
                     * dans l'arbre, on peut ici dans la zone selected, voir et
                     * modifier les informations de l'user story
                     *
                     * @param vert
                     *            : panel sur lequel on ajoute les éléments
                     * @param obj
                     *            : User story à modifier
                     */
                    private void changeUserStory(final VerticalPanel vert, final Object obj) {
                        // modifier user story
                        final HorizontalPanel horizon = new HorizontalPanel();
                        final UserStory userStory = (UserStory) obj;
                        final TextBox textBox = new TextBox();
                        textBox.setText(userStory.getName());
                        final Label label = new Label("Nom : ");
                        horizon.add(label);
                        horizon.add(textBox);
                        vert.add(horizon);
                        final HorizontalPanel horizon2 = new HorizontalPanel();
                        final Label label2 = new Label("Temps estimé : ");
                        horizon2.add(label2);
                        final IntegerBox integerBox = new IntegerBox();
                        integerBox.setValue(userStory.getTimeEstimatedDay());
                        horizon2.add(integerBox);
                        vert.add(horizon2);
                        // création d'un bouton valider
                        final Button button = new Button();
                        button.setText("valider");
                        vert.add(button);

                        button.addClickHandler(new ClickHandler() {

                            @Override
                            public void onClick(final ClickEvent event) {
                                // vérification que les champs soient bien
                                // remplies
                                if (textBox.getValue().trim().equals("") || integerBox.getValue() == null) {
                                    vert.add(errorLabelForm);
                                } else {
                                    // récupération et affectation des valeurs
                                    // tapées par l'utilisateur
                                    userStory.setName(textBox.getValue());
                                    userStory.setTimeEstimatedDay(integerBox.getValue());
                                    // envoi de la requête pour modifier une
                                    // user story
                                    userStoyClient.putUserStory(userStory, new MethodCallback<Void>() {

                                        @Override
                                        public void onFailure(final Method method, final Throwable exception) {

                                        }

                                        @Override
                                        public void onSuccess(final Method method, final Void response) {
                                            // on actualise les informations de
                                            // l'arbre
                                            refreshTree();
                                        }
                                    });
                                }
                            }
                        });
                        // ajouter tache
                        final Button buttonAdd = new Button();
                        buttonAdd.setText("Ajouter une tache ");
                        buttonAdd.addClickHandler(new ClickHandler() {

                            @Override
                            public void onClick(final ClickEvent event) {
                                // fenetre pour ajouter une tache à l'user story
                                final DialogBox box = new DialogBoxUserStory(userStory.getId());
                                box.show();
                                box.addCloseHandler(new CloseHandler<PopupPanel>() {

                                    @Override
                                    public void onClose(final CloseEvent<PopupPanel> event) {
                                        // a la fermeture de la popup, on
                                        // actualise es informations de
                                        // l'arborescence
                                        refreshTree();
                                    }
                                });
                            }
                        });
                        // ajout du bouton pour ajouter une tache au panel
                        vert.add(buttonAdd);
                    }

                    /**
                     * Méthode appelé lorsqu'on clique sur un noeud sprint de
                     * l'arborescence, cette méthode affiche dans la zone
                     * selected les éléments nécéssaire à la modification d'un
                     * sprint
                     *
                     * @param vert
                     *            : panel sur lequel on ajoute les éléments
                     * @param obj
                     *            : sprint à modifier
                     */
                    private void changeSprint(final VerticalPanel vert, final Object obj) {
                        final HorizontalPanel horizon = new HorizontalPanel();
                        final Sprint sprint = (Sprint) obj;
                        final TextBox textBox = new TextBox();
                        textBox.setText(sprint.getName());
                        final DateBox datebox = new DateBox();
                        datebox.setValue(new Date(sprint.getDateBegin()));
                        datebox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd")));
                        final Label label = new Label("Nom : ");
                        horizon.add(label);
                        horizon.add(textBox);
                        vert.add(horizon);
                        final HorizontalPanel horizon2 = new HorizontalPanel();
                        final Label label2 = new Label("Date de début : ");
                        horizon2.add(label2);
                        horizon2.add(datebox);
                        vert.add(horizon2);
                        final HorizontalPanel horizon3 = new HorizontalPanel();
                        final DateBox datebox2 = new DateBox();
                        datebox2.setValue(new Date(sprint.getDateEnd()));
                        datebox2.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd")));
                        final Label label3 = new Label("Date de fin : ");
                        horizon3.add(label3);
                        horizon3.add(datebox2);
                        vert.add(horizon3);
                        // création du bouton modifier
                        final Button button = new Button();
                        button.setText("modifier");
                        vert.add(button);
                        // lors d'un clique sur le bouton modifier
                        button.addClickHandler(new ClickHandler() {

                            @Override
                            public void onClick(final ClickEvent event) {
                                // vérification que les champs soient bien
                                // remplies et que la date de fin soit bien
                                // après la date de début
                                if (textBox.getValue().trim().equals("")
                                        || datebox.getValue().after(datebox2.getValue())) {
                                    vert.add(new Label(
                                            "Le champs nom est vide ou la date de début est après la date de fin."));
                                } else {
                                    // récupération et affectation des valeurs
                                    // tapées par l'utilisateur
                                    sprint.setDateEnd(
                                            datebox2.getFormat().format(datebox2, datebox2.getValue()).toString());
                                    sprint.setDateBegin(
                                            datebox.getFormat().format(datebox, datebox.getValue()).toString());
                                    sprint.setName(textBox.getValue());
                                    // envoi au serveur REST d'une requête pour
                                    // modifier le sprint
                                    sprintClient.putSprint(sprint, new MethodCallback<Void>() {

                                        @Override
                                        public void onFailure(final Method method, final Throwable exception) {

                                        }

                                        @Override
                                        public void onSuccess(final Method method, final Void o) {
                                            // actualisation des informations de
                                            // l'arborescence
                                            refreshTree();
                                        }

                                    });
                                }
                            }
                        });

                        // ajouter user story
                        final Button buttonAdd = new Button();
                        buttonAdd.setText("Ajouter une user story ");
                        // lors d'un click sur le bouton ajouter user story
                        buttonAdd.addClickHandler(new ClickHandler() {

                            @Override
                            public void onClick(final ClickEvent event) {
                                // affichage d'une popup pour ajouter une user
                                // story
                                final DialogBox box = new DialogBoxUserStory(sprint.getId());
                                box.show();
                                box.addCloseHandler(new CloseHandler<PopupPanel>() {

                                    @Override
                                    public void onClose(final CloseEvent<PopupPanel> event) {
                                        // à la fermeture de la popup on
                                        // actualise les informations de l'arbre
                                        refreshTree();
                                    }
                                });
                            }
                        });
                        // ajout du bouton ajouter une user story au panel
                        vert.add(buttonAdd);
                    }

                    /**
                     * Méthode appelé lorsqu'on clique sur un noeud projet,
                     * cette méthode créé aussi les éléments nécéssaires à la
                     * modification du projet
                     *
                     * @param vert
                     *            : panel sur lequel on ajoute les éléments
                     *            nécéssaire à la modification du projet
                     * @param obj
                     *            : projet à afficher, modifier
                     */
                    private void changeProject(final VerticalPanel vert, final Object obj) {

                        final HorizontalPanel horizon = new HorizontalPanel();
                        final Project projet = (Project) obj;
                        final TextBox textBox = new TextBox();
                        textBox.setText(projet.getName());
                        final Label label = new Label("Nom : ");
                        horizon.add(label);
                        horizon.add(textBox);
                        final HorizontalPanel horizon2 = new HorizontalPanel();
                        // création d'un composant qui permet d'affecter des
                        // développeurs à un projet
                        final DualListBoxDevelopper dualistbox = new DualListBoxDevelopper(projet.getId());

                        horizon2.add(dualistbox);

                        vert.add(horizon);
                        vert.add(horizon2);
                        final Button button = new Button("modifier");
                        // lorsqu'on clique sur le bouton pour modifier un
                        // projet
                        button.addClickHandler(new ClickHandler() {

                            @Override
                            public void onClick(final ClickEvent event) {
                                // vérification que le nom du projet soit bien
                                // rempli
                                if (textBox.getValue().trim().equals("")) {
                                    vert.add(errorLabelForm);
                                } else {
                                    // s'il y a déjà un projet avec un nom
                                    // identique avec un identifiant différent
                                    projetClient.existNameWithIdentifiant(textBox.getValue(), projet.getId(),
                                            new MethodCallback<Boolean>() {

                                        @Override
                                        public void onFailure(final Method method, final Throwable exception) {

                                        }

                                        @Override
                                        public void onSuccess(final Method method, final Boolean response) {
                                            // s'il il n'y a pas de projet avec
                                            // un nom identique
                                            if (!response) {
                                                // récupration et affectation
                                                // des valeurs tapées par
                                                // l'utilisateur
                                                projet.setName(textBox.getValue());
                                                projet.setDeveloppers(dualistbox.getValues());
                                                // on appelle le serveur REST
                                                // pour mettre à jour les
                                                // informations du projet
                                                projetClient.updateProject(projet, new MethodCallback<Void>() {

                                                    @Override
                                                    public void onFailure(final Method method,
                                                            final Throwable exception) {

                                                    }

                                                    @Override
                                                    public void onSuccess(final Method method, final Void response) {
                                                        // on actualise les
                                                        // informations de
                                                        // l'arborescence
                                                        refreshTree();
                                                    }
                                                });
                                            } else {
                                                vert.add(new Label("Un projet avec le même nom existe déjà."));
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        // ajout du bouton pour modifier un projet
                        vert.add(button);

                        // ajouter sprint
                        final Button buttonAdd = new Button("Ajouter un sprint ");
                        buttonAdd.addClickHandler(new ClickHandler() {

                            @Override
                            public void onClick(final ClickEvent event) {
                                // affichage d'une popup pour ajouter un sprint
                                // au projet
                                final DialogBox box = new DialogBoxProject(projet.getId());
                                box.show();
                                box.addCloseHandler(new CloseHandler<PopupPanel>() {

                                    @Override
                                    public void onClose(final CloseEvent<PopupPanel> event) {
                                        // a a fermeture de la popup, on
                                        // actualise les informations de
                                        // l'arborescence
                                        refreshTree();
                                    }
                                });
                            }
                        });
                        // ajout du bouton pour ajouter un sprint
                        vert.add(buttonAdd);
                    }

                });

            }
        });
    }

    /**
     * Méthode pour ajouter les éléments UI utilisé pour la gestion les
     * développeurs
     */
    private void refreshDeveloppeur() {
        // suppression du content
        RootPanel.get("content").clear();
        // supression des éléments sélectionnés
        RootPanel.get("selectedTree").clear();
        final VerticalPanel vert = new VerticalPanel();
        final Button buttonProject = new Button("Gestion des projets");
        // lorsqu'on clique sur le bouton de gestion des projets
        buttonProject.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                // on retourne sur la page de l'arborescence
                refreshTree();
            }
        });
        // ajout au panel du bouton de gestion des projets
        vert.add(buttonProject);
        // récupération de tous les développeurs
        developperClient.getDeveloppers(new MethodCallback<List<Developper>>() {

            @Override
            public void onFailure(final Method method, final Throwable exception) {

            }

            @Override
            public void onSuccess(final Method method, final List<Developper> response) {
                // affichage d'un tableau contenant les développeurs
                final CellTable<Developper> table = new CellTable<Developper>();

                // première colonne contenant le prénom du développeur
                final TextColumn<Developper> firstNameColumn = new TextColumn<Developper>() {
                    @Override
                    public String getValue(final Developper dev) {
                        return dev.getFirstName();
                    }
                };

                // la colonne est triable
                firstNameColumn.setSortable(true);

                // deuxième colonne contenant le nom du développeur
                final TextColumn<Developper> lastNameColumn = new TextColumn<Developper>() {
                    @Override
                    public String getValue(final Developper developper) {
                        return developper.getLastName();
                    }
                };

                // ajout des colonnes
                table.addColumn(firstNameColumn, "Prénom");
                table.addColumn(lastNameColumn, "Nom");

                // Create a data provider.
                final ListDataProvider<Developper> dataProvider = new ListDataProvider<Developper>();

                // Connect the table to the data provider.
                dataProvider.addDataDisplay(table);

                // ajout des développeurs à la table
                final List<Developper> list = dataProvider.getList();
                for (final Developper dev : response) {
                    list.add(dev);
                }
                vert.add(table);

                // lorsqu'on clique sur le bouton pour ajouter un développeur
                final Button addDevelopper = new Button("Ajouter un développeur");
                addDevelopper.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(final ClickEvent event) {
                        // affichage d'une popup pour ajouter un développeur
                        final DialogBox box = new DialogBoxDeveloppeur();
                        box.show();
                        box.addCloseHandler(new CloseHandler<PopupPanel>() {

                            @Override
                            public void onClose(final CloseEvent<PopupPanel> event) {
                                // à la fermeture on rafraichit les informations
                                // des développeurs
                                refreshDeveloppeur();
                            }
                        });
                    }
                });
                vert.add(addDevelopper);

            }
        });
        RootPanel.get("content").add(vert);
    }
}
