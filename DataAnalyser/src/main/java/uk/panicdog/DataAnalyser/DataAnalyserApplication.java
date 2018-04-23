package uk.panicdog.DataAnalyser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

	}
}
