package goldenshadow.gearviewspoofer.client;

public enum ShinyType {
    MOBS_KILLED(1, "mobsKilled", "Mobs Killed"),
    RAIDS_WON(2, "raidsWon", "Raids Won"),
    CHESTS_OPENED(3, "chestsOpened", "Chests Opened"),
    WARS_WON(4, "warsWon", "Wars Won"),
    PLAYERS_KILLED(5, "playersKilled", "Players Killed"),
    DUNGEONS_WON(6, "dungeonsWon", "Dungeons Won"),
    DEATHS(7, "deaths", "Deaths"),
    LOOTRUNS_COMPLETED(8, "lootrunsCompleted", "Lootruns Completed"),
    BOSS_ALTARS_WON(9, "bossAltarsWon", "Boss Altars Won"),
    GUILD_RAIDS_WON(10, "guildRaidsWon", "Guild Raids Won"),
    WORLD_EVENTS_WON(11, "worldEventsWon", "World Events Won"),
    MAJOR_WORLD_EVENTS_WON(12, "majorWorldEventsWon", "Major World Events Won");

    public final int id;
    public final String key;
    public final String displayName;

    ShinyType(int id, String key, String displayName) {
        this.id = id;
        this.key = key;
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
