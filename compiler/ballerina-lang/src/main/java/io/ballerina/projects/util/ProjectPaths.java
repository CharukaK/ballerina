/*
 *  Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package io.ballerina.projects.util;

import io.ballerina.projects.Project;
import io.ballerina.projects.ProjectException;
import org.wso2.ballerinalang.compiler.util.ProjectDirConstants;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static io.ballerina.projects.util.ProjectConstants.BALLERINA_TOML;

/**
 * Consists of static methods that may be used to obtain {@link Project}
 * information using file paths.
 *
 * @since 2.0.0
 */
public class ProjectPaths {

    /**
     * Finds the root directory of a Ballerina package using the filepath provided.
     *
     * @param filepath ballerina file that belongs to a package
     * @return path to the package root directory
     * @throws ProjectException if the provided path is invalid or if it is a standalone ballerina file
     */
    public static Path packageRoot(Path filepath) throws ProjectException {
        // check if the file exists
        if (!Files.exists(filepath)) {
            throw new ProjectException("provided path does not exist");
        }

        // check if the file is a regular file
        if (!Files.isRegularFile(filepath)) {
            throw new ProjectException("provided path is not a regular file");
        }

        // check if the file is the Ballerina.toml file
        if (Optional.of(filepath.getFileName()).get().toString().equals(ProjectConstants.BALLERINA_TOML)) {
            return filepath.getParent();
        }

        if (!isBalFile(filepath)) {
            throw new ProjectException("provided path is not a valid Ballerina source file");
        }
        Path absFilePath = filepath.toAbsolutePath().normalize();

        // check if the file is a source file in the default module
        if (isDefaultModuleSrcFile(absFilePath)) {
            return absFilePath.getParent();
        }
        // check if the file is a test file in the default module
        if (isDefaultModuleTestFile(absFilePath)) {
            Path testsRoot = Optional.of(absFilePath.getParent()).get();
            return testsRoot.getParent();
        }
        // check if the file is a source file in a non-default module
        if (isNonDefaultModuleSrcFile(filepath)) {
            Path modulesRoot = Optional.of(Optional.of(absFilePath.getParent()).get().getParent()).get();
            return modulesRoot.getParent();
        }
        // check if the file is a test file in a non-default module
        if (isNonDefaultModuleTestFile(filepath)) {
            Path testsRoot = Optional.of(absFilePath.getParent()).get();
            Path modulesRoot = Optional.of(Optional.of(testsRoot.getParent()).get().getParent()).get();
            return modulesRoot.getParent();
        }

        throw new ProjectException("provided file path does not belong to a Ballerina package");
    }

    /**
     * Returns whether the provided path is a valid Ballerina source file.
     *
     * @param filepath Ballerina file path
     * @return true if the path is a Ballerina source file
     */
    public static boolean isBalFile(Path filepath) {
        return Files.exists(filepath)
                && Files.isRegularFile(filepath)
                && filepath.toString().endsWith(ProjectDirConstants.BLANG_SOURCE_EXT);
    }

    /**
     * Checks if the provided path is a standalone filepath.
     *
     * @param filepath path to bal filepath
     * @return true if the filepath is a standalone bal filepath
     */
    public static boolean isStandaloneBalFile(Path filepath) {
        // Check if the filepath is a valid Ballerina source filepath
        if (!isBalFile(filepath)) {
            return false;
        }

        // check if the filepath is a source filepath in the default module
        if (isDefaultModuleSrcFile(filepath)) {
            return false;
        }
        // check if the filepath is a test filepath in the default module
        if (isDefaultModuleTestFile(filepath)) {
            return false;
        }
        // check if the filepath is a source filepath in a non-default module
        if (isNonDefaultModuleSrcFile(filepath)) {
            return false;
        }
        // check if the filepath is a test filepath in a non-default module
        if (isNonDefaultModuleTestFile(filepath)) {
            return false;
        }

        return true;
    }

    static boolean isDefaultModuleSrcFile(Path filePath) {
        Path absFilePath = filePath.toAbsolutePath().normalize();
        return hasBallerinaToml(Optional.of(absFilePath.getParent()).get());
    }

    static boolean isDefaultModuleTestFile(Path filePath) {
        Path absFilePath = filePath.toAbsolutePath().normalize();
        Path testsRoot = Optional.of(absFilePath.getParent()).get();
        Path projectRoot = Optional.of(testsRoot.getParent()).get();
        return ProjectConstants.TEST_DIR_NAME.equals(testsRoot.toFile().getName())
                && hasBallerinaToml(projectRoot);
    }

    static boolean isNonDefaultModuleSrcFile(Path filePath) {
        Path absFilePath = filePath.toAbsolutePath().normalize();
        Path modulesRoot = Optional.of(Optional.of(absFilePath.getParent()).get().getParent()).get();
        Path projectRoot = modulesRoot.getParent();
        return ProjectConstants.MODULES_ROOT.equals(modulesRoot.toFile().getName())
                && hasBallerinaToml(projectRoot);
    }

    static boolean isNonDefaultModuleTestFile(Path filePath) {
        Path absFilePath = filePath.toAbsolutePath().normalize();
        Path testsRoot = Optional.of(absFilePath.getParent()).get();
        Path modulesRoot = Optional.of(Optional.of(testsRoot.getParent()).get().getParent()).get();
        Path projectRoot = modulesRoot.getParent();
        return ProjectConstants.MODULES_ROOT.equals(modulesRoot.toFile().getName())
                && hasBallerinaToml(projectRoot);
    }

    private static boolean hasBallerinaToml(Path filePath) {
        Path absFilePath = filePath.toAbsolutePath().normalize();
        return absFilePath.resolve(BALLERINA_TOML).toFile().exists();
    }

    private static Path findProjectRoot(Path filePath) {
        if (filePath != null) {
            filePath = filePath.toAbsolutePath().normalize();
            if (filePath.toFile().isDirectory()) {
                if (hasBallerinaToml(filePath)) {
                    return filePath;
                }
            }
            return findProjectRoot(filePath.getParent());
        }
        return null;
    }
}
