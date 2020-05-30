package org.npathai;

import org.npathai.api.IdGeneratorAPI;
import org.npathai.domain.IdGenerator;
import spark.Spark;

public class Router {

    public static void initRoutes() {
        IdGeneratorAPI idGeneratorAPI = new IdGeneratorAPI(new IdGenerator());
        Spark.get("/generate", (req, res) -> idGeneratorAPI.generate(req, res));
    }
}
