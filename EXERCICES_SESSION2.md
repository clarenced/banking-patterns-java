# Exercices Session 2 - Patterns Structurels Avancés

## Vue d'ensemble

Cette session approfondit les patterns structurels en implémentant le pattern **Composite** dans un nouveau contexte (portefeuille d'investissement) et le pattern **Iterator** pour parcourir des collections de manière élégante.

**Durée totale estimée :** 9-11 heures

**Patterns couverts :**
- Composite Pattern (portefeuille d'investissement)
- Iterator Pattern
- Flyweight Pattern (optimisation mémoire des transactions)
- Memento Pattern (sauvegarde/restauration d'états)

---

## Exercice 1 : Pattern Composite (1h30)

### 🎯 Objectif
Gérer un portefeuille d'investissement contenant à la fois des instruments financiers simples (actions, obligations, ETF) et des sous-portefeuilles, et calculer la valeur totale de manière récursive.

### 📋 Contexte
Une banque gère des portefeuilles d'investissement pour ses clients. Chaque portefeuille peut contenir :
- **Des instruments financiers simples** : actions, obligations, ETF
- **Des sous-portefeuilles** : par exemple, un portefeuille "Actions Tech", un portefeuille "Obligations", etc.

**Problème** : Comment calculer la valeur totale d'un portefeuille, quel que soit son niveau de complexité dans la hiérarchie ?

### 🔍 Cas d'usage
```java
// Créer des instruments individuels
Action apple = new Action("AAPL", 10, 150.0);  // 10 actions à 150€
Obligation govBond = new Obligation("FR-BOND", 5000, 0.02);  // 5000€ à 2%

// Créer un sous-portefeuille
Portfolio techPortfolio = new Portfolio("Tech Stocks");
techPortfolio.ajouter(apple);
techPortfolio.ajouter(microsoft);

// Créer un portefeuille principal
Portfolio mainPortfolio = new Portfolio("Portefeuille Principal");
mainPortfolio.ajouter(techPortfolio);  // Sous-portefeuille
mainPortfolio.ajouter(govBond);        // Instrument simple

// Calculer la valeur totale (récursif)
double valeurTotale = mainPortfolio.getValeur();  // Somme de tout !
```

### ✏️ Tâches

#### Étape 1 : Créer l'interface Component

Créez l'interface `InvestmentComponent` dans le package `com.bank.patterns.composite`

**Méthodes requises :**
- `double getValeur()` - Calculer la valeur actuelle
- `double getRendement()` - Calculer le rendement (en %)
- `String getDescription()` - Obtenir une description
- `void ajouter(InvestmentComponent component)` - Ajouter un élément (pour Composite)
- `void retirer(InvestmentComponent component)` - Retirer un élément
- `List<InvestmentComponent> getChildren()` - Obtenir les enfants

**Note** : Les méthodes `ajouter()` et `retirer()` lancent `UnsupportedOperationException` pour les Leaf.

#### Étape 2 : Créer les Leaf (instruments financiers)

Créez **trois classes** qui implémentent `InvestmentComponent` :

**1. Action.java**
- Attributs : `String symbole`, `int quantite`, `double prixUnitaire`
- `getValeur()` = quantite × prixUnitaire
- `getRendement()` = 5% (simplifié)
- `getDescription()` = "Action AAPL: 10 x 150.00€ = 1500.00€"
- `ajouter()`, `retirer()` : lancent `UnsupportedOperationException`
- `getChildren()` : retourne `Collections.emptyList()`

**2. Obligation.java**
- Attributs : `String nom`, `double montantNominal`, `double tauxInteret`
- `getValeur()` = montantNominal
- `getRendement()` = tauxInteret × 100 (en %)
- `getDescription()` = "Obligation FR-BOND: 5000.00€ à 2.0%"
- `ajouter()`, `retirer()` : lancent `UnsupportedOperationException`
- `getChildren()` : retourne `Collections.emptyList()`

**3. ETF.java**
- Attributs : `String nom`, `int parts`, `double prixPart`
- `getValeur()` = parts × prixPart
- `getRendement()` = 7% (simplifié)
- `getDescription()` = "ETF SP500: 50 parts x 100.00€ = 5000.00€"
- `ajouter()`, `retirer()` : lancent `UnsupportedOperationException`
- `getChildren()` : retourne `Collections.emptyList()`

**Indices communs pour les Leaf :**
- Utilisez `String.format()` pour formater les montants avec 2 décimales
- Exemple : `String.format("%.2f€", montant)`

#### Étape 3 : Créer le Composite (Portfolio)

Créez la classe `Portfolio` qui implémente `InvestmentComponent`

**Caractéristiques :**
- Attribut : `String nom` (nom du portefeuille)
- Attribut : `List<InvestmentComponent> investments` (instruments et sous-portefeuilles)

**Méthodes à implémenter :**

**getValeur()** :
- Somme récursive de la valeur de tous les éléments
- Utilisez un stream ou une boucle :
  ```java
  return investments.stream()
                   .mapToDouble(InvestmentComponent::getValeur)
                   .sum();
  ```

**getRendement()** :
- Rendement moyen pondéré par la valeur
- Formule : `somme(valeur_i * rendement_i) / valeur_totale`
- Indices :
  ```java
  double totalValeur = getValeur();
  if (totalValeur == 0) return 0;

  double sommeRendementsPonderes = 0;
  for (InvestmentComponent inv : investments) {
      double valeur = inv.getValeur();
      double rendement = inv.getRendement();
      sommeRendementsPonderes += valeur * rendement;
  }

  return sommeRendementsPonderes / totalValeur;
  ```

**getDescription()** :
- Affiche le nom du portefeuille et liste tous les éléments
- Format arborescent avec indentation pour les sous-portefeuilles
- Créez une méthode privée `getDescription(int niveau)` pour gérer l'indentation récursive

**ajouter() / retirer()** :
- Gèrent la liste `investments`
- Affichent un message de confirmation

**getChildren()** :
- Retourne une copie de la liste `investments`

#### Étape 4 : Créer la démonstration

Créez `CompositeDemo.java` qui démontre :

1. **Création d'instruments simples**
   - 3 actions (Apple, Microsoft, Google)
   - 2 obligations (gouvernement, entreprise)
   - 1 ETF (S&P 500)

2. **Création de sous-portefeuilles thématiques**
   - Portfolio "Actions Tech" contenant les 3 actions
   - Portfolio "Revenu Fixe" contenant les 2 obligations

3. **Création du portefeuille principal**
   - Contient les 2 sous-portefeuilles
   - Contient l'ETF directement

4. **Opérations**
   - Afficher la valeur totale du portefeuille principal
   - Afficher le rendement moyen du portefeuille
   - Afficher la description complète (hiérarchie visible)
   - Ajouter un nouvel instrument (ex: Action Tesla)
   - Retirer un instrument
   - Recalculer et afficher la nouvelle valeur

5. **Portfolio multi-niveaux (bonus)**
   - Créer un portfolio global qui contient le portfolio principal
   - Démontrer que le calcul fonctionne à 3 niveaux de profondeur

**Exemple de sortie attendue** :
```
=== PORTEFEUILLE PRINCIPAL ===
Valeur totale: 15750.00€
Rendement moyen: 4.52%

Description détaillée:
Portfolio: Portefeuille Principal (15750.00€)
  Portfolio: Actions Tech (4500.00€)
    Action AAPL: 10 x 150.00€ = 1500.00€
    Action MSFT: 15 x 120.00€ = 1800.00€
    Action GOOGL: 8 x 150.00€ = 1200.00€
  Portfolio: Revenu Fixe (6250.00€)
    Obligation FR-BOND-2030: 5000.00€ à 2.0%
    Obligation CORP-BOND: 1250.00€ à 3.5%
  ETF SP500-ETF: 50 parts x 100.00€ = 5000.00€
```

### 🎓 Points d'apprentissage
- Composite traite objets individuels et compositions uniformément
- Structure en arbre avec hiérarchie illimitée
- Opérations récursives (valeur, rendement)
- Permet de composer des portefeuilles complexes
- Calcul du rendement pondéré (important en finance !)
- Affichage hiérarchique avec indentation

### 💡 Points de discussion
1. **Transparence vs Sécurité** : Interface commune avec `ajouter()` → Leaf lance exception
2. **Calcul du rendement pondéré** : Pourquoi pondérer par la valeur ?
   - Exemple : 1000€ à 10% + 100€ à 5% = rendement moyen de ~9.5%, pas 7.5%
3. **Performance** : O(n) où n = nombre total d'instruments dans l'arbre

### ✅ Critères de validation
- [ ] Interface `InvestmentComponent` créée
- [ ] 3 Leaf créés (Action, Obligation, ETF)
- [ ] Composite `Portfolio` implémenté
- [ ] Calcul récursif de la valeur fonctionne
- [ ] Calcul du rendement pondéré fonctionne
- [ ] Possibilité de composer des portfolios de portfolios
- [ ] Description affiche la hiérarchie avec indentation
- [ ] Démo complète et fonctionnelle

---

## Exercice 2 : Pattern Iterator (1h30-2h)

### 🎯 Objectif
Créer un système d'itérateurs personnalisés complets pour parcourir les transactions d'un compte selon différents critères, en implémentant le pattern Iterator de manière formelle.

### 📋 Contexte
Vous allez créer vos **propres classes Iterator** pour avoir un contrôle total sur le parcours et ajouter des fonctionnalités comme `reset()` que `java.util.Iterator` n'a pas.

### 🔍 Avantages par rapport à java.util.Iterator
- Ajout de `reset()` pour recommencer le parcours
- Logique de tri/filtrage encapsulée dans l'iterator
- Possibilité d'ajouter d'autres méthodes personnalisées
- Contrôle total sur le comportement

### ✏️ Tâches

#### Étape 1 : Créer l'interface Iterator personnalisée

Créez l'interface `TransactionIterator` dans le package `com.bank.patterns.iterator`

**Méthodes requises :**
- `boolean hasNext()` - Y a-t-il un élément suivant ?
- `Transaction next()` - Obtenir l'élément suivant
- `void reset()` - Réinitialiser l'itérateur pour recommencer

#### Étape 2 : Créer l'interface Aggregate

Créez l'interface `TransactionCollection`

**Méthodes requises :**
- `TransactionIterator createChronologicalIterator()` - Iterator trié par date
- `TransactionIterator createAmountIterator()` - Iterator trié par montant
- `TransactionIterator createTypeIterator(String type)` - Iterator filtré par type
- `TransactionIterator createDateRangeIterator(Date start, Date end)` - Iterator filtré par période

#### Étape 3 : Créer les itérateurs concrets

Créez au moins 4 classes qui implémentent `TransactionIterator` :

**1. ChronologicalIterator**
- Trie les transactions par date (du plus ancien au plus récent)
- Maintient un index `position` pour le parcours
- `reset()` remet `position` à 0

**Structure suggérée :**
```java
public class ChronologicalIterator implements TransactionIterator {
    private List<Transaction> transactions; // Copie triée
    private int position = 0;

    public ChronologicalIterator(List<Transaction> transactions) {
        // 1. Copier la liste
        this.transactions = new ArrayList<>(transactions);
        // 2. Trier par date
        this.transactions.sort(Comparator.comparing(Transaction::getTransactionDate));
    }

    @Override
    public boolean hasNext() {
        return position < transactions.size();
    }

    @Override
    public Transaction next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return transactions.get(position++);
    }

    @Override
    public void reset() {
        position = 0;
    }
}
```

**2. AmountIterator**
- Trie les transactions par montant (croissant)
- Même structure que ChronologicalIterator
- Utilisez `Comparator.comparingDouble(Transaction::getAmount)`

**3. TypeIterator**
- Filtre les transactions par type (DEPOT, RETRAIT, VIREMENT)
- Trie les résultats filtrés par date
- Dans le constructeur :
  ```java
  // Copier et filtrer
  this.transactions = new ArrayList<>();
  for (Transaction tx : transactions) {
      if (tx.getType().equals(type)) {
          this.transactions.add(tx);
      }
  }
  // Trier
  this.transactions.sort(Comparator.comparing(Transaction::getTransactionDate));
  ```

**4. DateRangeIterator**
- Filtre les transactions dans une période donnée [start, end]
- Utilise `Date.before()` et `Date.after()` pour filtrer
- Trie les résultats par date

**Indices :**
- Toujours créer une **copie** de la liste pour ne pas modifier l'originale
- Tous les itérateurs ont la même structure : `List<Transaction> transactions` + `int position`
- `hasNext()` est toujours : `position < transactions.size()`
- `next()` est toujours : `return transactions.get(position++)`
- `reset()` est toujours : `position = 0`

#### Étape 4 : Créer l'Aggregate concret

Créez la classe `BankAccountTransactionCollection` qui implémente `TransactionCollection`

**Caractéristiques :**
- Contient une référence vers un `BankAccount`
- Chaque méthode `createXXXIterator()` instancie l'iterator correspondant en lui passant `account.getTransactions()`

**Structure :**
```java
public class BankAccountTransactionCollection implements TransactionCollection {
    private BankAccount account;

    public BankAccountTransactionCollection(BankAccount account) {
        this.account = account;
    }

    @Override
    public TransactionIterator createChronologicalIterator() {
        return new ChronologicalIterator(account.getTransactions());
    }

    @Override
    public TransactionIterator createAmountIterator() {
        return new AmountIterator(account.getTransactions());
    }

    // ... autres méthodes
}
```

#### Étape 5 : Créer la démonstration

Créez `IteratorDemo.java` qui démontre :

1. **Création d'un compte avec plusieurs transactions** (au moins 7)
   - Transactions à différentes dates
   - Transactions de différents types
   - Transactions de différents montants

2. **Création de la TransactionCollection**

3. **Parcours chronologique**
   ```java
   System.out.println("=== PARCOURS CHRONOLOGIQUE ===");
   TransactionIterator iterator = collection.createChronologicalIterator();
   while (iterator.hasNext()) {
       Transaction tx = iterator.next();
       System.out.println(tx.getDescription());
   }
   ```

4. **Parcours par montant**

5. **Filtrage par type (DEPOT)**

6. **Filtrage par période** (ex: 7 derniers jours)

7. **Démonstration de reset()**
   ```java
   System.out.println("\n=== DÉMONSTRATION DE RESET ===");
   System.out.println("Premier parcours:");
   while (iterator.hasNext()) {
       System.out.println(iterator.next().getTransactionId());
   }

   iterator.reset();

   System.out.println("\nDeuxième parcours (après reset):");
   while (iterator.hasNext()) {
       System.out.println(iterator.next().getTransactionId());
   }
   ```

**Méthode helper suggérée :**
```java
private static void printTransactions(String title, TransactionIterator iterator) {
    System.out.println("\n" + "=".repeat(60));
    System.out.println(title);
    System.out.println("=".repeat(60));

    int count = 0;
    while (iterator.hasNext()) {
        Transaction tx = iterator.next();
        count++;
        System.out.printf("%d. [%s] %s - %.2f EUR - %s\n",
            count, tx.getTransactionId(), tx.getType(),
            tx.getAmount(), tx.getTransactionDate());
    }

    System.out.println("Total: " + count + " transaction(s)");
}
```

### 🎓 Points d'apprentissage
- Iterator encapsule la logique de parcours
- Permet plusieurs algorithmes de parcours sans changer la collection
- Respect du principe de responsabilité unique (SRP)
- Possibilité de filtrer/transformer pendant l'itération
- `reset()` permet de parcourir plusieurs fois
- Pattern très utilisé (for-each en Java utilise Iterator)

### 💡 Points de discussion
1. **Pourquoi créer une copie ?** Pour ne pas modifier la collection originale et éviter `ConcurrentModificationException`
2. **java.util.Iterator vs TransactionIterator** : Notre version ajoute `reset()` et d'autres fonctionnalités
3. **Performance** : Le tri/filtrage se fait une fois dans le constructeur, pas à chaque `next()`

### ✅ Critères de validation
- [ ] Interface TransactionIterator avec hasNext(), next(), reset()
- [ ] Au moins 4 itérateurs concrets créés
- [ ] Les itérateurs ne modifient pas la collection originale
- [ ] Possibilité de parcourir plusieurs fois (reset fonctionne)
- [ ] Interface TransactionCollection créée
- [ ] BankAccountTransactionCollection implémenté
- [ ] Démo fonctionnelle avec tous les scénarios

---

## Exercice 3 : Pattern Flyweight (1h30)

### 🎯 Objectif
Optimiser l'affichage d'un journal de transactions bancaires (TransactionLog) en réduisant drastiquement la consommation mémoire grâce au partage d'objets immuables communs.

### 📋 Contexte
Une banque doit afficher l'historique complet des transactions d'un client. Pour un client actif, cela peut représenter des **dizaines de milliers de transactions** sur plusieurs années.

**Problème** : Chaque transaction contient des informations répétitives :
- **TypeOperation** : DEPOT, RETRAIT, VIREMENT (3 valeurs possibles seulement)
- **Devise** : EUR, USD, GBP (quelques devises seulement)
- **Canal** : AGENCE, DISTRIBUTEUR, INTERNET, MOBILE (4 canaux possibles)
- **Statut** : EN_COURS, COMPLETE, ANNULEE (3 statuts possibles)

Si on crée un nouvel objet pour chaque de ces attributs, **on gaspille énormément de mémoire**. Pour 100,000 transactions, on créerait potentiellement 400,000 objets alors qu'on pourrait n'en créer que 13 !

### 🔍 Principe du Pattern Flyweight

Le pattern Flyweight permet de **partager efficacement les objets immuables** identiques. On distingue :
- **Données intrinsèques** (partagées) : TypeOperation, Devise, Canal, Statut
- **Données extrinsèques** (uniques) : ID transaction, montant, date, comptes source/destination

### ✏️ Tâches

#### Étape 1 : Créer les classes Flyweight (données intrinsèques)

Créez dans le package `com.bank.patterns.flyweight` les classes suivantes :

**1. TypeOperation.java**
- Attributs : `String code`, `String libelle`
- Constructeur : `TypeOperation(String code, String libelle)`
- Getters uniquement (classe **immuable**)
- Exemples : DEPOT ("Dépôt"), RETRAIT ("Retrait"), VIREMENT ("Virement")

**2. Devise.java**
- Attributs : `String code`, `String symbole`
- Constructeur : `Devise(String code, String symbole)`
- Getters uniquement
- Exemples : EUR ("€"), USD ("$"), GBP ("£")

**3. Canal.java**
- Attributs : `String code`, `String description`
- Constructeur : `Canal(String code, String description)`
- Getters uniquement
- Exemples : AGENCE ("Agence bancaire"), DISTRIBUTEUR ("Distributeur automatique")

**4. Statut.java**
- Attributs : `String code`, `String description`
- Constructeur : `Statut(String code, String description)`
- Getters uniquement
- Exemples : COMPLETE ("Opération complétée"), EN_COURS ("En cours de traitement")

**⚠️ IMPORTANT** : Ces classes doivent être **immuables** (pas de setters, attributs final).

#### Étape 2 : Créer les Flyweight Factories

Créez les factories pour gérer le cache des flyweights :

**1. TypeOperationFactory.java**
```java
public class TypeOperationFactory {
    private final Map<String, TypeOperation> cache = new HashMap<>();

    public TypeOperation getTypeOperation(String code) {
        // Si existe dans cache, retourner l'instance existante
        // Sinon, créer une nouvelle instance et la mettre en cache
        // Retourner l'instance (partagée)
    }

    public int getNombreFlyweights() {
        // Retourne le nombre d'objets dans le cache
    }
}
```

**Indices** :
- Utilisez `Map.computeIfAbsent()` pour une implémentation élégante
- Ou bien `get()` + `if (null)` + `put()` pour une approche classique

**2. DeviseFactory.java**
- Même structure que TypeOperationFactory

**3. CanalFactory.java**
- Même structure que TypeOperationFactory

**4. StatutFactory.java**
- Même structure que TypeOperationFactory

**Bonus** : Ajoutez des compteurs `cacheHits` et `cacheMisses` pour les statistiques.

#### Étape 3 : Créer la classe Transaction avec Flyweights

Créez la classe `BankTransactionLog` qui utilise les flyweights :

**Attributs** :
- Flyweights (références partagées) :
  - `TypeOperation typeOperation`
  - `Devise devise`
  - `Canal canal`
  - `Statut statut`

- Données extrinsèques (uniques) :
  - `String id`
  - `String compteSource`
  - `String compteDestination`
  - `BigDecimal montant`
  - `LocalDateTime dateOperation`
  - `String libelle`

**Constructeur** :
```java
public BankTransactionLog(String id, TypeOperation typeOperation, Devise devise,
                          Canal canal, Statut statut, String compteSource,
                          String compteDestination, BigDecimal montant,
                          LocalDateTime dateOperation, String libelle) {
    // Stocker les références vers les flyweights
    this.typeOperation = typeOperation;
    this.devise = devise;
    // ... autres attributs
}
```

**Méthodes** :
- Getters pour tous les attributs
- `String afficher()` : retourne une représentation formatée de la transaction

#### Étape 4 : Créer le TransactionLogManager

Créez la classe `TransactionLogManager` qui gère la création des transactions :

**Attributs** :
```java
private List<BankTransactionLog> transactions = new ArrayList<>();
private TypeOperationFactory typeOpFactory = new TypeOperationFactory();
private DeviseFactory deviseFactory = new DeviseFactory();
private CanalFactory canalFactory = new CanalFactory();
private StatutFactory statutFactory = new StatutFactory();
```

**Méthodes** :
```java
public void ajouterTransaction(String id, String typeOpCode, String deviseCode,
                               String canalCode, String statutCode,
                               String compteSource, String compteDestination,
                               BigDecimal montant, LocalDateTime date, String libelle) {
    // 1. Récupérer les flyweights via les factories
    TypeOperation typeOp = typeOpFactory.getTypeOperation(typeOpCode);
    Devise devise = deviseFactory.getDevise(deviseCode);
    Canal canal = canalFactory.getCanal(canalCode);
    Statut statut = statutFactory.getStatut(statutCode);

    // 2. Créer la transaction avec les flyweights
    BankTransactionLog transaction = new BankTransactionLog(
        id, typeOp, devise, canal, statut,
        compteSource, compteDestination, montant, date, libelle
    );

    // 3. Ajouter à la liste
    transactions.add(transaction);
}

public void afficherResume() {
    // Afficher le nombre de transactions
    // Afficher le nombre de flyweights créés pour chaque type
}

public void afficherStatistiques() {
    // Afficher les cache hits/misses
    // Calculer le taux de réutilisation
}
```

#### Étape 5 : Créer la démonstration

Créez `FlyweightDemo.java` qui démontre :

1. **Création du TransactionLogManager**

2. **Chargement de nombreuses transactions** (au moins 1000 pour observer l'effet)
   - Variez les types d'opérations
   - Utilisez plusieurs devises
   - Utilisez différents canaux et statuts
   - Générez des montants et dates aléatoires

3. **Comparaison SANS vs AVEC Flyweight** :
   - Calculez l'estimation de mémoire sans flyweight : `nbTransactions * 4 objets par transaction`
   - Affichez le nombre réel de flyweights créés
   - Calculez le gain : `(objets_sans - objets_avec) / objets_sans * 100`

4. **Affichage des statistiques** :
   - Nombre de flyweights par type
   - Taux de réutilisation (cache hits vs misses)

5. **Démonstration du partage** :
   - Vérifiez que deux transactions avec le même type partagent la même instance
   ```java
   BankTransactionLog tx1 = transactions.get(0);
   BankTransactionLog tx2 = transactions.get(1);
   boolean sameInstance = (tx1.getTypeOperation() == tx2.getTypeOperation());
   ```

**Exemple de sortie attendue** :
```
=== JOURNAL DE TRANSACTIONS BANCAIRES ===
Nombre de transactions chargées : 10,000

SANS Flyweight :
  - Objets créés : 40,000 (10,000 transactions × 4 objets chacune)

AVEC Flyweight :
  - Transactions créées : 10,000
  - TypeOperation flyweights : 3
  - Devise flyweights : 3
  - Canal flyweights : 4
  - Statut flyweights : 3
  - Total flyweights : 13
  - Gain mémoire : 99.97% d'objets en moins !

Statistiques de cache :
  - TypeOperation : 9,997 hits, 3 misses (99.97% réutilisation)
  - Devise : 9,997 hits, 3 misses
  - Canal : 9,996 hits, 4 misses
  - Statut : 9,997 hits, 3 misses
```

### 🎓 Points d'apprentissage
- Flyweight réduit drastiquement la consommation mémoire
- Distinction entre données intrinsèques (partagées) et extrinsèques (uniques)
- Les flyweights doivent être **immuables**
- Utilisation de factories pour gérer le cache
- Pattern très utile quand on a beaucoup d'objets avec des données répétitives
- Trade-off : légère complexité en échange de gros gains mémoire

### 💡 Points de discussion
1. **Quand utiliser Flyweight ?** Quand on a beaucoup d'objets similaires (> 1000) avec données répétitives
2. **Pourquoi l'immuabilité ?** Si un objet change, toutes les références seraient affectées !
3. **Flyweight vs Singleton ?** Singleton = 1 instance unique. Flyweight = quelques instances partagées
4. **Performance** : Légère surcharge au premier accès (création), puis accès instantané (cache)

### ✅ Critères de validation
- [ ] Classes flyweight créées (TypeOperation, Devise, Canal, Statut) et immuables
- [ ] 4 factories créées avec cache
- [ ] BankTransactionLog utilise des références vers les flyweights
- [ ] TransactionLogManager coordonne la création
- [ ] Démonstration montre le gain de mémoire (> 95%)
- [ ] Statistiques de cache (hits/misses) fonctionnent
- [ ] Vérification que les instances sont bien partagées (==)

---

## Exercice 4 : Pattern Memento (2h)

### 🎯 Objectif
Implémenter un système de sauvegarde et restauration d'états pour les comptes bancaires, permettant d'annuler des opérations et de conserver un historique complet des modifications.

### 📋 Contexte
Une banque souhaite offrir à ses clients premium la possibilité de **revenir en arrière** sur certaines opérations bancaires. De plus, le système doit conserver un **audit trail complet** de tous les changements d'état d'un compte.

**Cas d'usage** :
- Un client effectue une série d'opérations et réalise une erreur
- Il peut restaurer l'état du compte à un point antérieur
- La banque doit garder une trace de tous les états précédents pour l'audit
- Le système doit protéger l'encapsulation : les états sauvegardés ne doivent pas être modifiables

### 🔍 Principe du Pattern Memento

Le pattern Memento permet de **capturer et externaliser l'état interne** d'un objet sans violer l'encapsulation, afin de pouvoir restaurer cet état ultérieurement.

**Trois rôles** :
1. **Originator** (BankAccount) : L'objet dont on veut sauvegarder l'état
2. **Memento** (AccountMemento) : Stocke l'état de l'Originator
3. **Caretaker** (AccountHistory) : Gère les mementos (historique)

### ✏️ Tâches

#### Étape 1 : Créer la classe Memento

Créez la classe `AccountMemento` dans le package `com.bank.patterns.memento` :

**Caractéristiques** :
- Classe **immuable** qui stocke une copie de l'état d'un compte
- Attributs :
  ```java
  private final String accountNumber;
  private final double balance;
  private final String status;
  private final double overdraftLimit;
  private final LocalDateTime timestamp;  // Moment de la sauvegarde
  private final String description;        // Description de l'état
  ```

**Méthodes** :
- Constructeur qui initialise tous les attributs
- Getters uniquement (pas de setters - immuabilité)
- `String getInfo()` : retourne une description lisible de l'état

**Indices** :
```java
public class AccountMemento {
    private final String accountNumber;
    private final double balance;
    private final String status;
    private final double overdraftLimit;
    private final LocalDateTime timestamp;
    private final String description;

    // Constructeur package-private : seul BankAccount peut créer des mementos
    AccountMemento(String accountNumber, double balance, String status,
                   double overdraftLimit, String description) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
        this.overdraftLimit = overdraftLimit;
        this.timestamp = LocalDateTime.now();
        this.description = description;
    }

    // Getters package-private : seul BankAccount peut lire les détails
    String getAccountNumber() { return accountNumber; }
    double getBalance() { return balance; }
    String getStatus() { return status; }
    double getOverdraftLimit() { return overdraftLimit; }

    // Méthodes publiques pour l'information
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }

    public String getInfo() {
        return String.format("[%s] %s - Solde: %.2f EUR - Statut: %s",
            timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            description, balance, status);
    }
}
```

**⚠️ IMPORTANT** : Les getters détaillés (`getBalance()`, etc.) sont **package-private** pour que seul `BankAccount` puisse y accéder. Cela protège l'encapsulation.

#### Étape 2 : Modifier BankAccount pour supporter Memento

Ajoutez des méthodes à la classe `BankAccount` (ou créez une classe `MementoBankAccount` qui étend `BankAccount`) :

**Méthodes à ajouter** :

**1. createMemento()** - Créer un snapshot de l'état actuel
```java
public AccountMemento createMemento(String description) {
    return new AccountMemento(
        this.accountNumber,
        this.balance,
        this.status,
        this.overdraftLimit,
        description
    );
}
```

**2. restoreFromMemento()** - Restaurer l'état depuis un memento
```java
public void restoreFromMemento(AccountMemento memento) {
    // Vérifier que c'est bien le bon compte
    if (!this.accountNumber.equals(memento.getAccountNumber())) {
        throw new IllegalArgumentException(
            "Impossible de restaurer : le memento appartient à un autre compte"
        );
    }

    // Restaurer l'état
    this.balance = memento.getBalance();
    this.status = memento.getStatus();
    this.overdraftLimit = memento.getOverdraftLimit();

    System.out.println("Compte restauré à l'état : " + memento.getDescription());
}
```

#### Étape 3 : Créer le Caretaker (Gestionnaire d'historique)

Créez la classe `AccountHistory` qui gère l'historique des mementos :

**Attributs** :
```java
private final List<AccountMemento> history = new ArrayList<>();
private int currentPosition = -1;  // Position actuelle dans l'historique
```

**Méthodes** :

**1. save()** - Sauvegarder un nouvel état
```java
public void save(AccountMemento memento) {
    // Si on n'est pas à la fin de l'historique, supprimer les états "futurs"
    if (currentPosition < history.size() - 1) {
        history.subList(currentPosition + 1, history.size()).clear();
    }

    // Ajouter le nouveau memento
    history.add(memento);
    currentPosition++;

    System.out.println("État sauvegardé : " + memento.getDescription());
}
```

**2. undo()** - Revenir à l'état précédent
```java
public AccountMemento undo() {
    if (currentPosition <= 0) {
        System.out.println("Impossible de revenir en arrière : début de l'historique");
        return null;
    }

    currentPosition--;
    return history.get(currentPosition);
}
```

**3. redo()** - Avancer à l'état suivant
```java
public AccountMemento redo() {
    if (currentPosition >= history.size() - 1) {
        System.out.println("Impossible d'avancer : fin de l'historique");
        return null;
    }

    currentPosition++;
    return history.get(currentPosition);
}
```

**4. Methods complémentaires** :
```java
public void showHistory() {
    System.out.println("\n=== HISTORIQUE DES ÉTATS ===");
    for (int i = 0; i < history.size(); i++) {
        String marker = (i == currentPosition) ? " <-- ACTUEL" : "";
        System.out.println(i + ". " + history.get(i).getInfo() + marker);
    }
}

public AccountMemento getCurrent() {
    return currentPosition >= 0 ? history.get(currentPosition) : null;
}

public int getHistorySize() {
    return history.size();
}

public boolean canUndo() {
    return currentPosition > 0;
}

public boolean canRedo() {
    return currentPosition < history.size() - 1;
}
```

#### Étape 4 : Créer la démonstration

Créez `MementoDemo.java` qui démontre :

1. **Création d'un compte avec état initial**
   ```java
   BankAccount compte = new BankAccount(...);
   AccountHistory history = new AccountHistory();

   // Sauvegarder l'état initial
   history.save(compte.createMemento("État initial"));
   ```

2. **Effectuer des opérations en sauvegardant après chacune**
   ```java
   // Dépôt
   compte.setBalance(compte.getBalance() + 1000);
   history.save(compte.createMemento("Après dépôt de 1000 EUR"));

   // Retrait
   compte.setBalance(compte.getBalance() - 300);
   history.save(compte.createMemento("Après retrait de 300 EUR"));

   // Changement de statut
   compte.setStatus("SUSPENDED");
   history.save(compte.createMemento("Compte suspendu"));
   ```

3. **Démonstration de UNDO**
   ```java
   System.out.println("\n=== ANNULATION (UNDO) ===");
   AccountMemento previous = history.undo();
   if (previous != null) {
       compte.restoreFromMemento(previous);
   }
   ```

4. **Démonstration de REDO**
   ```java
   System.out.println("\n=== RÉTABLIR (REDO) ===");
   AccountMemento next = history.redo();
   if (next != null) {
       compte.restoreFromMemento(next);
   }
   ```

5. **Affichage de l'historique complet**
   ```java
   history.showHistory();
   ```

6. **Navigation dans l'historique**
   - Faire plusieurs undo successifs
   - Afficher l'état courant
   - Faire une nouvelle opération après un undo (l'historique "futur" est supprimé)

7. **Cas d'usage audit**
   - Montrer comment parcourir tout l'historique pour un audit
   - Exporter les états dans un log

**Exemple de sortie attendue** :
```
=== DÉMONSTRATION DU PATTERN MEMENTO ===

État initial sauvegardé
Solde actuel : 1000.00 EUR

--- Opération 1 : Dépôt de 500 EUR ---
État sauvegardé : Après dépôt de 500 EUR
Solde actuel : 1500.00 EUR

--- Opération 2 : Retrait de 200 EUR ---
État sauvegardé : Après retrait de 200 EUR
Solde actuel : 1300.00 EUR

--- Opération 3 : Suspension du compte ---
État sauvegardé : Compte suspendu
Statut actuel : SUSPENDED

=== ANNULATION (UNDO) ===
Compte restauré à l'état : Après retrait de 200 EUR
Solde actuel : 1300.00 EUR
Statut actuel : ACTIVE

=== HISTORIQUE DES ÉTATS ===
0. [2024-01-15 10:00:00] État initial - Solde: 1000.00 EUR - Statut: ACTIVE
1. [2024-01-15 10:05:00] Après dépôt de 500 EUR - Solde: 1500.00 EUR - Statut: ACTIVE
2. [2024-01-15 10:10:00] Après retrait de 200 EUR - Solde: 1300.00 EUR - Statut: ACTIVE <-- ACTUEL
3. [2024-01-15 10:15:00] Compte suspendu - Solde: 1300.00 EUR - Statut: SUSPENDED
```

### 🎓 Points d'apprentissage
- Memento permet de sauvegarder/restaurer l'état sans violer l'encapsulation
- Utile pour undo/redo et audit trail
- Le Memento est immuable pour éviter les modifications accidentelles
- Visibilité package-private protège les détails internes
- Distinction entre état public (info) et état privé (détails de restauration)
- Trade-off : consommation mémoire si beaucoup de sauvegardes

### 💡 Points de discussion
1. **Memento vs Serialization ?** Memento offre plus de contrôle et protège mieux l'encapsulation
2. **Gestion de la mémoire** : Comment limiter la taille de l'historique ? (ex: garder seulement les 50 derniers états)
3. **Mementos delta** : Stocker seulement les différences au lieu de l'état complet
4. **Thread safety** : Comment rendre l'historique thread-safe ?
5. **Persistence** : Comment sauvegarder l'historique sur disque ?

### ✅ Critères de validation
- [ ] Classe AccountMemento créée et immuable
- [ ] Getters détaillés en package-private
- [ ] BankAccount supporte createMemento() et restoreFromMemento()
- [ ] AccountHistory gère l'historique avec undo/redo
- [ ] L'historique "futur" est supprimé lors d'une nouvelle opération après undo
- [ ] showHistory() affiche tous les états avec marqueur de position
- [ ] Démonstration complète avec scénarios undo/redo
- [ ] Protection de l'encapsulation vérifiée

---

## 🎯 Synthèse Session 2

### Patterns appliqués

```
Banking System - Session 2
├── Exercice 1: Composite (portefeuille d'investissement)
│   ├── InvestmentComponent (interface)
│   ├── Action, Obligation, ETF (Leaf)
│   └── Portfolio (Composite)
├── Exercice 2: Iterator (parcours personnalisé des transactions)
│   ├── TransactionIterator (interface)
│   ├── ChronologicalIterator
│   ├── AmountIterator
│   ├── TypeIterator
│   └── DateRangeIterator
├── Exercice 3: Flyweight (optimisation mémoire)
│   ├── TypeOperation, Devise, Canal, Statut (Flyweights)
│   ├── TypeOperationFactory, DeviseFactory, etc. (Factories)
│   ├── BankTransactionLog (utilise les flyweights)
│   └── TransactionLogManager (coordonnateur)
└── Exercice 4: Memento (undo/redo et audit)
    ├── AccountMemento (stocke l'état)
    ├── BankAccount (Originator)
    └── AccountHistory (Caretaker)
```

### Métriques de réussite

À la fin de la Session 2, vous devriez avoir :
- ✅ Pattern Composite implémenté avec calcul récursif et rendement pondéré
- ✅ Pattern Iterator avec au moins 4 itérateurs personnalisés
- ✅ Pattern Flyweight avec réduction mémoire > 95%
- ✅ Pattern Memento avec undo/redo fonctionnel
- ✅ Possibilité de parcourir les transactions de multiples façons
- ✅ Système d'historique complet pour audit
- ✅ Code client simple et découplé

### Questions de révision

1. Quelle est la différence entre Composite et Decorator ?
2. Pourquoi Iterator crée-t-il une copie de la liste ?
3. Comment implémenter un itérateur qui modifie la collection pendant le parcours ?
4. Peut-on combiner plusieurs filtres (ex: Type ET DateRange) ?
5. Quelle est la différence entre `java.util.Iterator` et votre `TransactionIterator` ?
6. Pourquoi avoir ajouté `reset()` dans l'interface Iterator ?
7. Comment calculer un rendement pondéré et pourquoi est-ce important ?
8. Quelle est la différence entre Flyweight et Singleton ?
9. Pourquoi les flyweights doivent-ils être immuables ?
10. Quelle est la différence entre Memento et Serialization ?
11. Comment limiter la consommation mémoire de l'historique dans Memento ?
12. Dans quels cas Flyweight est-il vraiment utile ?

### Exercices supplémentaires (si temps disponible)

1. **Itérateur inversé** : Créer un ReverseChronologicalIterator
2. **Itérateur avec critères multiples** : Combiner type ET montant minimum
3. **Itérateur lazy** : Ne trier/filtrer qu'au moment du parcours (pas dans le constructeur)
4. **Composite avec limites** : Portfolio avec limite de nombre d'instruments
5. **Iterator avec callback** : Exécuter une action sur chaque transaction
6. **Iterator bidirectionnel** : Ajouter une méthode `previous()`

---

## 📚 Ressources

- Code source à créer dans `/src/main/java/com/bank/patterns/composite/`
- Code source à créer dans `/src/main/java/com/bank/patterns/iterator/`
- Solutions complètes dans `/GUIDE_INSTRUCTEUR.md`
- Documentation Java Iterator : https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html

Bon courage ! 🚀
