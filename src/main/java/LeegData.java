import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class LeegData {

    public static void main(String[] args) throws IOException {

        ArrayList champList = new ArrayList<ChampionPlayed>();

        String baseUrl = "https://s3-us-west-1.amazonaws.com/riot-developer-portal/seed-data/matches";

        for(int j = 1; j < 11; j++) {
            String url = baseUrl + j + ".json";


            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();


            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject obj = new JSONObject(response.toString());
            JSONArray matches = obj.getJSONArray("matches");

            for (int i = 0; i < matches.length(); i++) {

                JSONObject match = matches.getJSONObject(i);

                /*
                System.out.println("===KEYS===");
                Iterator<String> keys = match.keys();
                while(keys.hasNext()) {
                    String key = keys.next();
                    System.out.println(key);
                }
                System.out.println("===KEYS===\n");
                */

                String team1Win = match.getJSONArray("teams").getJSONObject(0).getString("win");
                int team1Id = match.getJSONArray("teams").getJSONObject(0).getInt("teamId");

                int winningTeam = 200;  // default to team 200 (red)

                if (team1Id == 100 && team1Win.equals("Win")) {
                    winningTeam = 100;
                } else if (team1Id == 200 && team1Win.equals("Fail")) {
                    winningTeam = 100;
                }

                //System.out.println("winning team: " + winningTeam);

                JSONArray participants = match.getJSONArray("participants");

                for (Object player : participants) {
                    JSONObject p = (JSONObject) player;
                    int id = p.getInt("championId");
                    int team = p.getInt("teamId");
                    ChampionPlayed cp;

                    if (team == winningTeam) {
                        // add winner
                        cp = new ChampionPlayed(id, true);
                    } else {
                        // add loser
                        cp = new ChampionPlayed(id, false);
                    }

                    champList.add(cp);
                }

            }

        }

        /*
        // print id : win/loss
        System.out.println("\n==LIST==");
        for(Object o : champList){
            ChampionPlayed cp = (ChampionPlayed) o;
            String win = cp.getWin() ? "win" : "lose";
            System.out.println(cp.getChampionId() + ":" + win);
        }
        */

        System.out.println("\nsize: " + champList.size());

    }
}
