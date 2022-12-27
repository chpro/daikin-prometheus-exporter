# Daikin prometheus exporter

The prometheus exporter uses chpro.daikin.api to connect to daikin aircondition devices and collect data

## URL

By default the sever is listening to port `8080` the prometheus endpoint is mapped to `metrics`

`http://localhost:8080/metrics`

## Configuration

Configuration is done in application.yml

```
...
daikin:
  prometheus-exporter:
    hosts:
        - ip_address_device_1
        - ip_address_device_2
    label-device-name: true # defines if the label of device name should be added to each metric
    metrics:
        -
          uri: aircon/get_sensor_info
          var: variable_name
          type: counter | gauge
          name: name of the metric (if unit is given the name needs also to be suffixed with it)
          help: a detailed description for the metric
          label-names:
            - name1
            - name2
          label-values:
            - value_for_name1
            - value_for_name2
          unit: iso units (grams, clesius ..)
...
```

### uri

can be one of
- common/basic_info
- aircon/get_control_info
- aircon/get_sensor_info
- aircon/get_week_power
- aircon/get_monitordata

### var

The name of the field returned by the endpoint which should be exposed via /metrics endpoint

### label-*

Static labels which should be added to the metric. Lists need to have same length

## Micronaut 3.7.4 Documentation

- [User Guide](https://docs.micronaut.io/3.7.4/guide/index.html)
- [API Reference](https://docs.micronaut.io/3.7.4/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/3.7.4/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

- [Shadow Gradle Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)
