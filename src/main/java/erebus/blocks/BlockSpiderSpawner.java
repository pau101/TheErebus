package erebus.blocks;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class BlockSpiderSpawner extends BlockSpawner {

	public BlockSpiderSpawner(ResourceLocation mobName) {
		super(mobName);
		setHarvestLevel("pickaxe", 0);
	}

	@Override
	  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.STRING;
	}

	@Override
	public int quantityDropped(Random rand) {
		return 1 + rand.nextInt(3);
	}
}