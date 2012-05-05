// DeathCannon plugin for bukkit
// Made by tomjw64

package me.tomjw64.DeathCannon;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathCannon extends JavaPlugin{
	public static DeathCannon plugin;
	public final Logger logger=Logger.getLogger("Minecraft");
	public PlayerListener pListener;
	private File configFile;
	private FileConfiguration config;
	private long lastDisplay=System.currentTimeMillis();
	private final ChatColor RED=ChatColor.RED;
	private World world;
	private boolean allWorlds;
	
	@Override
	public void onDisable()
	{
		PluginDescriptionFile pdf = getDescription();
		logger.info("["+pdf.getName()+"] disabled!");
	}
	@Override
	public void onEnable()
	{
		pListener=new PlayerListener(this);
		PluginDescriptionFile pdf=getDescription();
		loadConfig(pdf);
		startTimeCheck();
		logger.info("["+pdf.getName()+"] version "+pdf.getVersion()+" is enabled!");
	}
	public void startTimeCheck()
	{
		final World w=world;
		getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
			public void run()
			{
				if(w.getTime()>13000&&System.currentTimeMillis()>lastDisplay+600000)
				{
					if(allWorlds)
					{
						getServer().broadcastMessage(RED+"Players killed today:");
						for(String x:pListener.getDeathList())
						{
							getServer().broadcastMessage(x);
						}
					}
					else
					{
						for(Player p:world.getPlayers())
						{
							p.sendMessage(RED+"Players killed today:");
							for(String x:pListener.getDeathList())
							{
								p.sendMessage(x);
							}
						}
					}
					lastDisplay=System.currentTimeMillis();
					pListener.clearDeathList();
				}
			}
		}, 0L, 1200L);
	}
	public void loadConfig(PluginDescriptionFile pdf)
	{
		configFile=new File(getDataFolder(),"config.yml");
		if(!configFile.exists())
		{
			configFile.getParentFile().mkdirs();
			try{
				configFile.createNewFile();
				logger.info("["+pdf.getName()+"] Generating empty config.yml!");
			} catch (IOException wtf){
				wtf.printStackTrace();
			}
		}
		config=new YamlConfiguration();
		try {
			config.load(configFile);
			logger.info("["+pdf.getName()+"] Loading config!");
		} catch (Exception wtf) {
			wtf.printStackTrace();
		}
		
		if(!config.contains("World"))
		{
			config.createSection("World");
			config.set("World", "world");
			saveConfig();
		}
		world=getServer().getWorld(config.getString("World"));
		if(!config.contains("AllWorlds"))
		{
			config.createSection("AllWorlds");
			config.set("AllWorlds", false);
			saveConfig();
		}
		allWorlds=config.getBoolean("AllWorlds");
	}
	public void saveConfig()
	{
		try {
			config.save(configFile);
		} catch (IOException wtf) {
			wtf.printStackTrace();
		}
	}
	public World getWorld()
	{
		return world;
	}
	public boolean getAllWorlds()
	{
		return allWorlds;
	}
}
