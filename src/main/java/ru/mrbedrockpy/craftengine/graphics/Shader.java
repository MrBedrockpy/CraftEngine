package ru.mrbedrockpy.craftengine.graphics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL46C.*;

@Getter
@AllArgsConstructor
public class Shader implements Disposable {

    private final int id;

    public static Shader load(String vertexPath, String fragmentPath) {
        int programID = glCreateProgram();
        int vertexShader = compileShader(vertexPath, GL_VERTEX_SHADER);
        int fragmentShader = compileShader(fragmentPath, GL_FRAGMENT_SHADER);
        glAttachShader(programID, vertexShader);
        glAttachShader(programID, fragmentShader);
        glLinkProgram(programID);
        checkProgramStatus(programID, GL_LINK_STATUS);
        glValidateProgram(programID);
        checkProgramStatus(programID, GL_VALIDATE_STATUS);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        return new Shader(programID);
    }

    private static int compileShader(String filePath, int shaderType) {
        String shaderCode;
        try {
            shaderCode = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load shader file: " + filePath, e);
        }
        int shaderID = glCreateShader(shaderType);
        if (shaderID == 0) throw new RuntimeException("Shader creation failed for: " + filePath);
        glShaderSource(shaderID, shaderCode);
        glCompileShader(shaderID);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer success = stack.mallocInt(1);
            glGetShaderiv(shaderID, GL_COMPILE_STATUS, success);
            if (success.get(0) == GL_FALSE) {
                String log = glGetShaderInfoLog(shaderID);
                throw new RuntimeException("Shader compilation failed: " + filePath + "\n" + log);
            }
        }
        return shaderID;
    }

    private static void checkProgramStatus(int programID, int statusType) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer success = stack.mallocInt(1);
            glGetProgramiv(programID, statusType, success);
            if (success.get(0) == GL_FALSE) {
                String log = glGetProgramInfoLog(programID);
                String statusName = (statusType == GL_LINK_STATUS) ? "Linking" : "Validation";
                throw new RuntimeException("Program " + statusName + " failed:\n" + log);
            }
        }
    }

    public void use() {
        glUseProgram(id);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void setUniformMatrix4f(String name, Matrix4f matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);
            matrix.get(buffer);
            int location = glGetUniformLocation(id, name);
            if (location != -1) {
                glUniformMatrix4fv(location, false, buffer);
            } else {
                System.err.println("Warning: uniform '" + name + "' not found in shader program.");
            }
        }
    }

    @Override
    public void dispose() {
        glDeleteProgram(id);
    }
}
