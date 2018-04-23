package uk.panicdog.DataAnalyser.model;

public class IndividualChampionData {

    public IndividualChampionData() {
        this.accountId = accountId;
        this.gameId = gameId;
        this.champion = champion;
        this.win = win;
        this.winTime = winTime;
        this.lane = lane;
        this.ban = ban;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.goldEarned = goldEarned;
        this.totalMinionsKilled = totalMinionsKilled;
        this.totalDamageToChampions = totalDamageToChampions;
        this.physicalDamageDealtToChampions = physicalDamageDealtToChampions;
        this.magicDamageDealtToChampions = magicDamageDealtToChampions;
        this.trueDamageDealtToChampions = trueDamageDealtToChampions;
        this.totalDamageTaken = totalDamageTaken;
        this.summ0 = summ0;
        this.summ1 = summ1;
        this.item0 = item0;
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
        this.item5 = item5;
        this.item6 = item6;
        this.rune0 = rune0;
        this.rune1 = rune1;
        this.rune2 = rune2;
        this.rune3 = rune3;
        this.mastery0 = mastery0;
        this.mastery1 = mastery1;
        this.mastery2 = mastery2;
        this.mastery3 = mastery3;
        this.mastery4 = mastery4;
        this.mastery5 = mastery5;
        this.mastery6 = mastery6;
        this.mastery7 = mastery7;
        this.mastery8 = mastery8;
        this.mastery9 = mastery9;
    }

    private long accountId;
    private long gameId;

    private int champion;
    private boolean win;
    private int winTime;
    private String lane;
    private int ban;

    private int kills;
    private int deaths;
    private int assists;
    private int goldEarned;
    private int totalMinionsKilled;
    private int totalDamageToChampions;
    private int physicalDamageDealtToChampions;
    private int magicDamageDealtToChampions;
    private int trueDamageDealtToChampions;
    private int totalDamageTaken;

    private int summ0;
    private int summ1;

    private int item0;
    private int item1;
    private int item2;
    private int item3;
    private int item4;
    private int item5;
    private int item6;

    private int rune0;
    private int rune1;
    private int rune2;
    private int rune3;

    private int mastery0;
    private int mastery1;
    private int mastery2;
    private int mastery3;
    private int mastery4;
    private int mastery5;
    private int mastery6;
    private int mastery7;
    private int mastery8;
    private int mastery9;

    public long getAccountId() {
        return accountId;
    }

    public long getGameId() {
        return gameId;
    }

    public int getChampion() {
        return champion;
    }

    public boolean isWin() {
        return win;
    }

    public int getWinTime() {
        return winTime;
    }

    public String getLane() {
        return lane;
    }

    public int getBan() {
        return ban;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getAssists() {
        return assists;
    }

    public int getGoldEarned() {
        return goldEarned;
    }

    public int getTotalMinionsKilled() {
        return totalMinionsKilled;
    }

    public int getTotalDamageToChampions() {
        return totalDamageToChampions;
    }

    public int getPhysicalDamageDealtToChampions() {
        return physicalDamageDealtToChampions;
    }

    public int getMagicDamageDealtToChampions() {
        return magicDamageDealtToChampions;
    }

    public int getTrueDamageDealtToChampions() {
        return trueDamageDealtToChampions;
    }

    public int getTotalDamageTaken() {
        return totalDamageTaken;
    }

    public int getSumm0() {
        return summ0;
    }

    public int getSumm1() {
        return summ1;
    }

    public int getItem0() {
        return item0;
    }

    public int getItem1() {
        return item1;
    }

    public int getItem2() {
        return item2;
    }

    public int getItem3() {
        return item3;
    }

    public int getItem4() {
        return item4;
    }

    public int getItem5() {
        return item5;
    }

    public int getItem6() {
        return item6;
    }

    public int getRune0() {
        return rune0;
    }

    public int getRune1() {
        return rune1;
    }

    public int getRune2() {
        return rune2;
    }

    public int getRune3() {
        return rune3;
    }

    public int getMastery0() {
        return mastery0;
    }

    public int getMastery1() {
        return mastery1;
    }

    public int getMastery2() {
        return mastery2;
    }

    public int getMastery3() {
        return mastery3;
    }

    public int getMastery4() {
        return mastery4;
    }

    public int getMastery5() {
        return mastery5;
    }

    public int getMastery6() {
        return mastery6;
    }

    public int getMastery7() {
        return mastery7;
    }

    public int getMastery8() {
        return mastery8;
    }

    public int getMastery9() {
        return mastery9;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public void setChampion(int champion) {
        this.champion = champion;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public void setWinTime(int winTime) {
        this.winTime = winTime;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public void setBan(int ban) {
        this.ban = ban;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public void setGoldEarned(int goldEarned) {
        this.goldEarned = goldEarned;
    }

    public void setTotalMinionsKilled(int totalMinionsKilled) {
        this.totalMinionsKilled = totalMinionsKilled;
    }

    public void setTotalDamageToChampions(int totalDamageToChampions) {
        this.totalDamageToChampions = totalDamageToChampions;
    }

    public void setPhysicalDamageDealtToChampions(int physicalDamageDealtToChampions) {
        this.physicalDamageDealtToChampions = physicalDamageDealtToChampions;
    }

    public void setMagicDamageDealtToChampions(int magicDamageDealtToChampions) {
        this.magicDamageDealtToChampions = magicDamageDealtToChampions;
    }

    public void setTrueDamageDealtToChampions(int trueDamageDealtToChampions) {
        this.trueDamageDealtToChampions = trueDamageDealtToChampions;
    }

    public void setTotalDamageTaken(int totalDamageTaken) {
        this.totalDamageTaken = totalDamageTaken;
    }

    public void setSumm0(int summ0) {
        this.summ0 = summ0;
    }

    public void setSumm1(int summ1) {
        this.summ1 = summ1;
    }

    public void setItem0(int item0) {
        this.item0 = item0;
    }

    public void setItem1(int item1) {
        this.item1 = item1;
    }

    public void setItem2(int item2) {
        this.item2 = item2;
    }

    public void setItem3(int item3) {
        this.item3 = item3;
    }

    public void setItem4(int item4) {
        this.item4 = item4;
    }

    public void setItem5(int item5) {
        this.item5 = item5;
    }

    public void setItem6(int item6) {
        this.item6 = item6;
    }

    public void setRune0(int rune0) {
        this.rune0 = rune0;
    }

    public void setRune1(int rune1) {
        this.rune1 = rune1;
    }

    public void setRune2(int rune2) {
        this.rune2 = rune2;
    }

    public void setRune3(int rune3) {
        this.rune3 = rune3;
    }

    public void setMastery0(int mastery0) {
        this.mastery0 = mastery0;
    }

    public void setMastery1(int mastery1) {
        this.mastery1 = mastery1;
    }

    public void setMastery2(int mastery2) {
        this.mastery2 = mastery2;
    }

    public void setMastery3(int mastery3) {
        this.mastery3 = mastery3;
    }

    public void setMastery4(int mastery4) {
        this.mastery4 = mastery4;
    }

    public void setMastery5(int mastery5) {
        this.mastery5 = mastery5;
    }

    public void setMastery6(int mastery6) {
        this.mastery6 = mastery6;
    }

    public void setMastery7(int mastery7) {
        this.mastery7 = mastery7;
    }

    public void setMastery8(int mastery8) {
        this.mastery8 = mastery8;
    }

    public void setMastery9(int mastery9) {
        this.mastery9 = mastery9;
    }
}
