package com.shilapi.xcertplay

import android.content.Context
import org.robolectric.RuntimeEnvironment
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class Mg4ScreenAndCallSettingsTest {
    private val context: Context = RuntimeEnvironment.getApplication()

    @Test fun screenSizeIsAutomaticByDefault() {
        assertFalse(AirPlayPersistence.loadStockScreenSize(context))
    }

    @Test fun carMfiChipIsOffByDefault() {
        assertFalse(AirPlayPersistence.loadUseCarMfiChip(context))
    }

    @Test fun narrowBandCallsAreOffByDefault() {
        assertFalse(AirPlayPersistence.loadNarrowBandCalls(context))
    }

    @Test fun swi68CanReadTheFormerSwi165Settings() {
        val field = Mg4FirmwareProfile::class.java.getDeclaredField("detected").apply { isAccessible = true }
        val original = field.get(null)
        try {
            field.set(null, Mg4FirmwareProfile.forFirmware("SWI68-29958-1300R69"))
            AirPlayPersistence.saveShowCallsToCar(context, true)
            AirPlayPersistence.saveTagMusicAsCarPlay(context, true)
            AirPlayPersistence.saveCarPhoneMode(context, true)
            AirPlayPersistence.saveNarrowBandCalls(context, true)
            AirPlayPersistence.saveStockScreenSize(context, true)
            AirPlayPersistence.saveUseCarMfiChip(context, true)
            assertTrue(AirPlayPersistence.loadShowCallsToCar(context))
            assertTrue(AirPlayPersistence.loadTagMusicAsCarPlay(context))
            assertTrue(AirPlayPersistence.loadCarPhoneMode(context))
            assertTrue(AirPlayPersistence.loadNarrowBandCalls(context))
            assertTrue(AirPlayPersistence.loadStockScreenSize(context))
            assertTrue(AirPlayPersistence.loadUseCarMfiChip(context))

            field.set(null, Mg4FirmwareProfile.forFirmware("OTHER-1"))
            assertFalse(AirPlayPersistence.loadShowCallsToCar(context))
            assertFalse(AirPlayPersistence.loadStockScreenSize(context))
        } finally {
            field.set(null, original)
        }
    }
}
