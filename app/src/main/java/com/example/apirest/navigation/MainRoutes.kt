
import kotlinx.serialization.Serializable

sealed class MainRoutes {

    @Serializable
    data object Pokemon : MainRoutes()

    @Serializable
    data class Detail(val name: String) : MainRoutes()
}

