package ru.mrbedrockpy.craftengine.graphics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL46C.*;

@Getter
@AllArgsConstructor
public class Texture {

    private final int id;

    private final int width;
    private final int height;

    public static Texture load(String filename) {
        STBImage.stbi_set_flip_vertically_on_load(true);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer widthBuf = stack.mallocInt(1);
            IntBuffer heightBuf = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer imageBuffer = STBImage.stbi_load(filename, widthBuf, heightBuf, channels, 4);
            if (imageBuffer == null) {
                throw new RuntimeException("Image load failed: " + STBImage.stbi_failure_reason());
            }

            int width = widthBuf.get(0);   // читаем один раз
            int height = heightBuf.get(0); // читаем один раз

            int textureId = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureId);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageBuffer);
            glGenerateMipmap(GL_TEXTURE_2D);

            STBImage.stbi_image_free(imageBuffer);
            glBindTexture(GL_TEXTURE_2D, 0);

            return new Texture(textureId, width, height); // безопасно
        }
    }
    public void use() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void dispose() {
        glDeleteTextures(id);
    }
}
