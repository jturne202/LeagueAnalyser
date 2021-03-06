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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import uk.panicdog.DataAnalyser.JSONReader;
import uk.panicdog.DataAnalyser.model.Search;

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

    private boolean checkExists(String query){
        JSONObject ddragonData = null;
        try {
            ddragonData = jr.readJsonObjectFromUrl("http://ddragon.leagueoflegends.com/cdn/6.24.1/data/en_US/champion/" + query + ".json");
        } catch (JSONException | IOException e) {
            //e.printStackTrace();
            return false;
        }
        return true;
    }

    //Gets JSON Object containing champion data
    private JSONObject getAllDdragonChampionData(String champion) {
        JSONObject ddragonData = null;
        try {
            ddragonData = jr.readJsonObjectFromUrl("http://ddragon.leagueoflegends.com/cdn/6.24.1/data/en_US/champion/" + champion + ".json");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return ddragonData;
    }

    //get champion id
    private int getChampionKey(String champion) {
        return Integer.parseInt(getAllDdragonChampionData(champion).getJSONObject("data").getJSONObject(champion).getString("key"));
    }

    //Creates name and title string for given champion
    private String getChampionNameAndTitle(String champion) {

        String name = getAllDdragonChampionData(champion).getJSONObject("data").getJSONObject(champion).getString("name");
        String title = getAllDdragonChampionData(champion).getJSONObject("data").getJSONObject(champion).getString("title");

        return name + " - " + title;
    }

    //Get champion picture for a given champion ie "Jax"
    private String getChampionStaticImage(String champion) {
        return "https://ddragon.leagueoflegends.com/cdn/" + getCurrentVersion() + "/img/champion/" + champion + ".png";
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

    //Get base stats for given champion
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

    //Gets average stats from mongodb for given champion
    //query = stat wanted e.g. 'kills', 'totalDamageDealt'
    private HashMap<String, Double> getAverageStats(String champion, String query, MongoCollection collection) {
        HashMap<String, Double> stats = new HashMap<>();

        FindIterable<Document> cursor = collection.find(eq("champion", getChampionKey(champion)));
        //.projection(fields(include("kills"), excludeId()))

        for (Document doc: cursor) {

            Document d = doc.get(query, Document.class);

            //stats.put("Minimum", Double.parseDouble(d.get("min").toString()));
            stats.put("Maximum", Double.parseDouble(d.get("max").toString()));
            stats.put("Mean", Double.parseDouble(d.get("mean").toString()));
            stats.put("Median", Double.parseDouble(d.get("median").toString()));
            stats.put("Standard Deviation", Double.parseDouble(d.get("standardDeviation").toString()));
        }

        return stats;
    }

    //retrieves histograms from mongodb where the value is an integer
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

    //gets other histograms from mongodb
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

    //gets total wins or losses
    //query1 can either be 'wins' or 'losses', and query2 can either be 'totalWins' or 'totalLosses' depending on query1
    private int getWinLoss(String champion, String query1, String query2, MongoCollection collection){

        int i = 0;

        FindIterable<Document> cursor = collection.find(eq("champion", getChampionKey(champion)));

        for (Document doc: cursor) {
            Document w = doc.get(query1, Document.class);
            i = (Integer) w.get(query2);
        }

        return i;
    }

    // sets first letter to capital letter and rest to lower case to handle user input for search functionality
    private String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }


    //Add attributes for thymeleaf in the html
    @RequestMapping(value = "/champion")
    public String champion(Model model, @ModelAttribute("search") Search search) {

        if (!checkExists(capitalizeFirstLetter(search.getQuery()))){
            model.addAttribute("notFound", ("Champion \'" + search.getQuery() + "\' not found"));
            return "index";
        }

        String champ = capitalizeFirstLetter(search.getQuery());

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
        model.addAttribute("killsStats", averageStats);
        model.addAttribute("killsFrequency", getKDAHistogram(champ, "kills", collection));

        averageStats = getAverageStats(champ, "deaths", collection);
        model.addAttribute("deathsStats", averageStats);
        model.addAttribute("deathsFrequency", getKDAHistogram(champ, "deaths", collection));

        averageStats = getAverageStats(champ, "assists", collection);
        model.addAttribute("assistStats", averageStats);
        model.addAttribute("assistsFrequency", getKDAHistogram(champ, "assists", collection));

        averageStats = getAverageStats(champ, "cs", collection);
        model.addAttribute("csStats", averageStats);
        model.addAttribute("csFrequency", getHistogram(champ, "cs", collection));

        averageStats = getAverageStats(champ, "totalDamageDealt", collection);
        model.addAttribute("totalDamageDealtStats", averageStats);
        model.addAttribute("totalDamageDealtFrequency", getHistogram(champ, "totalDamageDealt", collection));

        averageStats = getAverageStats(champ, "physicalDamageDealt", collection);
        model.addAttribute("physicalDamageDealtStats", averageStats);
        model.addAttribute("physicalDamageDealtFrequency", getHistogram(champ, "physicalDamageDealt", collection));

        averageStats = getAverageStats(champ, "magicDamageDealt", collection);
        model.addAttribute("magicDamageDealtStats", averageStats);
        model.addAttribute("magicDamageDealtFrequency", getHistogram(champ, "magicDamageDealt", collection));

        averageStats = getAverageStats(champ, "trueDamageDealt", collection);
        model.addAttribute("trueDamageDealtStats", averageStats);
        model.addAttribute("trueDamageDealtFrequency", getHistogram(champ, "trueDamageDealt", collection));

        averageStats = getAverageStats(champ, "totalDamageTaken", collection);
        model.addAttribute("totalDamageTakenStats", averageStats);
        model.addAttribute("totalDamageTakenFrequency", getHistogram(champ, "totalDamageTaken", collection));

        mongoClient.close();
        //src/main/resources/templates/<return value>.html
        return "champion";
    }

    //index controller
    @RequestMapping("/")
    public String index(Model model){

        model.addAttribute("search", new Search());

        return "index";
    }
}
