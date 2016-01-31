# DiceRoller
Simulates rolling of dice.
The user enters a command into the program to simulate a roll of that kind.  
A roll is declared in the notation XdY to roll X dice where each die has Y sides. The value of all the dice are summed up and
the result is returned to the user. The value of each individual die is displayed to the user.  
For example, rolling two twenty-sided die would be ``2d20``. Each command returns an output consisting of the initial expression, the expression expanded to display the result of each individual die, and then result of the expression.
for example ``2d20 : [4, 10] = 14``

The program is able to handle longer expressions as input, where one roll is added or subtracted to another. One can also 
use static values to either increase or decrease the expression by a set amount.  
For example: ``4d12-1d10+3 : [9, 4, 8, 8] - [2] + 3 = 30``


###Dropping high and low values
A roll using more than one die can be instructed to ignore up to all but one die, starting with either the lower or higher
values. This is done by adding a suffix to the roll, ``dH`` to drop high, or ``dL`` to drop low, followed by the number of dice to
ignore. It goes without saying that the number of dropped dice must be less than the number of dice rolled.  
For example, rolling two twenty-sided die and dropping the lower one is done by giving the command ``2d20dL1``.  
All the dice that are dropped from a roll are marked with an asterisk in the output: ``2d20dL1 : [5*, 11] = 11``
