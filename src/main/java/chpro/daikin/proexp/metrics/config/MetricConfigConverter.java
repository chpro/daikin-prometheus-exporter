
package chpro.daikin.proexp.metrics.config;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import chpro.daikin.proexp.metrics.config.MetricConfig.Metric;
import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;
import jakarta.inject.Singleton;

@Singleton
@SuppressWarnings({ "unchecked", "rawtypes" })
public class MetricConfigConverter implements TypeConverter<Map, Metric> {

    @Override
    public Optional<Metric> convert(Map object, Class<Metric> targetType, ConversionContext context) {
        Metric metric = new Metric();
        metric.setUri(object.getOrDefault("uri", "").toString());
        metric.setType(object.getOrDefault("type", "").toString());
        metric.setHelp(object.getOrDefault("help", "").toString());
        metric.setName(object.getOrDefault("name", "").toString());
        metric.setUnit(object.getOrDefault("unit", "").toString());
        metric.setVar(object.getOrDefault("var", "").toString());
        metric.setLabelNames((List<String>) object.getOrDefault("label-names", Collections.emptyList()));
        metric.setLabelValues((List<String>) object.getOrDefault("label-values", Collections.emptyList()));

        return Optional.of(metric);
    }

}
