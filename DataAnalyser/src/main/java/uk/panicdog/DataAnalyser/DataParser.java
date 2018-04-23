package uk.panicdog.DataAnalyser;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class DataParser {

    @SuppressWarnings("Duplicates")
    public void toMongo(String url){

        //new localhost mongo connection
        //uses test database
        //uses individualChampionData collection
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        MongoDatabase db = mongoClient.getDatabase("testdb");
        MongoCollection collection = db.getCollection("participantGameData");

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

                Document document = new Document();

                //game id
                document.put("gameId ", m.get("gameId"));

                //game duration
                document.put("gameDuration", m.get("gameDuration"));

                //participant id's
                document.put("currentAccountId", m.getJSONArray("participantIdentities").getJSONObject(i).getJSONObject("player").get("currentAccountId"));
                document.put("accountId", m.getJSONArray("participantIdentities").getJSONObject(i).getJSONObject("player").get("accountId"));
                document.put("summonerId", m.getJSONArray("participantIdentities").getJSONObject(i).getJSONObject("player").get("summonerId"));

                //picks and bans
                document.put("champion", m.getJSONArray("participants").getJSONObject(i).get("championId"));
                document.put("lane", m.getJSONArray("participants").getJSONObject(i).getJSONObject("timeline").get("lane"));
                document.put("role", m.getJSONArray("participants").getJSONObject(i).getJSONObject("timeline").get("role"));
                document.put("ban", m.getJSONArray("teams").getJSONObject(1).getJSONArray("bans").getJSONObject(i).getInt("championId"));
                document.put("win", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("win"));

                //k/d/a
                document.put("kills", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("kills"));
                document.put("deaths", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("deaths"));
                document.put("assists", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("assists"));

                //gold and cs
                document.put("goldEarned", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("goldEarned"));
                document.put("cs", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("totalMinionsKilled"));

                //damage dealt and taken
                document.put("totalDamageDealt", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("totalDamageDealtToChampions"));
                document.put("magicDamageDealt", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("magicDamageDealtToChampions"));
                document.put("physicalDamageDealt", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("physicalDamageDealtToChampions"));
                document.put("trueDamageDealt", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("trueDamageDealtToChampions"));

                document.put("damageTaken", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("totalDamageTaken"));

                //summoners
                document.put("summ1", m.getJSONArray("participants").getJSONObject(i).get("spell1Id"));
                document.put("summ2", m.getJSONArray("participants").getJSONObject(i).get("spell2Id"));

                //items
                Document items = new Document();

                items.put("item0", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item0"));
                items.put("item1", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item1"));
                items.put("item2", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item2"));
                items.put("item3", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item3"));
                items.put("item4", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item4"));
                items.put("item5", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item5"));
                items.put("item6", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item6"));

                document.put("items", items);

                //vision
                document.put("wardsPlaced", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("wardsPlaced"));

                //healing
                document.put("totalHeal", m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("totalHeal"));

                //rank last season
                document.put("highestAchievedSeasonTier", m.getJSONArray("participants").getJSONObject(i).get("highestAchievedSeasonTier"));

                //runes
                if (m.getJSONArray("participants").getJSONObject(i).has("runes")) {
                    Document runes = new Document();
                    for (int l = 0; l < m.getJSONArray("participants").getJSONObject(i).getJSONArray("runes").length(); l++) {
                        runes.put("rune" + l, m.getJSONArray("participants").getJSONObject(i).getJSONArray("runes").getJSONObject(l).get("runeId"));
                    }
                    document.put("runes", runes);
                }

                //masteries
                if (m.getJSONArray("participants").getJSONObject(i).has("masteries")) {
                    Document masteries = new Document();
                    for (int k = 0; k < m.getJSONArray("participants").getJSONObject(i).getJSONArray("masteries").length(); k++) {
                        masteries.put("mastery" + k, m.getJSONArray("participants").getJSONObject(i).getJSONArray("masteries").getJSONObject(k).get("masteryId"));
                    }
                    document.put("masteries", masteries);
                }

                //insert
                collection.insertOne(document);

                //end for
            }

            //Team 2
            for (int j = 5; j < 10; j++) {

                Document document = new Document();

                //game id
                document.put("gameId ", m.get("gameId"));

                //game duration
                document.put("gameDuration", m.get("gameDuration"));

                //participant id's
                document.put("currentAccountId", m.getJSONArray("participantIdentities").getJSONObject(j).getJSONObject("player").get("currentAccountId"));
                document.put("accountId", m.getJSONArray("participantIdentities").getJSONObject(j).getJSONObject("player").get("accountId"));
                document.put("summonerId", m.getJSONArray("participantIdentities").getJSONObject(j).getJSONObject("player").get("summonerId"));

                //picks and bans
                document.put("champion", m.getJSONArray("participants").getJSONObject(j).get("championId"));
                document.put("lane", m.getJSONArray("participants").getJSONObject(j).getJSONObject("timeline").get("lane"));
                document.put("role", m.getJSONArray("participants").getJSONObject(j).getJSONObject("timeline").get("role"));
                document.put("ban", m.getJSONArray("teams").getJSONObject(1).getJSONArray("bans").getJSONObject(j - 5).getInt("championId"));
                document.put("win", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("win"));

                //k/d/a
                document.put("kills", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("kills"));
                document.put("deaths", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("deaths"));
                document.put("assists", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("assists"));

                //gold and cs
                document.put("goldEarned", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("goldEarned"));
                document.put("cs", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("totalMinionsKilled"));

                //damage dealt and taken
                document.put("totalDamageDealt", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("totalDamageDealtToChampions"));
                document.put("magicDamageDealt", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("magicDamageDealtToChampions"));
                document.put("physicalDamageDealt", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("physicalDamageDealtToChampions"));
                document.put("trueDamageDealt", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("trueDamageDealtToChampions"));

                document.put("damageTaken", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("totalDamageTaken"));

                //summoners
                document.put("summ1", m.getJSONArray("participants").getJSONObject(j).get("spell1Id"));
                document.put("summ2", m.getJSONArray("participants").getJSONObject(j).get("spell2Id"));

                //items
                Document items = new Document();

                items.put("item0", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item0"));
                items.put("item1", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item1"));
                items.put("item2", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item2"));
                items.put("item3", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item3"));
                items.put("item4", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item4"));
                items.put("item5", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item5"));
                items.put("item6", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item6"));

                document.put("items", items);

                //vision
                document.put("wardsPlaced", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("wardsPlaced"));

                //healing
                document.put("totalHeal", m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("totalHeal"));

                //rank last season
                document.put("highestAchievedSeasonTier", m.getJSONArray("participants").getJSONObject(j).get("highestAchievedSeasonTier"));

                //runes
                if (m.getJSONArray("participants").getJSONObject(j).has("runes")) {
                    Document runes = new Document();
                    for (int l = 0; l < m.getJSONArray("participants").getJSONObject(j).getJSONArray("runes").length(); l++) {
                        runes.put("rune" + l, m.getJSONArray("participants").getJSONObject(j).getJSONArray("runes").getJSONObject(l).get("runeId"));
                    }
                    document.put("runes", runes);
                }

                //masteries
                if (m.getJSONArray("participants").getJSONObject(j).has("masteries")) {
                    Document masteries = new Document();
                    for (int k = 0; k < m.getJSONArray("participants").getJSONObject(j).getJSONArray("masteries").length(); k++) {
                        masteries.put("mastery" + k, m.getJSONArray("participants").getJSONObject(j).getJSONArray("masteries").getJSONObject(k).get("masteryId"));
                    }
                    document.put("masteries", masteries);
                }

                //insert
                collection.insertOne(document);

                //end for
            }
        }//end for

        System.out.println("Games inserted");
        mongoClient.close();
    }//end void

}
