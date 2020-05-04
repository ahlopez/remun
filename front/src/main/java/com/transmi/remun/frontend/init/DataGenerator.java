package com.transmi.remun.frontend.init;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.transmi.remun.backend.data.entity.Contract;
import com.transmi.remun.backend.data.entity.Contractor;
import com.transmi.remun.backend.data.entity.HistoryItem;
import com.transmi.remun.backend.data.entity.Parm;
import com.transmi.remun.backend.data.entity.User;
import com.transmi.remun.backend.data.repositories.ContractRepository;
import com.transmi.remun.backend.data.repositories.UserRepository;
import com.transmi.remun.service.util.ContractStatus;
import com.transmi.remun.service.util.HasLogger;
import com.transmi.remun.service.util.ParmType;
import com.transmi.remun.service.util.Role;
import com.transmi.remun.service.util.TransmiPhase;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
public class DataGenerator implements HasLogger
{

  private static final ParmType[] TYPE = new ParmType[] { ParmType.NUMBER, ParmType.STRING, ParmType.DATE };

  private static final TransmiPhase[] PHASE = new TransmiPhase[] { TransmiPhase.I, TransmiPhase.II, TransmiPhase.III, TransmiPhase.IV, TransmiPhase.G, TransmiPhase.G };

  private static final String[] FIRST_NAME = new String[] {
      "Olga",
      "Cecilia",
      "Octavia",
      "Laura",
      "Lucía",
      "Josefina",
      "Gerson",
      "David",
      "Arsenio",
      "Santiago",
      "Cristian",
      "Silvia",
      "Vladimir",
      "Patricia",
      "Estela",
      "Fernando",
      "Guillermo",
      "Alvaro",
      "Hernando" };

  private static final String[] LAST_NAME = new String[] {
      "Carter",
      "Castro",
      "Rodriguez",
      "Fernandez",
      "Chaves",
      "Parias",
      "Lopez",
      "Mogollon",
      "Forero",
      "Cortes",
      "Nunez",
      "Macias",
      "Gallegos",
      "Bogota",
      "Mejia",
      "Melo",
      "Duque",
      "Uribe",
      "Petro",
      "Chen",
      "Macias",
      "Puentes",
      "Gomez",
      "Agudelo",
      "Correal",
      "Alvarez",
      "Boada",
      "Camacho",
      "Gutierrez",
      "Munevar",
      "Obregon",
      "Zapata",
      "Cubides",
      "Terreros" };

  private static final String[] CONTRACTOR_NAME = new String[] {
      "Express del Futuro",
      "ETIB",
      "Consorcio Express",
      "Contratista-4",
      "Contratista-5",
      "Contratista-6",
      "Contratista-7",
      "Contratista-8",
      "Contratista-9",
      "Contratista-10" };

  private final Random random = new Random(1L);
  private int nUser = 0;
  private int nHist = 0;

  private ContractRepository contractRepository;
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public DataGenerator(
      ContractRepository contractRepository, UserRepository userRepository, PasswordEncoder passwordEncoder
  )
  {

    this.contractRepository = contractRepository;
    this.userRepository     = userRepository;
    this.passwordEncoder    = passwordEncoder;
  }// DataGenerator

  @PostConstruct
  public void loadData() {
    if (userRepository.count() != 0L)
    {
      getLogger().info("Usa la base de datos existente");
      return;
    }

    getLogger().info("Genera datos demo");

    getLogger().info("... genera usuarios");
    User baker   = createBaker(userRepository, passwordEncoder);
    User barista = createBarista(userRepository, passwordEncoder);
    User admin   = createAdmin(userRepository, passwordEncoder);
    createDeletableUsers(userRepository, passwordEncoder);

    getLogger().info("... genera contratos");
    createContracts(contractRepository, barista, baker, admin);

    getLogger().info("Genero datos demo");
  }// loadData

  private <T> T getRandom(T[] array) { return array[random.nextInt(array.length)]; }

  private String getRandomPhone() { return "+57-310-" + String.format("%07d", random.nextInt(10000000)); }

  // -------------------------- Contracts --------------------------------

  private void createContracts(ContractRepository contractRepo, User barista, User baker, User admin) {

    // Cree contrato global
    Contractor contractor = createContractor("GLOBAL");
    Contract   contract   = createContract(contractor, "GLOBAL", admin, admin);
    contractRepo.save(contract);

    // Cree los demas contratos
    for (int i = 1; i < 21; i++ )
    {
      contractor = createContractor("C" + i);
      contract   = createContract(contractor, "C" + i, barista, baker);
      contractRepo.save(contract);
    }
  }// createContracts

  private Contract createContract(Contractor contractor, String code, User barista, User baker) {

    Contract contract = new Contract(contractor, barista);

    LocalDate now      = LocalDate.now();
    LocalDate fromDate = now.minusYears(20 - random.nextInt(15));
    LocalDate toDate   = fromDate.plusYears(16);

    contract.setCode(code);
    contract.setName(getRandom(FIRST_NAME) + " " + getRandom(LAST_NAME));
    contract.setFase(getRandom(PHASE));
    contract.setFromDate(fromDate);
    contract.setToDate(toDate);
    contract.setStatus(ContractStatus.VIGENTE);
    contract.setParms(createParms(contract, random.nextInt(10)));
    contract.setHistory(createContractHistory(contract, barista, baker));

    getLogger().info("))) Creo " + contract.toString());
    return contract;
  }// createContract

  // ----------------------------- History --------------------------------

  private List<HistoryItem> createContractHistory(Contract contract, User barista, User baker) {
    ArrayList<HistoryItem> history = new ArrayList<>();

    HistoryItem item = new HistoryItem(barista, "Contrato creado");
    item.setCode("H" + ( ++nHist));
    item.setNewState(ContractStatus.VIGENTE);
    LocalDateTime d = contract.getFromDate().atStartOfDay();
    item.setTimestamp(d);
    item.setCreatedBy(baker);
    history.add(item);

    for (int i = 0; i < 5; i++ )
    {
      item = new HistoryItem(barista, "History " + i);
      item.setCode("H" + ( ++nHist));
      item.setNewState(ContractStatus.VIGENTE);
      d = d.plusYears(random.nextInt(2));
      item.setTimestamp(d);
      item.setCreatedBy(baker);
      getLogger().info("))) Creo " + item.toString());
      history.add(item);
    }

    return history;
  }// createContractHistory

  // --------------------------- Parms -----------------------------

  private List<Parm> createParms(Contract contract, int nParms) {
    Set<Parm> parms = new TreeSet<>();

    for (int i = 1; i <= nParms; i++ )
    {

      Parm parm = new Parm();
      parm.setCode(contract.getCode() + ".P" + i);
      parm.setName("Parametro " + i);
      parm.setDateFrom(contract.getFromDate());
      parm.setDateTo(contract.getToDate().minusYears(random.nextInt(15)));
      parm.setType(getRandom(TYPE));
      parm.setComment("");
      parm.setValue(String.valueOf(random.nextDouble()));

      getLogger().info("))) Creo " + parm.toString());
      parms.add(parm);
    }

    return new ArrayList<Parm>(parms);
  }// createParms

  // --------------------------- Contractors ----------------------

  private Contractor createContractor(String code) {
    Contractor contractor = new Contractor();
    String     name       = getRandom(CONTRACTOR_NAME);
    contractor.setCode(code);
    contractor.setFullName(name);
    contractor.setPhoneNumber(getRandomPhone());
    if (random.nextInt(3) == 0)
      contractor.setDetails("Contratista VIP");

    getLogger().info("))) Creo " + contractor.toString());
    return contractor;
  }// createContractor

  // --------------------------------- Users ----------------------------------------

  private User createBaker(UserRepository userRepository, PasswordEncoder passwordEncoder) { return userRepository.save(
      createUser("baker@vaadin.com", "Pedro", "Perez", passwordEncoder.encode("baker"), Role.LIQUIDADOR, false)
  ); }

  private User createBarista(UserRepository userRepository, PasswordEncoder passwordEncoder) { return userRepository.save(
      createUser("barista@vaadin.com", "Pacho", "Riarte", passwordEncoder.encode("barista"), Role.REVISOR, true)
  ); }

  private User createAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) { return userRepository.save(
      createUser("admin@vaadin.com", "Claudia", "Lopez", passwordEncoder.encode("admin"), Role.ADMIN, true)
  ); }

  private void createDeletableUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    userRepository.save(
        createUser("peter@vaadin.com", "Peter", "Bush", passwordEncoder.encode("peter"), Role.LIQUIDADOR, false)
    );
    userRepository
        .save(createUser("mary@vaadin.com", "Mary", "Ocon", passwordEncoder.encode("mary"), Role.LIQUIDADOR, true));
  }

  private User createUser(String email, String firstName, String lastName, String passwordHash, String role, boolean locked) {
    User user = new User();
    user.setCode("U" + ( ++nUser));
    user.setEmail(email);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setPasswordHash(passwordHash);
    user.setRole(role);
    user.setLocked(locked);
    getLogger().info("))) Creo " + user.toString());
    return user;
  }// createUser

}// DataGenerator
