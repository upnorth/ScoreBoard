# Sportradar Score Board

## Code structure

The library is implemented as a standard Java 21/Maven project with JUnit 5 and AssertJ for testing.

The code is organized in the ScoreBoard, Match and Team Java-classes that simply represent the relevant entities of the
use-case and its business logic.

## Usage

### Running the code

As per the instructions there is no standalone runnable implementation in terms of building and running the project in
the terminal or as a web REST API.
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
The code checks that the team names in a new match doesn't already exist in any match currently in the Score Board as
the same team naturally can't play multiple concurrent matches.

### Operation 2: Update scores of active matches

```
scoreBoard.updateMatch(matchId, updatedHomeTeamScore, updatedAwayTeamScore);
```

Current match scores can be updated by simply providing the matchId and the new scores for the home and away team
respectively.
One team at a time can get one more goal, all other changes are checked and blocked including updates to identical
scores as it's a redundant update.

### Operation 3: Remove finished matches

```
scoreBoard.finishMatch(matchId);
```

Any existing match can easily be removed with its unique match id as above.
Removing a match id that doesn't exist will cause an error.

### Operation 4: Get scoreboard summary

```
scoreBoard.getSummary();
```

getSummary() simply returns a list of Match objects sorted with bigger total score first, or in case of identical
totals, earlier added first.
The data in this list can then be iterated over and used by the library user.

## Developer notes

### Assumptions

#### To be used in fairly modern projects

Selected Java 21 to enable some nice syntax, but that can of course limit use of the library especially in bigger
systems that usually doesn't get language upgrades unless they are critical.

Initially intellij had suggested version 22 but since that's very new and 21 is at least the latest LTS I downgraded to
that (pom.xml and project settings).
Everything worked as before so the code wasn't using any Java 22 features, which isn't surprising since I only have a
few months experience with 21 so far.

#### Explicit match id

In order to create an easy way to sort the matches on when they were added to the score board I decided to add the
internal match id.
It would likely be possible to implement an index based solution where that explicit field isn't required,
but having the id field does enable intuitive business logic implementation and also a more free choice of how to store
and manage the data.
The requirements state that a finished match should be removed from the summary, but that could be controlled with a
boolean flag as well.
That would also enable keeping track of historical matches, which would probably be a nice feature to add.

#### Teams must have valid names

Mostly driven by a realization that null-checks seemed like they should be redundant,
but also seemed like a reasonable business requirement to have.
Valid in the loosest sense possible, not being null or an empty string.
This does allow for the library to be used for almost anything, where team names can't be known in advance.
But for more official use-cases they could also be pre-set enums of known teams around the world,
which lowers the risk of misspelling free-text strings and similar issues.

#### Summary returned as List of MatchSummary objects

Since the requirements state that the solution is supposed to be a simple library but the summary is a bit ambiguously
presented in the task,
I decided that the most likely expectations is to simply return a List of Java objects.
I.e. it's not entirely clear in what format the summary should be delivered, only which sort order.
This allows for the library to both be copied into an existing Java project (with possible modifications depending on
Java-language compatibility) and used directly,
or be used in a REST API where there is typically a Java to JSON serialization in place.

#### Placeholder exceptions

The task doesn't specify any specific error handling, so I opted to just throwing exceptions with messages.

This implementation relies on using IllegalArgumentException with a custom message.
A nice update would be to add custom exceptions such as ScoreBoardException, MatchException and TeamException,
based on the same or RuntimeException.
Then the tests could focus more on types rather than the specific messages.
An alternative could be to create enum error codes and document those, depending on the usec-case.

#### README.md with Markdown formatting

Seems fairly straight forward but might be worth mentioning.
The requirements only state that the file should be included and provide good documentation,
but formatting it with Markdown felt appropriate since it's a de facto industry standard.

#### More verbose commits than usual

As per the instructions I've tried to document my changes extensively with Git.
Normally most of this would be present in a PR/MR description or code-comments instead.
I went out of my way to clarify my intentions directly in Git here.
My commit messages are usually fairly concise and I like to squash them all into a feature commit as the PR/MR is
merged, but until then I figure that they might provide some value for me or the reviewer.

#### Regarding production ready code and scalability

The recruiter presenting this task helpfully pointed out that considering things like thread safety is appreciated,
and certainly seems relevant in the systems Sportradar develops and maintains.

However, the task is also clear that it's supposed to be a simple library, so I'm hesitant to add complexities like
that, especially without clearer technical requirements for it.
I did however read about it a bit and besides already using final for immutability, I would consider switching to a
"CopyOnWriteArrayList" or similar for better thread safety as well as using an Atomic integer for the match counter.
Both from suggestions by ChatGPT that seemed relevant for this use-case.

My implementation is focused on a simple implementation to "my standards", as also instructed.

### Approach

#### Day 1 (~6h)

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

#### Day 2 (~3h)

Second day I spent a couple of hours on the summary.
The first version having working sorting with a corresponding minimal test with both same and different total scores.
In the second iteration I changed from Map to List as data-structure for the current matches to simplify sorting syntax.

Later re-read the task description a second time after all the main features were implemented.
I noticed the expectation of documented assumptions and added those to the README.
I also realized that the summary data structure wasn't really designed for consumption and added the MatchSummary record
to provide a more concise, flat and more relevant model.