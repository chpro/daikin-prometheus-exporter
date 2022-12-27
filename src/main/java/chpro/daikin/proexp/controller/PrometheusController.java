package chpro.daikin.proexp.controller;

import java.io.IOException;
import java.io.StringWriter;

import chpro.daikin.proexp.metrics.MetricsCollector;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import jakarta.inject.Inject;

@Controller("/metrics") 
public class PrometheusController {
    
    // Injected here to execut register@postconstruct
    @Inject
    MetricsCollector collector;

    CollectorRegistry registry = CollectorRegistry.defaultRegistry;
    @Get(produces = MediaType.TEXT_PLAIN) 
    public String index(@Header("Accept") String acceptHeader) throws IOException {
        String contentType = TextFormat.chooseContentType(acceptHeader);
        StringWriter writer = new StringWriter();
        TextFormat.writeFormat(contentType, writer, registry.metricFamilySamples());
        return writer.toString();
    }
}