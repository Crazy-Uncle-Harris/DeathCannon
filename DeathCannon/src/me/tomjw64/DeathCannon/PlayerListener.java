package me.tomjw64.DeathCannon;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerListener implements Listener{
	public static DeathCannon plugin;
	private Set<String> deathList=new HashSet<String>();
	
	public PlayerListener(DeathCannon instance)
	{
		plugin=instance;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerDeath(final PlayerDeathEvent death)
	{
		World world=death.getEntity().getWorld();
		Player died=death.getEntity();
		if(plugin.getWorld().equals(world)||plugin.getAllWorlds()==true)
		{
			world.strikeLightningEffect(died.getLocation().add(0, 100, 0));
			if(!deathList.contains(died.getName()))
			{
				deathList.add(died.getName());
			}
			death.setDeathMessage(null);
		}
	}
	public Set<String> getDeathList()
	{
		return deathList;
	}
	public void clearDeathList()
	{
		deathList.clear();
	}
}
