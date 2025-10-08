# Formation Design Patterns - Système Bancaire

## 📋 Contexte

Ce projet de formation vous permet d'apprendre et de pratiquer les design patterns dans un contexte bancaire réaliste.

Vous allez travailler sur un **code legacy mal écrit** que vous allez progressivement refactorer en appliquant différents design patterns. Ensuite, vous ajouterez de nouvelles fonctionnalités en utilisant ces patterns.

## 🎯 Objectifs pédagogiques

- Identifier les problèmes dans du code legacy
- Appliquer les design patterns pour résoudre ces problèmes
- Comprendre quand et pourquoi utiliser chaque pattern
- Pratiquer le refactoring progressif
- Ajouter des fonctionnalités de manière maintenable

## 🏗️ Structure du projet

```
banking-patterns-java/
├── src/main/java/com/bank/
│   └── legacy/              # Code legacy à refactorer
│       ├── BankAccount.java
│       ├── Transaction.java
│       ├── BankingService.java
│       └── Main.java
├── src/test/java/com/bank/
│   └── legacy/              # Tests du code legacy
└── pom.xml
```

## 🚀 Démarrage

### Prérequis
- Java 17 ou supérieur
- Maven 3.6 ou supérieur
- Un IDE Java (IntelliJ IDEA, Eclipse, VS Code)

### Installation et exécution

```bash
# Compiler le projet
mvn clean compile

# Exécuter le programme principal
mvn exec:java -Dexec.mainClass="com.bank.legacy.Main"

# Exécuter les tests
mvn test
```

## 📚 Exercices - Parcours de refactoring

### 🔍 Étape 0 : Analyse du code legacy

**Objectif** : Comprendre les problèmes du code existant

**Instructions** :
1. Exécutez le programme principal (`Main.java`) et observez le fonctionnement
2. Lisez attentivement le code de `BankingService.java`
3. Identifiez les **code smells** et problèmes :
   - Code dupliqué
   - Méthodes trop longues
   - Trop de responsabilités
   - Logique métier en dur (hard-coded)
   - Manque de validation cohérente
   - Couplage fort
   - Impossibilité d'étendre facilement

**Questions de réflexion** :
- Que se passe-t-il si on veut ajouter un nouveau type de compte ?
- Comment tester unitairement la logique de calcul des frais ?
- Comment réutiliser la logique de validation ?

---

## 🎨 PARTIE 1 : PATTERNS CRÉATIONNELS

### Exercice 1 : Builder Pattern

**Problème identifié** : Le constructeur de `BankAccount` a trop de paramètres et n'est pas flexible.

**Objectif** : Créer un Builder pour simplifier la création de comptes bancaires

**Instructions** :
1. Créez une classe interne `BankAccount.Builder`
2. Implémentez le pattern Builder avec une interface fluide
3. Ajoutez des validations dans le Builder
4. Refactorisez le code pour utiliser le Builder

**Exemple d'utilisation attendu** :
```java
BankAccount account = new BankAccount.Builder()
    .accountNumber("ACC1001")
    .accountType("COURANT")
    .customerName("Jean Dupont")
    .customerEmail("jean@email.fr")
    .balance(500.0)
    .build();
```

**Critères de validation** :
- Le Builder valide les données avant la création
- Le code est plus lisible
- Les paramètres optionnels sont gérés élégamment

---

### Exercice 2 : Factory Pattern

**Problème identifié** : La logique de création des comptes dans `BankingService.createAccount()` est dupliquée et rigide.

**Objectif** : Extraire la logique de création dans une Factory

**Instructions** :
1. Créez une interface `AccountFactory`
2. Créez des implémentations concrètes :
   - `CurrentAccountFactory` (compte courant)
   - `SavingsAccountFactory` (compte épargne)
   - `BusinessAccountFactory` (compte professionnel)
3. Chaque factory encapsule les règles spécifiques (dépôt minimum, taux d'intérêt, découvert)
4. Refactorisez `BankingService` pour utiliser ces factories

**Exemple d'utilisation attendu** :
```java
AccountFactory factory = new SavingsAccountFactory();
BankAccount account = factory.createAccount("Marie Martin", "marie@email.fr", "0601020304", 1000.0);
```

**Critères de validation** :
- Chaque type de compte a sa propre factory
- Les règles métier sont centralisées
- Facile d'ajouter un nouveau type de compte

---

### Exercice 3 : Abstract Factory Pattern

**Problème identifié** : Nous avons besoin de créer des familles de produits bancaires cohérentes.

**Objectif** : Créer une Abstract Factory pour gérer différents "packages" bancaires

**Instructions** :
1. Créez une interface `BankingPackageFactory` avec les méthodes :
   - `createAccount()`
   - `createTransactionProcessor()`
   - `createFeeCalculator()`
2. Implémentez des packages concrets :
   - `StandardBankingPackage` (clients particuliers)
   - `PremiumBankingPackage` (clients premium)
   - `BusinessBankingPackage` (clients professionnels)
3. Chaque package crée des objets cohérents entre eux

**Exemple d'utilisation attendu** :
```java
BankingPackageFactory packageFactory = new PremiumBankingPackage();
BankAccount account = packageFactory.createAccount();
FeeCalculator feeCalc = packageFactory.createFeeCalculator();
```

**Critères de validation** :
- Les objets créés sont cohérents entre eux
- Facile de changer de package complet
- Évite les incompatibilités entre composants

---

### Exercice 4 : Singleton Pattern

**Problème identifié** : Certains composants doivent être uniques (configuration, générateurs d'ID).

**Objectif** : Créer des Singletons pour les ressources partagées

**Instructions** :
1. Créez un `TransactionIdGenerator` en Singleton (thread-safe)
2. Créez un `BankingConfiguration` en Singleton pour les paramètres globaux
3. Utilisez ces singletons dans le code

**Exemple d'utilisation attendu** :
```java
String txId = TransactionIdGenerator.getInstance().generateId();
double maxTransfer = BankingConfiguration.getInstance().getMaxTransferAmount();
```

**Critères de validation** :
- Une seule instance existe
- Thread-safe
- Lazy initialization

---

### Exercice 5 : Prototype Pattern

**Problème identifié** : Création de comptes similaires ou templates de transactions.

**Objectif** : Utiliser le Prototype pattern pour cloner des objets

**Instructions** :
1. Implémentez `Cloneable` dans `BankAccount`
2. Créez une méthode `clone()` appropriée
3. Créez un `AccountTemplateRegistry` qui stocke des prototypes de comptes
4. Permettez la création de nouveaux comptes à partir de templates

**Exemple d'utilisation attendu** :
```java
BankAccount template = templateRegistry.getTemplate("COMPTE_ETUDIANT");
BankAccount newAccount = template.clone();
newAccount.setCustomerName("Nouveau client");
```

**Critères de validation** :
- Le clonage est profond (deep copy)
- Les templates sont réutilisables
- Simplification pour les comptes standards

---

## 🏛️ PARTIE 2 : PATTERNS STRUCTURELS

### Exercice 6 : Adapter Pattern

**Problème identifié** : Intégration avec des systèmes externes (API de paiement, services tiers).

**Objectif** : Créer des Adapters pour les systèmes externes

**Instructions** :
1. Créez une interface `PaymentGateway` (standard interne)
2. Simulez une API externe `ExternalPaymentAPI` (avec une interface différente)
3. Créez un `PaymentGatewayAdapter` qui adapte l'API externe à notre interface
4. Intégrez l'adapter dans le système de transactions

**Exemple d'utilisation attendu** :
```java
PaymentGateway gateway = new PaymentGatewayAdapter(new ExternalPaymentAPI());
boolean success = gateway.processPayment(account, amount);
```

**Critères de validation** :
- L'interface interne reste stable
- Facile d'ajouter d'autres API externes
- Découplage entre le système et les services externes

---

### Exercice 7 : Composite Pattern

**Problème identifié** : Gestion de comptes groupés (comptes joints, comptes d'entreprise avec sous-comptes).

**Objectif** : Utiliser Composite pour gérer des hiérarchies de comptes

**Instructions** :
1. Créez une interface `AccountComponent` avec :
   - `getBalance()`
   - `addTransaction()`
   - `generateStatement()`
2. Implémentez :
   - `SimpleAccount` (feuille)
   - `CompositeAccount` (composite, contient d'autres comptes)
3. Permettez de traiter un compte simple et un groupe de comptes de manière uniforme

**Exemple d'utilisation attendu** :
```java
CompositeAccount familyAccount = new CompositeAccount("Compte Famille");
familyAccount.add(new SimpleAccount("Compte Parent 1"));
familyAccount.add(new SimpleAccount("Compte Parent 2"));
double totalBalance = familyAccount.getBalance(); // Somme des sous-comptes
```

**Critères de validation** :
- Traitement uniforme des comptes simples et composites
- Navigation dans la hiérarchie
- Opérations récursives fonctionnelles

---

### Exercice 8 : Decorator Pattern

**Problème identifié** : Ajout dynamique de fonctionnalités aux comptes (assurance, alertes, cashback).

**Objectif** : Utiliser Decorator pour enrichir les comptes

**Instructions** :
1. Créez des decorators pour les comptes :
   - `InsuredAccountDecorator` (ajoute une assurance)
   - `CashbackAccountDecorator` (ajoute du cashback)
   - `NotificationAccountDecorator` (ajoute des notifications enrichies)
2. Permettez de combiner plusieurs decorators
3. Chaque decorator ajoute son comportement sans modifier l'objet de base

**Exemple d'utilisation attendu** :
```java
AccountComponent account = new SimpleAccount("ACC001");
account = new InsuredAccountDecorator(account, 10000.0);
account = new CashbackAccountDecorator(account, 0.02);
account.processTransaction(transaction); // Avec assurance ET cashback
```

**Critères de validation** :
- Ajout dynamique de fonctionnalités
- Combinaison de plusieurs decorators
- Pas de modification des classes existantes

---

### Exercice 9 : Facade Pattern

**Problème identifié** : L'interface de `BankingService` est trop complexe et expose trop de détails.

**Objectif** : Créer une Facade simplifiée pour les opérations courantes

**Instructions** :
1. Créez une classe `BankingFacade` qui simplifie :
   - Ouverture de compte complète (avec validation, notification, etc.)
   - Transfert d'argent (avec toutes les vérifications)
   - Clôture de compte (avec toutes les étapes)
2. La facade coordonne les appels aux sous-systèmes
3. Cache la complexité aux clients

**Exemple d'utilisation attendu** :
```java
BankingFacade facade = new BankingFacade();
BankAccount account = facade.openNewAccount("COURANT", "Jean", "jean@email.fr", 500.0);
facade.transferMoney(account1, account2, 200.0); // Gère toute la complexité
```

**Critères de validation** :
- Interface simple pour les cas d'usage courants
- Coordination de plusieurs sous-systèmes
- Réduction du couplage

---

## ⚙️ PARTIE 3 : PATTERNS COMPORTEMENTAUX

### Exercice 10 : Strategy Pattern

**Problème identifié** : Le calcul des frais est en dur dans `processTransaction()` avec des if/else.

**Objectif** : Extraire les stratégies de calcul de frais

**Instructions** :
1. Créez une interface `FeeCalculationStrategy` avec `calculateFee(Transaction)`
2. Implémentez des stratégies concrètes :
   - `CurrentAccountFeeStrategy`
   - `SavingsAccountFeeStrategy`
   - `BusinessAccountFeeStrategy`
   - `NoFeeStrategy` (pour clients premium)
3. Injectez la stratégie dans le compte ou le processeur de transaction

**Exemple d'utilisation attendu** :
```java
FeeCalculationStrategy feeStrategy = new CurrentAccountFeeStrategy();
double fees = feeStrategy.calculateFee(transaction);
```

**Critères de validation** :
- Élimination des if/else pour les frais
- Facile d'ajouter de nouvelles stratégies
- Stratégie changeable à runtime

---

### Exercice 11 : Template Method Pattern

**Problème identifié** : Le traitement des transactions a toujours les mêmes étapes mais avec des variations.

**Objectif** : Créer un Template Method pour le traitement des transactions

**Instructions** :
1. Créez une classe abstraite `TransactionProcessor` avec :
   - `processTransaction()` (template method)
   - Étapes abstraites : `validate()`, `executeTransaction()`, `notifyCustomer()`
2. Implémentez des processeurs concrets :
   - `DepositProcessor`
   - `WithdrawalProcessor`
   - `TransferProcessor`
3. Chaque processeur implémente les étapes spécifiques

**Exemple d'utilisation attendu** :
```java
TransactionProcessor processor = new WithdrawalProcessor();
processor.processTransaction(transaction); // Suit le template défini
```

**Critères de validation** :
- L'algorithme général est dans la classe de base
- Les variations sont dans les sous-classes
- Pas de code dupliqué

---

### Exercice 12 : Chain of Responsibility Pattern

**Problème identifié** : La validation des transactions a de multiples règles imbriquées.

**Objectif** : Créer une chaîne de validateurs

**Instructions** :
1. Créez une interface `TransactionValidator` avec :
   - `setNext(TransactionValidator)`
   - `validate(Transaction)`
2. Implémentez des validateurs concrets :
   - `AmountValidator` (montant positif et dans les limites)
   - `AccountExistsValidator`
   - `BalanceValidator` (solde suffisant)
   - `DailyLimitValidator`
   - `AntifraudValidator`
3. Chaînez les validateurs

**Exemple d'utilisation attendu** :
```java
TransactionValidator chain = new AmountValidator();
chain.setNext(new AccountExistsValidator())
     .setNext(new BalanceValidator())
     .setNext(new AntifraudValidator());

ValidationResult result = chain.validate(transaction);
```

**Critères de validation** :
- Chaque validateur a une seule responsabilité
- Ordre des validations configurable
- Facile d'ajouter de nouveaux validateurs

---

### Exercice 13 : Observer Pattern

**Problème identifié** : Les notifications (email, SMS) sont dispersées et couplées au code métier.

**Objectif** : Implémenter Observer pour les notifications

**Instructions** :
1. Créez une interface `TransactionObserver` avec `onTransactionCompleted(Transaction)`
2. Implémentez des observateurs concrets :
   - `EmailNotificationObserver`
   - `SmsNotificationObserver`
   - `AuditLogObserver`
   - `FraudDetectionObserver`
3. Modifiez `BankingService` pour notifier les observateurs
4. Permettez l'ajout/retrait dynamique d'observateurs

**Exemple d'utilisation attendu** :
```java
TransactionSubject subject = new TransactionSubject();
subject.addObserver(new EmailNotificationObserver());
subject.addObserver(new SmsNotificationObserver());
subject.addObserver(new AuditLogObserver());

subject.notifyObservers(transaction); // Tous les observateurs sont notifiés
```

**Critères de validation** :
- Découplage entre le code métier et les notifications
- Facile d'ajouter de nouveaux types de notifications
- Observateurs configurables dynamiquement

---

## 🚀 PARTIE 4 : NOUVELLES FONCTIONNALITÉS

Une fois le refactoring terminé, ajoutez les fonctionnalités suivantes en utilisant les patterns appropriés :

### Fonctionnalité 1 : Prêts bancaires
- Créez un système de prêts (immobilier, consommation, professionnel)
- Utilisez : Factory, Strategy (pour calcul d'intérêts), Template Method (pour approbation)

### Fonctionnalité 2 : Cartes bancaires
- Ajoutez des cartes (débit, crédit, premium)
- Utilisez : Builder, Decorator, Chain of Responsibility (validation)

### Fonctionnalité 3 : Investissements
- Permettez les placements (actions, obligations, fonds)
- Utilisez : Composite (portefeuille), Observer (alertes de prix)

### Fonctionnalité 4 : Multi-devises
- Supportez plusieurs devises
- Utilisez : Adapter (services de change), Facade

### Fonctionnalité 5 : Rapports et analytics
- Générez des rapports complexes
- Utilisez : Prototype (templates), Strategy (différents formats)

---

## 📖 Ressources

### Liens utiles
- [Refactoring Guru - Design Patterns](https://refactoring.guru/design-patterns)
- [Source Making - Design Patterns](https://sourcemaking.com/design_patterns)

### Conseils
- Refactorisez progressivement, ne réécrivez pas tout d'un coup
- Faites tourner les tests après chaque modification
- Committez régulièrement vos changements
- Discutez des choix de design avec vos collègues

---

## ✅ Critères de réussite du projet

À la fin de la formation, vous devriez avoir :

1. ✅ Éliminé le code dupliqué
2. ✅ Séparé les responsabilités (SRP)
3. ✅ Rendu le code facilement extensible (OCP)
4. ✅ Découplé les composants
5. ✅ Appliqué au moins 13 design patterns
6. ✅ Maintenu les tests verts
7. ✅ Ajouté au moins 3 nouvelles fonctionnalités

---

## 🤝 Contribution

Ce projet est un support de formation. N'hésitez pas à :
- Poser des questions à votre formateur
- Partager vos solutions avec le groupe
- Proposer des améliorations

Bon courage ! 🚀
