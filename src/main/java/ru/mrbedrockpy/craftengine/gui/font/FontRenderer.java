package ru.mrbedrockpy.craftengine.gui.font;

import lombok.Getter;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL46C.*;
import static org.lwjgl.stb.STBTruetype.*;

@Getter
public class FontRenderer {
    private static final int BITMAP_W = 512;
    private static final int BITMAP_H = 512;
    private static final int FONT_SIZE = 32;

    private int textureId;
    private final STBTTPackedchar.Buffer charData = STBTTPackedchar.malloc(96);

    private ByteBuffer fontBuffer;
    private STBTTFontinfo fontInfo;
    private float scale;
    private int ascent;

    public void init(String ttfPath) throws IOException {
        byte[] bytes = Files.readAllBytes(Path.of(ttfPath));
        fontBuffer = BufferUtils.createByteBuffer(bytes.length);
        fontBuffer.put(bytes).flip();

        fontInfo = STBTTFontinfo.create();
        if (!stbtt_InitFont(fontInfo, fontBuffer)) {
            throw new IllegalStateException("Could not init font info");
        }

        scale = stbtt_ScaleForPixelHeight(fontInfo, FONT_SIZE);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pAscent = stack.mallocInt(1);
            stbtt_GetFontVMetrics(fontInfo, pAscent, null, null);
            ascent = pAscent.get(0);
        }

        ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            STBTTPackContext pack = STBTTPackContext.malloc(stack);
            stbtt_PackBegin(pack, bitmap, BITMAP_W, BITMAP_H, 0, 1, 0);
            stbtt_PackFontRange(pack, fontBuffer, 0, FONT_SIZE, 32, charData);
            stbtt_PackEnd(pack);
        }

        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, BITMAP_W, BITMAP_H, 0, GL_RED, GL_UNSIGNED_BYTE, bitmap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void dispose() {
        glDeleteTextures(textureId);
        charData.free();
    }
}