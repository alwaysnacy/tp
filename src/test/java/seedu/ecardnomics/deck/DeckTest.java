package seedu.ecardnomics.deck;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class DeckTest {
    @Test
    void getName() {
        Deck deck = new Deck("Pokemon");
        assertEquals("Pokemon", deck.getName());
    }

    @Test
    void setName() {
        Deck deck = new Deck("Pokemon");
        deck.setName("Pokemon Go");
        assertEquals("Pokemon Go", deck.getName());
    }

    @Test
    void get_emptyDeck_exceptionThrown() {
        Deck deck = new Deck("Pokemon");
        try {
            assertEquals(null, deck.get(0));
            fail(); // test should not reach this line
        } catch (Exception e) {
            NullPointerException npe = new NullPointerException();
            assertEquals(npe.getMessage(), e.getMessage());
        }
    }

    @Test
    void get_invalidIndex_exceptionThrown() {
        Deck deck = initialiseDeck(2);
        try {
            assertEquals(null, deck.get(-1));
            assertEquals(null, deck.get(2));
            assertEquals(null, deck.get(3));
            fail(); // test should not reach this line
        } catch (Exception e) {
            NullPointerException npe = new NullPointerException();
            assertEquals(npe.getMessage(), e.getMessage());
        }
    }

    @Test
    void get_validIndex_success() {
        Deck deck = new Deck("Pokemon");
        FlashCard q1 = new FlashCard("q 1", "a 1");
        FlashCard q2 = new FlashCard("q 2", "a 2");
        deck.add(q1);
        deck.add(q2);
        assertEquals(q1, deck.get(0));
        assertEquals(q2, deck.get(1));
    }

    @Test
    void size() {
        Deck deck = initialiseDeck(2);
        assertEquals(2, deck.size());
        deck.delete(1);
        assertEquals(1, deck.size());
        deck.delete(2);
        assertEquals(0, deck.size());
        deck.add(new FlashCard("q 1", "a 1"));
        assertEquals(1, deck.size());
    }

    @Test
    void delete_emptyDeck_exceptionThrown() {
        Deck deck = initialiseDeck(0);
        try {
            deck.delete(0);
            fail(); // test should not reach this line
        } catch (Exception e) {
            NullPointerException npe = new NullPointerException();
            assertEquals(npe.getMessage(), e.getMessage());
        }
    }

    @Test
    void delete_invalidIndex_exceptionThrown() {
        Deck deck = initialiseDeck(2);
        try {
            deck.delete(-1);
            deck.delete(2);
            deck.delete(3);
            fail(); // test should not reach this line
        } catch (Exception e) {
            NullPointerException npe = new NullPointerException();
            assertEquals(npe.getMessage(), e.getMessage());
        }
    }

    @Test
    void delete_validIndex_success() {
        Deck deck = initialiseDeck(2);
        deck.delete(1);
        assertEquals(1, deck.size());
    }

    @Test
    void testToString_default_goodFormat() {
        Deck deck = initialiseDeck(2);
    }

    @Test
    void testToString_withAns_goodFormat() {
    }

    private Deck initialiseDeck(int size) {
        Deck deck = new Deck("Pokemon");
        for (int i = 0; i < size; i++) {
            FlashCard flashCard = new FlashCard(String.format("q %d", i), String.format("a %d", i));
            deck.add(flashCard);
        }
        return deck;
    }
}