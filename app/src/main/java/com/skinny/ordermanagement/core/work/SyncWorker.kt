package com.skinny.ordermanagement.core.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.skinny.ordermanagement.core.notifications.NotificationHelper
import com.skinny.ordermanagement.features.admin.domain.usecases.GetDashboardUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val getDashboardUseCase: GetDashboardUseCase,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Log.d("SyncWorker", "Iniciando sincronización en segundo plano")

            getDashboardUseCase().onSuccess { dashboard ->
                if (dashboard.pendingOrders > 0) {
                    notificationHelper.showNotification(
                        title = "Pedidos Pendientes",
                        body = "Hay ${dashboard.pendingOrders} pedidos pendientes de procesar."
                    )
                }

                if (dashboard.onWayOrders > 0) {
                    notificationHelper.showNotification(
                        title = "Pedidos en Camino",
                        body = "Hay ${dashboard.onWayOrders} pedidos en camino."
                    )
                }

                Log.d("SyncWorker", "Sincronización completada exitosamente")
            }.onFailure { error ->
                Log.e("SyncWorker", "Error en sincronización: ${error.message}")
                return@withContext Result.retry()
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("SyncWorker", "Excepción en SyncWorker: ${e.message}")
            Result.failure()
        }
    }

    companion object {
        fun createWorkRequest(): PeriodicWorkRequest {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

            return PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInitialDelay(5, TimeUnit.MINUTES)
                .build()
        }
    }
}
