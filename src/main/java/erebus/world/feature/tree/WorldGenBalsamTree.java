package erebus.world.feature.tree;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import erebus.lib.EnumWood;

public class WorldGenBalsamTree extends WorldGenTreeBase {

	public WorldGenBalsamTree() {
		super(EnumWood.BALSAM);
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		int height = rand.nextInt(4) + 12;
		int maxRadius = 5;
		boolean alternate = rand.nextBoolean();

		for (int xx = x - maxRadius; xx <= x + maxRadius; xx++)
			for (int zz = z - maxRadius; zz <= z + maxRadius; zz++)
				for (int yy = y + 2; yy < y + height; yy++)
					if (!world.isAirBlock(new BlockPos(xx, yy, zz)))
						return false;

		for (int yy = y; yy < y + height; ++yy) {

			if (yy < y + height -1)
				world.setBlockState(new BlockPos(x, yy, z), log.getStateFromMeta(0), 2);

			if (yy == y + height - 1) {
				createLeaves(world, x, yy, z);
				placeLeaves(world, x, yy + 1, z);
			}

			if (yy == y + height - 7 || yy == y + height - 10) {
				if (alternate) {
					createBranch(world, rand, x + 1, yy - rand.nextInt(2), z, 1, 1);
					createBranch(world, rand, x - 1, yy - rand.nextInt(2), z, 2, 1);
					alternate = false;

				} else {
					createBranch(world, rand, x, yy - rand.nextInt(2), z + 1, 3, 1);
					createBranch(world, rand, x, yy - rand.nextInt(2), z - 1, 4, 1);
					alternate = true;
				}
			}

			if (yy == y + height - 4) {
				if (alternate) {
					createBranch(world, rand, x + 1, yy - rand.nextInt(2), z, 1, 1);
					createBranch(world, rand, x - 1, yy - rand.nextInt(2), z, 2, 1);
					alternate = false;

				} else {
					createBranch(world, rand, x, yy - rand.nextInt(2), z + 1, 3, 1);
					createBranch(world, rand, x, yy - rand.nextInt(2), z - 1, 4, 1);
					alternate = true;
				}
			}
		}
		return true;
	}

	private void createBranch(World world, Random rand, int x, int y, int z, int dir, int branchLength) {
		int meta = dir;
		for (int i = 0; i <= branchLength; ++i) {

			if (i >= 1) {
				y++;
				meta = 0;
			}

			if (dir == 1) {
				world.setBlockState(new BlockPos(x + i, y, z), log.getStateFromMeta(meta == 0 ? 0 : 4), 2);
				if (i == branchLength) {
					createLeaves(world, x + i, y, z);
					placeLeaves(world, x + i + 2, y, z);
				}
			}

			if (dir == 2) {
				world.setBlockState(new BlockPos(x - i, y, z), log.getStateFromMeta(meta == 0 ? 0 : 4), 2);
				if (i == branchLength) {
					createLeaves(world, x - i, y, z);
					placeLeaves(world, x - i - 2, y, z);
				}
			}

			if (dir == 3) {
				world.setBlockState(new BlockPos(x, y, z + i), log.getStateFromMeta(meta == 0 ? 0 : 8), 2);
				if (i == branchLength) {
					createLeaves(world, x, y, z + i);
					placeLeaves(world, x, y, z + i + 2);
				}
			}

			if (dir == 4) {
				world.setBlockState(new BlockPos(x, y, z - i), log.getStateFromMeta(meta == 0 ? 0 : 8), 2);
				if (i == branchLength) {
					createLeaves(world, x, y, z - i);
					placeLeaves(world, x, y, z - i - 2);
				}
			}
		}
	}

	private void createLeaves(World world, int x, int y, int z) {
		for (int d = -1; d < 2; d++)
			for (int d2 = -1; d2 < 2; d2++)
				placeLeaves(world, x + d, y, z + d2);
	}

	private void placeLeaves(World world, int x, int y, int z) {
		if (world.isAirBlock(new BlockPos(x, y, z)))
			world.setBlockState(new BlockPos(x, y, z), leaves.getStateFromMeta(0), 2);
	}

}