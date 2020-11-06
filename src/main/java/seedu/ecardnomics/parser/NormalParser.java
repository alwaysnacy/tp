package seedu.ecardnomics.parser;

import org.apache.commons.math3.analysis.function.Log;
import seedu.ecardnomics.Ui;
import seedu.ecardnomics.command.Command;
import seedu.ecardnomics.command.ExitCommand;
import seedu.ecardnomics.command.VersionCommand;
import seedu.ecardnomics.command.VoidCommand;
import seedu.ecardnomics.command.normal.CreateCommand;
import seedu.ecardnomics.command.normal.DecksCommand;
import seedu.ecardnomics.command.normal.DeleteDeckCommand;
import seedu.ecardnomics.command.normal.EditCommand;
import seedu.ecardnomics.command.normal.HelpCommand;
import seedu.ecardnomics.command.normal.PowerPointCommand;
import seedu.ecardnomics.command.normal.StartCommand;
import seedu.ecardnomics.command.normal.TagCommand;
import seedu.ecardnomics.command.normal.UntagCommand;
import seedu.ecardnomics.command.normal.SearchCommand;
import seedu.ecardnomics.deck.Deck;
import seedu.ecardnomics.deck.DeckList;
import seedu.ecardnomics.exceptions.DeckRangeException;
import seedu.ecardnomics.exceptions.EmptyInputException;
import seedu.ecardnomics.exceptions.IndexFormatException;
import seedu.ecardnomics.exceptions.NoSeparatorException;
import seedu.ecardnomics.exceptions.NumberTooBigException;
import seedu.ecardnomics.storage.LogStorage;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Parser for commands supplied in Normal Mode.
 */
public class NormalParser extends Parser {
    DeckList deckList;
    private static LogStorage logger = new LogStorage("NormalParserLogger");

    /** Constructor. */
    public NormalParser(DeckList deckList) {
        this.deckList = deckList;
    }

    @Override
    protected int getIndex(String arguments) throws IndexFormatException,
            DeckRangeException, NumberTooBigException {

        arguments = arguments.trim();

        logger.log(Level.INFO, "Logging method getIndex() in NormalParser.");

        if (!arguments.matches(Ui.DIGITS_REGEX)) {
            logger.log(Level.WARNING, "User did not enter a valid integer index. string = " + arguments);
            throw new IndexFormatException();
        }

        int index;
        try {
            index = Integer.parseInt(arguments) - INDEX_OFFSET;
        } catch (NumberFormatException e) {
            throw new NumberTooBigException();
        }

        if ((index >= deckList.size()) || (index < LOWEST_POSSIBLE_INDEX)) {
            logger.log(Level.WARNING, "User did not enter an index in the valid range.");
            throw new DeckRangeException();
        }

        return index;
    }

    /**
     * Retrieves deck at index specified in arguments from deckList.
     * getIndex() is used to convert arguments from String to an int index.
     *
     * @param arguments String that contains the ID number of the deck requested
     * @return Reference to requested deck
     * @throws IndexFormatException if arguments is not a digit
     * @throws DeckRangeException if index obtained from arguments is not in range
     */
    private Deck prepareDeck(String arguments) throws Exception {
        return deckList.getDeck(getIndex(arguments));
    }

    /**
     * Prepares an instance of DeleteDeckCommand from given arguments.
     *
     * @param arguments String that contains the user arguments for the delete command
     * @return DeleteDeckCommand that can be executed to delete the deck.
     * @throws Exception if index is invalid or no index is supplied.
     */
    private Command prepareDeleteDeck(String arguments) throws Exception {
        boolean isDeckDeleted;
        int deckID;
        if (arguments.contains("-y")) {
            arguments = arguments.replaceAll("-y", "");
            deckID = getIndex(arguments);
            isDeckDeleted = true;
        } else {
            deckID = getIndex(arguments);
            isDeckDeleted = Ui.getDeletedDeckConfirmation(deckList.getDeck(deckID).getName());
        }
        return new DeleteDeckCommand(deckList, deckID, isDeckDeleted);
    }

    /**
     * Prepares new Tag Command from given arguments.
     *
     * @param arguments arguments input from user
     * @return a Tag Command
     * @throws Exception if index is invalid or empty arguments
     */
    private Command prepareTagCommand(String arguments) throws Exception {
        String[] idAndNewTags = arguments.split("/tag");
        if (idAndNewTags.length < 2) {
            logger.log(Level.WARNING, "User did not provide /tag when adding tag.");
            throw new NoSeparatorException();
        }
        assert (arguments.contains("/tag")) :
                "Tags to be added are after /tag label.";
        int deckID = getIndex(idAndNewTags[0]);

        if (idAndNewTags[1].trim().isEmpty()) {
            logger.log(Level.WARNING, "User did not supply tags when adding tag.");
            throw new EmptyInputException();
        }

        String[] newTags = idAndNewTags[1].trim().split(" ");
        return new TagCommand(deckList, deckID, newTags);
    }

    /**
     * Prepares new Tag Command from given arguments.
     *
     * @param arguments arguments input from user
     * @return a Tag Command
     * @throws Exception if index is invalid or empty arguments
     */
    private Command prepareUntagCommand(String arguments) throws Exception {
        String[] idAndRemovedTags = arguments.split("/tag");

        if (idAndRemovedTags.length < 2) {
            logger.log(Level.WARNING, "User did not provide /tag when removing tags.");
            throw new NoSeparatorException();
        }
        assert (arguments.contains("/tag")) :
                "tags to be removed are after /tag label";

        int deckID = getIndex(idAndRemovedTags[0]);
        if (idAndRemovedTags[1].trim().isEmpty()) {
            logger.log(Level.WARNING, "User did not supply tags when removing tags.");
            throw new EmptyInputException();
        }
        String[] removedTags = idAndRemovedTags[1].trim().split(" ");

        return new UntagCommand(deckList, deckID, removedTags);
    }

    /**
     * Creates a new deck for adding to deckList.
     *
     * @param arguments String that represents the nae of deck to be created
     * @return Reference to the deck created
     * @throws EmptyInputException if no name is supplied for the deck
     */
    private Deck prepareNewDeck(String arguments) throws EmptyInputException {
        logger.log(Level.INFO, "Logging method prepareNewDeck() in NormalParser.");
        if (arguments.trim().isEmpty()) {
            logger.log(Level.WARNING, "User did not supply name when creating a new deck.");
            throw new EmptyInputException();
        }

        if (arguments.contains("/tag")) {
            ArrayList<String> tagsList = new ArrayList<>();
            String[] nameAndTags = arguments.split("/tag", 2);
            String name = nameAndTags[0].trim();
            String[] tags = nameAndTags[1].trim().split(" ");
            for (String tag: tags) {
                tagsList.add(tag.trim());
            }
            return new Deck(name, tagsList);
        } else {
            return new Deck(arguments);
        }
    }

    private PowerPointCommand preparePptxDeck(String arguments) throws Exception {
        if (arguments.contains("-y")) {
            arguments = arguments.replaceAll("-y", "");
            return new PowerPointCommand(deckList, prepareDeck(arguments), true);
        }
        int deckID = getIndex(arguments);
        boolean isPptxCreated = Ui.getPptxDeckConfirmation(deckList.getDeck(deckID).getName());
        return new PowerPointCommand(deckList, deckList.getDeck(deckID), isPptxCreated);
    }

    private Command prepareSearchCommand(String arguments) throws EmptyInputException {
        logger.log(Level.INFO, "Logging method prepareSearchCommand() in NormalParser.");

        if (arguments.trim().isEmpty()) {
            logger.log(Level.WARNING, "User did not supply tags when searching for decks.");
            throw new EmptyInputException();
        }

        String[] relevantTags = arguments.trim().split(" ");
        return new SearchCommand(deckList, relevantTags);
    }

    /**
     * Prepare Command for execution in Main.
     *
     * @param commandWord String that corresponds to a command
     * @param arguments String that lists the arguments for the command
     * @return respective Command object
     * @throws Exception when something wrong with the argument
     */
    @Override
    protected Command parseCommand(String commandWord, String arguments)
            throws Exception {

        assert (commandWord != null && arguments != null) :
                "commandWord and arguments should not be null";

        logger.log(Level.INFO, "Logging method parseCommand() in NormalParser.");

        switch (commandWord) {
        // Version
        case Ui.VERSION_CMD:
            return new VersionCommand();
        // Exit
        case Ui.EXIT:
            logger.log(Level.INFO, "User issued command to terminate program.");
            return new ExitCommand();
        // Help
        case Ui.HELP:
            logger.log(Level.INFO, "User issued command to view help.");
            return new HelpCommand();
        // Edit
        case Ui.EDIT:
            Deck deck = prepareDeck(arguments);
            logger.log(Level.INFO, "User issued command to edit deck " + deck.getName() + ".");
            return new EditCommand(deckList, deck);
        // Start
        case Ui.START:
            Deck startDeck = prepareDeck(arguments);
            logger.log(Level.INFO, "User issued command to start deck " + startDeck.getName() + ".");
            return new StartCommand(deckList, startDeck);
        // Create
        case Ui.CREATE:
            Deck newDeck = prepareNewDeck(arguments);
            logger.log(Level.INFO, "User issued command to create deck " + newDeck.getName() + ".");
            return new CreateCommand(deckList, newDeck);
        // Decks
        case Ui.DECKS:
            logger.log(Level.INFO, "User issued command to list decks.");
            return new DecksCommand(deckList);
        // Delete
        case Ui.DELETE:
            logger.log(Level.INFO, "User issued command to delete deck");
            return prepareDeleteDeck(arguments);
        // Tag
        case Ui.TAG:
            logger.log(Level.INFO, "User issued command to tag a deck.");
            return prepareTagCommand(arguments);
        // Untag
        case Ui.UNTAG:
            logger.log(Level.INFO, "User issued command to untag a deck.");
            return prepareUntagCommand(arguments);
        // Create new PowerPoint
        case Ui.PPTX:
            logger.log(Level.INFO, "User issued command to create a PowerPoint.");
            return preparePptxDeck(arguments);
        // Search
        case Ui.SEARCH:
            logger.log(Level.INFO, "User issued command to search for decks.");
            return prepareSearchCommand(arguments);
        default:
            logger.log(Level.INFO, "User issued an invalid command.");
            return new VoidCommand();
        }
    }

    /**
     * Parses User Input from Main.
     *
     * @param userInput Input from user, passed through Main
     * @return Command to be executed
     */
    @Override
    public Command parse(String userInput) {
        logger.log(Level.INFO, "Logging method parse() in NormalParser.");
        String[] splitString = userInput.split(" ", 2);
        String commandWord = splitString[0];
        logger.log(Level.INFO, "Parsed commandWord");
        boolean argumentsExist =  splitString.length > 1;
        String arguments = "";

        if (argumentsExist) {
            arguments = splitString[1];
            logger.log(Level.INFO, "Parsed arguments");
        }

        try {
            logger.log(Level.INFO, "Parsing command");
            return parseCommand(commandWord, arguments);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Parsed void or invalid command");
            return new VoidCommand(e.getMessage());
        }
    }

}
