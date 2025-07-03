import android.app.Application
import android.widget.Toast
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gouni_mobile_application.domain.model.Route
import com.example.gouni_mobile_application.domain.repository.RouteRepository
import kotlinx.coroutines.launch

class RouteViewModel(
    application: Application,
    private val routeRepository: RouteRepository
) : AndroidViewModel(application) {
    fun createRoute(route: Route) {
        viewModelScope.launch {
            Log.d("Route", "ViewModel createRoute called")
            val result = routeRepository.createRoute(route)
            Log.d("Route", "Result: $result")
            result.onSuccess { id ->
                Log.d("Route", "Route created with id: $id")
                Toast.makeText(getApplication(), "Ruta creada con éxito", Toast.LENGTH_SHORT).show()
                // TODO: Navigate or update UI as needed
            }.onFailure { error ->
                Log.e("Route", "Failed to create route: ${error.message}")
                Toast.makeText(getApplication(), "Error al crear la ruta: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
} 