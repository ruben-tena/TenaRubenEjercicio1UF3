import java.io.*;
import java.util.*;

public class TenaRubenMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TenaRubenGame game = new TenaRubenGame();

        System.out.println("Bienvenido al juego Adivina la película!");

        while (!game.isGameOver()) {
            System.out.println("\nPelícula: " + game.getMaskedTitle());
            System.out.println("Intentos restantes: " + game.getAttemptsLeft());
            System.out.println("Letras incorrectas: " + game.getWrongLetters());
            System.out.println("Puntuación actual: " + game.getScore());
            System.out.println("[1] Adivinar letra | [2] Adivinar título | [3] Salir");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Introduce una letra: ");
                    char letter = scanner.nextLine().charAt(0);
                    if (!Character.isLetter(letter)) {
                        System.out.println("¡Solo se pueden introducir letras!");
                    } else {
                        game.guessLetter(letter);
                    }
                    break;
                case "2":
                    System.out.print("Introduce el título: ");
                    String titleGuess = scanner.nextLine();
                    if (game.guessTitle(titleGuess)) {
                        System.out.println("Felicidades has adivinado la película.");
                    } else {
                        System.out.println("Incorrecto has perdido.");
                    }
                    break;
                case "3":
                    System.out.println("Gracias por jugar!");
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }

        System.out.println("\nLa película era: " + game.getMovieTitle());
        System.out.println("Puntuación final: " + game.getScore());
        manageRanking(game.getScore(), scanner);
    }

    private static void manageRanking(int score, Scanner scanner) {
        List<TenaRubenPlayer> ranking = new ArrayList<>();
        File file = new File("ranking.dat");

        if (!file.exists()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(ranking);
            } catch (IOException e) {
                System.out.println("Error al crear el archivo ranking.dat: " + e.getMessage());
                return;
            }
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            ranking = (List<TenaRubenPlayer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al leer el archivo de ranking: " + e.getMessage());
        }

        if (ranking.size() < 5 || score > ranking.get(ranking.size() - 1).getScore()) {
            String nickname;
            boolean isDuplicate;

            do {
                System.out.print("\nIntroduce tu nickname: ");
                nickname = scanner.nextLine();
                isDuplicate = false;

                for (TenaRubenPlayer player : ranking) {
                    if (player.getNickname().equalsIgnoreCase(nickname)) {
                        isDuplicate = true;
                        System.out.println("El nickname ya existe, introduce uno diferente.");
                        break;
                    }
                }
            } while (isDuplicate);

            ranking.add(new TenaRubenPlayer(nickname, score));
            ranking.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));

            if (ranking.size() > 5) {
                ranking = ranking.subList(0, 5);
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(ranking);
            } catch (IOException e) {
                System.out.println("Error al guardar el archivo de ranking: " + e.getMessage());
            }
        }

        for (TenaRubenPlayer player : ranking) {
            System.out.println(player.getNickname() + ": " + player.getScore());
        }
    }
}