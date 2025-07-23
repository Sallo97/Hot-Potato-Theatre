# Hot Potato Theatre
Hot Potato Theatre is a program for building, analysing and executing Simple Hot Potato Games. It has been developed as 
the final project for the exam *Algorithmic Game Theory* held at University of Pisa.
The project is based on the Simple Hot Potato Games proposed in the paper *Rational and Empirical Play in the Simple Hot Potato Game*
by *Carter T. Butts and David C. Rode*.

## Features
- Extends the concept of hot potato seen in the paper, indicated within the application as a *Fixed* hot potato, by defining the *Mutable* hot potato, that is an object whose *gain* and *loss* changes over time by some associated factors.
- An assortment of players inspired by the empirical hypothesis proposed in the paper, whose acceptance-logic has been extended to support also *Mutable* potatoes.
- Support the building and execution of Simple Hot Potato Games, distinguishing between *Homogenous*, that is where players are all the same type, or *Mixed*, where they could be of different type.
- An algorithm that defined a gain, tries to find a longest possible chain, that is a sequence of players from the starting set of players such that, if the game proposes the hot potato to them in the given
  order, each one will accept it, resulting in the maximum possible chain length in the game.
- A command-line UI to let user play with the offered features.

## Supported Operating Systems
At the time of writing the application has been tested on a MacBook Pro M2 with macOS Sequoia 15.5 (ARM64).

## Building and Execution
It's possible to compile and execute the application either within the IDE *IntelliJ IDEA* or manually using `gradlew`.

### Building and execution with IntelliJ IDEA

Clone the following repository and then open it with **IntelliJ IDEA**. To build the project either press `Cmd + F9` on macOS, or click on `Build` -> `Build Project`. After the compilation, **Simple Hot Potato Theatre** can be executed by selecting `Main` in the configuration panel on the top-left of the IDE and then by pressing the play button to its right.

### Building and executing manually with gradlew
It is possible to compile the process manually within a terminal using `gradlew`. Clone the following repository and open the terminal inside the generated folder, then execute:

```bash
./gradlew build      // Building the project
./gradlew run   // Running the application
```
