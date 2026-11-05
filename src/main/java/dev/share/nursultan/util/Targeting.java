package dev.share.nursultan.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/** Target selection and aiming helpers. */
public final class Targeting {
    public static final String[] TARGET_MODES = {"both", "players", "hostile", "all"};
    public static final String[] SORT_MODES = {"distance", "health"};

    private Targeting() {
    }

    /**
     * Finds the best target.
     *
     * @param targets      both, players, hostile or all
     * @param sort         distance or health
     * @param fov          field of view in degrees (360 = no limit)
     * @param throughWalls if false, the target must be visible
     */
    public static LivingEntity find(MinecraftClient client, double range, String targets, String sort,
                                    double fov, boolean throughWalls) {
        if (client.player == null || client.world == null) {
            return null;
        }
        ClientPlayerEntity player = client.player;
        LivingEntity best = null;
        double bestScore = Double.MAX_VALUE;

        for (Entity entity : client.world.getEntities()) {
            if (entity == player || !(entity instanceof LivingEntity) || entity instanceof ArmorStandEntity) {
                continue;
            }
            LivingEntity living = (LivingEntity) entity;
            if (!living.isAlive() || !matches(living, targets)) {
                continue;
            }
            double distance = player.distanceTo(entity);
            if (distance > range) {
                continue;
            }
            if (!throughWalls && !player.canSee(entity)) {
                continue;
            }
            if (fov < 360.0) {
                float diff = MathHelper.wrapDegrees(yawTo(player, entity) - player.getYaw());
                if (Math.abs(diff) > fov / 2.0) {
                    continue;
                }
            }
            double score = "health".equals(sort) ? living.getHealth() : distance;
            if (score < bestScore) {
                best = living;
                bestScore = score;
            }
        }
        return best;
    }

    private static boolean matches(LivingEntity entity, String targets) {
        boolean isPlayer = entity instanceof PlayerEntity;
        boolean isHostile = entity instanceof HostileEntity;
        switch (targets) {
            case "players":
                return isPlayer;
            case "hostile":
                return isHostile;
            case "all":
                return true;
            default:
                return isPlayer || isHostile;
        }
    }

    public static float yawTo(ClientPlayerEntity player, Entity target) {
        Vec3d eye = player.getEyePos();
        Vec3d center = target.getBoundingBox().getCenter();
        return (float) (Math.toDegrees(Math.atan2(center.z - eye.z, center.x - eye.x)) - 90.0);
    }

    /** Turns the player's view toward the center of the target. */
    public static void faceEntity(ClientPlayerEntity player, Entity target) {
        Vec3d eye = player.getEyePos();
        Vec3d center = target.getBoundingBox().getCenter();
        double dx = center.x - eye.x;
        double dy = center.y - eye.y;
        double dz = center.z - eye.z;
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        player.setYaw(yawTo(player, target));
        player.setPitch((float) -Math.toDegrees(Math.atan2(dy, horizontal)));
    }
}
