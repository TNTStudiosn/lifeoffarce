package com.TNTStudios.lifeoffarce.entity.client;

import com.TNTStudios.lifeoffarce.entity.custom.ElGiganteEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ElGiganteRenderer extends GeoEntityRenderer<ElGiganteEntity> {
    public ElGiganteRenderer(EntityRendererProvider.Context renderManager) {
        // Le paso el modelo que acabamos de crear.
        super(renderManager, new ElGiganteModel());
        // Defino el tamaño de la sombra.
        this.shadowRadius = 0.8f;
    }
}