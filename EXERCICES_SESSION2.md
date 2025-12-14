# Exercices Session 2 - Patterns Structurels Avancés

## Vue d'ensemble

Cette session approfondit les patterns structurels en implémentant le pattern **Composite** dans un nouveau contexte (portefeuille d'investissement) et le pattern **Iterator** pour parcourir des collections de manière élégante.

**Durée totale estimée :** 4-5 heures

**Patterns couverts :**
- Composite Pattern (portefeuille d'investissement)
- Iterator Pattern
- Combinaison Composite + Iterator

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

## Exercice Bonus : Combiner Composite et Iterator (30min)

### 🎯 Objectif
Créer un itérateur qui parcourt TOUS les instruments d'un portefeuille (y compris ceux dans les sous-portefeuilles) de manière plate, pour obtenir une liste complète de tous les investissements.

### 📋 Contexte
Vous avez un portefeuille principal avec plusieurs sous-portefeuilles. Vous voulez parcourir **tous les instruments financiers individuels** (actions, obligations, ETF) sans vous soucier de la hiérarchie.

**Exemple** :
```
Portefeuille Principal
├── Portfolio Tech
│   ├── Action AAPL
│   └── Action MSFT
├── Portfolio Obligations
│   ├── Obligation FR-BOND
│   └── Obligation CORP
└── ETF SP500

Parcours plat attendu :
1. Action AAPL
2. Action MSFT
3. Obligation FR-BOND
4. Obligation CORP
5. ETF SP500
```

### ✏️ Tâches

Créez la classe `PortfolioIterator` dans le package `com.bank.patterns.iterator`

**Caractéristiques :**
- Utilise une `Stack<InvestmentComponent>` pour le parcours en profondeur
- Initialisation : empiler la racine
- `next()` :
  - Dépiler un élément
  - Si c'est un Portfolio : empiler ses enfants et continuer (ne pas le retourner)
  - Si c'est un instrument : le retourner
- **Important** : Ne retourne que les feuilles (instruments), pas les portfolios

**Structure suggérée :**
```java
public class PortfolioIterator {
    private Stack<InvestmentComponent> stack = new Stack<>();

    public PortfolioIterator(InvestmentComponent root) {
        stack.push(root);
    }

    public boolean hasNext() {
        // Vérifier s'il reste des éléments à traiter
        // Peut nécessiter de "sauter" les portfolios pour trouver le prochain instrument
        return !stack.isEmpty();
    }

    public InvestmentComponent next() {
        // Algorithme :
        while (!stack.isEmpty()) {
            InvestmentComponent current = stack.pop();

            // Si c'est un Portfolio, ajouter ses enfants à la pile
            if (current instanceof Portfolio) {
                List<InvestmentComponent> children = current.getChildren();
                // Ajouter en ordre inverse pour préserver l'ordre
                for (int i = children.size() - 1; i >= 0; i--) {
                    stack.push(children.get(i));
                }
                // Ne pas retourner le portfolio, continuer la boucle
            } else {
                // C'est un instrument (Action, Obligation, ETF)
                return current;
            }
        }
        throw new NoSuchElementException("No more instruments");
    }
}
```

**Utilisation :**
```java
// Créer un portefeuille complexe avec sous-portefeuilles
Portfolio mainPortfolio = createComplexPortfolio();

// Parcourir tous les instruments de manière plate
PortfolioIterator iterator = new PortfolioIterator(mainPortfolio);

System.out.println("=== Tous les instruments (parcours plat) ===");
while (iterator.hasNext()) {
    InvestmentComponent instrument = iterator.next();
    System.out.println(instrument.getDescription());
}

// Résultat : uniquement les instruments individuels,
// pas les portfolios intermédiaires
```

**Extension (optionnelle) :**
- Ajoutez une méthode `reset()` pour recommencer le parcours depuis le début
- Ajoutez un filtre pour ne retourner que certains types (ex: uniquement les actions)

### 🎓 Points d'apprentissage
- Combinaison de deux patterns (Composite + Iterator)
- Parcours en profondeur avec Stack
- Algorithme itératif vs récursif
- Filtrage des types pendant le parcours

### ✅ Critères de validation
- [ ] Parcourt tous les instruments de manière plate
- [ ] Ne retourne pas les portfolios intermédiaires
- [ ] Utilise une Stack pour le parcours en profondeur
- [ ] Fonctionne avec des hiérarchies de profondeur arbitraire

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
└── Exercice Bonus: Composite + Iterator
    └── PortfolioIterator (parcours plat d'un arbre)
```

### Métriques de réussite

À la fin de la Session 2, vous devriez avoir :
- ✅ Pattern Composite implémenté avec calcul récursif et rendement pondéré
- ✅ Pattern Iterator avec au moins 4 itérateurs personnalisés
- ✅ Possibilité de parcourir les transactions de multiples façons
- ✅ Code client simple et découplé
- ✅ Tests pour chaque itérateur
- ✅ Bonus : Combinaison de Composite et Iterator

### Questions de révision

1. Quelle est la différence entre Composite et Decorator ?
2. Pourquoi Iterator crée-t-il une copie de la liste ?
3. Comment implémenter un itérateur qui modifie la collection pendant le parcours ?
4. Peut-on combiner plusieurs filtres (ex: Type ET DateRange) ?
5. Quelle est la différence entre `java.util.Iterator` et votre `TransactionIterator` ?
6. Pourquoi avoir ajouté `reset()` dans l'interface Iterator ?
7. Comment calculer un rendement pondéré et pourquoi est-ce important ?

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
