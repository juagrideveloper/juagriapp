package com.juagri.shared.domain.model.employee

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

private object SessionStateSerializer : KSerializer<JUEmployee> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "JUEmployee",
        kind = PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): JUEmployee {
        return Json.decodeFromString(decoder.decodeString())
    }

    override fun serialize(
        encoder: Encoder,
        value: JUEmployee
    ) {
        //encoder.encodeString(Json.encodeToString(value))
    }
}

private object CustomSerializer : KSerializer<JUEmployee> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "JUEmployee",
        kind = PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): JUEmployee {
        return Json.decodeFromString(decoder.decodeString())
    }

    override fun serialize(
        encoder: Encoder,
        value: JUEmployee
    ) {
        //encoder.encodeString(Json.encodeToString(value))
    }
}


@Serializable//(with = CustomSerializer::class)
data class JUEmployee(
    @SerialName("ccode") var ccode:String? = null,
    @SerialName("cdoj") var cdoj:String? = null,
    @SerialName("cemailid") var cemailid:String? = null,
    @SerialName("cflno") var cflno:String? = null,
    @SerialName("cgst") var cgst:String? = null,
    @SerialName("cname") var cname:String? = null,
    @SerialName("cpan") var cpan:String? = null,
    @SerialName("cplno") var cplno:String? = null,
    @SerialName("cphoneno") var cphoneno:String? = null,
    @SerialName("cprintname") var cprintname:String? = null,
    @SerialName("creditlimit") var creditlimit:String? = null,
    @SerialName("regcode") var regcode:String? = null,
    @SerialName("socode") var socode:String? = null,
    @SerialName("tcode") var tcode:String? = null,
    @SerialName("caddress1") var caddress1:String? = null,
    @SerialName("caddress2") var caddress2:String? = null,
    @SerialName("cvillage") var cvillage:String? = null,
    @SerialName("ccity") var ccity:String? = null,
    @SerialName("cstcode") var cstcode:String? = null,
    @SerialName("cstname") var cstname:String? = null,
    @SerialName("cpincode") var cpincode:String? = null,
    @SerialName("today") var today:String? = null,
    @SerialName("l90") var l90:Double = 0.0,
    @SerialName("l120") var l120:Double = 0.0,
    @SerialName("l180") var l180:Double = 0.0,
    @SerialName("g180") var g180:Double = 0.0,
    @SerialName("totalos") var totalos:Double = 0.0,
    @SerialName("isKyccompleted") var isKyccompleted:Boolean = false,
    @SerialName("isKycapproved") var isKycapproved:Boolean = false)