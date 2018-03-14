package erebus.world.biomes.decorators;

import erebus.ModBlocks;
import erebus.blocks.BlockDustLayer;
import erebus.blocks.BlockThorns;
import erebus.world.biomes.decorators.data.FeatureType;
import erebus.world.biomes.decorators.data.OreSettings;
import erebus.world.biomes.decorators.data.OreSettings.OreType;
import erebus.world.biomes.decorators.data.SurfaceType;
import erebus.world.feature.decoration.WorldGenRockSpike;
import erebus.world.feature.decoration.WorldGenScorchedWood;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldGenLakes;


public class BiomeDecoratorPetrifiedForest extends BiomeDecoratorBaseErebus {

	private final WorldGenScorchedWood genScorchedWood = new WorldGenScorchedWood();
	private final WorldGenLakes genLavaLakes = new WorldGenLakes(ModBlocks.FORMIC_ACID);
	private final WorldGenRockSpike rockSpike = new WorldGenRockSpike();
	private static final int[] offsetX = new int[] { -1, 1, 0, 0 };
	private static final int[] offsetZ = new int[] { 0, 0, -1, 1 };

	@Override
	public void populate() {
		for (attempt = 0; attempt < 35; attempt++) {
			xx = x + offsetXZ();
			yy = 15 + rand.nextInt(90);
			zz = z + offsetXZ();
			BlockPos pos = new BlockPos(xx, yy, zz);
			if (checkSurface(SurfaceType.VOLCANIC_ROCK, pos))
				genLavaLakes.generate(world, world.rand, pos.up());
		}
	}

	@Override
	public void decorate() {
		
		for (attempt = 0; attempt < 240; attempt++) {
			xx = x + offsetXZ();
			zz = z + offsetXZ();

			for (yy = 20; yy < 100; yy += rand.nextInt(2) + 1) {
				BlockPos pos = new BlockPos(xx, yy, zz);
				if (world.getBlockState(pos) == ModBlocks.VOLCANIC_ROCK.getDefaultState() && world.isAirBlock(pos.up())) {
					if (rand.nextInt(3) == 0)
						world.setBlockState(pos, ModBlocks.UMBERGRAVEL.getDefaultState(), 2);
					if (rand.nextInt(3) == 0)
						world.setBlockState(pos, ModBlocks.DUST.getDefaultState(), 2);
					break;
				}
			}
		}

        for (attempt = 0; attempt < 3; attempt++) {
        	xx = x + offsetXZ();
        	yy = rand.nextInt(100);
        	zz = z + offsetXZ();
        	BlockPos pos = new BlockPos(xx, yy, zz);
            rockSpike.generate(world, rand, pos.up());
        }

		for (attempt = 0; attempt < 240; attempt++) {
			xx = x + offsetXZ();
			zz = z + offsetXZ();

			for (yy = 20; yy < 100; yy += rand.nextInt(2) + 1) {
				BlockPos pos = new BlockPos(xx, yy, zz);
				if ((world.getBlockState(pos) == ModBlocks.VOLCANIC_ROCK.getDefaultState() || world.getBlockState(pos) == ModBlocks.PETRIFIED_WOOD_ROCK.getDefaultState())  && world.isAirBlock(pos.up())) {
					world.setBlockState(pos.up(), ModBlocks.DUST_LAYER.getDefaultState().withProperty(BlockDustLayer.LAYERS, rand.nextInt(3) + 1), 2);
					break;
				}
			}
		}
	
		for (attempt = 0; attempt < 22; attempt++) {
			xx = x + offsetXZ();
			yy = rand.nextInt(120);
			zz = z + offsetXZ();
			BlockPos pos = new BlockPos(xx, yy, zz);
			if (checkSurface(SurfaceType.VOLCANIC_ROCK, pos) && !world.isAirBlock(pos.down(2))) {
				genScorchedWood.generate(world, rand, pos);
				if (rand.nextInt(4) != 0)
					break;
			}
		}
		
		int offset;
		for (attempt = 0; attempt < 800; attempt++) {
			xx = x + offsetXZ();
			yy = 30 + rand.nextInt(80);
			zz = z + offsetXZ();
			BlockPos pos = new BlockPos(xx, yy, zz);
			if (world.isAirBlock(pos)) {
				offset = rand.nextInt(4);

				if (!world.getBlockState(new BlockPos(xx + offsetX[offset], yy, zz + offsetZ[offset])).isNormalCube())
					continue;

				for (int vineY = rand.nextInt(30); vineY > 0; vineY--)
					if (world.isAirBlock(new BlockPos(xx + offsetX[offset], yy - vineY, zz + offsetZ[offset]))) {
						if (offset == 3)
							world.setBlockState(new BlockPos(xx + offsetX[offset], yy - vineY, zz + offsetZ[offset]), ModBlocks.THORNS.getDefaultState().withProperty(BlockThorns.SOUTH, true), 2);
						if (offset == 2)
							world.setBlockState(new BlockPos(xx + offsetX[offset], yy - vineY, zz + offsetZ[offset]), ModBlocks.THORNS.getDefaultState().withProperty(BlockThorns.NORTH, true), 2);
						if (offset == 1)
							world.setBlockState(new BlockPos(xx + offsetX[offset], yy - vineY, zz + offsetZ[offset]), ModBlocks.THORNS.getDefaultState().withProperty(BlockThorns.EAST, true), 2);
						if (offset == 0)
							world.setBlockState(new BlockPos(xx + offsetX[offset], yy - vineY, zz + offsetZ[offset]), ModBlocks.THORNS.getDefaultState().withProperty(BlockThorns.WEST, true), 2);
					}
			}
		}
					
	}

	@Override
	public void generateFeature(FeatureType featureType) {
		if (featureType == FeatureType.RED_GEM)
			for (attempt = 0; attempt < 10; attempt++)
				genRedGem.generate(world, rand, new BlockPos(x + offsetXZ(), rand.nextInt(64), z + offsetXZ()));
		else
			super.generateFeature(featureType);
	}

	@Override
	@SuppressWarnings("incomplete-switch")
	protected void modifyOreGen(OreSettings oreGen, OreType oreType, boolean extraOres) {
		switch (oreType) {
			case COAL:
				oreGen.setChance(0F);
				break;
			case GOLD:
				oreGen.setIterations(extraOres ? 1 : 2, extraOres ? 2 : 3);
				break; // less common
			case DIAMOND:
				oreGen.setType(OreType.DIAMOND_ENCRUSTED).setChance(0.4F).setIterations(1, 2).setOreAmount(2).setY(5, 16);
				break; // clusters of 4, ~7 times smaller area thus lowered chance and iterations
			case JADE:
				oreGen.setIterations(0, 2);
				break; // less common
			case FOSSIL:
				oreGen.setChance(0.25F).setIterations(0, 1);
				break; // much more rare
		}
	}
}