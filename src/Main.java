/*
 * Skeleton Program code for the AQA A Level Paper 1 2019 examination
 * this code should be used in conjunction with the Preliminary Material
 * written by the AQA Programmer Team
 * developed using NetBeans IDE 8.1
 */


import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.*;

public class Main {

    static final int INVENTORY = 1001;
    static final int MINIMUM_ID_FOR_ITEM = 2001;
    static final int ID_DIFFERENCE_FOR_OBJECT_IN_TWO_LOCATIONS = 10000;

    class Place {
        String description;
        int id, north, east, south, west, up, down;
    }

    class Character {
        String name, description;
        int id, currentLocation;
    }

    class Item {
        int id, location;
        String description, status, name, commands, results;
    }

    String getInstruction() {
        String instruction;
        Console.write("\n> ");
        instruction = Console.readLine().toLowerCase();
        return instruction;
    }

    String[] extractCommand(String instruction) {
        String command = "";
        if (!instruction.contains(" ")) {
            command = instruction;
            return new String[]{command, instruction};
        }
        while (instruction.length() > 0 && instruction.charAt(0) != ' ') {
            command += instruction.charAt(0);
            instruction = instruction.substring(1);
        }
        while (instruction.length() > 0 && instruction.charAt(0) == ' ') {
            instruction = instruction.substring(1);
        }
        return new String[]{command, instruction};
    }

    boolean go(Character you, String direction, Place currentPlace) {
        boolean moved = true;
        switch (direction)
        {
            case "north":
                if (currentPlace.north == 0) {
                    moved = false;
                } else {
                    you.currentLocation = currentPlace.north;
                }
                break;
            case "east":
                if (currentPlace.east == 0) {
                    moved = false;
                } else {
                    you.currentLocation = currentPlace.east;
                }
                break;
            case "south":
                if (currentPlace.south == 0) {
                    moved = false;
                } else {
                    you.currentLocation = currentPlace.south;
                }
                break;
            case "west":
                if (currentPlace.west == 0) {
                    moved = false;
                } else {
                    you.currentLocation = currentPlace.west;
                }
                break;
            case "up":
                if (currentPlace.up == 0) {
                    moved = false;
                } else {
                    you.currentLocation = currentPlace.up;
                }
                break;
            case "down":
                if (currentPlace.down == 0) {
                    moved = false;
                } else {
                    you.currentLocation = currentPlace.down;
                }
                break;
            default:
                moved = false;
        }
        if (!moved) {
            Console.writeLine("You are not able to go in that direction.");
        }
        return moved;
    }

    void displayDoorStatus(String status) {
        if (status.equals("open")) {
            Console.writeLine("The door is open.");
        } else {
            Console.writeLine("The door is closed.");
        }
    }

    void displayContentsOfContainerItem(ArrayList<Item> items, int containerID) {
        Console.write("It contains: ");
        boolean containsItem = false;
        for (Item thing : items) {
            if (thing.location == containerID) {
                if (containsItem) {
                    Console.write(", ");
                }
                containsItem = true;
                Console.write(thing.name);
            }
        }
        if (containsItem) {
            Console.writeLine(".");
        } else {
            Console.writeLine("nothing.");
        }
    }

    void examine(ArrayList<Item> items, ArrayList<Character> characters, String itemToExamine, int currentLocation) {
        int count = 0;
        if (itemToExamine.equals("inventory")) {
            displayInventory(items);
        } else {
            int indexOfItem = getIndexOfItem(itemToExamine, -1, items);
            if (indexOfItem != -1) {
                if (items.get(indexOfItem).name.equals(itemToExamine) && (items.get(indexOfItem).location == INVENTORY || items.get(indexOfItem).location == currentLocation)) {
                    Console.writeLine(items.get(indexOfItem).description);
                    if (items.get(indexOfItem).name.contains("door")) {
                        displayDoorStatus(items.get(indexOfItem).status);
                    }
                    if(items.get(indexOfItem).status.contains("container")) {
                        displayContentsOfContainerItem(items, items.get(indexOfItem).id);
                    }
                    return;
                }
            }
            while (count < characters.size()) {
                if (characters.get(count).name.equals(itemToExamine) && characters.get(count).currentLocation == currentLocation) {
                    Console.writeLine(characters.get(count).description);
                    return;
                }
                count++;
            }
            Console.writeLine("You cannot find " + itemToExamine + " to look at.");
        }
    }

    int getPositionOfCommand(String commandList, String command) {
        int position = 0, count = 0;
        while (count <= commandList.length() - command.length()) {
            String test = commandList.substring(count, count+command.length());
            if (test.equals(command)) {
                return position;
            } else if (commandList.charAt(count) == ',') {
                position++;
            }
            count++;
        }
        return position;
    }

    String getResultForCommand(String results, int postion) {
        int count = 0, currentPostion = 0;
        String resultForCommand = "";
        while (currentPostion < postion && count < results.length()) {
            if (results.charAt(count) == ';') {
                currentPostion++;
            }
            count++;
        }
        while (count < results.length()) {
            if (results.charAt(count) == ';') {
                break;
            }
            resultForCommand += results.charAt(count);
            count++;
        }
        return resultForCommand;
    }

    void say(String speech) {
        Console.writeLine();
        Console.writeLine(speech);
        Console.writeLine();
    }

    String[] extractResultForCommand(String subCommand, String subCommandParameter, String resultForCommand) {
        int count = 0;
        while (count < resultForCommand.length() && resultForCommand.charAt(count) != ',') {
            subCommand += resultForCommand.charAt(count);
            count++;
        }
        count++;
        while (count < resultForCommand.length()) {
            if (resultForCommand.charAt(count) != ',' && resultForCommand.charAt(count) != ';') {
                subCommandParameter += resultForCommand.charAt(count);
            } else {
                return new String[]{subCommand, subCommandParameter};
            }
            count++;
        }
        return new String[]{subCommand, subCommandParameter};
    }

    void changeLocationReference(String direction, int newLocationReference, ArrayList<Place> places, int indexOfCurrentLocation, boolean opposite) {
        Place thisPlace;
        thisPlace = places.get(indexOfCurrentLocation);
        if (direction.equals("north") && !opposite || direction.equals("south") && opposite) {
            thisPlace.north = newLocationReference;
        } else if (direction.equals("east") && !opposite || direction.equals("west") && opposite) {
            thisPlace.east = newLocationReference;
        } else if (direction.equals("south") && !opposite || direction.equals("north") && opposite) {
            thisPlace.south = newLocationReference;
        } else if (direction.equals("west") && !opposite || direction.equals("east") && opposite) {
            thisPlace.west = newLocationReference;
        } else if (direction.equals("up") && !opposite || direction.equals("down") && opposite) {
            thisPlace.up = newLocationReference;
        } else if (direction.equals("down") && !opposite || direction.equals("up") && opposite) {
            thisPlace.down = newLocationReference;
        }
        places.set(indexOfCurrentLocation, thisPlace);
    }

    int openClose(boolean open, ArrayList<Item> items, ArrayList<Place> places, String itemToOpenClose, int currentLocation) {
        String command, resultForCommand;
        int count = 0, postion, count2;
        String direction = "", directionChange = "";
        boolean actionWorked = false;
        if (open) {
            command = "open";
        } else {
            command = "close";
        }
        while (count < items.size() && actionWorked == false) {
            if (items.get(count).name.equals(itemToOpenClose)) {
                if (items.get(count).location == currentLocation) {
                    if (items.get(count).commands.length() >= 4) {
                        if (items.get(count).commands.contains(command)) {
                            if (items.get(count).status.equals(command)) {
                                return -2;
                            } else if (items.get(count).status.equals("locked")) {
                                return -3;
                            }
                            postion = getPositionOfCommand(items.get(count).commands, command);
                            resultForCommand = getResultForCommand(items.get(count).results, postion);
                            String[] returnArray = extractResultForCommand(direction, directionChange, resultForCommand);
                            direction = returnArray[0];
                            directionChange = returnArray[1];
                            changeStatusOfItem(items, count, command);
                            count2 = 0;
                            actionWorked = true;
                            while (count2 < places.size()) {
                                if (places.get(count2).id == currentLocation) {
                                    changeLocationReference(direction, Integer.parseInt(directionChange), places, count2, false);
                                } else if (places.get(count2).id == Integer.parseInt(directionChange)) {
                                    changeLocationReference(direction, currentLocation, places, count2, true);
                                }
                                count2++;
                            }
                            int indexOfOtherSideOfDoor;
                            if (items.get(count).id > ID_DIFFERENCE_FOR_OBJECT_IN_TWO_LOCATIONS) {
                                indexOfOtherSideOfDoor = getIndexOfItem("", items.get(count).id - ID_DIFFERENCE_FOR_OBJECT_IN_TWO_LOCATIONS, items);
                            } else {
                                indexOfOtherSideOfDoor = getIndexOfItem("", items.get(count).id + ID_DIFFERENCE_FOR_OBJECT_IN_TWO_LOCATIONS, items);
                            }
                            changeStatusOfItem(items, indexOfOtherSideOfDoor, command);
                            count = items.size() + 1;
                        }
                    }
                }
            }
            count++;
        }
        if (!actionWorked) {
            return -1;
        }
        return Integer.parseInt(directionChange);
    }

    int getIndexOfItem(String itemNameToGet, int itemIDToGet, ArrayList<Item> items) {
        int count = 0;
        boolean stopLoop = false;
        while (!stopLoop && count < items.size()) {
            if (itemIDToGet == -1 && items.get(count).name.equals(itemNameToGet) || items.get(count).id == itemIDToGet) {
                stopLoop = true;
            } else {
                count++;
            }
        }
        if (!stopLoop) {
            return  -1;
        } else {
            return count;
        }
    }

    void changeLocationOfItem(ArrayList<Item> items, int indexOfItem, int newLocation) {
        Item thisItem = items.get(indexOfItem);
        thisItem.location = newLocation;
        items.set(indexOfItem, thisItem);
    }

    void changeStatusOfItem(ArrayList<Item> items, int indexOfItem, String newStatus) {
        Item thisItem = items.get(indexOfItem);
        thisItem.status = newStatus;
        items.set(indexOfItem, thisItem);
    }

    int getRandomNumber(int lowerLimitValue, int upperLimitValue) {
        Random rnd = new Random();
        return rnd.nextInt(upperLimitValue - lowerLimitValue + 1) + lowerLimitValue;
    }

    int rollDie(String lower, String upper) {
        int lowerLimitValue = 0;
        if (isNumeric(lower)){
            lowerLimitValue = Integer.parseInt(lower);
        } else {
            while (lowerLimitValue < 1 || lowerLimitValue > 6) {
                Console.write("Enter minimum: ");
                lowerLimitValue = Integer.parseInt(Console.readLine());
            }
        }
        int upperLimitValue = 0;
        if (isNumeric(upper)) {
            upperLimitValue = Integer.parseInt(upper);
        } else {
            while (upperLimitValue < lowerLimitValue || upperLimitValue > 6) {
                Console.write("Enter maximum: ");
                upperLimitValue = Integer.parseInt(Console.readLine());
            }
        }
        return getRandomNumber(lowerLimitValue, upperLimitValue);
    }

    boolean isNumeric(String inputData) {
        try {
            Integer.parseInt(inputData);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    void changeStatusOfDoor(ArrayList<Item> items, int currentLocation, int indexOfItemToLockUnlock, int indexOfOtherSideItemToLockUnlock) {
        if (currentLocation == items.get(indexOfItemToLockUnlock).location || currentLocation == items.get(indexOfOtherSideItemToLockUnlock).location) {
            if (items.get(indexOfItemToLockUnlock).status.equals("locked")) {
                changeStatusOfItem(items, indexOfItemToLockUnlock, "close");
                changeStatusOfItem(items, indexOfOtherSideItemToLockUnlock, "close");
                say(items.get(indexOfItemToLockUnlock).name + " now unlocked.");
            } else if (items.get(indexOfItemToLockUnlock).status.equals("close")) {
                changeStatusOfItem(items, indexOfItemToLockUnlock, "locked");
                changeStatusOfItem(items, indexOfOtherSideItemToLockUnlock, "locked");
                say(items.get(indexOfItemToLockUnlock).name + " now locked.");
            } else {
                say(items.get(indexOfItemToLockUnlock).name + " is open so can't be locked.");
            }
        } else {
            say("Can't use that key in this location.");
        }
    }

    void useItem(ArrayList<Item> items, String itemToUse, int currentLocation, ArrayList<Place> places) {
        boolean stopGame = false;
        int position, indexOfItem;
        String resultForCommand, subCommand = "", subCommandParameter = "";
        indexOfItem = getIndexOfItem(itemToUse, -1, items);
        if (indexOfItem != -1) {
            if (items.get(indexOfItem).location == INVENTORY || (items.get(indexOfItem).location == currentLocation && items.get(indexOfItem).status.contains("usable"))) {
                position = getPositionOfCommand(items.get(indexOfItem).commands, "use");
                resultForCommand = getResultForCommand(items.get(indexOfItem).results, position);
                String[] returnArray = extractResultForCommand(subCommand, subCommandParameter, resultForCommand);
                subCommand = returnArray[0];
                subCommandParameter = returnArray[1];
                if (subCommand.equals("say")) {
                    say(subCommandParameter);
                } else if (subCommand.equals("lockunlock")) {
                    int indexOfItemToLockUnlock, indexOfOtherSideItemToLockUnlock;
                    indexOfItemToLockUnlock = getIndexOfItem("", Integer.parseInt(subCommandParameter), items);
                    indexOfOtherSideItemToLockUnlock = getIndexOfItem("", Integer.parseInt(subCommandParameter) + ID_DIFFERENCE_FOR_OBJECT_IN_TWO_LOCATIONS, items);
                    changeStatusOfDoor(items, currentLocation, indexOfItemToLockUnlock, indexOfOtherSideItemToLockUnlock);
                } else if (subCommand.equals("roll")) {
                    say("You have rolled a " + rollDie(resultForCommand.substring(5, 6), resultForCommand.substring(7, 8)));
                }
                return;
            }
        }
        Console.writeLine("You can't use that!");
    }

    void readItem(ArrayList<Item> items, String itemToRead, int currentLocation) {
        String subCommand = "", subCommandParameter = "", resultForCommand;
        int indexOfItem, position;
        indexOfItem = getIndexOfItem(itemToRead, -1, items);
        if (indexOfItem == -1) {
            Console.writeLine("You can't find " + itemToRead + ".");
        } else if (! items.get(indexOfItem).commands.contains("read")) {
            Console.writeLine("You can't read " + itemToRead + ".");
        } else if (items.get(indexOfItem).location != currentLocation && items.get(indexOfItem).location != INVENTORY) {
            Console.writeLine("You can't find " + itemToRead + ".");
        } else {
            position = getPositionOfCommand(items.get(indexOfItem).commands, "read");
            resultForCommand = getResultForCommand(items.get(indexOfItem).results, position);
            String[] returnArray = extractResultForCommand(subCommand, subCommandParameter, resultForCommand);
            subCommand = returnArray[0];
            subCommandParameter = returnArray[1];
            if (subCommand.equals("say")) {
                say(subCommandParameter);
            }
        }
    }

    boolean getItem(ArrayList<Item> items, String itemToGet, int currentLocation) {
        boolean stopGame = false, canGet = false;
        String resultForCommand, subCommand = "", subCommandParameter = "";
        int indexOfItem, position;
        indexOfItem = getIndexOfItem(itemToGet, -1, items);
        if (indexOfItem == -1) {
            Console.writeLine("You can't find " + itemToGet + ".");
        } else if (items.get(indexOfItem).location == INVENTORY) {
            Console.writeLine("You have already got that!");
        } else if (!items.get(indexOfItem).commands.contains("get")) {
            Console.writeLine("You can't get " + itemToGet + ".");
        } else if (items.get(indexOfItem).location >= MINIMUM_ID_FOR_ITEM && items.get(getIndexOfItem("", items.get(indexOfItem).location, items)).location != currentLocation) {
            Console.writeLine("You can't find " + itemToGet + ".");
        } else if (items.get(indexOfItem).location < MINIMUM_ID_FOR_ITEM && items.get(indexOfItem).location != currentLocation) {
            Console.writeLine("You can't find " + itemToGet + ".");
        } else {
            canGet = true;
        }
        if (canGet) {
            position = getPositionOfCommand(items.get(indexOfItem).commands, "get");
            resultForCommand = getResultForCommand(items.get(indexOfItem).results, position);
            String[] returnArray = extractResultForCommand(subCommand, subCommandParameter, resultForCommand);
            subCommand = returnArray[0];
            subCommandParameter = returnArray[1];
            if (subCommand.equals("say")) {
                say(subCommandParameter);
            } else if (subCommand.equals("win")) {
                say("You have won the game");
                stopGame = true;
                return stopGame;
            }
            if (items.get(indexOfItem).status.contains("gettable")) {
                changeLocationOfItem(items, indexOfItem, INVENTORY);
                Console.writeLine("You have got that now.");
            }
        }
        return stopGame;
    }

    int[] checkIfDiceGamePossible(ArrayList<Item> items, ArrayList<Character> characters, int indexOfPlayerDie, int indexOfOtherCharacter, int indexOfOtherCharacterDie, String otherCharacterName) {
        boolean playerHasDie = false, playersInSameRoom = false, otherCharacterHasDie = false;
        for (Item thing : items) {
            if (thing.location == INVENTORY && thing.name.contains("die")) {
                playerHasDie = true;
                indexOfPlayerDie = getIndexOfItem("", thing.id, items);
            }
        }
        int count = 1;
        while (count < characters.size() && !playersInSameRoom) {
            if (characters.get(0).currentLocation == characters.get(count).currentLocation && characters.get(count).name.equals(otherCharacterName)) {
                playersInSameRoom = true;
                for (Item thing : items) {
                    if (thing.location == characters.get(count).id && thing.name.contains("die")) {
                        otherCharacterHasDie = true;
                        indexOfOtherCharacterDie = getIndexOfItem("", thing.id, items);
                        indexOfOtherCharacter = count;
                    }
                }
            }
            count++;
        }
        int diceGamePossible = 0;
        if (playerHasDie && playersInSameRoom && otherCharacterHasDie) {
            diceGamePossible = 1;
        }
        return new int[]{diceGamePossible, indexOfPlayerDie, indexOfOtherCharacter, indexOfOtherCharacterDie};
    }

    void takeItemFromOtherCharacter(ArrayList<Item> items, int otherCharacterID) {
        ArrayList<Integer> listOfIndicesOfItemsInInventory = new ArrayList<>();
        ArrayList<String> listOfNamesOfItemsInInventory = new ArrayList<>();
        int count = 0;
        while (count < items.size()) {
            if (items.get(count).location == otherCharacterID) {
                listOfIndicesOfItemsInInventory.add(count);
                listOfNamesOfItemsInInventory.add(items.get(count).name);
            }
            count++;
        }
        count = 1;
        Console.write("Which item do you want to take?  They have: ");
        Console.write(listOfNamesOfItemsInInventory.get(0));
        while (count < listOfNamesOfItemsInInventory.size() - 1) {
            Console.write(", " + listOfNamesOfItemsInInventory.get(count));
            count++;
        }
        Console.writeLine(".");
        String chosenItem = Console.readLine();
        if (listOfNamesOfItemsInInventory.contains(chosenItem)) {
            Console.writeLine("You have that now.");
            int pos = listOfNamesOfItemsInInventory.indexOf(chosenItem);
            changeLocationOfItem(items, listOfIndicesOfItemsInInventory.get(pos), INVENTORY);
        } else {
            Console.writeLine("They don't have that item, so you don't take anything this time.");
        }
    }

    void takeRandomItemFromPlayer(ArrayList<Item> items, int otherCharacterID) {
        ArrayList<Integer> listofIndicesOfItemsInInventory = new ArrayList<>();
        int count = 0;
        while (count < items.size()) {
            if (items.get(count).location == INVENTORY) {
                listofIndicesOfItemsInInventory.add(count);
            }
            count++;
        }
        int rno = getRandomNumber(0, listofIndicesOfItemsInInventory.size() - 1);
        Console.writeLine("They have taken your " + items.get(listofIndicesOfItemsInInventory.get(rno)).name + ".");
        changeLocationOfItem(items, listofIndicesOfItemsInInventory.get(rno), otherCharacterID);
    }

    void playDiceGame(ArrayList<Character> characters, ArrayList<Item> items, String otherCharacterName) {
        int playerScore = 0, otherCharacterScore = 0, indexOfOtherCharacter = 0, indexOfOtherCharacterDie = 0, indexOfPlayerDie = 0, position;
        boolean diceGamePossible = false;
        String resultForCommand;
        int[] returnArray = checkIfDiceGamePossible(items, characters, indexOfPlayerDie, indexOfOtherCharacter, indexOfOtherCharacterDie, otherCharacterName);
        if (returnArray[0] == 1) {
            diceGamePossible = true;
        }
        indexOfPlayerDie = returnArray[1];
        indexOfOtherCharacter = returnArray[2];
        indexOfOtherCharacterDie = returnArray[3];
        if (!diceGamePossible) {
            Console.writeLine("You can't play a dice game.");
        } else {
            position = getPositionOfCommand(items.get(indexOfPlayerDie).commands, "use");
            resultForCommand = getResultForCommand(items.get(indexOfPlayerDie).results, position);
            playerScore = rollDie(resultForCommand.substring(5, 6), resultForCommand.substring(7, 8));
            Console.writeLine("You rolled a " + playerScore + ".");
            position = getPositionOfCommand(items.get(indexOfOtherCharacterDie).commands, "use");
            resultForCommand = getResultForCommand(items.get(indexOfOtherCharacterDie).results, position);
            otherCharacterScore = rollDie(resultForCommand.substring(5, 6), resultForCommand.substring(7, 8));
            Console.writeLine("They rolled a " + otherCharacterScore + ".");
            if (playerScore > otherCharacterScore) {
                Console.writeLine("You win!");
                takeItemFromOtherCharacter(items, characters.get(indexOfOtherCharacter).id);
            } else if (playerScore < otherCharacterScore) {
                Console.writeLine("You lose!");
                takeRandomItemFromPlayer(items, characters.get(indexOfOtherCharacter).id);
            } else {
                Console.writeLine("Draw!");
            }
        }
    }

    void moveItem(ArrayList<Item> items, String itemToMove, int currentLocation) {
        int position;
        String resultForCommand, subCommand  = "", subCommandParameter = "";
        int indexOfItem  = getIndexOfItem(itemToMove, -1, items);
        if (indexOfItem != -1) {
            if (items.get(indexOfItem).location == currentLocation) {
                if (items.get(indexOfItem).commands.length() >= 4) {
                    if (items.get(indexOfItem).commands.contains("move")) {
                        position = getPositionOfCommand(items.get(indexOfItem).commands, "move");
                        resultForCommand = getResultForCommand(items.get(indexOfItem).results, position);
                        String[] returnArray = extractResultForCommand(subCommand, subCommandParameter, resultForCommand);
                        subCommand = returnArray[0];
                        subCommandParameter = returnArray[1];
                        if (subCommand.equals("say")) {
                            say(subCommandParameter);
                        }
                    } else {
                        Console.writeLine("You can't move " + itemToMove + ".");
                    }
                } else {
                    Console.writeLine("You can't move " + itemToMove + ".");
                }
                return;
            }
        }
        Console.writeLine("You can't find " + itemToMove + ".");
    }

    void displayInventory(ArrayList<Item> items) {
        Console.writeLine();
        Console.writeLine("You are currently carrying the following items:");
        for (Item thing : items) {
            if (thing.location == INVENTORY) {
                Console.writeLine(thing.name);
            }
        }
        Console.writeLine();
    }

    void displayGettableItemsInLocation(ArrayList<Item> items, int currentLocation) {
        boolean containsGettableItems = false;
        String listOfItems = "On the floor there is: ";
        for (Item thing : items) {
            if (thing.location == currentLocation && thing.status.contains("gettable")) {
                if (containsGettableItems) {
                    listOfItems += ", ";
                }
                listOfItems += thing.name;
                containsGettableItems = true;
            }
        }
        if (containsGettableItems) {
            Console.writeLine(listOfItems + ".");
        }
    }

    void displayOpenCloseMessage(int resultOfOpenClose, boolean openCommand) {
        if (resultOfOpenClose >= 0) {
            if (openCommand) {
                say("You have opened it.");
            } else {
                say("You have closed it.");
            }
        } else if (resultOfOpenClose == -3) {
            say("You can't do that, it is locked.");
        } else if (resultOfOpenClose == -2) {
            say("It already is.");
        } else if (resultOfOpenClose == -1) {
            say("You can't open that.");
        }
    }

    void playGame(ArrayList<Character> characters, ArrayList<Item> items, ArrayList<Place> places) {
        boolean stopGame = false, moved = true;
        String instruction, command;
        int resultOfOpenClose;
        while (!stopGame) {
            if (moved) {
                Console.writeLine();
                Console.writeLine();
                Console.writeLine(places.get(characters.get(0).currentLocation - 1).description);
                displayGettableItemsInLocation(items, characters.get(0).currentLocation);
                moved = false;
            }
            instruction = getInstruction();
            String[] returnStrings = extractCommand(instruction);
            command = returnStrings[0];
            instruction = returnStrings[1];
            switch (command)
            {
                case "get":
                    stopGame = getItem(items, instruction, characters.get(0).currentLocation);
                    break;
                case "use":
                    useItem(items, instruction, characters.get(0).currentLocation, places);
                    break;
                case "go":
                    moved = go(characters.get(0), instruction, places.get(characters.get(0).currentLocation - 1));
                    break;
                case "read":
                    readItem(items, instruction, characters.get(0).currentLocation);
                    break;
                case "examine":
                    examine(items, characters, instruction, characters.get(0).currentLocation);
                    break;
                case "open":
                    resultOfOpenClose = openClose(true, items, places, instruction, characters.get(0).currentLocation);
                    displayOpenCloseMessage(resultOfOpenClose, true);
                    break;
                case "close":
                    resultOfOpenClose = openClose(false, items, places, instruction, characters.get(0).currentLocation);
                    displayOpenCloseMessage(resultOfOpenClose, false);
                    break;
                case "move":
                    moveItem(items, instruction, characters.get(0).currentLocation);
                    break;
                case "say":
                    say(instruction);
                    break;
                case "playdice":
                    playDiceGame(characters, items, instruction);
                    break;
                case "quit":
                    say("You decide to give up, try again another time.");
                    stopGame = true;
                    break;
                default:
                    Console.writeLine("Sorry, you don't know how to " + command + ".");
            }
        }
        Console.readLine();
    }

    boolean loadGame(String filename, ArrayList<Character> characters, ArrayList<Item> items, ArrayList<Place> places) {
        int noOfCharacters, noOfPlaces, count, noOfItems;
        Character tempCharacter;
        Place tempPlace;
        Item tempItem;
        try {
            FileInputStream binaryReader = new FileInputStream(filename);
            DataInputStream  reader = new DataInputStream (binaryReader);
            noOfCharacters = reader.readInt();
            for (count = 1; count <= noOfCharacters ; count++) {
                tempCharacter = new Character();
                tempCharacter.id = reader.readInt();
                tempCharacter.name = reader.readUTF();
                tempCharacter.description = reader.readUTF();
                tempCharacter.currentLocation = reader.readInt();
                characters.add(tempCharacter);
            }
            noOfPlaces = reader.readInt();
            for (count = 1; count <= noOfPlaces ; count++) {
                tempPlace = new Place();
                tempPlace.id = reader.readInt();
                tempPlace.description = reader.readUTF();
                tempPlace.north = reader.readInt();
                tempPlace.east = reader.readInt();
                tempPlace.south = reader.readInt();
                tempPlace.west = reader.readInt();
                tempPlace.up = reader.readInt();
                tempPlace.down = reader.readInt();
                places.add(tempPlace);
            }
            noOfItems = reader.readInt();
            for (count = 1; count <= noOfItems ; count++) {
                tempItem = new Item();
                tempItem.id = reader.readInt();
                tempItem.description = reader.readUTF();
                tempItem.status = reader.readUTF();
                tempItem.location = reader.readInt();
                tempItem.name = reader.readUTF();
                tempItem.commands = reader.readUTF();
                tempItem.results = reader.readUTF();
                items.add(tempItem);
            }
            return  true;
        } catch (Exception e) {
            return  false;
        }
    }

    public Main() {
        String filename;
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<Character> characters = new ArrayList<>();
        ArrayList<Place> places = new ArrayList<>();
        Console.write("Enter filename> ");
        filename = Console.readLine() + ".gme";
        Console.writeLine();
        if (loadGame(filename, characters, items, places)) {
            playGame(characters, items, places);
        } else {
            Console.writeLine("Unable to load game.");
            Console.readLine();
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}