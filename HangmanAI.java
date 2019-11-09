package hangman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class HangmanAI {

    public ArrayList<String> currentDictionaryWordLength = new ArrayList<>();
    public Map<Character, Integer> map = new HashMap<>();
    public ArrayList<String> lettersToAvoid = new ArrayList<>();

    public String[] master(boolean start, int wordLength, boolean success, String guessChar, String[] currentProgressWord) {
        String[] data = new String[1];

        if(start) {
            //System.out.println("Start");
            getDictionaryByWordLength(wordLength);
            getDictionaryByLetterFrequency();
            data[0] = getGuess();
            return data;
        }
        if(success && !start) {
            lettersToAvoid.add(guessChar);
            keepCorrectWordsByArray(currentProgressWord);
            getDictionaryByLetterFrequency();
            data[0] = getGuess();
            return data;
        }
        if(!start && !success) {
            removeIncorrectWordsAndLetters(guessChar);
            keepCorrectWordsByArray(currentProgressWord);
            getDictionaryByLetterFrequency();
            data[0] = getGuess();
            return data;
        }
        map.clear();
        return data;
    }

    public void getDictionaryByWordLength(int wordLength) {
        String[] d = getDictionary();
        for(String s : d) {
            if(s.length() == wordLength) {
                currentDictionaryWordLength.add(s);
            }
        }
    }

    public void getDictionaryByLetterFrequency() {
        for(int i = 0; i < currentDictionaryWordLength.size(); i++) {
            for(int c = 0; c < currentDictionaryWordLength.get(i).length(); c++) {
                Integer r = map.get( currentDictionaryWordLength.get(i).charAt(c) );
                if(r == null) {
                    map.put( currentDictionaryWordLength.get(i).charAt(c), 1 );
                } else {
                    map.put( currentDictionaryWordLength.get(i).charAt(c), r + 1 );
                }
            }
        }
    }

    public void removeIncorrectWordsAndLetters(String incorrectChar) {
        for(int i = 0; i < currentDictionaryWordLength.size(); i++) {
            if(currentDictionaryWordLength.get(i).contains(incorrectChar)) {
                currentDictionaryWordLength.remove(i);
            }
        }
        lettersToAvoid.add(incorrectChar);
    }

    public String getGuess() {
        Integer max = Integer.MIN_VALUE;
        String result = "";

        for(int i = 0; i < 26; i++) {
            Character cv = Character.valueOf((char) (i + 'a'));
            if(!lettersToAvoid.contains(cv+"")) {
                Integer r = map.get(cv);
                if(r!=null) {
                    int compare = r.compareTo(max);
                    if (compare > 0) {
                        max = r;
                        result = cv + "";
                    }
                }
            }
        }
        return result;
    }

    public String[] getDictionary() {
        FileIO reader = new FileIO();
        return reader.load("/Users/cathalmcgannon/Documents/HangmanAI/src/hangman/dictionary.txt");
    }

    public void keepCorrectWordsByArray(String[] word) {
        ArrayList<String> newDiction = new ArrayList<>();
        String pattern = "";
        boolean matches;

        for(int p = 0; p < word.length; p++) {
            pattern += word[p];
        }

        for(int i = 0; i < currentDictionaryWordLength.size(); i++) {
            matches = Pattern.matches(pattern, currentDictionaryWordLength.get(i));
            if(matches) {
                newDiction.add(currentDictionaryWordLength.get(i));
            }
        }
        currentDictionaryWordLength.clear();
        currentDictionaryWordLength.addAll(newDiction);
    }
}
