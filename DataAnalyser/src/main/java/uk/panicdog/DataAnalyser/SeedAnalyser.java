package uk.panicdog.DataAnalyser;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import static com.mongodb.client.model.Filters.eq;

public class SeedAnalyser {

    private JSONReader jr = new JSONReader();

    private double getMean(ArrayList<Integer> stat){
        int sum = 0;

        for (Integer aStat : stat) sum += aStat;

        return (double) sum / (double) stat.size();
    }

    private int getMedian(ArrayList<Integer> stat){
        Collections.sort(stat);

        if (stat.size() % 2 == 0)
            return (stat.get(stat.size() / 2) + stat.get(stat.size() / 2 - 1)) / 2;
        else
            return stat.get(stat.size() / 2);
    }

    private double getStandardDeviation(ArrayList<Integer> stat) {
        double mean = getMean(stat);
        double total = 0;

        for (Integer aStat : stat) {
            int val = aStat;
            double sqrDiffToMean = Math.pow(val - mean, 2);
            total += sqrDiffToMean;
        }

        double meanOfDiffs = total / (double) (stat.size());
        return Math.sqrt(meanOfDiffs);
    }

    private ArrayList<String> getHistogramKDA(ArrayList<Integer> stat) {
        ArrayList<String> kda = new ArrayList<>();

        for (int i = 0; i < stat.size(); i++) {
            if (stat.contains(i)) {
                kda.add(i + " : " + toStars(Collections.frequency(stat, i)));
            }
        }
        return kda;
    }

    private String toStars(int number) {
        StringBuilder temp = new StringBuilder();
        for(int i=0;i<number;i++) {
            temp.append("*");
        }
        return temp.toString();
    }

    private JSONObject getChampionDdragonData() {
        JSONObject ddragonData = null;
        try {
            ddragonData = jr.readJsonObjectFromUrl("http://ddragon.leagueoflegends.com/cdn/7.17.2/data/en_US/champion.json");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ddragonData;
    }

    private JSONObject championDdragonData = getChampionDdragonData();

    private ArrayList<String> getHistogramDamage(ArrayList<Integer> stat) {

        ArrayList<Integer> damage1 = new ArrayList<>();
        ArrayList<Integer> damage2 = new ArrayList<>();
        ArrayList<Integer> damage3 = new ArrayList<>();
        ArrayList<Integer> damage4 = new ArrayList<>();
        ArrayList<Integer> damage5 = new ArrayList<>();
        ArrayList<Integer> damage6 = new ArrayList<>();
        ArrayList<Integer> damage7 = new ArrayList<>();
        ArrayList<Integer> damage8 = new ArrayList<>();
        ArrayList<Integer> damage9 = new ArrayList<>();
        ArrayList<Integer> damage10 = new ArrayList<>();
        ArrayList<Integer> damagem10 = new ArrayList<>();

        for (int i = 0; i < stat.size(); i++) {
            if (stat.get(i) < 2000){
                damage1.add(stat.get(i));
            } else if (stat.get(i) < 10000) {
                damage2.add(stat.get(i));
            } else if (stat.get(i) < 15000) {
                damage3.add(stat.get(i));
            } else if (stat.get(i) < 20000) {
                damage4.add(stat.get(i));
            } else if (stat.get(i) < 25000) {
                damage5.add(stat.get(i));
            } else if (stat.get(i) < 30000) {
                damage6.add(stat.get(i));
            } else if (stat.get(i) < 35000) {
                damage7.add(stat.get(i));
            } else if (stat.get(i) < 40000) {
                damage8.add(stat.get(i));
            } else if (stat.get(i) < 45000) {
                damage9.add(stat.get(i));
            } else if (stat.get(i) < 50000) {
                damage10.add(stat.get(i));
            } else if (stat.get(i) > 50000) {
                damagem10.add(stat.get(i));
            }
        }

        ArrayList<String> damage = new ArrayList<>();

        damage.add("Less than 2k damage: " + toStars(damage1.size()));
        damage.add("Between 2k and 10k damage: " + toStars(damage2.size()));
        damage.add("Between 10k and 15k damage: " + toStars(damage3.size()));
        damage.add("Between 15k and 20k damage: " + toStars(damage4.size()));
        damage.add("Between 20k and 25k damage: " + toStars(damage5.size()));
        damage.add("Between 25k and 30k damage: " + toStars(damage6.size()));
        damage.add("Between 30k and 35k damage: " + toStars(damage7.size()));
        damage.add("Between 35k and 40k damage: " + toStars(damage8.size()));
        damage.add("Between 40k and 45k damage: " + toStars(damage9.size()));
        damage.add("Between 45k and 50k damage: " + toStars(damage10.size()));
        damage.add("More than 50k damage: " + toStars(damagem10.size()));

        return damage;
    }

    private ArrayList<String> getHistogramTrueDamage(ArrayList<Integer> stat) {
        ArrayList<Integer> damage1 = new ArrayList<>();
        ArrayList<Integer> damage2 = new ArrayList<>();
        ArrayList<Integer> damage3 = new ArrayList<>();
        ArrayList<Integer> damage4 = new ArrayList<>();
        ArrayList<Integer> damagem4 = new ArrayList<>();


        for (int i = 0; i < stat.size(); i++) {
            if (stat.get(i) < 1000) {
                damage1.add(stat.get(i));
            } else if (stat.get(i) < 3000) {
                damage2.add(stat.get(i));
            } else if (stat.get(i) < 6000) {
                damage3.add(stat.get(i));
            } else if (stat.get(i) < 9000) {
                damage4.add(stat.get(i));
            } else if (stat.get(i) > 9000) {
                damagem4.add(stat.get(i));
            }
        }

        ArrayList<String> damage = new ArrayList<>();

        damage.add("Less than 1k true damage: " + toStars(damage1.size()));
        damage.add("Between 1k and 3k true damage: " + toStars(damage2.size()));
        damage.add("Between 3k and 6k true damage: " + toStars(damage3.size()));
        damage.add("Between 6k and 9k true damage: " + toStars(damage4.size()));
        damage.add("More than 9k true damage: " + toStars(damagem4.size()));

        return damage;
    }

    private ArrayList<String> getHistogramCs(ArrayList<Integer> cs) {

        ArrayList<Integer> cs000 = new ArrayList<>();
        ArrayList<Integer> cs050 = new ArrayList<>();
        ArrayList<Integer> cs100 = new ArrayList<>();
        ArrayList<Integer> cs150 = new ArrayList<>();
        ArrayList<Integer> cs200 = new ArrayList<>();
        ArrayList<Integer> cs250 = new ArrayList<>();
        ArrayList<Integer> cs300 = new ArrayList<>();

        for (int i = 0; i < cs.size(); i++) {

            if (cs.get(i) < 50){
                cs000.add(cs.get(i));
            } else if (cs.get(i) < 100) {
                cs050.add(cs.get(i));
            } else if (cs.get(i) < 150) {
                cs100.add(cs.get(i));
            } else if (cs.get(i) < 200) {
                cs150.add(cs.get(i));
            } else if (cs.get(i) < 250) {
                cs200.add(cs.get(i));
            } else if (cs.get(i) < 300) {
                cs250.add(cs.get(i));
            } else if (cs.get(i) > 300) {
                cs300.add(cs.get(i));
            }
        }

        ArrayList<String> minions = new ArrayList<>();

        minions.add("Less than 50 cs: " + toStars(cs000.size()));
        minions.add("Between 50 and 100 cs: " + toStars(cs050.size()));
        minions.add("Between 100 and 150 cs: " + toStars(cs100.size()));
        minions.add("Between 150 and 200 cs: " + toStars(cs150.size()));
        minions.add("Between 200 and 250 cs: " + toStars(cs200.size()));
        minions.add("Between 250 and 300: " + toStars(cs250.size()));
        minions.add("More than 300 cs: " + toStars(cs300.size()));

        return minions;
    }

    private ArrayList gameHistogramTime(ArrayList<Integer> stat, String winLoss){
        ArrayList<Integer> timeLessThan20 = new ArrayList<>();
        ArrayList<Integer> timeLessThan25 = new ArrayList<>();
        ArrayList<Integer> timeLessThan30 = new ArrayList<>();
        ArrayList<Integer> timeLessThan35 = new ArrayList<>();
        ArrayList<Integer> timeLessThan40 = new ArrayList<>();
        ArrayList<Integer> timeLessThan45 = new ArrayList<>();
        ArrayList<Integer> timeMoreThan45 = new ArrayList<>();

        for (int i = 0; i < stat.size(); i++) {

            if (stat.get(i) < 1200) {
                timeLessThan20.add((stat.get(i)));
            } else if (stat.get(i) < 1500) {
                timeLessThan25.add((stat.get(i)));
            } else if (stat.get(i) < 1800) {
                timeLessThan30.add((stat.get(i)));
            } else if (stat.get(i) < 2100) {
                timeLessThan35.add((stat.get(i)));
            } else if (stat.get(i) < 2400) {
                timeLessThan40.add((stat.get(i)));
            } else if (stat.get(i) < 2700) {
                timeLessThan45.add((stat.get(i)));
            } else if (stat.get(i) > 2700) {
                timeMoreThan45.add((stat.get(i)));
            }
        }

        ArrayList<String> frequency = new ArrayList<>();

        frequency.add(winLoss + " time less than 20 minutes: " + toStars(timeLessThan20.size()));
        frequency.add(winLoss + " time between 20 and 25 minutes: " + toStars(timeLessThan25.size()));
        frequency.add(winLoss + " time between 25 and 30 minutes: " + toStars(timeLessThan30.size()));
        frequency.add(winLoss + " time between 30 and 35 minutes: " + toStars(timeLessThan35.size()));
        frequency.add(winLoss + " time between 35 and 40 minutes: " + toStars(timeLessThan40.size()));
        frequency.add(winLoss + " time between 40 and 45 minutes: " + toStars(timeLessThan45.size()));
        frequency.add(winLoss + " time more than 45 minutes: " + toStars(timeMoreThan45.size()));

        return frequency;
    }

    //testing
    public void printStats(String statName, ArrayList<Integer> statList){
        if (!statList.isEmpty()) {
            //System.out.println(statName + " count: " + statList.size());
            System.out.println(statName + " min: " + Collections.min(statList));
            System.out.println(statName + " max: " + Collections.max(statList));
            System.out.println(statName + " mean: " + getMean(statList));
            System.out.println(statName + " median: " + getMedian(statList));
            System.out.println(statName + " standard deviation: " + getStandardDeviation(statList));
        }
    }

    //analyse
    public void analyse (){

        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        MongoDatabase db = mongoClient.getDatabase("testdb");


        JSONObject championList = new JSONObject();
        JSONReader jr = new JSONReader();

        try {
            championList = jr.readJsonObjectFromUrl("http://ddragon.leagueoflegends.com/cdn/6.24.1/data/en_US/champion.json");
            //x = m.get("gameId").toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<String> keys = championList.getJSONObject("data").keys();

        for (int x = 0; x < championList.getJSONObject("data").length(); x++) {

            int count = 0;

            int champion = 0;
            boolean win = false;

            ArrayList<Integer> winTime = new ArrayList<>();
            ArrayList<Integer> lossTime = new ArrayList<>();
            ArrayList<Integer> kills = new ArrayList<>();
            ArrayList<Integer> deaths = new ArrayList<>();
            ArrayList<Integer> assists = new ArrayList<>();
            ArrayList<Integer> cs = new ArrayList<>();
            ArrayList<Integer> totalDamageDealt = new ArrayList<>();
            ArrayList<Integer> physicalDamageDealt = new ArrayList<>();
            ArrayList<Integer> magicDamageDealt = new ArrayList<>();
            ArrayList<Integer> trueDamageDealt = new ArrayList<>();
            ArrayList<Integer> totalDamageTaken = new ArrayList<>();

            MongoCollection collection = db.getCollection("participantGameData");
            MongoCursor<Document> cursor = collection.find(eq("champion", championList.getJSONObject("data").getJSONObject(keys.next()).getInt("key"))).iterator();

            try {
                while (cursor.hasNext()) {

                    Document document = cursor.next();

                    champion = document.getInteger("champion");

                    if (document.getBoolean("win")){
                        win = true;
                        winTime.add(document.getInteger("gameDuration"));
                    } else {
                        lossTime.add(document.getInteger("gameDuration"));
                    }

                    kills.add(document.getInteger("kills"));
                    deaths.add(document.getInteger("deaths"));
                    assists.add(document.getInteger("assists"));
                    cs.add(document.getInteger("cs"));
                    totalDamageDealt.add(document.getInteger("totalDamageDealt"));
                    physicalDamageDealt.add(document.getInteger("physicalDamageDealt"));
                    magicDamageDealt.add(document.getInteger("magicDamageDealt"));
                    trueDamageDealt.add(document.getInteger("trueDamageDealt"));
                    totalDamageTaken.add(document.getInteger("damageTaken"));

                    count++;
                    //System.out.println(cursor.next().toJson());
                }
            } finally {
                cursor.close();
            }

            collection = db.getCollection("analysedGameData");

            Document analysedData = new Document();

            analysedData.put("champion", champion);
            analysedData.put("count", count);

            Document winDoc = new Document();
            winDoc.put("totalWins", winTime.size());
            winDoc.put("frequency", gameHistogramTime(winTime, "Win"));
            analysedData.put("wins", winDoc);

            Document lossDoc = new Document();
            lossDoc.put("totalLosses", lossTime.size());
            lossDoc.put("frequency", gameHistogramTime(lossTime, "Loss"));
            analysedData.put("losses", lossDoc);

            Document killsDoc = new Document();
            killsDoc.put("min", Collections.min(kills));
            killsDoc.put("max", Collections.max(kills));
            killsDoc.put("mean", getMean(kills));
            killsDoc.put("median", getMedian(kills));
            killsDoc.put("standardDeviation", getStandardDeviation(kills));
            killsDoc.put("frequency", getHistogramKDA(kills));
            analysedData.put("kills", killsDoc);

            Document deathsDoc = new Document();
            deathsDoc.put("min", Collections.min(deaths));
            deathsDoc.put("max", Collections.max(deaths));
            deathsDoc.put("mean", getMean(deaths));
            deathsDoc.put("median", getMedian(deaths));
            deathsDoc.put("standardDeviation", getStandardDeviation(deaths));
            deathsDoc.put("frequency", getHistogramKDA(deaths));
            analysedData.put("deaths", deathsDoc);

            Document assistsDoc = new Document();
            assistsDoc.put("min", Collections.min(assists));
            assistsDoc.put("max", Collections.max(assists));
            assistsDoc.put("mean", getMean(assists));
            assistsDoc.put("median", getMedian(assists));
            assistsDoc.put("standardDeviation", getStandardDeviation(assists));
            assistsDoc.put("frequency", getHistogramKDA(assists));
            analysedData.put("assists", assistsDoc);

            Document csDoc = new Document();
            csDoc.put("min", Collections.min(cs));
            csDoc.put("max", Collections.max(cs));
            csDoc.put("mean", getMean(cs));
            csDoc.put("median", getMedian(cs));
            csDoc.put("standardDeviation", getStandardDeviation(cs));
            csDoc.put("frequency", getHistogramCs(cs));
            analysedData.put("cs", csDoc);

            Document totalDamageDealtDoc = new Document();
            totalDamageDealtDoc.put("min", Collections.min(totalDamageDealt));
            totalDamageDealtDoc.put("max", Collections.max(totalDamageDealt));
            totalDamageDealtDoc.put("mean", getMean(totalDamageDealt));
            totalDamageDealtDoc.put("median", getMedian(totalDamageDealt));
            totalDamageDealtDoc.put("standardDeviation", getStandardDeviation(totalDamageDealt));
            totalDamageDealtDoc.put("frequency", getHistogramDamage(totalDamageDealt));
            analysedData.put("totalDamageDealt", totalDamageDealtDoc);

            Document physicalDamageDealtDoc = new Document();
            physicalDamageDealtDoc.put("min", Collections.min(physicalDamageDealt));
            physicalDamageDealtDoc.put("max", Collections.max(physicalDamageDealt));
            physicalDamageDealtDoc.put("mean", getMean(physicalDamageDealt));
            physicalDamageDealtDoc.put("median", getMedian(physicalDamageDealt));
            physicalDamageDealtDoc.put("standardDeviation", getStandardDeviation(physicalDamageDealt));
            physicalDamageDealtDoc.put("frequency", getHistogramDamage(physicalDamageDealt));
            analysedData.put("physicalDamageDealt", physicalDamageDealtDoc);

            Document magicDamageDealtDoc = new Document();
            magicDamageDealtDoc.put("min", Collections.min(magicDamageDealt));
            magicDamageDealtDoc.put("max", Collections.max(magicDamageDealt));
            magicDamageDealtDoc.put("mean", getMean(magicDamageDealt));
            magicDamageDealtDoc.put("median", getMedian(magicDamageDealt));
            magicDamageDealtDoc.put("standardDeviation", getStandardDeviation(magicDamageDealt));
            magicDamageDealtDoc.put("frequency", getHistogramDamage(magicDamageDealt));
            analysedData.put("magicDamageDealt", magicDamageDealtDoc);

            Document trueDamageDealtDoc = new Document();
            trueDamageDealtDoc.put("min", Collections.min(trueDamageDealt));
            trueDamageDealtDoc.put("max", Collections.max(trueDamageDealt));
            trueDamageDealtDoc.put("mean", getMean(trueDamageDealt));
            trueDamageDealtDoc.put("median", getMedian(trueDamageDealt));
            trueDamageDealtDoc.put("standardDeviation", getStandardDeviation(trueDamageDealt));
            trueDamageDealtDoc.put("frequency", getHistogramTrueDamage(trueDamageDealt));
            analysedData.put("trueDamageDealt", trueDamageDealtDoc);

            Document totalDamageTakenDoc = new Document();
            totalDamageTakenDoc.put("min", Collections.min(totalDamageTaken));
            totalDamageTakenDoc.put("max", Collections.max(totalDamageTaken));
            totalDamageTakenDoc.put("mean", getMean(totalDamageTaken));
            totalDamageTakenDoc.put("median", getMedian(totalDamageTaken));
            totalDamageTakenDoc.put("standardDeviation", getStandardDeviation(totalDamageTaken));
            totalDamageTakenDoc.put("frequency", getHistogramDamage(totalDamageTaken));
            analysedData.put("totalDamageTaken", totalDamageTakenDoc);

            collection.insertOne(analysedData);
        }
        System.out.println("Analysed Game Data Inserted");
    }




    //
    //TESTING
    //

    //Testing getting the data
    public void individualChampionData(String url) {

        JSONObject matches = null;

        try {
            matches = jr.readJsonObjectFromUrl(url);
            //x = m.get("gameId").toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //JSONObject m = (JSONObject) matches.getJSONArray("matches").get(0);

        for (int x = 0; x < matches.getJSONArray("matches").length(); x++) {
            JSONObject m = (JSONObject) matches.getJSONArray("matches").get(x);
            System.out.println("GAME : " + x);

            for (int i = 0; i < 5; i++) {
                System.out.println("PARTICIPANT " + i + ": ");

                System.out.println("champion: " + m.getJSONArray("participants").getJSONObject(i).get("championId"));

                System.out.println("lane: " + m.getJSONArray("participants").getJSONObject(i).getJSONObject("timeline").get("lane"));

                System.out.println("win: " + m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("win"));

                System.out.println("ban: " + m.getJSONArray("teams").getJSONObject(1).getJSONArray("bans").getJSONObject(i).getInt("championId"));

                System.out.println("kills: " + m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("kills"));
                System.out.println("deaths: " + m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("deaths"));
                System.out.println("assists: " + m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("assists"));

                System.out.println("gold earned: " + m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("goldEarned"));

                System.out.println("cs: " + m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("totalMinionsKilled"));

                System.out.println("total damage dealt: " + m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("totalDamageDealtToChampions"));
                System.out.println("magic damage dealt: " + m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("magicDamageDealtToChampions"));
                System.out.println("physical damage dealt: " + m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("physicalDamageDealtToChampions"));
                System.out.println("true damage dealt: " + m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("trueDamageDealtToChampions"));

                System.out.println("damage taken: " + m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("totalDamageTaken"));

                System.out.println("summ 1: " + m.getJSONArray("participants").getJSONObject(i).get("spell1Id"));
                System.out.println("summ 2: " + m.getJSONArray("participants").getJSONObject(i).get("spell2Id"));

                System.out.println("item0: " + m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item0"));
                System.out.println("item1: " + m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item1"));
                System.out.println("item2: " + m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item2"));
                System.out.println("item3: " + m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item3"));
                System.out.println("item4: " + m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item4"));
                System.out.println("item5: " + m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item5"));
                System.out.println("item6: " + m.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").get("item6"));

                if (m.getJSONArray("participants").getJSONObject(i).has("runes")) {
                    for (int l = 0; l < m.getJSONArray("participants").getJSONObject(i).getJSONArray("runes").length(); l++) {
                        System.out.println("rune " + l + ": " + m.getJSONArray("participants").getJSONObject(i).getJSONArray("runes").getJSONObject(l).get("runeId"));
                    }
                }

                if (m.getJSONArray("participants").getJSONObject(i).has("masteries")) {
                    for (int k = 0; k < m.getJSONArray("participants").getJSONObject(i).getJSONArray("masteries").length(); k++) {
                        System.out.println("mastery " + k + ": " + m.getJSONArray("participants").getJSONObject(i).getJSONArray("masteries").getJSONObject(k).get("masteryId"));
                    }
                }

                System.out.println();
            }

            for (int j = 5; j < 10; j++) {
                System.out.println("PARTICIPANT " + j + ": ");

                System.out.println("champion: " + m.getJSONArray("participants").getJSONObject(j).get("championId"));

                System.out.println("lane: " + m.getJSONArray("participants").getJSONObject(j).getJSONObject("timeline").get("lane"));

                System.out.println("win: " + m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("win"));

                System.out.println("ban: " + m.getJSONArray("teams").getJSONObject(1).getJSONArray("bans").getJSONObject(j - 5).getInt("championId"));

                System.out.println("kills: " + m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("kills"));
                System.out.println("deaths: " + m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("deaths"));
                System.out.println("assists: " + m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("assists"));

                System.out.println("gold earned: " + m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("goldEarned"));

                System.out.println("cs: " + m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("totalMinionsKilled"));

                System.out.println("total damage dealt: " + m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("totalDamageDealtToChampions"));
                System.out.println("magic damage dealt: " + m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("magicDamageDealtToChampions"));
                System.out.println("physical damage dealt: " + m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("physicalDamageDealtToChampions"));
                System.out.println("true damage dealt: " + m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("trueDamageDealtToChampions"));

                System.out.println("damage taken: " + m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("totalDamageTaken"));

                System.out.println("summ 1: " + m.getJSONArray("participants").getJSONObject(j).get("spell1Id"));
                System.out.println("summ 2: " + m.getJSONArray("participants").getJSONObject(j).get("spell2Id"));

                System.out.println("item0: " + m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item0"));
                System.out.println("item1: " + m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item1"));
                System.out.println("item2: " + m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item2"));
                System.out.println("item3: " + m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item3"));
                System.out.println("item4: " + m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item4"));
                System.out.println("item5: " + m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item5"));
                System.out.println("item6: " + m.getJSONArray("participants").getJSONObject(j).getJSONObject("stats").get("item6"));

                if (m.getJSONArray("participants").getJSONObject(j).has("runes")) {
                    for (int l = 0; l < m.getJSONArray("participants").getJSONObject(j).getJSONArray("runes").length(); l++) {
                        System.out.println("rune " + l + ": " + m.getJSONArray("participants").getJSONObject(j).getJSONArray("runes").getJSONObject(l).get("runeId"));
                    }
                }

                if (m.getJSONArray("participants").getJSONObject(j).has("masteries")) {
                    for (int k = 0; k < m.getJSONArray("participants").getJSONObject(j).getJSONArray("masteries").length(); k++) {
                        System.out.println("mastery " + k + ": " + m.getJSONArray("participants").getJSONObject(j).getJSONArray("masteries").getJSONObject(k).get("masteryId"));
                    }
                }

                System.out.println();
            }
        }
    }

    //Gets first game from first seed data file
    public String firstGame(){
        JSONObject matches = null;

        try {
            matches = jr.readJsonObjectFromUrl("https://s3-us-west-1.amazonaws.com/riot-developer-portal/seed-data/matches1.json");
            JSONObject m = (JSONObject) matches.getJSONArray("matches").get(0);
            //x = m.get("gameId").toString();

        } catch (IOException e) {
            e.printStackTrace();
        }



        //**Individual Champion Data Analysis**
        //
        //(for checks)
        //accountid:
        //gameid:
        //---
        //champion:
        //win:
        //wintime:
        //lane:
        //ban:
        //<rank>
        //---
        //kills:
        //deaths:
        //assists:
        //goldearned:
        //creepscore:
        //damagedealt:
        //physicaldealt:
        //magicdealt:
        //truedealt:
        //damagetaken:
        //---
        //abilityorder:
        //summonerspells:
        //startitems:
        //finalbuild:
        //runes:
        //masteries:


        //[0].gameId
        //gameId
        //participantIdentities[0].player.accountId

        //participants[0].championId
        //participants[0].stats.win
        //participants[0].stats.win + gameDuration
        //participants[0].timeline.lane
        //teams[0].bans[0].championId

        //participants[0].stats.kills
        //participants[0].stats.deaths
        //participants[0].stats.assists
        //participants[0].stats.goldEarned
        //participants[0].stats.totalMinionsKilled
        //participants[0].stats.totalDamageDealtToChampions
        //participants[0].stats.physicalDamageDealtToChampions
        //participants[0].stats.magicDamageDealtToChampions
        //participants[0].stats.trueDamageDealtToChampions
        //participants[0].stats.totalDamageTaken

        //
        //participants[0].spell1Id, participants[0].spell2Id
        //
        //participants[0].stats.item0, participants[0].stats.item1, participants[0].stats.item2, participants[0].stats.item3, participants[0].stats.item4, participants[0].stats.item5, participants[0].stats.item5, participants[0].stats.item6
        //participants[0].runes[0].runeId, participants[0].runes[1].runeId, participants[0].runes[2].runeId, participants[0].runes[3].runeId
        //participants[0].masteries[0].masteryId, participants[0].masteries[1].masteryId, participants[0].masteries[2].masteryId, participants[0].masteries[3].masteryId, participants[0].masteries[4].masteryId, participants[0].masteries[5].masteryId, participants[0].masteries[6].masteryId, participants[0].masteries[7].masteryId, participants[0].masteries[8].masteryId, participants[0].masteries[9].masteryId


        //rest controller can access from mongodb
        //return nothing.
        /*.get(0).toString()*/
        return(matches.getJSONArray("matches").get(0).toString());

        //return(x);
    }

    //Test insert
    public void indiToMongo(){
        uk.panicdog.DataAnalyser.controller.IndividualChampionData i = new uk.panicdog.DataAnalyser.controller.IndividualChampionData();

        //new localhost mongo connection
        //uses test database
        //uses individualChampionData collection
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        MongoDatabase db = mongoClient.getDatabase("testdb");
        MongoCollection collection = db.getCollection("participantGameData");

        JSONArray j = i.individualChampionData("https://s3-us-west-1.amazonaws.com/riot-developer-portal/seed-data/matches1.json");

        int count = 0;

        for (int x = 0; x < i.individualChampionData("https://s3-us-west-1.amazonaws.com/riot-developer-portal/seed-data/matches1.json").length(); x++) {
            Document document = Document.parse(j.getJSONObject(x).toString());
            collection.insertOne(document);
            count++;
        }

        System.out.println(count);
    }

}
