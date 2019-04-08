public class ChampionPlayed {
    private int championId;
    private boolean win;

    public ChampionPlayed(int id, boolean w){
        championId = id;
        win = w;
    }

    public int getChampionId(){
        return championId;
    }

    public boolean getWin(){
        return win;
    }
}
