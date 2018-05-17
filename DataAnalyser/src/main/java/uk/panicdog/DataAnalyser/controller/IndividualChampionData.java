package uk.panicdog.DataAnalyser.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.panicdog.DataAnalyser.JSONReader;
import uk.panicdog.DataAnalyser.SeedAnalyser;

import java.io.IOException;

@RestController
//@Controller
public class IndividualChampionData {

    @SuppressWarnings("Duplicates")
    public JSONArray individualChampionData(String url) {

        JSONReader jr = new JSONReader();
        JSONObject matches = null;

        try {
            matches = jr.readJsonObjectFromUrl(url);
            //x = m.get("gameId").toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray gamesArray = new JSONArray();

        for (int x = 0; x < matches.getJSONArray("matches").length(); x++) {

            JSONObject m = (JSONObject) matches.getJSONArray("matches").get(x);

            //Team 1
            for (int i = 0; i < 5; i++) {

                JSONObject p = new JSONObject();

                //game id
                p.put("gameId ", m.get("gameId"));

                //game duration
                p.put("gameDuration", m.get("gameDuration"));

                //participant id's
                p.put("currentAccountId", m.getJSONArray("participantIdentities").getJSONObject(i).getJSONObject("player").get("currentAccountId"));
                p.put("accountId", m.getJSONArray("participantIdentities").getJSONObject(i).getJSONObject("player").get("accountId"));
                p.put("summonerId", m.getJSONArray("participantIdentities").getJSONObject(i).getJSONObject("player").get("summonerId"));

                //picks and bans
                p.put("champion", m.getJSONArray("participants").getJSONObject(i).get("championId"));
                p.put("lane", m.getJSONArray("participants").getJSONObject(i).getJSONObject("timeline").get("lane"));
                p.put("ban", m.getJSONArray("teams").getJSONObject(1).getJSONArray("bans").getJSONObject(i).getInt("championId"));
                p.put("win", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("win"));

                //k/d/a
                p.put("kills", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("kills"));
                p.put("deaths", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("deaths"));
                p.put("assists", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("assists"));

                //gold and cs
                p.put("gold earned", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("goldEarned"));
                p.put("cs", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("totalMinionsKilled"));

                //damage dealt and taken
                p.put("totalDamageDealt", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("totalDamageDealtToChampions"));
                p.put("magicDamageDealt", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("magicDamageDealtToChampions"));
                p.put("physicalDamageDealt", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("physicalDamageDealtToChampions"));
                p.put("trueDamageDealt", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("trueDamageDealtToChampions"));

                p.put("damageTaken", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("totalDamageTaken"));

                //summoners
                p.put("summ1", m.getJSONArray("participants").getJSONObject(i).get("spell1Id"));
                p.put("summ2", m.getJSONArray("participants").getJSONObject(i).get("spell2Id"));

                //items
                JSONObject items = new JSONObject();

                items.put("item0", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item0"));
                items.put("item1", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item1"));
                items.put("item2", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item2"));
                items.put("item3", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item3"));
                items.put("item4", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item4"));
                items.put("item5", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item5"));
                items.put("item6", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item6"));

                p.put("items", items);

                //vision
                p.put("wardsPlaced", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("wardsPlaced"));

                //healing
                p.put("totalHeal", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("totalHeal"));

                //rank last season
                p.put("highestAchievedSeasonTier", m.getJSONArray("participants").getJSONObject(i).get("highestAchievedSeasonTier"));

                //runes
                if (m.getJSONArray("participants").getJSONObject(i).has("runes")) {
                    JSONObject runes = new JSONObject();
                    for (int l = 0; l < m.getJSONArray("participants").getJSONObject(i).getJSONArray("runes").length(); l++) {
                        runes.put("rune" + l, m.getJSONArray("participants").getJSONObject(i).getJSONArray("runes").getJSONObject(l).get("runeId"));
                    }
                    p.put("runes", runes);
                }

                //masteries
                if (m.getJSONArray("participants").getJSONObject(i).has("masteries")) {
                    JSONObject masteries = new JSONObject();
                    for (int k = 0; k < m.getJSONArray("participants").getJSONObject(i).getJSONArray("masteries").length(); k++) {
                        masteries.put("mastery" + k, m.getJSONArray("participants").getJSONObject(i).getJSONArray("masteries").getJSONObject(k).get("masteryId"));
                    }
                    p.put("masteries", masteries);
                }

                gamesArray.put(p);
            }

            //Team 2
            for (int j = 5; j < 10; j++) {

                JSONObject p = new JSONObject();

                //game id
                p.put("gameId ", m.get("gameId"));

                //game duration
                p.put("gameDuration", m.get("gameDuration"));

                //participant id's
                p.put("currentAccountId", m.getJSONArray("participantIdentities").getJSONObject(j).getJSONObject("player").get("currentAccountId"));
                p.put("accountId", m.getJSONArray("participantIdentities").getJSONObject(j).getJSONObject("player").get("accountId"));
                p.put("summonerId", m.getJSONArray("participantIdentities").getJSONObject(j).getJSONObject("player").get("summonerId"));

                //picks and bans
                p.put("champion", m.getJSONArray("participants").getJSONObject(j).get("championId"));
                p.put("lane", m.getJSONArray("participants").getJSONObject(j).getJSONObject("timeline").get("lane"));
                p.put("ban", m.getJSONArray("teams").getJSONObject(1).getJSONArray("bans").getJSONObject(j - 5).getInt("championId"));
                p.put("win", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("win"));

                //k/d/a
                p.put("kills", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("kills"));
                p.put("deaths", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("deaths"));
                p.put("assists", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("assists"));

                //gold and cs
                p.put("goldEarned", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("goldEarned"));
                p.put("cs", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("totalMinionsKilled"));

                //damage dealt and taken
                p.put("totalDamageDealt", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("totalDamageDealtToChampions"));
                p.put("magicDamageDealt", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("magicDamageDealtToChampions"));
                p.put("physicalDamageDealt", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("physicalDamageDealtToChampions"));
                p.put("trueDamageDealt", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("trueDamageDealtToChampions"));

                p.put("damageTaken", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("totalDamageTaken"));

                //summoners
                p.put("summ1", m.getJSONArray("participants").getJSONObject(j).get("spell1Id"));
                p.put("summ2", m.getJSONArray("participants").getJSONObject(j).get("spell2Id"));

                //items
                JSONObject items = new JSONObject();

                items.put("item0", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item0"));
                items.put("item1", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item1"));
                items.put("item2", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item2"));
                items.put("item3", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item3"));
                items.put("item4", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item4"));
                items.put("item5", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item5"));
                items.put("item6", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item6"));

                p.put("items", items);

                //vision
                p.put("wardsPlaced", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("wardsPlaced"));

                //healing
                p.put("totalHeal", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("totalHeal"));

                //rank last season
                p.put("highestAchievedSeasonTier", m.getJSONArray("participants").getJSONObject(j).get("highestAchievedSeasonTier"));

                //runes
                if (m.getJSONArray("participants").getJSONObject(j).has("runes")) {
                    JSONObject runes = new JSONObject();
                    for (int l = 0; l < m.getJSONArray("participants").getJSONObject(j).getJSONArray("runes").length(); l++) {
                        runes.put("rune" + l, m.getJSONArray("participants").getJSONObject(j).getJSONArray("runes").getJSONObject(l).get("runeId"));
                    }
                    p.put("runes", runes);
                }

                //masteries
                if (m.getJSONArray("participants").getJSONObject(j).has("masteries")) {
                    JSONObject masteries = new JSONObject();
                    for (int k = 0; k < m.getJSONArray("participants").getJSONObject(j).getJSONArray("masteries").length(); k++) {
                        masteries.put("mastery" + k, m.getJSONArray("participants").getJSONObject(j).getJSONArray("masteries").getJSONObject(k).get("masteryId"));
                    }
                    p.put("masteries", masteries);
                }
                gamesArray.put(p);
            }
        }
        return gamesArray;
    }

    //participantGameData


    //takes seed data and turns it into rest controller
    //this could be used to turn analysed data from mongo into rest controller
    @RequestMapping("/indi")
    public String indi(Model model) {
        return individualChampionData("https://s3-us-west-1.amazonaws.com/riot-developer-portal/seed-data/matches1.json").toString();
    }

    //Gets first game from first seed data file
    //rest controller containing that game data
    @RequestMapping("/game")
    public String game(Model model){

        SeedAnalyser s = new SeedAnalyser();

        return s.firstGame();
    }
}
