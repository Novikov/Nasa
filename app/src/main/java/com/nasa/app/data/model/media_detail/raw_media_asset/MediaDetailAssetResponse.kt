package com.nasa.app.data.model.media_detail.raw_media_asset

import com.nasa.app.data.model.ContentType

class MediaDetailAssetResponse (val assetType:ContentType, val assets:Map<String,String>)