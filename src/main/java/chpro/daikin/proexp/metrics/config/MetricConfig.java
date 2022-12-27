
package chpro.daikin.proexp.metrics.config;

import java.util.ArrayList;
import java.util.List;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.util.CollectionUtils;

@ConfigurationProperties("daikin.prometheus-exporter")
public interface MetricConfig {

    public List<Metric> getMetrics();

    public class Metric {

        protected String uri;

        protected String type;

        protected String help;

        protected String name;

        protected String unit;

        protected String var;

        protected List<String> labelNames;

        protected List<String> labelValues;

        public Metric() {
            super();
        }

        public Metric(String uri, String type, String help, String name, String unit, String var, List<String> labelNames,
                List<String> labelValues) {
            super();
            this.uri = uri;
            this.type = type;
            this.help = help;
            this.name = name;
            this.unit = unit;
            this.var = var;
            this.labelNames = labelNames;
            this.labelValues = labelValues;
        }

        public Metric(Metric metric) {
            this(metric.getUri(), metric.getType(), metric.getHelp(), metric.getName(), metric.getUnit(), metric.getVar(),
                    new ArrayList<String>(metric.getLabelNames()), new ArrayList<String>(metric.getLabelValues()));
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getHelp() {
            return help;
        }

        public void setHelp(String help) {
            this.help = help;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public List<String> getLabelNames() {
            return labelNames;
        }

        public void setLabelNames(List<String> labelNames) {
            this.labelNames = labelNames;
        }

        public List<String> getLabelValues() {
            return labelValues;
        }

        public void setLabelValues(List<String> labelValues) {
            this.labelValues = labelValues;
        }

        public String getVar() {
            return var;
        }

        public void setVar(String var) {
            this.var = var;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Metric [getUri()=");
            builder.append(getUri());
            builder.append(", getType()=");
            builder.append(getType());
            builder.append(", getHelp()=");
            builder.append(getHelp());
            builder.append(", getName()=");
            builder.append(getName());
            builder.append(", getLabelNames()=");
            builder.append(CollectionUtils.toString(getLabelNames()));
            builder.append(", getLabelValues()=");
            builder.append(CollectionUtils.toString(getLabelValues()));
            builder.append(", getUnit()=");
            builder.append(getUnit());
            builder.append(", getVar()=");
            builder.append(getVar());
            builder.append("]");
            return builder.toString();
        }
    }
}
