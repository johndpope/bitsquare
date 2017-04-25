package io.bisq.core.provider;

import com.google.inject.Inject;
import io.bisq.core.app.AppOptionKeys;
import io.bisq.network.NetworkOptionKeys;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.util.Random;

public class ProvidersRepository {
    private static final Logger log = LoggerFactory.getLogger(ProvidersRepository.class);

    private final String[] providerArray;
    private String baseUrl;


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Inject
    public ProvidersRepository(@Named(AppOptionKeys.PROVIDERS) String providers,
                               @Named(NetworkOptionKeys.USE_LOCALHOST_FOR_P2P) boolean useLocalhostForP2P) {
        if (providers.isEmpty()) {
            if (useLocalhostForP2P) {
                // If we run in localhost mode we don't have the tor node running, so we need a clearnet host
                // Use localhost for using a locally running provider
                providers = "http://localhost:8080/, 146.185.175.243:8080/";
            } else {
                providers = "http://kijf4m2pqd54tbck.onion/";
            }
        }

        providerArray = StringUtils.deleteWhitespace(providers).split(",");
        
        int index = new Random().nextInt(providerArray.length);
        baseUrl = providerArray[index];
        log.info("baseUrl for PriceFeedService: " + baseUrl);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public boolean hasMoreProviders() {
        return providerArray.length > 1;
    }

    public void setNewRandomBaseUrl() {
        String newBaseUrl;
        do {
            int index = new Random().nextInt(providerArray.length);
            newBaseUrl = providerArray[index];
        }
        while (baseUrl.equals(newBaseUrl));
        baseUrl = newBaseUrl;
        log.info("Try new baseUrl after error: " + baseUrl);
    }
}