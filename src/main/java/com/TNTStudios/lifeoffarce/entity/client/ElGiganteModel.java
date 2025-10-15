package com.TNTStudios.lifeoffarce.entity.client;

import com.TNTStudios.lifeoffarce.Lifeoffarce;
import com.TNTStudios.lifeoffarce.entity.custom.ElGiganteEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ElGiganteModel extends GeoModel<ElGiganteEntity> {

    // Le digo a GeckoLib dónde está el archivo del modelo.
    @Override
    public ResourceLocation getModelResource(ElGiganteEntity animatable) {
        return new ResourceLocation(Lifeoffarce.MODID, "geo/el_gigante.geo.json");
    }

    // Le indico cuál es la textura que debe usar.
    @Override
    public ResourceLocation getTextureResource(ElGiganteEntity animatable) {
        return new ResourceLocation(Lifeoffarce.MODID, "textures/entity/el_gigante.png");
    }

    // Y finalmente, le paso el archivo de animaciones.
    @Override
    public ResourceLocation getAnimationResource(ElGiganteEntity animatable) {
        return new ResourceLocation(Lifeoffarce.MODID, "animations/el_gigante.animation.json");
    }
}