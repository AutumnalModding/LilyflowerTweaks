package xyz.lilyflower.solaris.mixin.lotr.client;

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
     * @author LilyflowerFDL
     * @reason Fix scrap trader rendering
     */
    @Overwrite
    public void doRender(EntityLiving entity, double d, double d1, double d2, float f, float f1) {
        if (LOTRTickHandlerClient.scrapTraderMisbehaveTick > 0) {
            int r = 3;
            for (int i = -r; i <= r; ++i) {
                for (int k = -r; k <= r; ++k) {
                    if (Math.abs(i) + Math.abs(k) <= 2) continue;
                    GL11.glPushMatrix();
                    GL11.glScalef(1.0f, 3.0f, 1.0f);
                    double g = 6.0;
                    super.doRender(entity, (double)i * g, 0.0, (double)k * g, f, f1);
                    GL11.glPopMatrix();
                }
            }
            GL11.glPushMatrix();
            float s = 6.0f;
            GL11.glScalef(1.0f, s, 1.0f);
            GL11.glColor3f(0.0f, 0.0f, 0.0f);
            super.doRender(entity, d, d1 / s, d2, f, f1);
            GL11.glPopMatrix();
            return;
        }
        super.doRender(entity, d, d1, d2, f, f1);
    }
}
