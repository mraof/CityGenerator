package ludelux.citygen;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;

//Permissions
//import com.nijiko.permissions.PermissionHandler;
//import com.nijikokun.bukkit.Permissions.Permissions;


public class CityMain extends JavaPlugin
{
    private File dataFolder;
    
    public String cityName = "city";
    
    CityGenerator generator = new CityGenerator();
    
    public void onEnable()
    {
        // Register our events
        PluginManager pm = getServer().getPluginManager();
        //pm.registerEvent(Event.Type.CHUNK_LOAD, chunkListenr, Priority.Normal, this);
        

        // Register our commands
        getCommand("citygen").setExecutor(this);
        getCommand("citytp").setExecutor(this);

        // Load the configuration
        dataFolder = new File("plugins/CityGen");
        if(!dataFolder.exists())
            dataFolder.mkdirs();

        CityConfig.load(new File(dataFolder, "config.yml"));
        
        getServer().createWorld(cityName, Environment.NORMAL, generator);

        PluginDescriptionFile pdfFile = this.getDescription();
        Logger.getLogger(cityName).info("[" + pdfFile.getName() + "]" + " version " + pdfFile.getVersion() + " is enabled!");
    }

    public void onDisable()
    {
        getServer().unloadWorld(cityName, true);
        
        PluginDescriptionFile pdf = this.getDescription();
        System.out.println(pdf.getName() + " version " + pdf.getVersion() + " is disabled.");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if(!(sender instanceof Player))
            return false;
        Player p = (Player)sender;
        
        if(commandLabel.equalsIgnoreCase("citygen"))
        {
            String name = "city";
            if(args.length >= 1)
                name = args[0];
            
            //CityGenerator.generate(cityName); //Use default for now only
            return true;
        }
        else if(commandLabel.equalsIgnoreCase("citytp"))
        {
            p.teleport(new Location(getServer().getWorld(cityName), 0, 40, 0));
            return true;
        }
        else if(commandLabel.equalsIgnoreCase("worldtp"))
        {
            p.teleport(new Location(getServer().getWorld("world"), 0, 0, 0));
            return true;
        }
        return false;
    }
}
