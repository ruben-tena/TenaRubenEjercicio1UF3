import java.io.Serializable;

public class TenaRubenPlayer implements Serializable {
    private String nickname;
    private int score;

    public TenaRubenPlayer(String nickname, int score) {
        this.nickname = nickname;
        this.score = score;
    }

    public String getNickname() {
        return nickname;
    }

    public int getScore() {
        return score;
    }

    public String toString() {
        return nickname + ": " + score;
    }
}