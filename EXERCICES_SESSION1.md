# Exercices Session 1 - Patterns Comportementaux

## Vue d'ensemble

Cette session vous permet de refactorer le code legacy bancaire en appliquant 5 design patterns différents. Chaque exercice se concentre sur un problème spécifique du code.

**Durée totale estimée :** 6-7 heures

**Patterns couverts :**
- Command Pattern
- State Pattern
- Composite Pattern
- Chain of Responsibility Pattern
- Observer Pattern

---

## Exercice 1 : Pattern Command (1h30)

### 🎯 Objectif
Encapsuler les opérations bancaires (dépôt, retrait, virement) en objets Command pour permettre l'annulation (undo), la journalisation, et la mise en file d'attente.

### 📋 Contexte
Actuellement, les transactions sont exécutées immédiatement. On veut pouvoir :
- Annuler une transaction (undo)
- Rejouer une transaction (redo)
- Mettre des transactions en file d'attente
- Auditer toutes les commandes

### 🔍 Code actuel (procédural)
```java
// Ligne 96-101 dans BankingService
account.setBalance(account.getBalance() + amount);
tx.setStatus("COMPLETED");
```

### ✏️ Tâches

#### Étape 1 : Créer l'interface Command
Créez une interface `BankCommand` dans le package `com.bank.patterns.command` avec les méthodes :
- `boolean execute()` - Exécuter la commande
- `boolean undo()` - Annuler la commande
- `String getDescription()` - Description de la commande

#### Étape 2 : Créer les commandes concrètes

**DepositCommand**
- Attributs : `BankAccount account`, `double amount`, `boolean executed`
- `execute()` : Vérifie que amount > 0, ajoute au solde, met executed = true
- `undo()` : Si executed, retire le montant, met executed = false

**WithdrawCommand**
- Attributs : `BankAccount account`, `double amount`, `boolean executed`
- `execute()` : Vérifie le solde suffisant, retire du solde, met executed = true
- `undo()` : Si executed, rajoute le montant, met executed = false

**TransferCommand** (Macro Command)
- Attributs : `WithdrawCommand withdraw`, `DepositCommand deposit`
- `execute()` : Exécute withdraw, puis deposit. Si deposit échoue, undo withdraw
- `undo()` : Annule deposit puis withdraw

#### Étape 3 : Créer l'Invoker (TransactionExecutor)
Créez une classe `TransactionExecutor` avec :
- `Stack<BankCommand> executedCommands` - Historique des commandes exécutées
- `Stack<BankCommand> undoneCommands` - Historique des commandes annulées
- `boolean execute(BankCommand command)` - Exécute et empile dans executedCommands
- `boolean undo()` - Annule la dernière commande et l'empile dans undoneCommands
- `boolean redo()` - Rejoue la dernière commande annulée

#### Étape 4 : Créer une démonstration
Créez `CommandDemo.java` qui démontre :
- Exécution de plusieurs commandes
- Utilisation de undo pour annuler
- Utilisation de redo pour rejouer
- Exemple avec TransferCommand (macro)

### 🎓 Points d'apprentissage
- Command encapsule une requête en objet
- Permet undo/redo
- Facilite la journalisation et les transactions
- Macro commands pour opérations composées

### ✅ Critères de validation
- [ ] Au moins 3 commandes concrètes
- [ ] Undo/Redo fonctionnent correctement
- [ ] TransferCommand utilise 2 commandes (macro)
- [ ] CommandExecutor maintient l'historique

---

## Exercice 2 : Pattern State (1h30)

### 🎯 Objectif
Gérer les différents états d'un compte bancaire (ACTIVE, SUSPENDED, FROZEN, CLOSED) avec des comportements différents selon l'état.

### 📋 Contexte
Un compte peut être dans différents états :
- **ACTIVE** : Toutes opérations autorisées
- **SUSPENDED** : Retraits limités, dépôts OK
- **FROZEN** : Aucune opération autorisée
- **CLOSED** : Compte fermé, aucune opération

Actuellement, le code utilise des `if` pour vérifier l'état :
```java
if (account.getStatus().equals("ACTIVE")) {
    // Autoriser
} else if (account.getStatus().equals("SUSPENDED")) {
    // Limiter
} // ...
```

### ✏️ Tâches

#### Étape 1 : Créer l'interface State
Créez une interface `AccountState` dans le package `com.bank.patterns.state` avec :
- `boolean canDeposit()` - Autorisation de dépôt
- `boolean canWithdraw()` - Autorisation de retrait
- `boolean canTransfer()` - Autorisation de virement
- `double getWithdrawalLimit()` - Limite de retrait
- `String getStateName()` - Nom de l'état
- `void handleStateTransition(BankAccount account, String action)` - Gérer les transitions

#### Étape 2 : Créer les états concrets

Créez 4 classes qui implémentent `AccountState` :

**ActiveState**
- Toutes opérations autorisées
- Limite de retrait : illimitée (`Double.MAX_VALUE`)
- Transitions : "SUSPEND" → SuspendedState, "FREEZE" → FrozenState

**SuspendedState**
- Dépôts autorisés, retraits limités, virements interdits
- Limite de retrait : 500 EUR par jour
- Transitions : "ACTIVATE" → ActiveState, "FREEZE" → FrozenState

**FrozenState**
- Aucune opération autorisée
- Limite de retrait : 0
- Transitions : "UNFREEZE" → ActiveState, "CLOSE" → ClosedState

**ClosedState**
- Aucune opération autorisée
- Aucune transition possible

#### Étape 3 : Modifier BankAccount
Ajoutez un attribut `AccountState state` dans BankAccount et utilisez-le dans les méthodes :
- `deposit()` : Vérifie `state.canDeposit()` avant d'autoriser
- `withdraw()` : Vérifie `state.canWithdraw()` et `state.getWithdrawalLimit()`
- `changeState(String action)` : Appelle `state.handleStateTransition()`

#### Étape 4 : Diagramme des transitions
```
ACTIVE ──SUSPEND──> SUSPENDED
  │                    │
  └────────FREEZE──────┴──> FROZEN ──CLOSE──> CLOSED
           │                   │
           └──────UNFREEZE─────┘
```

#### Étape 5 : Créer une démonstration
Créez `StateDemo.java` qui démontre :
- Changements d'état avec transitions
- Comportements différents selon l'état
- Tentatives d'opérations interdites

### 🎓 Points d'apprentissage
- State encapsule le comportement lié à l'état
- Élimine les conditionnelles multiples
- Facilite l'ajout de nouveaux états
- Les transitions sont gérées par les états eux-mêmes

### ✅ Critères de validation
- [ ] 4 états implémentés (Active, Suspended, Frozen, Closed)
- [ ] Les comportements varient selon l'état
- [ ] Les transitions d'état sont gérées correctement
- [ ] Plus de `if (status.equals(...))` dans le code métier

---

## Exercice 3 : Pattern Composite (1h)

### 🎯 Objectif
Gérer des comptes individuels et des portefeuilles de comptes de manière uniforme.

### 📋 Contexte
Les clients peuvent avoir :
- Un compte individuel
- Un portefeuille familial (plusieurs comptes)
- Un portefeuille d'entreprise (compte principal + sous-comptes)

On veut pouvoir traiter un compte seul ou un groupe de comptes de la même façon.

### 🔍 Cas d'usage
```java
// Calculer le solde total d'un portefeuille
portfolio.getBalance(); // Somme de tous les comptes

// Effectuer un dépôt qui se répartit
portfolio.deposit(1000); // Répartir sur tous les comptes
```

### ✏️ Tâches

#### Étape 1 : Créer l'interface Component
Créez une interface `AccountComponent` dans le package `com.bank.patterns.composite` avec :
- `double getBalance()` - Solde total
- `boolean deposit(double amount)` - Déposer
- `boolean withdraw(double amount)` - Retirer
- `String getAccountInfo()` - Information du compte
- `void addChild(AccountComponent component)` - Ajouter un enfant
- `void removeChild(AccountComponent component)` - Retirer un enfant
- `List<AccountComponent> getChildren()` - Liste des enfants

#### Étape 2 : Créer le Leaf (compte individuel)
Créez une classe `IndividualAccount` qui implémente `AccountComponent` :
- Enveloppe un `BankAccount`
- Implémente toutes les méthodes
- `addChild()` et `removeChild()` lancent `UnsupportedOperationException`
- `getChildren()` retourne une liste vide

#### Étape 3 : Créer le Composite (portefeuille)
Créez une classe `AccountPortfolio` qui implémente `AccountComponent` :
- Attributs : `String name`, `List<AccountComponent> accounts`
- `getBalance()` : Somme récursive des soldes
- `deposit()` : Répartit équitablement sur tous les comptes
- `withdraw()` : Retire proportionnellement (vérifie solde total d'abord)
- `addChild()` / `removeChild()` : Gèrent la liste accounts
- `getAccountInfo()` : Affiche le nom et tous les sous-comptes

#### Étape 4 : Créer une démonstration
Créez `CompositeDemo.java` qui démontre :
- Création de comptes individuels
- Création d'un portefeuille familial
- Création d'un portefeuille entreprise avec sous-portefeuilles
- Opérations sur le portefeuille (balance, deposit, withdraw)
- Affichage de la structure hiérarchique

### 🎓 Points d'apprentissage
- Composite traite objets individuels et compositions uniformément
- Structure en arbre
- Opérations récursives (balance, deposit, etc.)
- Permet des hiérarchies complexes

### ✅ Critères de validation
- [ ] Interface commune Component
- [ ] Leaf (IndividualAccount) et Composite (Portfolio)
- [ ] Possibilité de composer des portfolios de portfolios
- [ ] Les opérations fonctionnent récursivement

---

## Exercice 4 : Pattern Chain of Responsibility (1h30)

### 🎯 Objectif
Créer une chaîne de validateurs pour les transactions, où chaque validateur vérifie un aspect spécifique et passe au suivant si OK.

### 📋 Contexte
Actuellement, toutes les validations sont dans `processTransaction` avec des if/else imbriqués. On veut séparer chaque validation dans son propre handler.

### 🔍 Validations nécessaires
1. **AmountValidator** : Montant > 0
2. **AccountExistsValidator** : Les comptes existent
3. **BalanceValidator** : Solde suffisant
4. **DailyLimitValidator** : Limite quotidienne respectée
5. **FraudDetectionValidator** : Pas de pattern suspect
6. **AccountStateValidator** : Compte actif (utilise State pattern)

### ✏️ Tâches

#### Étape 1 : Créer l'interface Handler
Créez une interface `TransactionValidator` dans le package `com.bank.patterns.chain` avec :
- `void setNext(TransactionValidator next)` - Définir le suivant dans la chaîne
- `ValidationResult validate(Transaction transaction, BankingService service)` - Valider

Créez aussi une classe `ValidationResult` avec :
- `boolean valid` - Validation réussie ?
- `String errorMessage` - Message d'erreur si échec
- Méthodes statiques : `success()` et `failure(String message)`

#### Étape 2 : Créer la classe abstraite de base
Créez une classe abstraite `AbstractTransactionValidator` qui :
- Contient l'attribut `TransactionValidator next`
- Implémente `setNext()`
- Fournit une méthode helper `validateNext()` pour passer au suivant

#### Étape 3 : Créer les validateurs concrets

Créez au moins 5 validateurs :

**AmountValidator**
- Vérifie que amount > 0
- Vérifie que amount <= 50000 EUR (limite maximale)

**AccountExistsValidator**
- Vérifie que le compte source existe (si non null)
- Vérifie que le compte destination existe (si non null)

**BalanceValidator**
- Pour les retraits : vérifie que le solde est suffisant
- Prend en compte le découvert autorisé

**DailyLimitValidator**
- Maintient un compteur des transactions par compte par jour
- Limite : 1000 EUR par jour par compte
- Utilise une Map<String, DailyTransactionTracker>

**FraudDetectionValidator**
- Détecte les patterns suspects :
  - Trop de transactions par heure (max 5)
  - Montants élevés la nuit (> 5000 EUR entre 23h et 6h)

#### Étape 4 : Construire la chaîne
Créez une classe `ValidationChainBuilder` avec une méthode statique `buildChain()` qui :
- Instancie tous les validateurs
- Les chaîne dans l'ordre logique
- Retourne le premier de la chaîne

#### Étape 5 : Utiliser dans BankingService (optionnel)
Montrez comment intégrer la chaîne dans BankingService pour remplacer les validations existantes.

### 🎓 Points d'apprentissage
- Chain of Responsibility découple l'émetteur du récepteur
- Chaque handler a une responsabilité unique
- Facile d'ajouter/retirer/réorganiser des validateurs
- Possibilité de court-circuiter la chaîne

### ✅ Critères de validation
- [ ] Au moins 5 validateurs dans la chaîne
- [ ] Chaque validateur est indépendant
- [ ] La validation s'arrête au premier échec
- [ ] Facile d'ajouter un nouveau validateur

---

## Exercice 5 : Pattern Observer (2h)

### 🎯 Objectif
Implémenter un système de notifications où plusieurs observateurs (email, SMS, audit log, fraud detection) réagissent aux événements bancaires sans couplage fort.

### 📋 Contexte
Actuellement, les notifications sont codées en dur dans `BankingService` :
```java
// Ligne 100, 151, 156, 204-205
System.out.println("Email envoyé à: " + account.getCustomerEmail());
System.out.println("SMS d'alerte envoyé à: " + account.getCustomerPhone());
```

On veut :
- Ajouter/retirer des observateurs dynamiquement
- Ne pas modifier `BankingService` quand on ajoute un canal
- Avoir plusieurs types d'observateurs (Email, SMS, Audit, Fraud)

### ✏️ Tâches

#### Étape 1 : Créer l'interface Observer
Créez une interface `TransactionObserver` dans le package `com.bank.patterns.observer` avec :
- `void onTransactionCompleted(Transaction transaction, BankingService service)` - Notification

#### Étape 2 : Créer le Subject (Observable)
Créez une classe `TransactionSubject` avec :
- `List<TransactionObserver> observers` - Liste des observateurs
- `void attach(TransactionObserver observer)` - Ajouter un observateur
- `void detach(TransactionObserver observer)` - Retirer un observateur
- `void notifyObservers(Transaction transaction, BankingService service)` - Notifier tous

#### Étape 3 : Créer les observateurs concrets

Créez au moins 4 observateurs :

**EmailNotificationObserver**
- Envoie un email au compte source et destination
- Format : `"EMAIL to email@example.com: Transaction TYPE of AMOUNT EUR completed"`
- Simule l'envoi avec `System.out.println()`

**SMSNotificationObserver**
- Envoie un SMS uniquement pour :
  - Montants élevés (> 1000 EUR)
  - Solde négatif (découvert)
- Format : `"SMS to +33XXXXXXXX: Alert message"`

**AuditLogObserver**
- Enregistre chaque transaction dans un log
- Format : `"[Date] Transaction ID | Type | Amount | Status"`
- Maintient une liste `List<String> auditLog` consultable
- Méthode `getAuditLog()` pour consulter l'historique

**FraudDetectionObserver**
- Détecte les patterns suspects :
  - Trop de transactions (> 5 en une heure)
  - Montants inhabituels (> 5x la moyenne du compte)
- Affiche des alertes avec `System.out.println()`
- Maintient un historique des transactions récentes par compte

**StatisticsObserver** (Bonus)
- Compte le nombre de transactions
- Calcule le montant total
- Groupe par type de transaction
- Méthode `printStatistics()` pour afficher

#### Étape 4 : Intégrer dans BankingService
Modifiez `BankingService` pour :
- Avoir un attribut `TransactionSubject transactionSubject`
- Configurer les observateurs par défaut dans le constructeur
- Notifier après chaque transaction réussie
- Fournir des méthodes `addObserver()` et `removeObserver()`

#### Étape 5 : Créer une démonstration
Créez `ObserverDemo.java` qui démontre :
- Configuration de plusieurs observateurs
- Exécution de transactions
- Notifications automatiques à tous les observateurs
- Ajout/retrait dynamique d'un observateur

### 🎓 Points d'apprentissage
- Observer découple le sujet des observateurs
- Push model vs Pull model
- Possibilité d'ajouter/retirer des observateurs dynamiquement
- Attention aux problèmes de performance avec beaucoup d'observateurs
- Gestion des erreurs dans les observateurs

### ✅ Critères de validation
- [ ] Au moins 4 observateurs implémentés
- [ ] Les observateurs peuvent être ajoutés/retirés dynamiquement
- [ ] Un observateur peut échouer sans bloquer les autres
- [ ] Les observateurs ne se connaissent pas entre eux
- [ ] BankingService ne dépend pas des observateurs concrets

---

## 🎯 Synthèse Session 1

### Patterns appliqués et leurs relations

```
BankingService
├── Command: Encapsulation des transactions (undo/redo)
├── State: Gestion des états du compte
├── Composite: Portefeuilles de comptes
├── Chain of Responsibility: Validation en chaîne
└── Observer: Notifications et audit
```

### Métriques de réussite

À la fin de la Session 1, vous devriez avoir :
- ✅ 5 patterns implémentés et fonctionnels
- ✅ Code legacy grandement amélioré
- ✅ Séparation claire des responsabilités
- ✅ Tests unitaires pour chaque pattern
- ✅ Moins de 50% du code original dans BankingService

### Questions de révision

1. Quand utiliser State plutôt que des if/else ?
2. Command permet quoi que les méthodes normales ne permettent pas ?
3. Observer vs Chain of Responsibility : quand utiliser l'un ou l'autre ?
4. Composite est-il toujours pertinent ou peut-il être over-engineering ?
5. Comment gérer les transactions complexes avec le pattern Command ?

---

## 📚 Ressources

- Code source à créer dans `/src/main/java/com/bank/patterns/`
- Tests dans `/src/test/java/com/bank/patterns/`
- Guide instructeur : `/GUIDE_INSTRUCTEUR.md`
- Stratégie de refactoring : `/REFACTORING_STRATEGY_BABYSTEPS.md`

Bon courage ! 🚀
