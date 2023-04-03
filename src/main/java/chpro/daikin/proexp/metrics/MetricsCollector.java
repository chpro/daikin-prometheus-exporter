
package chpro.daikin.proexp.metrics;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chpro.daikin.api.client.ClientService;
import chpro.daikin.api.client.data.RawData;
import chpro.daikin.api.client.data.RawData.Field;
import chpro.daikin.api.client.impl.DefaultClientService;
import chpro.daikin.proexp.metrics.config.MetricConfig;
import chpro.daikin.proexp.metrics.config.MetricConfig.Metric;
import io.micronaut.context.annotation.Value;
import io.prometheus.client.Collector;
import io.prometheus.client.Collector.MetricFamilySamples.Sample;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class MetricsCollector extends Collector {

    private static final String LABEL_NAME_FOR_DEVICE = "device";

    private static final Logger LOG = LoggerFactory.getLogger(MetricsCollector.class);

    @Value("${daikin.prometheus-exporter.hosts}")
    protected List<String> hosts;

    @Value("${daikin.prometheus-exporter.label-device-name}")
    protected boolean labelWithDeviceName;

    @Inject
    protected MetricConfig metrics;

    @Inject
    ClientService clientService;

    @Override
    public List<MetricFamilySamples> collect() {
        LOG.info(String.format("Starting to collect %d metrics", metrics.getMetrics().size()));

        List<MetricFamilySamples> samples = new ArrayList<>();

        for (String host : hosts) {

            Map<String, RawData> data = getData(host);

            samples.addAll(metrics.getMetrics().stream().map(metric -> createFamilySample(metric, data)).filter(x -> x != null).collect(Collectors.toList()));
        }
        return samples;
    }

    private Map<String, RawData> getData(String host) {

        Map<String, RawData> data = new HashMap<>();
        try {
            InetAddress inetAddress = InetAddress.getByName(host);
            data.put(DefaultClientService.URI_COMMON_BASIC_INFO, clientService.getBasicInfo(inetAddress));
            data.put(DefaultClientService.URI_AIRCON_GET_SENSOR_INFO, clientService.getSensorInfo(inetAddress));
            data.put(DefaultClientService.URI_AIRCON_GET_CONTROL_INFO, clientService.getControlInfo(inetAddress));
            data.put(DefaultClientService.URI_AIRCON_GET_MONITORDATA, clientService.getMonitorData(inetAddress));
            data.put(DefaultClientService.URI_AIRCON_GET_WEEK_POWER, clientService.getWeekPower(inetAddress));
        } catch (UnknownHostException e) {
            LOG.error("Was not able to get data", e);
        }
        return data;
    }

    protected MetricFamilySamples createFamilySample(Metric metricConfig, Map<String, RawData> data) {
        // copy metric config because we may change labels
        metricConfig = new Metric(metricConfig);
        String help = metricConfig.getHelp();
        String name = metricConfig.getName();
        String unit = metricConfig.getUnit();
        List<String> labelNames = metricConfig.getLabelNames();
        List<String> labelValues = metricConfig.getLabelValues();

        if (labelWithDeviceName) {
            labelNames.add(LABEL_NAME_FOR_DEVICE);
            labelValues.add(getDeviceName(data));
        }

        LOG.debug("Colllecting metric {}", metricConfig);
        try {
            Collector.Type type = Collector.Type.valueOf(metricConfig.getType().toUpperCase());

            double value = getValue(metricConfig, data);

            switch (type) {
            case GAUGE:
            case COUNTER: {
                List<Sample> samples = Collections.singletonList(new Sample(name, labelNames, labelValues, value));
                MetricFamilySamples metric = new MetricFamilySamples(name, unit, type, help, samples);
                return metric;
            }
            case GAUGE_HISTOGRAM:
            case HISTOGRAM:
            case SUMMARY:
            case STATE_SET:
            case INFO:
            case UNKNOWN:
            default:
                throw new RuntimeException("Type not implemented yet " + type);

            }
        } catch (Exception e) {
            LOG.error("Could not create metric " + metricConfig.toString(), e);
            return null;
        }
    }

    protected String getDeviceName(Map<String, RawData> data) {
        return data.get(DefaultClientService.URI_COMMON_BASIC_INFO).getDecodedValue(Field.NAME);
    }

    protected double getValue(Metric metricConfig, Map<String, RawData> data) {
        RawData rawData = data.get(metricConfig.getUri());
        Field field = Field.getById(metricConfig.getVar());
        String value = rawData.getRawValue(field);
        switch (value) {
        case "A":
            return 1.0d;
        case "B":
            return 2.0d;
        default:
            return rawData.getNumberValue(field).doubleValue();
        }

    }

    @Override
    @PostConstruct
    public <T extends Collector> T register() {
        LOG.info("Registering metrics collector " + this.getClass().getName());
        return super.register();
    }

}
