package ludelux.citygen;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class CityGenerator extends ChunkGenerator
{
    enum ChunkType
    {
        NULL,
        GRASS,
        ROAD,
        BUILDING,
    };

    public static ChunkType[][] chunkTypes = new ChunkType[256][256];

    //enum Direction{EAST, NORTH, WEST, SOUTH};

    @Override
    public byte[] generate(World world, Random rand, int chunkX, int chunkZ)
    {
        int offX = chunkX + 128;
        int offZ = chunkZ + 128;

        if(offX < 0 || offX >= 256 || offZ < 0 || offZ >= 256)
            return new byte[32768];

        ChunkType west = ChunkType.NULL;
        ChunkType east = ChunkType.NULL;
        ChunkType north = ChunkType.NULL;
        ChunkType south = ChunkType.NULL;

        if(offX > 0) west = chunkTypes[offX-1][offZ];
        if(offX < 255) east = chunkTypes[offX+1][offZ];
        if(offZ > 0) north = chunkTypes[offX][offZ-1];
        if(offZ == 0) south = chunkTypes[offX][offZ+1];

        byte[] chunk = new byte[32768];
        int buildingHeight = 127;
        int floorHeight = 5;
        ChunkType type = ChunkType.GRASS;
        double chance = rand.nextDouble();
        if(chance < 0.60)
            type = ChunkType.ROAD;
        else //if(chance < 0.60)
        {
            type = ChunkType.BUILDING;
            buildingHeight = randRange(rand,60/floorHeight,125/floorHeight)*5+2;
        }

        boolean makestair = true;

        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                for (int y = 0; y < buildingHeight; y++)
                {
                    int index = (x * 16 + z) * 128 + y;
                    //Lower layers
                    if(y <= 1)
                        chunk[index] = getId(Material.BEDROCK);
                    else if(y < 20)
                        chunk[index] = getId(Material.STONE);
                    else if(y < 50)
                        chunk[index] = getId(Material.DIRT);
                    else if(y == 50)
                    {
                        chunk[index] = getId(Material.GRASS);

                        //Generate roads
                        if(type == ChunkType.ROAD)
                        {
                            //Generate vertical road
                            /*if(x < 2 || z < 2); //Do nothing
                            else if(x < 4 || z < 4) chunk[index] = getId(Material.COBBLESTONE);
                            else if(x < 12 || z < 12) chunk[index] = getId(Material.STONE);
                            else if(x < 14 || z < 14) chunk[index] = getId(Material.COBBLESTONE);*/
                            chunk[index] = getId(Material.OBSIDIAN);
                        }
                    }
                    else
                    {
                        //y = 1
                        //z = 128
                        //x = 2048
                        //x*2048+z*128+y
                        if(type == ChunkType.BUILDING)
                        {
                            if (makestair)
                            {
                                makestair = true;
                                for (int yy = floorHeight; yy < buildingHeight; yy+=floorHeight)
                                {
                                    chunk[4*2048+5*128+(yy/*%floorHeight*/)+0] = getId(Material.DOUBLE_STEP);
                                    chunk[5*2048+5*128+(yy/*%floorHeight*/)+0] = getId(Material.DOUBLE_STEP);
                                    chunk[6*2048+5*128+(yy/*%floorHeight*/)+1] = getId(Material.STEP);
                                    chunk[7*2048+5*128+(yy/*%floorHeight*/)+1] = getId(Material.DOUBLE_STEP);
                                    chunk[7*2048+4*128+(yy/*%floorHeight*/)+2] = getId(Material.STEP);
                                    chunk[7*2048+3*128+(yy/*%floorHeight*/)+2] = getId(Material.DOUBLE_STEP);
                                    chunk[6*2048+3*128+(yy/*%floorHeight*/)+3] = getId(Material.STEP);
                                    chunk[5*2048+3*128+(yy/*%floorHeight*/)+3] = getId(Material.DOUBLE_STEP);
                                    chunk[4*2048+3*128+(yy/*%floorHeight*/)+4] = getId(Material.STEP);
                                    chunk[3*2048+3*128+(yy/*%floorHeight*/)+4] = getId(Material.DOUBLE_STEP);
                                    chunk[3*2048+4*128+(yy/*%floorHeight*/)+5] = getId(Material.STEP);
                                    chunk[3*2048+5*128+(yy/*%floorHeight*/)+5] = getId(Material.DOUBLE_STEP);
                                    
                                    for (int air = 1; air <= 3; air++)
                                    {
                                        chunk[4*2048+5*128+(yy/*%floorHeight*/)+air] = 0;
                                        chunk[5*2048+5*128+(yy/*%floorHeight*/)+air] = 0;
                                        chunk[6*2048+5*128+(yy/*%floorHeight*/)+1+air] = 0;
                                        chunk[7*2048+5*128+(yy/*%floorHeight*/)+1+air] = 0;
                                        chunk[7*2048+4*128+(yy/*%floorHeight*/)+2+air] = 0;
                                        chunk[7*2048+3*128+(yy/*%floorHeight*/)+2+air] = 0;
                                        chunk[6*2048+3*128+(yy/*%floorHeight*/)+3+air] = 0;
                                        chunk[5*2048+3*128+(yy/*%floorHeight*/)+3+air] = 0;
                                        chunk[4*2048+3*128+(yy/*%floorHeight*/)+4+air] = 0;
                                        chunk[3*2048+3*128+(yy/*%floorHeight*/)+4+air] = 0;
                                        chunk[3*2048+4*128+(yy/*%floorHeight*/)+5+air] = 0;
                                        chunk[3*2048+5*128+(yy/*%floorHeight*/)+5+air] = 0;
                                    }
                                }

                            }
                            if(x >= 2 && z >= 2 && x <= 13 && z <= 13)
                            {
                                byte buildingMaterial = getId(Material.STONE);
                                //default is stone
                                if(chance < 0.80) buildingMaterial = getId(Material.COBBLESTONE);
                                else if(chance < 0.81)buildingMaterial = getId(Material.WOOD);
                                //chance of cobblestone
                                if(x == 2 || z == 2 || x == 13 || z == 13)
                                {
                                    //Outer walls
                                    chunk[index] = buildingMaterial;
                                    //walls
                                    if((y+2)%floorHeight < 2)chunk[index] = getId(Material.GLASS);
                                    //windows
                                }
                                else
                                {
                                    //inside buildings
                                if((y%floorHeight) == 0)chunk[index] = buildingMaterial;
                                //floors
                                else if(((y+3)%floorHeight) == 0 && (x%floorHeight == 1 || z%floorHeight == 1) && ( x == 3 || z == 3 || x == 12 || z == 12))chunk[index] = getId(Material.TORCH);
                                //lighting
//                                else chunk[index] = 0;
                                }
                            }
                        }
                    }
                }
            }
        }

        return chunk;
    }

    /*public List<BlockPopulator> getDefaultPopulators(World world)
    {
        return Arrays.asList(new CityPopulator());
    }*/

    @Override
    public Location getFixedSpawnLocation(World world, Random random)
    {
        return new Location(world, 0, 0, 0);
    }

    @Override
    public boolean canSpawn(World world, int x, int z)
    {
        return true;
    }

    public byte getId(Material material)
    {
        return (byte)material.getId();
    }

    int randRange(Random rand, int low, int high)
    {
        return rand.nextInt(high-low) + low;
    }
}
