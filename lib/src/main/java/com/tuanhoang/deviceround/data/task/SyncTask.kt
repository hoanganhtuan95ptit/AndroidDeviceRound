package com.tuanhoang.deviceround.data.task

import com.one.task.Task

interface SyncTask : Task<SyncParam, Unit>

data class SyncParam(val deviceModel: String)