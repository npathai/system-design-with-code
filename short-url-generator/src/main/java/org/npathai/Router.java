package org.npathai;

import org.npathai.api.UrlExpanderAPI;
import org.npathai.api.UrlShortenerAPI;
import org.npathai.cache.RedisRedirectionCache;
import org.npathai.client.IdGenerationServiceClient;
import org.npathai.controller.RedirectionController;
import org.npathai.dao.MySqlRedirectionDao;
import org.npathai.discovery.ServiceDiscoveryClient;
import org.npathai.discovery.zookeeper.ZkServiceDiscoveryClientFactory;
import org.npathai.domain.UrlShortener;
import org.npathai.util.NullSafe;
import org.npathai.util.Stoppable;
import org.npathai.zookeeper.ZkManager;
import spark.Spark;

import java.time.Clock;
import java.util.Properties;

public class Router implements Stoppable {

    private final Properties applicationProperties;
    private final ZkManager zkManager;
    private IdGenerationServiceClient idGenerationServiceClient;
    private ZkServiceDiscoveryClientFactory zkServiceDiscoveryClientFactory;
    private RedisRedirectionCache redirectionCache;

    public Router(Properties applicationProperties, ZkManager zkManager) {
        this.applicationProperties = applicationProperties;
        this.zkManager = zkManager;
    }

    // FIXME starting to become Object Mother. Need to do damage control
    public void initRoutes() throws Exception {
        zkServiceDiscoveryClientFactory = new ZkServiceDiscoveryClientFactory(zkManager);
        Properties properties = new Properties();
        properties.setProperty(ZkServiceDiscoveryClientFactory.Z_NODE_PATH, "/instances");
        ServiceDiscoveryClient idGenServiceDiscoveryClient =
                zkServiceDiscoveryClientFactory.createDiscoveryClient("id-gen-service", properties);

        idGenerationServiceClient = new IdGenerationServiceClient(idGenServiceDiscoveryClient);

        redirectionCache = new RedisRedirectionCache(applicationProperties);

        UrlShortener urlShortener = new UrlShortener(idGenerationServiceClient,
                new MySqlRedirectionDao(applicationProperties),
                redirectionCache, Clock.systemDefaultZone());
        UrlShortenerAPI urlShortenerApi = new UrlShortenerAPI(urlShortener);
        UrlExpanderAPI urlExpanderAPI = new UrlExpanderAPI(urlShortener);
        RedirectionController redirectionController = new RedirectionController(urlShortener);


        Spark.post("/shorten", (req, res) -> urlShortenerApi.shorten(req, res));
        Spark.get("/expand/:id", (req, res) -> urlExpanderAPI.expand(req, res));
        Spark.get("/:id", (req, res) -> redirectionController.handle(req, res));
    }

    @Override
    public void stop() throws Exception {
        NullSafe.ifNotNull(idGenerationServiceClient, IdGenerationServiceClient::stop);
        NullSafe.ifNotNull(redirectionCache, RedisRedirectionCache::close);
    }
}