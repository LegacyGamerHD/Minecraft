package net.minecraft.server;

import java.util.Random;

public class BiomeSwamp extends BiomeBase {

    protected static final IBlockData x = Blocks.WATERLILY.getBlockData();

    protected BiomeSwamp(BiomeBase.a biomebase_a) {
        super(biomebase_a);
        this.s.z = 2;
        this.s.B = 1;
        this.s.D = 1;
        this.s.E = 8;
        this.s.F = 10;
        this.s.J = 1;
        this.s.y = 4;
        this.s.I = 0;
        this.s.H = 0;
        this.s.C = 5;
        this.t.add(new BiomeBase.BiomeMeta(EntitySlime.class, 1, 1, 1));
    }

    public WorldGenTreeAbstract a(Random random) {
        return BiomeSwamp.o;
    }

    public BlockFlowers.EnumFlowerVarient a(Random random, BlockPosition blockposition) {
        return BlockFlowers.EnumFlowerVarient.BLUE_ORCHID;
    }

    public void a(World world, Random random, ChunkSnapshot chunksnapshot, int i, int j, double d0) {
        double d1 = BiomeSwamp.k.a((double) i * 0.25D, (double) j * 0.25D);

        if (d1 > 0.0D) {
            int k = i & 15;
            int l = j & 15;

            for (int i1 = 255; i1 >= 0; --i1) {
                if (chunksnapshot.a(l, i1, k).getMaterial() != Material.AIR) {
                    if (i1 == 62 && chunksnapshot.a(l, i1, k).getBlock() != Blocks.WATER) {
                        chunksnapshot.a(l, i1, k, BiomeSwamp.h);
                        if (d1 < 0.12D) {
                            chunksnapshot.a(l, i1 + 1, k, BiomeSwamp.x);
                        }
                    }
                    break;
                }
            }
        }

        this.b(world, random, chunksnapshot, i, j, d0);
    }

    public void a(World world, Random random, BlockPosition blockposition) {
        super.a(world, random, blockposition);
        if (random.nextInt(64) == 0) {
            (new WorldGenFossils()).generate(world, random, blockposition);
        }

    }
}
