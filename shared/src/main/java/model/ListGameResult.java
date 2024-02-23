package model;
import java.util.Collection;

public class ListGameResult {

    private Collection<GameData> games;

    public ListGameResult(Collection<GameData> games) {
        this.games = games;
    }

    public Collection<GameData> getGames() {
        return games;
    }

}
