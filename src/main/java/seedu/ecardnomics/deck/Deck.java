package seedu.ecardnomics.deck;

import java.util.ArrayList;

/**
 * Deck of flashcards.
 */
public class Deck {
    private String name;
    private ArrayList<String> tags;
    private ArrayList<FlashCard> deck;

    /** Constructor. */
    public Deck(String name) {
        assert (name != null && !name.isEmpty()) : "A deck requires a name.";
        this.name = name;
        deck = new ArrayList<>();
        tags = new ArrayList<>();
    }

    public Deck(String name, ArrayList<String> tags) {
        assert (name != null && !name.isEmpty()) : "A deck requires a name.";
        this.name = name;
        deck = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.tags = tags;
    }

    /**
     * Gets the name of the deck.
     *
     * @return name of deck
     */
    public String getName() {
        return name;
    }

    public String[] getTag() {
        String[] tagString = new String[tags.size()];
        for (int j = 0; j < tags.size(); j++) {
            tagString[j] = tags.get(j);
        }
        return tagString;
    }

    public void addTag(String[] newTags) {
        for (String tag: newTags) {
            tags.add(tag);
        }
    }

    public void removeTag(String[] deletedTags) {
        for (String tag: deletedTags) {
            tags.remove(tag);
        }
    }

    /**
     * Sets the name of the deck.
     *
     * @param name new name of the deck
     */
    public void setName(String name) {
        assert (name != null && !name.isEmpty()) : "A deck requires a name.";
        this.name = name;
    }

    /**
     * Retrieves the inherent ArrayList data structure of the deck.
     *
     * @return ArrayList of FlashCards
     */
    public ArrayList<FlashCard> getDeck() {
        return deck;
    }

    /**
     * Retrieves the flashcard at specified index.
     *
     * @param index Index of flashcard to be found
     * @return FlashCard at index
     */
    public FlashCard get(int index) {
        assert (index >= 0 && index < deck.size()) : "Index should be within range";
        return deck.get(index);
    }

    /**
     * Adds a flashcard to the deck.
     *
     * @param flashCard Reference to the flashcard to be added
     */
    public void add(FlashCard flashCard) {
        assert flashCard != null : "Do not add null objects into deck";
        deck.add(flashCard);
    }

    /**
     * Returns size of current deck.
     *
     * @return size of current deck
     */
    public int size() {
        return deck.size();
    }

    /**
     * Deletes the flashcard at specified index from the deck.
     *
     * @param index Index of flashcard to be deleted
     */
    public void delete(int index) {
        assert (index >= 0 && index < deck.size()) : "Index should be within range";
        deck.remove(index);
    }

    @Override
    public String toString() {
        String output = name + ":\n";
        for (int i = 0; i < deck.size(); i++) {
            output += (i + 1) + ". " + deck.get(i).toString();
            if (i != deck.size() - 1) {
                output += "\n\n";
            }
        }
        return output;
    }

    public String toString(String type) {
        String output = "";
        if (this.deck.size() == 0) {
            return output;
        }
        for (int i = 0; i < deck.size(); i++) {
            output += (i + 1) + ". " + deck.get(i).toString(type);
            if (i != deck.size() - 1) {
                output += "\n\n";
            }
        }
        return output;
    }
}
