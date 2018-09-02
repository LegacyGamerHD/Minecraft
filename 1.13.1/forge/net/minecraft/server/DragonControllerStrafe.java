package net.minecraft.server;

import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DragonControllerStrafe extends AbstractDragonController {
    private static final Logger b = LogManager.getLogger();
    private int c;
    private PathEntity d;
    private Vec3D e;
    private EntityLiving f;
    private boolean g;

    public DragonControllerStrafe(EntityEnderDragon entityenderdragon) {
        super(entityenderdragon);
    }

    public void c() {
        if (this.f == null) {
            b.warn("Skipping player strafe phase because no player was found");
            this.a.getDragonControllerManager().setControllerPhase(DragonControllerPhase.a);
        } else {
            if (this.d != null && this.d.b()) {
                double d0 = this.f.locX;
                double d1 = this.f.locZ;
                double d2 = d0 - this.a.locX;
                double d3 = d1 - this.a.locZ;
                double d4 = (double)MathHelper.sqrt(d2 * d2 + d3 * d3);
                double d5 = Math.min((double)0.4F + d4 / 80.0D - 1.0D, 10.0D);
                this.e = new Vec3D(d0, this.f.locY + d5, d1);
            }

            double d12 = this.e == null ? 0.0D : this.e.c(this.a.locX, this.a.locY, this.a.locZ);
            if (d12 < 100.0D || d12 > 22500.0D) {
                this.j();
            }

            double d13 = 64.0D;
            if (this.f.h(this.a) < 4096.0D) {
                if (this.a.hasLineOfSight(this.f)) {
                    ++this.c;
                    Vec3D vec3d1 = (new Vec3D(this.f.locX - this.a.locX, 0.0D, this.f.locZ - this.a.locZ)).a();
                    Vec3D vec3d = (new Vec3D((double)MathHelper.sin(this.a.yaw * ((float)Math.PI / 180F)), 0.0D, (double)(-MathHelper.cos(this.a.yaw * ((float)Math.PI / 180F))))).a();
                    float f1 = (float)vec3d.b(vec3d1);
                    float fx = (float)(Math.acos((double)f1) * (double)(180F / (float)Math.PI));
                    fx = fx + 0.5F;
                    if (this.c >= 5 && fx >= 0.0F && fx < 10.0F) {
                        double d14 = 1.0D;
                        Vec3D vec3d2 = this.a.f(1.0F);
                        double d6 = this.a.bD.locX - vec3d2.x * 1.0D;
                        double d7 = this.a.bD.locY + (double)(this.a.bD.length / 2.0F) + 0.5D;
                        double d8 = this.a.bD.locZ - vec3d2.z * 1.0D;
                        double d9 = this.f.locX - d6;
                        double d10 = this.f.locY + (double)(this.f.length / 2.0F) - (d7 + (double)(this.a.bD.length / 2.0F));
                        double d11 = this.f.locZ - d8;
                        this.a.world.a((EntityHuman)null, 1017, new BlockPosition(this.a), 0);
                        EntityDragonFireball entitydragonfireball = new EntityDragonFireball(this.a.world, this.a, d9, d10, d11);
                        entitydragonfireball.setPositionRotation(d6, d7, d8, 0.0F, 0.0F);
                        this.a.world.addEntity(entitydragonfireball);
                        this.c = 0;
                        if (this.d != null) {
                            while(!this.d.b()) {
                                this.d.a();
                            }
                        }

                        this.a.getDragonControllerManager().setControllerPhase(DragonControllerPhase.a);
                    }
                } else if (this.c > 0) {
                    --this.c;
                }
            } else if (this.c > 0) {
                --this.c;
            }

        }
    }

    private void j() {
        if (this.d == null || this.d.b()) {
            int i = this.a.l();
            int j = i;
            if (this.a.getRandom().nextInt(8) == 0) {
                this.g = !this.g;
                j = i + 6;
            }

            if (this.g) {
                ++j;
            } else {
                --j;
            }

            if (this.a.ds() != null && this.a.ds().c() > 0) {
                j = j % 12;
                if (j < 0) {
                    j += 12;
                }
            } else {
                j = j - 12;
                j = j & 7;
                j = j + 12;
            }

            this.d = this.a.a(i, j, (PathPoint)null);
            if (this.d != null) {
                this.d.a();
            }
        }

        this.k();
    }

    private void k() {
        if (this.d != null && !this.d.b()) {
            Vec3D vec3d = this.d.f();
            this.d.a();
            double d0 = vec3d.x;
            double d2 = vec3d.z;

            double d1;
            while(true) {
                d1 = vec3d.y + (double)(this.a.getRandom().nextFloat() * 20.0F);
                if (!(d1 < vec3d.y)) {
                    break;
                }
            }

            this.e = new Vec3D(d0, d1, d2);
        }

    }

    public void d() {
        this.c = 0;
        this.e = null;
        this.d = null;
        this.f = null;
    }

    public void a(EntityLiving entityliving) {
        this.f = entityliving;
        int i = this.a.l();
        int j = this.a.k(this.f.locX, this.f.locY, this.f.locZ);
        int k = MathHelper.floor(this.f.locX);
        int l = MathHelper.floor(this.f.locZ);
        double d0 = (double)k - this.a.locX;
        double d1 = (double)l - this.a.locZ;
        double d2 = (double)MathHelper.sqrt(d0 * d0 + d1 * d1);
        double d3 = Math.min((double)0.4F + d2 / 80.0D - 1.0D, 10.0D);
        int i1 = MathHelper.floor(this.f.locY + d3);
        PathPoint pathpoint = new PathPoint(k, i1, l);
        this.d = this.a.a(i, j, pathpoint);
        if (this.d != null) {
            this.d.a();
            this.k();
        }

    }

    @Nullable
    public Vec3D g() {
        return this.e;
    }

    public DragonControllerPhase<DragonControllerStrafe> getControllerPhase() {
        return DragonControllerPhase.b;
    }
}