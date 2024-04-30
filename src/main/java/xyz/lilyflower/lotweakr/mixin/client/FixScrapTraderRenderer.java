package xyz.lilyflower.lotweakr.mixin.client;

import lotr.client.LOTRTickHandlerClient;
import lotr.client.render.entity.LOTRRenderBiped;
import lotr.client.render.entity.LOTRRenderScrapTrader;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LOTRRenderScrapTrader.class)
public abstract class FixScrapTraderRenderer extends LOTRRenderBiped {
    public FixScrapTraderRenderer(ModelBiped model, float f) {
        super(model, f);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void doRender(EntityLiving entity, double d, double d1, double d2, float f, float f1) {
        if (LOTRTickHandlerClient.scrapTraderMisbehaveTick > 0) {
            int r = 3;
            for (int i = -r; i <= r; ++i) {
                for (int k = -r; k <= r; ++k) {
                    if (Math.abs(i) + Math.abs(k) <= 2) continue;
                    GL11.glPushMatrix();
                    GL11.glScalef((float)1.0f, (float)3.0f, (float)1.0f);
                    double g = 6.0;
                    super.doRender(entity, (double)i * g, 0.0, (double)k * g, f, f1);
                    GL11.glPopMatrix();
                }
            }
            GL11.glPushMatrix();
            float s = 6.0f;
            GL11.glScalef((float)1.0f, (float)s, (float)1.0f);
            GL11.glColor3f((float)0.0f, (float)0.0f, (float)0.0f);
            super.doRender(entity, d, d1 /= (double)s, d2, f, f1);
            GL11.glPopMatrix();
            return;
        }
        super.doRender(entity, d, d1, d2, f, f1);
    }
}
