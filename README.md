# LeagueAnalyser

## About

This project aims to parse league of legends games, analyse them, and provide the analysed game stats in a web platform for players to see where they can improve.

## Project Layout

1. DataParser
	*Parses JSON seed data.
	*Stores in mongodb
2. SeedAnalyser 
	*Analyses data stored in mongodb
	*Stores results in mongodb
3. ChampionController
	*Shows results to user

## Future Work

1. Create rest controller for analysed data using Spring
2. Do further analysis on parsed data
3. Use D3 or another data visualisation library to better visualise analysed data on the front end