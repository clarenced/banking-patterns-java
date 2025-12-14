# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Java training project for teaching design patterns in a banking context. The codebase contains intentionally poorly-written legacy code that students refactor using various design patterns.

**Language**: French (all class materials, exercises, and code comments)

## Build Commands

```bash
# Compile the project
mvn clean compile

# Run all tests
mvn test

# Run the legacy main program
mvn exec:java -Dexec.mainClass="com.bank.legacy.old.Main"

# Run a specific pattern demo (replace with actual class)
mvn exec:java -Dexec.mainClass="com.bank.patterns.decorator.BankAccountDemo"
```

## Architecture

```
src/main/java/com/bank/
├── legacy/           # Intentionally flawed code for refactoring exercises
│                     # Contains BankAccount, Transaction, BankingService
└── patterns/         # Refactored implementations using design patterns
    ├── adapter/      # Payment system adapters (Visa, MasterCard)
    ├── adapter2/     # External system adapters (ZOS mainframe, Cardif)
    ├── bridge/       # Account types × Notification channels
    ├── decorator/    # Account features (overdraft, insurance, premium)
    ├── decorator2/   # Transfer fee calculations
    ├── facade/       # Banking operations facade
    ├── mediator/     # Payment processing coordination
    ├── observer/     # Transfer event notifications
    ├── prototype/    # Transaction cloning
    ├── singleton/    # Configuration and ID generation
    └── template/     # Account opening processors
```

## Key Conventions

- **Legacy code**: Files in `legacy/` are meant to have problems (code duplication, mixed responsibilities, tight coupling). Do not "fix" these unless explicitly asked.
- **Pattern demos**: Each pattern package contains a `*Demo.java` with a main method demonstrating the pattern usage.
- **Testing**: JUnit 5 tests are in `src/test/java/`. Run tests after each refactoring.

## Training Materials

- `EXERCICES_SESSION1.md` - Behavioral patterns exercises
- `EXERCICES_SESSION2.md` - Structural patterns exercises
- `GUIDE_INSTRUCTEUR.md` - Complete solutions (instructor guide)
- `REFACTORING_STRATEGY_BABYSTEPS.md` - Incremental refactoring approach
