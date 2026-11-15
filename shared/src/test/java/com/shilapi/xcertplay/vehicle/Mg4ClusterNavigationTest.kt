package com.shilapi.xcertplay.vehicle

import android.os.Binder
import android.os.Parcel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class Mg4ClusterNavigationTest {
    private val leftHand = 1
    private val rightHand = 0

    @Test
    fun turnsMapToTheClusterArrows() {
        assertEquals(1, Mg4ClusterNavigation.icon(1, leftHand))
        assertEquals(2, Mg4ClusterNavigation.icon(2, leftHand))
        assertEquals(3, Mg4ClusterNavigation.icon(49, leftHand))
        assertEquals(4, Mg4ClusterNavigation.icon(50, leftHand))
        assertEquals(8, Mg4ClusterNavigation.icon(47, leftHand))
        assertEquals(7, Mg4ClusterNavigation.icon(48, leftHand))
        assertEquals(0, Mg4ClusterNavigation.icon(3, leftHand))
        assertEquals(20, Mg4ClusterNavigation.icon(12, leftHand))
    }

    @Test
    fun uTurnsAndRoundaboutsFollowTheDrivingSide() {
        assertEquals(6, Mg4ClusterNavigation.icon(4, leftHand))
        assertEquals(5, Mg4ClusterNavigation.icon(4, rightHand))
        // Roundabout exits 1, 2, 3 and beyond (Apple types 28, 29, 30, 31).
        assertEquals(10, Mg4ClusterNavigation.icon(28, leftHand))
        assertEquals(9, Mg4ClusterNavigation.icon(29, leftHand))
        assertEquals(11, Mg4ClusterNavigation.icon(30, leftHand))
        assertEquals(12, Mg4ClusterNavigation.icon(31, leftHand))
        assertEquals(11, Mg4ClusterNavigation.icon(28, rightHand))
    }

    @Test
    fun motorwayExitsUseTheExitArrows() {
        assertEquals(36, Mg4ClusterNavigation.icon(22, leftHand))
        assertEquals(37, Mg4ClusterNavigation.icon(23, leftHand))
        assertEquals(36, Mg4ClusterNavigation.icon(8, leftHand))
        assertEquals(17, Mg4ClusterNavigation.icon(13, leftHand))
        assertEquals(18, Mg4ClusterNavigation.icon(14, leftHand))
    }

    @Test
    fun unhandledBinderTransactionIsNotReportedAsSuccess() {
        assertFalse(Mg4ClusterNavigation.call(Binder(), 30) { writeInt(1) })
    }

    @Test
    fun acceptedBinderTransactionSendsTheExpectedData() {
        val service = object : Binder() {
            override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
                assertEquals(30, code)
                data.enforceInterface("com.saicmotor.adapterservice.IMapService")
                assertEquals(1, data.readInt())
                reply!!.writeNoException()
                return true
            }
        }
        assertTrue(Mg4ClusterNavigation.call(service, 30) { writeInt(1) })
    }
}
