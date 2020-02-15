package edu.ramapo.btunney.fivecrowns.combination;

import android.util.Log;
import android.util.Pair;

import java.util.Vector;

import edu.ramapo.btunney.fivecrowns.CardDealerModel;
import edu.ramapo.btunney.fivecrowns.RoundActivity;
import edu.ramapo.btunney.fivecrowns.card.CardModel;
import edu.ramapo.btunney.fivecrowns.card.CardVectorModel;

/*******************************************************
 Class that finds combinations given a list of cards
 @author Brendan Tunney
 @since 12/05/2019
  ******************************************************** */

public class CombinationFinder {

    // *********************************************************
    // **************** Configuration Variables ****************
    // *********************************************************

    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************

    /** The type of Combination */
    public enum Type {
        Undefined,
        Book,
        Run,
        PartialBook,
        PartialRun
    }

    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    /** The initial cards given */
//    private Vector<CardModel> initialCards;
    private CardVectorModel initialCards;

    /** A copy of the cards to check for books/runs */
    private CardVectorModel copyOfCards;

    private CardVectorModel jokersAndWilds;

    /** The books and partial books found */
    private Vector<CardVectorModel> books;
    private Vector<CardVectorModel> partialBooks;

    /** The runs and partial runs found */
    private Vector<CardVectorModel> runs;
    private Vector<CardVectorModel> partialRuns;

    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ******************** Constructor ************************
    // *********************************************************

    public CombinationFinder(CardVectorModel cards) {
        initialCards = new CardVectorModel(cards);

        // TODO: Figure out if this is true deep copy?
        books = new Vector<CardVectorModel>(3, 6);
        partialBooks = new Vector<CardVectorModel>(3, 6);
        runs = new Vector<CardVectorModel>(3, 6);
        partialRuns = new Vector<CardVectorModel>(3, 6);
        copyOfCards = new CardVectorModel();
        jokersAndWilds = new CardVectorModel();
        copyOfCards = new CardVectorModel(initialCards);
    }

    // *********************************************************
    // ******************** Paint - View ***********************
    // *********************************************************

    // *********************************************************
    // ******************** actionPerformed - Controller *******
    // *********************************************************

    // *********************************************************
    // ******************** Selectors **************************
    // *********************************************************

    /**
     * Purpose: Return the remaining cards after making books/runs
     * @return a CardVectorModel, remaining cards
     */
    public CardVectorModel getRemainingCards() {

//        // Make sure al partials are back and counted
//        movePartialsBackToHand();

        CardVectorModel remainingCards = new CardVectorModel();
        CardModel card = new CardModel();
        // Add all jokers/wilds remaining
        for (int i = 0; i < jokersAndWilds.size(); i++) {
            card = new CardModel(jokersAndWilds.get(i));
            remainingCards.add(card);
        }
        // Add all remaining cards in copy of cards
        for (int i = 0; i < copyOfCards.size(); i++) {
            card = new CardModel(copyOfCards.get(i));
            remainingCards.add(card);;
        }

        return remainingCards;
    }

    /**
     * Purpose: Return the score of all the cards existing in copyOfCards
     *      Used for determining if we should make books first or runs first
     * @return the total score of all the cards
     */
    public int getScoreOfRemainingCards() {
        CardVectorModel cardsRemaining = getRemainingCards();

        int score = 0;
        for (int i = 0; i < cardsRemaining.size(); i++) {
            score += cardsRemaining.get(i).getValue();
        }

        return score;
    }

    /**
     * Purpose: Return the books found
     * @return a Vector<CardVectorModel>, the books
     */
    public Vector<CardVectorModel> getBooks() {
        return books;
    }

    /**
     * Purpose: Return the runs found
     * @return a Vector<CardVectorModel>, the runs
     */
    public Vector<CardVectorModel> getRuns() {
        return runs;
    }

    /**
     * Purpose: Return the number of cards leftover after making books and runs
     * @return an int, the number of cards
     */
    public int getNumberOfRemainingCards() {
        return copyOfCards.size();
    }

    /**
     * Purpose: Find the card best to remove after making books and runs
 *          First try highest valued card in the remaining cards
     *      Try not to remove jokers and wild cards if we can help it
     *      Removes from books/runs if we have no other option
     * @return a Pair<String, String>, the card and reason
     */
    public Pair<String, String> getBestCardToDiscard() {
        copyOfCards.sort();


        // Really bad check to see if all but one cards have same suit
//        int count = 0;
//        count += jokersAndWilds.size();
//        for (int i = 0; i < initialCards.size() -1; i++) {
//            if (initialCards.get(i).getSuit() == initialCards.get(i+1).getSuit()) {
//                count++;
//            }
//        }
//        if (count == initialCards.size() -1) {
//            for (int i = 0; i < initialCards.size() -1; i++) {
//                if (initialCards.get(i).getSuit() != initialCards.get(i+1).getSuit()) {
//                    if (!initialCards.get(i).isJoker() && !initialCards.get(i).isWildCard()) {
//                        return new Pair<String, String>(initialCards.get(i).toString(), "oh no");
//                    }
//                }
//            }
//        }

        if (copyOfCards.size() == 1) {
            return new Pair<String, String> (copyOfCards.get(0).toString(), "it was the only card remaining after making books/runs");
        }

        if (!copyOfCards.isEmpty()) {
            int countOfJokersAndWilds = 0;

            // Get the highest valued remaining non wild/joker card
            for (int i = copyOfCards.size() - 1; i >= 0; i--) {
                if (!copyOfCards.get(i).isWildCard() && !copyOfCards.get(i).isJoker()) {
                    return new Pair<String, String> (copyOfCards.get(i).toString(), "it was the highest valued non wild/joker remaining after books/runs");
                } else {
                    countOfJokersAndWilds++;
                }
            }

            // Get the highest valued joker/wild if they are all jokers/wilds
            if (countOfJokersAndWilds == copyOfCards.size()) {
                return new Pair<String, String> (copyOfCards.lastElement().toString(), "there were only wilds/jokers remaining after books/runs");
            }
        } else {
            // When there's no cards remaining, we need to get the highest valued card in a book/run we can safely remove without invalidating the book/runs

            // Safe removal of card in book
            for (int book = 0; book < books.size(); book++) {
                if (books.get(book).size() > 3) {
                    // The last card in a book that can be safely removed (will still be a book)
                    return new Pair<String, String>(books.get(book).lastElement().toString(), "all cards were made into books/runs and it was safe to remove");
                }
            }

            // Safe removal of card in run
            for (int run = 0; run < runs.size(); run++) {
                int x = runs.get(run).size();
                if (runs.get(run).size() > 3) {
                    // The last card in a run that can be safely removed (will still be a run)
                    return new Pair<String, String>(runs.get(run).lastElement().toString(), "all cards were made into books/runs and it was safe to remove");
                }
            }

            // Non-safe removal of card in book/run because all cards were made into books/runs
            // TODO: just make books and runs with initial cards with every combination besides 1 card
            //  (i.e. if we had 4 cards try with 3 cards without index 0, then index 1, index 2, then 3 and see if still score of 0 or 0 remaining cards
            for (int book = books.size() - 1; book >= 0; book--) {
                if (books.get(book).size() > 2) {
                    // The last card in a book that can be safely removed (will still be a book)
                    return new Pair<String, String>(books.get(book).lastElement().toString(), "all cards were made into books/runs and a book had to be destroyed");
                }
            }
            for (int run = runs.size() - 1; run >= 0; run--) {
                if (runs.get(run).size() > 2) {
                    // The last card in a book that can be safely removed (will still be a book)
                    return new Pair<String, String>(runs.get(run).lastElement().toString(), "all cards were made into books/runs and a run had to be destroyed");
                }
            }

        }


        return new Pair<String, String> ("", "no optimal solution found");
    }

    /**
     * Purpose: Return the index of the card given a string and the reason why to discard it
     * @return a Pair<Integer, String>, the index and reason
     */
    public Pair<Integer, String> getIndexOfBestCardToDiscard() {
        Pair<String, String> cardAndReason = getBestCardToDiscard();
        String cardString = cardAndReason.first;
        String reason = cardAndReason.second;

        // Find where that card exists within the initial cards passed in to constructor
        for (int i = 0; i < initialCards.size(); i++) {
            if (cardString.equals(initialCards.get(i).toString())) {
                return new Pair<Integer, String>(i, reason);
//                return i;
            }
        }

        // Card not found
        return new Pair<Integer, String>(-1, reason);
    }



    // *********************************************************
    // ******************** Mutators ***************************
    // *********************************************************

    /**
     * Purpose: Find whether making books or runs first results in a lower score of the remaining cards
     *      not used in books or runs.
     */
    public void makeCombinationsInBestOrder() {
        int scoreForBooksFirst = scoreFromBooksFirst();
        int scoreForRunsFirst = scoreFromRunsFirst();

        // Make the combination which would result in creating the least amount of points
        if (scoreForRunsFirst > scoreForBooksFirst) {
            booksThenRuns();
        } else {
            runsThenBooks();
        }
    }

    /**
     * Purpose: Get the score of the remaining cards when making books first
     * @return the score
     */
    private int scoreFromBooksFirst() {
        copyOfCards = new CardVectorModel(initialCards);
        booksThenRuns();

        return getScoreOfRemainingCards();

    }

    /**
     * Purpose: Get the score of the remaining cards when making runs first
     * @return the score
     */
    private int scoreFromRunsFirst() {
        copyOfCards = new CardVectorModel(initialCards);
        runsThenBooks();

        return getScoreOfRemainingCards();
    }


    /**
     * Purpose: Go through all the jokers and use them to create more books and runs if we can
     */
    private void useJokersAndWilds() {
        makeCombinationsWithPartialAndWild(Type.PartialRun);
        makeCombinationsWithPartialAndWild(Type.PartialBook);
        makeCombinationsWithStrayAndWild();
        makeCombinationsWithRemainingWild();

    }

    /**
     * Purpose: Make books/runs using partial books/runs and wild/jokers until there are no more jokers or no more partials
     */
    private void makeCombinationsWithPartialAndWild(Type partialType) {
        CardVectorModel combination = new CardVectorModel();
        CardModel card = new CardModel();

        // Partial runs
        if (partialType == Type.PartialRun) {
            while (!partialRuns.isEmpty() && !jokersAndWilds.isEmpty()) {
                combination = new CardVectorModel();
                combination = returnAndDeletePartial(Type.PartialRun);
                card = new CardModel(jokersAndWilds.get(0));
                combination.add(card);
                jokersAndWilds.remove(0);
                runs.add(combination);
            }
        } else if (partialType == Type.PartialBook) {
            // Partial books
            while (!partialBooks.isEmpty() && !jokersAndWilds.isEmpty()) {
                combination = new CardVectorModel();
                combination = returnAndDeletePartial(Type.PartialBook);
                card = new CardModel(jokersAndWilds.get(0));
                combination.add(card);
                jokersAndWilds.remove(0);
                books.add(combination);
            }
        }


    }

    /**
     * Purpose: Make books/runs with stray cards left over after finding books/runs and using wild/jokers
     *      to complete partial books/runs by using 2 jokers/wilds for every stray card
     */
    private void makeCombinationsWithStrayAndWild() {
        CardModel card = new CardModel();
        CardVectorModel book = new CardVectorModel();

        while (jokersAndWilds.size() >= 2 && copyOfCards.size() > 0) {
            book = new CardVectorModel();
            // Use and remove 2 jokers/wilds and use and remove 1 of the stray cards to make a book
            for (int joker = 0; joker < 2; joker++) {
                card = new CardModel(jokersAndWilds.get(0));
                book.add(card);
                jokersAndWilds.remove(0);
            }
            card = new CardModel(copyOfCards.get(0));
            book.add(card);
            copyOfCards.remove(0);

            // Add new book to books
            books.add(book);
        }
    }

    /**
     * Purpose: Use the remaining jokers/wilds not used to complete partials/strays
     */
    private void makeCombinationsWithRemainingWild() {
        CardModel card = new CardModel();
        CardVectorModel book = new CardVectorModel();

        // If we have no books or runs and we have at least 3 wild/jokers, make book of all of them
        if (books.size() == 0 && runs.size() == 0) {
            if (jokersAndWilds.size() > 2) {
                while (jokersAndWilds.size() != 0) {
                    card = new CardModel(jokersAndWilds.get(0));
                    book.add(card);
                    jokersAndWilds.remove(0);
                }
                books.add(book);
            }
            // If there is at least 1 book, put rest of jokers/wilds on the first book
            // TODO: put them in the largest book
        } else if (books.size() > 0) {
            while (jokersAndWilds.size() != 0) {
                card = new CardModel(jokersAndWilds.get(0));
                books.get(0).add(card);
                jokersAndWilds.remove(0);
            }
            // If there is at least 1 run, put rest of jokers/wilds on the first run
            // TODO: put them in the largest run
        } else if (runs.size() > 0) {
            while (jokersAndWilds.size() != 0) {
                card = new CardModel(jokersAndWilds.get(0));
                runs.get(0).add(card);
                jokersAndWilds.remove(0);
            }
            // Something's gone terribly wrong
        } else {
            throw new IllegalStateException("jokers/wilds can't be used!");
        }
    }


    /**
     * Purpose: Move all cards in partial books and runs back into the copyOfCards
     *      Would be used after finding all books and runs and we need to make sure
     *      that we count these cards when calculating the score of the hand
     */
    private void movePartialsBackToHand() {
        CardModel card;
        while (!partialBooks.isEmpty()) {
            for (int i = partialBooks.get(0).size() - 1; i >= 0; i--) {
                card = new CardModel(partialBooks.get(0).get(i));
                copyOfCards.add(card);
                partialBooks.remove(partialBooks.get(0).get(i));
            }
            partialBooks.removeElementAt(0);

        }

        while (!partialRuns.isEmpty()) {
            for (int i = partialRuns.get(0).size() - 1; i >= 0; i--) {
                card = new CardModel(partialRuns.get(0).get(i));
                copyOfCards.add(card);
                partialRuns.remove(partialRuns.get(0).get(i));
            }
            partialRuns.removeElementAt(0);

        }
    }

    /**
     * Purpose: Return and remove a partial run/book from the Vector of partial runs/books
     * @param partialType
     */
    private CardVectorModel returnAndDeletePartial(Type partialType) {
        CardModel card = new CardModel();
        CardVectorModel partialCombination = new CardVectorModel();
        if (partialType == Type.PartialRun) {
            // Add each card in the partial run to new partial run and remove from old
            for (int i = partialRuns.get(0).size() - 1; i >= 0; i--) {
                card = new CardModel(partialRuns.get(0).get(i));
                partialCombination.add(0, card);
                partialRuns.get(0).remove(i);
            }
            partialRuns.removeElementAt(0);
        } else if (partialType == Type.PartialBook) {
            // Add each card in the partial run to new partial run and remove from old
            for (int i = partialBooks.get(0).size() - 1; i >= 0; i--) {
                card = new CardModel(partialBooks.get(0).get(i));
                partialCombination.add(0, card);
                partialBooks.get(0).remove(i);
            }
            partialBooks.removeElementAt(0);

        }
        return partialCombination;

    }


    /**
     * Purpose: Find all books and runs by making books first then runs.
     *      Also makes partial books then partial runs after whole combinations
     */
    private void booksThenRuns() {
        // Reset cards so we don't have duplicate combinations
        resetEverything();

        findBooks();
        findRuns();
        findPartialBooks();
        findPartialRuns();

        // Use all wild cards and jokers to complete books and runs
        useJokersAndWilds();

        // Move all the partials books and runs back to copyOfCards
        movePartialsBackToHand();
    }

    /**
     * Purpose: Find all books and runs by making runs first then books.
     *      Also makes partial runs then partial books after whole combinations
     */
    private void runsThenBooks() {
        // Reset cards so we don't have duplicate combinations
        resetEverything();

        findRuns();
        findBooks();
        findPartialRuns();
        findPartialBooks();

        // Use all wild cards and jokers to complete books and runs
        useJokersAndWilds();

        // Move all the partials books and runs back to copyOfCards
        movePartialsBackToHand();
    }

    /**
     * Purpose: Reset all cards so we don't end up with duplicate combinations
     */
    private void resetEverything() {
        copyOfCards = new CardVectorModel(initialCards);
        books.clear();
        runs.clear();
        partialBooks.clear();
        partialRuns.clear();
        jokersAndWilds.clear();
    }

    /**
     * Purpose: Sort carts in a decent order to check for books
     * 4D 6D 4C 8D 3C 5D 4T -> 3C 4C 4D 4T 5D 8D
     */
    private void sortCardsForBooks() {
        separateJokersAndWilds(copyOfCards);
        copyOfCards.sort();
    }

    /**
     * Purpose: Sort the cards in a decent order to check for runs
     * 3S 3T 3D 4S 4T 4D 5S 5T 5D - > 3D 4D 5D 3S 4S 5S 3D 4D 5D
     */
    private void sortCardsForRuns() {
        CardVectorModel clubs = new CardVectorModel();
        CardVectorModel diamonds = new CardVectorModel();
        CardVectorModel hearts  = new CardVectorModel();
        CardVectorModel spades = new CardVectorModel();
        CardVectorModel tridents = new CardVectorModel();


        // Add each card to respective CardVectorModel or add to joker
        for (int i = copyOfCards.size() - 1; i >= 0; i--) {
            char suit;
            CardModel card;
            CardModel cardCopy;
            card = copyOfCards.get(i);

            suit = card.getSuit();
            cardCopy = new CardModel(card);

            switch (suit) {
                case 'C':
                    clubs.add(cardCopy);
                    copyOfCards.remove(i);
                    break;
                case 'D':
                    diamonds.add(cardCopy);
                    copyOfCards.remove(i);
                    break;
                case 'H':
                    hearts.add(cardCopy);
                    copyOfCards.remove(i);
                    break;
                case 'S':
                    spades.add(cardCopy);
                    copyOfCards.remove(i);
                    break;
                case 'T':
                    tridents.add(cardCopy);
                    copyOfCards.remove(i);
                    break;
            }
        }

        separateJokersAndWilds(copyOfCards);
        //clearCopyCards();

        clubs.sort();
        diamonds.sort();
        hearts.sort();
        spades.sort();
        tridents.sort();

        for (int i = 0; i < clubs.size(); i++) {
            copyOfCards.add(clubs.get(i));
        }
        for (int i = 0; i < diamonds.size(); i++) {
            copyOfCards.add(diamonds.get(i));
        }
        for (int i = 0; i < hearts.size(); i++) {
            copyOfCards.add(hearts.get(i));
        }
        for (int i = 0; i < spades.size(); i++) {
            copyOfCards.add(spades.get(i));
        }
        for (int i = 0; i < tridents.size(); i++) {
            copyOfCards.add(tridents.get(i));
        }

    }

    /**
     * Purpose: Find full runs without wild and jokers
     */
    private void findRuns() {
        // i.e. 3S 4S 5C 4D 5D 6D 7D..
        sortCardsForRuns();

        int beginningOfRunIndex;

        // Check every card in the hand
        for (int currentIndex = 0; currentIndex < copyOfCards.size() - 1; currentIndex++) {
            beginningOfRunIndex = currentIndex;

            char suit = copyOfCards.get(currentIndex).getSuit();
            while (currentIndex < (copyOfCards.size() - 1) && (copyOfCards.get(currentIndex).sameSuit(copyOfCards.get(currentIndex)) == true) && (copyOfCards.get(currentIndex).getValue()) == (copyOfCards.get(currentIndex + 1).getValue()) - 1) {
                char suit2 = copyOfCards.get(currentIndex + 1).getSuit();

                // The suit of the current and next card must be the same
                if (suit != suit2) {
                    break;
                }
                currentIndex++;

            }

            // If we found a run
            if ((currentIndex - beginningOfRunIndex) >= 2) {
                // Add to runs and remove from copyOfCards
                addCombination(beginningOfRunIndex, currentIndex, Type.Run);
                findRuns();
            }
        }

    }

    /**
     * Purpose: Find full books without wild and jokers
     */
    private void findBooks() {
        sortCardsForBooks();

        int beginningOfBookIndex;

        // Check every card in the hand
        for (int currentIndex = 0; currentIndex < copyOfCards.size() - 1; currentIndex++) {
            beginningOfBookIndex = currentIndex;

            int face = copyOfCards.get(currentIndex).getFace();
            while (currentIndex < (copyOfCards.size() - 1)) {
                int face2 = copyOfCards.get(currentIndex + 1).getFace();

                // Compare current card's face to the next card's
                if (face != face2) {
                    break;
                }

                currentIndex++;

            }

            // If we found a book
            if ((currentIndex - beginningOfBookIndex) >= 2) {
                // Add to books and remove from copyOfCards
                addCombination(beginningOfBookIndex, currentIndex, Type.Book);
                findBooks();
            }
        }
    }

    /**
     * Purpose: Find partial runs without wild and jokers
     */
    private void findPartialRuns() {
        sortCardsForRuns();

        int beginningOfPartialRunIndex;
        int cardCount = 0;

        // Check every card in the hand
        for (int currentIndex = 0; currentIndex < copyOfCards.size() - 1; currentIndex++) {
            beginningOfPartialRunIndex = currentIndex;
            cardCount = 1;

            char suit = copyOfCards.get(currentIndex).getSuit();

            // Make sure we haven't gone beyond the end of the hand and the next card is is 1 or 2 more then the current card
            while (currentIndex < (copyOfCards.size() - 1) && (copyOfCards.get(currentIndex).sameSuit(copyOfCards.get(currentIndex)) == true)
                    && ((copyOfCards.get(currentIndex).getValue()) == (copyOfCards.get(currentIndex + 1).getValue()) - 1)
                    || (copyOfCards.get(currentIndex).getValue() == (copyOfCards.get(currentIndex + 1).getValue() - 2))){
                char suit2 = copyOfCards.get(currentIndex + 1).getSuit();

                // Compare current card's suit to the next card's
                if (suit != suit2) {
                    break;
                }

                // Increment the number of cards valid
                cardCount++;
                currentIndex++;

                // Partial run found
                if (cardCount == 2) {
                    break;
                }

            }

            // If we found a partial run
            if (cardCount == 2) {
                // Add to runs and remove from copyOfCards
                addCombination(beginningOfPartialRunIndex, currentIndex, Type.PartialRun);
                findPartialRuns();
            }
        }
    }


    /**
     * Purpose: Find partial books without wild and jokers
     */
    private void findPartialBooks() {
        sortCardsForBooks();

        int beginningOfPartialBookIndex;

        // Check every card in the hand
        for (int currentIndex = 0; currentIndex < copyOfCards.size() - 1; currentIndex++) {
            beginningOfPartialBookIndex = currentIndex;

            int face = copyOfCards.get(currentIndex).getFace();
            while (currentIndex < (copyOfCards.size() - 1)) {
                int face2 = copyOfCards.get(currentIndex + 1).getFace();

                // Make sure the face of the current and next card are the same
                if (face != face2) {
                    break;
                }

                currentIndex++;

            }

            // If we found a partial book
            if ((currentIndex - beginningOfPartialBookIndex) == 1) {
                // Add to books and remove from copyOfCards
                addCombination(beginningOfPartialBookIndex, currentIndex, Type.PartialBook);
                findPartialBooks();
            }
        }
    }

    /**
     * Purpose: Add a found combination to the respective Vector of that type
     * @param beginningIndex the index in copyOfCards the combination starts at
     * @param endIndex the index in copyOfCards the combination starts at (inclusive)
     * @param combinationType the type of combination
     */
    private void addCombination(int beginningIndex, int endIndex, Type combinationType) {
        CardVectorModel combination = new CardVectorModel();

        for (int i = endIndex; i >= beginningIndex; i--) {
            CardModel card = new CardModel(copyOfCards.get(i));
            combination.add(0, card);
            copyOfCards.remove(i);
        }

        switch (combinationType) {
            case Book:
                books.add(combination);
                break;
            case Run:
                runs.add(combination);
                break;
            case PartialBook:
                partialBooks.add(combination);
                break;
            case PartialRun:
                partialRuns.add(combination);
                break;
            default:
                throw new IllegalArgumentException();
                // TODO: default with error/exception
        }
    }


    /**
     * Purpose: Clear the copy of the initial cards
     */
    private void clearCopyCards() {
        copyOfCards.clear();
    }

    /**
     * Purpose: Clear the jokers and wild cards stored
     */
    private void clearJokersAndWilds() {
        jokersAndWilds.clear();
    }

    /**
     * Purpose: Clear cards in the copy of cards and the jokers/wilds
     */
    private void clearAllCards() {
        copyOfCards.clear();
        jokersAndWilds.clear();
    }

    /**
     * Purpose: Remove all jokers and wilds from given cards
     * @param cards the given cards
     */
    private void separateJokersAndWilds(CardVectorModel cards) {
        for (int i = cards.size() - 1; i >= 0; i--) {
            if (cards.get(i).isWildCard() || cards.get(i).isJoker()) {
                jokersAndWilds.add(cards.get(i));
                cards.remove(i);
            }
        }
    }

    // *********************************************************
    // ******************** Code Generation ********************
    // *********************************************************

    // *********************************************************
    // ******************** Code Explanation *******************
    // *********************************************************

    // *********************************************************
    // ******************** Utility Methods ********************
    // *********************************************************

    // *********************************************************
    // ******************** Printing Methods *******************
    // *********************************************************

    public void printCards() {
        for (int i = 0; i < initialCards.size(); i++) {
            Log.d(this.getClass().toString(), initialCards.get(i).toString());
        }
    }

    // *********************************************************
    // ******************** Debugging Methods ******************
    // *********************************************************

}
