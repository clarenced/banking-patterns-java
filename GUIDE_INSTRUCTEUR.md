# Guide de l'Instructeur - Formation Design Patterns

## 📋 Vue d'ensemble de la formation

### Durée estimée
- **Total** : 2-3 jours (16-24 heures)
- **Partie 1 - Créationnels** : 6-8 heures
- **Partie 2 - Structurels** : 4-6 heures
- **Partie 3 - Comportementaux** : 4-6 heures
- **Partie 4 - Nouvelles fonctionnalités** : 2-4 heures

### Niveau
- Développeurs Java intermédiaires
- Connaissance de base de la POO
- Expérience avec Maven/Git souhaitable

---

## 🎯 Objectifs pédagogiques détaillés

À la fin de cette formation, les participants seront capables de :

1. **Identifier** les code smells et anti-patterns dans du code legacy
2. **Appliquer** les design patterns appropriés selon le contexte
3. **Refactorer** du code progressivement sans tout casser
4. **Justifier** leurs choix de design patterns
5. **Étendre** le code de manière maintenable

---

## 📚 Déroulement recommandé

### Jour 1 - Matin : Introduction et Patterns Créationnels

#### 1. Introduction (30 min)
- Présentation du projet bancaire
- Explication du code legacy
- Démonstration de l'application
- Identification collective des problèmes

**🎓 Point pédagogique** : Faites exécuter le code et demandez aux participants d'identifier au moins 5 problèmes majeurs.

#### 2. Exercice 1 : Builder Pattern (1h30)

**Problèmes à identifier** :
```java
// Constructeur illisible avec trop de paramètres
BankAccount account = new BankAccount(accountNumber, "COURANT", name, email,
                                      phone, initialDeposit, 0.0, 500.0);
```

**Solution attendue** :
```java
public class BankAccount {
    // Champs...

    private BankAccount(Builder builder) {
        this.accountNumber = builder.accountNumber;
        this.accountType = builder.accountType;
        this.customerName = builder.customerName;
        this.customerEmail = builder.customerEmail;
        this.customerPhone = builder.customerPhone;
        this.balance = builder.balance;
        this.interestRate = builder.interestRate;
        this.overdraftLimit = builder.overdraftLimit;
        this.creationDate = new Date();
        this.status = "ACTIVE";
    }

    public static class Builder {
        private String accountNumber;
        private String accountType;
        private String customerName;
        private String customerEmail;
        private String customerPhone;
        private double balance;
        private double interestRate;
        private double overdraftLimit;

        public Builder accountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder accountType(String accountType) {
            this.accountType = accountType;
            return this;
        }

        public Builder customerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public Builder customerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
            return this;
        }

        public Builder customerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
            return this;
        }

        public Builder balance(double balance) {
            this.balance = balance;
            return this;
        }

        public Builder interestRate(double interestRate) {
            this.interestRate = interestRate;
            return this;
        }

        public Builder overdraftLimit(double overdraftLimit) {
            this.overdraftLimit = overdraftLimit;
            return this;
        }

        public BankAccount build() {
            // Validations
            if (accountNumber == null || accountNumber.isEmpty()) {
                throw new IllegalStateException("Account number is required");
            }
            if (customerName == null || customerName.isEmpty()) {
                throw new IllegalStateException("Customer name is required");
            }
            if (customerEmail == null || !customerEmail.contains("@")) {
                throw new IllegalStateException("Valid email is required");
            }
            if (balance < 0) {
                throw new IllegalStateException("Balance cannot be negative");
            }

            return new BankAccount(this);
        }
    }
}
```

**🎓 Points à discuter** :
- Quand utiliser Builder vs constructeur simple ?
- Avantages : lisibilité, validation, paramètres optionnels
- Limites : verbosité (mentionner Lombok pour les projets réels)

---

#### 3. Exercice 2 : Factory Pattern (1h30)

**Solution attendue** :

```java
public interface AccountFactory {
    BankAccount createAccount(String customerName, String email, String phone, double initialDeposit);
}

public class CurrentAccountFactory implements AccountFactory {
    private static final double MIN_INITIAL_DEPOSIT = 100.0;
    private static final double OVERDRAFT_LIMIT = 500.0;
    private static final double INTEREST_RATE = 0.0;

    private int accountCounter = 1000;

    @Override
    public BankAccount createAccount(String customerName, String email,
                                     String phone, double initialDeposit) {
        if (initialDeposit < MIN_INITIAL_DEPOSIT) {
            throw new IllegalArgumentException(
                "Minimum initial deposit for current account: " + MIN_INITIAL_DEPOSIT + " EUR");
        }

        String accountNumber = "CUR" + (accountCounter++);

        return new BankAccount.Builder()
            .accountNumber(accountNumber)
            .accountType("COURANT")
            .customerName(customerName)
            .customerEmail(email)
            .customerPhone(phone)
            .balance(initialDeposit)
            .interestRate(INTEREST_RATE)
            .overdraftLimit(OVERDRAFT_LIMIT)
            .build();
    }
}

public class SavingsAccountFactory implements AccountFactory {
    private static final double MIN_INITIAL_DEPOSIT = 500.0;
    private static final double INTEREST_RATE = 2.5;
    private static final double OVERDRAFT_LIMIT = 0.0;

    private int accountCounter = 2000;

    @Override
    public BankAccount createAccount(String customerName, String email,
                                     String phone, double initialDeposit) {
        if (initialDeposit < MIN_INITIAL_DEPOSIT) {
            throw new IllegalArgumentException(
                "Minimum initial deposit for savings account: " + MIN_INITIAL_DEPOSIT + " EUR");
        }

        String accountNumber = "SAV" + (accountCounter++);

        return new BankAccount.Builder()
            .accountNumber(accountNumber)
            .accountType("EPARGNE")
            .customerName(customerName)
            .customerEmail(email)
            .customerPhone(phone)
            .balance(initialDeposit)
            .interestRate(INTEREST_RATE)
            .overdraftLimit(OVERDRAFT_LIMIT)
            .build();
    }
}

public class BusinessAccountFactory implements AccountFactory {
    private static final double MIN_INITIAL_DEPOSIT = 1000.0;
    private static final double INTEREST_RATE = 0.5;
    private static final double OVERDRAFT_LIMIT = 2000.0;

    private int accountCounter = 3000;

    @Override
    public BankAccount createAccount(String customerName, String email,
                                     String phone, double initialDeposit) {
        if (initialDeposit < MIN_INITIAL_DEPOSIT) {
            throw new IllegalArgumentException(
                "Minimum initial deposit for business account: " + MIN_INITIAL_DEPOSIT + " EUR");
        }

        String accountNumber = "BUS" + (accountCounter++);

        return new BankAccount.Builder()
            .accountNumber(accountNumber)
            .accountType("PROFESSIONNEL")
            .customerName(customerName)
            .customerEmail(email)
            .customerPhone(phone)
            .balance(initialDeposit)
            .interestRate(INTEREST_RATE)
            .overdraftLimit(OVERDRAFT_LIMIT)
            .build();
    }
}
```

**🎓 Points à discuter** :
- Factory vs Factory Method vs Abstract Factory
- Open/Closed Principle : facile d'ajouter un nouveau type
- Centralisation des règles métier

---

### Jour 1 - Après-midi : Suite Patterns Créationnels

#### 4. Exercice 3 : Abstract Factory (1h30)

**Solution attendue** :

```java
public interface BankingPackageFactory {
    BankAccount createAccount(String name, String email, String phone, double deposit);
    FeeCalculator createFeeCalculator();
    NotificationService createNotificationService();
}

public class StandardBankingPackage implements BankingPackageFactory {
    @Override
    public BankAccount createAccount(String name, String email, String phone, double deposit) {
        return new CurrentAccountFactory().createAccount(name, email, phone, deposit);
    }

    @Override
    public FeeCalculator createFeeCalculator() {
        return new StandardFeeCalculator();
    }

    @Override
    public NotificationService createNotificationService() {
        return new EmailOnlyNotificationService();
    }
}

public class PremiumBankingPackage implements BankingPackageFactory {
    @Override
    public BankAccount createAccount(String name, String email, String phone, double deposit) {
        return new SavingsAccountFactory().createAccount(name, email, phone, deposit);
    }

    @Override
    public FeeCalculator createFeeCalculator() {
        return new NoFeeCalculator(); // Pas de frais pour premium
    }

    @Override
    public NotificationService createNotificationService() {
        return new MultiChannelNotificationService(); // Email + SMS
    }
}
```

**🎓 Points à discuter** :
- Cohérence des familles de produits
- Utilisation dans les applications multi-tenant
- Différence avec Factory simple

---

#### 5. Exercice 4 : Singleton (1h)

**Solution attendue** :

```java
public class TransactionIdGenerator {
    private static volatile TransactionIdGenerator instance;
    private final AtomicLong counter = new AtomicLong(1);

    private TransactionIdGenerator() {
        // Constructeur privé
    }

    public static TransactionIdGenerator getInstance() {
        if (instance == null) {
            synchronized (TransactionIdGenerator.class) {
                if (instance == null) {
                    instance = new TransactionIdGenerator();
                }
            }
        }
        return instance;
    }

    public String generateId() {
        return "TX" + counter.getAndIncrement();
    }
}

public class BankingConfiguration {
    private static volatile BankingConfiguration instance;

    private double maxTransferAmount = 50000.0;
    private double maxDailyWithdrawal = 1000.0;
    private int fraudAlertThreshold = 5;

    private BankingConfiguration() {
        // Load from properties file or environment
    }

    public static BankingConfiguration getInstance() {
        if (instance == null) {
            synchronized (BankingConfiguration.class) {
                if (instance == null) {
                    instance = new BankingConfiguration();
                }
            }
        }
        return instance;
    }

    public double getMaxTransferAmount() { return maxTransferAmount; }
    public double getMaxDailyWithdrawal() { return maxDailyWithdrawal; }
    public int getFraudAlertThreshold() { return fraudAlertThreshold; }
}
```

**⚠️ Avertissement à donner** : Dans les applications modernes (Spring, Jakarta EE), préférez l'injection de dépendances. Le Singleton est utile pour des cas spécifiques (configuration, cache, etc.).

**🎓 Points à discuter** :
- Double-checked locking
- Thread-safety
- Alternatives : enum singleton, holder pattern
- Problèmes de testabilité

---

#### 6. Exercice 5 : Prototype (1h)

**Solution attendue** :

```java
public class BankAccount implements Cloneable {
    // Champs existants...

    @Override
    public BankAccount clone() {
        try {
            BankAccount cloned = (BankAccount) super.clone();
            // Deep copy si nécessaire pour des objets complexes
            cloned.creationDate = (Date) this.creationDate.clone();
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }
}

public class AccountTemplateRegistry {
    private Map<String, BankAccount> templates = new HashMap<>();

    public AccountTemplateRegistry() {
        // Initialiser les templates
        BankAccount studentTemplate = new BankAccount.Builder()
            .accountType("COURANT")
            .interestRate(0.0)
            .overdraftLimit(200.0)
            .build();
        templates.put("COMPTE_ETUDIANT", studentTemplate);

        BankAccount seniorTemplate = new BankAccount.Builder()
            .accountType("EPARGNE")
            .interestRate(3.0)
            .overdraftLimit(0.0)
            .build();
        templates.put("COMPTE_SENIOR", seniorTemplate);
    }

    public BankAccount createFromTemplate(String templateName, String customerName,
                                         String email, String phone, double balance) {
        BankAccount template = templates.get(templateName);
        if (template == null) {
            throw new IllegalArgumentException("Template not found: " + templateName);
        }

        BankAccount newAccount = template.clone();
        // Personnaliser
        String accountNumber = "ACC" + System.currentTimeMillis();
        // Utiliser reflection ou setter pour modifier les champs

        return newAccount;
    }
}
```

**🎓 Points à discuter** :
- Shallow vs Deep copy
- Quand utiliser Prototype vs Factory
- Alternatives modernes (copy constructors, builders)

---

### Jour 2 - Matin : Patterns Structurels

#### 7. Exercice 6 : Adapter (1h)

**Solution attendue** :

```java
// Interface interne
public interface PaymentGateway {
    boolean processPayment(String accountNumber, double amount);
    TransactionStatus checkStatus(String transactionId);
}

// API externe (simulation)
public class ExternalPaymentAPI {
    public PaymentResult executePayment(PaymentRequest request) {
        // Logique externe...
        return new PaymentResult(true, "EXT" + System.currentTimeMillis());
    }

    public PaymentStatusResponse getPaymentStatus(String externalId) {
        return new PaymentStatusResponse("COMPLETED");
    }
}

// Adapter
public class PaymentGatewayAdapter implements PaymentGateway {
    private ExternalPaymentAPI externalAPI;

    public PaymentGatewayAdapter(ExternalPaymentAPI externalAPI) {
        this.externalAPI = externalAPI;
    }

    @Override
    public boolean processPayment(String accountNumber, double amount) {
        // Conversion de notre format vers le format externe
        PaymentRequest request = new PaymentRequest();
        request.setAccountId(accountNumber);
        request.setAmountCents((long)(amount * 100));

        PaymentResult result = externalAPI.executePayment(request);
        return result.isSuccess();
    }

    @Override
    public TransactionStatus checkStatus(String transactionId) {
        PaymentStatusResponse response = externalAPI.getPaymentStatus(transactionId);
        // Conversion du format externe vers notre format
        return TransactionStatus.valueOf(response.getStatus());
    }
}
```

**🎓 Points à discuter** :
- Intégration de systèmes tiers
- Stabilité de l'interface interne
- Testabilité (mocking de l'API externe)

---

#### 8. Exercice 7 : Composite (1h30)

**Solution attendue** :

```java
public interface AccountComponent {
    double getBalance();
    void deposit(double amount);
    void withdraw(double amount);
    String generateStatement();
}

public class SimpleAccount implements AccountComponent {
    private BankAccount account;

    public SimpleAccount(BankAccount account) {
        this.account = account;
    }

    @Override
    public double getBalance() {
        return account.getBalance();
    }

    @Override
    public void deposit(double amount) {
        account.setBalance(account.getBalance() + amount);
    }

    @Override
    public void withdraw(double amount) {
        account.setBalance(account.getBalance() - amount);
    }

    @Override
    public String generateStatement() {
        return "Account: " + account.getAccountNumber() +
               " Balance: " + account.getBalance();
    }
}

public class CompositeAccount implements AccountComponent {
    private String name;
    private List<AccountComponent> children = new ArrayList<>();

    public CompositeAccount(String name) {
        this.name = name;
    }

    public void add(AccountComponent account) {
        children.add(account);
    }

    public void remove(AccountComponent account) {
        children.remove(account);
    }

    @Override
    public double getBalance() {
        return children.stream()
                      .mapToDouble(AccountComponent::getBalance)
                      .sum();
    }

    @Override
    public void deposit(double amount) {
        // Distribuer équitablement
        double amountPerAccount = amount / children.size();
        children.forEach(child -> child.deposit(amountPerAccount));
    }

    @Override
    public void withdraw(double amount) {
        double amountPerAccount = amount / children.size();
        children.forEach(child -> child.withdraw(amountPerAccount));
    }

    @Override
    public String generateStatement() {
        StringBuilder sb = new StringBuilder();
        sb.append("Composite Account: ").append(name).append("\n");
        sb.append("Total Balance: ").append(getBalance()).append("\n");
        sb.append("Sub-accounts:\n");
        children.forEach(child -> sb.append("  ").append(child.generateStatement()).append("\n"));
        return sb.toString();
    }
}
```

**🎓 Points à discuter** :
- Traitement uniforme d'objets simples et composés
- Cas d'usage : portefeuilles, comptes joints
- Navigation dans l'arbre

---

#### 9. Exercice 8 : Decorator (1h30)

**Solution attendue** :

```java
// Même interface que Composite
public abstract class AccountDecorator implements AccountComponent {
    protected AccountComponent decoratedAccount;

    public AccountDecorator(AccountComponent account) {
        this.decoratedAccount = account;
    }

    @Override
    public double getBalance() {
        return decoratedAccount.getBalance();
    }

    @Override
    public void deposit(double amount) {
        decoratedAccount.deposit(amount);
    }

    @Override
    public void withdraw(double amount) {
        decoratedAccount.withdraw(amount);
    }

    @Override
    public String generateStatement() {
        return decoratedAccount.generateStatement();
    }
}

public class InsuredAccountDecorator extends AccountDecorator {
    private double insuranceCoverage;

    public InsuredAccountDecorator(AccountComponent account, double coverage) {
        super(account);
        this.insuranceCoverage = coverage;
    }

    @Override
    public String generateStatement() {
        return super.generateStatement() +
               "\nInsurance Coverage: " + insuranceCoverage + " EUR";
    }

    public double getInsuranceCoverage() {
        return insuranceCoverage;
    }
}

public class CashbackAccountDecorator extends AccountDecorator {
    private double cashbackRate;
    private double totalCashback = 0;

    public CashbackAccountDecorator(AccountComponent account, double rate) {
        super(account);
        this.cashbackRate = rate;
    }

    @Override
    public void withdraw(double amount) {
        super.withdraw(amount);
        double cashback = amount * cashbackRate;
        totalCashback += cashback;
        super.deposit(cashback);
        System.out.println("Cashback earned: " + cashback + " EUR");
    }

    @Override
    public String generateStatement() {
        return super.generateStatement() +
               "\nTotal Cashback Earned: " + totalCashback + " EUR";
    }
}

public class NotificationAccountDecorator extends AccountDecorator {
    private NotificationService notificationService;

    public NotificationAccountDecorator(AccountComponent account,
                                       NotificationService notificationService) {
        super(account);
        this.notificationService = notificationService;
    }

    @Override
    public void deposit(double amount) {
        super.deposit(amount);
        notificationService.send("Deposit of " + amount + " EUR processed");
    }

    @Override
    public void withdraw(double amount) {
        super.withdraw(amount);
        notificationService.send("Withdrawal of " + amount + " EUR processed");
    }
}
```

**🎓 Points à discuter** :
- Ajout dynamique de responsabilités
- Composition vs héritage
- Combiner plusieurs decorators
- Différence avec Proxy

---

### Jour 2 - Après-midi : Facade + Patterns Comportementaux

#### 10. Exercice 9 : Facade (1h)

**Solution attendue** :

```java
public class BankingFacade {
    private AccountFactory accountFactory;
    private TransactionProcessor transactionProcessor;
    private ValidationService validationService;
    private NotificationService notificationService;
    private AuditService auditService;

    public BankingFacade() {
        // Initialiser les sous-systèmes
        this.accountFactory = new CurrentAccountFactory();
        this.transactionProcessor = new StandardTransactionProcessor();
        this.validationService = new ValidationService();
        this.notificationService = new EmailNotificationService();
        this.auditService = new AuditService();
    }

    public BankAccount openNewAccount(String type, String name,
                                     String email, double initialDeposit) {
        // 1. Validation
        validationService.validateCustomerInfo(name, email);
        validationService.validateInitialDeposit(type, initialDeposit);

        // 2. Sélection de la factory
        AccountFactory factory = selectFactory(type);

        // 3. Création du compte
        BankAccount account = factory.createAccount(name, email, "", initialDeposit);

        // 4. Notification
        notificationService.sendWelcomeEmail(email, account.getAccountNumber());

        // 5. Audit
        auditService.logAccountCreation(account);

        return account;
    }

    public boolean transferMoney(BankAccount source, BankAccount destination,
                                double amount) {
        // 1. Validation complète
        if (!validationService.validateTransfer(source, destination, amount)) {
            return false;
        }

        // 2. Traitement
        Transaction transaction = new Transaction("TX" + System.currentTimeMillis(),
                                                  "VIREMENT", amount,
                                                  source.getAccountNumber(),
                                                  destination.getAccountNumber());

        boolean success = transactionProcessor.process(transaction);

        // 3. Notifications
        if (success) {
            notificationService.notifyTransfer(source, destination, amount);
            auditService.logTransaction(transaction);
        }

        return success;
    }

    public void closeAccount(BankAccount account) {
        // 1. Vérifications
        if (account.getBalance() > 0) {
            throw new IllegalStateException("Account must have zero balance to close");
        }

        // 2. Mise à jour du statut
        account.setStatus("CLOSED");

        // 3. Notification
        notificationService.sendAccountClosureConfirmation(account.getCustomerEmail());

        // 4. Archivage
        auditService.archiveAccount(account);
    }

    private AccountFactory selectFactory(String type) {
        switch (type) {
            case "COURANT": return new CurrentAccountFactory();
            case "EPARGNE": return new SavingsAccountFactory();
            case "PROFESSIONNEL": return new BusinessAccountFactory();
            default: throw new IllegalArgumentException("Unknown account type");
        }
    }
}
```

**🎓 Points à discuter** :
- Simplification pour les clients
- Coordination de sous-systèmes
- Ne pas abuser : la facade ne doit pas devenir un God Object

---

#### 11. Exercice 10 : Strategy (1h30)

**Solution attendue** :

```java
public interface FeeCalculationStrategy {
    double calculateFee(Transaction transaction);
}

public class CurrentAccountFeeStrategy implements FeeCalculationStrategy {
    private static final double WITHDRAWAL_THRESHOLD = 1000.0;
    private static final double HIGH_WITHDRAWAL_FEE = 2.5;
    private static final double TRANSFER_FEE = 1.0;

    @Override
    public double calculateFee(Transaction transaction) {
        switch (transaction.getType()) {
            case "DEPOT":
                return 0.0;
            case "RETRAIT":
                return transaction.getAmount() > WITHDRAWAL_THRESHOLD ?
                       HIGH_WITHDRAWAL_FEE : 0.0;
            case "VIREMENT":
                return TRANSFER_FEE;
            default:
                return 0.0;
        }
    }
}

public class SavingsAccountFeeStrategy implements FeeCalculationStrategy {
    private static final double WITHDRAWAL_FEE = 1.0;
    private static final double TRANSFER_FEE = 2.5;

    @Override
    public double calculateFee(Transaction transaction) {
        switch (transaction.getType()) {
            case "DEPOT":
                return 0.0;
            case "RETRAIT":
                return WITHDRAWAL_FEE;
            case "VIREMENT":
                return TRANSFER_FEE; // Frais élevé pour décourager les virements
            default:
                return 0.0;
        }
    }
}

public class BusinessAccountFeeStrategy implements FeeCalculationStrategy {
    private static final double LARGE_WITHDRAWAL_THRESHOLD = 5000.0;
    private static final double LARGE_WITHDRAWAL_FEE = 5.0;
    private static final double SMALL_WITHDRAWAL_FEE = 2.0;
    private static final double TRANSFER_FEE = 0.5;

    @Override
    public double calculateFee(Transaction transaction) {
        switch (transaction.getType()) {
            case "DEPOT":
                return 0.0;
            case "RETRAIT":
                return transaction.getAmount() > LARGE_WITHDRAWAL_THRESHOLD ?
                       LARGE_WITHDRAWAL_FEE : SMALL_WITHDRAWAL_FEE;
            case "VIREMENT":
                return TRANSFER_FEE;
            default:
                return 0.0;
        }
    }
}

public class NoFeeStrategy implements FeeCalculationStrategy {
    @Override
    public double calculateFee(Transaction transaction) {
        return 0.0; // Pas de frais (clients premium)
    }
}

// Utilisation dans BankAccount
public class BankAccount {
    private FeeCalculationStrategy feeStrategy;

    public void setFeeStrategy(FeeCalculationStrategy strategy) {
        this.feeStrategy = strategy;
    }

    public double calculateTransactionFee(Transaction transaction) {
        return feeStrategy.calculateFee(transaction);
    }
}
```

**🎓 Points à discuter** :
- Élimination des conditionnelles
- Changement de stratégie à runtime
- Combinaison avec Factory

---

#### 12. Exercice 11 : Template Method (1h30)

**Solution attendue** :

```java
public abstract class TransactionProcessor {

    // Template method
    public final boolean processTransaction(Transaction transaction) {
        // 1. Validation
        if (!validate(transaction)) {
            transaction.setStatus("REJECTED");
            transaction.setRejectionReason(getValidationError());
            return false;
        }

        // 2. Exécution
        boolean success = executeTransaction(transaction);

        if (success) {
            // 3. Post-traitement
            postProcess(transaction);

            // 4. Notification
            notifyCustomer(transaction);

            transaction.setStatus("COMPLETED");
        } else {
            transaction.setStatus("REJECTED");
        }

        // 5. Audit (toujours effectué)
        audit(transaction);

        return success;
    }

    // Méthodes abstraites (à implémenter)
    protected abstract boolean validate(Transaction transaction);
    protected abstract boolean executeTransaction(Transaction transaction);
    protected abstract void notifyCustomer(Transaction transaction);

    // Méthodes avec implémentation par défaut (hooks)
    protected void postProcess(Transaction transaction) {
        // Comportement par défaut (peut être surchargé)
    }

    protected String getValidationError() {
        return "Validation failed";
    }

    private void audit(Transaction transaction) {
        System.out.println("Audit: " + transaction.getTransactionId() +
                          " - " + transaction.getStatus());
    }
}

public class DepositProcessor extends TransactionProcessor {
    private BankingService bankingService;

    public DepositProcessor(BankingService service) {
        this.bankingService = service;
    }

    @Override
    protected boolean validate(Transaction transaction) {
        if (transaction.getAmount() <= 0) {
            return false;
        }
        if (transaction.getAmount() > 10000) {
            return false; // Nécessite vérification manuelle
        }
        BankAccount account = bankingService.getAccount(transaction.getDestinationAccount());
        return account != null;
    }

    @Override
    protected boolean executeTransaction(Transaction transaction) {
        BankAccount account = bankingService.getAccount(transaction.getDestinationAccount());
        account.setBalance(account.getBalance() + transaction.getAmount());
        return true;
    }

    @Override
    protected void notifyCustomer(Transaction transaction) {
        BankAccount account = bankingService.getAccount(transaction.getDestinationAccount());
        System.out.println("Email sent to: " + account.getCustomerEmail() +
                          " - Deposit of " + transaction.getAmount() + " EUR");
    }
}

public class WithdrawalProcessor extends TransactionProcessor {
    private BankingService bankingService;
    private FeeCalculationStrategy feeStrategy;

    public WithdrawalProcessor(BankingService service, FeeCalculationStrategy feeStrategy) {
        this.bankingService = service;
        this.feeStrategy = feeStrategy;
    }

    @Override
    protected boolean validate(Transaction transaction) {
        if (transaction.getAmount() <= 0) {
            return false;
        }
        BankAccount account = bankingService.getAccount(transaction.getSourceAccount());
        if (account == null) {
            return false;
        }

        double fees = feeStrategy.calculateFee(transaction);
        double totalAmount = transaction.getAmount() + fees;

        return account.getBalance() - totalAmount >= -account.getOverdraftLimit();
    }

    @Override
    protected boolean executeTransaction(Transaction transaction) {
        BankAccount account = bankingService.getAccount(transaction.getSourceAccount());
        double fees = feeStrategy.calculateFee(transaction);
        double totalAmount = transaction.getAmount() + fees;

        account.setBalance(account.getBalance() - totalAmount);
        return true;
    }

    @Override
    protected void postProcess(Transaction transaction) {
        BankAccount account = bankingService.getAccount(transaction.getSourceAccount());
        if (account.getBalance() < 0) {
            System.out.println("ALERT: Account in overdraft!");
        }
    }

    @Override
    protected void notifyCustomer(Transaction transaction) {
        BankAccount account = bankingService.getAccount(transaction.getSourceAccount());
        double fees = feeStrategy.calculateFee(transaction);

        System.out.println("Email sent to: " + account.getCustomerEmail() +
                          " - Withdrawal of " + transaction.getAmount() +
                          " EUR (fees: " + fees + " EUR)");

        if (account.getBalance() < 0) {
            System.out.println("SMS sent to: " + account.getCustomerPhone() +
                              " - Overdraft alert!");
        }
    }
}

public class TransferProcessor extends TransactionProcessor {
    private BankingService bankingService;
    private FeeCalculationStrategy feeStrategy;

    public TransferProcessor(BankingService service, FeeCalculationStrategy feeStrategy) {
        this.bankingService = service;
        this.feeStrategy = feeStrategy;
    }

    @Override
    protected boolean validate(Transaction transaction) {
        if (transaction.getAmount() <= 0) {
            return false;
        }

        BankAccount source = bankingService.getAccount(transaction.getSourceAccount());
        BankAccount destination = bankingService.getAccount(transaction.getDestinationAccount());

        if (source == null || destination == null) {
            return false;
        }

        double fees = feeStrategy.calculateFee(transaction);
        double totalAmount = transaction.getAmount() + fees;

        return source.getBalance() - totalAmount >= -source.getOverdraftLimit();
    }

    @Override
    protected boolean executeTransaction(Transaction transaction) {
        BankAccount source = bankingService.getAccount(transaction.getSourceAccount());
        BankAccount destination = bankingService.getAccount(transaction.getDestinationAccount());

        double fees = feeStrategy.calculateFee(transaction);
        double totalAmount = transaction.getAmount() + fees;

        source.setBalance(source.getBalance() - totalAmount);
        destination.setBalance(destination.getBalance() + transaction.getAmount());

        return true;
    }

    @Override
    protected void notifyCustomer(Transaction transaction) {
        BankAccount source = bankingService.getAccount(transaction.getSourceAccount());
        BankAccount destination = bankingService.getAccount(transaction.getDestinationAccount());

        System.out.println("Email sent to: " + source.getCustomerEmail() +
                          " - Transfer sent: " + transaction.getAmount() + " EUR");
        System.out.println("Email sent to: " + destination.getCustomerEmail() +
                          " - Transfer received: " + transaction.getAmount() + " EUR");
    }
}
```

**🎓 Points à discuter** :
- Algorithme général vs variations
- Hollywood Principle : "Don't call us, we'll call you"
- Hooks optionnels
- Template Method vs Strategy

---

### Jour 3 - Matin : Suite Patterns Comportementaux

#### 13. Exercice 12 : Chain of Responsibility (1h30)

**Solution attendue** :

```java
public interface TransactionValidator {
    void setNext(TransactionValidator next);
    ValidationResult validate(Transaction transaction, BankingService service);
}

public class ValidationResult {
    private boolean valid;
    private String errorMessage;

    public ValidationResult(boolean valid, String errorMessage) {
        this.valid = valid;
        this.errorMessage = errorMessage;
    }

    public boolean isValid() { return valid; }
    public String getErrorMessage() { return errorMessage; }

    public static ValidationResult success() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult failure(String message) {
        return new ValidationResult(false, message);
    }
}

public abstract class AbstractTransactionValidator implements TransactionValidator {
    protected TransactionValidator next;

    @Override
    public void setNext(TransactionValidator next) {
        this.next = next;
    }

    protected ValidationResult validateNext(Transaction transaction, BankingService service) {
        if (next != null) {
            return next.validate(transaction, service);
        }
        return ValidationResult.success();
    }
}

public class AmountValidator extends AbstractTransactionValidator {
    @Override
    public ValidationResult validate(Transaction transaction, BankingService service) {
        if (transaction.getAmount() <= 0) {
            return ValidationResult.failure("Amount must be positive");
        }

        double maxAmount = BankingConfiguration.getInstance().getMaxTransferAmount();
        if (transaction.getAmount() > maxAmount) {
            return ValidationResult.failure("Amount exceeds maximum limit: " + maxAmount);
        }

        return validateNext(transaction, service);
    }
}

public class AccountExistsValidator extends AbstractTransactionValidator {
    @Override
    public ValidationResult validate(Transaction transaction, BankingService service) {
        if (transaction.getSourceAccount() != null) {
            BankAccount source = service.getAccount(transaction.getSourceAccount());
            if (source == null) {
                return ValidationResult.failure("Source account not found");
            }
        }

        if (transaction.getDestinationAccount() != null) {
            BankAccount destination = service.getAccount(transaction.getDestinationAccount());
            if (destination == null) {
                return ValidationResult.failure("Destination account not found");
            }
        }

        return validateNext(transaction, service);
    }
}

public class BalanceValidator extends AbstractTransactionValidator {
    private FeeCalculationStrategy feeStrategy;

    public BalanceValidator(FeeCalculationStrategy feeStrategy) {
        this.feeStrategy = feeStrategy;
    }

    @Override
    public ValidationResult validate(Transaction transaction, BankingService service) {
        if (transaction.getSourceAccount() == null) {
            return validateNext(transaction, service);
        }

        BankAccount account = service.getAccount(transaction.getSourceAccount());
        double fees = feeStrategy.calculateFee(transaction);
        double totalAmount = transaction.getAmount() + fees;

        if (account.getBalance() - totalAmount < -account.getOverdraftLimit()) {
            return ValidationResult.failure("Insufficient funds (overdraft limit exceeded)");
        }

        return validateNext(transaction, service);
    }
}

public class DailyLimitValidator extends AbstractTransactionValidator {
    private Map<String, DailyTransactionTracker> trackers = new HashMap<>();

    @Override
    public ValidationResult validate(Transaction transaction, BankingService service) {
        if (transaction.getSourceAccount() == null) {
            return validateNext(transaction, service);
        }

        String accountNumber = transaction.getSourceAccount();
        DailyTransactionTracker tracker = trackers.computeIfAbsent(
            accountNumber, k -> new DailyTransactionTracker());

        double maxDaily = BankingConfiguration.getInstance().getMaxDailyWithdrawal();
        double todayTotal = tracker.getTodayTotal();

        if (todayTotal + transaction.getAmount() > maxDaily) {
            return ValidationResult.failure("Daily withdrawal limit exceeded");
        }

        tracker.addTransaction(transaction.getAmount());
        return validateNext(transaction, service);
    }
}

public class AntifraudValidator extends AbstractTransactionValidator {
    @Override
    public ValidationResult validate(Transaction transaction, BankingService service) {
        // Logique anti-fraude simple
        if (transaction.getAmount() > 5000 && isNightTime()) {
            return ValidationResult.failure("Large night-time transaction flagged for review");
        }

        if (transaction.getSourceAccount() != null &&
            transaction.getDestinationAccount() != null) {
            // Vérifier si les deux comptes sont dans des pays différents (simulation)
            if (isCrossBorder(transaction)) {
                System.out.println("Cross-border transaction - additional verification required");
            }
        }

        return validateNext(transaction, service);
    }

    private boolean isNightTime() {
        int hour = java.time.LocalTime.now().getHour();
        return hour >= 23 || hour <= 6;
    }

    private boolean isCrossBorder(Transaction transaction) {
        // Simulation
        return false;
    }
}

// Utilisation
public class ValidationChainBuilder {
    public static TransactionValidator buildChain(FeeCalculationStrategy feeStrategy) {
        TransactionValidator amountValidator = new AmountValidator();
        TransactionValidator accountValidator = new AccountExistsValidator();
        TransactionValidator balanceValidator = new BalanceValidator(feeStrategy);
        TransactionValidator dailyLimitValidator = new DailyLimitValidator();
        TransactionValidator antifraudValidator = new AntifraudValidator();

        amountValidator.setNext(accountValidator);
        accountValidator.setNext(balanceValidator);
        balanceValidator.setNext(dailyLimitValidator);
        dailyLimitValidator.setNext(antifraudValidator);

        return amountValidator;
    }
}
```

**🎓 Points à discuter** :
- Responsabilité unique par validateur
- Ordre de la chaîne important
- Possibilité de court-circuiter
- Ajout facile de nouveaux validateurs

---

#### 14. Exercice 13 : Observer (2h)

**Solution attendue** :

```java
public interface TransactionObserver {
    void onTransactionCompleted(Transaction transaction, BankingService service);
}

public class EmailNotificationObserver implements TransactionObserver {
    @Override
    public void onTransactionCompleted(Transaction transaction, BankingService service) {
        if (transaction.getSourceAccount() != null) {
            BankAccount source = service.getAccount(transaction.getSourceAccount());
            sendEmail(source.getCustomerEmail(),
                     "Transaction completed: " + transaction.getType() +
                     " - " + transaction.getAmount() + " EUR");
        }

        if (transaction.getDestinationAccount() != null) {
            BankAccount destination = service.getAccount(transaction.getDestinationAccount());
            sendEmail(destination.getCustomerEmail(),
                     "Transaction received: " + transaction.getType() +
                     " - " + transaction.getAmount() + " EUR");
        }
    }

    private void sendEmail(String email, String message) {
        System.out.println("EMAIL to " + email + ": " + message);
    }
}

public class SmsNotificationObserver implements TransactionObserver {
    @Override
    public void onTransactionCompleted(Transaction transaction, BankingService service) {
        // Envoyer SMS uniquement pour les gros montants
        if (transaction.getAmount() > 1000) {
            if (transaction.getSourceAccount() != null) {
                BankAccount source = service.getAccount(transaction.getSourceAccount());
                sendSms(source.getCustomerPhone(),
                       "Large transaction alert: " + transaction.getAmount() + " EUR");
            }
        }

        // SMS pour découvert
        if (transaction.getSourceAccount() != null) {
            BankAccount source = service.getAccount(transaction.getSourceAccount());
            if (source.getBalance() < 0) {
                sendSms(source.getCustomerPhone(), "Overdraft alert! Balance: " + source.getBalance());
            }
        }
    }

    private void sendSms(String phone, String message) {
        System.out.println("SMS to " + phone + ": " + message);
    }
}

public class AuditLogObserver implements TransactionObserver {
    @Override
    public void onTransactionCompleted(Transaction transaction, BankingService service) {
        // Log dans un fichier ou base de données
        System.out.println("AUDIT: Transaction " + transaction.getTransactionId() +
                          " | Type: " + transaction.getType() +
                          " | Amount: " + transaction.getAmount() +
                          " | Status: " + transaction.getStatus() +
                          " | Date: " + transaction.getTransactionDate());
    }
}

public class FraudDetectionObserver implements TransactionObserver {
    private Map<String, List<Transaction>> recentTransactions = new HashMap<>();

    @Override
    public void onTransactionCompleted(Transaction transaction, BankingService service) {
        String accountNumber = transaction.getSourceAccount();
        if (accountNumber == null) return;

        List<Transaction> recent = recentTransactions.computeIfAbsent(
            accountNumber, k -> new ArrayList<>());
        recent.add(transaction);

        // Nettoyer les anciennes transactions (> 1 heure)
        cleanOldTransactions(recent);

        // Détecter les patterns suspects
        int threshold = BankingConfiguration.getInstance().getFraudAlertThreshold();
        if (recent.size() > threshold) {
            System.out.println("FRAUD ALERT: Account " + accountNumber +
                              " has " + recent.size() +
                              " transactions in the last hour!");
            // Bloquer le compte ou demander vérification
        }
    }

    private void cleanOldTransactions(List<Transaction> transactions) {
        long oneHourAgo = System.currentTimeMillis() - (60 * 60 * 1000);
        transactions.removeIf(tx -> tx.getTransactionDate().getTime() < oneHourAgo);
    }
}

// Subject (Observable)
public class TransactionSubject {
    private List<TransactionObserver> observers = new ArrayList<>();

    public void addObserver(TransactionObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TransactionObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Transaction transaction, BankingService service) {
        for (TransactionObserver observer : observers) {
            observer.onTransactionCompleted(transaction, service);
        }
    }
}

// Intégration dans BankingService
public class BankingService {
    private TransactionSubject transactionSubject = new TransactionSubject();

    public BankingService() {
        // Configuration des observateurs par défaut
        transactionSubject.addObserver(new EmailNotificationObserver());
        transactionSubject.addObserver(new SmsNotificationObserver());
        transactionSubject.addObserver(new AuditLogObserver());
        transactionSubject.addObserver(new FraudDetectionObserver());
    }

    public boolean processTransaction(...) {
        // ... logique existante ...

        if (success) {
            transactionSubject.notifyObservers(transaction, this);
        }

        return success;
    }

    public void addTransactionObserver(TransactionObserver observer) {
        transactionSubject.addObserver(observer);
    }

    public void removeTransactionObserver(TransactionObserver observer) {
        transactionSubject.removeObserver(observer);
    }
}
```

**🎓 Points à discuter** :
- Découplage entre sujet et observateurs
- Push vs Pull model
- Gestion des erreurs dans les observateurs
- Performance (beaucoup d'observateurs)
- Observer vs EventBus moderne

---

### Jour 3 - Après-midi : Nouvelles Fonctionnalités + Synthèse

#### 15. Nouvelles fonctionnalités (2-3h)

Laissez les participants choisir parmi les fonctionnalités proposées et les implémenter en autonomie, en utilisant les patterns appropriés.

**Accompagnement** :
- Faire des revues de code intermédiaires
- Encourager les discussions en groupe
- Valider les choix de patterns

#### 16. Synthèse et Discussion (1h)

**Questions de discussion** :
1. Quels patterns avez-vous trouvé les plus utiles ?
2. Lesquels sont les plus difficiles à implémenter ?
3. Comment décidez-vous quel pattern utiliser ?
4. Quels sont les risques d'over-engineering ?

**Récap des patterns** :
- Créationnels : quand la création est complexe
- Structurels : quand on compose des objets
- Comportementaux : quand on gère des algorithmes et responsabilités

---

## 🎓 Conseils pour l'instructeur

### Préparation
1. Testez tous les exercices au préalable
2. Préparez des branches Git avec les solutions
3. Ayez des exemples de code alternatifs

### Pendant la formation
1. **Encouragez la discussion** : les patterns sont subjectifs
2. **Faites des live-coding** : montrez comment refactorer étape par étape
3. **Adaptez le rythme** : certains participants iront plus vite
4. **Utilisez les tests** : montrez que le refactoring ne casse rien
5. **Code reviews** : examinez les solutions des participants

### Pièges courants à éviter
1. **Over-engineering** : ne pas utiliser un pattern juste pour l'utiliser
2. **Pattern obsession** : parfois, du code simple suffit
3. **Refactoring brutal** : refactorer progressivement
4. **Ignorer les tests** : toujours garder les tests verts

### Variantes possibles
1. **Formation courte (1 jour)** : focus sur 6-8 patterns essentiels
2. **Formation longue (5 jours)** : ajouter des patterns avancés (Memento, State, Command, etc.)
3. **Format workshop** : alternance théorie/pratique avec des sessions de 30 min

---

## 📊 Évaluation des participants

### Grille d'évaluation suggérée

| Critère | Points |
|---------|--------|
| Identification des code smells | /10 |
| Application correcte des patterns | /30 |
| Code propre et maintenable | /20 |
| Tests fonctionnels | /10 |
| Compréhension conceptuelle | /15 |
| Nouvelles fonctionnalités | /15 |
| **Total** | **/100** |

### Questions d'évaluation finale

1. Expliquez la différence entre Factory Method et Abstract Factory
2. Quand utiliseriez-vous Decorator plutôt que l'héritage ?
3. Donnez un exemple d'utilisation de Strategy dans un contexte différent
4. Comment testez-vous du code qui utilise un Singleton ?
5. Proposez un nouveau pattern pour une fonctionnalité bancaire

---

## 📚 Ressources complémentaires

### Livres recommandés
- "Design Patterns" - Gang of Four
- "Head First Design Patterns" - Freeman & Robson
- "Refactoring" - Martin Fowler
- "Clean Code" - Robert Martin

### Sites web
- https://refactoring.guru
- https://sourcemaking.com
- https://java-design-patterns.com

---

Bonne formation ! 🚀
