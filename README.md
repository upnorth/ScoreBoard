# Sportradar Score Board

## Code structure

The library is implemented as a standard Java 22/Maven project with JUnit 5 and AssertJ for testing.

The code is organized in the ScoreBoard, Match and Team Java-classes that simply represent the relevant entities of the use-case and its business logic.

## Usage

### Running the code

As per the instructions there is no standalone runnable implementation in terms of building and running the project in the terminal or as a web REST API.
It's simply a library class accessible via regular constructors and methods.
It can also be run through the test suite inside intellij.

### Start a new Score Board

```
new ScoreBoard();
```

An empty scoreboard is initialized simply by instantiating the ScoreBoard.java class.
This creates an empty scoreboard that you can start adding new matches to.

### Operation 1: Add new match

```
int matchId = scoreBoard.newMatch("Home team name", "Away team name");
```

With that instance new matches can be added by providing the team names, home and away respectively.
The code checks that valid names are provided, null or empty strings are not permitted.
Those teams both get 0 as starting scores.
Successfully adding a new match returns its unique id for future reference.
The code checks that the team names in a new match doesn't already exist in any match currently in the Score Board as the same team naturally can't play multiple concurrent matches.

### Operation 2: Update scores of active matches

```
scoreBoard.updateMatch(matchId, updatedHomeTeamScore, updatedAwayTeamScore);
```

Current match scores can be updated by simply providing the matchId and the new scores for the home and away team respectively.
One team at a time can get one more goal, all other changes are checked and blocked including updates to identical scores as it's a redundant update.

### Operation 3: Remove finished matches

```
scoreBoard.finishMatch(matchId);
```

Any existing match can easily be removed with its unique match id as above.
Removing a match id that doesn't exist will cause an error.

###  Operation 4: Get scoreboard summary

```
scoreBoard.getSummary();
```

getSummary() simply returns a list of Match objects sorted with bigger total score first, or in case of identical totals, earlier added first.
The data in this list can then be iterated over and used by the library user.

## Developer notes

The approach for implementation was to read through all instructions before getting started.
It's hard to pick up on all the details on the first read-through, but it should be done at least once.

After that a basic Java/Maven project was created through intellij and a basic class with a test suite was added.
From there commits in Git with detailed commit-messages have been used to document the process.
One of the commits doesn't even compile, on purpose, to showcase TDD and adding tests for things that doesn't yet exist.
Although development has not been strictly been test-first-implementation-second.
Sometimes getting into a flow causes you to run off-road for a bit, so to speak.

The solution has been implemented an operation at a time with continuous refactoring of the code as it grew.
The first versions of operations 1-3 including tests were implemented day 1 in about 6 hours.
The first version of the README was added as well.

Second day I spent a couple of hours on the summary.
The first version having working sorting with a corresponding minimal test with both same and different total scores.
In the second iteration I changed from Map to List as data-structure for the current matches to simplify sorting syntax.