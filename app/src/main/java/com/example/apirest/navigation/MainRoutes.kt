
import kotlinx.serialization.Serializable

sealed class MainRoutes {

    @Serializable
    data object Pokemon : MainRoutes(){
    }
}

