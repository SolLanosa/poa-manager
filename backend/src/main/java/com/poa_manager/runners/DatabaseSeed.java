// https://medium.com/@sudhikshapirai/beyond-sql-scripts-mastering-custom-seeders-in-spring-boot-b4903ee32a41

package com.poa_manager.runners;

import com.poa_manager.entities.*;
import com.poa_manager.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.poa_manager.entities.FacultyAction.*;
import static com.poa_manager.entities.FacultyObject.*;

@Component
@Order(1)
public class DatabaseSeed implements CommandLineRunner {
  private static final String USD = "USD";
  // we use this to create the faculty requiments thresholds.
  // The POAs have different signature requirements per money threshdold.
  // We pass one value and do min = value and another faculty max = value - CENT
  private static final BigDecimal CENT = new BigDecimal("0.01");

  private final CompanyRepository companyRepo;
  private final PersonRepository personRepo;
  private final PowerOfAttorneyRepository poaRepo;
  private final PowerOfAttorneyGroupRepository groupRepo;
  private final PowerOfAttorneyGroupMemberRepository memberRepo;
  private final PowerOfAttorneyFacultyRepository facultyRepo;
  private final FacultyScopeItemRepository scopeRepo;
  private final PowerOfAttorneySigningRuleRepository ruleRepo;
  private final PowerOfAttorneySigningRuleRequirementRepository reqRepo;
  private final CompanyMemberRepository companyMemberRepo;

  public DatabaseSeed(CompanyRepository companyRepo,
      PersonRepository personRepo,
      PowerOfAttorneyRepository poaRepo,
      PowerOfAttorneyGroupRepository groupRepo,
      PowerOfAttorneyGroupMemberRepository memberRepo,
      PowerOfAttorneyFacultyRepository facultyRepo,
      FacultyScopeItemRepository scopeRepo,
      PowerOfAttorneySigningRuleRepository ruleRepo,
      PowerOfAttorneySigningRuleRequirementRepository reqRepo,
      CompanyMemberRepository companyMemberRepo) {
    this.companyRepo = companyRepo;
    this.personRepo = personRepo;
    this.poaRepo = poaRepo;
    this.groupRepo = groupRepo;
    this.memberRepo = memberRepo;
    this.facultyRepo = facultyRepo;
    this.scopeRepo = scopeRepo;
    this.ruleRepo = ruleRepo;
    this.reqRepo = reqRepo;
    this.companyMemberRepo = companyMemberRepo;
  }

  @Override
  @Transactional
  public void run(String... args) {
    // this runs every time we start the app so we checked if we already have data
    // in the DB, and skip it
    if (companyRepo.count() > 0) {
      return;
    }
    seedAurora();
    seedDelta();
  }

  private void seedAurora() {
    Company aurora = saveCompany("Aurora Energia Renovable SA", "SA");
    Person ferreyra = savePerson("Gustavo Daniel", "Ferreyra", "23456789");
    saveCompanyMember(aurora, ferreyra);
    Person sanchez = savePerson("Laura Beatriz", "Sanchez", "24567810");
    saveCompanyMember(aurora, sanchez);
    Person rios = savePerson("Sebastian Emilio", "Rios", "27678921");
    saveCompanyMember(aurora, rios);
    Person molina = savePerson("Veronica Andrea", "Molina", "28789032");
    saveCompanyMember(aurora, molina);
    Person dominguez = savePerson("Pablo Hernan", "Dominguez", "29890143");
    saveCompanyMember(aurora, dominguez);
    Person guzman = savePerson("Natalia Soledad", "Guzman", "30901254");
    saveCompanyMember(aurora, guzman);
    Person ibanez = savePerson("Facundo Martin", "Ibañez", "31012365");
    saveCompanyMember(aurora, ibanez);
    Person acosta = savePerson("Maria Florencia", "Acosta", "32123476");
    saveCompanyMember(aurora, acosta);
    Person nunez = savePerson("Carolina Isabel", "Nuñez", "26345698");
    saveCompanyMember(aurora, nunez);
    Person cabrera = savePerson("Diego Alejandro", "Cabrera", "25234587");
    saveCompanyMember(aurora, cabrera);

    PowerOfAttorney bankingPOA = savePoa(aurora,
        toDate(2024, 11, 14), toDate(2024, 12, 9), toDate(2027, 12, 31),
        toDate(2026, 6, 17));

    PowerOfAttorneyGroup bankingGroupA = saveGroup(bankingPOA, "Group A");
    PowerOfAttorneyGroup bankingGroupB = saveGroup(bankingPOA, "Group B");
    PowerOfAttorneyGroup bankingGroupC = saveGroup(bankingPOA, "Group C");
    PowerOfAttorneyGroup bankingGroupD = saveGroup(bankingPOA, "Group D");
    saveMembers(bankingGroupA, ferreyra, sanchez);
    saveMembers(bankingGroupB, rios, molina);
    saveMembers(bankingGroupC, dominguez, guzman);
    saveMembers(bankingGroupD, ibanez, acosta);

    for (PowerOfAttorneyFaculty f : saveBankingFaculties(bankingPOA)) {
      saveBankingRules(f, new BigDecimal("1500000.00"), bankingGroupA, bankingGroupB, bankingGroupC,
          bankingGroupD);
    }

    PowerOfAttorney adminPOA = savePoa(aurora,
        toDate(2024, 11, 14), toDate(2024, 12, 9), toDate(2028, 6, 30), null);

    PowerOfAttorneyGroup adminGroupA = saveGroup(adminPOA, "Group A");
    PowerOfAttorneyGroup adminGroupB = saveGroup(adminPOA, "Group B");
    PowerOfAttorneyGroup adminGroupC = saveGroup(adminPOA, "Group C");
    saveMembers(adminGroupA, ferreyra, sanchez);
    saveMembers(adminGroupB, rios, molina);
    saveMembers(adminGroupC, dominguez, guzman);

    for (PowerOfAttorneyFaculty f : saveAdministrationFaculties(adminPOA)) {
      adminRules(f, new BigDecimal("8000000.00"), new BigDecimal("1000000.00"), adminGroupA,
          adminGroupB,
          adminGroupC);
    }

    List<FacultyAction> turbineActions = List.of(DISPOSE, ENCUMBER, ASSIGN);
    for (FacultyAction action : turbineActions) {
      PowerOfAttorneyFaculty turbine = saveFaculty(adminPOA, action, ENERGY_EQUIPMENT,
          "Aerogeneradores parque eolico vientos del sur");
      for (int i = 1; i <= 12; i++) {
        String ref = String.format("AE-%02d", i);
        saveScopeItem(turbine, ref,
            "Aerogenerador " + ref);
      }
      saveRule(turbine, null, null, USD, adminGroupA, adminGroupA);
    }

    List<FacultyObject> taxObjects = List.of(TAX_OBLIGATION, FEES);
    for (FacultyObject object : taxObjects) {
      PowerOfAttorneyFaculty t = saveFaculty(adminPOA, PAY, object,
          "Pago de tributos hasta USD 1000000");
      saveRule(t, null, new BigDecimal("1000000.00"), USD, adminGroupC);
    }

    PowerOfAttorney filingsPOA = savePoa(aurora,
        toDate(2024, 11, 14), toDate(2025, 1, 20), toDate(2027, 12, 31), null);
    PowerOfAttorneyGroup filingsGroup = saveGroup(filingsPOA, "Apoderados");
    saveMembers(filingsGroup, nunez, guzman);
    for (PowerOfAttorneyFaculty f : saveAdministrativeFilingFaculties(filingsPOA)) {
      saveRule(f, null, null, USD, filingsGroup);
    }

    PowerOfAttorney customsPOA = savePoa(aurora,
        toDate(2024, 11, 14), toDate(2025, 1, 20), toDate(2027, 12, 31), null);
    PowerOfAttorneyGroup customsGroup = saveGroup(customsPOA, "Apoderados");
    saveMembers(customsGroup, nunez, dominguez);
    for (PowerOfAttorneyFaculty f : saveCustomsFaculties(customsPOA)) {
      saveRule(f, null, null, USD, customsGroup);
    }

    PowerOfAttorney laborPOA = savePoa(aurora,
        toDate(2024, 11, 14), toDate(2025, 2, 18), toDate(2027, 12, 31), null);
    PowerOfAttorneyGroup laborGroup = saveGroup(laborPOA, "Apoderados");
    saveMembers(laborGroup, cabrera, nunez);
    for (PowerOfAttorneyFaculty f : saveLaborFaculties(laborPOA)) {
      saveRule(f, null, null, USD, laborGroup);
    }

    PowerOfAttorney restrictedPOA = savePoa(aurora,
        toDate(2026, 7, 20), toDate(2026, 8, 12), toDate(2028, 6, 30), null);
    PowerOfAttorneyGroup restrictedGroupA = saveGroup(restrictedPOA, "Group A");
    PowerOfAttorneyGroup restrictedGroupB = saveGroup(restrictedPOA, "Group B");
    saveMembers(restrictedGroupA, ferreyra, sanchez);
    saveMembers(restrictedGroupB, rios, molina);

    for (PowerOfAttorneyFaculty f : saveRestrictedBankingFaculties(restrictedPOA)) {
      saveScopeItem(f, "0034-9981", "Banco Galicia: cuenta corriente en pesos N0034-9981");
      saveScopeItem(f, "0182-4457", "Banco Santander: caja de ahorro en USD N0182-4457");
      saveRule(f, null, new BigDecimal("250000.00"), USD, restrictedGroupA,
          restrictedGroupB);
    }
  }

  private void seedDelta() {
    Company delta = saveCompany("Delta Logistica Integral SA", "SA");

    Person vega = savePerson("Alejandro Nicolas", "Vega", "23987654");
    saveCompanyMember(delta, vega);
    Person ramos = savePerson("Silvina Patricia", "Ramos", "24876501");
    saveCompanyMember(delta, ramos);
    Person herrera = savePerson("Matias Ezequiel", "Herrera", "27765412");
    saveCompanyMember(delta, herrera);
    Person castro = savePerson("Gabriela Noemi", "Castro", "28654323");
    saveCompanyMember(delta, castro);
    Person medina = savePerson("Leandro Javier", "Medina", "29543234");
    saveCompanyMember(delta, medina);
    Person paz = savePerson("Romina Alejandra", "Paz", "30432145");
    saveCompanyMember(delta, paz);
    Person luna = savePerson("Nicolas Adrian", "Luna", "31321056");
    saveCompanyMember(delta, luna);
    Person ledesma = savePerson("Julieta Antonella", "Ledesma", "32210987");
    saveCompanyMember(delta, ledesma);
    Person aguirre = savePerson("Valeria Cristina", "Aguirre", "26098765");
    saveCompanyMember(delta, aguirre);
    Person correa = savePerson("Fernando Ariel", "Correa", "25109876");
    saveCompanyMember(delta, correa);

    PowerOfAttorney bankingPOA = savePoa(delta,
        toDate(2025, 3, 3), toDate(2025, 3, 28), toDate(2026, 12, 31), null);

    PowerOfAttorneyGroup bankingGroupA = saveGroup(bankingPOA, "Group A");
    PowerOfAttorneyGroup bankingGroupB = saveGroup(bankingPOA, "Group B");
    PowerOfAttorneyGroup bankingGroupC = saveGroup(bankingPOA, "Group C");
    PowerOfAttorneyGroup bankingGroupD = saveGroup(bankingPOA, "Group D");
    saveMembers(bankingGroupA, vega, ramos);
    saveMembers(bankingGroupB, herrera, castro);
    saveMembers(bankingGroupC, medina, paz);
    saveMembers(bankingGroupD, luna, ledesma);

    for (PowerOfAttorneyFaculty f : saveBankingFaculties(bankingPOA)) {
      saveBankingRules(f, new BigDecimal("750000.00"), bankingGroupA, bankingGroupB, bankingGroupC,
          bankingGroupD);
    }

    PowerOfAttorneyFaculty taxPay = saveFaculty(bankingPOA, PAY, TAX_OBLIGATION,
        "Pago de tributos hasta USD300000");
    saveRule(taxPay, null, new BigDecimal("300000.00"), USD, bankingGroupC, bankingGroupD);

    PowerOfAttorney adminPOA = savePoa(delta,
        toDate(2025, 3, 3), toDate(2025, 3, 28), toDate(2027, 12, 31), null);

    PowerOfAttorneyGroup adminGroupA = saveGroup(adminPOA, "Group A");
    PowerOfAttorneyGroup adminGroupB = saveGroup(adminPOA, "Group B");
    PowerOfAttorneyGroup adminGroupC = saveGroup(adminPOA, "Group C");
    saveMembers(adminGroupA, vega, ramos);
    saveMembers(adminGroupB, herrera, castro);
    saveMembers(adminGroupC, medina, paz);

    for (PowerOfAttorneyFaculty f : saveAdministrationFaculties(adminPOA)) {
      adminRules(f, new BigDecimal("3000000.00"), new BigDecimal("300000.00"), adminGroupA,
          adminGroupB,
          adminGroupC);
    }

    PowerOfAttorneyFaculty flota = saveFaculty(adminPOA, DISPOSE, VEHICLES,
        "Enajenacion de unidades de la flota");
    saveScopeItem(flota, "AE123BC", "Flota AE123BC");
    saveScopeItem(flota, "AF456DE", "Flota AF456DE");
    saveScopeItem(flota, "AG789FG", "Flota AG789FG");
    saveRule(flota, null, null, USD, adminGroupA, adminGroupA);

    List<FacultyAction> warehouseActions = List.of(LEASE_IN, LEASE_OUT, ASSIGN);
    for (FacultyAction action : warehouseActions) {
      PowerOfAttorneyFaculty wh = saveFaculty(adminPOA, action, REAL_ESTATE,
          "Deposito de Garin");
      saveScopeItem(wh, "052-14-7788",
          "Deposito Ruta Panamericana Km 34,5, Garin — Nom. Catastral 052-14-7788");
      saveRule(wh, null, null, USD, adminGroupB, adminGroupC);
    }

    PowerOfAttorney filingsPOA = savePoa(delta,
        toDate(2025, 3, 3), toDate(2025, 4, 15), toDate(2027, 12, 31), null);
    PowerOfAttorneyGroup filingsGroup = saveGroup(filingsPOA, "Apoderados");
    saveMembers(filingsGroup, aguirre, paz);
    for (PowerOfAttorneyFaculty f : saveAdministrativeFilingFaculties(filingsPOA)) {
      saveRule(f, null, null, USD, filingsGroup);
    }

    PowerOfAttorney customsPOA = savePoa(delta, toDate(2025, 3, 3), toDate(2025, 4, 15),
        toDate(2027, 12, 31),
        null);
    PowerOfAttorneyGroup customsGroup = saveGroup(customsPOA, "Apoderados");
    saveMembers(customsGroup, aguirre, medina);
    for (PowerOfAttorneyFaculty f : saveCustomsFaculties(customsPOA)) {
      saveRule(f, null, null, USD, customsGroup);
    }

    PowerOfAttorney laborPOA = savePoa(delta, toDate(2025, 3, 3), toDate(2025, 5, 7), toDate(2027, 12, 31),
        null);
    PowerOfAttorneyGroup laborGroup = saveGroup(laborPOA, "Apoderados");
    saveMembers(laborGroup, correa, aguirre);
    for (PowerOfAttorneyFaculty f : saveLaborFaculties(laborPOA)) {
      saveRule(f, null, null, USD, laborGroup);
    }

    PowerOfAttorney transportPOA = savePoa(delta, toDate(2026, 2, 11), toDate(2026, 3, 5),
        toDate(2028, 12, 31),
        null);
    PowerOfAttorneyGroup transportGroup = saveGroup(transportPOA, "Apoderados");
    saveMembers(transportGroup, luna, correa);
    for (PowerOfAttorneyFaculty f : saveTransportFaculties(transportPOA)) {
      saveRule(f, null, null, USD, transportGroup);
    }
  }

  private void saveBankingRules(PowerOfAttorneyFaculty f, BigDecimal threshold, PowerOfAttorneyGroup a,
      PowerOfAttorneyGroup b, PowerOfAttorneyGroup c, PowerOfAttorneyGroup d) {
    BigDecimal below = threshold.subtract(CENT);

    saveRule(f, threshold, null, USD, a, a);
    saveRule(f, threshold, null, USD, a, b);
    saveRule(f, threshold, null, USD, a, c);
    saveRule(f, threshold, null, USD, a, d);

    saveRule(f, null, below, USD, a, a);
    saveRule(f, null, below, USD, a, b);
    saveRule(f, null, below, USD, a, c);
    saveRule(f, null, below, USD, a, d);
    saveRule(f, null, below, USD, b, b);
    saveRule(f, null, below, USD, b, c);
    saveRule(f, null, below, USD, b, d);
  }

  private void adminRules(PowerOfAttorneyFaculty f, BigDecimal high, BigDecimal mid, PowerOfAttorneyGroup a,
      PowerOfAttorneyGroup b, PowerOfAttorneyGroup c) {
    BigDecimal belowHigh = high.subtract(CENT);
    BigDecimal belowMid = mid.subtract(CENT);

    saveRule(f, high, null, USD, a, a);
    saveRule(f, high, null, USD, a, b);

    saveRule(f, mid, belowHigh, USD, a, a);
    saveRule(f, mid, belowHigh, USD, a, b);
    saveRule(f, mid, belowHigh, USD, b, b);
    saveRule(f, mid, belowHigh, USD, b, c);

    saveRule(f, null, belowMid, USD, a, a);
    saveRule(f, null, belowMid, USD, a, b);
    saveRule(f, null, belowMid, USD, a, c);
    saveRule(f, null, belowMid, USD, b, b);
    saveRule(f, null, belowMid, USD, b, c);
    saveRule(f, null, belowMid, USD, c, c);
  }

  private List<PowerOfAttorneyFaculty> saveBankingFaculties(PowerOfAttorney poa) {
    List<PowerOfAttorneyFaculty> faculties = new ArrayList<>();
    faculties.add(saveFaculty(poa, OPEN, BANK_ACCOUNT, "Abrir cuentas bancarias (de todo tipo)"));
    faculties.add(saveFaculty(poa, CLOSE, BANK_ACCOUNT, "Cerrar cuentas bancarias (de todo tipo)"));
    faculties.add(saveFaculty(poa, OPEN, SAFE_DEPOSIT_BOX, "Abrir cajas de seguridad"));
    faculties.add(saveFaculty(poa, CLOSE, SAFE_DEPOSIT_BOX, "Cerrar cajas de seguridad"));
    faculties.add(saveFaculty(poa, REQUEST, CHECKBOOK, "Solicitar libretas de cheques"));
    faculties.add(saveFaculty(poa, RECEIVE, CHECKBOOK, "Recibir libretas de cheques"));
    faculties.add(saveFaculty(poa, ISSUE, CHECK, "Librar cheques contra fondos o en descubierto"));
    faculties.add(saveFaculty(poa, SIGN, CHECK, "Firmar cheques"));
    faculties.add(saveFaculty(poa, ENDORSE, CHECK, "Endosar cheques"));
    faculties.add(saveFaculty(poa, ACCEPT, CHECK, "Aceptar cheques"));
    faculties.add(saveFaculty(poa, ISSUE, PAYMENT_ORDER, "Librar ordenes de pago y giros"));
    faculties.add(saveFaculty(poa, ISSUE, BILL_OF_EXCHANGE, "Librar letras de cambio"));
    faculties.add(saveFaculty(poa, ISSUE, PROMISSORY_NOTE, "Librar y firmar pagarés"));
    faculties.add(saveFaculty(poa, RENEW, BILL_OF_EXCHANGE, "Renovar letras de cambio"));
    faculties.add(saveFaculty(poa, AMORTIZE, BILL_OF_EXCHANGE, "Amortizar letras de cambio"));
    faculties.add(saveFaculty(poa, CANCEL, BILL_OF_EXCHANGE, "Cancelar letras de cambio y cheques"));
    faculties.add(saveFaculty(poa, OVERDRAW, BANK_ACCOUNT, "Girar en descubierto autorizado"));
    faculties.add(saveFaculty(poa, WITHDRAW, FUNDS, "Retirar fondos"));
    faculties.add(saveFaculty(poa, COLLECT, FUNDS, "Cobrar fondos"));
    faculties.add(saveFaculty(poa, RECEIVE, FUNDS, "Percibir fondos"));
    faculties.add(saveFaculty(poa, DEPOSIT, FUNDS, "Efectuar depositos en efectivo"));
    faculties.add(saveFaculty(poa, DEPOSIT, SECURITIES, "Depositar valores, bonos y acciones"));
    faculties.add(saveFaculty(poa, DEPOSIT, TIME_DEPOSIT, "Constituir depositos a plazo fijo"));
    faculties.add(saveFaculty(poa, TRANSFER, FUNDS, "Transferencias entre cuentas y al exterior"));
    faculties.add(saveFaculty(poa, TRADE, MUTUAL_FUND_UNITS, "Operar con fondos comunes de inversion"));
    faculties.add(saveFaculty(poa, SUBSCRIBE, MUTUAL_FUND_UNITS, "Suscribir cuotapartes de FCI"));
    faculties.add(saveFaculty(poa, REDEEM, MUTUAL_FUND_UNITS, "Solicitar rescate de cuotapartes"));
    faculties.add(saveFaculty(poa, ACQUIRE, SECURITIES, "Comprar acciones y titulos"));
    faculties.add(saveFaculty(poa, DISPOSE, SECURITIES, "Vender acciones y titulos"));
    faculties.add(saveFaculty(poa, ACQUIRE, NEGOTIABLE_OBLIGATIONS, "Comprar obligaciones negociables"));
    faculties.add(saveFaculty(poa, ACQUIRE, TREASURY_BILLS, "Comprar letras de tesoreria"));
    faculties.add(saveFaculty(poa, TRADE, DERIVATIVE_CONTRACT,
        "Pases, cauciones,opciones, forwards y swaps"));
    faculties.add(saveFaculty(poa, ACQUIRE, FOREIGN_CURRENCY, "Comprar moneda extranjera"));
    faculties.add(saveFaculty(poa, DISPOSE, FOREIGN_CURRENCY, "Vender moneda extranjera"));
    faculties.add(saveFaculty(poa, OPERATE_ELECTRONICALLY, BANK_ACCOUNT, "Operar por medios electronicos"));
    faculties.add(saveFaculty(poa, REQUEST, ACCOUNT_STATEMENT, "Solicitar extractos y resumenes"));
    faculties.add(saveFaculty(poa, SIGN, ACCOUNT_STATEMENT, "Firmar informes de cuenta corriente"));
    faculties.add(saveFaculty(poa, REQUEST, LOAN, "Solicitar préstamos comerciales y financieros"));
    faculties.add(saveFaculty(poa, PAY, TAX_OBLIGATION, "Pagar impuestos, tasas y contribuciones"));
    return faculties;
  }

  private List<PowerOfAttorneyFaculty> saveRestrictedBankingFaculties(PowerOfAttorney poa) {
    List<PowerOfAttorneyFaculty> faculties = new ArrayList<>();
    faculties.add(saveFaculty(poa, TRANSFER, FUNDS, "Efectuar transferencias de fondos"));
    faculties.add(saveFaculty(poa, WITHDRAW, FUNDS, "Retirar fondos"));
    faculties.add(saveFaculty(poa, COLLECT, FUNDS, "Cobrar fondos"));
    faculties.add(saveFaculty(poa, RECEIVE, FUNDS, "Percibir fondos"));
    faculties.add(saveFaculty(poa, ISSUE, CHECK, "Librar cheques"));
    faculties.add(saveFaculty(poa, SIGN, CHECK, "Firmar cheques"));
    faculties.add(saveFaculty(poa, ENDORSE, CHECK, "Endosar cheques"));
    faculties.add(saveFaculty(poa, DEPOSIT, FUNDS, "Efectuar depositos"));
    faculties.add(saveFaculty(poa, REQUEST, ACCOUNT_STATEMENT,
        "Solicitar extractos, resumenes e informacion de movimientos"));
    return faculties;
  }

  private List<PowerOfAttorneyFaculty> saveAdministrationFaculties(PowerOfAttorney poa) {
    List<PowerOfAttorneyFaculty> faculties = new ArrayList<>();
    faculties.add(saveFaculty(poa, MANAGE, MOVABLE_ASSETS, "Administrar bienes muebles"));
    faculties.add(saveFaculty(poa, MANAGE, LIVESTOCK, "Administrar semovientes"));
    faculties.add(saveFaculty(poa, REPAIR, MOVABLE_ASSETS, "Reparar y conservar bienes muebles"));
    faculties.add(saveFaculty(poa, PAY, TAX_OBLIGATION, "Pagar impuestos y tasas"));
    faculties.add(saveFaculty(poa, COLLECT, SALARY, "Cobrar haberes y sueldos"));
    faculties.add(saveFaculty(poa, PAY, SALARY, "Pagar haberes y sueldos"));
    faculties.add(saveFaculty(poa, COLLECT, FEES, "Cobrar honorarios"));
    faculties.add(saveFaculty(poa, PAY, FEES, "Pagar honorarios"));
    faculties.add(saveFaculty(poa, COLLECT, RENT, "Cobrar alquileres y arrendamientos"));
    faculties.add(saveFaculty(poa, PAY, RENT, "Pagar alquileres y arrendamientos"));
    faculties.add(saveFaculty(poa, COLLECT, RECEIVABLES, "Cobrar créditos"));
    faculties.add(saveFaculty(poa, ASSIGN, RIGHTS, "Efectuar y aceptar cesiones de derechos"));
    faculties.add(saveFaculty(poa, ASSIGN, RECEIVABLES, "Efectuar y aceptar cesiones de créditos"));
    faculties.add(saveFaculty(poa, ACCEPT, OBLIGATION, "Aceptar daciones en pago"));
    faculties.add(saveFaculty(poa, VERIFY, OBLIGATION, "Verificar e impugnar pagos"));
    faculties.add(saveFaculty(poa, EXECUTE, NOVATION, "Hacer novaciones"));
    faculties.add(saveFaculty(poa, EXECUTE, DELEGATION, "Hacer delegaciones"));
    faculties.add(saveFaculty(poa, EXECUTE, SUBROGATION, "Hacer subrogaciones"));
    faculties.add(saveFaculty(poa, TERMINATE, OBLIGATION, "Extinguir obligaciones"));
    faculties.add(saveFaculty(poa, WAIVE, RIGHTS, "Renunciar y aceptar renuncias de derechos"));
    faculties.add(saveFaculty(poa, EXECUTE, INSURANCE_POLICY, "Contratar seguros"));
    faculties.add(saveFaculty(poa, PAY, INSURANCE_POLICY, "Pagar primas de seguro"));
    faculties.add(saveFaculty(poa, COLLECT, DAMAGES, "Cobrar indemnizaciones de seguro"));
    faculties.add(saveFaculty(poa, EXECUTE, MANAGEMENT_CONTRACTS, "Celebrar contratos de administracion"));
    faculties.add(saveFaculty(poa, ACQUIRE, POSSESSION, "Adquirir posesion de bienes"));
    faculties.add(saveFaculty(poa, SETTLE, CONTRACT, "Transigir y rescindir transacciones"));
    faculties.add(saveFaculty(poa, GRANT, GUARANTEE, "Prestar fianzas o cauciones"));
    faculties.add(saveFaculty(poa, REQUEST, GUARANTEE, "Exigir fianzas o cauciones"));
    faculties.add(saveFaculty(poa, HIRE, EMPLOYMENT_RELATIONSHIP, "Nombrar administradores y empleados"));
    faculties.add(saveFaculty(poa, ACKNOWLEDGE, OBLIGATION, "Reconocer obligaciones preexistentes"));
    faculties.add(saveFaculty(poa, ISSUE, RECEIPT, "Dar recibos y cartas de pago"));
    faculties.add(saveFaculty(poa, ACQUIRE, MOVABLE_ASSETS, "Adquirir bienes muebles"));
    faculties.add(saveFaculty(poa, DISPOSE, MOVABLE_ASSETS, "Enajenar bienes muebles"));
    faculties.add(saveFaculty(poa, ACQUIRE, REAL_ESTATE, "Adquirir bienes inmuebles"));
    faculties.add(saveFaculty(poa, DISPOSE, REAL_ESTATE, "Enajenar bienes inmuebles"));
    faculties.add(saveFaculty(poa, ACQUIRE, LIVESTOCK, "Adquirir semovientes"));
    faculties.add(saveFaculty(poa, DISPOSE, LIVESTOCK, "Enajenar semovientes"));
    faculties.add(saveFaculty(poa, ACQUIRE, SECURITIES, "Adquirir titulos valores"));
    faculties.add(saveFaculty(poa, DISPOSE, SECURITIES, "Enajenar titulos valores"));
    faculties.add(saveFaculty(poa, ACQUIRE, VEHICLES, "Adquirir rodados"));
    faculties.add(saveFaculty(poa, DISPOSE, VEHICLES, "Enajenar rodados"));
    faculties.add(saveFaculty(poa, ACQUIRE, RECEIVABLES, "Adquirir créditos"));
    faculties.add(saveFaculty(poa, DISPOSE, RECEIVABLES, "Enajenar créditos"));
    faculties.add(saveFaculty(poa, ACQUIRE, RIGHTS, "Adquirir derechos"));
    faculties.add(saveFaculty(poa, DISPOSE, RIGHTS, "Enajenar derechos"));
    faculties.add(saveFaculty(poa, ACQUIRE, TRADEMARK, "Adquirir marcas"));
    faculties.add(saveFaculty(poa, DISPOSE, TRADEMARK, "Enajenar marcas"));
    faculties.add(saveFaculty(poa, ACQUIRE, PATENT, "Adquirir patentes"));
    faculties.add(saveFaculty(poa, DISPOSE, PATENT, "Enajenar patentes"));
    faculties.add(saveFaculty(poa, ACQUIRE, MERCHANDISE, "Adquirir mercaderias"));
    faculties.add(saveFaculty(poa, DISPOSE, MERCHANDISE, "Enajenar mercaderias"));
    faculties.add(saveFaculty(poa, ACQUIRE, FRUITS, "Adquirir frutos"));
    faculties.add(saveFaculty(poa, DISPOSE, FRUITS, "Enajenar frutos"));
    faculties.add(saveFaculty(poa, SIGN, PUBLIC_INSTRUMENT, "Firmar instrumentos publicos y privados"));
    faculties.add(saveFaculty(poa, INCORPORATE, COMPANY, "Formar sociedades"));
    faculties.add(saveFaculty(poa, REPRESENT, CORPORATE_SHAREHOLDING,
        "Representar en sociedades participadas"));
    faculties.add(saveFaculty(poa, ATTEND, CORPORATE_MEETING, "Asistir a asambleas"));
    faculties.add(saveFaculty(poa, VOTE, CORPORATE_MEETING, "Votar en asambleas"));
    faculties.add(saveFaculty(poa, SUBSCRIBE, SECURITIES, "Suscribir acciones"));
    faculties.add(saveFaculty(poa, PAY_IN, SECURITIES, "Integrar acciones"));
    faculties.add(saveFaculty(poa, EXECUTE, MOTION, "Efectuar mociones"));
    faculties.add(saveFaculty(poa, APPROVE, FINANCIAL_STATEMENTS, "Aprobar y votar cuentas y balances"));
    faculties.add(saveFaculty(poa, SIGN, CORPORATE_MEETING, "Firmar actas de asamblea"));
    faculties.add(saveFaculty(poa, COLLECT, DIVIDENDS, "Cobrar dividendos y beneficios"));
    faculties.add(saveFaculty(poa, DISSOLVE, COMPANY, "Disolver sociedades participadas"));
    faculties.add(saveFaculty(poa, LIQUIDATE, COMPANY, "Liquidar sociedades participadas"));
    faculties.add(saveFaculty(poa, AMEND, COMPANY, "Modificar sociedades participadas"));
    faculties.add(saveFaculty(poa, TRANSFER, CORPORATE_SHAREHOLDING,
        "Transferir acciones y derechos accionarios"));
    faculties.add(saveFaculty(poa, LEASE_OUT, REAL_ESTATE, "Dar en locacion bienes inmuebles"));
    faculties.add(saveFaculty(poa, LEASE_IN, REAL_ESTATE, "Tomar en locacion bienes inmuebles"));
    faculties.add(saveFaculty(poa, LEASE_OUT, MOVABLE_ASSETS, "Dar en locacion bienes muebles"));
    faculties.add(saveFaculty(poa, LEASE_IN, MOVABLE_ASSETS, "Tomar en locacion bienes muebles"));
    faculties.add(saveFaculty(poa, RENEW, LEASE_AGREEMENT, "Renovar y prorrogar locaciones"));
    faculties.add(saveFaculty(poa, TERMINATE, LEASE_AGREEMENT, "Rescindir locaciones"));
    faculties.add(saveFaculty(poa, ENCUMBER, MORTGAGE, "Constituir hipotecas"));
    faculties.add(saveFaculty(poa, ENCUMBER, PLEDGE, "Constituir prendas"));
    faculties.add(saveFaculty(poa, ENCUMBER, EASEMENT, "Constituir servidumbres"));
    faculties.add(saveFaculty(poa, RELEASE, MORTGAGE, "Cancelar hipotecas"));
    faculties.add(saveFaculty(poa, RELEASE, PLEDGE, "Cancelar prendas"));
    faculties.add(saveFaculty(poa, COLLECT, OBLIGATION, "Cobrar créditos y obligaciones"));
    faculties.add(saveFaculty(poa, PAY, OBLIGATION, "Pagar créditos y obligaciones"));
    faculties.add(saveFaculty(poa, COLLECT, INSURANCE_POLICY, "Cobrar polizas e indemnizaciones"));
    faculties.add(saveFaculty(poa, FILE, PROTEST, "Formular protestos y protestas"));
    faculties.add(saveFaculty(poa, APPROVE, INVENTORY, "Practicar y aprobar inventarios"));
    faculties.add(saveFaculty(poa, APPROVE, APPRAISAL, "Practicar y aprobar avaluos y pericias"));
    faculties
        .add(saveFaculty(poa, EXECUTE, ENERGY_SUPPLY_CONTRACT,
            "Contratos de suministro de energia eléctrica"));
    faculties.add(saveFaculty(poa, EXECUTE, ENERGY_EQUIPMENT,
        "Compra, venta y leasing de equipos de generacion"));
    faculties.add(saveFaculty(poa, AMEND, CONTRACT, "Modificar y ratificar actos y contratos"));
    faculties.add(saveFaculty(poa, REVOKE, CONTRACT, "Revocar y extinguir actos y contratos"));
    faculties.add(saveFaculty(poa, GRANT, PUBLIC_INSTRUMENT, "Otorgar instrumentos publicos y privados"));

    return faculties;
  }

  private List<PowerOfAttorneyFaculty> saveAdministrativeFilingFaculties(PowerOfAttorney poa) {
    List<PowerOfAttorneyFaculty> faculties = new ArrayList<>();
    faculties.add(saveFaculty(poa, FILE, ADMINISTRATIVE_FILE,
        "Iniciar y proseguir expedientes administrativos"));
    faculties.add(saveFaculty(poa, INSPECT, ADMINISTRATIVE_FILE, "Tomar vista de expedientes"));
    faculties.add(saveFaculty(poa, FILE, BRIEF, "Presentar escritos"));
    faculties.add(saveFaculty(poa, FILE, EVIDENCE, "Presentar documentos y pruebas"));
    faculties.add(saveFaculty(poa, FILE, DEFENSE, "Presentar descargos"));
    faculties.add(saveFaculty(poa, APPEAL, ADMINISTRATIVE_DECISION,
        "Interponer recursos administrativos y judiciales"));
    faculties.add(saveFaculty(poa, CHALLENGE, ADMINISTRATIVE_DECISION, "Impugnar actos administrativos"));
    faculties.add(saveFaculty(poa, CHALLENGE, JURISDICTION, "Plantear cuestiones de competencia"));
    faculties.add(saveFaculty(poa, FILE, STATUTE_OF_LIMITATIONS, "Articular caducidades y prescripciones"));
    faculties.add(saveFaculty(poa, ACKNOWLEDGE, ADMINISTRATIVE_DECISION,
        "Notificarse y consentir resoluciones"));
    faculties.add(saveFaculty(poa, WAIVE, RIGHTS, "Desistir de derechos y procedimientos"));
    faculties.add(saveFaculty(poa, SIGN, SWORN_STATEMENT, "Firmar declaraciones juradas"));
    faculties.add(saveFaculty(poa, SIGN, PLAN_AND_FORM, "Firmar planos y planillas"));
    faculties.add(saveFaculty(poa, SIGN, PUBLIC_INSTRUMENT, "Firmar instrumentos publicos o privados"));
    faculties.add(saveFaculty(poa, REQUEST, INSPECTION, "Solicitar inspecciones y verificaciones"));
    faculties.add(saveFaculty(poa, REQUEST, PERMIT, "Solicitar habilitaciones y rehabilitaciones"));
    faculties.add(saveFaculty(poa, SIGN, INSPECTION, "Firmar actas de inspeccion"));
    faculties.add(saveFaculty(poa, REQUEST, UTILITY_SERVICE, "Solicitar conexiones y servicios publicos"));
    faculties.add(saveFaculty(poa, TERMINATE, UTILITY_SERVICE, "Dar de baja servicios"));
    faculties
        .add(saveFaculty(poa, REQUEST, TAX_RELIEF,
            "Solicitar condonacion, exencion y devolucion de tributos"));
    faculties.add(saveFaculty(poa, REQUEST, FINE, "Gestionar multas, intereses y recargos"));
    faculties.add(saveFaculty(poa, FILE, VEHICLE_REGISTRATION, "Tramites ante el Registro Automotor"));
    faculties.add(saveFaculty(poa, TRANSFER, VEHICLES, "Transferir vehiculos"));
    faculties.add(saveFaculty(poa, COLLECT, CORRESPONDENCE,
        "Retirar correspondencia epistolar y telegrafica"));
    faculties.add(saveFaculty(poa, COLLECT, CARGO, "Retirar cargas y encomiendas"));
    faculties.add(saveFaculty(poa, ESTABLISH, SPECIAL_DOMICILE, "Constituir domicilios especiales"));
    return faculties;
  }

  private List<PowerOfAttorneyFaculty> saveCustomsFaculties(PowerOfAttorney poa) {
    List<PowerOfAttorneyFaculty> faculties = new ArrayList<>();
    faculties.add(saveFaculty(poa, FILE, CUSTOMS_DECLARATION, "Efectuar gestiones aduaneras"));
    faculties.add(
        saveFaculty(poa, SIGN, CUSTOMS_DECLARATION,
            "Suscribir documentacion de importacion y exportacion"));
    faculties.add(saveFaculty(poa, COLLECT, EXPORT_REFUND, "Cobrar reintegros de exportacion"));
    faculties.add(saveFaculty(poa, ATTEND, CUSTOMS_INSPECTION, "Asistir a la verificacion de mercaderia"));
    faculties.add(saveFaculty(poa, APPROVE, CUSTOMS_INSPECTION,
        "Dar conformidad al resultado de la verificacion"));
    faculties.add(saveFaculty(poa, SIGN, BILL_OF_LADING, "Suscribir conocimientos y cartas de porte"));
    faculties.add(saveFaculty(poa, SIGN, INVOICE, "Suscribir facturas"));
    faculties.add(saveFaculty(poa, SIGN, CERTIFICATE_OF_ORIGIN, "Suscribir certificados de origen"));
    faculties.add(saveFaculty(poa, RECEIVE, GUARANTEE, "Recibir devolucion de garantias aduaneras"));
    faculties.add(saveFaculty(poa, REPRESENT, COMPANY, "Representar a la Sociedad ante la DGA"));
    faculties.add(saveFaculty(poa, FILE, ADMINISTRATIVE_FILE,
        "Iniciar y proseguir expedientes ante AFIP y DGI"));
    faculties.add(saveFaculty(poa, APPEAL, ADMINISTRATIVE_DECISION, "Interponer recursos"));
    faculties.add(saveFaculty(poa, SIGN, SWORN_STATEMENT, "Firmar declaraciones juradas"));
    faculties.add(saveFaculty(poa, REQUEST, TAX_RELIEF, "Solicitar condonacion y devolucion de tributos"));
    faculties.add(saveFaculty(poa, COLLECT, CARGO, "Retirar cargas y encomiendas"));
    faculties.add(saveFaculty(poa, ESTABLISH, SPECIAL_DOMICILE, "Constituir domicilios especiales"));
    return faculties;
  }

  private List<PowerOfAttorneyFaculty> saveLaborFaculties(PowerOfAttorney poa) {
    List<PowerOfAttorneyFaculty> faculties = new ArrayList<>();
    faculties
        .add(saveFaculty(poa, ISSUE, TERMINATION_NOTICE,
            "Suscribir cartas documento y telegramas de despido"));
    faculties.add(saveFaculty(poa, FILE, LABOR_CLAIM, "Promover demandas y reclamos"));
    faculties.add(saveFaculty(poa, ANSWER, LABOR_CLAIM, "Contestar demandas y reclamos"));
    faculties.add(saveFaculty(poa, FILE, DEFENSE, "Oponer excepciones e incidentes"));
    faculties.add(saveFaculty(poa, FILE, COUNTERCLAIM, "Reconvenir"));
    faculties.add(saveFaculty(poa, CHALLENGE, JUDGE, "Recusar jueces o funcionarios"));
    faculties.add(saveFaculty(poa, LITIGATE, LABOR_CLAIM, "Instaurar acciones reales o personales"));
    faculties.add(saveFaculty(poa, ATTEND, HEARING, "Asistir a audiencias"));
    faculties.add(saveFaculty(poa, ACKNOWLEDGE, EVIDENCE, "Reconocer y desconocer documentacion"));
    faculties.add(saveFaculty(poa, FILE, EVIDENCE, "Producir pruebas e informaciones"));
    faculties.add(saveFaculty(poa, FILE, WITNESS, "Proponer y tachar testigos"));
    faculties.add(saveFaculty(poa, REQUEST, EXPERT_REPORT, "Solicitar pericias e informes de peritos"));
    faculties.add(saveFaculty(poa, ANSWER, INTERROGATORY,
        "Contestar interrogatorios y absolver posiciones"));
    faculties.add(saveFaculty(poa, WAIVE, JURISDICTION, "Declinar y prorrogar jurisdicciones"));
    faculties.add(saveFaculty(poa, APPEAL, LABOR_CLAIM, "Interponer recursos legales"));
    faculties.add(saveFaculty(poa, INTERRUPT, STATUTE_OF_LIMITATIONS,
        "Oponer e interrumpir prescripciones"));
    faculties.add(saveFaculty(poa, SUBMIT_TO_ARBITRATION, LABOR_CLAIM, "Comprometer causas en arbitros"));
    faculties.add(saveFaculty(poa, SETTLE, LABOR_CLAIM, "Transigir, conciliar y celebrar acuerdos"));
    faculties.add(saveFaculty(poa, ACCEPT, OATH, "Prestar o diferir juramentos"));
    faculties.add(saveFaculty(poa, REQUEST, PRECAUTIONARY_MEASURE,
        "Pedir embargos, inhibiciones y cautelares"));
    faculties.add(saveFaculty(poa, DISCLOSE, MOVABLE_ASSETS, "Denunciar bienes"));
    faculties.add(saveFaculty(poa, GRANT, GRACE_PERIOD, "Conceder quitas y esperas"));
    faculties.add(saveFaculty(poa, APPOINT, EXPERT, "Nombrar peritos, tasadores y martilleros"));
    faculties.add(saveFaculty(poa, REQUEST, GUARANTEE, "Aceptar o exigir fianzas, cauciones y arraigos"));
    faculties.add(saveFaculty(poa, FILE, LETTER_ROGATORY, "Diligenciar exhortos, mandamientos y cédulas"));
    faculties.add(saveFaculty(poa, ISSUE, FORMAL_DEMAND,
        "Efectuar y contestar intimaciones y notificaciones"));
    faculties.add(saveFaculty(poa, REQUEST, NOTARIAL_CERTIFICATE, "Solicitar actas de constatacion"));
    faculties.add(saveFaculty(poa, REQUEST, INVENTORY, "Realizar o solicitar inventarios"));
    faculties.add(saveFaculty(poa, INSPECT, ACCOUNTING_BOOKS, "Solicitar compulsas de libros"));
    faculties.add(saveFaculty(poa, DELEGATE, POWER_OF_ATTORNEY, "Delegar el mandato"));
    faculties.add(saveFaculty(poa, COLLECT, RECEIVABLES, "Percibir créditos preexistentes o posteriores"));
    faculties.add(saveFaculty(poa, ISSUE, RECEIPT, "Dar y exigir recibos y cartas de pago"));
    faculties.add(saveFaculty(poa, COLLECT, DAMAGES, "Hacer cargos y cobrar indemnizaciones"));
    faculties.add(saveFaculty(poa, APPROVE, ACCOUNTING,
        "Rendir, exigir e impugnar rendiciones de cuentas"));
    faculties.add(saveFaculty(poa, ACCEPT, FORCE_MAJEURE,
        "Aceptar o rechazar casos fortuitos o fuerza mayor"));
    faculties.add(saveFaculty(poa, REQUEST, UNION_DUES, "Solicitar devolucion de cuotas sindicales"));
    faculties.add(saveFaculty(poa, SETTLE, EMPLOYMENT_RELATIONSHIP,
        "Disponer, conciliar y suscribir acuerdos laborales"));
    faculties.add(saveFaculty(poa, TERMINATE, EMPLOYMENT_RELATIONSHIP, "Extinguir relaciones laborales"));
    faculties.add(saveFaculty(poa, EXECUTE, BARGAINING_COMMITTEE, "Conformar comisiones negociadoras"));
    faculties.add(saveFaculty(poa, NEGOTIATE, COLLECTIVE_AGREEMENT,
        "Negociar y suscribir convenios colectivos"));
    return faculties;
  }

  private List<PowerOfAttorneyFaculty> saveTransportFaculties(PowerOfAttorney poa) {
    List<PowerOfAttorneyFaculty> faculties = new ArrayList<>();
    faculties.add(saveFaculty(poa, REQUEST, PERMIT,
        "Solicitar habilitaciones y permisos de transporte de cargas"));
    faculties.add(saveFaculty(poa, RENEW, PERMIT, "Renovar habilitaciones y licencias"));
    faculties.add(saveFaculty(poa, TERMINATE, PERMIT, "Dar de baja habilitaciones"));
    faculties.add(
        saveFaculty(poa, REGISTER, VEHICLE_REGISTRATION,
            "Inscribir unidades en el RUTA y Registro Automotor"));
    faculties.add(saveFaculty(poa, AMEND, VEHICLE_REGISTRATION, "Modificar inscripciones de unidades"));
    faculties.add(saveFaculty(poa, TRANSFER, VEHICLE_REGISTRATION, "Transferir unidades"));
    faculties.add(saveFaculty(poa, COLLECT, CERTIFICATE, "Retirar documentacion de las unidades"));
    faculties.add(saveFaculty(poa, FILE, ADMINISTRATIVE_FILE,
        "Iniciar y proseguir expedientes ante la CNRT"));
    faculties.add(saveFaculty(poa, SIGN, SWORN_STATEMENT, "Firmar declaraciones juradas"));
    faculties.add(saveFaculty(poa, REQUEST, INSPECTION, "Solicitar inspecciones técnicas"));
    faculties.add(saveFaculty(poa, SIGN, INSPECTION, "Firmar actas de inspeccion"));
    faculties.add(
        saveFaculty(poa, APPEAL, ADMINISTRATIVE_DECISION,
            "Interponer recursos administrativos y judiciales"));
    faculties.add(saveFaculty(poa, ESTABLISH, SPECIAL_DOMICILE, "Constituir domicilios especiales"));
    return faculties;
  }

  private Company saveCompany(String name, String type) {
    Company company = new Company();
    company.setName(name);
    company.setType(type);
    return companyRepo.save(company);
  }

  private Person savePerson(String firstName, String lastName, String nationalId) {
    Person person = new Person();
    person.setFirstName(firstName);
    person.setLastName(lastName);
    person.setNationalId(nationalId);
    return personRepo.save(person);
  }

  private CompanyMember saveCompanyMember(Company company, Person person) {
    CompanyMember companyMember = new CompanyMember();
    companyMember.setCompany(company);
    companyMember.setPerson(person);
    return companyMemberRepo.save(companyMember);
  }

  private PowerOfAttorney savePoa(Company company, LocalDate granted, LocalDate from, LocalDate until,
      LocalDate revoked) {
    PowerOfAttorney powerOfAttorney = new PowerOfAttorney();
    powerOfAttorney.setCompany(company);
    powerOfAttorney.setGrantedOn(granted.atStartOfDay());
    powerOfAttorney.setValidFrom(from.atStartOfDay());
    powerOfAttorney.setValidUntil(until.atStartOfDay());
    if (revoked != null) {
      powerOfAttorney.setRevokedEffective(revoked.atStartOfDay());
    }
    return poaRepo.save(powerOfAttorney);
  }

  private PowerOfAttorneyGroup saveGroup(PowerOfAttorney poa, String label) {
    PowerOfAttorneyGroup powerOfAttorneyGroup = new PowerOfAttorneyGroup();
    powerOfAttorneyGroup.setPowerOfAttorney(poa);
    powerOfAttorneyGroup.setLabel(label);
    return groupRepo.save(powerOfAttorneyGroup);
  }

  private void saveMembers(PowerOfAttorneyGroup group, Person... people) {
    for (Person person : people) {
      PowerOfAttorneyGroupMember member = new PowerOfAttorneyGroupMember();
      member.setPowerOfAttorneyGroup(group);
      member.setPerson(person);
      memberRepo.save(member);
    }
  }

  private PowerOfAttorneyFaculty saveFaculty(PowerOfAttorney poa, FacultyAction action, FacultyObject object,
      String description) {
    PowerOfAttorneyFaculty faculty = new PowerOfAttorneyFaculty();
    faculty.setPowerOfAttorney(poa);
    faculty.setAction(action);
    faculty.setObjectCategory(object);
    faculty.setDescription(description);
    return facultyRepo.save(faculty);
  }

  private void saveScopeItem(PowerOfAttorneyFaculty faculty, String externalRef, String label) {
    FacultyScopeItem item = new FacultyScopeItem();
    item.setFaculty(faculty);
    item.setExternalRef(externalRef);
    item.setLabel(label);
    scopeRepo.save(item);
  }

  private void saveRule(PowerOfAttorneyFaculty faculty, BigDecimal min, BigDecimal max, String currency,
      PowerOfAttorneyGroup... groups) {
    PowerOfAttorneySigningRule rule = new PowerOfAttorneySigningRule();
    rule.setPowerOfAttorneyFaculty(faculty);
    rule.setMinAmount(min);
    rule.setMaxAmount(max);
    rule.setCurrency(currency);
    ruleRepo.save(rule);

    Map<PowerOfAttorneyGroup, Integer> counts = new HashMap<>();
    for (PowerOfAttorneyGroup group : groups) {
      Integer current = counts.get(group);
      if (current == null) {
        counts.put(group, 1);
      } else {
        counts.put(group, current + 1);
      }
    }

    for (Map.Entry<PowerOfAttorneyGroup, Integer> entry : counts.entrySet()) {
      PowerOfAttorneySigningRuleRequirement req = new PowerOfAttorneySigningRuleRequirement();
      req.setPowerOfAttorneySigningRule(rule);
      req.setPowerOfAttorneyGroup(entry.getKey());
      req.setCountRequired(entry.getValue());
      reqRepo.save(req);
    }
  }

  private LocalDate toDate(int year, int month, int day) {
    return LocalDate.of(year, month, day);
  }
}