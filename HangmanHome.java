package hangman;

public class HangmanHome {
    public static boolean newCharPosition = false;
    public static String[] result;
    public static boolean complete = false;

    public static void main(String args[]) {
        int simulation = 0;
        int totalSimulations = 1000000;
        int wins = 0;
        int progressReport = 1000;

        while(simulation < totalSimulations) {

            int guessesAllowedCount = 0, guessesAllowedMax = 6;
            String[] book = getDictionary();
            String word = findRandomWord(book);
            boolean answered = false;
            HangmanAI ai = new HangmanAI();
            boolean start = true;
            int wordLength = word.length();
            result = new String[wordLength];
            fill$();
            boolean success = false;

            String[] data = new String[1];
            String guessChar = "";

            while (!answered) {

                if (guessesAllowedCount == guessesAllowedMax) {
                    break;
                }
                data = ai.master(start, wordLength, success, guessChar, result);
                start = false;
                guessChar = data[0];

                checkLetterPosition(word, guessChar);

                if(newCharPosition) {
                    success = true;
                }
                else {
                    success = false;
                }
                if (complete) {
                    answered = true;
                    wins++;
                }
                guessesAllowedCount++;
            }

            if(simulation == progressReport) {

                System.out.println("PROGRESS REPORT Number " + progressReport);
                System.out.println();
                System.out.println("Current WINS = " + wins);
                System.out.println();
                System.out.println("PERCENTAGE WINS = " + ((double)wins/(double)simulation) * 100.0 + "%");
                progressReport+=1000;
            }

            simulation++;
        }

        System.out.println("SIMULATION FINISHED");
        System.out.println();
        System.out.println("RESULTS");
        System.out.println();
        System.out.println("WINS = " + wins);
        System.out.println();
        System.out.println("PERCENTAGE WINS = " + ((double)wins/(double)totalSimulations) * 100.0 + "%");

    }

    static String[] getDictionary() {
        FileIO reader = new FileIO();
        return reader.load("/Users/cathalmcgannon/Documents/HangmanAI/src/hangman/dictionary.txt");
    }

    static String findRandomWord(String[] book) {
        return book[(int)(Math.random()*(book.length-1))];
    }

    static void checkLetterPosition(String word, String guessChar) {
        int cp = -1;
        if(word.contains(guessChar)) {
            for(int i = 0; i < word.length(); i++) {
                if(word.charAt(i) == guessChar.charAt(0)) {
                    result[i] = guessChar;
                    cp++;
                    //printProgress();
                }
                if(checkIfFinished())complete = true;
            }
        }

        if(cp > -1) {
            newCharPosition = true;
        }
    }

    static boolean checkIfFinished() {
        boolean r = false;
        for(String s: result) {
            if(s.length()==1){
                r = true;
            } else {
                r = false;
            }
        } return r;
    }

    static void printProgress() {
        for(String s: result) {
            System.out.print(s + ", ");
        }
        System.out.println();

    }

    static void fill$() {
        for(int i = 0; i < result.length; i++) {
            result[i] = "[a-z]";
        }
    }
}
