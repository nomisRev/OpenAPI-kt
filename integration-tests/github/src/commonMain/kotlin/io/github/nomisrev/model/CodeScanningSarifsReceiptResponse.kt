package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class CodeScanningSarifsReceiptResponse(val id: CodeScanningAnalysisSarifId? = null, val url: String? = null)
