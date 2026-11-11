package com.example.application.views;

import com.example.application.data.model.Company;
import com.example.application.data.model.Contact;
import com.example.application.data.model.Status;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

// ① ContactForm extends FormLayout: a responsive layout that shows form fields in one or
//    two columns, depending on the viewport width
public class ContactForm extends FormLayout {

    // ② Creates all the UI components as fields in the component.
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    EmailField email = new EmailField("Email");
    ComboBox<Status> status = new ComboBox<>("Status");
    ComboBox<Company> company = new ComboBox<>("Company");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");


    /*
    - Istanzionazione del Binder e data binding con i campi del form
    - BeanValidationBinder è un Binder che riconosce le annotazioni di Bean Validation.
      Passandogli Contact.class, definisco il tipo di oggetto a cui sto effettuando il binding.
     */
    Binder<Contact> binder = new BeanValidationBinder<>(Contact.class);


    public ContactForm(List<Company> companies, List<Status> statuses) {

        addClassName("contact-form");
        /* bindInstanceFields() associa i campi in Contact e ContactForm in base ai loro nomi. */
        binder.bindInstanceFields(this);

        company.setItems(companies);
        company.setItemLabelGenerator(Company::getName);
        status.setItems(statuses);
        status.setItemLabelGenerator(Status::getName);

        // ④-a) Adds all the UI components to the layout.
        // ④-c)  The buttons require a bit of extra configuration = call createButtonsLayout()
        add(firstName,
                lastName,
                email,
                company,
                status,
                createButtonsLayout());
    }


    private HorizontalLayout createButtonsLayout() {


        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);


        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        /// Adds click listeners to the buttons that fire the corresponding events.
        save.addClickListener(event -> validateAndSave()); /// ①
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean()))); /// ②
        close.addClickListener(event -> fireEvent(new CloseEvent(this))); /// ③
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid())); /// ④

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean())); /// (5)
        }
    }

    /* Chiama binder.setBean() per associare i valori del contatto ai campi dell'interfaccia utente.
       Il metodo aggiunge inoltre dei listener di variazione del valore, in modo che le modifiche
       apportate nell'interfaccia utente vengano riportate nell'oggetto di dominio.
    */
    public void setContact(Contact contact) {
        binder.setBean(contact);
    }


    /// SEZIONE DEGLI EVENTI
    // Events
    public static abstract class ContactFormEvent extends ComponentEvent<ContactForm> {
        private Contact contact;
        protected ContactFormEvent(ContactForm source, Contact contact) { /// (1)
            super(source, false);
            this.contact = contact;
        }
        public Contact getContact() {
            return contact;
        }
    }
    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(ContactForm source, Contact contact) {
            super(source, contact);
        }
    }
    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(ContactForm source, Contact contact) {
            super(source, contact);
        }
    }
    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(ContactForm source) {
            super(source, null);
        }
    }
    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener)
    { /// (2)
        return addListener(DeleteEvent.class, listener);
    }
    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

}














