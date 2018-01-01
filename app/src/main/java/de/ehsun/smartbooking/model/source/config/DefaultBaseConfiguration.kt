package de.ehsun.smartbooking.model.source.config

import de.ehsun.smartbooking.BuildConfig

class DefaultBaseConfiguration : BaseConfiguration {

    override fun getServerBaseUrl(): String {
        return BuildConfig.BASE_URL
    }
}