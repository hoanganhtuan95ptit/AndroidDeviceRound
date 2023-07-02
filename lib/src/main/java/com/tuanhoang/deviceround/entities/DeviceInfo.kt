package com.tuanhoang.deviceround.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class DeviceInfo(var name: String = "", var timeUpdate: Long = -1, var radius: List<Int> = emptyList())
