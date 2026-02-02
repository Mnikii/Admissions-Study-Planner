package com.eduplan.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "app")
data class AppProperties(
    var restrictedNames: List<String> = emptyList(),
    var config: ConfigProperties = ConfigProperties(),
    var demo: DemoProperties = DemoProperties()
) {
    data class ConfigProperties(
        var featureFlags: FeatureFlags = FeatureFlags()
    ) {
        data class FeatureFlags(
            var enableValidation: Boolean = false,
            var enableLogging: Boolean = false,
            var incrementViews: Boolean = false,
            var maxResults: Int = 0
        )
    }

    data class DemoProperties(
        var enableValidation: Boolean = false,
        var enableLogging: Boolean = false,
        var incrementViews: Boolean = false,
        var showOnlyPublished: Boolean = false,
        var maxResults: Int = 0
    )
}