package net.minecraft.server;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.math.DoubleMath;
import com.google.common.math.IntMath;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class VoxelShapes {

    private static final VoxelShape a = new VoxelShapeArray(new VoxelShapeBitSet(0, 0, 0), new DoubleArrayList(new double[] { 0.0D}), new DoubleArrayList(new double[] { 0.0D}), new DoubleArrayList(new double[] { 0.0D}));
    private static final VoxelShape b = (VoxelShape) SystemUtils.a(() -> {
        VoxelShapeBitSet voxelshapebitset = new VoxelShapeBitSet(1, 1, 1);

        voxelshapebitset.a(0, 0, 0, true, true);
        return new VoxelShapeCube(voxelshapebitset);
    });

    public static VoxelShape a() {
        return VoxelShapes.a;
    }

    public static VoxelShape b() {
        return VoxelShapes.b;
    }

    public static VoxelShape a(double d0, double d1, double d2, double d3, double d4, double d5) {
        return a(new AxisAlignedBB(d0, d1, d2, d3, d4, d5));
    }

    public static VoxelShape a(AxisAlignedBB axisalignedbb) {
        int i = a(axisalignedbb.a, axisalignedbb.d);
        int j = a(axisalignedbb.b, axisalignedbb.e);
        int k = a(axisalignedbb.c, axisalignedbb.f);

        if (i >= 0 && j >= 0 && k >= 0) {
            if (i == 0 && j == 0 && k == 0) {
                return axisalignedbb.e(0.5D, 0.5D, 0.5D) ? b() : a();
            } else {
                int l = 1 << i;
                int i1 = 1 << j;
                int j1 = 1 << k;
                int k1 = (int) Math.round(axisalignedbb.a * (double) l);
                int l1 = (int) Math.round(axisalignedbb.d * (double) l);
                int i2 = (int) Math.round(axisalignedbb.b * (double) i1);
                int j2 = (int) Math.round(axisalignedbb.e * (double) i1);
                int k2 = (int) Math.round(axisalignedbb.c * (double) j1);
                int l2 = (int) Math.round(axisalignedbb.f * (double) j1);
                VoxelShapeBitSet voxelshapebitset = new VoxelShapeBitSet(l, i1, j1, k1, i2, k2, l1, j2, l2);

                for (long i3 = (long) k1; i3 < (long) l1; ++i3) {
                    for (long j3 = (long) i2; j3 < (long) j2; ++j3) {
                        for (long k3 = (long) k2; k3 < (long) l2; ++k3) {
                            voxelshapebitset.a((int) i3, (int) j3, (int) k3, false, true);
                        }
                    }
                }

                return new VoxelShapeCube(voxelshapebitset);
            }
        } else {
            return new VoxelShapeArray(VoxelShapes.b.a, new double[] { axisalignedbb.a, axisalignedbb.d}, new double[] { axisalignedbb.b, axisalignedbb.e}, new double[] { axisalignedbb.c, axisalignedbb.f});
        }
    }

    private static int a(double d0, double d1) {
        if (d0 >= -1.0E-7D && d1 <= 1.0000001D) {
            for (int i = 0; i <= 3; ++i) {
                double d2 = d0 * (double) (1 << i);
                double d3 = d1 * (double) (1 << i);
                boolean flag = Math.abs(d2 - Math.floor(d2)) < 1.0E-7D;
                boolean flag1 = Math.abs(d3 - Math.floor(d3)) < 1.0E-7D;

                if (flag && flag1) {
                    return i;
                }
            }

            return -1;
        } else {
            return -1;
        }
    }

    protected static long a(int i, int j) {
        return (long) i * (long) (j / IntMath.gcd(i, j));
    }

    public static VoxelShape a(VoxelShape voxelshape, VoxelShape voxelshape1) {
        return a(voxelshape, voxelshape1, OperatorBoolean.OR);
    }

    public static VoxelShape a(VoxelShape voxelshape, VoxelShape voxelshape1, OperatorBoolean operatorboolean) {
        return b(voxelshape, voxelshape1, operatorboolean).c();
    }

    public static VoxelShape b(VoxelShape voxelshape, VoxelShape voxelshape1, OperatorBoolean operatorboolean) {
        if (operatorboolean.apply(false, false)) {
            throw new IllegalArgumentException();
        } else if (voxelshape == voxelshape1) {
            return operatorboolean.apply(true, true) ? voxelshape : a();
        } else {
            boolean flag = operatorboolean.apply(true, false);
            boolean flag1 = operatorboolean.apply(false, true);

            if (voxelshape.b()) {
                return flag1 ? voxelshape1 : a();
            } else if (voxelshape1.b()) {
                return flag ? voxelshape : a();
            } else {
                VoxelShapeMerger voxelshapemerger = a(1, voxelshape.a(EnumDirection.EnumAxis.X), voxelshape1.a(EnumDirection.EnumAxis.X), flag, flag1);
                VoxelShapeMerger voxelshapemerger1 = a(voxelshapemerger.a().size() - 1, voxelshape.a(EnumDirection.EnumAxis.Y), voxelshape1.a(EnumDirection.EnumAxis.Y), flag, flag1);
                VoxelShapeMerger voxelshapemerger2 = a((voxelshapemerger.a().size() - 1) * (voxelshapemerger1.a().size() - 1), voxelshape.a(EnumDirection.EnumAxis.Z), voxelshape1.a(EnumDirection.EnumAxis.Z), flag, flag1);
                VoxelShapeBitSet voxelshapebitset = VoxelShapeBitSet.a(voxelshape.a, voxelshape1.a, voxelshapemerger, voxelshapemerger1, voxelshapemerger2, operatorboolean);

                return (VoxelShape) (voxelshapemerger instanceof VoxelShapeCubeMerger && voxelshapemerger1 instanceof VoxelShapeCubeMerger && voxelshapemerger2 instanceof VoxelShapeCubeMerger ? new VoxelShapeCube(voxelshapebitset) : new VoxelShapeArray(voxelshapebitset, voxelshapemerger.a(), voxelshapemerger1.a(), voxelshapemerger2.a()));
            }
        }
    }

    public static boolean c(VoxelShape voxelshape, VoxelShape voxelshape1, OperatorBoolean operatorboolean) {
        if (operatorboolean.apply(false, false)) {
            throw new IllegalArgumentException();
        } else if (voxelshape == voxelshape1) {
            return operatorboolean.apply(true, true);
        } else if (voxelshape.b()) {
            return operatorboolean.apply(false, !voxelshape1.b());
        } else if (voxelshape1.b()) {
            return operatorboolean.apply(!voxelshape.b(), false);
        } else {
            boolean flag = operatorboolean.apply(true, false);
            boolean flag1 = operatorboolean.apply(false, true);
            EnumDirection.EnumAxis[] aenumdirection_enumaxis = EnumAxisCycle.d;
            int i = aenumdirection_enumaxis.length;

            for (int j = 0; j < i; ++j) {
                EnumDirection.EnumAxis enumdirection_enumaxis = aenumdirection_enumaxis[j];

                if (voxelshape.c(enumdirection_enumaxis) < voxelshape1.b(enumdirection_enumaxis) - 1.0E-7D) {
                    return flag || flag1;
                }

                if (voxelshape1.c(enumdirection_enumaxis) < voxelshape.b(enumdirection_enumaxis) - 1.0E-7D) {
                    return flag || flag1;
                }
            }

            VoxelShapeMerger voxelshapemerger = a(1, voxelshape.a(EnumDirection.EnumAxis.X), voxelshape1.a(EnumDirection.EnumAxis.X), flag, flag1);
            VoxelShapeMerger voxelshapemerger1 = a(voxelshapemerger.a().size() - 1, voxelshape.a(EnumDirection.EnumAxis.Y), voxelshape1.a(EnumDirection.EnumAxis.Y), flag, flag1);
            VoxelShapeMerger voxelshapemerger2 = a((voxelshapemerger.a().size() - 1) * (voxelshapemerger1.a().size() - 1), voxelshape.a(EnumDirection.EnumAxis.Z), voxelshape1.a(EnumDirection.EnumAxis.Z), flag, flag1);

            return a(voxelshapemerger, voxelshapemerger1, voxelshapemerger2, voxelshape.a, voxelshape1.a, operatorboolean);
        }
    }

    private static boolean a(VoxelShapeMerger voxelshapemerger, VoxelShapeMerger voxelshapemerger1, VoxelShapeMerger voxelshapemerger2, VoxelShapeDiscrete voxelshapediscrete, VoxelShapeDiscrete voxelshapediscrete1, OperatorBoolean operatorboolean) {
        return !voxelshapemerger.a((i, j, k) -> {
            return voxelshapemerger.a((ix, jx, kx) -> {
                return voxelshapemerger.a((ixx, jxx, k) -> {
                    return !operatorboolean.apply(voxelshapediscrete.c(i, ix, ixx), voxelshapediscrete1.c(j, jx, jxx));
                });
            });
        });
    }

    public static double a(EnumDirection.EnumAxis enumdirection_enumaxis, AxisAlignedBB axisalignedbb, Stream<VoxelShape> stream, double d0) {
        for (Iterator iterator = stream.iterator(); iterator.hasNext(); d0 = ((VoxelShape) iterator.next()).a(enumdirection_enumaxis, axisalignedbb, d0)) {
            if (Math.abs(d0) < 1.0E-7D) {
                return 0.0D;
            }
        }

        return d0;
    }

    public static boolean b(VoxelShape voxelshape, VoxelShape voxelshape1, EnumDirection enumdirection) {
        if (voxelshape != b() && voxelshape1 != b()) {
            EnumDirection.EnumAxis enumdirection_enumaxis = enumdirection.k();
            EnumDirection.EnumAxisDirection enumdirection_enumaxisdirection = enumdirection.c();
            VoxelShape voxelshape2 = enumdirection_enumaxisdirection == EnumDirection.EnumAxisDirection.POSITIVE ? voxelshape : voxelshape1;
            VoxelShape voxelshape3 = enumdirection_enumaxisdirection == EnumDirection.EnumAxisDirection.POSITIVE ? voxelshape1 : voxelshape;

            if (!DoubleMath.fuzzyEquals(voxelshape2.c(enumdirection_enumaxis), 1.0D, 1.0E-7D)) {
                voxelshape2 = a();
            }

            if (!DoubleMath.fuzzyEquals(voxelshape3.b(enumdirection_enumaxis), 0.0D, 1.0E-7D)) {
                voxelshape3 = a();
            }

            return !c(b(), b(new VoxelShapeSlice(voxelshape2, enumdirection_enumaxis, voxelshape2.a.c(enumdirection_enumaxis) - 1), new VoxelShapeSlice(voxelshape3, enumdirection_enumaxis, 0), OperatorBoolean.OR), OperatorBoolean.ONLY_FIRST);
        } else {
            return true;
        }
    }

    @VisibleForTesting
    protected static VoxelShapeMerger a(int i, DoubleList doublelist, DoubleList doublelist1, boolean flag, boolean flag1) {
        if (doublelist instanceof VoxelShapeCubePoint && doublelist1 instanceof VoxelShapeCubePoint) {
            int j = doublelist.size() - 1;
            int k = doublelist1.size() - 1;
            long l = a(j, k);

            if ((long) i * l <= 256L) {
                return new VoxelShapeCubeMerger(j, k);
            }
        }

        return (VoxelShapeMerger) (doublelist.getDouble(doublelist.size() - 1) < doublelist1.getDouble(0) - 1.0E-7D ? new VoxelShapeMergerDisjoint(doublelist, doublelist1, false) : (doublelist1.getDouble(doublelist1.size() - 1) < doublelist.getDouble(0) - 1.0E-7D ? new VoxelShapeMergerDisjoint(doublelist1, doublelist, true) : (Objects.equals(doublelist, doublelist1) ? (doublelist instanceof VoxelShapeMergerIdentical ? (VoxelShapeMerger) doublelist : (doublelist1 instanceof VoxelShapeMergerIdentical ? (VoxelShapeMerger) doublelist1 : new VoxelShapeMergerIdentical(doublelist))) : new VoxelShapeMergerList(doublelist, doublelist1, flag, flag1))));
    }

    public interface a {

        void consume(double d0, double d1, double d2, double d3, double d4, double d5);
    }
}