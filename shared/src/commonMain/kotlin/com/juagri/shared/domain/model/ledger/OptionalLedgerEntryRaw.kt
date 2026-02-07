package com.juagri.shared.domain.model.ledger

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Wrapper so we can decode Firestore map values that may be either
 * an object (CustomerLedgerEntryRaw) or a primitive (e.g. Double).
 * When the value is a number we store it in [doubleValue] so root-level
 * or entry "0" opening/closing balance is not lost.
 */
@kotlinx.serialization.Serializable(with = OptionalLedgerEntryRawSerializer::class)
data class OptionalLedgerEntryRaw(
    val entry: CustomerLedgerEntryRaw? = null,
    val doubleValue: Double? = null
)

object OptionalLedgerEntryRawSerializer : KSerializer<OptionalLedgerEntryRaw> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("OptionalLedgerEntryRaw", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): OptionalLedgerEntryRaw {
        return try {
            OptionalLedgerEntryRaw(entry = decoder.decodeSerializableValue(CustomerLedgerEntryRaw.serializer()))
        } catch (_: Throwable) {
            // Value was not a map (e.g. Double). Consume and store so opening/closing balance can be used.
            val num = try {
                decoder.decodeDouble()
            } catch (_: Throwable) {
                try {
                    decoder.decodeLong().toDouble()
                } catch (_: Throwable) {
                    try {
                        decoder.decodeInt().toDouble()
                    } catch (_: Throwable) {
                        try {
                            decoder.decodeString()
                            null
                        } catch (_: Throwable) {
                            try {
                                decoder.decodeBoolean()
                                null
                            } catch (_: Throwable) {
                                null
                            }
                        }
                    }
                }
            }
            OptionalLedgerEntryRaw(entry = null, doubleValue = num)
        }
    }

    override fun serialize(encoder: Encoder, value: OptionalLedgerEntryRaw) {
        value.entry?.let { encoder.encodeSerializableValue(CustomerLedgerEntryRaw.serializer(), it) }
    }
}
