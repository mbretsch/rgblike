RGBlike was programmed during 7DRL 2017. It is a simple Rougelike with a unique game mechanic. The main goal is to keep your 3 colors balanced and use them to get enemies out of balance. Once one of your colors reaches 0, you have lost.
The game uses Java + Squidlib, is tested in Windows, but should work in Linux.

Attacks are possible on every enemy in your FOV (use <A> to attack). Select color and strength to attack with.

After an enemy died, he drops an item. If you walk into the item you can pick it up or pass. Items currently equiped will be destroyed. If you pass, the item is destroyed aswell.

The goal is to find and kill the boss. After his death a downward stair will appear on the map to get you to the next stage.

use 'mvn package' to create an executable with dependencies or 'mvn package exec:java' to start the game directly