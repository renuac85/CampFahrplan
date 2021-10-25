package nerd.tuxmobil.fahrplan.congress.alarms

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import info.metadude.android.eventfahrplan.commons.testing.verifyInvokedNever
import info.metadude.android.eventfahrplan.commons.testing.verifyInvokedOnce
import nerd.tuxmobil.fahrplan.congress.NoLogging
import nerd.tuxmobil.fahrplan.congress.alarms.AlarmUpdater.OnAlarmUpdateListener
import nerd.tuxmobil.fahrplan.congress.preferences.SharedPreferencesRepository
import nerd.tuxmobil.fahrplan.congress.repositories.AppRepository
import nerd.tuxmobil.fahrplan.congress.utils.ConferenceTimeFrame
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify

class AlarmUpdaterTest {

    private companion object {

        // 2015-12-27T00:00:00+0100, in seconds: 1451170800000
        const val FIRST_DAY_START_TIME = 1451170800000L

        // 2015-12-31T00:00:00+0100, in seconds: 1451516400000
        const val LAST_DAY_END_TIME = 1451516400000L

        const val NEVER_USED: Long = -1

        val conferenceTimeFrame = ConferenceTimeFrame(FIRST_DAY_START_TIME, LAST_DAY_END_TIME)
    }

    private val sharedPreferencesRepository = mock<SharedPreferencesRepository>()

    private val testableAppRepository: AppRepository
        get() = with(AppRepository) {
            initialize(
                context = mock(),
                logging = mock(),
                networkScope = mock(),
                alarmsDatabaseRepository = mock(),
                highlightsDatabaseRepository = mock(),
                sessionsDatabaseRepository = mock(),
                metaDatabaseRepository = mock(),
                scheduleNetworkRepository = mock(),
                engelsystemNetworkRepository = mock(),
                sharedPreferencesRepository = sharedPreferencesRepository
            )
            return this
        }

    private lateinit var alarmUpdater: AlarmUpdater
    private val mockListener = mock<OnAlarmUpdateListener>()

    @Before
    fun setUp() {
        alarmUpdater = AlarmUpdater(conferenceTimeFrame, mockListener, testableAppRepository, NoLogging)
    }

    @Test
    fun `calculateInterval schedules alarm with development interval`() {
        whenever(sharedPreferencesRepository.getScheduleRefreshInterval()).doReturn(3000)
        val interval = alarmUpdater.calculateInterval(LAST_DAY_END_TIME, false)
        assertThat(interval).isEqualTo(3000L)
        verifyInvokedOnce(mockListener).onCancelAlarm()
        verifyInvokedNever(mockListener).onRescheduleInitialAlarm(NEVER_USED, NEVER_USED)
        verifyInvokedOnce(mockListener).onRescheduleAlarm(3000L, LAST_DAY_END_TIME + 3000L)
    }

    @Test
    fun `calculateInterval schedules initial alarm with development interval`() {
        whenever(sharedPreferencesRepository.getScheduleRefreshInterval()).doReturn(3000)
        val interval = alarmUpdater.calculateInterval(LAST_DAY_END_TIME, true)
        assertThat(interval).isEqualTo(3000L)
        verifyInvokedOnce(mockListener).onCancelAlarm()
        verifyInvokedOnce(mockListener).onRescheduleInitialAlarm(3000L, LAST_DAY_END_TIME + 3000L)
        verifyInvokedNever(mockListener).onRescheduleAlarm(NEVER_USED, NEVER_USED)
    }

    // Start <= Time < End

    @Test
    fun `calculateInterval with time of first day`() {
        // 2015-12-27T11:30:00+0100, in seconds: 1451212200000
        val interval = alarmUpdater.calculateInterval(1451212200000L, false)
        assertThat(interval).isEqualTo(7200000L)
        verifyInvokedNever(mockListener).onCancelAlarm()
        verifyInvokedNever(mockListener).onRescheduleAlarm(NEVER_USED, NEVER_USED)
        verifyInvokedNever(mockListener).onRescheduleInitialAlarm(NEVER_USED, NEVER_USED)
    }

    @Test
    fun `calculateInterval with time of first day initial`() {
        // 2015-12-27T11:30:00+0100, in seconds: 1451212200000
        val interval = alarmUpdater.calculateInterval(1451212200000L, true)
        assertThat(interval).isEqualTo(7200000L)
        verify(mockListener).onCancelAlarm()
        verify(mockListener).onRescheduleInitialAlarm(7200000L, 1451212200000L + 7200000L)
        verifyInvokedNever(mockListener).onRescheduleAlarm(NEVER_USED, NEVER_USED)
    }

    // Time == End

    @Test
    fun `calculateInterval with time of last day end time`() {
        // 2015-12-31T00:00:00+0100, in seconds: 1451516400000
        val interval = alarmUpdater.calculateInterval(1451516400000L, false)
        assertThat(interval).isEqualTo(0)
        verify(mockListener).onCancelAlarm()
        verifyInvokedNever(mockListener).onRescheduleAlarm(NEVER_USED, NEVER_USED)
        verifyInvokedNever(mockListener).onRescheduleInitialAlarm(NEVER_USED, NEVER_USED)
    }

    @Test
    fun `calculateInterval with time of last day end time initial`() {
        // 2015-12-31T00:00:00+0100, in seconds: 1451516400000
        val interval = alarmUpdater.calculateInterval(1451516400000L, true)
        assertThat(interval).isEqualTo(0)
        verify(mockListener).onCancelAlarm()
        verifyInvokedNever(mockListener).onRescheduleAlarm(NEVER_USED, NEVER_USED)
        verifyInvokedNever(mockListener).onRescheduleInitialAlarm(NEVER_USED, NEVER_USED)
    }

    // Time < Start, diff == 1 second

    @Test
    fun `calculateInterval with time one second before first day`() {
        // 2015-12-26T23:59:59+0100, in seconds: 1451170799000
        val interval = alarmUpdater.calculateInterval(1451170799000L, false)
        assertThat(interval).isEqualTo(7200000L)
        verify(mockListener).onCancelAlarm()
        verify(mockListener).onRescheduleAlarm(7200000L, 1451170800000L)
        verifyInvokedNever(mockListener).onRescheduleInitialAlarm(NEVER_USED, NEVER_USED)
    }

    @Test
    fun `calculateInterval with time one second before first day initial`() {
        // 2015-12-26T23:59:59+0100, in seconds: 1451170799000
        val interval = alarmUpdater.calculateInterval(1451170799000L, true)
        assertThat(interval).isEqualTo(7200000L)
        verify(mockListener).onCancelAlarm()
        verify(mockListener).onRescheduleInitialAlarm(7200000L, 1451170800000L)
        verifyInvokedNever(mockListener).onRescheduleAlarm(NEVER_USED, NEVER_USED)
    }

    // Time < Start, diff == 1 day

    @Test
    fun `calculateInterval with time one day before first day`() {
        // 2015-12-26T00:00:00+0100, in seconds: 1451084400000
        val interval = alarmUpdater.calculateInterval(1451084400000L, false)
        assertThat(interval).isEqualTo(7200000L)
        verify(mockListener).onCancelAlarm()
        verify(mockListener).onRescheduleAlarm(7200000L, 1451170800000L)
        verifyInvokedNever(mockListener).onRescheduleInitialAlarm(NEVER_USED, NEVER_USED)
    }

    @Test
    fun `calculateInterval with time one day before first day initial`() {
        // 2015-12-26T00:00:00+0100, in seconds: 1451084400000
        val interval = alarmUpdater.calculateInterval(1451084400000L, true)
        assertThat(interval).isEqualTo(7200000L)
        verify(mockListener).onCancelAlarm()
        verify(mockListener).onRescheduleInitialAlarm(7200000L, 1451170800000L)
        verifyInvokedNever(mockListener).onRescheduleAlarm(NEVER_USED, NEVER_USED)
    }

    // Time < Start, diff > 1 day

    @Test
    fun `calculateInterval with time more than one day before first day`() {
        // 2015-12-25T23:59:59+0100, in seconds: 1451084399000
        val interval = alarmUpdater.calculateInterval(1451084399000L, false)
        assertThat(interval).isEqualTo(86400000L)
        // TODO Is this behavior intended?
        verifyInvokedNever(mockListener).onCancelAlarm()
        verifyInvokedNever(mockListener).onRescheduleAlarm(NEVER_USED, NEVER_USED)
        verifyInvokedNever(mockListener).onRescheduleInitialAlarm(NEVER_USED, NEVER_USED)
    }

    @Test
    fun `calculateInterval with time more than one day before first day initial`() {
        // 2015-12-25T23:59:59+0100, in seconds: 1451084399000
        val interval = alarmUpdater.calculateInterval(1451084399000L, true)
        assertThat(interval).isEqualTo(86400000L)
        verify(mockListener).onCancelAlarm()
        verify(mockListener).onRescheduleInitialAlarm(86400000L, 1451084399000L + 86400000L)
        verifyInvokedNever(mockListener).onRescheduleAlarm(NEVER_USED, NEVER_USED)
    }

}
