# Project 3 Prep

**What distinguishes a hallway from a room? How are they similar?**

Answer: A hallway has a constant width of 1 tile with a random length. Whereas a room can have a random width and length. Both have random length.

**Can you think of an analogy between the process of 
drawing a knight world and randomly generating a world 
using rooms and hallways?**

Answer: Just how I used conditionals to determine which areas should be empty to create the knight world, I can use similar spatial relations to determine how far apart certain rooms and hallways should be.

**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab and the process used to eventually 
get to the full knight world.**

Answer: I would write the constructor method that initializes the random seed for the world generation. I would also create a method that determines which tile type to use based its spatial distance to other items.

**This question is open-ended. Write some classes 
and methods that might be useful for Project 3. Think 
about what helper methods and classes you used for the lab!**

Answer: World(), createHallway(), Room(), createArea(), getItemType(tile), getDistancetoPlayer() 
