package com.boaglio.springai.vendotudo.tools;

import java.io.File;
import java.nio.file.Paths;

public class FileUtil {

    public static  File getVectorStoreFile(String vectorStoreName) {

        var path = Paths.get("src", "main", "resources");
        var absolutePath = path.toFile().getAbsolutePath() + "/" + vectorStoreName;
        return new File(absolutePath);

    }

}
