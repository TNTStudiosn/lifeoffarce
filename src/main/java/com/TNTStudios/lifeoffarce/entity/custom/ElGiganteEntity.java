package com.TNTStudios.lifeoffarce.entity.custom;

import com.TNTStudios.lifeoffarce.Lifeoffarce; // Asegúrate que esta importación exista
import com.TNTStudios.lifeoffarce.entity.config.EntityStats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Optional;

public class ElGiganteEntity extends Monster implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public ElGiganteEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    // Aquí está la magia. Ahora los atributos se leen desde el gestor de estadísticas.
    public static AttributeSupplier.Builder createAttributes() {
        // Primero, obtengo las estadísticas para este tipo de entidad.
        Optional<EntityStats> statsOptional = Lifeoffarce.ENTITY_STAT_MANAGER.getStats(com.TNTStudios.lifeoffarce.entity.ModEntities.EL_GIGANTE.get());

        // Si encuentro un JSON para la entidad, uso esos valores.
        if (statsOptional.isPresent()) {
            EntityStats stats = statsOptional.get();
            return Monster.createMonsterAttributes()
                    .add(Attributes.MAX_HEALTH, stats.getMaxHealth())
                    .add(Attributes.ATTACK_DAMAGE, stats.getAttackDamage())
                    .add(Attributes.MOVEMENT_SPEED, stats.getMovementSpeed())
                    .add(Attributes.FOLLOW_RANGE, stats.getFollowRange());
        }

        // Si no, uso valores por defecto para evitar que el juego crashee.
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.20D)
                .add(Attributes.FOLLOW_RANGE, 35.0D);
    }

    // El resto de la clase permanece igual...
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void aiStep() {
        if (this.isAlive() && this.level().isDay() && !this.level().isClientSide) {
            float brightness = this.getLightLevelDependentMagicValue();
            if (brightness > 0.5F && this.random.nextFloat() * 30.0F < (brightness - 0.4F) * 2.0F && this.level().canSeeSky(this.blockPosition())) {
                this.setSecondsOnFire(8);
            }
        }
        super.aiStep();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 3, this::predicate));
    }

    private <T extends GeoEntity> PlayState predicate(AnimationState<T> state) {
        if (this.swinging) {
            state.getController().setAnimation(RawAnimation.begin().then("attack", Animation.LoopType.PLAY_ONCE));
            return PlayState.CONTINUE;
        }
        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}