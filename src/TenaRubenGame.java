import java.io.*;
import java.util.*;

public class TenaRubenGame {
    private String movieTitle;
    private String maskedTitle;
    private Set<Character> guessedLetters;
    private int attemptsLeft;
    private int score;
    private List<Character> wrongLetters;

    public TenaRubenGame() {
        guessedLetters = new HashSet<>();
        wrongLetters = new ArrayList<>();
        attemptsLeft = 10;
        score = 0;
        loadRandomMovie();
    }

    private void loadRandomMovie() {
        try (BufferedReader br = new BufferedReader(new FileReader("peliculas.txt"))) {
            List<String> movies = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                movies.add(line.trim().toLowerCase());
            }
            movieTitle = movies.get(new Random().nextInt(movies.size()));
            maskedTitle = movieTitle.replaceAll("[a-zA-Z]", "*");
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo de películas.");
        }
    }

    public boolean guessLetter(char letter) {
        letter = Character.toLowerCase(letter);
        if (guessedLetters.contains(letter)) {
            System.out.println("Letra ya intentada.");
            return false;
        }
        guessedLetters.add(letter);
        if (movieTitle.indexOf(letter) >= 0) {
            StringBuilder updatedMask = new StringBuilder(maskedTitle);
            for (int i = 0; i < movieTitle.length(); i++) {
                if (movieTitle.charAt(i) == letter) {
                    updatedMask.setCharAt(i, letter);
                }
            }
            maskedTitle = updatedMask.toString();
            score += 10;
            return true;
        } else {
            wrongLetters.add(letter);
            attemptsLeft--;
            score -= 10;
            return false;
        }
    }

    public boolean guessTitle(String guess) {
        if (guess.equalsIgnoreCase(movieTitle)) {
            score += 20;
            maskedTitle = movieTitle;
            return true;
        } else {
            score -= 20;
            attemptsLeft = 0;
            return false;
        }
    }

    public boolean isGameOver() {
        return attemptsLeft <= 0 || maskedTitle.equals(movieTitle);
    }

    public String getMaskedTitle() {
        return maskedTitle;
    }

    public int getScore() {
        return score;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public int getAttemptsLeft() {
        return attemptsLeft;
    }

    public List<Character> getWrongLetters() {
        return wrongLetters;
    }
}