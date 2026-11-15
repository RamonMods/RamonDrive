package com.ramon.ramondrive.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

/**
 * Drawn black on white whatever the theme: scanners expect dark on light and many reject an
 * inverted code.
 *
 * The modules are painted straight onto the canvas at whole-pixel sizes instead of scaling a
 * bitmap, so the code stays sharp and even at any window size.
 */
@Composable
fun QrCode(content: String, contentDescription: String?, modifier: Modifier = Modifier) {
    val matrix: BitMatrix? = remember(content) { encodeQr(content) }
    if (matrix == null) return
    val label = contentDescription

    Canvas(
        modifier = modifier
            .aspectRatio(1f)
            .then(
                if (label == null) {
                    Modifier
                } else {
                    Modifier.semantics { this.contentDescription = label }
                }
            )
    ) {
        drawRect(Color.White)
        // Whole pixels per module keep every square the same size and the edges crisp.
        val module = (size.minDimension / matrix.width).toInt().coerceAtLeast(1).toFloat()
        val side = module * matrix.width
        val left = (size.width - side) / 2f
        val top = (size.height - side) / 2f
        for (y in 0 until matrix.height) {
            for (x in 0 until matrix.width) {
                if (matrix.get(x, y)) {
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(left + x * module, top + y * module),
                        size = Size(module, module)
                    )
                }
            }
        }
    }
}

private fun encodeQr(content: String): BitMatrix? = runCatching {
    val hints = mapOf(
        EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M,
        EncodeHintType.MARGIN to QR_MARGIN
    )
    // Zero asks ZXing for the smallest exact matrix, which is then painted at the display size.
    QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, 0, 0, hints)
}.getOrNull()

private const val QR_MARGIN = 2
