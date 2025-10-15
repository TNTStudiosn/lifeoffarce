package com.TNTStudios.lifeoffarce.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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

public class ElGiganteEntity extends Monster implements GeoEntity {
    // Cache para las animaciones, optimiza el rendimiento.
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public ElGiganteEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    // Aquí defino los atributos base de la entidad.
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.20D) // Un poco más lento que un zombie (0.23D)
                .add(Attributes.FOLLOW_RANGE, 35.0D);
    }

    // Aquí registro la IA de la entidad.
    @Override
    protected void registerGoals() {
        // Le doy prioridades a las acciones que debe tomar.
        this.goalSelector.addGoal(0, new FloatGoal(this)); // Flotar si cae al agua.
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false)); // Atacar cuerpo a cuerpo.
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8D)); // Caminar evitando el agua.
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F)); // Mirar al jugador cercano.
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this)); // Mirar a su alrededor.

        // Defino a quién debe atacar.
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    // Lógica para quemarse con el sol, como los zombies.
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

    // --- ANIMACIONES CON GECKOLIB ---

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        // Añado un controlador de animación.
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    // Este método decide qué animación reproducir.
    private <T extends GeoEntity> PlayState predicate(AnimationState<T> tAnimationState) {
        // Si la entidad se está moviendo, reproduzco la animación de caminar.
        if(tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.elgigante.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        // Si no se mueve, reproduzco la animación de estar quieto.
        tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.elgigante.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}