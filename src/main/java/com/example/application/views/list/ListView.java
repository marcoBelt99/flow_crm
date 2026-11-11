package com.example.application.views.list;

import com.example.application.data.model.Contact;
import com.example.application.services.CrmService;
import com.example.application.views.ContactForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "")
@PageTitle("Contacts | Vaadin CRM")
public class ListView extends VerticalLayout { // ① places all child components vertically

    Grid<Contact> grid = new Grid<>(Contact.class); // ② Grid component is typed with Contact
    TextField filterText = new TextField();

    // ****
    // a) Aggiunta componente form custom
    //    Creates a reference to the form so you have access to it from other methods.
    // ****
    ContactForm form;


    CrmService crmService;

    /* Nel costruttore dichiaro come voglio il layout */
    public ListView(CrmService crmService) { // (1) Il costruttore riceve come parametro il servizio che gestisce i dati

        // Inietto il servizio
        this.crmService = crmService;

        addClassName("list-view");
        setSizeFull();

        // Configurazione dei componenti della view
        configureGrid();
        configureForm();

        // Aggiungo i componenti alla view
        add( getToolbar(), getContent() );

        // Aggiorno la lista dei contatti
        updateList();

        
        /// Il codice che segue è solo per testare il layout e non fa parte della view finale
        //Inserisco un bottone e un datepicker in un layout orizzontale
        Button button = new Button("Sono un bottone");
        HorizontalLayout layout = new HorizontalLayout( button, new DatePicker("Scegli una data") );

        button.addClickListener(clickEvent -> add( new Text("cliccato")));
        layout.setDefaultVerticalComponentAlignment(Alignment.END);
        add(layout);
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);

        content.setFlexGrow(2, grid); //④
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }


    private void configureForm() {
        form = new ContactForm( crmService.findAllCompanies(), crmService.findAllStatuses() );
        form.setWidth("25em");
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid"); // ⑩
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "email"); // ⑤ proprietà della classe Contact che vogliamo visualizzare come colonne
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status"
        ); // ⑥ definisco colonne custom per gli oggetti associati (Status e Company) che non sono attributi diretti della classe Contact
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
                grid.getColumns().forEach(col -> col.setAutoWidth(true)); // ⑦ configuro la larghezza delle colonne in modo che si adatti al contenuto
    }
    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY); // ⑧ configuro il campo di testo per aggiornare il filtro solo quando l'utente smette di digitare.
                                                             //    In questo modo evitiamo di eseguire troppe query al database mentre l'utente sta digitando.

        filterText.addValueChangeListener(e -> updateList()); // ⑧ aggiungo un listener per aggiornare la lista dei contatti quando il filtro cambia
        Button addContactButton = new Button("Add contact");
        var toolbar = new HorizontalLayout(filterText, addContactButton); // ⑨ la toolbar è un layout orizzontale che contiene il campo di testo e il pulsante l'uno accanto all'altro
        toolbar.addClassName("toolbar"); // ⑩
        return toolbar;
    }

    private void updateList() {
        grid.setItems(crmService.findAllContacts(filterText.getValue()));
    }
}