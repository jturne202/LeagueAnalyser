package uk.panicdog.DataAnalyser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@SpringBootApplication
public class DataAnalyserApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataAnalyserApplication.class, args);

		/*DataParser dp = new DataParser();
		dp.toMongo("https://s3-us-west-1.amazonaws.com/riot-developer-portal/seed-data/matches1.json");
		dp.toMongo("https://s3-us-west-1.amazonaws.com/riot-developer-portal/seed-data/matches2.json");
		dp.toMongo("https://s3-us-west-1.amazonaws.com/riot-developer-portal/seed-data/matches3.json");
		dp.toMongo("https://s3-us-west-1.amazonaws.com/riot-developer-portal/seed-data/matches4.json");
		dp.toMongo("https://s3-us-west-1.amazonaws.com/riot-developer-portal/seed-data/matches5.json");
		dp.toMongo("https://s3-us-west-1.amazonaws.com/riot-developer-portal/seed-data/matches6.json");
		dp.toMongo("https://s3-us-west-1.amazonaws.com/riot-developer-portal/seed-data/matches7.json");
		dp.toMongo("https://s3-us-west-1.amazonaws.com/riot-developer-portal/seed-data/matches8.json");
		dp.toMongo("https://s3-us-west-1.amazonaws.com/riot-developer-portal/seed-data/matches9.json");
		dp.toMongo("https://s3-us-west-1.amazonaws.com/riot-developer-portal/seed-data/matches10.json");*/

		/*SeedAnalyser da = new SeedAnalyser();
		da.analyse();*/

		//Testing
		//ArrayList<Integer> a = new ArrayList<>();
		//a.add(2);
		//a.add(3);
		//a.add(4);
		//a.add(17);
		//a.add(20);
		//a.add(14);
		//a.add(23);
		//a.add(8);
		//a.add(12);
		//a.add(9);
		//
		//SeedAnalyser s = new SeedAnalyser();
		//System.out.println(s.getMean(a));
		//System.out.println(s.getMedian(a));
		//System.out.println(s.getStandardDeviation(a));
	}
}
