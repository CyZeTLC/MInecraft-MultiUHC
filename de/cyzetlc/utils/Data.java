package de.cyzetlc.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.cyzetlc.utils.team.Team;

public class Data
{
	/*
	 * Modi
	 */
	public static int votesUHC = 0;
	public static int votesSpeedUHC = 0;
	public static int votesMeetup = 0;
	public static String MODI = "UHC";
	public static HashMap<Player, String> playerVotes = new HashMap<>();
	
	/*
	 * Prefix
	 */
	public static String PREFIX = "§7» §b§l"+MODI+" §8§l● §r".replace("&", "§");
	
	/*
	 * PlayerJoin / PlayerQuit
	 */
	public static final String PLAYER_JOIN = PREFIX + "&c%s &7ist dem Spiel beigetreten".replace("&", "§");
	public static final String PLAYER_QUIT = PREFIX + "&c%s &7hat das Spiel verlassen".replace("&", "§");
	public static final String GAME_STARTED = PREFIX + "&7Das Spiel ist bereits &cgestartet&7!".replace("&", "§");
	
	/*
	 * Countdowns
	 */
	public static final String GAME_START_COUNTDOWN = PREFIX + "&7Das &5Spiel &7startet in &c%s &7Sekunden!".replace("&", "§");
	public static final String SAVE_TIME_COUNTDOWN = PREFIX + "&7Die Schutzzeit endet in &c%s &7Sekunden!".replace("&", "§");
	public static final String STOP_TIME_COUNTDOWN = PREFIX + "&7Der Server &cstoppt &7in &c%s Sekunden!".replace("&", "§");
	public static final String GAME_PLAYER_IDLE = PREFIX + "&7Warten auf §6%s §7weitere Spieler..".replace("&", "§");
	public static final String PLAY_TIME_STARTS = PREFIX + "§7Die Spielzeit §abeginnt §7in §c%s Sekunden§7!";
	
	/*
	 * Player Death
	 */
	public static final String PLAYER_DEATH_BY_PLAYER_PUBLIC = PREFIX + "&c%p &7wurde von &c%k &7eliminiert!".replace("&", "§");
	public static final String PLAYER_DEATH_PUBLIC = PREFIX + "&7Der Spieler &b%s &7ist &cgestorben&7!".replace("&", "§");
	public static final String PLAYER_KILLED = PREFIX + "&aDu hast %p getötet!".replace("&", "§");
	public static final String PLAYER_DEATH = PREFIX + "&cDu wurdest von %k &7[&e❤%l&7]&c getötet!".replace("&", "§");
	
	/*
	 * Win Game
	 */
	public static final String TEAM_WIN = PREFIX + "&7Das Team &a%s &7hat das Spiel &agewonnen&7!".replace("&", "§");
	public static final String PLAYER_WIN = PREFIX + "&7Der Spieler &a%s &7hat das Spiel &agewonnen&7!".replace("&", "§");
	
	/*
	 * Team Messages
	 */
	public static final String ALREADY_IN_SAME_TEAM = PREFIX + "§7Du bist bereits in diesem Team!";
	public static final String TEAM_IS_FULL = PREFIX + "§7Dieses Team ist voll!";
	public static final String TEAM_JOIN = PREFIX + "§7Du hast das Team §a#%s §7betreten";
	public static final String TEAM_JOIN_ERROR = PREFIX + "§7Du kannst diesem Team nicht beitreten!";
	public static final String TEAM_SPAWN_NOT_SET = PREFIX + "§7Du hast noch keinen Teamspawn!";
	public static final String TEAM_ELIMINATE = PREFIX + "§7Das Team §e%s §7wurde ausgelöscht!";
	public static final String TEAM_LOSE_PLAYER = PREFIX + "§7Das Team §e%s §7hat nur noch §e%c §7Spieler!";
	
	/*
	 * Not Allowed
	 */
	public static final String NO_PERMS = PREFIX + "§7Dazu hast du keine Rechte!";
	public static final String SAVE_TIME_ACTIVE = PREFIX + "§7Du darfst §e%s §7erst nach der Schutzzeit (ab)bauen!";
	
	/*
	 * Setup Messages
	 */
	public static final String ALL_COMMANDS = PREFIX + "§7Hilfe: \n"
			                                 + "§e/setspawn lobby \n"
			                                 + "§e/setspawn holo \n"
			                                 + "§e/setspawn skull <ID> \n"
			                                 + "§e/setspawn team <TEAMNAME>";
	public static final String CHANGE_LOBBY_LOC = PREFIX + "§7Die Position der §aLobby §7wurde geändert";
	public static final String SET_LOBBY_LOC = PREFIX + "§7Du hast die §aLobby §7erstellt";
	public static final String CHANGE_HOLO_LOC = PREFIX + "§7Die Position des §aHologram §7wurde geändert";
	public static final String SET_HOLO_LOC = PREFIX + "§7Du hast das §aHologram §7erstellt";
	public static final String CHANGE_TEAM_LOC = PREFIX + "§7Der Spawn für §a%s §7wurde geändert";
	public static final String SET_TEAM_LOC = PREFIX + "§7Du hast den Spawn für §a%s §7erstellt";
	public static final String TEAM_NOT_EXSISTS = PREFIX + "§7Dieses Team gibt es nicht!";
	
	/*
	 * Other
	 */
	public static final String SKULL_SET = PREFIX + "§7Du hast den Skull §e%s§7 gesetzt";
	public static final String COMMAND_START_PLAYERS = PREFIX + "§7Es müssen mindestens 2 Spieler in der Runde sein!";
	public static final String COMMAND_START_WAITING = PREFIX + "§7Du kannst das Spiel nur wärend der Wartezeit starten!";
	
	/*
	 * To Messages
	 */
	public static final String ALL_COMMANDS_TO = PREFIX + "§7Hilfe: \n"
            + "§e/to\n"
            + "§e/to <ANZAHL>";
	public static final String ONLY_NUMBERS = PREFIX + "§7Du musst eine Zahl angeben!";
	public static final String SETTINGS_SAVED = PREFIX + "§7Alle Einstellungen wurden gespeichert";
	
	/*
	 * Szenarie Messages
	 */
	public static final String ALL_COMMANDS_SZ = PREFIX + "§7Hilfe: \n"
            + "§e/sz\n"
            + "§e/sz <SZENARIE>";
	public static final String SZ_NOT_FOUND = PREFIX + "§7Diese Szenarie wurde nicht gefunden!";
	public static final String SZ_NEW_SET = PREFIX + "§7Die neue Szenarie wurde gesetzt";
	
	/*
	 * Kit Messages
	 */
	public static final String ALL_COMMANDS_KIT = PREFIX + "§7Hilfe: \n"
            + "§e/kit create <KITNAME> \n"
            + "§e/kit set <TEAMNAME>";
	public static final String KIT_EXISTS = PREFIX + "§7Dieses §aKit §6gibt es schon!";
	public static final String KIT_CREATE_NEW = PREFIX + "§7Du hast das Kit §a%s §7erstellt";
	public static final String KIT_NOT_EXISTS = PREFIX + "§7Das Kit §a%s §7gibt es nicht!";
	public static final String DEFAULT_KIT_SET = PREFIX + "§7Das Kit §a%s §7ist nun das default Kit";
	
	/*
	 * Permissions
	 */
	public static final String MEETUP_START = "meetup.start";
	public static final String MEETUP_SETSPAWN = "meetup.setspawn";
	public static final String MEETUP_TO = "meetup.to";
	public static final String MEETUP_SPEC = "meetup.spec";
	public static final String MEETUP_KIT = "meetup.kit";
	public static final String MEETUP_SZ = "meetup.sz";
	public static final String MEETUP_JOIN_INGAME = "meetup.join.ingame";
	
	/*
	 * Stats Format
	 */
	public static final String STATS_FORMAT = "§8» §7Statistiken von §5%name% §8«\n" + 
			"§7Platz » §e%rank%\n" + 
			"§7Spiele » §e%gs%\n" + 
			"§7Siege » §e%wins%\n" + 
			"§7Kills » §e%Kills%\n" + 
			"§7Deaths » §e%deaths%\n" + 
			"§7KD » §e%kd%";
	public static final String STATS_FORMAT_HEADER = "§8» §7Deine Statistiken §8«";
	public static final String STATS_FORMAT_RANK = "§7Platz » §e%rank%";
	public static final String STATS_FORMAT_GAMES = "§7Spiele » §e%gs%";
	public static final String STATS_FORMAT_WINS = "§7Siege » §e%wins%";
	public static final String STATS_FORMAT_KILLS = "§7Kills » §e%Kills%";
	public static final String STATS_FORMAT_DEATHS = "§7Deaths » §e%deaths%";
	public static final String STATS_FORMAT_KD = "§7KD » §e%kd%";
	public static final String NO_STATS_FOUND = PREFIX + "§7Es wurden keine Stats zu diesem Spieler gefunden!";
	
	/*
	 * Items
	 */
	// Lobby
	public static final String ITEM_TEAM_SELECTION = "&6Teamwahl".replace("&", "§");
	public static final String ITEM_LEAVE_GAME = "&6Spiel verlassen".replace("&", "§");
	
	/*
	 * Game Values
	 */
	public static int MIN_PLAYERS = 2;
	public static int MAX_PLAYERS = 16;
	
	public static Collection<Player> players = new ArrayList<Player>();
	public static Collection<Player> spectators = new ArrayList<Player>();
	
	/*
	 * Teams
	 */
	public static int TEAM_COUNT = 26;
	public static final String[] TEAM_COLORS = new String[] {
			"§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7",
			"§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f",
			"§0§l", "§1§l", "§2§l", "§3§l", "§4§l", "§5§l", "§6§l", "§7§l",
			"§8§l", "§9§l", "§a§l", "§b§l", "§c§l", "§d§l", "§e§l", "§f§l",
			"§0§l"
	};

	public static Team[] allTeams = new Team[TEAM_COUNT];
	
	public static void initTeams()
	{
		allTeams = new Team[TEAM_COUNT];
		
		for (int i = 0; i < TEAM_COUNT; i++)
		{
			allTeams[i] = new Team("T" + (i + 1), TEAM_COLORS[i]);
		}
	}
	
	/*
	 * Kills
	 */
	public static HashMap<Player, Integer> kills = new HashMap<Player, Integer>();
	public static HashMap<Player, String> teamName = new HashMap<Player, String>();
	
	public static void setModi()
	{
		if ((votesUHC > votesMeetup && votesUHC > votesSpeedUHC)
				|| votesUHC == votesMeetup && votesSpeedUHC < votesUHC
				|| votesUHC == votesSpeedUHC && votesMeetup < votesUHC)
		{
			MODI = "UHC";
		}
		if ((votesMeetup > votesUHC && votesMeetup > votesSpeedUHC)
				|| votesMeetup == votesSpeedUHC && votesUHC < votesMeetup)
		{
			MODI = "Meetup";
		}
		if (votesSpeedUHC > votesUHC && votesSpeedUHC > votesMeetup)
		{
			MODI = "SpeedUHC";
		}
		if (votesMeetup == votesUHC && votesSpeedUHC == votesUHC)
		{
			MODI = "UHC";
		}
	}
}
