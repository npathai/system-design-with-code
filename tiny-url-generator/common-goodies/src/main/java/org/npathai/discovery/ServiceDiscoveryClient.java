package org.npathai.discovery;

import javax.annotation.Nonnull;
import java.io.Closeable;

public interface ServiceDiscoveryClient extends Closeable {
    @Nonnull Instance getInstance() throws DiscoveryException;
}
