# Formation Design Patterns - Système Bancaire

## 📋 Contexte

Ce projet de formation vous permet d'apprendre et de pratiquer les design patterns dans un contexte bancaire réaliste.

Vous allez travailler sur un **code legacy mal écrit** que vous allez progressivement refactorer en appliquant différents design patterns.

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
│   ├── legacy/              # Code legacy à refactorer
│   │   ├── BankAccount.java
│   │   ├── Transaction.java
│   │   ├── BankingService.java
│   │   └── Main.java
│   └── patterns/            # Code refactoré avec patterns
│       ├── command/
│       ├── state/
│       ├── composite/
│       ├── chain/
│       ├── observer/
│       ├── iterator/
│       └── template/
├── src/test/java/com/bank/
│   └── legacy/              # Tests
├── EXERCICES_SESSION1.md    # Exercices Session 1
├── EXERCICES_SESSION2.md    # Exercices Session 2
├── GUIDE_INSTRUCTEUR.md     # Solutions complètes
└── pom.xml
```

## 🚀 Démarrage

### Prérequis
- Java 17 ou supérieur
- Maven 3.6 ou supérieur
- Un IDE Java (IntelliJ IDEA, Eclipse, VS Code)

### Installation

```bash
# Cloner le projet
git clone <url-du-repo>
cd banking-patterns-java

# Compiler le projet
mvn clean compile
```

### Exécution

```bash
# Exécuter le programme principal
mvn exec:java -Dexec.mainClass="com.bank.legacy.old.Main"

# Exécuter les tests
mvn test

# Exécuter un pattern spécifique (exemple)
mvn exec:java -Dexec.mainClass="com.bank.patterns.decorator2.Demo"
```

## 📚 Parcours de formation

### Session 1 - Patterns Comportementaux
Voir le fichier [EXERCICES_SESSION1.md](EXERCICES_SESSION1.md)

**Patterns couverts :**
- Command Pattern
- State Pattern
- Composite Pattern
- Chain of Responsibility Pattern
- Observer Pattern

**Durée estimée :** 6-7 heures

---

### Session 2 - Patterns Structurels Avancés
Voir le fichier [EXERCICES_SESSION2.md](EXERCICES_SESSION2.md)

**Patterns couverts :**
- Composite Pattern (approfondissement)
- Iterator Pattern
- Combinaison de patterns

**Durée estimée :** 4-5 heures

---

## 📖 Documentation

- **[EXERCICES_SESSION1.md](EXERCICES_SESSION1.md)** : Exercices de la session 1
- **[EXERCICES_SESSION2.md](EXERCICES_SESSION2.md)** : Exercices de la session 2
- **[GUIDE_INSTRUCTEUR.md](GUIDE_INSTRUCTEUR.md)** : Solutions complètes et guide pour l'instructeur
- **[REFACTORING_STRATEGY_BABYSTEPS.md](REFACTORING_STRATEGY_BABYSTEPS.md)** : Stratégie de refactoring progressif

## 📚 Ressources complémentaires

### Liens utiles
- [Refactoring Guru - Design Patterns](https://refactoring.guru/design-patterns)
- [Source Making - Design Patterns](https://sourcemaking.com/design_patterns)
- [Java Iterator Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html)

### Conseils
- Refactorisez progressivement, ne réécrivez pas tout d'un coup
- Faites tourner les tests après chaque modification
- Committez régulièrement vos changements
- Discutez des choix de design avec vos collègues

## ✅ Critères de réussite

À la fin de la formation, vous devriez avoir :

1. ✅ Éliminé le code dupliqué
2. ✅ Séparé les responsabilités (SRP)
3. ✅ Rendu le code facilement extensible (OCP)
4. ✅ Découplé les composants
5. ✅ Appliqué les 7 design patterns principaux
6. ✅ Maintenu les tests verts

---

## 🤝 Contribution

Ce projet est un support de formation. N'hésitez pas à :
- Poser des questions à votre formateur
- Partager vos solutions avec le groupe
- Proposer des améliorations

Bon courage ! 🚀
