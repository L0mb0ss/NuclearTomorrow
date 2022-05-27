package mod.L0mb0ss.NuclearTomorrow.dimensions.realWorld;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.Event.Result;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.*;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ANIMALS;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.DUNGEON;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA;

public class RealWorldChunkProvider implements IChunkProvider {
	
	private Random rand;
	private World worldObj;
	/** are map structures going to be generated (e.g. strongholds) */
	private final boolean mapFeaturesEnabled;
	private WorldType worldType;
	/**Caves Generator*/
	private MapGenBase caveGenerator = TerrainGen.getModdedMapGen(new MapGenCaves(), CAVE);
	/** Stronghold Generator */
	private MapGenStronghold strongholdGenerator = (MapGenStronghold) TerrainGen.getModdedMapGen(new MapGenStronghold(), STRONGHOLD);
	/** Village gon */
	//private MapGenVillage villageGenerator = (MapGenVillage) TerrainGen.getModdedMapGen(new MapGenVillage(), VILLAGE);
	/** Mineshaft Generator */
	private MapGenMineshaft mineshaftGenerator = (MapGenMineshaft) TerrainGen.getModdedMapGen(new MapGenMineshaft(), MINESHAFT);
	private MapGenScatteredFeature scatteredFeatureGenerator = (MapGenScatteredFeature) TerrainGen.getModdedMapGen(new MapGenScatteredFeature(), SCATTERED_FEATURE);
	/** Ravine Generator */
	private MapGenBase ravineGenerator = TerrainGen.getModdedMapGen(new MapGenRavine(), RAVINE);
	//Noise generation
	private NoiseGeneratorOctaves noiseGen1Octaves1;
    private NoiseGeneratorOctaves noiseGen2Octaves2;
    private NoiseGeneratorOctaves noiseGen3Octaves3;
    private NoiseGeneratorPerlin noiseGen4Perlin1;
    
    /** A NoiseGeneratorOctaves used in generating terrain */
    public NoiseGeneratorOctaves noiseGen5Octaves4;
    /** A NoiseGeneratorOctaves used in generating terrain */
    public NoiseGeneratorOctaves noiseGen6Octaves6;
    
    public NoiseGeneratorOctaves mobSpawnerNoise;
	
	/**
	 * Holds terrain noise
	 */
	double[] noiseGen3Octaves3DataHolder;
    double[] noiseGen1Octaves1DataHolder;
    double[] noiseGen2Octaves2DataHolder;
    double[] noiseGen6Octaves6DataHolder;
    
    /**
     * Noise field holding noises for further chunk generation
     */
    private final double[] noiseField;
    
	private double[] stoneNoiseOrnoiseGen4Perlin1DataHolder = new double[256];
	
	/**
     * Used to store the 5x5 parabolic field that is used during terrain generation.
     */
	private final float[] parabolicField;

	/**
	 * Temp array holding biomes for current chunk gen.
	 */
	private BiomeGenBase[] biomesForGeneration;
	
	/**
	 * Position of chunk that is currently being generated.
	 */
	private ChunkPosition currentChunkGenPos;
	private WorldChunkManager worldChunkMgr;
	
	public RealWorldChunkProvider(World world, WorldChunkManager wChunkMgr) {
		this.worldObj = world;
		this.mapFeaturesEnabled = world.getWorldInfo().isMapFeaturesEnabled();
		this.worldType = world.getWorldInfo().getTerrainType();
		this.rand = new Random(world.getSeed());
		this.worldChunkMgr = wChunkMgr;
		
		this.noiseGen1Octaves1 = new NoiseGeneratorOctaves(this.rand, 16);
        this.noiseGen2Octaves2 = new NoiseGeneratorOctaves(this.rand, 16);
        this.noiseGen3Octaves3 = new NoiseGeneratorOctaves(this.rand, 8);
        this.noiseGen4Perlin1 = new NoiseGeneratorPerlin(this.rand, 4);
        this.noiseGen5Octaves4 = new NoiseGeneratorOctaves(this.rand, 10);
        this.noiseGen6Octaves6 = new NoiseGeneratorOctaves(this.rand, 16);
        this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.noiseField = new double[825];
        this.parabolicField = new float[25];
        
        for (int j = -2; j <= 2; ++j)
        {
            for (int k = -2; k <= 2; ++k)
            {
                float f = 10.0F / MathHelper.sqrt_float((float)(j * j + k * k) + 0.2F);
                this.parabolicField[j + 2 + (k + 2) * 5] = f;
            }
        }
        
        NoiseGenerator[] noiseGens = {noiseGen1Octaves1, noiseGen2Octaves2, noiseGen3Octaves3, noiseGen4Perlin1, noiseGen5Octaves4, noiseGen6Octaves6, mobSpawnerNoise};
        noiseGens = TerrainGen.getModdedNoiseGenerators(world, this.rand, noiseGens);
        this.noiseGen1Octaves1 = (NoiseGeneratorOctaves)noiseGens[0];
        this.noiseGen2Octaves2 = (NoiseGeneratorOctaves)noiseGens[1];
        this.noiseGen3Octaves3 = (NoiseGeneratorOctaves)noiseGens[2];
        this.noiseGen4Perlin1 = (NoiseGeneratorPerlin)noiseGens[3];
        this.noiseGen5Octaves4 = (NoiseGeneratorOctaves)noiseGens[4];
        this.noiseGen6Octaves6 = (NoiseGeneratorOctaves)noiseGens[5];
        this.mobSpawnerNoise = (NoiseGeneratorOctaves)noiseGens[6];
	}
	
	@Override
	public Chunk loadChunk(int x, int z) {
		return this.provideChunk(x, z);
	}
	
	public Chunk provideChunk(int x, int z) {
		
		currentChunkGenPos = new ChunkPosition(x, 0, z);
		
		this.rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
		Block[] blocks = new Block[65536];
		byte[] metadata = new byte[65536];
		this.generateTerrain(x, z, blocks);
		this.biomesForGeneration = worldChunkMgr.loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
		this.replaceBlocksForBiome(x, z, blocks, metadata, this.biomesForGeneration);
		this.caveGenerator.func_151539_a(this, this.worldObj, x, z, blocks);
		this.ravineGenerator.func_151539_a(this, this.worldObj, x, z, blocks);

		if (this.mapFeaturesEnabled) {
			this.mineshaftGenerator.func_151539_a(this, this.worldObj, x, z, blocks);
			//this.villageGenerator.func_151539_a(this, this.worldObj, x, z, blocks);
			this.strongholdGenerator.func_151539_a(this, this.worldObj, x, z, blocks);
			this.scatteredFeatureGenerator.func_151539_a(this, this.worldObj, x, z, blocks);
		}

		Chunk chunk = new Chunk(this.worldObj, blocks, metadata, x, z);
		byte[] abyte1 = chunk.getBiomeArray();

		for (int k = 0; k < abyte1.length; ++k)
		{
			abyte1[k] = (byte)this.biomesForGeneration[k].biomeID;
		}

		chunk.generateSkylightMap();
		
		return chunk;
	}
	
	public void generateTerrain(int x, int z, Block[] blocks) {
		
		byte b0 = 63;
        this.biomesForGeneration = worldChunkMgr.getBiomesForGeneration(this.biomesForGeneration, x * 4 - 2, z * 4 - 2, 10, 10);
        this.initNoiseField(x * 4, 0, z * 4);

        for (int k = 0; k < 4; ++k)
        {
            int l = k * 5;
            int i1 = (k + 1) * 5;

            for (int j1 = 0; j1 < 4; ++j1)
            {
                int k1 = (l + j1) * 33;
                int l1 = (l + j1 + 1) * 33;
                int i2 = (i1 + j1) * 33;
                int j2 = (i1 + j1 + 1) * 33;

                for (int k2 = 0; k2 < 32; ++k2)
                {
                    double d0 = 0.125D;
                    double d1 = this.noiseField[k1 + k2];
                    double d2 = this.noiseField[l1 + k2];
                    double d3 = this.noiseField[i2 + k2];
                    double d4 = this.noiseField[j2 + k2];
                    double d5 = (this.noiseField[k1 + k2 + 1] - d1) * d0;
                    double d6 = (this.noiseField[l1 + k2 + 1] - d2) * d0;
                    double d7 = (this.noiseField[i2 + k2 + 1] - d3) * d0;
                    double d8 = (this.noiseField[j2 + k2 + 1] - d4) * d0;

                    for (int l2 = 0; l2 < 8; ++l2)
                    {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;

                        for (int i3 = 0; i3 < 4; ++i3)
                        {
                            int j3 = i3 + k * 4 << 12 | 0 + j1 * 4 << 8 | k2 * 8 + l2;
                            short short1 = 256;
                            j3 -= short1;
                            double d14 = 0.25D;
                            double d16 = (d11 - d10) * d14;
                            double d15 = d10 - d16;

                            for (int k3 = 0; k3 < 4; ++k3)
                            {
                                if ((d15 += d16) > 0.0D)
                                {
                                	blocks[j3 += short1] = Blocks.stone;
                                }
                                else if (k2 * 8 + l2 < b0)
                                {
                                	blocks[j3 += short1] = Blocks.water;
                                }
                                else
                                {
                                	blocks[j3 += short1] = null;
                                }
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }
	
	private void initNoiseField(int noiseOffestX, int noiseOffestY, int noiseOffestZ) {
        
		double d0 = 684.412D;
        double d1 = 684.412D;
        double d2 = 512.0D;
        double d3 = 512.0D;
        this.noiseGen6Octaves6DataHolder = this.noiseGen6Octaves6.generateNoiseOctaves(this.noiseGen6Octaves6DataHolder, noiseOffestX, noiseOffestZ, 5, 5, 200.0D, 200.0D, 0.5D);
        this.noiseGen3Octaves3DataHolder = this.noiseGen3Octaves3.generateNoiseOctaves(this.noiseGen3Octaves3DataHolder, noiseOffestX, noiseOffestY, noiseOffestZ, 5, 33, 5, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
        this.noiseGen1Octaves1DataHolder = this.noiseGen1Octaves1.generateNoiseOctaves(this.noiseGen1Octaves1DataHolder, noiseOffestX, noiseOffestY, noiseOffestZ, 5, 33, 5, 684.412D, 684.412D, 684.412D);
        this.noiseGen2Octaves2DataHolder = this.noiseGen2Octaves2.generateNoiseOctaves(this.noiseGen2Octaves2DataHolder, noiseOffestX, noiseOffestY, noiseOffestZ, 5, 33, 5, 684.412D, 684.412D, 684.412D);
        boolean flag1 = false;
        boolean flag = false;
        int l = 0;
        int i1 = 0;
        double d4 = 8.5D;

        for (int j1 = 0; j1 < 5; ++j1)
        {
            for (int k1 = 0; k1 < 5; ++k1)
            {
                float f = 0.0F;
                float f1 = 0.0F;
                float f2 = 0.0F;
                byte b0 = 2;
                BiomeGenBase biomegenbase = this.biomesForGeneration[j1 + 2 + (k1 + 2) * 10];

                for (int l1 = -b0; l1 <= b0; ++l1)
                {
                    for (int i2 = -b0; i2 <= b0; ++i2)
                    {
                        BiomeGenBase biomegenbase1 = this.biomesForGeneration[j1 + l1 + 2 + (k1 + i2 + 2) * 10];
                        float f3 = biomegenbase1.rootHeight;
                        float f4 = biomegenbase1.heightVariation;

                        if (this.worldType == WorldType.AMPLIFIED && f3 > 0.0F)
                        {
                            f3 = 1.0F + f3 * 2.0F;
                            f4 = 1.0F + f4 * 4.0F;
                        }

                        float f5 = this.parabolicField[l1 + 2 + (i2 + 2) * 5] / (f3 + 2.0F);

                        if (biomegenbase1.rootHeight > biomegenbase.rootHeight)
                        {
                            f5 /= 2.0F;
                        }

                        f += f4 * f5;
                        f1 += f3 * f5;
                        f2 += f5;
                    }
                }

                f /= f2;
                f1 /= f2;
                f = f * 0.9F + 0.1F;
                f1 = (f1 * 4.0F - 1.0F) / 8.0F;
                double d12 = this.noiseGen6Octaves6DataHolder[i1] / 8000.0D;

                if (d12 < 0.0D)
                {
                    d12 = -d12 * 0.3D;
                }

                d12 = d12 * 3.0D - 2.0D;

                if (d12 < 0.0D)
                {
                    d12 /= 2.0D;

                    if (d12 < -1.0D)
                    {
                        d12 = -1.0D;
                    }

                    d12 /= 1.4D;
                    d12 /= 2.0D;
                }
                else
                {
                    if (d12 > 1.0D)
                    {
                        d12 = 1.0D;
                    }

                    d12 /= 8.0D;
                }

                ++i1;
                double d13 = (double)f1;
                double d14 = (double)f;
                d13 += d12 * 0.2D;
                d13 = d13 * 8.5D / 8.0D;
                double d5 = 8.5D + d13 * 4.0D;

                for (int j2 = 0; j2 < 33; ++j2)
                {
                    double d6 = ((double)j2 - d5) * 12.0D * 128.0D / 256.0D / d14;

                    if (d6 < 0.0D)
                    {
                        d6 *= 4.0D;
                    }

                    double d7 = this.noiseGen1Octaves1DataHolder[l] / 512.0D;
                    double d8 = this.noiseGen2Octaves2DataHolder[l] / 512.0D;
                    double d9 = (this.noiseGen3Octaves3DataHolder[l] / 10.0D + 1.0D) / 2.0D;
                    double d10 = MathHelper.denormalizeClamp(d7, d8, d9) - d6;

                    if (j2 > 29)
                    {
                        double d11 = (double)((float)(j2 - 29) / 3.0F);
                        d10 = d10 * (1.0D - d11) + -10.0D * d11;
                    }

                    this.noiseField[l] = d10;
                    ++l;
                }
            }
        }
    }
	
	public void replaceBlocksForBiome(int x, int y, Block[] blocks, byte[] meta, BiomeGenBase[] biomes) {
        ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(this, x, y, blocks, meta, biomes, this.worldObj);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Result.DENY) return;

        double d0 = 0.03125D;
        this.stoneNoiseOrnoiseGen4Perlin1DataHolder = this.noiseGen4Perlin1.func_151599_a(this.stoneNoiseOrnoiseGen4Perlin1DataHolder, (double)(x * 16), (double)(y * 16), 16, 16, d0 * 2.0D, d0 * 2.0D, 1.0D);

        for (int k = 0; k < 16; ++k)
        {
            for (int l = 0; l < 16; ++l)
            {
                BiomeGenBase biomegenbase = biomes[l + k * 16];
                biomegenbase.genTerrainBlocks(this.worldObj, this.rand, blocks, meta, x * 16 + k, y * 16 + l, this.stoneNoiseOrnoiseGen4Perlin1DataHolder[l + k * 16]);
            }
        }
    }
	
	/*
	 * Entities
	 */

	@Override
	public List getPossibleCreatures(EnumCreatureType type, int x, int y, int z) {
		BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(x, z);
		return type == EnumCreatureType.monster && this.scatteredFeatureGenerator.func_143030_a(x, y, z) ? this.scatteredFeatureGenerator.getScatteredFeatureSpawnList() : biomegenbase.getSpawnableList(type);
	}

	/*
	 * Unknown
	 */

	@Override
	public net.minecraft.world.ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_, int p_147416_5_) {
		return "Stronghold".equals(p_147416_2_) && this.strongholdGenerator != null ? this.strongholdGenerator.func_151545_a(p_147416_1_, p_147416_3_, p_147416_4_, p_147416_5_) : null;
	}

	/*
	 * Population
	 */

	@Override
	public void populate(IChunkProvider provider, int x, int y) {
		BlockFalling.fallInstantly = true;
		int k = x * 16;
		int l = y * 16;
		BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(k + 16, l + 16);
		this.rand.setSeed(this.worldObj.getSeed());
		long i1 = this.rand.nextLong() / 2L * 2L + 1L;
		long j1 = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed((long)x * i1 + (long)y * j1 ^ this.worldObj.getSeed());
		boolean flag = false;

		MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(provider, worldObj, rand, x, y, flag));

		if (this.mapFeaturesEnabled)
		{
			this.mineshaftGenerator.generateStructuresInChunk(this.worldObj, this.rand, x, y);
			//flag = this.villageGenerator.generateStructuresInChunk(this.worldObj, this.rand, x, y);
			this.strongholdGenerator.generateStructuresInChunk(this.worldObj, this.rand, x, y);
			this.scatteredFeatureGenerator.generateStructuresInChunk(this.worldObj, this.rand, x, y);
		}

		int k1;
		int l1;
		int i2;

		if (biomegenbase != BiomeGenBase.desert && biomegenbase != BiomeGenBase.desertHills && !flag && this.rand.nextInt(4) == 0 && TerrainGen.populate(provider, worldObj, rand, x, y, flag, LAKE)) {
			k1 = k + this.rand.nextInt(16) + 8;
			l1 = this.rand.nextInt(256);
			i2 = l + this.rand.nextInt(16) + 8;
			(new WorldGenLakes(Blocks.water)).generate(this.worldObj, this.rand, k1, l1, i2);
		}

		if (TerrainGen.populate(provider, worldObj, rand, x, y, flag, LAVA) && !flag && this.rand.nextInt(8) == 0)
		{
			k1 = k + this.rand.nextInt(16) + 8;
			l1 = this.rand.nextInt(this.rand.nextInt(248) + 8);
			i2 = l + this.rand.nextInt(16) + 8;

			if (l1 < 63 || this.rand.nextInt(10) == 0)
			{
				(new WorldGenLakes(Blocks.lava)).generate(this.worldObj, this.rand, k1, l1, i2);
			}
		}

		boolean doGen = TerrainGen.populate(provider, worldObj, rand, x, y, flag, DUNGEON);
		for (k1 = 0; doGen && k1 < 8; ++k1)
		{
			l1 = k + this.rand.nextInt(16) + 8;
			i2 = this.rand.nextInt(256);
			int j2 = l + this.rand.nextInt(16) + 8;
			(new WorldGenDungeons()).generate(this.worldObj, this.rand, l1, i2, j2);
		}

		biomegenbase.decorate(this.worldObj, this.rand, k, l);
		if (TerrainGen.populate(provider, worldObj, rand, x, y, flag, ANIMALS))
		{
			SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, k + 8, l + 8, 16, 16, this.rand);
		}
		k += 8;
		l += 8;

		doGen = TerrainGen.populate(provider, worldObj, rand, x, y, flag, ICE);
		for (k1 = 0; doGen && k1 < 16; ++k1)
		{
			for (l1 = 0; l1 < 16; ++l1)
			{
				i2 = this.worldObj.getPrecipitationHeight(k + k1, l + l1);

				if (this.worldObj.isBlockFreezable(k1 + k, i2 - 1, l1 + l))
				{
					this.worldObj.setBlock(k1 + k, i2 - 1, l1 + l, Blocks.ice, 0, 2);
				}

				if (this.worldObj.func_147478_e(k1 + k, i2, l1 + l, true))
				{
					this.worldObj.setBlock(k1 + k, i2, l1 + l, Blocks.snow_layer, 0, 2);
				}
			}
		}

		MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(provider, worldObj, rand, x, y, flag));

		BlockFalling.fallInstantly = false;
	}

	/*
	 * Structures
	 */

	@Override
	public void recreateStructures(int x, int y) {
		if (this.mapFeaturesEnabled)
		{
			this.mineshaftGenerator.func_151539_a(this, this.worldObj, x, y, (Block[])null);
			//this.villageGenerator.func_151539_a(this, this.worldObj, x, y, (Block[])null);
			this.strongholdGenerator.func_151539_a(this, this.worldObj, x, y, (Block[])null);
			this.scatteredFeatureGenerator.func_151539_a(this, this.worldObj, x, y, (Block[])null);
		}
	}

	/*
	 * Debug menu
	 */

	@Override
	public String makeString() {
		return "WCSW Level Source";
	}

	/*
	 * Unused
	 */

	/**
	 * Unused
	 */
	@Override
	public boolean chunkExists(int x, int y) {
		return true;
	}

	/**
	 * Unused
	 */
	@Override
	public boolean unloadQueuedChunks() {
		return false;
	}

	@Override
	public int getLoadedChunkCount() {
		return 0;
	}

	/**
	 * Unused
	 */
	@Override
	public boolean canSave() {
		return true;
	}

	/**
	 * Unused
	 */
	@Override
	public boolean saveChunks(boolean b, IProgressUpdate p) {
		return true;
	}

	/**
	 * Unused
	 */
	@Override
	public void saveExtraData() {

	}

}