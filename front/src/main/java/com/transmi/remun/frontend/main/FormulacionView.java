package com.transmi.remun.frontend.main;

import static com.transmi.remun.service.util.FrontConst.PAGE_FORMULACION;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.transmi.remun.backend.data.entity.User;
import com.transmi.remun.backend.service.UserService;
import com.transmi.remun.frontend.crud.AbstractRemunCrudView;
import com.transmi.remun.frontend.security.CurrentUser;
import com.transmi.remun.service.util.FrontConst;
import com.transmi.remun.service.util.Role;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = PAGE_FORMULACION, layout = MainView.class)
@PageTitle(FrontConst.TITLE_FORMULACION)
@Secured(Role.ADMIN)
public class FormulacionView extends AbstractRemunCrudView<User>
{

  @Autowired
  public FormulacionView(UserService service, CurrentUser currentUser, PasswordEncoder passwordEncoder)
  { super(User.class, service, new Grid<>(), createForm(passwordEncoder), currentUser); }

  @Override
  public void setupGrid(Grid<User> grid) {
    grid.addColumn(User::getEmail).setWidth("270px").setHeader("Email").setFlexGrow(5);
    grid.addColumn(u-> u.getFirstName() + " " + u.getLastName()).setHeader("Nombre").setWidth("200px").setFlexGrow(5);
    grid.addColumn(User::getRole).setHeader("Rol").setWidth("150px");
  }

  @Override
  protected String getBasePage() { return PAGE_FORMULACION; }

  private static BinderCrudEditor<User> createForm(PasswordEncoder passwordEncoder) {

    Notification.show("Saludos desde Formulación!!");

    EmailField email = new EmailField("Email (login)");
    email.getElement().setAttribute("colspan", "2");
    TextField     first    = new TextField("Nombre");
    TextField     last     = new TextField("Apellido");
    PasswordField password = new PasswordField("Password");
    password.getElement().setAttribute("colspan", "2");
    ComboBox<String> role = new ComboBox<>();
    role.getElement().setAttribute("colspan", "2");
    role.setLabel("Rol");

    FormLayout form = new FormLayout(email, first, last, password, role);

    BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);

    ListDataProvider<String> roleProvider = DataProvider.ofItems(Role.getAllRoles());
    role.setItemLabelGenerator(
        s-> s != null ?
            s :
            ""
    );
    role.setDataProvider(roleProvider);

    binder.bind(first, "firstName");
    binder.bind(last, "lastName");
    binder.bind(email, "email");
    binder.bind(role, "role");

    binder.forField(password)
        .withValidator(pass-> pass.matches("^(|(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,})$"),
            "need 6 or more chars, mixing digits, lowercase and uppercase letters"
        )
        .bind(
            user-> password.getEmptyValue(), (
              user, pass)->
              {
                if (!password.getEmptyValue().equals(pass))
                {
                  user.setPasswordHash(passwordEncoder.encode(pass));
                }

              }
        );

    return new BinderCrudEditor<User>(binder, form);
  }// BinderCrudEditor

}// FormulacionView
