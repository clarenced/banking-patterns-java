# Guide de l'Instructeur - Formation Design Patterns

## 📋 Vue d'ensemble de la formation

### Durée estimée
- **Total** : 2-3 jours (16-24 heures)
- **Partie 1 - Comportementaux** : 2-3 heures (Strategy)
- **Partie 2 - Créationnels** : 5-7 heures (Builder, Factory, Abstract Factory)
- **Partie 3 - Structurels** : 5-7 heures (Adapter, Facade, Decorator, Chain of Responsibility)
- **Partie 4 - Patterns additionnels** : 3-5 heures (si temps disponible)
- **Partie 5 - Nouvelles fonctionnalités** : 2-4 heures

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

### Jour 1 - Matin : Introduction et Pattern Strategy

#### 1. Introduction (30 min)
- Présentation du projet bancaire
- Explication du code legacy
- Démonstration de l'application
- Identification collective des problèmes

**🎓 Point pédagogique** : Faites exécuter le code et demandez aux participants d'identifier au moins 5 problèmes majeurs, notamment les if/else dans le calcul des frais.

**⚠️ Important** : Insistez sur le fait qu'on commence par Strategy car c'est le problème le plus visible et le plus facile à isoler dans le code legacy.

---

#### 2. Exercice 1 : Strategy Pattern (2h)

**Problèmes à identifier** :
```java
// Dans BankingService.processTransaction() - lignes 120-134 et 179-187
// CALCUL DE FRAIS EN DUR avec IF/ELSE
double fees = 0;
if (account.getAccountType().equals("COURANT")) {
    if (amount > 1000) {
        fees = 2.5;
    }
} else if (account.getAccountType().equals("EPARGNE")) {
    fees = 1.0;
} else if (account.getAccountType().equals("PROFESSIONNEL")) {
    if (amount > 5000) {
        fees = 5.0;
    } else {
        fees = 2.0;
    }
}
```

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
                return TRANSFER_FEE;
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
        return 0.0; // Pour clients premium
    }
}
```

**🎓 Points à discuter** :
- Élimination des if/else : Open/Closed Principle
- Testabilité : chaque stratégie peut être testée indépendamment
- Changement de stratégie à runtime (ex: promotion client vers premium)
- **Très important** : ces stratégies seront réutilisées dans les exercices suivants (Abstract Factory, Chain of Responsibility)

**💡 Astuce pédagogique** : Montrez les tests unitaires pour les frais (déjà présents dans BankingServiceTest) et comment ils valident que le refactoring ne casse rien.

---

### Jour 1 - Après-midi : Patterns Créationnels

#### 3. Exercice 2 : Builder Pattern (1h30)

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

#### 4. Exercice 3 : Factory Pattern (1h30)

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

### Jour 1 - Après-midi (suite) : Abstract Factory

#### 5. Exercice 4 : Abstract Factory (1h30)

**⚠️ Point clé** : L'Abstract Factory **réutilise** les `FeeCalculationStrategy` créées dans l'Exercice 1. C'est important pour montrer la cohésion entre les patterns !

**Solution attendue** :

```java
public interface BankingPackageFactory {
    BankAccount createAccount(String name, String email, String phone, double deposit);
    FeeCalculationStrategy createFeeCalculationStrategy();
    NotificationService createNotificationService();
}

public class StandardBankingPackage implements BankingPackageFactory {
    @Override
    public BankAccount createAccount(String name, String email, String phone, double deposit) {
        return new CurrentAccountFactory().createAccount(name, email, phone, deposit);
    }

    @Override
    public FeeCalculationStrategy createFeeCalculationStrategy() {
        // Réutilise la stratégie de l'Exercice 1
        return new CurrentAccountFeeStrategy();
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
    public FeeCalculationStrategy createFeeCalculationStrategy() {
        // Les clients premium n'ont pas de frais !
        return new NoFeeStrategy();
    }

    @Override
    public NotificationService createNotificationService() {
        return new MultiChannelNotificationService(); // Email + SMS
    }
}

public class BusinessBankingPackage implements BankingPackageFactory {
    @Override
    public BankAccount createAccount(String name, String email, String phone, double deposit) {
        return new BusinessAccountFactory().createAccount(name, email, phone, deposit);
    }

    @Override
    public FeeCalculationStrategy createFeeCalculationStrategy() {
        // Frais réduits pour les professionnels
        return new BusinessAccountFeeStrategy();
    }

    @Override
    public NotificationService createNotificationService() {
        return new MultiChannelNotificationService(); // Email + SMS
    }
}
```

**🎓 Points à discuter** :
- **Cohérence des familles** : un package premium crée un compte ET une stratégie sans frais
- **Réutilisation** : on ne crée pas de nouveau concept (FeeCalculator), on réutilise FeeCalculationStrategy
- Différence avec Factory simple : ici on crée une **famille** d'objets cohérents
- Utilisation pratique : changement de package client (standard → premium) change tout automatiquement

---

### Jour 2 - Matin : Patterns Comportementaux

#### 5. Exercice 8 : Command Pattern (1h30)

**Problème à identifier** :
```java
// Les transactions sont exécutées immédiatement sans historique
account.setBalance(account.getBalance() + amount);
tx.setStatus("COMPLETED");
// Pas de possibilité d'annulation ou de rejeu
```

**Solution attendue** :

```java
// Interface Command
public interface BankCommand {
    boolean execute();
    boolean undo();
    String getDescription();
}

// Commande concrète : Dépôt
public class DepositCommand implements BankCommand {
    private BankAccount account;
    private double amount;
    private boolean executed = false;

    public DepositCommand(BankAccount account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public boolean execute() {
        if (amount <= 0) return false;
        account.setBalance(account.getBalance() + amount);
        executed = true;
        System.out.println("Deposited " + amount + " EUR");
        return true;
    }

    @Override
    public boolean undo() {
        if (!executed) return false;
        account.setBalance(account.getBalance() - amount);
        executed = false;
        System.out.println("Undo deposit of " + amount + " EUR");
        return true;
    }

    @Override
    public String getDescription() {
        return "Deposit " + amount + " EUR to account " + account.getAccountNumber();
    }
}

// Commande concrète : Retrait
public class WithdrawCommand implements BankCommand {
    private BankAccount account;
    private double amount;
    private boolean executed = false;

    public WithdrawCommand(BankAccount account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public boolean execute() {
        if (account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            executed = true;
            System.out.println("Withdrew " + amount + " EUR");
            return true;
        }
        System.out.println("Insufficient funds");
        return false;
    }

    @Override
    public boolean undo() {
        if (!executed) return false;
        account.setBalance(account.getBalance() + amount);
        executed = false;
        System.out.println("Undo withdrawal of " + amount + " EUR");
        return true;
    }

    @Override
    public String getDescription() {
        return "Withdraw " + amount + " EUR from account " + account.getAccountNumber();
    }
}

// Macro Command : Virement
public class TransferCommand implements BankCommand {
    private WithdrawCommand withdraw;
    private DepositCommand deposit;

    public TransferCommand(BankAccount from, BankAccount to, double amount) {
        this.withdraw = new WithdrawCommand(from, amount);
        this.deposit = new DepositCommand(to, amount);
    }

    @Override
    public boolean execute() {
        if (withdraw.execute()) {
            if (deposit.execute()) {
                System.out.println("Transfer completed");
                return true;
            } else {
                withdraw.undo(); // Rollback
                System.out.println("Transfer failed - rollback");
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean undo() {
        deposit.undo();
        withdraw.undo();
        System.out.println("Transfer undone");
        return true;
    }

    @Override
    public String getDescription() {
        return "Transfer from " + withdraw.getDescription() + " to " + deposit.getDescription();
    }
}

// Invoker : Exécuteur de transactions
public class TransactionExecutor {
    private Stack<BankCommand> executedCommands = new Stack<>();
    private Stack<BankCommand> undoneCommands = new Stack<>();

    public boolean execute(BankCommand command) {
        if (command.execute()) {
            executedCommands.push(command);
            undoneCommands.clear(); // Efface l'historique redo
            return true;
        }
        return false;
    }

    public boolean undo() {
        if (executedCommands.isEmpty()) {
            System.out.println("Nothing to undo");
            return false;
        }
        BankCommand command = executedCommands.pop();
        if (command.undo()) {
            undoneCommands.push(command);
            return true;
        }
        return false;
    }

    public boolean redo() {
        if (undoneCommands.isEmpty()) {
            System.out.println("Nothing to redo");
            return false;
        }
        BankCommand command = undoneCommands.pop();
        if (command.execute()) {
            executedCommands.push(command);
            return true;
        }
        return false;
    }

    public void printHistory() {
        System.out.println("Command history:");
        for (BankCommand cmd : executedCommands) {
            System.out.println("  - " + cmd.getDescription());
        }
    }
}
```

**🎓 Points à discuter** :
- Command encapsule une requête en objet
- Permet undo/redo facilement
- Macro commands pour opérations composées (TransferCommand)
- Facilite la journalisation et les transactions
- Possibilité de mettre en file d'attente
- Command vs simple méthode : quand utiliser Command ?

**💡 Astuce pédagogique** : Montrez l'analogie avec Ctrl+Z / Ctrl+Y dans un éditeur de texte.

---

#### 6. Exercice 9 : State Pattern (1h30)

**Problème à identifier** :
```java
// if/else pour gérer les états
if (account.getStatus().equals("ACTIVE")) {
    // Autoriser
} else if (account.getStatus().equals("SUSPENDED")) {
    // Limiter
} else if (account.getStatus().equals("FROZEN")) {
    // Bloquer
}
```

**Solution attendue** :

```java
// Interface State
public interface AccountState {
    boolean canDeposit();
    boolean canWithdraw();
    boolean canTransfer();
    double getWithdrawalLimit();
    String getStateName();
    void handleStateTransition(BankAccount account, String action);
}

// État concret : Actif
public class ActiveState implements AccountState {
    @Override
    public boolean canDeposit() { return true; }

    @Override
    public boolean canWithdraw() { return true; }

    @Override
    public boolean canTransfer() { return true; }

    @Override
    public double getWithdrawalLimit() { return Double.MAX_VALUE; }

    @Override
    public String getStateName() { return "ACTIVE"; }

    @Override
    public void handleStateTransition(BankAccount account, String action) {
        if (action.equals("SUSPEND")) {
            account.setState(new SuspendedState());
        } else if (action.equals("FREEZE")) {
            account.setState(new FrozenState());
        }
    }
}

// État concret : Suspendu
public class SuspendedState implements AccountState {
    private static final double DAILY_LIMIT = 500.0;

    @Override
    public boolean canDeposit() { return true; }

    @Override
    public boolean canWithdraw() { return true; } // Mais limité

    @Override
    public boolean canTransfer() { return false; }

    @Override
    public double getWithdrawalLimit() { return DAILY_LIMIT; }

    @Override
    public String getStateName() { return "SUSPENDED"; }

    @Override
    public void handleStateTransition(BankAccount account, String action) {
        if (action.equals("ACTIVATE")) {
            account.setState(new ActiveState());
        } else if (action.equals("FREEZE")) {
            account.setState(new FrozenState());
        }
    }
}

// État concret : Gelé
public class FrozenState implements AccountState {
    @Override
    public boolean canDeposit() { return false; }

    @Override
    public boolean canWithdraw() { return false; }

    @Override
    public boolean canTransfer() { return false; }

    @Override
    public double getWithdrawalLimit() { return 0; }

    @Override
    public String getStateName() { return "FROZEN"; }

    @Override
    public void handleStateTransition(BankAccount account, String action) {
        if (action.equals("UNFREEZE")) {
            account.setState(new ActiveState());
        } else if (action.equals("CLOSE")) {
            account.setState(new ClosedState());
        }
    }
}

// État concret : Fermé
public class ClosedState implements AccountState {
    @Override
    public boolean canDeposit() { return false; }

    @Override
    public boolean canWithdraw() { return false; }

    @Override
    public boolean canTransfer() { return false; }

    @Override
    public double getWithdrawalLimit() { return 0; }

    @Override
    public String getStateName() { return "CLOSED"; }

    @Override
    public void handleStateTransition(BankAccount account, String action) {
        System.out.println("Cannot change state of closed account");
    }
}

// Modification de BankAccount
public class BankAccount {
    private AccountState state;
    // ... autres champs

    public BankAccount(...) {
        // ...
        this.state = new ActiveState(); // État initial
    }

    public boolean deposit(double amount) {
        if (!state.canDeposit()) {
            System.out.println("Deposits not allowed in " + state.getStateName());
            return false;
        }
        balance += amount;
        return true;
    }

    public boolean withdraw(double amount) {
        if (!state.canWithdraw()) {
            System.out.println("Withdrawals not allowed in " + state.getStateName());
            return false;
        }
        if (amount > state.getWithdrawalLimit()) {
            System.out.println("Amount exceeds withdrawal limit");
            return false;
        }
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void changeState(String action) {
        state.handleStateTransition(this, action);
    }

    public void setState(AccountState newState) {
        System.out.println("State changed from " + state.getStateName() +
                          " to " + newState.getStateName());
        this.state = newState;
    }
}
```

**🎓 Points à discuter** :
- State encapsule le comportement lié à l'état
- Élimine les conditionnelles multiples (Open/Closed Principle)
- Facilite l'ajout de nouveaux états
- Les transitions sont gérées par les états eux-mêmes
- State vs Strategy : différence ?
- Diagramme d'états important pour la conception

**💡 Astuce pédagogique** : Dessinez le diagramme de transitions d'états au tableau pour visualiser.

---

### Jour 2 - Après-midi : Patterns Structurels

#### 7. Exercice 7 : Adapter Pattern (1h)

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

### Jour 2 - Après-midi : Patterns Structurels (suite)

#### 8. Exercice 10 : Composite Pattern (1h)

**Problème à identifier** :
```java
// Gestion séparée des comptes individuels et portefeuilles
// Pas de traitement uniforme
```

**Solution attendue** :

```java
// Interface Component
public interface AccountComponent {
    double getBalance();
    boolean deposit(double amount);
    boolean withdraw(double amount);
    String getAccountInfo();
    void addChild(AccountComponent component);
    void removeChild(AccountComponent component);
    List<AccountComponent> getChildren();
}

// Leaf : Compte individuel
public class IndividualAccount implements AccountComponent {
    private BankAccount account;

    public IndividualAccount(BankAccount account) {
        this.account = account;
    }

    @Override
    public double getBalance() {
        return account.getBalance();
    }

    @Override
    public boolean deposit(double amount) {
        account.setBalance(account.getBalance() + amount);
        return true;
    }

    @Override
    public boolean withdraw(double amount) {
        if (account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            return true;
        }
        return false;
    }

    @Override
    public String getAccountInfo() {
        return "Account " + account.getAccountNumber() +
               ": " + account.getBalance() + " EUR";
    }

    @Override
    public void addChild(AccountComponent component) {
        throw new UnsupportedOperationException("Cannot add to leaf");
    }

    @Override
    public void removeChild(AccountComponent component) {
        throw new UnsupportedOperationException("Cannot remove from leaf");
    }

    @Override
    public List<AccountComponent> getChildren() {
        return Collections.emptyList();
    }
}

// Composite : Portefeuille de comptes
public class AccountPortfolio implements AccountComponent {
    private String name;
    private List<AccountComponent> accounts = new ArrayList<>();

    public AccountPortfolio(String name) {
        this.name = name;
    }

    @Override
    public double getBalance() {
        return accounts.stream()
                      .mapToDouble(AccountComponent::getBalance)
                      .sum();
    }

    @Override
    public boolean deposit(double amount) {
        if (accounts.isEmpty()) return false;

        // Répartir équitablement sur tous les comptes
        double amountPerAccount = amount / accounts.size();
        for (AccountComponent account : accounts) {
            account.deposit(amountPerAccount);
        }
        return true;
    }

    @Override
    public boolean withdraw(double amount) {
        // Vérifier si le solde total est suffisant
        if (getBalance() < amount) {
            return false;
        }

        // Retirer proportionnellement
        double amountPerAccount = amount / accounts.size();
        for (AccountComponent account : accounts) {
            if (!account.withdraw(amountPerAccount)) {
                // Rollback si un retrait échoue
                return false;
            }
        }
        return true;
    }

    @Override
    public String getAccountInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Portfolio: ").append(name).append("\n");
        info.append("Total Balance: ").append(getBalance()).append(" EUR\n");
        info.append("Accounts:\n");
        for (AccountComponent account : accounts) {
            info.append("  - ").append(account.getAccountInfo()).append("\n");
        }
        return info.toString();
    }

    @Override
    public void addChild(AccountComponent component) {
        accounts.add(component);
        System.out.println("Added account to portfolio " + name);
    }

    @Override
    public void removeChild(AccountComponent component) {
        accounts.remove(component);
        System.out.println("Removed account from portfolio " + name);
    }

    @Override
    public List<AccountComponent> getChildren() {
        return new ArrayList<>(accounts);
    }
}
```

**🎓 Points à discuter** :
- Composite traite objets individuels et compositions uniformément
- Structure en arbre (portfolios de portfolios possibles)
- Opérations récursives (getBalance, deposit, withdraw)
- Cas d'usage : comptes familiaux, comptes entreprise avec sous-comptes
- Permet des hiérarchies complexes
- Attention au rollback en cas d'échec partiel

---

## 🔄 PATTERNS ADDITIONNELS (SI TEMPS DISPONIBLE)

### Jour 2 - Après-midi (suite) / Jour 3 - Patterns additionnels

#### 9. Exercice 9 : Decorator Pattern (1h30)

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

#### 10. Exercice 10 : Facade Pattern (1h)

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

### Jour 2 - Après-midi (suite) : Patterns Comportementaux

#### 11. Exercice 11 : Chain of Responsibility Pattern (1h30)

**Problème à identifier** :
```java
// Toutes les validations sont dans processTransaction (lignes 72-214)
// C'est un monstre de if/else imbriqués !
```

**Solution attendue** :

```java
// Interface Handler
public interface TransactionValidator {
    void setNext(TransactionValidator next);
    ValidationResult validate(Transaction transaction, BankingService service);
}

// Classe de résultat
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

// Classe abstraite de base
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

// Validateur de montant
public class AmountValidator extends AbstractTransactionValidator {
    @Override
    public ValidationResult validate(Transaction transaction, BankingService service) {
        if (transaction.getAmount() <= 0) {
            return ValidationResult.failure("Amount must be positive");
        }

        if (transaction.getAmount() > 50000) {
            return ValidationResult.failure("Amount exceeds maximum limit (50000 EUR)");
        }

        return validateNext(transaction, service);
    }
}

// Validateur d'existence de compte
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
            BankAccount dest = service.getAccount(transaction.getDestinationAccount());
            if (dest == null) {
                return ValidationResult.failure("Destination account not found");
            }
        }

        return validateNext(transaction, service);
    }
}

// Validateur de solde
public class BalanceValidator extends AbstractTransactionValidator {
    @Override
    public ValidationResult validate(Transaction transaction, BankingService service) {
        if (transaction.getSourceAccount() == null) {
            return validateNext(transaction, service);
        }

        BankAccount account = service.getAccount(transaction.getSourceAccount());
        double requiredAmount = transaction.getAmount(); // + fees si applicable

        if (account.getBalance() - requiredAmount < -account.getOverdraftLimit()) {
            return ValidationResult.failure("Insufficient funds (overdraft limit exceeded)");
        }

        return validateNext(transaction, service);
    }
}

// Validateur de limite quotidienne
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

        double todayTotal = tracker.getTodayTotal();
        double maxDaily = 1000.0; // Configuration

        if (todayTotal + transaction.getAmount() > maxDaily) {
            return ValidationResult.failure("Daily withdrawal limit exceeded");
        }

        tracker.addTransaction(transaction.getAmount());
        return validateNext(transaction, service);
    }
}

// Validateur anti-fraude
public class FraudDetectionValidator extends AbstractTransactionValidator {
    private static final int MAX_TRANSACTIONS_PER_HOUR = 5;
    private Map<String, List<Transaction>> recentTransactions = new HashMap<>();

    @Override
    public ValidationResult validate(Transaction transaction, BankingService service) {
        if (transaction.getSourceAccount() == null) {
            return validateNext(transaction, service);
        }

        String accountNumber = transaction.getSourceAccount();
        List<Transaction> recent = recentTransactions.computeIfAbsent(
            accountNumber, k -> new ArrayList<>());

        // Nettoyer les anciennes transactions
        cleanOldTransactions(recent);

        // Vérifier le nombre de transactions
        if (recent.size() >= MAX_TRANSACTIONS_PER_HOUR) {
            return ValidationResult.failure(
                "Too many transactions in the last hour. Possible fraud.");
        }

        // Vérifier les montants inhabituels
        if (transaction.getAmount() > 5000 && isNightTime()) {
            return ValidationResult.failure(
                "Large transaction during night hours requires verification");
        }

        recent.add(transaction);
        return validateNext(transaction, service);
    }

    private boolean isNightTime() {
        int hour = java.time.LocalTime.now().getHour();
        return hour >= 23 || hour <= 6;
    }

    private void cleanOldTransactions(List<Transaction> transactions) {
        long oneHourAgo = System.currentTimeMillis() - (60 * 60 * 1000);
        transactions.removeIf(tx ->
            tx.getTransactionDate().getTime() < oneHourAgo);
    }
}

// Builder pour construire la chaîne
public class ValidationChainBuilder {
    public static TransactionValidator buildChain() {
        TransactionValidator amountValidator = new AmountValidator();
        TransactionValidator accountValidator = new AccountExistsValidator();
        TransactionValidator balanceValidator = new BalanceValidator();
        TransactionValidator dailyLimitValidator = new DailyLimitValidator();
        TransactionValidator fraudValidator = new FraudDetectionValidator();

        // Construire la chaîne
        amountValidator.setNext(accountValidator);
        accountValidator.setNext(balanceValidator);
        balanceValidator.setNext(dailyLimitValidator);
        dailyLimitValidator.setNext(fraudValidator);

        return amountValidator; // Retourne le premier de la chaîne
    }
}

// Utilisation dans BankingService
public class BankingService {
    private TransactionValidator validationChain;

    public BankingService() {
        this.validationChain = ValidationChainBuilder.buildChain();
    }

    public boolean processTransaction(...) {
        Transaction tx = new Transaction(...);

        // Valider avec la chaîne
        ValidationResult result = validationChain.validate(tx, this);

        if (!result.isValid()) {
            System.out.println("Validation failed: " + result.getErrorMessage());
            tx.setStatus("REJECTED");
            tx.setRejectionReason(result.getErrorMessage());
            return false;
        }

        // Exécuter la transaction
        // ...
        return true;
    }
}
```

**🎓 Points à discuter** :
- Chain of Responsibility découple l'émetteur du récepteur
- Chaque handler a une responsabilité unique (SRP)
- Facile d'ajouter/retirer/réorganiser des validateurs
- Possibilité de court-circuiter la chaîne (dès le premier échec)
- L'ordre de la chaîne est important !
- Builder pattern pour construire la chaîne facilement

**💡 Astuce pédagogique** : Montrez que chaque validateur peut être testé indépendamment.

---

#### 12. Exercice 12 : Observer Pattern (2h)

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

**Problème à identifier** :
```java
// Ligne 100, 151, 156, 204-205 dans BankingService
// Notifications codées en dur
System.out.println("Email envoyé à: " + account.getCustomerEmail());
System.out.println("SMS d'alerte envoyé à: " + account.getCustomerPhone());
```

**Solution attendue** :

```java
// Interface Observer
public interface TransactionObserver {
    void onTransactionCompleted(Transaction transaction, BankingService service);
}

// Subject (Observable)
public class TransactionSubject {
    private List<TransactionObserver> observers = new ArrayList<>();

    public void attach(TransactionObserver observer) {
        observers.add(observer);
        System.out.println("Observer attached: " + observer.getClass().getSimpleName());
    }

    public void detach(TransactionObserver observer) {
        observers.remove(observer);
        System.out.println("Observer detached: " + observer.getClass().getSimpleName());
    }

    public void notifyObservers(Transaction transaction, BankingService service) {
        for (TransactionObserver observer : observers) {
            try {
                observer.onTransactionCompleted(transaction, service);
            } catch (Exception e) {
                System.err.println("Observer failed: " + observer.getClass().getSimpleName() +
                                  " - " + e.getMessage());
                // Continue avec les autres observateurs
            }
        }
    }
}

// Observateur concret : Email
public class EmailNotificationObserver implements TransactionObserver {
    @Override
    public void onTransactionCompleted(Transaction transaction, BankingService service) {
        if (transaction.getSourceAccount() != null) {
            BankAccount source = service.getAccount(transaction.getSourceAccount());
            sendEmail(source.getCustomerEmail(),
                     "Transaction " + transaction.getType() + " of " +
                     transaction.getAmount() + " EUR completed");
        }

        if (transaction.getDestinationAccount() != null) {
            BankAccount dest = service.getAccount(transaction.getDestinationAccount());
            sendEmail(dest.getCustomerEmail(),
                     "You received " + transaction.getAmount() + " EUR");
        }
    }

    private void sendEmail(String email, String message) {
        System.out.println("EMAIL to " + email + ": " + message);
    }
}

// Observateur concret : SMS
public class SMSNotificationObserver implements TransactionObserver {
    @Override
    public void onTransactionCompleted(Transaction transaction, BankingService service) {
        // Envoyer SMS seulement pour les montants élevés
        if (transaction.getAmount() > 1000) {
            if (transaction.getSourceAccount() != null) {
                BankAccount source = service.getAccount(transaction.getSourceAccount());
                sendSMS(source.getCustomerPhone(),
                       "Large transaction alert: " + transaction.getAmount() + " EUR");
            }
        }

        // SMS pour découvert
        if (transaction.getSourceAccount() != null) {
            BankAccount source = service.getAccount(transaction.getSourceAccount());
            if (source.getBalance() < 0) {
                sendSMS(source.getCustomerPhone(),
                       "Overdraft alert! Balance: " + source.getBalance() + " EUR");
            }
        }
    }

    private void sendSMS(String phone, String message) {
        System.out.println("SMS to " + phone + ": " + message);
    }
}

// Observateur concret : Audit
public class AuditLogObserver implements TransactionObserver {
    private List<String> auditLog = new ArrayList<>();

    @Override
    public void onTransactionCompleted(Transaction transaction, BankingService service) {
        String logEntry = String.format(
            "[%s] Transaction %s | Type: %s | Amount: %.2f | Status: %s",
            new Date(),
            transaction.getTransactionId(),
            transaction.getType(),
            transaction.getAmount(),
            transaction.getStatus()
        );

        auditLog.add(logEntry);
        System.out.println("AUDIT: " + logEntry);
    }

    public List<String> getAuditLog() {
        return new ArrayList<>(auditLog);
    }
}

// Observateur concret : Détection de fraude
public class FraudDetectionObserver implements TransactionObserver {
    private Map<String, List<Transaction>> recentTransactions = new HashMap<>();
    private static final int FRAUD_THRESHOLD = 5;

    @Override
    public void onTransactionCompleted(Transaction transaction, BankingService service) {
        String accountNumber = transaction.getSourceAccount();
        if (accountNumber == null) return;

        List<Transaction> recent = recentTransactions.computeIfAbsent(
            accountNumber, k -> new ArrayList<>());
        recent.add(transaction);

        // Nettoyer les anciennes transactions
        cleanOldTransactions(recent);

        // Détecter les patterns suspects
        if (recent.size() > FRAUD_THRESHOLD) {
            System.out.println("FRAUD ALERT: Account " + accountNumber +
                              " has " + recent.size() +
                              " transactions in the last hour!");
        }

        // Détecter montants inhabituels
        double avgAmount = calculateAverageAmount(recent);
        if (transaction.getAmount() > avgAmount * 5) {
            System.out.println("FRAUD ALERT: Unusual amount " +
                              transaction.getAmount() +
                              " (avg: " + avgAmount + ")");
        }
    }

    private void cleanOldTransactions(List<Transaction> transactions) {
        long oneHourAgo = System.currentTimeMillis() - (60 * 60 * 1000);
        transactions.removeIf(tx ->
            tx.getTransactionDate().getTime() < oneHourAgo);
    }

    private double calculateAverageAmount(List<Transaction> transactions) {
        if (transactions.isEmpty()) return 0;
        return transactions.stream()
                          .mapToDouble(Transaction::getAmount)
                          .average()
                          .orElse(0);
    }
}

// Observateur concret : Statistiques (bonus)
public class StatisticsObserver implements TransactionObserver {
    private int totalTransactions = 0;
    private double totalAmount = 0;
    private Map<String, Integer> transactionsByType = new HashMap<>();

    @Override
    public void onTransactionCompleted(Transaction transaction, BankingService service) {
        totalTransactions++;
        totalAmount += transaction.getAmount();

        String type = transaction.getType();
        transactionsByType.put(type, transactionsByType.getOrDefault(type, 0) + 1);
    }

    public void printStatistics() {
        System.out.println("\n=== STATISTICS ===");
        System.out.println("Total transactions: " + totalTransactions);
        System.out.println("Total amount: " + totalAmount + " EUR");
        System.out.println("Average amount: " + (totalAmount / totalTransactions) + " EUR");
        System.out.println("By type:");
        transactionsByType.forEach((type, count) ->
            System.out.println("  " + type + ": " + count));
    }
}

// Intégration dans BankingService
public class BankingService {
    private TransactionSubject transactionSubject = new TransactionSubject();

    public BankingService() {
        // Configuration des observateurs par défaut
        transactionSubject.attach(new EmailNotificationObserver());
        transactionSubject.attach(new SMSNotificationObserver());
        transactionSubject.attach(new AuditLogObserver());
        transactionSubject.attach(new FraudDetectionObserver());
        transactionSubject.attach(new StatisticsObserver());
    }

    public boolean processTransaction(...) {
        // ... logique de transaction

        if (success) {
            // Notifier tous les observateurs
            transactionSubject.notifyObservers(transaction, this);
        }

        return success;
    }

    public void addObserver(TransactionObserver observer) {
        transactionSubject.attach(observer);
    }

    public void removeObserver(TransactionObserver observer) {
        transactionSubject.detach(observer);
    }
}
```

**🎓 Points à discuter** :
- Observer découple le sujet des observateurs
- Push model (données passées) vs Pull model (observateur va chercher)
- Possibilité d'ajouter/retirer des observateurs dynamiquement
- Gestion des erreurs : un observateur ne doit pas bloquer les autres (try-catch)
- Attention aux problèmes de performance avec beaucoup d'observateurs
- Observer vs EventBus/MessageQueue moderne
- Pattern très utilisé dans les frameworks (JavaFX, Spring Events, etc.)

---

## 📝 RÉSUMÉ DE L'ORDRE DES EXERCICES

### ✅ Exercices principaux (Jour 1-2, ~14-16h)

**Jour 1 - Patterns Créationnels et Structurels** :
1. **Strategy** (2h) - Calcul des frais - *COMMENCE ICI*
2. **Builder** (1h30) - Construction de BankAccount
3. **Factory** (1h30) - Création de comptes
4. **Abstract Factory** (1h30) - Packages bancaires (réutilise Strategy)
5. **Adapter** (1h) - Intégration API externe
6. **Facade** (1h) - Simplification de BankingService
7. **Decorator** (1h30) - Fonctionnalités additionnelles

**Jour 2 - Patterns Comportementaux et Structurels** :
8. **Command** (1h30) - Encapsulation des transactions (undo/redo)
9. **State** (1h30) - Gestion des états du compte
10. **Composite** (1h) - Portefeuilles de comptes
11. **Chain of Responsibility** (1h30) - Validation en chaîne
12. **Observer** (2h) - Notifications et audit

---

### Nouvelles Fonctionnalités + Synthèse (Jour 3)

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

### ⚠️ IMPORTANT : Cohérence entre les patterns

**Point clé à expliquer** : Les patterns ne sont pas isolés, ils se réutilisent !

- **Strategy (Exercice 1)** crée `FeeCalculationStrategy`
- **Abstract Factory (Exercice 4)** RÉUTILISE `FeeCalculationStrategy` (pas de nouveau `FeeCalculator`)
- **Chain of Responsibility (Exercice 8)** UTILISE aussi `FeeCalculationStrategy` dans `BalanceValidator`

➡️ **Message pédagogique** : On ne crée pas de nouveaux concepts pour chaque pattern. On compose et réutilise ce qui existe déjà. C'est ça, la vraie architecture !

### Préparation
1. Testez tous les exercices au préalable
2. Préparez des branches Git avec les solutions
3. Ayez des exemples de code alternatifs
4. **Soulignez les réutilisations** entre patterns tout au long de la formation

### Pendant la formation
1. **Encouragez la discussion** : les patterns sont subjectifs
2. **Faites des live-coding** : montrez comment refactorer étape par étape
3. **Adaptez le rythme** : certains participants iront plus vite
4. **Utilisez les tests** : montrez que le refactoring ne casse rien (tests des frais inclus)
5. **Code reviews** : examinez les solutions des participants
6. **Montrez la progression** : Strategy → Abstract Factory montre comment les patterns se combinent

### Pièges courants à éviter
1. **Over-engineering** : ne pas utiliser un pattern juste pour l'utiliser
2. **Pattern obsession** : parfois, du code simple suffit
3. **Refactoring brutal** : refactorer progressivement
4. **Ignorer les tests** : toujours garder les tests verts
5. **Duplication de concepts** : si `FeeCalculationStrategy` existe, ne créez pas `FeeCalculator` !

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
