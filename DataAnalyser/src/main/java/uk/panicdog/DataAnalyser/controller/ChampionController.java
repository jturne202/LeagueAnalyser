package uk.panicdog.DataAnalyser.controller;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.panicdog.DataAnalyser.JSONReader;

import java.io.IOException;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;

@Controller
public class ChampionController {

    //JSONReader class to read json objects and arrays
    private JSONReader jr = new JSONReader();
    private String key = "<key>";

    //Get all champion stats for a given champion from the static data api ie "Jax"
    //*WARNING* RATE LIMITED
    public JSONObject championStaticStats(String champion) {

        JSONObject staticData = null;
        try {
            staticData = jr.readJsonObjectFromUrl("https://euw1.api.riotgames.com/lol/static-data/v3/champions?locale=en_US&tags=all&dataById=false&api_key=" + key);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jax = staticData.getJSONObject("data").getJSONObject(champion);

        return jax;
    }

    //Get current patch version ie "8.2.1"
    //commented code is rate limited
    private String getCurrentVersion() {

        //String currentVersion = null;
        //
        //try {
        //    JSONArray jsonArray = jr.readJSonArrayFromUrl("https://euw1.api.riotgames.com/lol/static-data/v3/versions?api_key=" + key);
        //    currentVersion = jsonArray.getString(0);
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
        //return currentVersion;

        return "7.17.2";
    }

    private JSONObject getAllDdragonChampionData(String champion) {
        JSONObject ddragonData = null;
        try {
            ddragonData = jr.readJsonObjectFromUrl("http://ddragon.leagueoflegends.com/cdn/6.24.1/data/en_US/champion/" + champion + ".json");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ddragonData;
    }

    private int getChampionKey(String champion) {

        int key = Integer.parseInt(getAllDdragonChampionData(champion).getJSONObject("data").getJSONObject(champion).getString("key"));

        return key;
    }

    private String getChampionNameAndTitle(String champion) {

        String name = getAllDdragonChampionData(champion).getJSONObject("data").getJSONObject(champion).getString("name");
        String title = getAllDdragonChampionData(champion).getJSONObject("data").getJSONObject(champion).getString("title");

        return name + " - " + title;
    }

    //Get champion picture for a given champion ie "Jax"
    private String getChampionStaticImage(String champion) {
        return "https://ddragon.leagueoflegends.com/cdn/" + getCurrentVersion() + "/img/champion/" + champion + ".png";
    }

    public int getChampionBaseStatsHP(String champion) {
        int hp = getAllDdragonChampionData(champion).getJSONObject("data").getJSONObject(champion).getJSONObject("stats").getInt("hp");
        return hp;
    }

    private ArrayList<JSONObject> mongoData(){
        //new localhost mongo connection
        //uses test database
        //uses staticChampionData collection
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        MongoDatabase db = mongoClient.getDatabase("testdb");
        MongoCollection collection = db.getCollection("staticChampionData");

        ArrayList<JSONObject> c = new ArrayList<JSONObject>();
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                JSONObject jsonObject = new JSONObject(cursor.next().toJson());
                c.add(jsonObject);
            }
        } finally {
            cursor.close();
        }

        System.out.println(c);
        //return collection.find().first().toString();
        return c;
    }

    //https://stackoverflow.com/questions/10948348/does-java-have-a-data-structure-that-stores-key-value-pairs-equivalent-to-idict?rq=1

    private HashMap<String, Double> getChampionBaseStats(String champion) {
        HashMap<String, Double> baseStats = new HashMap<>();

        JSONObject dd = getAllDdragonChampionData(champion);

        baseStats.put("hp", dd.getJSONObject("data").getJSONObject(champion).getJSONObject("stats").getDouble("hp"));
        baseStats.put("hpperlevel", dd.getJSONObject("data").getJSONObject(champion).getJSONObject("stats").getDouble("hpperlevel"));
        baseStats.put("hpregen", dd.getJSONObject("data").getJSONObject(champion).getJSONObject("stats").getDouble("hpregen"));
        baseStats.put("hpregenperlevel", dd.getJSONObject("data").getJSONObject(champion).getJSONObject("stats").getDouble("hpregenperlevel"));
        baseStats.put("mp", dd.getJSONObject("data").getJSONObject(champion).getJSONObject("stats").getDouble("mp"));
        baseStats.put("mpperlevel", dd.getJSONObject("data").getJSONObject(champion).getJSONObject("stats").getDouble("mpperlevel"));
        baseStats.put("attackdamage", dd.getJSONObject("data").getJSONObject(champion).getJSONObject("stats").getDouble("attackdamage"));
        baseStats.put("attackdamageperlevel", dd.getJSONObject("data").getJSONObject(champion).getJSONObject("stats").getDouble("attackdamageperlevel"));
        baseStats.put("attackspeed", (0.640 + dd.getJSONObject("data").getJSONObject(champion).getJSONObject("stats").getDouble("attackspeedoffset")));
        baseStats.put("attackspeedperlevel", dd.getJSONObject("data").getJSONObject(champion).getJSONObject("stats").getDouble("attackspeedperlevel"));
        baseStats.put("armor", dd.getJSONObject("data").getJSONObject(champion).getJSONObject("stats").getDouble("armor"));
        baseStats.put("armorperlevel", dd.getJSONObject("data").getJSONObject(champion).getJSONObject("stats").getDouble("armorperlevel"));
        baseStats.put("magicresist", dd.getJSONObject("data").getJSONObject(champion).getJSONObject("stats").getDouble("spellblock"));
        baseStats.put("magicresistperlevel", dd.getJSONObject("data").getJSONObject(champion).getJSONObject("stats").getDouble("spellblockperlevel"));
        baseStats.put("movespeed", dd.getJSONObject("data").getJSONObject(champion).getJSONObject("stats").getDouble("movespeed"));

        return baseStats;
    }

    private HashMap<String, Double> getAverageStats(String champion, String query, MongoCollection collection) {
        HashMap<String, Double> stats = new HashMap<>();

        FindIterable<Document> cursor = collection.find(eq("champion", getChampionKey(champion)));
        //.projection(fields(include("kills"), excludeId()))

        for (Document doc: cursor) {

            Document d = doc.get(query, Document.class);

            stats.put("Minimum", Double.parseDouble(d.get("min").toString()));
            stats.put("Maximum", Double.parseDouble(d.get("max").toString()));
            stats.put("Mean", Double.parseDouble(d.get("mean").toString()));
            stats.put("Median", Double.parseDouble(d.get("median").toString()));
            stats.put("Standard Deviation", Double.parseDouble(d.get("standardDeviation").toString()));
        }

        return stats;
    }

    private HashMap<String, String> getKDAHistogram(String champion, String query, MongoCollection collection) {

        List<String> list = null;

        FindIterable<Document> cursor = collection.find(eq("champion", getChampionKey(champion)));
        //.projection(fields(include("kills"), excludeId()))

        for (Document doc: cursor) {
            Document d = doc.get(query, Document.class);
            list = (List<String>)d.get("frequency");
        }

        HashMap<String, String> hm = new HashMap<>();
        //for (String element : list) {
        for (int i = 0; i < list.size(); i++) {
            String[] split = list.get(i).split(":");
            hm.put(String.format("%02d", Integer.parseInt(split[0].trim())), split[1].trim());
        }

        HashMap sortedHashMap = new LinkedHashMap();

        Map<String, String> sorted = new TreeMap<String, String>(hm);
        Set set = sorted.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }


        return sortedHashMap;
    }

    private List<String> getHistogram(String champion, String query, MongoCollection collection) {

        List<String> list = null;

        FindIterable<Document> cursor = collection.find(eq("champion", getChampionKey(champion)));
        //.projection(fields(include("kills"), excludeId()))

        for (Document doc: cursor) {
            Document d = doc.get(query, Document.class);
            list = (List<String>)d.get("frequency");
        }

        return list;
    }

    private int getWinLoss(String champion, String query1, String query2, MongoCollection collection){

        int i = 0;

        FindIterable<Document> cursor = collection.find(eq("champion", getChampionKey(champion)));

        for (Document doc: cursor) {
            Document w = doc.get(query1, Document.class);
            i = (Integer) w.get(query2);
        }

        return i;
    }


    //Add attributes for html
    @RequestMapping("/champion")
    public String champion(Model model, @RequestParam("champ")String champ) {

        //Champion name and title
        model.addAttribute("nameAndTitle", getChampionNameAndTitle(champ));

        //Champion Image
        model.addAttribute("championStaticImage", getChampionStaticImage(champ));

        //Champion Base stats
        HashMap<String, Double> baseStats = getChampionBaseStats(champ);
        model.addAttribute("hp", baseStats.get("hp"));
        model.addAttribute("hpperlevel", baseStats.get("hpperlevel"));
        model.addAttribute("hpregen", baseStats.get("hpregen"));
        model.addAttribute("hpregenperlevel", baseStats.get("hpregenperlevel"));
        model.addAttribute("mp", baseStats.get("mp"));
        model.addAttribute("mpperlevel", baseStats.get("mpperlevel"));
        model.addAttribute("attackdamage", baseStats.get("attackdamage"));
        model.addAttribute("attackdamageperlevel", baseStats.get("attackdamageperlevel"));
        model.addAttribute("attackspeed", baseStats.get("attackspeed"));
        model.addAttribute("attackspeedperlevel", baseStats.get("attackspeedperlevel"));
        model.addAttribute("armor", baseStats.get("armor"));
        model.addAttribute("armorperlevel", baseStats.get("armorperlevel"));
        model.addAttribute("magicresist", baseStats.get("magicresist"));
        model.addAttribute("magicresistperlevel", baseStats.get("magicresistperlevel"));
        model.addAttribute("movespeed", baseStats.get("movespeed"));

        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        MongoDatabase db = mongoClient.getDatabase("testdb");
        MongoCollection collection = db.getCollection("analysedGameData");

        HashMap<String, Double> averageStats;

        //Champion Analysed Average Stats
        int wins = getWinLoss(champ, "wins", "totalWins", collection);
        model.addAttribute("wins", wins);
        model.addAttribute("winsFrequency", getHistogram(champ, "wins", collection));

        int losses = getWinLoss(champ, "losses", "totalLosses", collection);
        model.addAttribute("losses", losses);
        model.addAttribute("lossesFrequency", getHistogram(champ, "losses", collection));

        averageStats = getAverageStats(champ, "kills", collection);
        /*model.addAttribute("minK", averageStats.get("min"));
        model.addAttribute("maxK", averageStats.get("max"));
        model.addAttribute("meanK",averageStats.get("mean"));
        model.addAttribute("medianK", averageStats.get("median"));
        model.addAttribute("standardDeviationK", averageStats.get("standardDeviation"));*/
        model.addAttribute("killsStats", averageStats);
        model.addAttribute("killsFrequency", getKDAHistogram(champ, "kills", collection));

        averageStats = getAverageStats(champ, "deaths", collection);
        /*model.addAttribute("minD", averageStats.get("min"));
        model.addAttribute("maxD", averageStats.get("max"));
        model.addAttribute("meanD",averageStats.get("mean"));
        model.addAttribute("medianD", averageStats.get("median"));
        model.addAttribute("standardDeviationD", averageStats.get("standardDeviation"));*/
        model.addAttribute("deathsStats", averageStats);
        model.addAttribute("deathsFrequency", getKDAHistogram(champ, "deaths", collection));

        averageStats = getAverageStats(champ, "assists", collection);
        /*model.addAttribute("minA", averageStats.get("min"));
        model.addAttribute("maxA", averageStats.get("max"));
        model.addAttribute("meanA",averageStats.get("mean"));
        model.addAttribute("medianA", averageStats.get("median"));
        model.addAttribute("standardDeviationA", averageStats.get("standardDeviation"));*/
        model.addAttribute("assistStats", averageStats);
        model.addAttribute("assistsFrequency", getKDAHistogram(champ, "assists", collection));

        averageStats = getAverageStats(champ, "cs", collection);
        /*model.addAttribute("minC", averageStats.get("min"));
        model.addAttribute("maxC", averageStats.get("max"));
        model.addAttribute("meanC",averageStats.get("mean"));
        model.addAttribute("medianC", averageStats.get("median"));
        model.addAttribute("standardDeviationC", averageStats.get("standardDeviation"));*/
        model.addAttribute("csStats", averageStats);
        model.addAttribute("csFrequency", getHistogram(champ, "cs", collection));

        averageStats = getAverageStats(champ, "totalDamageDealt", collection);
        /*model.addAttribute("minDD", averageStats.get("min"));
        model.addAttribute("maxDD", averageStats.get("max"));
        model.addAttribute("meanDD",averageStats.get("mean"));
        model.addAttribute("medianDD", averageStats.get("median"));
        model.addAttribute("standardDeviationDD", averageStats.get("standardDeviation"));*/
        model.addAttribute("totalDamageDealtStats", averageStats);
        model.addAttribute("totalDamageDealtFrequency", getHistogram(champ, "totalDamageDealt", collection));

        averageStats = getAverageStats(champ, "physicalDamageDealt", collection);
        /*model.addAttribute("minP", averageStats.get("min"));
        model.addAttribute("maxP", averageStats.get("max"));
        model.addAttribute("meanP",averageStats.get("mean"));
        model.addAttribute("medianP", averageStats.get("median"));
        model.addAttribute("standardDeviationP", averageStats.get("standardDeviation"));*/
        model.addAttribute("physicalDamageDealtStats", averageStats);
        model.addAttribute("physicalDamageDealtFrequency", getHistogram(champ, "physicalDamageDealt", collection));

        averageStats = getAverageStats(champ, "magicDamageDealt", collection);
        /*model.addAttribute("minM", averageStats.get("min"));
        model.addAttribute("maxM", averageStats.get("max"));
        model.addAttribute("meanM",averageStats.get("mean"));
        model.addAttribute("medianM", averageStats.get("median"));
        model.addAttribute("standardDeviationM", averageStats.get("standardDeviation"));*/
        model.addAttribute("magicDamageDealtStats", averageStats);
        model.addAttribute("magicDamageDealtFrequency", getHistogram(champ, "magicDamageDealt", collection));

        averageStats = getAverageStats(champ, "trueDamageDealt", collection);
        /*model.addAttribute("minT", averageStats.get("min"));
        model.addAttribute("maxT", averageStats.get("max"));
        model.addAttribute("meanT",averageStats.get("mean"));
        model.addAttribute("medianT", averageStats.get("median"));
        model.addAttribute("standardDeviationT", averageStats.get("standardDeviation"));*/
        model.addAttribute("trueDealtStats", averageStats);
        model.addAttribute("trueDamageDealtFrequency", getHistogram(champ, "trueDamageDealt", collection));

        averageStats = getAverageStats(champ, "totalDamageTaken", collection);
        /*model.addAttribute("minTD", averageStats.get("min"));
        model.addAttribute("maxTD", averageStats.get("max"));
        model.addAttribute("meanTD",averageStats.get("mean"));
        model.addAttribute("medianTD", averageStats.get("median"));
        model.addAttribute("standardDeviationTD", averageStats.get("standardDeviation"));*/
        model.addAttribute("totalDamageTakenStats", averageStats);
        model.addAttribute("totalDamageTakenFrequency", getHistogram(champ, "totalDamageTaken", collection));

        /*model.addAttribute("killsFrequency", getHistogram(champ, "kills", collection));
        model.addAttribute("deathsFrequency", getHistogram(champ, "deaths", collection));
        model.addAttribute("assistsFrequency", getHistogram(champ, "assists", collection));
        model.addAttribute("csFrequency", getHistogram(champ, "cs", collection));
        model.addAttribute("totalDamageDealtFrequency", getHistogram(champ, "totalDamageDealt", collection));
        model.addAttribute("physicalDamageDealtFrequency", getHistogram(champ, "physicalDamageDealt", collection));
        model.addAttribute("magicDamageDealtFrequency", getHistogram(champ, "magicDamageDealt", collection));
        model.addAttribute("trueDamageDealtFrequency", getHistogram(champ, "trueDamageDealt", collection));
        model.addAttribute("totalDamageTakenFrequency", getHistogram(champ, "totalDamageTaken", collection));*/

        return "champion";
    }
}
